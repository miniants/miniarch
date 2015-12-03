/**
 * 
 */
package cn.remex.db.rsql;


import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.RemexRefreshable;
import cn.remex.core.aop.AOPCaller;
import cn.remex.core.aop.AOPFactory;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.StringHelper;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.FatalOrmBeanException;
import cn.remex.db.exception.RsqlDBInitException;
import cn.remex.db.exception.RsqlInitException;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.SqlType.FieldType;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.DisposableBean;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.*;

/**
 * @author yangyang
 * 
 *  本对象本希望采用池从处理所有的条件处理，但此处并为采用，望以后处理 此处采用的是对象clone方法来解决条件冲突的。不止效率如何。
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-23
 *
 */
public final class RsqlCore extends ContainerFactory  implements RsqlConstants,RemexRefreshable,DisposableBean {

	/**
	 * #drivers=sun.jdbc.odbc.JdbcOdbcDriver com.microsoft.jdbc.sqlserver.SQLServerDriver oracle.jdbc.OracleDriver com.mysql.jdbc.Driver net.sourceforge.jtds.jdbc.Driver
	 * 默认值：drivers=net.sourceforge.jtds.jdbc.Driver com.mysql.jdbc.Driver oracle.jdbc.OracleDriver
	 */
	private static String drivers = "net.sourceforge.jtds.jdbc.Driver com.mysql.jdbc.Driver oracle.jdbc.OracleDriver";

	private static HashMap<String, RDBSpaceConfig> spaceMap = new HashMap<String, RDBSpaceConfig>();

	/**
	 * 设置是否去数据库验证主键正确否。
	 */
	public static boolean checkPKFromDataBase = false;

