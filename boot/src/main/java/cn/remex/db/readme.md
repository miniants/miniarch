Remex2 db模块
============


一、依赖
------------
* javax.peresist
    * @Table
    * @Column
    * @OneToMany
    * @ManyToMany
* spring
* cn.remrx.core & cn.remex.db

二、配置及初始化
-------------
* 配置:application-context.xml
```
    <bean id="default_space" class="cn.remex.db.rsql.connection.RDBSpaceConfig">
        <property name="space" value="default"/>
        <property name="type" value="mysql"/>
        <property name="dialectClass"
                  value="cn.remex.db.rsql.connection.dialect.MysqlDialect"/>
        <property name="url" value="jdbc:mysql://101.200.75.191:3306/KLB?characterEncoding=utf-8"/>
        <!--
                <property name="rv" 		value="jdbc:oracle:thin:@192.168.1.123:1521:orcl"></property>
                 <property name="rv" 		value="jdbc:oracle:thin:@localhost:1521:orcl"></property>  -->
        <property name="username" value="KLB"/>
        <property name="password" value="KLB_PW"/>
        <property name="maxconn" value="10"/>
        <!-- 配置用于建立数据库模型的包，不支持通配符。自动支持子包。建议数据模型的包都建立在一个大包下面。 -->
        <property name="ormBeanPackages">
            <list>
                <value>cn.remex.model</value>
                <value>zhb.aias.model.vehicle</value>
            </list>
        </property>
    </bean>
```
* 启动并初始化数据库:
>RsqlCore.reset(true); //根据上述配置重新初始化数据库

>考虑到效率问题，每次可以只重建特定的模型；//TODO 此功能暂未实现

三、API
--------------
#1. 数据库会话
##1.1 说明:
* 当前线程同一个spaceName共享同一个数据库会话
* 同一个数据会话内数据事务不隔离
* 嵌套事务以最后一个事务为准统一提交

##1.2 使用:
* 通过工厂接口:
		
		ContainerFactory.getSession()
* 通过初始化DbCvo对象自动创建:
		
		ContainerFactory.createDbCvo(Class<? extends Modelable).[chainMethod].ready().[actionMethod];

#2. 模型建立
##2.1 说明:
* 模型
* 模型建立符合jpa规范
* 模型建立需继承 ModelableImpl
* 模型可持久化的属性支持:
> 基本类型：数字,char,string
> Model类型：继承于ModelableImpl的对象
> List类型：List<? extends ModelableImpl>
* 模型关系建立包括:
> 一对一:通过@OneToOne()
> 一对多:一方通过@OneToMany()；多方通过ManyToOne();
> 多对多:通过@ManyToMany(),默认情况List数据与对应的Model建立多对多非级联关系.
* 文档用语说明
	* _当前Model_ 指当前模型，即当前设计的数据表
	* _外键Model_ 指当前模型通过一对一、一对多、多对多关联的模型

##2.2基本属性的建立
//TODO 文档补充，代码走查

##2.3一对一关系建立
* 方法

默认：通过在Model中直接建立Model属性[fieldName]即可
>注意:
如该属性被对应的 外键Model 通过在List<当前Model> [curModels] 属性的
注解OneToMany(mappedBy='fieldName')引用，该属性将被转化为 外键对象OneToMany关系的多方维护方ManyToOne。

* 显式方式建立
    * 必须：T extends Modelable 类型的属性
    * 可选：OneToOne的属性
        cascade: {CascadeType.PERSIST, CascadeType.removed}
        mappedBy: 该属性必须成对匹配出现，在当前Model该属性与外键Model该属性相互引用

##2.4一对多及多对多关系的建立
* 说明：
    * 在本架构内，OneToMany 与 ManyToOne属于是互为对方，
        
        通过这两个注解的属性mappedBy 和 targetEntity来维护关系。
    OneToMany中
        (mappedBy="fieldName")  -->    指定外键Model中的fieldName属性为多方属性
    ManyToOne中
        (mappedBy="fieldName")  -->    指定外键Model中的fieldName属性为一方属性

    如果没有通过属性维护关系，则是分别属于两个一对多/多对一的关系。

    * 当前Model中存在T extends Model属性或List<T extends Modelable>属性时，默认创建了本关系：
        
        T extends Model             -->  ManyToOne
        List<T extends Modelable>   -->  OneToMany

