javax.peresist
@Table
@Column
@OneToMany

一\数据库开发方式
1.数据库会话的获取
Rsql框架内,当前线程同一个spaceName共享同一个数据库会话,同一个数据会话内数据事务不隔离,嵌套事务以最后一个事务为准统一提交.

2.数据库操作数据的配置
2.1链式访问
ContainerFactory.createDbCvo(*)....ready().[oper]();


2.2lamba表达式
ContainerFactory.createSession((DbCvo<T extends Modelable> dbCvo, T t)->{
})

二\数据库操作功能:

0.通用条件DbCvo
    配置约束
    .putSpaceName

    进行条件约束
    .putWhere
    .putWhereOper
    .putWhereGroup
    .putWhereGroupOper

    进行查询或更新字段的约束的
    .putDataTye
    .putDataColumns //存在表连接在这里自动发现,非数据库的外键关联不允许自动查询

1.查询
基础条件DbCvo
    排序
    .putOrder

    分页
    .putPagination
    .putRowCount
    .putRecord //为了优化分页,如果传入record则可以进行跳过总数查询

基础动作Container
    .query

高级动作
    .queryBy


2.新增

3.更新

4.删除

5.存在判断

6.sql执行

7.call执行
