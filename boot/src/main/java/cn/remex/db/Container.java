package cn.remex.db;

import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlContainer;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.db.sql.WhereRuleOper;

import javax.persistence.ManyToMany;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangaiguo
 * 数据库操作的接口类
 */
public interface Container {
	/**
	 * 以callSql为SQL语句创建存储过程/函数等。
	 * @param callSql
	 * @return RsqlRvo
	 */
	public DbRvo createCall(String callSql);
	/**
	 * 创建一个受控的、代理的数据库Bean
	 * @param beanClass
	 * @return
	 */
	public <T extends Modelable> T createBean(Class<T> beanClass);
	/**
	 * 创建一个数据查询的对象，进行链式访问
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass);

	/**
	 *  从数据中删除对象o。<br>
	 * @rmx.summary 1.根据o的Class去检索BeanClass
	 * 2.根据o.getId()为主键，从数据中删除数据。
	 * 3.根据其List属性中，OneToMany/ManyToMany指定的{@link ManyToMany#cascade()}来级联删除数据.
	 * @param o 删除的对象
	 * @return DbRvo
	 * @rmx.call {@link RsqlContainer#storeFKList(Class, Object, Object, String)}
	 */
	public <T extends Modelable> DbRvo delete(T o);
	
	/**根据传入的对象类型clazz和主键id删除数据
	 * @param clazz
	 * @param idObject
	 * @return DbRvo
	 * @rmx.call {@link RsqlContainer#deleteByIds(Class, String)}
	 */
	public <T extends Modelable> DbRvo deleteById(Class<T> clazz,String idObject);

	/**
	 * 根据ids批量删除数据
	 * @param clazz
	 * @param ids 多条记录的id主键，用","分隔
	 * @return DbRvo
	 */
	public <T extends Modelable> DbRvo deleteByIds(Class<T> clazz, String ids);
	
	/**
	 * 根据传入的sql和参数对数据进行操作<br>
	 *  如果string以"SQL_"开头则从spring的配置文件Rsql_SQL中获取真实的SQL。
	 * @rmx.summary sql 为传入要执行的sql语句，存在where语句等条件时，条件值为 :+字段名称（命名参数），
	 * params为map<命名参数的字段名称,参数值>
	 * @param sql  执行的sql语句，其中的where条件为命名参数形式，:
	 * @param params 主要为where条件中的命名参数和参数值的map
	 * @return DbRvo
	 * @rmx.call {@link RsqlCore#refreshORMConstraints(cn.remex.db.rsql.connection.RDBSpaceConfig)}
	 */
	@RsqlTransaction
	public DbRvo execute(String sql, HashMap<String, Object> params);
	
	/**
	 * 根据传入的sql语句和参数执行数据库查询操作<br>
	 *  如果string以"SQL_"开头则从spring的配置文件Rsql_SQL中获取真实的SQL。
	 *  @param sql  执行的sql语句，其中的where条件为命名参数形式，:
	 *	 @param params 主要为where条件中的命名参数和参数值的map
	 *  @return RsqlRvo
	 *  @rmx.call {@link RsqlCore#refreshORMConstraints(cn.remex.db.rsql.connection.RDBSpaceConfig)}
	 *  @rmx.call {@link RsqlUtils#queryCollectionBeans(Class, String, Object)}
	 */
	public DbRvo executeQuery(String sql, HashMap<String, Object> params);

	/**
	 * 根据传入的sql语句和参数执行数据库更新操作<br>
	 *  如果string以"SQL_"开头则从spring的配置文件Rsql_SQL中获取真实的SQL。
	 *  @param sql 执行的sql语句，其中的where条件为命名参数形式，:
	 *	@param params 主要为where条件中的命名参数和参数值的map
	 * @return RsqlRvo
	 * @rmx.call {@link RsqlCore#AlterAddColumn}
	 * @rmx.call {@link RsqlCore#AlterModifyColumn}
	 * @rmx.call {@link RsqlCore#createBaseTable}
	 * @rmx.call {@link RsqlCore#createCollectionTable}
	 * @rmx.call {@link RsqlUtils#doManyToMany_delete(Class, String, String, Object, Object, boolean)}
	 * @rmx.call {@link RsqlUtils#doManyToMany_insert(Class, String, String, Object, Object, boolean)}
	 */
	@RsqlTransaction
	public DbRvo executeUpdate(String sql, HashMap<String, Object> params);