* 一对多关系建立
  通过以下方法建立
    * 必须：List<T extends Modelable>属性类型必须匹配；OneToMany及OneToMany.mappedBy不能缺失
    * 可选：OneToMany的属性cascade={CascadeType.PERSIST, CascadeType.removed}，CascadeType其他属性忽略
    * 忽略：OneToMany的属性：targetEntity、orphanRemoval、fetch
    * 用法：
    mappedBy对应的属性在外键Model中必须存在且类型必须为当前Model的类型。
    如果需要级联保存或删除则需指定cascade={CascadeType.PERSIST}，当一方（当前Model）保存时将自动
    新增或保存外键对象（外键对id为null或-1时新增，id为其他值切datastatus不为managed、saved时保存，框架控制）

* 多对多关系建立
  通过以下方法建立
    * 必须：List<T extends Modelable>属性类型必须匹配；不写注解或注解为ManyToMany //TODO 后面多对多需要显示声明
    * 可选：
    targetEntity 属性指定由哪一方维护多对多关系，当模型中存在双方功能指定同一个多对多关系时只能有一方指定targetEntity
    mappedBy
        属性用于再双方都建立List属性来维护同一个多对多关系是明确对方属性，该属性必须再双方共同成对配置，相互指定。
        不指定时则只有List属性的一方维护此多对多关系
    ManyToMany 属性cascade={CascadeType.PERSIST}，CascadeType其他属性忽略
    * 忽略：OneToMany的属性：orphanRemoval、fetch
    * 用法：
    * 注意：ManyToMany 注解优先级低于OneToMany优先级，强烈建议不用同时配置OneToMany和ManyToMany


3.数据库操作
说明:
*通过链式方式向DbCvo中输入sql参数
*通过方法方式向DbCvo中输入sql参数
*建议通过lamba表达式引用JavaBean属性/数据库表的字段

使用:
3.1链式访问
ContainerFactory.createDbCvo(*)....ready().[oper]();


3.2lamba表达式
ContainerFactory.createSession((DbCvo<T extends Modelable> dbCvo, T t)->{
})

二\数据库操作功能:

0.通用条件DbCvo
  0.1配置表空间
    .useSpace //TODO

  0.2条件约束
  说明：a)所有的约束方法都以filter开头;b)约束目前可以在DbCvo及SqlColumn中使用，方法略有不同
    核心方法：
    .filterOper
    .filterBy
    .filterByGroup //通过Group可以实现任何复杂的SQL WHERE子句。目前不支持子查询约束。

    延展方法：
    .filterById

  0.3列指定
  说明：a)列指定包括明确需要查询出来的数据列，或者明确需要更新的数据列;b)字段包括四种类型：基本列，Model列，List列，非本表模型的其他列
    .withColumns //指定需要查询的基本列，可以一次性指定多个，可用于Model/List列的子属性
    .withModel   //指定需要查询的Model列，一次只能指定一个，只能使用一次，多次使用则会进行多次LeftJoin，可用于Model/List列的子属性
    .withList    //指定需要查询的List列，一次只能指定一个，只能使用一次，多次使用则会进行多次LeftJoin，可用于Model/List列的子属性
    .withOther   //TODO 指定需要查询的本表表的其他表列，一次只能指定一个，可用于Model/List列的子属性

1.查询
  1.1排序
    .OrderBy // TODO 没重构完

  1.1分页
    .setPagination //指定查询的页码 //TODO 目前用的是put方法，将来替换掉
    .setRowCount   //指定查询的行数 //TODO 目前用的是put方法，将来替换掉
    .setRecord     //为了优化分页,如果传入record则可以进行跳过总数查询 //TODO 目前用的是put方法，将来替换掉

基础动作Container
    .query

高级动作
    .queryBy