	/**
	 * spring 的bean工厂<br>
	 * 
	 */
	private static AOPFactory DBBeanFactory = new AOPFactory(new AOPCaller(null) {
		private static final long serialVersionUID = 865574373191977031L;
		@SuppressWarnings("unchecked")
		@Override
		public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
				throws Throwable {
			// 查找
			// getIaop().beforeMethod();
			Object result = proxy.invokeSuper(obj, args); // 方法在下面
			String methodName = method.getName();
			Class<?> returnType = method.getReturnType();
			boolean isGetMethod = args.length == 0 && methodName.startsWith("get");
			boolean isSetMethod = args.length == 1 && methodName.startsWith("set");
			@SuppressWarnings("unused")
			boolean isRetCollection=false,isRetObject=false;
			boolean fetchAndStoreAutoFlag;
			if (isGetMethod && (fetchAndStoreAutoFlag=isLocalAutoFetchObjectFiled())){
				if (SYS_Method_getDataStatus.equals(methodName) || SYS_Method_getId.equals(methodName)){
					// 此部分是无需任何处理
				} else if (null == result 
						&& (isRetCollection=Collection.class.isAssignableFrom(returnType))
						&& !Judgment.nullOrBlank(((Modelable) obj).getId())
						) {
						Class<?> beanClass = Class.forName(StringHelper.getClassName(obj.getClass()));
						String getterName = method.getName().substring(3);
						String fieldName = StringHelper.lowerFirstLetter(getterName);
						Type fieldType = SqlType.getFields(beanClass, FieldType.TCollection).get(fieldName); 
						
						DbRvo sqlDbRvo = RsqlUtils.queryCollectionBeans(beanClass, fieldName,
								ReflectUtil.invokeMethod(beanClass.getMethod(SYS_Method_getId), obj));
						
//						result = sqlDbRvo.obtainObjects(ReflectUtil.getListActualType(fieldType));LHY 20141105 返回的集合对象不是代理对象的bug
						result = sqlDbRvo.obtainBeans((Class<? extends Modelable>) ReflectUtil.getListActualType(fieldType));
						// ReflectUtil.invokeMethod(beanClass.getMethod("set"+getterName,
						// List.class), obj, result);
						ReflectUtil.invokeSetter(fieldName, obj, result);
//LHY 2015-2-17 将自动获取对象的触发事件后置到obj的某个属性为空或为0时，dataStatus状态为DS_part+DS_beanNew时去数据库中查询，提供效率。这样通过oed查询的数据列进行操作时不会读取数据库，效率有明显的提高。
				} else if (fetchAndStoreAutoFlag && null != result && (isRetObject=Modelable.class.isAssignableFrom(returnType))
						&& (DS_part+DS_beanNew).contains(((Modelable) result).getDataStatus()) // 新建的bean 或者 是再数据部分读取数据的bean
						//&& (null==((Modelable) result)._getModifyFileds() || ((Modelable) result)._getModifyFileds().contains()) 当时部分获取的时候值为空的时候，是不是每次都要读取一次数据库？
						) {
					
					Object subId = ((Modelable) result).getId();
					if (null != subId) {
						String fieldName = StringHelper.lowerFirstLetter(methodName.substring(3));
						Class<Modelable> bc = (Class<Modelable>) method.getReturnType();
						result = ContainerFactory.getSession().queryBeanById(bc , subId); // 用数据中的bean替换掉
						if(null!=result)
							ReflectUtil.invokeSetter(fieldName, obj, result);
					}
			}
//代码可用，但是否合适尚未考虑清楚，还用上面老的
//				} else if(fetchAndStoreAutoFlag && (null == result || result.equals(0)) && (!isRetCollection && !isRetObject)//基本数据列为null或等于0
//						&& (DS_part+DS_beanNew).contains(((Modelable) obj).getDataStatus())
//						){ //普通base字段，没有指名读取的，且datastatus为part的，需要去数据库中读取。
//					
//					Object id = ((Modelable) obj).getId();
//					if (null != id) {
//						Class<Modelable> beanClass = (Class<Modelable>) obj.getClass();
//						Modelable _obj = (Modelable) obj;
//						ContainerFactory.getSession().queryByFields(beanClass,null,SYS_id,id).assignRow(_obj ); // 用数据中的bean替换掉
//						if(null!=_obj){
//							_obj.setDataStatus(DS_managed);
//							result=ReflectUtil.invokeMethod(methodName, _obj);
//						}
//					}
//				}
//end for LHY 2015-2-17 将自动获取对象的触发事件后置到obj的某个属性为空或为0时，dataStatus状态为DS_part+DS_beanNew时去数据库中查询，提供效率。这样通过oed查询的数据列进行操作时不会读取数据库，效率有明显的提高。
			}else if(isSetMethod
					&& (isLocalAutoStoreObjectFiled())) {
				if ((SYS_Method_setDataStatus+SYS_Method_setId).contains(methodName)){
				}else{
					Modelable m = (Modelable)obj;
					m.setDataStatus(DS_needSave);
					//将调用了set方法的属性保存起来，在store的时候使用，节约资源
					if(!Collection.class.isAssignableFrom(returnType)
							&& !Modelable.class.isAssignableFrom(returnType))
					m._addModifyFileds(StringHelper.lowerFirstLetter(methodName.substring(3)));
				}
			}
			// getIaop().afterMethod();
			return result;
		}
	});

	/**
	 * 本线程是否自动获取object类型属性标示
	 * @rmx.summary false代表自动获取，true代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
	 */
	private static final String FecthObjectFlied = "cn.remex.db.rsql.RsqlCore.FecthObjectFlied";
	private static final String StoreObjectFlied = "cn.remex.db.rsql.RsqlCore.StoreObjectFlied";
	/**
	 * 向表中添加一个数据列。
	 * @param dialect 方言
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param columnType 列属性
	 */
	private static void AlterAddColumn(final Dialect dialect, final String tableName, final String columnName, final ColumnType columnType) {
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("ALTER TABLE ").append(dialect.quoteKey(tableName)).append(" ADD ").append(dialect.quoteKey(columnName) + "")
				.append(dialect.obtainSQLTypeString(columnType.type, columnType.length));
		ContainerFactory.getSession().executeUpdate(sqlString.toString(), null);
	}
	private static void AlterModifyColumn(final Dialect dialect, final String tableName, final String columnName, final ColumnType columnType) {
		// alter table tablename modify  columnName varchar(len);
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("ALTER TABLE ").append(dialect.quoteKey(tableName)).append(" MODIFY ").append(dialect.quoteKey(columnName) + "")
		.append(dialect.obtainSQLTypeString(columnType.type, columnType.length));
		ContainerFactory.getSession().executeUpdate(sqlString.toString(), null);
	}
	