	/**
	 * 判断dataStatus及通过{@link RsqlContainer#getPK(Modelable)}来检查是否在本数据库中存在该对象。<br>
	 * @rmx.summary 本数据库中存在该对象。
	 * @param obj
	 *            需要检索的对象
	 * @param forceCheckFromDB
	 * 						如果id存在，强制是否去数据库中判断该对象是否持久化
	 * @return boolean 对象在数据中仅唯一存在则返回true<br>
	 *         以下情况将返回false<br>
	 *         <li>obj == null <li>obj 没有getId() 和 setId() <li>obj 的属性id为空 <li>
	 *         obj 的属性id有值，但在数据库中不存在<li>dataStatus为{@link RsqlConstants#DS_beanNew}
	 * @throws Exception
	 *             <li>如果数据库查询有错误<br> <li>或者本对象所对应的类在数据中持久化的表中的数据有重复项将会跑出异常<br>
	 *             如果数据中同样id的数据存在多条则数据完整性及结构存在问题<br>
	 *             默认有{@link RsqlCore#reset(boolean...)} 方法创建的数据库结构是不会出现该问题的，
	 *             一旦出现问题说明有人在程序外修改了数据结构。
	 * @see RsqlContainer#getPK(Modelable)
	 */
	public <T extends Modelable> boolean exists(T obj,boolean forceCheckFromDB);

	public boolean existsModel(String beanName);
	
	/**
	 * 根据非空属性或非0属性进行数据属性匹配查询并返回结果，如果没有查到，将返回size为0的new ArrayList<T>()
	 * @param model
	 * @return List TargetType extends Modelable
	 * @rmx.call {@link RsqlContainer#pickUp(Modelable)}
	 */
	public <T extends Modelable> List<T> list(T model);

	public Class<?> obtainModelClass(String beanName);

	/**
	 * 根据非空属性或非0属性进行数据属性（{@link WhereRuleOper#eq}等于判断符）匹配查询并返回结果，如果没有查到，将返回model
	 * 功能有三<br>
	 * <li>查询到唯一结果时，返回一个由当前数据库空间({@link RDBSpaceConfig#getDBBean(Class, Object)})返回的一个存有数据库全量信息的代理bean。
	 * <li>
	 * @param model
	 * @return TargetType
	 */
	public <T extends Modelable> T pickUp(T model);
	/**
	 * container实现的核心方法之一。
	 * @rmx.summary 此方法是面向对象编程面向数据库无条件查询（不包含where 语句）的入口方法。
	 * @param clazz
	 * @return DbRvo
	 * @see cn.remex.db.Container#query(java.lang.Class)
	 */
	public <T extends Modelable> DbRvo query(Class<T> clazz);

