package cn.remex.db.rsql;

import cn.remex.core.RemexApplication;
import cn.remex.core.RemexContext;
import cn.remex.core.exception.InvalidOperException;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.DateHelper;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.StringHelper;
import cn.remex.db.Container;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.FatalOrmBeanException;
import cn.remex.db.exception.IllegalSqlBeanArgumentException;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.SqlType.FieldType;
import cn.remex.db.sql.WhereRuleOper;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 本类对外交互的参数只有两个cvo、rvo。 其中cvo封装了读取json的基本参数，并且 在cvo中的parameter变量中必须含有
 * sqlCvoName变量的索引
 * 
 */
public class RsqlContainer implements Container, RsqlConstants {

	@SuppressWarnings("rawtypes")
	private DbCvo innerDbCvo;
	private String spaceName = RDBManager.DEFAULT_SPACE;

	@Override
	public DbRvo createCall(final String callSql) {
		return RsqlDao.getDefaultRemexDao().createCall(callSql, spaceName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> T createBean(Class<T> beanClass) {
		return (T) RDBManager.getLocalSpaceConfig(spaceName).getDBBean(beanClass);
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.Container#createDbCvo(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass) {
		innerDbCvo = new DbCvo<T>(beanClass);
		innerDbCvo.setContainer(this);
		return (DbCvo<T>) innerDbCvo;
	}
	
	@Override
	@RsqlTransaction
	public <T extends Modelable> DbRvo delete(final T o) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) o.getClass();
		DbCvo<T> dbCvo;
		if(null==innerDbCvo)
			dbCvo = new DbCvo<T>(clazz, SqlOper.del);
		else
			dbCvo = innerDbCvo;
		dbCvo.setOper(SqlOper.del);
		o.setDataStatus(DataStatus.removed.toString());// ReflectUtil.invokeSetter(SYS_dataStatus,
																										// o,
																										// DataStatus.removed.toString());
																										// LHY 2015-2-17
		dbCvo.setDataType(DT_whole);
		dbCvo.setSpaceName(spaceName);
		dbCvo.setId(o.getId());// dbCvo.setId((String)
														// ReflectUtil.invokeGetter(SYS_id, o)); LHY
														// 2015-2-17
		// dbCvo.initParam(); LHY 2015-2-17 在store中已经处理
		// RsqlRvo dbRvo = RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);
		DbRvo dbRvo = store(o, dbCvo);
		return dbRvo;
	}

	@Override
	@RsqlTransaction
	public <T extends Modelable> DbRvo deleteById(Class<T> clazz, final String id) {
		T bean = ReflectUtil.invokeNewInstance(clazz);
		bean.setId(id);
		return delete(bean);
	}

	@Override
	@RsqlTransaction
	public <T extends Modelable> DbRvo deleteByIds(Class<T> clazz, final String ids) {
		if (Judgment.nullOrBlank(ids)) {
			throw new RsqlExecuteException("批量根据主键id删除对象时，ids参数不能为空!");
		}
		DbRvo dbRvo = null;
		int ec = 0;
		for (String id : ids.split("[,;]")) {
			dbRvo = deleteById(clazz, id);
			ec += dbRvo.getEffectRowCount();
		}
		((RsqlRvo) dbRvo).setEffectRowCount(ec);
		return dbRvo;
	}

	// /**
	// * 重新整理RemexContainer入口。 此Cvo是从request、或者java程序中的action中传递过来的。有三种语言模式：
	// * 1.Map<key(String)-value(String)>的传统requtest Parameters Map模式
	// * 2.Map<JSONSqlBean-value(String JsonString)>,
	// * 此方式中requestParameter有一个名为JSONSqlBean的变量用于保存sql的json对象
	// * 3.Map<JAVASqlBean-value(SqlBean sqlBean)> 此方式中Parameters
	// * Map有一个名为JAVASqlBean的变量用于保存sqlBean的java实例对象，可直接用于程序调用。
	// * 另外，map中有一个变量SqlBeanType，取值可以为JSON,JAVA,LIST三种。用于确定以上三个模式。
	// * 默认情况可以不赋值，优先权为：JAVA,JSON,LIST
	// * @param <T>
	// * @param cvo
	// * @return
	// * @throws Exception
	// */
	// public Rvo execute(Cvo cvo) throws Exception {
	// return RsqlDao.SimpleExecute(cvo);
	// }

	// public <T extends Modelable> DbRvo execute(final DbCvo<T> cvo) {
	// DbRvo dbRvo = null;
	// dbCvo.initParam();
	// RsqlDao dao = RsqlDao.getDefaultRemexDao();
	//
	// return dbRvo;
	// }

	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public DbRvo execute(final String sql, final HashMap<String, Object> params) {
		Assert.isTrue(null==innerDbCvo,"此处execute(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！",RsqlException.class);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		DbCvo<?> dbCvo = new DbCvo(obtainManualSql(sql), params);
		dbCvo.setSpaceName(spaceName);
		dbCvo.setRowCount(0); // TODO 现在是自动全查
		dbCvo.initParam();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
		return RsqlDao.getDefaultRemexDao().execute(dbCvo);
	}