	/**
	 * 本程序用于自动创建JavaBean对应的关系型数据表基本结构 仅支持Int nvarchar两种数据类型
	 * @param dialect
	 * @param tableName
	 * @param beanClass
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 */
	public static void createBaseTable(final Dialect dialect, final String tableName, final Class<?> beanClass) {
		Map<String, Type> baseFields = SqlType.getFields(beanClass, FieldType.TBase);
		Map<String, Type> objectFields = SqlType.getFields(beanClass, FieldType.TObject);
		Map<String, ColumnType> partSysColumns = new HashMap<String, ColumnType>();
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, baseFields);
		Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(objectFields);

		String prefix = "\r\nCREATE TABLE " + dialect.quoteKey(tableName) + " (\r\n";
		String suffix = "\r\n)\r\n";
		StringBuilder content = new StringBuilder();

		// 系统数据列
		content.append("		").append(dialect.quoteKey(SYS_id) + " " + dialect.obtainSQLTypeString(Types.CHAR,50) + " NOT NULL PRIMARY KEY,\r\n");
		content.append("		").append(dialect.quoteKey(SYS_dataStatus) + " " + dialect.obtainSQLTypeString(Types.CHAR,10) + " NULL,\r\n");
		content.append("		").append(dialect.quoteKey(SYS_version) + " " + dialect.obtainSQLTypeString(Types.INTEGER,22) + " NULL,\r\n");
		partSysColumns.putAll(RsqlUtils.SysCreateColumns);
		partSysColumns.putAll(RsqlUtils.SysModifyColumns);
		for (String column : partSysColumns.keySet()) {
			ColumnType ct = partSysColumns.get(column);
			content.append("		").append(dialect.quoteKey(column)).append(dialect.obtainSQLTypeString(ct.type,ct.length)).append(" NULL,\r\n");
		}

		// ****************添加bean中的基本数据属性，定义为数据库中的字段
		for (String baseColumn : baseColumns.keySet()) {
			ColumnType ct = baseColumns.get(baseColumn);
			content.append("		").append(dialect.quoteKey(baseColumn)).append(dialect.obtainSQLTypeString(ct.type,ct.length)).append(" NULL,\r\n");
		}

		// *****************添加bean中的表连接属性，定义为int，并随后建立表外键连接
		for (String objectColumn : objectColumns.keySet()) {
			ColumnType ct = objectColumns.get(objectColumn);
			content.append("		").append(dialect.quoteKey(objectColumn)).append(dialect.obtainSQLTypeString(ct.type,ct.length)).append(" NULL,\r\n");
		}

		String mid = content.toString();
		mid = mid.substring(0, mid.length() - 3);