	/**
	 * container实现的核心方法之一。
	 * @rmx.summary 此方法是面向对象编程进行数据库条件查询（包含where 语句）的入口方法。
	 * @param dbCvo 
	 * @return DbRvo
	 * <pre>
	 * 		List&lt;AfterSaleService&gt; services = session.query(AfterSaleService.class,
	 * 				new SqldbCvo&lt;AfterSaleService&gt;() {
	 * 			Override
	 * 			public Class&lt;?&gt; initRules(AfterSaleService t) {
	 * 				String insuComName;
	 * 				for(ReqAddedService reqAddedService:reqService){
	 * 					insuComName = reqAddedService.getInsuComName();
	 * 					addRule(t.getInsuComName(), WhereRuleOper.eq,insuComName);
	 * 				}
	 * 				setGroupOp(WhereGroupOp.OR);
	 * 				return null;
	 * 			}
	 * 		}).obtainBeans(AfterSaleService.class);
	 * </pre>
	 * @see cn.remex.db.Container#query(DbCvo)
	 * @rmx.call {@link RsqlContainer#list(Modelable)}
	 * @rmx.call {@link RsqlContainer#queryById(Class, Object)}
	 */
	public <T extends Modelable> DbRvo query(DbCvo<T> dbCvo);
	/**
	 * @param <T>
	 * @param clazz
	 * @param resultFields 需要查询的列
	 */
	public <T extends Modelable> DbRvo query(final Class<T> clazz, String resultFields);

	/**
	 * 根据传入的查询对象的类型及主键区查询唯一条数据
	 * @param clazz
	 * @param idObject
	 * @return TargetType 如果数据库中没有则返回Null，返回的结果中包含所有bd、od列数据。
	 * @rmx.call {@link RsqlContainer#copy(DbCvo)}
	 * @rmx.call {@link RsqlCore#intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)}
	 */
	public <T extends Modelable> T queryBeanById(Class<T> clazz, Object idObject);
	/**
	 * 通过联合主键Join Primary Key（此处不对是否为联合主键进行检查，但在程序设计中需为联合主键）查询唯一的数据库bean。<br>
	 * 如果结果集超过1条，则抛出异常，异常信息为最后一个参数。
	 * @param clazz
	 * @param resultFields 返回的列。如果为null则返回所有列bd、od列。
	 * @param jpkFields 需要进行相等匹配的列，一次用[;]号隔开
	 * @param values 依次为jpkFields对应的值
	 * @return TargetType 如果数据库中没有则返回Null
	 */
	public <T extends Modelable> T queryBeanByJpk(Class<T> clazz, String resultFields, String jpkFields,Object... valuesandLastErrorMsg);

	/**
	 * 通过传入的类型clazz和主键id的值来查询其在数据库中对应的对象。
	 * @param clazz
	 * @param idObject 代表ID的object
	 * @return DbRvo
	 * @throws Exception
	 * @rmx.call {@link RsqlContainer#getPK(Modelable)}
	 * @rmx.call {@link RsqlContainer#queryBeanById(Class, Object)}
	 */
	public <T extends Modelable> DbRvo queryById(Class<T> clazz, Object idObject);

	/**
	 * @param clazz
	 * @param resultFields 需要查询出来的列，如果为null则返回所有
	 * @param constraintFields  需要进行相等匹配的列，一次用[;]号隔开
	 * @param values 依次为fields对应的值
	 * @return 返回的DbRvo中获取的bean对象仅包含resultFields的值。
	 */
	public <T extends Modelable> DbRvo queryByFields(Class<T> clazz, String resultFields,String constraintFields,Object... values);

	public <T extends Modelable> DbRvo queryWithCollectionField(Class<T> clazz,String collectionField);
	
	public <T extends Modelable> DbRvo queryWithCollectionTree(Class<T> clazz,String[] collectionFields,String[] parentIds);

	/**
	 * @param poolName
	 */
	public void setSpaceName(String poolName);