	@Override
	public RsqlRvo executeQuery(final String sql, final HashMap<String, Object> params) {
		Assert.isTrue(null==innerDbCvo,"此处executeQuery(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！",RsqlException.class);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		DbCvo<?> dbCvo = new DbCvo(obtainManualSql(sql), params);
		dbCvo.setSpaceName(spaceName);
		dbCvo.setRowCount(0); // TODO 现在是自动全查
		dbCvo.initParam();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
		return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
	}

	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public RsqlRvo executeUpdate(final String sql, final HashMap<String, Object> params) {
		Assert.isTrue(null==innerDbCvo,"此处executeUpdate(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！",RsqlException.class);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		DbCvo<?> dbCvo = new DbCvo(obtainManualSql(sql), params);
		dbCvo.setSpaceName(spaceName);
		dbCvo.initParam();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
		return RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);
	}

	@Override
	public <T extends Modelable> boolean exists(final T obj, final boolean forceCheckFromDB) {
		// ormBeans一定有getDataStatus方法，已经断言
		Object dataStatus = obj.getDataStatus();
		if (DS_saving.equals(dataStatus) || null != getPK(obj, forceCheckFromDB))
			return true;
		else
			return false;
	}

	@Override
	public boolean existsModel(String beanName) {
		return null != RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
	}

	/**
	 * 判断数据状态，本数据库模型不支持没有状态的对象数据
	 * 
	 * @param obj
	 * @return Object
	 * @rmx.call {@link RsqlContainer#needStore(Modelable)}
	 * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
	 */
	private <T extends Modelable> String getDS(final T obj) {
		// // 不为空则检查id来判断是否保存
		// // 判断是否有getId方法
		// Method getter =
		// ReflectUtil.getAllGetters(obj.getClass()).get(SYS_dataStatus);
		//
		// // 没有getDataStatus方法的对象不能通过本数据库模型保存,库中没有，直接返回null
		// if (null == getter)
		// return null;
		//
		// // 有getgetDataStatus方法则调研该方法
		// String DS = (String) ReflectUtil.invokeMethod(getter, obj);
		// 2015-2-17
		return obj.getDataStatus();
	}

	/**
	 * 判断目前数据库中是否包含参数对象<br>
	 * 
	 * @rmx.summary 本方法通过{@link ReflectUtil} 映射工具检查参数param<br>
	 *              的值，并通过{@link RsqlContainer#queryById(Class, Object)}来检查是否在<br>
	 *              本数据库中存在该对象。
	 * @param obj
	 *          需要检索的对象
	 * @return String 对象存在主键id的值，<br>
	 *         如果需要检查其在数据中是否仅唯一存在主键，请指定{@link RsqlCore#checkPKFromDataBase}
	 *         为true，否则将直接检查bean的id来验证 以下情况将返回null<br>
	 *         <li>obj == null <li>obj 没有getId() 和 setId() <li>obj 的属性id为空 <li>
	 *         obj 的属性id有值，但在数据库中不存在.此功能必须指定{@link RsqlCore#checkPKFromDataBase}
	 *         为true，否则将直接检查bean的id来验证
	 * @throws Exception
	 *           <li>如果数据库查询有错误<br> <li>或者本对象所对应的类在数据中持久化的表中的数据有重复项将会跑出异常<br>
	 *           如果数据中同样id的数据存在多条则数据完整性及结构存在问题<br>
	 *           默认有{@link RsqlCore#reset(boolean...)} 方法创建的数据库结构是不会出现该问题的，
	 *           一旦出现问题说明有人在程序外修改了数据结构。<br>
	 * 
	 *           {@link RsqlContainer#existsModel(String)}
	 *           调用本方法通过判断返回值是否为空来确定是否在数据库中存在该对象。
	 * 
	 * @see RsqlCore#setCheckPKFromDatabase(boolean)
	 */
	private <T extends Modelable> String getPK(final T obj, final boolean forceCheckFromDB) {
		String id;
		// 如果id为空则为未保存对象
		// id为空则表示是新对象，库中没有，返回fasle
		if (null == obj)
			return null;
		// 有getId方法则调研该方法
		Object idObj = obj.getId();// ReflectUtil.invokeGetter(SYS_id, obj); LHY
																// 2015-02-16
		// Object dataStatus = getDS(obj);

		id = (String) ReflectUtil.caseObject(String.class, idObj);
		/* 可以通过配置RDBConfignation.setCheckPKFULL来控制是否去数据库检查数据正确与否 */
		if ((forceCheckFromDB || RsqlCore.checkPKFromDataBase) && null != idObj) {

			// 如果id不为空，可能是数据库中的对象（标准状态id必须是从数据中获取的）
			// 通过id来检索该对象是否存在于本数据库
			RsqlRvo qr = (RsqlRvo) this.queryById(obj.getClass(), idObj);

			// 如果不在数据中
			if (qr.getRowCount() == 0)
				return null;
			else if (qr.getRecordCount() > 1)
				throw new RsqlExecuteException(obj.getClass(), idObj.toString(), "主键ID重复！");
			// return null;// never arrived here
			// 如果在数据中唯一存在
			else
				// (qr.getRecordCount() == 1)数据库中有唯一项，则保存其id作为外键
				return (String) ReflectUtil.caseObject(String.class, qr.getCell(0, SYS_id));

		}
		return id;
	}

	public String getSpaceName() {
		return spaceName;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Modelable> List<T> list(final T model) {
		Assert.notNull(model, "通过实例查询数据库中的bean时，model实例不能为空！");
		Assert.isTrue(null==innerDbCvo,"此处list(final T model)不支持链式模式的数据库访问！",RsqlException.class);

		Class<T> clazz = null;
		try {
			clazz = (Class<T>) Class.forName(StringHelper.getClassName(model.getClass()));
		} catch (ClassNotFoundException e) {
			throw new RsqlExecuteException("不存在对应的ModelClass类型。");// ReflectUtil.handleReflectionException(e);
		}
		
		List<T> beans = this.query(new DbCvo<T>(clazz) {
			private static final long serialVersionUID = 1L;

			@Override
			public void initRules(final T t) {
				setDataType(DT_base + DT_object);
				Map<String, Method> getters = RsqlUtils.obtainListGetters(model.getClass());
				Object value = null;
				Method method;
				for (String key : getters.keySet()) {
					method = getters.get(key);
					value = ReflectUtil.invokeMethod(method, model);
					if (null != value)
						if (value instanceof Modelable)
							addRule(key, WhereRuleOper.eq, ((Modelable) value).getId());
						else if (ReflectUtil.isNumeralType(value.getClass()) && Double.valueOf(value.toString()) == 0)
							;// 将所有的数字类型为零的约束条件去掉
						else if (ReflectUtil.isSimpleType(value.getClass()) && !SYS_dataStatus.equals(key) && !"version".equals(key))
							addRule(key, WhereRuleOper.eq, value.toString());
				}
			}
		}).obtainBeans();

		return beans;
	}

	@Override
	public <T extends Modelable> T pickUp(final T model) {

		List<T> beans = list(model);
		int s = beans.size();
		if (s > 1)
			throw new RsqlExecuteException("在RsqlContainer.pickUp方法中查询到多条符合查询条件的bean！");
		else if (s == 1)
			return beans.get(0);
		else
			return model;
	}

	@Override
	public <T extends Modelable> DbRvo queryWithCollectionField(Class<T> clazz, String collectionField) {
		//
		// // 如果是要显示子list
		// String sl ;
		// if ((sl = cvo.$V(PN_subList)) != null) {
		// if (dbRvo.getRecords() > 1)
		// throw new RsqlExecuteException("显示子列表" + sl + "时 目标bean不能超过一条，请添加约束条件！");
		// if (null == SqlType.getFields(beanClass, FieldType.TCollection).get(sl))
		// throw new RsqlExecuteException("要显示子列表" + sl + "不存在！");
		//
		// dbRvo = RsqlUtils.queryCollectionBeans(beanClass,
		// sl,((RsqlRvo)dbRvo).getCell(0, SYS_id));
		//
		// }else if ((sl = cvo.$V(PN_listFied)) != null) {
		// if (dbRvo.getRecords() > 1)
		// throw new RsqlExecuteException("显示子列表" + sl + "时 目标bean不能超过一条，请添加约束条件！");
		// List<Object> sublist = new ArrayList<Object>();
		// String[] fields = sl.split("[;\\.]");
		// //父id可以有也可以没有，如果有，则指查询符合条件的数据链。
		// String[] pids = Judgment.nullOrBlank(cvo.$V(PN_parentIds))?new
		// String[0]:((String) cvo.$V(PN_parentIds)).split("[;\\.]");
		// String root = fields[0];
		// if (null == SqlType.getFields(beanClass,
		// FieldType.TCollection).get(root))
		// throw new RsqlExecuteException("要显示子列表" + sl + "不存在！");
		// Object bean = dbRvo.obtainBeans().get(0);
		// ReflectUtil.invokeGetter(root, bean);
		//
		// CollectionUtils.invokeListField(bean,fields,pids,0);
		//
		// sublist.add(bean);
		//
		// RsqlRvo rvo2 = new RsqlRvo();
		// rvo2.setRows(sublist);
		// rvo2.setStatus(true);
		// dbRvo = rvo2;
		// }
		return null;
	}

	@Override
	public <T extends Modelable> DbRvo queryWithCollectionTree(Class<T> clazz, String[] collectionFields, String[] parentIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> DbRvo query(final Class<T> clazz) {
		DbCvo<T> dbCvo = null==innerDbCvo?new DbCvo<T>(clazz):innerDbCvo;
		// LHY 2015-2-17
		// dbCvo.initParam();
		// return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
		return query(dbCvo);
	}
	@SuppressWarnings("unchecked")
	@Override
	public DbRvo query() {
		Assert.notNull(innerDbCvo, "query()方法仅支持通过DbCvo.ready()来链式执行", RsqlException.class);
		// LHY 2015-2-17
		// dbCvo.initParam();
		// return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
		return query(innerDbCvo);
	}

	@Override
	public <T> T queryBean() {
		List<T> beans = (List<T>)query(innerDbCvo).obtainBeans();

		return beans.size()>0?beans.get(0):null;
	}

	@Override
	public <T> List<T> queryBeans() {
		return (List<T>)query(innerDbCvo).obtainBeans();
	}

	@Override
	public <T> T queryObject() {
		List<T> beans = (List<T>)query(innerDbCvo).obtainObjects(innerDbCvo.getBeanClass());
		return beans.size()>0?beans.get(0):null;
	}

	@Override
	public <O> O queryObject(Class<O> clazz) {
		List<O> beans = (List<O>)query(innerDbCvo).obtainObjects(clazz);
		return beans.size()>0?beans.get(0):null;
	}

	@Override
	public <T> List<T> queryObjects() {
		return (List<T>)query(innerDbCvo).obtainObjects(innerDbCvo.getBeanClass());
	}

	@Override
	public <O> List<O> queryObjects(Class<O> clazz) {
		return (List<O>)query(innerDbCvo).obtainObjects(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T extends Modelable> DbRvo query(final Class<T> clazz, String fields) {
		DbCvo<T> dbCvo =  null==innerDbCvo?new DbCvo<T>(clazz):innerDbCvo;
		dbCvo.setDataColumns(fields);
		return query(dbCvo);
	}

	@Override
	public <T extends Modelable> DbRvo query(final DbCvo<T> dbCvo) {
		dbCvo.initParam();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
		return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
	}

	@Override
	public <T extends Modelable> T queryBeanById(final Class<T> clazz, final Object idObject) {
		DbRvo dbRvo = queryById(clazz, idObject);
		List<T> beans = dbRvo.obtainBeans();
		if (beans.size() > 0)
			return beans.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> DbRvo queryById(final Class<T> clazz, final Object idObject) {
		DbCvo<T> dbCvo = null == innerDbCvo? new DbCvo<T>(clazz):innerDbCvo;
		dbCvo.setId(idObject.toString());
		dbCvo.setDataType(DT_base + DT_object);
		dbCvo.addRule(SYS_id, WhereRuleOper.eq, idObject.toString());
		return this.query(dbCvo);
	}

	@Override
	public Class<?> obtainModelClass(String beanName) {
		return RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
	}

	@SuppressWarnings("unchecked")
	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo store(final T obj) {
		DbCvo dbCvo;
		if(null ==innerDbCvo)
			dbCvo = new DbCvo<Modelable>((Class<Modelable>) obj.getClass(), SqlOper.store, DT_whole);
		else{
			innerDbCvo.putOper(SqlOper.store);
			innerDbCvo.putDataType((DT_base + DT_object).equals(innerDbCvo.getDataType()) ? DT_whole : innerDbCvo.getDataType());
			dbCvo = innerDbCvo;
		}
		return store(obj, dbCvo);//如果修改了默认值，则使用当前的
	}

	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo store(final T obj, final DbCvo<T> cvo) {
		// 如果对象为空 则不保存，空对象不保存
		RsqlAssert.notNullBean(obj);
		RsqlAssert.isOrmBean(obj);

		DbRvo dbRvo;
		String beanName = StringHelper.getClassSimpleName(obj.getClass());
		Class<?> beanClass = RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);// 必须考虑的cgil加强,后面用这个原始beanClass替换了
		// 没有在配置文件中配置的bean不保存

		// 进入对象保存，
		// 为了避免无限递归必须检查数据状态，如需保存还需设置saving
		Object dataStatus = getDS(obj);
		if (DS_saving.equals(dataStatus))
			return null;
		else
			updateDS(obj, DS_saving);
		String obj_id = getPK(obj, false);

		/** =================保存base及外键object类型数据============ */
		String dt = String.valueOf(null != cvo && null != cvo.getDataType() ? cvo.getDataType() : DT_whole);
		// 初始化数据条件对象
		SqlOper oper = obtainOper(obj_id, dataStatus, cvo.getOper(), cvo); // del/add/edit
		String dcs = (null != cvo && Judgment.notEmpty(cvo.getDataColumns())) ? cvo.getDataColumns() : (obj._isAopModelBean() ? obj._getModifyFileds() : null);// 通过切面的监控优化
		@SuppressWarnings("unchecked")
		DbCvo<T> dbCvo = new DbCvo<T>((Class<T>) obj.getClass(), oper, dt, dcs);// 需要生成本地所需的DBCVO
		dbCvo.setId(obj_id);
		if (!SqlOper.del.equals(oper)) {
			dbCvo.putParameters(maplizeObject(obj, oper, dcs)); // 更新、插入时才需要序列化基本数据列，id，datastatus列必须处理。
		}
		if (SqlOper.edit.equals(oper) && null != cvo.getSqlBeanWhere()) {
			dbCvo.setSqlBeanWhere(cvo.getSqlBeanWhere());
		}
		dbCvo.setBean((Modelable) obj);
		// 外键对象数据
		if (dt.contains(DT_object))
			this.storeFKBean(beanClass, obj, dbCvo);// 保存外键对象并将更新的外键对象的Id存入DbCvo,如Person.department更新后将
		// partment.id更新至本obj的DbCvo为partment=id

		// 执行base及object's id 保存.
		dbCvo.initParam();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
		dbRvo = RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);

		// 对象基本数据保存成功后必须更新其id
		if (dbRvo.getStatus() && null != dbRvo.getId()) {
			obj_id = dbRvo.getId();
			this.updatePK(obj, obj_id);
			this.updateDS(obj, !SqlOper.del.equals(oper) ? DS_managed : DS_removed);
		}else{
			this.updateDS(obj, dataStatus);
		}

		/** =================检查并保存collection类型数据============ */
		if (dt.contains(DT_collection))
			this.storeFKList(beanClass, obj, dbCvo, obj_id, beanName, oper);

		// 标记对象已经完成保存。
		// updateDS(obj, DS_managed); LHY 挪动至461
		return dbRvo;
	}

	@SuppressWarnings("unchecked")
	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo store(T bean, String fields) {
		DbCvo<T> dbCvo = null == innerDbCvo? new DbCvo<T>((Class<T>) bean.getClass()):innerDbCvo;

		dbCvo.setDataColumns(fields);
		return store(bean, dbCvo);
	}

	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo updateByFields(T bean, String constraintFields) {
		return updateByFields(bean, null, constraintFields);
	}

	@SuppressWarnings("unchecked")
	@Override
	// 保存基本列可以不加上事务，次数保险起见，加上。
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo storeBase(T bean) {
		DbCvo<T> dbCvo = null == innerDbCvo? new DbCvo<T>((Class<T>) bean.getClass()):innerDbCvo;
		dbCvo.setDataType(DT_base);
		return store(bean, dbCvo);
	}

	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo copy(DbCvo<T> cvo) {
		DbRvo dbRvo = null;
		String beanName = cvo.getBeanName();
		cvo.setSpaceName(this.spaceName);
		@SuppressWarnings("unchecked")
		Class<T> beanClass = (Class<T>) RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
		if (null == beanClass)
			throw new FatalOrmBeanException("指定名为" + beanName + "的ormBean并不在数据库建模的包中，请联系程序管理员！");
		Object ids = cvo.$V(SYS_ids) == null ? cvo.$V(SYS_id) : cvo.$V(SYS_ids);
		if (!Judgment.nullOrBlank(ids)) {
			String[] uniqueFields = cvo.$V("uniqueFields") != null ? cvo.$V("uniqueFields").toString().split(";") : null;
			for (String id : ids.toString().split("[,;]")) {
				Modelable sourceBean = (Modelable) queryBeanById(beanClass, id);
				Modelable newBean = ReflectUtil.invokeNewInstance(beanClass);
				ReflectUtil.copyProperties(newBean, sourceBean);// 浅层复制，不用复制id和status
				if (null != uniqueFields)
					for (String field : uniqueFields) {
						if (!Judgment.nullOrBlank(field)) {
							Method setter = ReflectUtil.getSetter(newBean.getClass(), field);
							Assert.notNull(setter, "没有如此属性:" + field);
							ReflectUtil.invokeMethod(setter, newBean, ReflectUtil.caseObject(setter.getParameterTypes()[0], Math.random()));
						}
					}
				newBean.setId(null);// 清除原来的id
				newBean.setDataStatus(DS_beanNew);// 清除原来的状态
				dbRvo = store(newBean);
			}
		} else
			throw new RsqlExecuteException("无有效的id，无法复制！");
		return dbRvo;
	}

	@Override
	public void setSpaceName(final String spaceName) {
		this.spaceName = spaceName;
	}

	// /**
	// * 本类是RemexSql操作数据库的对外接口API。<br>
	// *
	// * @rmx.summary 本方法用于检查、补充接口参数cvo的值。<br>
	// * <li>{@link DbCvo#$V(String beanName)}是RemexSql操作数据的主要参数之一，通过
	// * {@link RsqlCore#getOrmBeanClass(String Name)}<br>
	// * 可以获得对应的ormBean并直接用于数据库表的查询，必不可少.</li> <li>
	// * {@link DbCvo#$V(String oper)}
	// * 是RemexSql操作数据的主要参数之一，是指定数据操作的指令，将会转化为ADUQ 的sql指令.<br>
	// * 其值包括于{@link RsqlConstants#DO_ALL}</li>
	// *
	// * @param cvo
	// * @return boolean
	// */
	// private <T extends Modelable> boolean check(final DbCvo<T> cvo) {
	// String beanName = cvo.getBeanName();
	// SqlOper oper = cvo.getOper();
	//
	// if (null == beanName || null == oper)
	// throw new RsqlExecuteException("cvo 中beanName和oper不能为空！");
	//
	// // if (null == cvo.$V(PN_rowCount))
	// // cvo.$S(PN_rowCount, cvo.$V(PN_jqg_rowCount));
	// // if (null == cvo.$V(PN_pagination))
	// // cvo.$S(PN_pagination, cvo.$V(PN_jqg_pagination));
	// // if (null == cvo.$V(PN_dt))
	// // cvo.$S(PN_dt, DT_base);
	//
	// if (SqlOper.view.equals(oper)) {
	// cvo.setRowCount(1);
	// cvo.setPagination(1);
	// cvo.setSearch(true);
	// cvo.addRule(SYS_id, WhereRuleOper.eq, cvo.getId());
	// // cvo.$S(PN_rowCount, "100");
	// // cvo.$S(PN_pagination, "1");
	// // cvo.$S(PN_search, "true");
	// // cvo.$S(PN_searchField, SYS_id);
	// // cvo.$S(PN_searchOper, WhereRuleOper.eq.toString());
	// // cvo.$S(PN_searchString, cvo.$V(PN_id));
	// } else if (SqlOper.add.equals(oper)) {
	// // AuthUser ab = RemexContext.getContext().getUser();
	// // Staff st = ab.getStaff();
	// // boolean stExistFLag = st == null;
	// // // if (st == null)
	// // // st = CertConfiguration.SYSTEM.getStaff();
	// // String n = DateHelper.getNow();
	// // cvo.$S(SYS_createOperator, stExistFLag ? ab.getId() : st.getId());
	// // cvo.$S(SYS_createOperator_name, stExistFLag ? ab.getUsername() :
	// // st.getName());
	// // cvo.$S(SYS_createTime, n);
	// // cvo.$S(SYS_ownership, stExistFLag ? "-1" :(st.getDepartment() == null ?
	// // "-1" : st.getDepartment().getId()));
	// // cvo.$S(SYS_ownership_name,stExistFLag ? "-1" : (st.getDepartment() ==
	// // null ? "-1" : st.getDepartment().getName()));
	// // cvo.$S(SYS_modifyOperator, stExistFLag ? ab.getId() : st.getId());
	// // cvo.$S(SYS_modifyOperator_name, stExistFLag ? ab.getUsername() :
	// // st.getName());
	// // cvo.$S(SYS_modifyTime, n);
	// // } else if (DO_edit.equals(oper)) {
	// // AuthUser ab = RemexContext.getContext().getUser();
	// // Staff st = ab.getStaff();
	// // if (st == null)
	// // st = CertConfiguration.SYSTEM.getStaff();
	// // cvo.$S(SYS_modifyOperator, ab.getId());
	// // cvo.$S(SYS_modifyOperator_name, st.getName());
	// // cvo.$S(SYS_modifyTime, DateHelper.getNow());
	// }
	//
	// // 用户信息添加
	// return true;
	// }

	/**
	 * 此方法用于根据id确定是否需要保存。 如果为空表示需要保存。
	 * 
	 * @param obj
	 * @return boolean
	 */

	private <T extends Modelable> boolean needStore(final T obj) {
		// ormBeans一定有getDataStatus方法，已经断言
		String id = getPK(obj, false);
		String ds = getDS(obj);
		if (null == id || DataStatus.needSave.equals(ds))
			return true;
		return false;
	}

	/**
	 * 如果string以"SQL_"开头则从spring的配置文件Rsql_SQL中获取真实的SQL。
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String obtainManualSql(String str) {
		return str.startsWith("SQL_") ? ((Map<String, String>) RemexApplication.getBean("Rsql_SQL")).get(str) : str;
	}

	/**
	 * 根据obj的id及dataStatus的属性确定操作类型
	 * 
	 * @param <T>
	 * 
	 * @param dataStatus
	 * @param sqlOper
	 * @return String
	 */
	private <T extends Modelable> SqlOper obtainOper(final String p_id, final Object dataStatus, final SqlOper sqlOper, final DbCvo<T> cvo) {
		boolean idForNew = Judgment.nullOrBlank(p_id) || "-1".equals(p_id); // ""，null，-1代表新生状态的id
		boolean isUpdateByWhere = cvo.isUpdateByWhere();
		if (idForNew && (!isUpdateByWhere) && (DS_beanNew.equals(dataStatus) || DS_needSave.equals(dataStatus) || SqlOper.store.equals(sqlOper)))
			return SqlOper.add;
		else if (!idForNew && (!isUpdateByWhere) && (DS_removed.equals(dataStatus) || SqlOper.del.equals(sqlOper)))
			return SqlOper.del;
		else if (isUpdateByWhere || (!idForNew && (DS_managed.equals(dataStatus) || DS_needSave.equals(dataStatus) || DS_part.equals(dataStatus) || SqlOper.edit.equals(sqlOper))))
			return SqlOper.edit;
		else
			throw new InvalidOperException("数据状态id及dataStatus错误！(add：id为空或者-1 && beanNew或者needSave)；del:id不能为空并且removed；edit:isUpdateByWhere=true/id不能为空 && managed或者needSave");
	}

	/**
	 * 序列化对象为Map 类型为TBase
	 * 
	 * @param o
	 * @param oper
	 * @param dcs
	 * @return Map
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> maplizeObject(final Object o, SqlOper oper, String dcs) {
		Map<String, Object> mapFromObject = new HashMap<String, Object>();

		Class<?> clazz = o.getClass();

		Map<String, Method> baseGetters = SqlType.getGetters(clazz, FieldType.TBase);
		if (Judgment.nullOrBlank(dcs)) {// 通过dataColumns进行优化
			for (String fieldName : baseGetters.keySet()) {
				Method getter = baseGetters.get(fieldName);
				Object value = ReflectUtil.invokeMethod(getter, o);
				mapFromObject.put(fieldName, value);
			}
		} else {
			for (String fieldName : dcs.split(";")) {
				if (Judgment.nullOrBlank(fieldName))
					continue;
				Method getter = baseGetters.get(fieldName);
				if (null != getter) {
					Object value = ReflectUtil.invokeMethod(getter, o);
					mapFromObject.put(fieldName, value);
				} else {
					if (isDebug)
						logger.debug("dataColumns配置值" + dcs + "包含非base字段");
					continue;
				}
			}
		}
		mapFromObject.put(RsqlConstants.SYS_id, ReflectUtil.invokeGetter(RsqlConstants.SYS_id, o));
		mapFromObject.put(RsqlConstants.SYS_dataStatus, ReflectUtil.invokeGetter(RsqlConstants.SYS_dataStatus, o));

		// 只有在添加的时候修改者三个字段
		String now = DateHelper.getNow();
		AuthUser au = (AuthUser) RemexContext.getContext().getBean();
		String un = au == null || Judgment.nullOrBlank(au.getUsername()) ? "TEMP" : au.getUsername();
		if (oper == SqlOper.add) {
			mapFromObject.put(RsqlConstants.SYS_createTime, now);
			mapFromObject.put(RsqlConstants.SYS_createOperator, un);
			mapFromObject.put(RsqlConstants.SYS_createOperator_name, un);
			mapFromObject.put(RsqlConstants.SYS_ownership, un);
			mapFromObject.put(RsqlConstants.SYS_ownership_name, un);
		}
		// 编辑的字段和添加都需要添加这三个字段
		// if(oper == SqlOper.edit){
		// }
		mapFromObject.put(RsqlConstants.SYS_modifyTime, now);
		mapFromObject.put(RsqlConstants.SYS_modifyOperator, un);
		mapFromObject.put(RsqlConstants.SYS_modifyOperator_name, un);

		return mapFromObject;
	}

	/**
	 * 保存数据库表中的外键数据，在java里面这个关系以为这一个外键的java bean，指FKBean
	 * 
	 * @param beanClass
	 * @param obj
	 * @param cvo
	 */
	@SuppressWarnings("unchecked")
	private <T extends Modelable> void storeFKBean(final Class<?> beanClass, final T obj, final DbCvo<T> cvo) {
		/***** 外键对象数据类型保存 ********************/
		Map<String, Method> objectGetters = SqlType.getGetters(beanClass, FieldType.TObject);
		String F_id = null;// 默认外键连接id为空
		Method getter;
		T value;
		String dcs = cvo.getDataColumns();
		boolean hasDcs = !Judgment.nullOrBlank(dcs);// dcs已经在Store(T,DbCvo)处理了。
		if (hasDcs) {
			dcs = cvo.getDataColumns() + ";";
		}
		;
		RsqlCore.setLocalAutoFecthObjectFiled(false);
		for (String fieldName : objectGetters.keySet()) {
			if (hasDcs && obj._isAopModelBean() && dcs.indexOf(fieldName + ";") < 0) {
				continue;
			}
			// OneToOne otm = ReflectUtil.getAnnotation(beanClass, fieldName,
			// OneToOne.class);// 要么一对多，要么多对多

			getter = objectGetters.get(fieldName);

			// 获取外键对象
			value = (T) ReflectUtil.invokeMethod(getter, obj);

			// 如果数据库中不含有本对象则按照参数option规则递归保存外联对象
			if (null == value)
				continue;

			if (DataStatus.removed.equalsString(obj.getDataStatus())) {
				OneToOne oto = ReflectUtil.getAnnotation(beanClass, fieldName, OneToOne.class);// 一对多
				boolean hasCascade = null != oto && null != oto.cascade();
				List<CascadeType> cascade = hasCascade ? Arrays.asList(oto.cascade()) : null;
				// 如果有级联删除的标志位，则删除
				if (hasCascade && cascade.contains(CascadeType.REMOVE)) {
					delete(obj);
				}
			} else if (needStore(value)) {
				OneToOne oto = ReflectUtil.getAnnotation(beanClass, fieldName, OneToOne.class);// 一对多多
				boolean hasCascade = null != oto && null != oto.cascade();
				List<CascadeType> cascade = hasCascade ? Arrays.asList(oto.cascade()) : null;
				// 如果有级联保存的标志位，则保存
				if (hasCascade && cascade.contains(CascadeType.PERSIST)) {
					F_id = this.store(value).getId();
				}

				if (null == F_id) {
					throw new IllegalSqlBeanArgumentException("在非级联状态下，一对一中的外键对象没有保存！");
				}
			} else
				F_id = this.getPK(value, false);

			// 外键对象处理完毕，无论保存与否都保存id，如果没有则id为null
			cvo.$S(fieldName, F_id);
		}// end for ObjectFields
		RsqlCore.setLocalAutoFecthObjectFiled(true);
	}

	/**
	 * 保存数据库表中的外键list。在关系型数据库中对应为一个一对多的数据关系。
	 * 
	 * @rmx.summary 保存有两种方式<li>主表的id存于字表的字段中，从字表的方向看为外键对象</li> <li>
	 *              新建一个关系表，专门用于保存一对多关系。在Rsql中一第一种方式为准。</li>
	 * 
	 * @param beanClass
	 * @param obj
	 * @param obj_Id
	 * @param beanName
	 * @param oper
	 */
	private <T extends Modelable> void storeFKList(final Class<?> beanClass, final T obj, final DbCvo<T> cvo, final Object obj_Id, final String beanName, SqlOper oper) {
		// 取得集合映射
		Map<String, Method> cGetters = SqlType.getGetters(beanClass, FieldType.TCollection);
		Map<String, Type> cFields = SqlType.getFields(beanClass, FieldType.TCollection);
		String dcs = cvo.getDataColumns();
		boolean hasDcs = !Judgment.nullOrBlank(dcs);// dcs已经在Store(T,DbCvo)处理了。
		if (hasDcs) {
			dcs = cvo.getDataColumns() + ";";
		}
		for (String fieldName : cFields.keySet()) {
			Type fieldType = cFields.get(fieldName);
			Class<?> subBeanClass = ReflectUtil.getListActualType(fieldType);
			// 读取当前bean中collection连接对象
			@SuppressWarnings("unchecked")
			Collection<Modelable> obj_fieldColl = (Collection<Modelable>) ReflectUtil.invokeMethod(cGetters.get(fieldName), obj);// 此行是调用的非代理方法，读取的时bean中的原始数据，没有去数据库读取。
			if (SqlOper.add.equals(oper) && (null == obj_fieldColl || 0 == obj_fieldColl.size()))
				continue;// 如果是父对象是新增，且列表为空，则不用进行下面的操作！ LHY 2014-11-1 优化
			if (SqlOper.edit.equals(oper) && hasDcs && dcs.indexOf(fieldName + ";") < 0 && obj._isAopModelBean()) {
				continue;// 编辑时，如果是代理对象，且dcs里没有则不用保存
			}
			boolean notDelOper = !(SqlOper.del.equals(oper) || DataStatus.removed.equalsString(((Modelable) obj).getDataStatus())); // 不是删除操作的标志位，如果oper为删除或者数据的状态为删除都代表删除。
			if(notDelOper && null == obj_fieldColl)// 定义:在非删除情况下， Collection属性为null，即前台该属性为null或者undefined时，多对多关系不变。
				continue;
			// 读取数据库的collection数据id，
			@SuppressWarnings("unchecked")
			Map<String, Modelable> db_fieldMap = (Map<String, Modelable>) ((RsqlRvo) RsqlUtils.queryCollectionBeans(beanClass, fieldName, obj_Id)).obtainObjectsMap(SYS_id, subBeanClass);
			// 如果当前bean的collection和数据的collection都为空则不用保存
			if ((null !=obj_fieldColl && 0 == obj_fieldColl.size()) && (null == db_fieldMap || 0 == db_fieldMap.size()) ) // 前端该属性为0的数组，且数据库中的集合也为0时，不用进行多对多关系维护。
				continue;

			OneToMany otm = ReflectUtil.getAnnotation(beanClass, fieldName, OneToMany.class);// 要么一对多，要么多对多
			if (null != otm) {
				/*************** 一对多,多方用一个属性来保存这个关系的，只需更新多方外键即可 ********/

				String mappedBy = otm.mappedBy();
				boolean hasCascade = null != otm.cascade();
				List<CascadeType> cascade = hasCascade ? Arrays.asList(otm.cascade()) : null;
				if (null == mappedBy)
					continue;

				// 不为空并且 父对象不是标示为删除 则逐一保存
				if (null != obj_fieldColl && notDelOper) { // if L2
					boolean isPersist = hasCascade && cascade.contains(CascadeType.PERSIST);// 级联更新及保存
					for (Modelable co : obj_fieldColl) {
						// 一对多的多方为空则不用保存
						if (null == co)
							continue;

						Object coField_id = ReflectUtil.invokeGetter(SYS_id, co);

						if (db_fieldMap.containsKey(coField_id)) {// 数据库中已经存在
							if (isPersist) {// 因为已经存在于数据库中，故为级联更新
								ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
								store(co);
							} else if (DataStatus.needSave.equalsString(co.getDataStatus())) {
								throw new IllegalSqlBeanArgumentException("在非级联状态下，一对多中的外键对象没有保存更新！");
							}

							db_fieldMap.remove(coField_id);
						} else {// 数据库中不存在
							if (isPersist) {// 级联保存
								ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
								coField_id = store(co).getId();
							}

							if (null == coField_id) {
								throw new IllegalSqlBeanArgumentException("在非级联状态下，一对多中的外键对象没有保存！");
							}
						}

						db_fieldMap.remove(coField_id);
					}// end for collections
				}// if L2 end
					// 如果列表中还有对象，说明是前端删除的
				if (hasCascade && cascade.contains(CascadeType.REMOVE))// 删除数据
					for (Modelable co : db_fieldMap.values())
						delete(co);
				else
					// 删除外键
					for (Modelable co : db_fieldMap.values()) {
						ReflectUtil.invokeSetter(mappedBy, co, null);
						store(co);
					}

			} else {
				// 清空，让list自动获取
				ReflectUtil.invokeSetter(fieldName, obj, null);
				/******* 多对多 */
				ManyToMany mtm = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);// 要么一对多，要么多对多
				boolean hasCascade = null != mtm && null != mtm.cascade();
				List<CascadeType> cascade = hasCascade ? Arrays.asList(mtm.cascade()) : null;
				boolean meIsPrimaryTable = mtm == null || !"void".equals(mtm.targetEntity().toString());

				// 不为空并且 父对象不是标示为删除 则逐一保存
				if (null != obj_fieldColl && notDelOper) { // if
																										// 2
					boolean isPersist = hasCascade && cascade.contains(CascadeType.PERSIST);// 级联更新及保存
					for (Modelable co : obj_fieldColl) {
						// 多对多的多方为空则不用保存
						if (null == co)
							continue;

						Object coField_id = ReflectUtil.invokeGetter(SYS_id, co);

						if (db_fieldMap.containsKey(coField_id)) {// 数据库中已经存在
							if (isPersist) {// 因为已经存在于数据库中，故为级联更新
								store(co);
							} else if (DataStatus.needSave.equalsString(co.getDataStatus())) {
								throw new IllegalSqlBeanArgumentException("在非级联状态下，多对多中的外键对象没有保存更新！");
							}

							db_fieldMap.remove(coField_id);
						} else {// 数据库中不存在
							if (isPersist) {// 级联保存
								coField_id = store(co).getId();
							}

							if (null != coField_id) {
								RsqlUtils.doManyToMany_insert(beanClass, beanName, fieldName, obj_Id, coField_id, meIsPrimaryTable);
							} else {
								throw new IllegalSqlBeanArgumentException("在非级联状态下，多对多中的外键对象没有保存！");
							}
						}
					}
				}// if 2 end
					// 如果列表中还有对象，说明是前端删除的
				if (hasCascade && cascade.contains(CascadeType.REMOVE))// 级联删除
					for (Modelable co : db_fieldMap.values())
						delete(co);
				else
					// 默认删除外键关系
					for (Object F_id : db_fieldMap.keySet())
						RsqlUtils.doManyToMany_delete(beanClass, beanName, fieldName, obj_Id, F_id, meIsPrimaryTable);
			}
			// TODO
			// 由于struts调用的list对象的原始构造方法，采用ongl对list进行赋值。
			// 故list中的对象绕过了AOPFactory，不能列入数据库缓存池中，所以在此把他清除
			// 清除后，数据库的事务会发现list 为空，自动进行数据库查询，补充真实的bean进来
			// ReflectUtil.invokeSetter(fieldName, obj, null);
		}
	}

	/**
	 * 更新obj的dataStatus，标明数据的状态
	 * 
	 * @param obj
	 */
	private void updateDS(final Object obj, final Object dataStatus) {
		Method setter = ReflectUtil.getAllSetters(obj.getClass()).get(RsqlConstants.SYS_dataStatus);
		if (null != setter)
			ReflectUtil.invokeMethod(setter, obj, ReflectUtil.caseObject(setter.getGenericParameterTypes()[0], dataStatus));
		else
			throw new RsqlExecuteException(obj.getClass(), Long.valueOf(0), "保存了一个没有dataStatus的Bean，数据库数据会出现错误！");
	}

	/**
	 * 更新obj的pk，因为每个数据保存的bean都应该明确了ormBean关系。其必有id属性{@link Modelable}<br>
	 * ,这个函数是通过reflect更新其id
	 * 
	 * @param obj
	 * @param P_Id
	 */
	private void updatePK(final Object obj, final Object P_Id) {

		Method setter = ReflectUtil.getAllSetters(obj.getClass()).get(RsqlConstants.SYS_id);
		if (null != setter)
			ReflectUtil.invokeMethod(setter, obj, ReflectUtil.caseObject(setter.getGenericParameterTypes()[0], P_Id));
		else
			throw new RsqlExecuteException(obj.getClass(), Long.valueOf(0), "保存了一个没有id的Bean，数据库数据会出现错误！");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.remex.db.Container#queryBeanByJpk(java.lang.Class,
	 * java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> T queryBeanByJpk(Class<T> clazz, String resultFields, String jpkFields, Object... valuesandLastErrorMsg) {
		if (null == valuesandLastErrorMsg || null == jpkFields || valuesandLastErrorMsg.length < 1)
			throw new RsqlExecuteException("进行queryByFields操作室，参数constraintFields、values不能为空,valuesandLastErrorMsg长度不能小于1！");

		String[] fields = jpkFields.split(";");
		boolean b = fields.length == valuesandLastErrorMsg.length;
		String errorMsg = b ? "进行queryBeanByJpk时从数据库中查询到多条数据，此为数据库数据保存不合理导致逻辑上的主键约束存在异常！" : valuesandLastErrorMsg[valuesandLastErrorMsg.length - 1].toString();
		Object[] values = valuesandLastErrorMsg;
		if (!b) {
			values = new Object[valuesandLastErrorMsg.length - 1];
			System.arraycopy(valuesandLastErrorMsg, 0, values, 0, valuesandLastErrorMsg.length - 1);
		}
		DbRvo dbRvo = queryByFields(clazz, resultFields, jpkFields, values);
		if (dbRvo.getRecordCount() > 1) {
			throw new RsqlExecuteException(errorMsg);
		}

		return (T) (dbRvo.getRecordCount() == 1 ? dbRvo.obtainBeans().get(0) : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.remex.db.Container#queryByFields(java.lang.Class, java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> DbRvo queryByFields(Class<T> clazz, String resultFields, String constraintFields, Object... values) {
		DbCvo<T> dbCvo = null==innerDbCvo? new DbCvo<T>(clazz):innerDbCvo;
		if (null == values || null == constraintFields)
			throw new RsqlExecuteException("进行queryByFields操作时，参数constraintFields、values不能为空！");
		String[] fields = constraintFields.split(";");
		if (fields.length != values.length)
			throw new RsqlExecuteException("进行queryByFields操作时，参数constraintFields的属性个数、与values的个数不相等！");

		for (int i = 0; i < fields.length; i++) {
			Method getter = ReflectUtil.getGetter(clazz, fields[i]);
			if (null == getter)
				throw new RsqlExecuteException("数据库保存时(queryByFields)，所依据的fields在模型中不存在！");
			dbCvo.addRule(fields[i], WhereRuleOper.eq, String.valueOf(values[i]));
		}
		dbCvo.setDataColumns(resultFields);
		return query(dbCvo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.remex.db.Container#storeByJpk(cn.remex.db.rsql.model.Modelable,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo storeByJpk(T bean, String saveFields, String jpkFields, String moreOneErrorMsg) {
		DbRvo dbRvo = updateByJpk(bean, saveFields, jpkFields, moreOneErrorMsg);
		if(dbRvo.getEffectRowCount() == 0) {// 如果没有更新，则次数新建。
			dbRvo = store(bean, saveFields);
			bean.setId(dbRvo.getId());
		} else {
			// donot need arrive here
		}
		return dbRvo;
	}

	@Override
	@RsqlTransaction
	public <T extends Modelable> DbRvo updateByJpk(T bean, String saveFields, String jpkFields, String moreOneErrorMsg) {
		DbRvo dbRvo = updateByFields(bean, saveFields, jpkFields);
		if (dbRvo.getEffectRowCount() > 1) {
			throw new RsqlExecuteException(moreOneErrorMsg);
		}
		
		if (dbRvo.getEffectRowCount() == 1) {
			String[] jpkFieldsArr = jpkFields.split(";");
			Object[] values = new Object[jpkFieldsArr.length];
			for (int i = 0, s = jpkFieldsArr.length; i < s; i++) {
				values[i] = ReflectUtil.invokeGetter(jpkFieldsArr[i], bean);
			}
			// 因为是通过update where语句实现的，id无法返回，需要查询一次。
			Object id = queryByFields(bean.getClass(), SYS_id, jpkFields, values).getCell(0, SYS_id);
			bean.setId(id.toString());
		}
		
		return dbRvo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.remex.db.Container#storeByFields(cn.remex.db.rsql.model.Modelable,
	 * java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RsqlTransaction
	// 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
	public <T extends Modelable> DbRvo updateByFields(T bean, String saveFields, String constraintFields) {
		DbCvo<T> dbCvo = null==innerDbCvo?new DbCvo<T>((Class<T>) bean.getClass()):innerDbCvo;
		dbCvo.setDataColumns(saveFields);
		if (exists(bean, false)) {
			return store(bean, dbCvo);
		} else {
			for (String field : constraintFields.split(";")) {
				Method getter = ReflectUtil.getGetter(dbCvo.getBeanClass(), field);
				if (null == getter)
					throw new RsqlExecuteException("数据库保存时(storeByField)，所依据的fields在模型中不存在！");
				dbCvo.addRule(field, WhereRuleOper.eq, String.valueOf(ReflectUtil.invokeMethod(getter, bean)));
			}
			dbCvo.setUpdateByWhere(true);

			DbRvo dbRvo = store(bean, dbCvo);
			//LHY update方法不进行新增操作。
		/*	if (dbRvo.getEffectRowCount() == 0)// 没有更新则保存。
				return store(bean);
			else*/
				return dbRvo;
		}
	}


}