		ContainerFactory.getSession().executeUpdate(prefix + mid + suffix, null);

	}
	/**
	 * 此函数建立bean中List/Set/Vector关联的表
	 * @param dialect
	 * @param beanName
	 * @param fieldName
	 * @param fieldType
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 */
	public static void createCollectionTable(final Dialect dialect, final String beanName, final String fieldName, final Type fieldType) {

		// 因为获取的就是List、Set、Vector类型，所以一定是ParameterizedTypeImpl，且ActualTypeArguments是一个参数
//		ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) fieldType;
//		Type[] types = typeImpl.getActualTypeArguments();
		Type[] types = ReflectUtil.getActualTypeArguments(fieldType);

		// 获取本表的必须列，共两列，是确定的
		Map<String, ColumnType> columns = RsqlUtils.obtainSKeyCollectionTableColumns(beanName, fieldName, fieldType);

		String tableName = RsqlUtils.obtainSKeyCollectionTableName(dialect, beanName, fieldName);
		String prefix = "CREATE TABLE " + dialect.quoteKey(tableName);
		String suffix = "\r\n)\r\n";

		StringBuilder content = new StringBuilder("(\n");
		for (Type type1 : types) {
			if (SqlType.isTMap(type1) || SqlType.isTCollection(type1)) {
				throw new RsqlInitException(beanName, "不支持深层Map映射，数据库ORM映射创建失败！");
			} else if (type1 == Object.class) {
				throw new RsqlInitException(beanName, "不支持Object 原始类型直接Map映射，数据库ORM映射创建失败！");
			}
		}

		content.append("		").append(dialect.quoteKey(SYS_id) + " " + dialect.obtainSQLTypeString(Types.CHAR,50) + " NOT NULL PRIMARY KEY,\r\n");
		content.append("		").append(dialect.quoteKey(SYS_dataStatus) + " " + dialect.obtainSQLTypeString(Types.CHAR,10) + ",\r\n");
		// *****************添加bean中的表连接属性，定义为int，并随后建立表外键连接
		for (String objectColumn : columns.keySet()) {
			ColumnType ct =columns.get(objectColumn);
			content.append("		").append(dialect.quoteKey(objectColumn)).append(dialect.obtainSQLTypeString(ct.type,ct.length)).append(" NOT NULL,\r\n");
		}

		String mid = content.toString();
		mid = mid.substring(0, mid.length() - 3);
		ContainerFactory.getSession().executeUpdate(prefix + mid + suffix, null);
	}
	/**
	 * 直接获取非缓存dbbean
	 * @param clazz
	 * @return T
	 * @rmx.call {@link RDBSpaceConfig#getDBBean(Class, Object)}
	 * @rmx.call {@link RsqlRvo#setValue}
	 */
	public static <T> T createDBBean(final Class<T> clazz) {
		RsqlAssert.notNull(clazz, "没有找到对应的ORMClass,无法创建相应的");
		if (!Modelable.class.isAssignableFrom(clazz)) {
			throw new FatalOrmBeanException(clazz.getName() + "没有实现Modelable,不是有效的ORMBean!");
		}
		T t = null;
		try {
			// t = (T) clazz.newInstance();
			t = (T) DBBeanFactory.getBean(clazz);
			((Modelable) t)._setAopModelBean();
		} catch (Exception e) {
			throw new FatalOrmBeanException(clazz.getName() + "无法创建！", e.getCause());
		}
		Modelable m = (Modelable) t;
		m.setDataStatus(DS_beanNew);
		return t;
	}

	/**
	 * 初始化 也用于外界调用以便重新初始化参数 设计中已经考虑的外界注入式的池初始
	 * @throws Exception
	 */
	public static void reset(final boolean... rebuildDB) {
		RDBManager.reset(spaceMap,drivers,null!=rebuildDB && rebuildDB[0]);
	}