	/**
	 * 根据传入的T<T extends Modelable>对象进行存储或更新
	 * @rmx.summary 根据传入的T<T extends Modelable>对象进行存储或更新
	 * @param o
	 * @return DbRvo
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo store(T o);

	/**
	 * RsqlContainer C增加U更新D删除的核心方法。
	 * 根据传入的对象和cvo中的相关参数存储数据
	 * @param o  存储的对象
	 * @param cvo
	 * @return DbRvo
	 * @rmx.call {@link RsqlContainer#delete(Modelable)}
	 * @rmx.call {@link RsqlContainer#store(Modelable)}
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo store(T o, DbCvo<T> cvo);

	/**
	 * 根据指定fields的数据列进行保存或查看
	 * @param bean
	 * @param fields 用分号[;]将相关的列名隔开
	 * @return DbRvo
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo store(T bean, String fields);
	/**
	 * 通过联合主键Join Primary Key（此处不论是否为联合主键进行检查，但在程序设计中需为联合主键）保存唯一的数据库bean。<br>
	 * 如果更新的结果集超过1条，则抛出异常，异常信息为最后一个参数。如果通过主键无法查询到，则新建一条记录。<br>
	 * 通过此方法可以实现因为查询 -> 更新而产生的竟态问题。
	 * @param clazz
	 * @param saveFields 返回的列。如果为null则返回所有列bd、od列。
	 * @param jpkFields 需要进行相等匹配的列，一次用[;]号隔开
	 * @param moreOneErrorMsg 更新记录超过一条的异常信息。因为是主键更新，不应该出现多条记录的情况。
	 * @return TargetType 如果数据库中没有则返回Null
	 * 
	 * 此方法会返回id。
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo storeByJpk(T bean, String saveFields, String jpkFields,String moreOneErrorMsg);
	/**
	 * 通过联合主键Join Primary Key（此处不论是否为联合主键进行检查，但在程序设计中需为联合主键）更新存唯一的数据库bean，如果数据库没有也不新建。<br>
	 * 如果更新的结果集超过1条，则抛出异常，异常信息为最后一个参数。
	 * @param clazz
	 * @param saveFields 返回的列。如果为null则返回所有列bd、od列。
	 * @param jpkFields 需要进行相等匹配的列，一次用[;]号隔开
	 * @param moreOneErrorMsg 更新记录超过一条的异常信息。因为是主键更新，不应该出现多条记录的情况。
	 * @return
	 * 
	 * 此方法会返回id。
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo updateByJpk(T bean, String saveFields, String jpkFields,String moreOneErrorMsg);
	/**
	 * 比如某对象Person p = new Person();<br>
	 * p.setName("name");<br>
	 * p.setPassword("password");<br>
	 * 
	 * updateByFields(p,"name")指：<br>
	 * 如果P存在主键id，或者依据fields 等于(eq) 数据中的值去数据库中查询，p已存在，则更新，否则不进行其他操作。<br>
	 * 
	 * 注意：本方法将保存所有符合fields值匹配的记录。
	 * 
	 * @param bean
	 * @param saveFields 用";"分割的saveFields属性字符串。指定需要保存的列。
	 * @param constraintFields  用";"分割的constraintFields属性字符串。需要进行eq匹配的列。
	 * @return
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo updateByFields(T bean, String saveFields,String constraintFields);
	/**
	 * 比如某对象Person p = new Person();<br>
	 * p.setName("name");<br>
	 * p.setPassword("password");<br>
	 * 
	 * updateByFields(p,"name")指：<br>
	 * 如果P存在主键id，或者依据fields 等于(eq) 数据中的值去数据库中查询，p已存在，则更新，否则不进行其他操作。<br>
	 * 
	 * 注意：本方法将保存所有符合fields值匹配的记录,所有bean中的bd od数据列均会保存。
	 * 
	 * @param bean
	 * @param constraintFields 用";"分割的fields属性字符串。需要进行eq匹配的列。
	 * @param valuesandLastErrorMsg 需要匹配的值，最后一个为异常信息，可以不传。
	 * @return
	 * 
	 * 如果是更新，此方法不会修改bean，如果是新增会将bean的id填充。
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo updateByFields(T bean, String constraintFields);
	/**
	 * 保存bean的基本数据列，外键及list集合都不保存
	 * @param bean
	 * @return DbRvo
	 */
	@RsqlTransaction
	public <T extends Modelable> DbRvo storeBase(T bean);
	
	@RsqlTransaction
	public <T extends Modelable> DbRvo copy(DbCvo<T> cvo);
	/**
	 * @return
	 */
	public DbRvo query();

}