//	public static void initDBSystemTable(final RDBSpaceConfig spaceConfig) {
//		Dialect dialect = spaceConfig.getDialect();
//		RsqlUtils.createSystemTable(dialect);
//	}

	public static boolean isLocalAutoFetchObjectFiled(){
		Object b = CoreSvo.$VL(FecthObjectFlied);
		return (null==b?true:(Boolean)b);
	}
	public static boolean isLocalAutoStoreObjectFiled(){
		Object b = CoreSvo.$VL(StoreObjectFlied);
		return (null==b?true:(Boolean)b);
	}
	private static void modifyColumn(String tableName,DbRvo columnNames, ColumnType ct,String columnName,   Dialect dialect){

		List<Object> cns = columnNames.getCells(0, columnName, 0);//存在的列名，要么有一个要么没有
		if (cns.size() == 0) { //没有
			AlterAddColumn(dialect, tableName, columnName, ct);
			logger.info("现已完成为名为" + tableName + "的表添加列" + columnName);
		}else{ // 有
			String type = columnNames.getCells(0, columnName, "DATA_TYPE").get(0).toString();
			Object length = columnNames.getCells(0, columnName, "DATA_LENGTH").get(0);
			String sqlTypeStr = dialect.obtainSQLTypeString(ct.type, ct.length).trim();
			
			if(ct.type == Types.CLOB) return;
			
			if(!sqlTypeStr.split("[ \t(]")[0].equalsIgnoreCase(type) || !String.valueOf(ct.length).equalsIgnoreCase(String.valueOf(length)) ){ // 长度和类型不同时需修改
				AlterModifyColumn(dialect, tableName, columnName, ct);
				logger.info("现已完成为名为" + tableName + "的表修改列" + columnName+":"+type+" "+length+" ->" +dialect.obtainSQLTypeString(ct.type, ct.length));
			}
		}
	
	}

	/**
	 * 此函数一检查了base object在内的基本表数据列
	 * @param spaceConfig
	 * @throws Exception
	 * @rmx.call {@link RDBManager#createSpace}
	 */
	public static void refreshORMBaseTables(final RDBSpaceConfig spaceConfig) {
		Map<String, Class<?>> ormBeans = spaceConfig.getOrmBeans();
		Dialect dialect = spaceConfig.getDialect();
		logger.debug("▄▄▄▄▄▄▄▄▄▄◢RemexDB的ORM组件正在检查数据库中的 基本表BaseTables◣▄▄▄▄▄▄▄▄▄▄");
		// 此语句仅适用于mssql查询数据库中表名
		Container session = ContainerFactory.getSession();
		DbRvo tableNames = session.executeQuery(dialect.obtainSQLSelectTableNames(), null);

		for (String beanName : ormBeans.keySet()) {
			String tableName = dialect.needLowCaseTableName()? beanName.toLowerCase():beanName;

			if (tableNames.getCells(0, tableName, 0).size() != 1) {
				createBaseTable(dialect, tableName, ormBeans.get(beanName));
				logger.info("创建名为" + tableName + "的表完成！");
			} else {
				Class<?> beanClass = ormBeans.get(beanName);
				ORMBaseTablesModify(tableName, beanClass, dialect, session);
			}
		}
		logger.debug("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄END▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

	}
	private static void ORMBaseTablesModify(String tableName,Class<?> beanClass, Dialect dialect, Container session){

		logger.debug("数据库中存在名为" + tableName + "的表！跳过创建阶段，进行表结构检查！");

		// 添加需要检查的列s
		List<Map<String, ColumnType>> columnsList = new ArrayList<Map<String, ColumnType>>();
		Map<String, ColumnType> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
		columnsList.add(sysColumns);// 检查系统列
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, SqlType.getFields(beanClass, FieldType.TBase));
		columnsList.add(baseColumns);// 检查基础数据列
		Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(SqlType.getFields(beanClass, FieldType.TObject));
		columnsList.add(objectColumns);// 检查一对一数据列
		
		// 获取数据库中对应表的列s
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put(PN_rowCount, "0");
		DbRvo columnNames = session.executeQuery(dialect.obtainSQLSelectTablesColumnNames(tableName), params);
		
		for (Map<String, ColumnType> columns : columnsList) {
			for (String columnName : columns.keySet()) {
				modifyColumn(tableName, columnNames, columns.get(columnName), columnName, dialect);
			}
		}
	
	}
	/**
	 * 此函数一检查了collection object在内的外键表CollectionTables
	 * @param spaceConfig
	 * @rmx.call {@link RDBManager#createSpace}
	 */
	public static void refreshORMCollectionTables(final RDBSpaceConfig spaceConfig) {
		Map<String, Class<?>> ormBeans = spaceConfig.getOrmBeans();
		Dialect dialect = spaceConfig.getDialect();
		logger.debug("▄▄▄▄▄▄▄▄▄▄◢RemexDB的ORM组件正在检查数据库中的外键表CollectionTables◣▄▄▄▄▄▄▄▄▄▄▄▄▄");
		// 此语句仅适用于mssql查询数据库中表名
		Container session = ContainerFactory.getSession();
		DbRvo tableNames = session.executeQuery(dialect.obtainSQLSelectTableNames(),null);

		for (String beanName : ormBeans.keySet()) {
			Class<?> beanClass = ormBeans.get(beanName);
			Map<String, Type> fields = SqlType.getFields(beanClass, FieldType.TCollection);

			for (String fieldName : fields.keySet()) {
				String tableName = RsqlUtils.obtainSKeyCollectionTableName(dialect, beanName, fieldName);
				tableName = dialect.needLowCaseTableName()? tableName.toLowerCase():tableName;
				Type field = fields.get(fieldName);
				if (!(tableNames.getCells(0, tableName.toString(), 0).size() == 1)) {
					OneToMany otm = ReflectUtil.getAnnotation(beanClass, fieldName, OneToMany.class);
					if (otm != null) {// 此为一对多，没有注明一对多则新建中间表,表示多对多
						String mappedField = otm.mappedBy();// 指明了OneToMany关系中，外键由多方保管更新，则需要核对多方是否有这个外键
						Class<?> targetClass = ReflectUtil.getListActualType(field);
						Map<String, Type> fbFields = SqlType.getFields(targetClass, FieldType.TObject);
						if (!fbFields.containsKey(mappedField)) {
							throw new RsqlDBInitException("OneToMany映射错误，在多方[ " + targetClass.toString() + " ]未设置外键[ "
									+ mappedField + " ]维护关系。可能是getter/setter的名称设置有误！");
						}
					} else {
						ManyToMany mtm = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);
						//不配置ManyToMany、或者显式配置ManyToMany时，本类为多对多方的主方，负责维护中间表
						if(null == mtm || !"void".equals(mtm.targetEntity()) ){
							createCollectionTable(dialect, beanName, fieldName, field);
							logger.info("创建名为" + tableName + "的表完成！ ");
						}
					}
				} else {
					logger.debug("数据库中存在名为" + tableName + "的表！跳过创建阶段，进行表结构检查！");

					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("tableName", tableName);
					DbRvo columnNames = session.executeQuery(dialect.obtainSQLSelectTablesColumnNames(tableName), params);

					List<Map<String, ColumnType>> columnsList = new ArrayList<Map<String, ColumnType>>();
					Map<String, ColumnType> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
					columnsList.add(sysColumns);// 检查系统列
					Map<String, ColumnType> neededColumns = RsqlUtils.obtainSKeyCollectionTableColumns(beanName, fieldName, field);
					columnsList.add(neededColumns);// 添加这个表的必需列

					for (Map<String, ColumnType> columns : columnsList) {
						for (String columnName : columns.keySet()) {
							modifyColumn(tableName, columnNames, columns.get(columnName), columnName, dialect);
						}
					}
				}
			}
		}
		logger.debug("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄END▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

	}
	
	/**
	 * 此函数一检查数据库中的约束
	 * @param spaceConfig
	 * @rmx.call {@link RDBManager#createSpace}
	 */
	public static void refreshORMConstraints(final RDBSpaceConfig spaceConfig) {
		Map<String, Class<?>> ormBeans = spaceConfig.getOrmBeans();
		Dialect dialect = spaceConfig.getDialect();
		logger.debug("▄▄▄▄▄▄▄▄▄▄◢RemexDB的ORM组件正在检查数据库中的约束◣▄▄▄▄▄▄▄▄▄▄▄▄▄");
		Container session = ContainerFactory.getSession();
		
//		// 测试用 删除所有索引用户索引
//		for(List<Object> row:indexNames.getRows()){
//			String idx = row.get(0).toString();
//			if(idx.toString().startsWith("SYS_") || idx.toString().startsWith("PK_")){
//				continue;
//			}
//			String tbname = row.get(3).toString();
//			String unique = row.get(5).toString();
//			if("UNIQUE".equals(unique)){
//				session.execute("alter table "+dialect.quoteKey(tbname)
//						+" drop constraint "+dialect.quoteKey(idx), null);
//			}else{
//				session.execute("drop index "+idx.toString(), null);
//			}
//		}
		
		
		for (String beanName : ormBeans.keySet()) {
			DbRvo indexNames = session.executeQuery(dialect.obtainSQLSelectIndexs(beanName),null);
			Class<?> beanClass = ormBeans.get(beanName);

			//检查系统索引
			for(String col:RsqlUtils.SysColumns.keySet()){
				if(indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, col), dialect.obtainSQLIndexNameField()).size()==0 && !SYS_id.equals(col))
					session.execute(dialect.obtainIndexSql(beanName, col), null);
			}
			for(String col:SqlType.getFields(beanClass, FieldType.TObject).keySet()){
				if(indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, col), dialect.obtainSQLIndexNameField()).size()==0 && !RsqlUtils.SysColumns.containsKey(col))
					session.execute(dialect.obtainIndexSql(beanName, col), null);
			}

			Table table_anno = ReflectUtil.getAnnotation(beanClass, Table.class);

            if(null != table_anno && null != table_anno.uniqueConstraints()){
                //检查自定配置索引 TODO 暂未实现,用于生成自定义的索引
//                Index[] idxs = table_anno.indexes();
//                for(Index idx:idxs){
//                    idx.
//                }
//                Map<String, Type> fields = SqlType.getFields(beanClass, FieldType.TBase);
//                for (String fieldName : fields.keySet()) {
//                    if(indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, fieldName), dialect.obtainSQLIndexNameField()).size() == 0) {
//                        Column anno = ReflectUtil.getAnnotation(beanClass, fieldName, Column.class);
//                        if(null!=anno && anno.index()) {
//                            session.execute(dialect.obtainIndexSql(beanName, fieldName), null);
//                        }
//                    }
//
//                }

                //检查唯一性索引
                UniqueConstraint[] ucs = table_anno.uniqueConstraints();
                for(UniqueConstraint uc:ucs){
                    String[] cols = uc.columnNames();
                    String name = null==uc.name()?uc.name():dialect.obtainIndexName(beanName, cols);//约束名称可以自定义，也可以由系统根据方言自动生成。
                    if(null!=cols && cols.length>0 && indexNames.getCells(dialect.obtainSQLIndexNameField(), name, dialect.obtainSQLIndexNameField()).size()==0){
                        session.execute(dialect.obtainConstraintSql(beanName, name,cols), null);
                    }
                }
            }

			//检查OneToOne、OneToManay、ManyToMany中的级联
			
			
			
		}
		
	}

	/**
	 * 指定是否从进行数据库查询来确定id主键的合法性。
	 * @param checkPKFromDataBase
	 */
	public static void setCheckPKFromDatabase(final boolean checkPKFromDataBase) {
		RsqlCore.checkPKFromDataBase = checkPKFromDataBase;
	}
	
	/**
	 * 本线程是否自动获取object类型属性标识
	 * @param b true代表自动获取，false代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
	 * @rmx.call {@link RsqlRvo#obtainBeans()}
	 * @rmx.call {@link RsqlContainer#storeFKBean}
	 */
	public static void setLocalAutoFecthObjectFiled(boolean b){
		CoreSvo.$SL(FecthObjectFlied, b);
	}
	
	/**
	 * 本线程是否自动存储object类型属性标识
	 * @param b true代表自动获取，false代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
	 * @rmx.call {@link RsqlRvo#obtainBeans()}
	 */
	public static void setLocalAutoStoreObjectFlied(boolean b){
		CoreSvo.$SL(StoreObjectFlied, b);
	}
	
	/**
	 * @param spaceName
	 * @return Container
	 *  @rmx.call {@link ContainerFactory#getSession}
	 */
	@Override
	public Container getContainer(String spaceName) {
		Container con = (Container) RemexApplication.getBean(RsqlContainer.class);
		con.setSpaceName(spaceName);
		return con;
	}
	
	/**
	 * 构造方法
	 * @param drivers
	 * @param spaceMap
	 * @param rebuildDB
	 */
	public RsqlCore(String drivers,HashMap<String, RDBSpaceConfig> spaceMap,boolean rebuildDB) {
		RsqlCore.drivers = drivers;
		RsqlCore.spaceMap = spaceMap;
		RDBManager.reset(spaceMap, drivers, rebuildDB);
	}
	@Override
	public void refresh() {
		logger.info("刷新Rsql配置，重新加载jdbc驱动、配置！");
		reset(false);
	}
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		logger.info("注销数据Rsql！");
		RDBManager.destroy();
	}
	
	

	// public static void refreshORMMapTables(Map<String, Class<?>> ormBeans) {
	// //
	// logger.debug("▄▄▄▄▄▄▄▄▄▄◢RemexDB的ORM组件正在检查数据库中的Map表MapTables◣▄▄▄▄▄▄▄▄▄▄");
	// // //此语句仅适用于mssql查询数据库中表名
	// // RsqlCvo cvo = new
	// RsqlCvo("SELECT Name FROM SysObjects WHERE XType='U' ORDER BY Name ");
	// // QueryResult tableNames =
	// RsqlDao.getDefaultRemexDao().executeQuery(cvo).getQueryResult();
	// //
	// // for (String beanName : ormBeans.keySet()) {
	// // Map<String, Type> fields =
	// SqlType.getFields(ormBeans.get(beanName),SqlType.TMap);
	// //
	// // for(String fieldName:fields.keySet()){
	// // String tableName = RsqlUtils.obtainSKeyMapTableName(beanName,
	// fieldName);
	// // if (!(tableNames.getCells(0, tableName.toString(), 0).size()==1)) {
	// // RsqlDao.getDefaultRemexDao().execute(
	// // new RsqlCvo(RsqlUtils.obtainSQLCreateMapTable(beanName, fieldName,
	// fields.get(fieldName))));
	// // logger.info("创建名为"+tableName+"的表完成！");
	// // }else{
	// // logger.debug("数据库中存在名为"+tableName+"的表！跳过创建阶段，进行表结构检查！");
	// // RsqlCvo cvo2 = new
	// RsqlCvo(RsqlUtils.obtainSQLSelectTablesColumnNames(tableName));
	// // QueryResult columnNames =
	// RsqlDao.getDefaultRemexDao().executeQuery(cvo2).getQueryResult();
	// //
	// // List<Map<String, Integer>> columnsList = new
	// ArrayList<Map<String,Integer>>();
	// // Map<String, Integer> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
	// // columnsList.add(sysColumns);//检查系统列
	// // Map<String, Integer> neededColumns =
	// RsqlUtils.obtainSKeyMapTableColumns(beanName,fieldName,
	// fields.get(fieldName));
	// // columnsList.add(neededColumns);//添加这个表的必需列
	// //
	// // for(Map<String, Integer> columns:columnsList){
	// // for(String columnName:columns.keySet()){
	// // if(!(columnNames.getCells(0, columnName, 0).size()==1)){
	// // RsqlDao.getDefaultRemexDao().execute(
	// // new RsqlCvo(RsqlUtils.obtainSQLAddColumn(tableName,
	// columnName,columns.get(columnName))));
	// // logger.info("现已完成为名为"+tableName+"的表添加列"+columnName);
	// // }
	// // }
	// // }
	// // }
	// // }
	// // }
	// //
	// logger.debug("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄END▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
	// }

}
