package cn.remex.db.rsql;

import cn.remex.core.cache.DataCachePool;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.*;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.RsqlInitException;
import cn.remex.db.rsql.aspect.RsqlMonitorAspect;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.*;
import cn.remex.db.sql.SqlType.FieldType;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.oro.text.regex.MatchResult;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RsqlUtils implements RsqlConstants {

	/**
	 * 分析处理带命名参数的SQL语句。使用Map存储参数，然后将参数替换成? <br/>
	 * @param sql
	 * @return
	 */
	private static final String NamedSqlEquationRegex = "(([_\\.\\w\u4e00-\u9fa5]+)=:)";

	private static final String NamedSqlEquationRegexRplc = ":";// 这个必须最后替换
	private static final String NamedSqlParamRegex = "(:([_\\.\\w\u4e00-\u9fa5]+))";
	private static final String NamedSqlParamRegexRplc = "(:([_\\.\\w\u4e00-\u9fa5]+))";
	static private Map<Integer, Class<?>> SqlClass;

	static Map<Class<?>, ColumnType> SqlTypes;
	static Map<String, ColumnType> SysColumns;
	static Map<Class<?>, Map<String,Method>> GettersWithOutSysColumnForList;
	static Map<String, ColumnType> SysCreateColumns;
	static Map<String, ColumnType> SysModifyColumns;
	static {
		SysCreateColumns = new ReadOnlyMap<String, ColumnType>();
		SysCreateColumns.put(SYS_createOperator, new ColumnType(Types.CHAR, 50));
		SysCreateColumns.put(SYS_createOperator_name, new ColumnType(Types.CHAR, 20));
		SysCreateColumns.put(SYS_createTime, new ColumnType(Types.CHAR, 50));
		SysCreateColumns.put(SYS_ownership, new ColumnType(Types.CHAR, 50));
		SysCreateColumns.put(SYS_ownership_name, new ColumnType(Types.CHAR, 20));

		SysModifyColumns = new ReadOnlyMap<String, ColumnType>();
		SysModifyColumns.put(SYS_modifyOperator, new ColumnType(Types.CHAR, 50));
		SysModifyColumns.put(SYS_modifyOperator_name, new ColumnType(Types.CHAR, 20));
		SysModifyColumns.put(SYS_modifyTime, new ColumnType(Types.CHAR, 50));

		SysColumns = new ReadOnlyMap<String, ColumnType>();
		SysColumns.put(SYS_id, new ColumnType(Types.CHAR, 50));
		SysColumns.put(SYS_dataStatus, new ColumnType(Types.CHAR, 10));
		SysColumns.put(SYS_version, new ColumnType(Types.INTEGER, 22));
		SysColumns.putAll(SysCreateColumns);
		SysColumns.putAll(SysModifyColumns);

		SqlTypes = new ReadOnlyMap<Class<?>, ColumnType>();
		SqlTypes.put(short.class, new ColumnType(Types.INTEGER,22));
		SqlTypes.put(int.class, new ColumnType(Types.INTEGER, 22));
		SqlTypes.put(long.class, new ColumnType(Types.INTEGER, 22));
		SqlTypes.put(double.class, new ColumnType(Types.DOUBLE, 22));
		SqlTypes.put(float.class, new ColumnType(Types.FLOAT, 22));
		SqlTypes.put(boolean.class, new ColumnType(Types.BOOLEAN, 0));
		SqlTypes.put(byte.class, new ColumnType(Types.BIT, 0));
		SqlTypes.put(char.class, new ColumnType(Types.CHAR, 0));

		SqlTypes.put(Short.class, new ColumnType(Types.INTEGER, 22));
		SqlTypes.put(Integer.class, new ColumnType(Types.INTEGER, 22));
		SqlTypes.put(Long.class, new ColumnType(Types.INTEGER, 22));
		SqlTypes.put(Double.class, new ColumnType(Types.DOUBLE, 22));
		SqlTypes.put(Float.class, new ColumnType(Types.FLOAT, 22));
		SqlTypes.put(Boolean.class, new ColumnType(Types.BOOLEAN, 0));
		SqlTypes.put(Byte.class, new ColumnType(Types.BIT, 0));
		SqlTypes.put(Character.class, new ColumnType(Types.CHAR, 0));

		SqlTypes.put(String.class, new ColumnType(Types.CHAR, 100));
		SqlTypes.put(Date.class, new ColumnType(Types.CHAR, 100));

		SqlClass = new ReadOnlyMap<Integer, Class<?>>();
		for (Class<?> i : SqlTypes.keySet()) {
			SqlClass.put(SqlTypes.get(i).type, i);
		}
		
		
		GettersWithOutSysColumnForList = new ReadOnlyMap<Class<?>, Map<String,Method>>();

	}

//	/**
//	 * 此函数建立系统配置表
//	 */
//	static public void createSystemTable(final Dialect dialect) {
//		QueryResult tableNames = RsqlDao.executeQuery(dialect.obtainSQLSelectTableNames()).getQueryResult();
//		if(tableNames.getCells(0, SYS_tableName, 0).size()==1){
//			return;
//		}
//
//
//		String prefix = "CREATE TABLE "+ dialect.openQuote()+SYS_tableName +dialect.closeQuote();
//		String suffix = "\r\n)\r\n";
//		StringBuilder content = new StringBuilder("(\n");
//
//		content.append("		").append(dialect.openQuote() + SYS_id + dialect.closeQuote()+" "
//				+ dialect.obtainSQLTypeString(Types.CHAR)
//				+ " NOT NULL PRIMARY KEY,\r\n");
//		content.append("		").append(dialect.openQuote() + SYS_key + dialect.closeQuote()+" "
//				+ dialect.obtainSQLTypeString(Types.CHAR) + " NULL,\r\n");
//		content.append("		").append(dialect.openQuote() + SYS_value + dialect.closeQuote()+" "
//				+ dialect.obtainSQLTypeString(Types.CHAR) + " NULL,\r\n");
//		String mid = content.toString();
//		mid = mid.substring(0, mid.length() - 3);
//		RsqlDao.executeUpdate(prefix + mid +suffix);
//	}
	//	/**
	//	 * 此函数建立bean中Map关联的表
	//	 *
	//	 * @return @
	//	 */
	//	static public String obtainSQLCreateMapTable(String beanName,
	//			String fieldName, Type fieldType) {
	//				return fieldName;
	//
	////		// 因为获取的就是List、Set、Vector类型，所以一定是ParameterizedTypeImpl，且ActualTypeArguments是一个参数
	////		ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) fieldType;
	////		Type[] types = typeImpl.getActualTypeArguments();
	////
	////		// 获取本表的必须列，共三列，是确定的
	////		Map<String, Integer> columns = obtainSKeyCollectionTableColumns(
	////				beanName, fieldName, fieldType);
	////
	////		String prefix = "CREATE TABLE ";
	////		StringBuilder content = new StringBuilder("(\n");
	////		for (Type type1 : types) {
	////			if (SqlType.isTMap(type1) || SqlType.isTCollection(type1))
	////				throw new (
	////						new RsqlInitException("不支持深层Map映射，数据库ORM映射创建失败！"));
	////			else if (type1 == Object.class)
	////				throw new RsqlInitException(
	////						"不支持Object 原始类型直接Map映射，数据库ORM映射创建失败！");
	////		}
	////
	////		content.append("		").append(dialect.openQuote() + SYS_id + dialect.closeQuote()+" "
	////				+ obtainSQLTypeString(Types.INTEGER) + " " + SQL_IDENTITY
	////				+ " NOT NULL PRIMARY KEY,\r\n");
	////		content.append("		").append(dialect.openQuote()+ SYS_dataStatus + dialect.closeQuote()+" "
	////				+ obtainSQLTypeString(Types.CHAR) + " NULL,\r\n");
	////		for (String column : columns.keySet()) {
	////			content.append("		").append(dialect.openQuote());
	////			content.append(column);
	////			content.append(dialect.closeQuote()+"");
	////			content.append(obtainSQLTypeString(columns.get(column))).append(
	////					",\r\n");
	////		}
	////
	////		String mid = content.toString();
	////		mid = mid.substring(0, mid.length() - 3);
	////		return prefix + obtainSKeyMapTableName(beanName, fieldName) + mid
	////				+ "\r\n)";
	//	}
	private static String doManyToMany_delete_cacheKey = "RsqlUtils.doManyToMany_delete";

	/**
	 * 多对多关系处理-删除
	 * @param beanClass
	 * @param beanName
	 * @param fieldName
	 * @param obj_id
	 * @param coField_id
	 * @param meIsPrimaryTable
	 * @return RsqlRvo
	 * @rmx.call {@link RsqlContainer#storeFKList}
	 */
	public static DbRvo doManyToMany_delete(Class<?> beanClass, final String beanName,
			final String fieldName, final Object obj_id,
			final Object coField_id, boolean meIsPrimaryTable) {
		Object key = new StringBuilder().append(beanClass.hashCode()).append(beanName.hashCode()).append(fieldName.hashCode()).append(meIsPrimaryTable);
		@SuppressWarnings("unchecked")
		ArrayList<String> sqlOpts = (ArrayList<String>) DataCachePool.get(doManyToMany_delete_cacheKey, key );
		
		if(null==sqlOpts){
			Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
			StringBuilder sqlString = new StringBuilder();
			// 获得外键表的列，此为一对多的collectionTable
			ManyToMany manyToMany = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);
			Type fieldType = SqlType.getFields(beanClass, FieldType.TCollection).get(fieldName);
		
			Class<?> primaryBeanClass = beanClass;//主方类，默认为当前，根据多多关系检查后，修改
			String primaryBeanName = beanName;//主方类名，默认为当前，根据多多关系检查后，修改
			String primaryFieldName = fieldName;//主方多对多属性名，默认为当前，根据多多关系检查后，修改
			Type primaryFieldType = fieldType;//主方多对多属性类型，默认为当前，根据多多关系检查后，修改
			
			if(!meIsPrimaryTable){
				Class<?> subBeanClass = ReflectUtil.getListActualType(fieldType);
				primaryBeanName = subBeanClass.getSimpleName();
				primaryBeanClass = subBeanClass;
				primaryFieldName = manyToMany.mappedBy();//自己mappedby的值刚好是对方的属性
				primaryFieldType = SqlType.getFields(primaryBeanClass, FieldType.TCollection).get(primaryFieldName);
			}
			
			Map<String, ColumnType> ctColumns = obtainSKeyCollectionTableColumns(primaryBeanName, primaryFieldName, primaryFieldType);
			Iterator<String> kitr = ctColumns.keySet().iterator();
			String fkey = kitr.next();
			String pkey = kitr.next();
		
			sqlString.append("DELETE FROM ").append(dialect.quoteKey(obtainSKeyCollectionTableName(dialect, primaryBeanName, primaryFieldName)))
				.append(" \r\n\tWHERE ");
			// 添加数据库约束字段
			sqlString.append(dialect.quoteKey(pkey)).append("= :").append(pkey);
			sqlString.append(" AND ");
			sqlString.append(dialect.quoteKey(fkey)).append("= :").append(fkey);
			
			sqlOpts = new ArrayList<String>();
			sqlOpts.add(sqlString.toString());
			sqlOpts.add(pkey);
			sqlOpts.add(fkey);
			sqlOpts.trimToSize();
			DataCachePool.put(doManyToMany_delete_cacheKey, key,sqlOpts);
		}

		Object P_id = meIsPrimaryTable?obj_id:coField_id;
		Object F_id = meIsPrimaryTable?coField_id:obj_id;
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(sqlOpts.get(1), P_id);
		params.put(sqlOpts.get(2), F_id);
		return ContainerFactory.getSession().executeUpdate(sqlOpts.get(0), params);
	}
	
	private static String doManyToMany_insert_cacheKey = "RsqlUtils.doManyToMany_insert";
	/**
	 * 多对多关系处理-插入
	 * @param beanClass 
	 * @param beanName
	 * @param fieldName
	 * @param obj_id
	 * @param coField_id 
	 * @param meIsPrimaryTable
	 * @return RsqlRvo
	 * @rmx.call {@link RsqlContainer#storeFKList}
	 */
	public static DbRvo doManyToMany_insert(
			final Class<?> beanClass, final String beanName, 
			final String fieldName, 
			final Object obj_id, final Object coField_id, 
			final boolean meIsPrimaryTable) {
		
		Object key = new StringBuilder().append(beanClass.hashCode()).append(beanName.hashCode()).append(fieldName.hashCode()).append(meIsPrimaryTable);
		@SuppressWarnings("unchecked")
		ArrayList<String> sqlOpts = (ArrayList<String>) DataCachePool.get(doManyToMany_insert_cacheKey, key );
		
		if(null==sqlOpts){
			Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
			StringBuilder sqlString = new StringBuilder();
			ManyToMany manyToMany = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);
			Type fieldType = SqlType.getFields(beanClass, FieldType.TCollection).get(fieldName);
			Class<?> subBeanClass = ReflectUtil.getListActualType(fieldType);
			
			Class<?> primaryBeanClass = beanClass;//主方类，默认为当前，根据多多关系检查后，修改
			String primaryBeanName = beanName;//主方类名，默认为当前，根据多多关系检查后，修改
			String primaryFieldName = fieldName;//主方多对多属性名，默认为当前，根据多多关系检查后，修改
			Type primaryFieldType = fieldType;//主方多对多属性类型，默认为当前，根据多多关系检查后，修改
			
			if(!meIsPrimaryTable){
				primaryBeanName = subBeanClass.getSimpleName();
				primaryBeanClass = subBeanClass;
				primaryFieldName = manyToMany.mappedBy();//自己mappedby的值刚好是对方的属性
				primaryFieldType = SqlType.getFields(primaryBeanClass, FieldType.TCollection).get(primaryFieldName);
			}
			
			// 获得外键表的列，此为一对多的collectionTable
			Map<String, ColumnType> ctColumns = obtainSKeyCollectionTableColumns(primaryBeanName, primaryFieldName, primaryFieldType);
			Iterator<String> kitr = ctColumns.keySet().iterator();
			String fkey = kitr.next();
			String pkey = kitr.next();
			
			sqlString.append("INSERT INTO ")
			.append(dialect.quoteKey(obtainSKeyCollectionTableName(dialect, primaryBeanName, primaryFieldName)))
			.append(" (\r\n\t");
			// 添加属性外键(F_)在前面,主键在后面(P_)
			sqlString.append(dialect.quoteKey(SYS_id)).append(",");
			sqlString.append(dialect.quoteKey(fkey)).append(",");
			sqlString.append(dialect.quoteKey(pkey));
			sqlString.append("\r\n)VALUES(\r\n\t");
			// 添加命名参数
			sqlString.append(":").append(SYS_id);
			sqlString.append(",:").append(fkey);
			sqlString.append(",:").append(pkey);
			sqlString.append("\r\n)");
			
			sqlOpts = new ArrayList<String>();
			sqlOpts.add(sqlString.toString());
			sqlOpts.add(pkey);
			sqlOpts.add(fkey);
			sqlOpts.trimToSize();
			DataCachePool.put(doManyToMany_insert_cacheKey, key,sqlOpts);
		}

		
		Object P_id = meIsPrimaryTable?obj_id:coField_id;
		Object F_id = meIsPrimaryTable?coField_id:obj_id;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(SYS_id, String.valueOf(System.currentTimeMillis())+(100000+Math.random()*10000));
		params.put(sqlOpts.get(1), P_id);
		params.put(sqlOpts.get(2), F_id);
		return ContainerFactory.getSession().executeUpdate(sqlOpts.get(0),params);
	}

	// public static RsqlRvo updateForManyToMany(String beanName,
	// String fieldName, Type fieldType, Object PK_id,
	// Object FK_id) {
	// RsqlRvo rvo = selectForManyToMany(beanName, fieldName, fieldType, PK_id,
	// FK_id);
	// if(rvo.getRecords()==0){
	// insertForManyToMany(beanName, fieldName, fieldType, PK_id, FK_id);
	// }else if(rvo.getRecords()>1){
	// throw new (
	// new
	// RsqlExecuteException("数据库中的多对多映射数据有异常冗余，请联系数据管理员检查！"+beanName+"_"+fieldName+"_"+fieldType+"_"+PK_id+"_"+FK_id));
	// }
	// return rvo;
	//
	// // StringBuilder sqlString = new StringBuilder();
	// // // 获得外键表的列，此为一对多的collectionTable
	// // Map<String, Integer> ctColumns = obtainSKeyCollectionTableColumns(
	// // beanName, fieldName, fieldType);
	// // Iterator<String> kitr = ctColumns.keySet().iterator();
	// // String pkey = kitr.next();
	// // String fkey = kitr.next();
	// //
	// //
	// //
	// // sqlString.append("UPDATE ").append(
	// // obtainSKeyCollectionTableName(beanName, fieldName)).append(
	// // " \r\n\tSET ");
	// // sqlString.append(pkey).append("=").append(PK_id);
	// // sqlString.append(" , ");
	// // sqlString.append(fkey).append("=").append(FK_id);
	// //
	// // sqlString.append(" \r\n\tWHERE ");
	// //
	// // RsqlCvo cvo = new RsqlCvo(sqlString.toString());
	//
	// }

//	/**
//	 * 查询多对多表中的列表
//	 * 
//	 * @param beanName
//	 * @param fieldName
//	 * @param fieldType
//	 * @param PK_id
//	 * @param FK_id
//	 *            如果为空，则查询全部。
//	 * @return 返回的结果集中，列顺序：id,PK_id,FK_id
//	 */
//	public static RsqlRvo doManyToMany_select(final String beanName, final String fieldName, final Type fieldType,
//			final Object PK_id) {
//		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
//		StringBuilder sqlString = new StringBuilder();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		// 获得外键表的列，此为一对多的collectionTable
//		Map<String, ColumnType> ctColumns = obtainSKeyCollectionTableColumns(beanName, fieldName, fieldType);
//		Iterator<String> kitr = ctColumns.keySet().iterator();
//		String fkey = kitr.next();
//		String pkey = kitr.next();
//
//		sqlString.append("SELECT ");
//		sqlString.append(dialect.quoteKey(SYS_id)).append(",").append(dialect.quoteKey(pkey)).append(",")
//				.append(dialect.quoteKey(fkey));
//		sqlString.append(" FROM ")
//				.append(dialect.quoteKey(obtainSKeyCollectionTableName(dialect, beanName, fieldName)))
//				.append(" \r\n\t WHERE ");
//		sqlString.append(dialect.quoteKey(pkey)).append("= :").append(pkey);
//		params.put(pkey, PK_id);
//
//		return ContainerFactory.getSession().executeQuery(sqlString.toString(),params);
//	}

	public static void handleSQLException(final Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("未找到此方法: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("无权访问此方法: " + ex.getMessage());
		}
		// if (ex instanceof InvocationTargetException) {
		// handleInvocationTargetException((InvocationTargetException) ex);
		// }
		// if (ex instanceof RuntimeException) {
		// throw (RuntimeException) ex;
		// }
		// handleUnexpectedException(ex);
	}

	/**
	 * 把带有:[参数名字]的sql字符串格式化成 一种是【:名字】 一种是【名字=:】 注意不要出现变量名交叉，如word=:word
	 * @rmx.summary 不管是哪一种，都会自动将包含下划线"_"的分割字符串变成首字母小写,分割单词首字母大写的变量指代字母 如:prsn_name变成prsnName
	 * @param sql
	 * @return TreeMap
	 * @rmx.call {@link RsqlUtils#createSqlStringSqlBean(DbCvo)}
	 * @rmx.call {@link SqlBean#initSqlString(String, List)}
	 */
	public static TreeMap<Integer,String> obtainNamedParamIndexs(final String sql) {
		TreeMap<Integer,String> paramsMap = new TreeMap<Integer,String>();

		Pattern p1 = Pattern.compile(NamedSqlParamRegex);
		Pattern p2 = Pattern.compile(NamedSqlEquationRegex);
		Matcher m1 = p1.matcher(sql);
		Matcher m2 = p2.matcher(sql);
		int idx = 1, stt1 = 10000, stt2 = 10000;// 变量大小不能超过10000个字符
		boolean b1 = m1.find();
		boolean b2 = m2.find();
		while (true) {
			if (!b1 && !b2)
			{
				break;// 如果:(word)和(word)=:两种形式的参数都没有则中断
			}
			if (b1)
			{
				stt1 = m1.start();// 查找下一个:(word)出现的位置
			}
			if (b2)
			{
				stt2 = m2.start();// 查找下一个(word)=:出现的位置
			}
			// 在检索到变量的同时，必须优先处理先出现的变量
			if (b1 && !b2 // 当有:(word)没有(word)=:
					|| b1 && stt1 < stt2) { // 当同时有时，:(word)在前面
				String word = m1.group(2);
				paramsMap.put(new Integer(idx++),word);
				b1 = m1.find();
				if (b1) {
					stt1 = m1.start();
				}
			} else if (b2 && !b1 // 当有(word)=:没有:(word)
					|| b2 && stt2 < stt1) { // 当同时有时，(word)=:在前面
				String word = m2.group(2);
				paramsMap.put(new Integer(idx++),word);// 还要进一步处理
				b2 = m2.find();
				if (b2) {
					stt2 = m2.start();
				}
			}
		}
		return paramsMap;
	}

	/**
	 * 把sqlString命名参数化<br>
	 * @rmx.summary sqlString支持两种参数,在此以名为name的参数说明。 <li>[name= :name]此种命名将会被解析器解析为[name =
	 * ?]注意等号与冒号之间的空格。<br> <li>[name=:]<br>
	 * 此种命名将会被解析为[name=?]<br>
	 * <b>注意第一种方法必须在等号(=)与(:)之间的空格，如果没有空格[name=:name]将被转化为[name=?name]<br>
	 * 这样式错误的</b><br>
	 * 以上两种命名方法都可以在转化为含问号的参数的同时生成一个对应的{@link SqlBeanNamedParam}<br>
	 * 生成是调用了{@link SqlBeanNamedParam#setName(String)}把上面的参数[name]填写进去。
	 * {@link RsqlUtils#obtainNamedParamIndexs(String)}可以获得参数的序号。
	 * 
	 * @see RsqlUtils#obtainNamedParamIndexs(String)
	 * @param sqlString
	 * @return String
	 * @rmx.call {@link SqlBean#initSqlString(String, List)}
	 */
	public static String obtainNamedSql(String sqlString) {
		sqlString = sqlString.replaceAll(NamedSqlParamRegexRplc, "?");// 这个必须先把
		// :namedParam换成
		// ?
		return sqlString.replaceAll(NamedSqlEquationRegexRplc, "?");// 这个必须后剩下的都是=:
		// 就把:换成?

	}

	/**
	 * 生成一对多的数据库表的必须列。 包含P_<主表名>,F_<外表名><br>
	 * @param beanName
	 * @param fieldName
	 * @param fieldType
	 * @return 返回的treeMap中FK在前面，PK在后面。
	 * @rmx.call {@link RsqlCore#createCollectionTable(Dialect, String, String, Type)}
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlUtils#doManyToMany_*(Class, String, String, Object, Object, boolean)}
	 * @rmx.call {@link RsqlUtils#queryCollectionBeans(Class, String, Object)}
	 */
	public static Map<String, ColumnType> obtainSKeyCollectionTableColumns(final String beanName, final String fieldName, final Type fieldType) {
//		ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) fieldType;
//		Type[] types = typeImpl.getActualTypeArguments();
		Type[] types = (Type[]) ReflectUtil.invokeMethod("getActualTypeArguments", fieldType);
		// 因为获取的就是Collection类型，所以一定是ParameterizedTypeImpl，且ActualTypeArguments是一个参数
		TreeMap<String, ColumnType> neededColumns = new TreeMap<String, ColumnType>();

		// 一对多，一方,一方一定是一个id 索引
		neededColumns.put("P_" + beanName, new ColumnType(Types.CHAR,50));
		// 一对多，多方
		neededColumns.put("F_" + StringHelper.getClassSimpleName(types[0]), obtainSQLType(types[0], null, null));

		return neededColumns;
	}

	/**
	 * 获取一对多关系表的名称。<br>
	 * @rmx.summary 如AuthUser表中有roles(指向AuthRole)，则表名为AuthUser_roles
	 * @param dialect
	 * @param beanName
	 * @param fieldName
	 * @return String
	 * @rmx.call {@link RsqlCore#createCollectionTable(Dialect, String, String, Type)}
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlUtils#doManyToMany_*(Class, String, String, Object, Object, boolean)}
	 * @rmx.call {@link RsqlUtils#queryCollectionBeans(Class, String, Object)}
	 */
	public static String obtainSKeyCollectionTableName(final Dialect dialect, final String beanName,
			final String fieldName) {
		StringBuilder tableName = new StringBuilder();
		tableName.append(beanName).append("_").append(fieldName);
		return tableName.toString();
	}

	/**
	 * 用于获取JavaBean应该对应的列,是基本数据<br/>
	 * @rmx.summary 并且id dataStatus标示不在其内
	 * @param beanClass
	 * @param map
	 * @return Map
	 * @rmx.call {@link RsqlUtils#createInsertSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createSelectSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createUpdateSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#createBaseTable(Dialect, String, Class)}
	 */
	public static Map<String, ColumnType> obtainSKeyColumnsBase(final Class<?> beanClass, final Map<String, Type> map) {
		HashMap<String, ColumnType> columns = new HashMap<String, ColumnType>();

		// ****************添加bean中的基本数据属性，定义为数据库中的字段
		Set<String> fieldNames = map.keySet();
		// 每个表必须有id,且id必须为唯一并为数字型

		for (String fieldName : fieldNames) {
			columns.put(fieldName, obtainSQLType(map.get(fieldName), fieldName, beanClass));
		}
		for (String fieldName : obtainSKeyColumnsSys().keySet()) {
			columns.remove(fieldName);
		}

		return columns;

	}
	/**
	 * 获取非系统的基础数据列。主要用于序列化对象。
	 * @param beanClass
	 * @return Map
	 * @rmx.call {@link RsqlContainer#list(Modelable)}
	 */
	public static Map<String, Method> obtainListGetters(final Class<?> beanClass) {
		Map<String, Method> map = GettersWithOutSysColumnForList.get(beanClass);
		if(null==map){
			map = new ReadOnlyMap<String, Method>();
			Map<String, Method> bgetters = SqlType.getGetters(beanClass, FieldType.TBase);
			Map<String, Method> ogetters = SqlType.getGetters(beanClass, FieldType.TObject);
			map.putAll(bgetters);
			map.putAll(ogetters);
			for (String fieldName : obtainSKeyColumnsSys().keySet()) {
				map.remove(fieldName);
			}
			GettersWithOutSysColumnForList.put(beanClass, map);
		}
		return map;
	}
	
	/**
	 * 用于获取JavaBean应该对应的列,是对象数据，即数据的一对一关系<br/>
	 *@rmx.summary  并且id dataStatus标示不在其内
	 * @param objectFields
	 * @return Map
	 * @rmx.call {@link RsqlUtils#createInsertSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createSelectSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createUpdateSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#createBaseTable(Dialect, String, Class)}
	 */
	public static Map<String, ColumnType> obtainSKeyColumnsObject(final Map<String, Type> objectFields) {
		HashMap<String, ColumnType> columns = new HashMap<String, ColumnType>();
		// *****************添加bean中的表连接属性，定义为int，并随后建立表外键连接
		Set<String> fieldNames = objectFields.keySet();
		for (String fieldName : fieldNames) {
			columns.put(fieldName, new ColumnType(Types.CHAR, 50));
		}

		for (String fieldName : obtainSKeyColumnsSys().keySet()) {
			columns.remove(fieldName);
		}
		return columns;
	}

	/**
	 * 用于获取JavaBean应该对应的列,此为系统保留列<br/>
	 * @rmx.summary 目前仅包含id dataStatus
	 * @return Map
	 * @rmx.call {@link RsqlUtils#createSelectSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#obtainListGetters(Class)}
	 * @rmx.call {@link RsqlUtils#obtainSKeyColumnsBase(Class, Map)}
	 * @rmx.call {@link RsqlUtils#obtainSKeyColumnsObject(Map)}
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 */
	static public Map<String, ColumnType> obtainSKeyColumnsSys() {
		return SysColumns;
	}

	//	static public Map<String, Integer> obtainSKeyMapTableColumns(
	//			final String beanName, final String fieldName, final Type fieldType) {
	//		ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) fieldType;
	//		Type[] types = typeImpl.getActualTypeArguments();
	//
	//		// 因为获取的就是Map类型，所以一定是ParameterizedTypeImpl，且ActualTypeArguments是两个参数
	//		Map<String, Integer> neededColumns = new HashMap<String, Integer>();
	//
	//		neededColumns.put(beanName, Types.CHAR);
	//
	//		neededColumns.put("key", obtainSQLType(types[0], null, null));
	//		neededColumns.put("value", obtainSQLType(types[1], null, null));
	//
	//		return neededColumns;
	//	}

	//	static public String obtainSKeyMapTableName(final String beanName,
	//			final String fieldName) {
	//		StringBuilder tableName = new StringBuilder();
	//		tableName.append(beanName).append("_").append(fieldName);
	//		return tableName.toString();
	//	}

	/**
	 * 获取sql查询时的排序order by条件
	 * @param cvo
	 * @return String
	 * @rmx.call {@link RsqlMonitorAspect#doAroundRemexSqlExecute(org.aspectj.lang.ProceedingJoinPoint)}
	 * @rmx.call {@link RsqlDao#executeQuery(DbCvo)}
	 */
	public static <T extends Modelable> String obtainSQLOrder(final DbCvo<T> cvo) {
		if(SqlOper.sql.equals(cvo.getOper()))//sql语句不能添加order。
			return "";
		
		List<SqlBeanOrder> orders = cvo.getOrders();
		if(null == orders)
			return "";
		
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		StringBuilder result = new StringBuilder(" ORDER BY ");
		for(SqlBeanOrder order:orders){
			if (null != orders && order.isSortable()) {
				result.append(dialect.quoteKey(order.getSidx())).append(" ").append(order.getSord()).append(", ");//
			}
			
		}
		result.append(dialect.quoteKey(SYS_id)).append(" DESC");
		
		return result.toString();
	}

	/**
	 * 此函数用于提取基本表中的数据类型。<br>
	 *@rmx.summary  数据类型包括ORMBeans，Enum，及序列化的对象三种<br>
	 * TCollection、TMap不属于本函数范围
	 * @param type
	 * @return ColumnType
	 * @rmx.call {@link RsqlUtils#obtainSKeyCollectionTableColumns(String, String, Type)}
	 * @rmx.call {@link RsqlUtils#obtainSKeyColumnsBase(Class, Map)}
	 */
	public static ColumnType obtainSQLType(final Type type, final String fieldName, final Class<?> beanClass) {
		// 如果是通过RemexSqlTypeAnnotation声明
		if (null != beanClass) {
			try {
				Column at = ReflectUtil.getAnnotation(beanClass, fieldName, Column.class);
				if (at != null) {
					return new ColumnType(at);
				}
			} catch (Exception e) {
			}
		}

		// SqlTypes定义了一些基本类型
		ColumnType sqlType = SqlTypes.get(type);
		// null则表明不是TBase
		// 是ormBeans对象，对象都保存id索引(Enum例外)，所以都是Types.INTEGER
		if (null != sqlType) {
			return sqlType;
		}
		if (null == sqlType && SqlType.isTObject(type)) {
			return new ColumnType(Types.CHAR, 50);
			// 对象型有普通对象和enum，enum保存为Char
		} else if (Enum.class.isAssignableFrom(ReflectUtil.obtainClass(type))) {
			return new ColumnType(Types.CHAR, 100);
		} else {// 普通CLass的object对象
			return new ColumnType(Types.JAVA_OBJECT, 100);
		}
	}

	/**
	 * 根据neamClass和field查询一对多或多对多映射的子集合collection
	 * @param beanClass
	 * @param fieldName
	 * @param beanId
	 * @return DbRvo
	 * @rmx.call {@link RsqlCore#intercept(Object obj, Method method, Object[] args, MethodProxy proxy)}
	 * @rmx.call {@link RsqlContainer#storeFKList}
	 */
	public static DbRvo queryCollectionBeans(final Class<?> beanClass, final String fieldName, final Object beanId) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		String beanName = StringHelper.getClassSimpleName(beanClass);
		Type fieldType = SqlType.getFields(beanClass, FieldType.TCollection).get(fieldName);
		StringBuilder sqlString = new StringBuilder();
		Class<?> subBeanClass = ReflectUtil.getListActualType(fieldType);
		OneToMany oneToMany = ReflectUtil.getAnnotation(beanClass, fieldName, OneToMany.class);
		ManyToMany manyToMany = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);
		String primaryBeanName = beanName;//主方类名，默认为当前，根据多多关系检查后，修改
		Class<?> primaryBeanClass = beanClass;//主方类，默认为当前，根据多多关系检查后，修改
		String primaryFieldName = fieldName;//主方多对多属性名，默认为当前，根据多多关系检查后，修改
		Type primaryFieldType = fieldType;//主方多对多属性类型，默认为当前，根据多多关系检查后，修改
		
		/**********************判断是多对多还是一对多***********************************/
		// 如果是一对多，并且指定了mappedBy
		boolean isOnetoMany = null != oneToMany ;
		if(isOnetoMany)Assert.isTrue(null!=oneToMany.mappedBy(),"一对多必须制定多方维护的属性字段",RsqlInitException.class);
		
		// 如果是多对多，不是一对多，则检查多对多关系
		boolean meIsManyToManyPrimary = true;
		if (!isOnetoMany) {
			String tmb = null,mb=null;//多对多中对方属性名称
			if (null != manyToMany) {// 显示声明多对多，双方共同维护。
				// 对方表的集合字段
				Map<String, Method> tcGetters = SqlType.getGetters(subBeanClass, FieldType.TCollection);

				// mb必须主从双方都配置，主方还需配置targetEntity。从方无需配置targetEntity属性。
				mb = manyToMany.mappedBy();

				Assert.notNull(mb, new StringBuilder("显式指定为双方维护的多对多映射中本类").append(beanClass.getName()).append("没有指定对方类多对多属性:").append(mb).toString(), RsqlInitException.class);
				Assert.isTrue(tcGetters.containsKey(mb), new StringBuilder(" 显式指定为双方维护的多对多映射中对方类").append(subBeanClass.getName()).append("不存在该多对多属性:").append(mb).toString(), RsqlInitException.class);

				// 获取对方类多对多声明
				ManyToMany tMtm = ReflectUtil.getAnnotation(subBeanClass, mb, ManyToMany.class);
				Assert.notNull(tMtm,  "显示声明多对多时，需要双方指定ManyToMany声明，此处对方类的ManyToMany声明为空！请更正！", RsqlInitException.class);
				tmb = tMtm.mappedBy();// 对方类的多对多属性

				Assert.notNull(tmb, new StringBuilder("显式指定为双方维护的多对多映射中对方类").append(subBeanClass.getName()).append("没有指定本类的多对多属性:").append(mb).toString(), RsqlInitException.class);

				Class<?> te = manyToMany.targetEntity(); // 检查主从关系 本类
				Class<?> tte = tMtm.targetEntity(); // 对方类

				Assert.isTrue((null == te && null == tte) || (null != te && null != tte), new StringBuilder("显式指定为双方维护的多对多映射中，ManyToMany声明不能同时设置或者同时为空！设置targetEntity的为主维护方。").append(mb).toString(),
						RsqlInitException.class);

				meIsManyToManyPrimary = ! "void".equals(te.toString());
			} else {
				// 如果不设置manytoMany，则定义为本表本字段为单方维护的多对多，本属性为主维护方。
				// 注意，如果将来需要添加双方维护的多对多，本类的本属性必须指定为主维护方，否则将重建中间表。
				meIsManyToManyPrimary = true;
			}
			
			//如果不是主表，则修改对应的表名和字段。
			if(!meIsManyToManyPrimary){
				primaryBeanName = subBeanClass.getSimpleName();
				primaryBeanClass = subBeanClass;
				primaryFieldName = mb;//自己mappedby的值刚好是对方的属性
				primaryFieldType = SqlType.getFields(primaryBeanClass, FieldType.TCollection).get(primaryFieldName);
			}
		}// 多对多 end
		
		
		
		// 获得外键表的列，此为一对多的collectionTable
		Map<String, ColumnType> ctColumns = obtainSKeyCollectionTableColumns(primaryBeanName, primaryFieldName, primaryFieldType);
		Iterator<Entry<String, ColumnType>> ctkeys = ctColumns.entrySet().iterator();
		String F_column = ctkeys.next().getKey();
		String P_column = ctkeys.next().getKey();
		
		String F_table = StringHelper.getClassSimpleName(subBeanClass);
		String F_table_alias = StringHelper.getAbbreviation(subBeanClass);
		sqlString.append("SELECT ");

		// for(String f:RsqlUtils.obtainSKeyColumnsSys().keySet()){
		// sqlString.append(F_table).append(".").append(f).append(",");
		// }
		for (String f : SqlType.getGetters(subBeanClass, FieldType.TBase).keySet()) {
			sqlString.append(dialect.aliasFullName(F_table_alias, f, f)).append(",");
		}
		for (String f : SqlType.getGetters(subBeanClass, FieldType.TObject).keySet()) {
			sqlString.append(dialect.aliasFullName(F_table_alias, f, f)).append(",");
		}
		
		// 代码与代码名称映射功能添加。 如果有CodeRef，sql末尾会添加 `,\t\r\n`需要删除,否则只有一个，号要删除
		boolean hasCodeRef = dealWithCodeRef(subBeanClass, F_table_alias, sqlString);
		sqlString.delete(sqlString.length() - (hasCodeRef?4:1),sqlString.length());

		sqlString.append(" FROM ").append(dialect.quoteKey(F_table)).append(" ").append(F_table_alias);

		if(isOnetoMany ){
				sqlString.append(" WHERE 1=1 AND ").append(dialect.quoteFullName(F_table_alias, oneToMany.mappedBy())).append("= :RMX_beanId");
		}else {
			String search_column = meIsManyToManyPrimary?P_column:F_column;
			String join_column = meIsManyToManyPrimary?F_column:P_column;
			//多对多从方查询
			sqlString.append(" LEFT JOIN ").append(dialect.quoteKey(obtainSKeyCollectionTableName(dialect, primaryBeanName, primaryFieldName)))
			.append(" ON ").append(dialect.quoteKey(join_column)).append("=")
			.append(dialect.quoteFullName(F_table_alias, SYS_id));
			sqlString.append(" WHERE 1=1 AND ").append(dialect.quoteKey(search_column)).append("= :RMX_beanId");
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("RMX_beanId", beanId);
		return ContainerFactory.getSession().executeQuery(sqlString.toString(), params);
		// return RsqlDao.executeQuery(sqlString.toString(), params );
	}

	
//	public static Object queryCurMaxId(final Class<?> beanClass){
//		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
//		String sql = "SELECT MAX("+dialect.quoteKey(SYS_id)+") "+dialect.quoteKey("MAXID")+" FROM "+dialect.quoteKey(StringHelper.getClassSimpleName(beanClass));
//		return RsqlDao.executeQuery(sql).getQueryResult().getCell(0, 0);
//	}

//	/**
//	 * 查询系统表中的值
//	 * @param key
//	 * @return
//	 */
//	public static Object querySystemKey(final String key) {
//		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
//		String sql = "SELECT " + dialect.quoteKey(SYS_key) + "," + dialect.quoteKey(SYS_value) + " FROM "
//				+ dialect.quoteKey(SYS_tableName) + " WHERE " + dialect.quoteKey(SYS_key) + "="
//				+ dialect.quoteAsString(key);
//		return RsqlDao.executeQuery(sql).getQueryResult().getCell(0, 1);
//	}
	
	/**
	 * 
	 * @param dtCols
	 * @param baseColumns
	 * @return Map
	 * @rmx.call {@link RsqlUtils#createInsertSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createSelectSqlBean(DbCvo)}
	 * @rmx.call {@link RsqlUtils#createUpdateSqlBean(DbCvo)}
	 */
	public static Map<String, ColumnType> restrictSKeyColumns(final String dtCols, final Map<String, ColumnType> baseColumns) {
		Map<String, ColumnType> cols = null;
		if (null == dtCols) {
			cols = baseColumns;
		} else {
			cols = new HashMap<String, ColumnType>();
			for (String col : dtCols.split("[;,]")) {
				ColumnType i = baseColumns.get(col);
				if (null != i) {
					cols.put(col, i);
				} else {
					if(isDebug)logger.debug("您提交的查询列" + col + "不在需要约束的数据列中。");
				}
			}
		}

		return cols;
	}
	// TODO 补充文字解释
	private static void extendColumn(final Class<?> fieldClass,
			final StringBuilder part_jion_sb, final StringBuilder part_column_sb,
			final String tb_alias, final TableAliasIndex taidx, final String objectColumn,
			final String pathHead, final ArrayList<String> ecList, final String tableName,
			final WherePart part_where, final ArrayList<SqlBeanNamedParam> namedParams) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		// 深度递归调用的列保存在此处
		HashMap<String, ArrayList<String>> ecListNexts = new HashMap<String, ArrayList<String>>(5);
		for (String extPath : ecList) {
			if (extPath.indexOf('.') < 0
					&& !SYS_name.equals(extPath)
					&& !SYS_id.equals(extPath)) {
				part_column_sb.append(dialect.aliasFullName(tb_alias, extPath, pathHead+"."+extPath)).append(",\r\n\t");
			} else if (extPath.indexOf('.') > 0) {// 其他的需要深度递归完成
				int idx = extPath.indexOf('.');
				String extColumnName = extPath.substring(0, idx);
				ArrayList<String> ecListNew = ecListNexts.get(extColumnName);
				if (null == ecListNew) {
					ecListNew = new ArrayList<String>();
					ecListNexts.put(extColumnName, ecListNew);
				}
				ecListNew.add(extPath.substring(idx + 1));
			}
		}
		// 递归完成哪些深度引用
		for (String exPath : ecListNexts.keySet()) {
			Class<?> fieldClassCur = (Class<?>) SqlType.getFields(fieldClass,FieldType.TObject).get(exPath);
			String jioned_Tb = StringHelper.getClassSimpleName(fieldClassCur);
			String jioned_Tb_alias = StringHelper.getAbbreviation(fieldClass)  + taidx.index++;
			part_jion_sb.append("\r\nLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias));
			part_jion_sb.append("\r\n\tON ")
			.append(dialect.quoteFullName(jioned_Tb_alias, SYS_id))
			.append("=")
			.append(dialect.quoteFullName(tb_alias, exPath))
			.append(" ");

			String fullPath = pathHead + "." + exPath;

			part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, SYS_id, fullPath+"."+SYS_id)).append(",\r\n\t");
			part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, SYS_name, fullPath+"."+SYS_name)).append(",\r\n\t");

			WherePart wp = new WherePart(part_where.wherePart);
			extendColumn(fieldClassCur, part_jion_sb, part_column_sb,
					jioned_Tb_alias, taidx, jioned_Tb, fullPath, ecListNexts
					.get(exPath), tableName, wp, namedParams);

			// TODO 修正扩展列的where查询条件比如job.name job.department中的job需要对应表的alias名字
			for (SqlBeanNamedParam sqlBeanNamedParam : namedParams) {
				String wh_tag = tableName + "\\." + fullPath + "\\.";
				if (sqlBeanNamedParam.getName().startsWith(fullPath + ".")) {
					part_where.wherePart = part_where.wherePart.replaceAll(
							wh_tag, jioned_Tb_alias + ".");
				}
			}
		}
	}
	
	/**
	 * @param cvo
	 * @return SqlBean
	 * @rmx.call {@link SqlBean#getInstance(DbCvo)}
	 */
	public static <T extends Modelable> SqlBean<T> createDeleteSqlBean(final DbCvo<T> cvo) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();

		SqlBean<T> sqlBean = new SqlBean<T>(cvo);
//		sqlBean.setTableName(cvo.getBeanName());


		ArrayList<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();

		StringBuilder sqlString = new StringBuilder();
		sqlString.append("DELETE FROM ").append(dialect.openQuote()).append(cvo.getBeanName()).append(
				dialect.closeQuote()+" WHERE " + dialect.quoteKey(SYS_id) + "= :"+SYS_id);
		// 默认索引都是整型
		namedParams.add(new SqlBeanNamedParam(-1, SYS_id, Types.CHAR, null));

		// 删除多余空间
		namedParams.trimToSize();
		sqlBean.init(cvo, sqlString.toString(), namedParams);
		return sqlBean;
	}
	
	/**
	 * @param cvo
	 * @return SqlBean
	 * @rmx.call {@link SqlBean#getInstance(DbCvo)}
	 */
	public static <T extends Modelable> SqlBean<T> createInsertSqlBean(final DbCvo<T> cvo) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		SqlBean<T> sqlBean = new SqlBean<T>(cvo);

		String dataOption_ = cvo.getDataType();
		String dataOption = null != dataOption_ ? dataOption_ : "";
		String dtCols = cvo.getDataColumns();

		String beanName = cvo.getBeanName();
//		sqlBean.setTableName(beanName);
		Class<?> beanClass=sqlBean.getBeanClass();
		String tableName = beanName;

		ArrayList<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();

		// 定义对象类型数据的查询sql
		String prefix = "INSERT INTO "+dialect.openQuote() + tableName+dialect.closeQuote();
		String part_column;
		String part_col2val = ")\r\n VALUES \r\n\t(";
		String part_value;
		String suffix = "";
		StringBuilder part_column_sb = new StringBuilder();
		StringBuilder part_value_sb = new StringBuilder();

		StringBuilder sqlString = new StringBuilder();

		// 定义完整baseData数据insert语句(id为默认自动索引)

		part_column_sb.append("\r\n\t(");
		// 插入系统配置列
		for (String column : SysColumns.keySet()) {
			part_column_sb.append(dialect.quoteKey(column)).append(",");
			part_value_sb.append(":").append(column).append(",");
			namedParams.add(new SqlBeanNamedParam(-1, column, SysColumns.get(column).type, null));
		}

		// 插入基础数据是默认选项
		Map<String, Type> baseFields = SqlType.getFields(beanClass, FieldType.TBase);
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, baseFields);
		Map<String, ColumnType> baseCols = RsqlUtils.restrictSKeyColumns(dtCols, baseColumns);
		for (String column : baseCols.keySet()) {
			part_column_sb.append(dialect.quoteKey(column)).append(",");
			part_value_sb.append(":").append(column).append(",");
			namedParams.add(new SqlBeanNamedParam(-1, column, baseCols.get(column).type, null));
		}

		if (dataOption.contains(DT_object)) {
			Map<String, Type> objectFields = SqlType.getFields(beanClass, FieldType.TObject);
			Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(objectFields);
			Map<String, ColumnType> objectCols = RsqlUtils.restrictSKeyColumns(dtCols, objectColumns);
			for (String column : objectCols.keySet()) {
				part_column_sb.append(dialect.quoteKey(column)).append(",");
				part_value_sb.append(":").append(column).append(",");
				namedParams.add(new SqlBeanNamedParam(-1, column, objectCols.get(column).type, null));
			}
		}

		part_value_sb.append(")\r\n");

		part_column = part_column_sb.deleteCharAt(part_column_sb.length() - 1)
				.toString();// 删除最后一个逗号
		part_value = part_value_sb.deleteCharAt(part_value_sb.length() - 4)
				.toString();// 删除最后一个逗号

		sqlString.append(prefix).append(part_column).append(part_col2val)
		.append(part_value).append(suffix);

		// 删除多余空间
		namedParams.trimToSize();
		sqlBean.init(cvo, sqlString.toString(), namedParams);
		return sqlBean;
	}

	/**
	 * 生成执行查询sql语句的bean dataOption 用于控制数据类型的
	 * @param cvo
	 * @return SqlBean
	 * @throws Exception
	 * @rmx.call {@link SqlBean#getInstance(DbCvo)}
	 */
	public static <T extends Modelable> SqlBean<T> createSelectSqlBean(final DbCvo<T> cvo) {
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		SqlBean<T> sqlBean = new SqlBean<T>(cvo);

		String dataOption_ = cvo.getDataType();
		String dataOption = null != dataOption_ ? dataOption_ : "";
		String dtCols = cvo.getDataColumns();
		boolean hasDcs=!Judgment.nullOrBlank(dtCols);//dcs已经在Store(T,DbCvo)处理了。
		if(hasDcs){dtCols=cvo.getDataColumns()+";";};
		String fb = cvo.getForeignBean();

		String beanName = cvo.getBeanName();
		Class<?> beanClass=sqlBean.getBeanClass();
//		String tableName = beanName;
		String tableAliasName = "T";
//		sqlBean.setTableName(tableAliasName);

		ArrayList<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();

		// 定义对象类型数据的查询sql
		String prefix = "SELECT \r\n\t";
		String part_column;
		String part_from = "\r\nFROM "+dialect.quoteKey(beanName)+" "+tableAliasName;
		String part_jion;
		String part_where = "\r\n  " + cvo.getSqlBeanWhere().toSQL(tableAliasName, namedParams, 0);
		// 动态排序必须放到dao
		// String part_order = "\r\n"+obtainSQLOrder(Option.getOrder());
		StringBuilder part_column_sb = new StringBuilder();
		StringBuilder part_jion_sb = new StringBuilder();
		StringBuilder sqlString = new StringBuilder();

		Map<String, ColumnType> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
		for (String sysColumn : sysColumns.keySet()) {
			part_column_sb.append(dialect.aliasFullName(tableAliasName, sysColumn, sysColumn)).append(",\r\n\t");
		}

		if (dataOption.contains(DT_base)) {
			Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, SqlType.getFields(beanClass,FieldType.TBase));
			Map<String, ColumnType> cols = RsqlUtils.restrictSKeyColumns(dtCols,baseColumns);
			for (String baseColumn : cols.keySet()) {
				part_column_sb.append(dialect.aliasFullName(tableAliasName, baseColumn, baseColumn)).append(",\r\n\t");
			}
		}


		//保存外键表信息
		//FbTableMap fbTableMap = new FbTableMap();
		// 读取属性列及属性列的扩展列
		// 读取扩展数据
		// 该数据必须是一对一外键引用的
		// 该数据必须通过[.]类似于java的属性那样应用,目前仅支持一级引用
		boolean ext = dataOption.contains(DT_objectExt);
		// HashMap<String,String[]> extColumnNames = new HashMap<String,
		// String[]>(5);
		HashMap<String, ArrayList<String>> extColumnPaths = new HashMap<String, ArrayList<String>>(5);
		if (ext) {
			ext = false;// 进一步检查
			String extColumnString = cvo.getExtColumn();
			if (null != extColumnString) {
				String[] extColumns = extColumnString.split(";");
				ext = true;// 所有条件合格
				// TODO 该数据必须通过[.]类似于java的属性那样应用,目前仅支持一级引用 ，故就数组就两个值
				for (String extColumn : extColumns) {
					// String[] refPath = extColumn.split("\\.");
					// if(refPath.length==2){
					// extColumnNames.put(refPath[0], refPath);
					//
					// }
					int idx = extColumn.indexOf('.');
					String extColumnName = extColumn.substring(0, idx);
					ArrayList<String> ecList = extColumnPaths
							.get(extColumnName);
					if (null == ecList) {
						ecList = new ArrayList<String>();
						extColumnPaths.put(extColumnName, ecList);
					}
					ecList.add(extColumn.substring(idx + 1));
				}
			}
		}
		// 读取对象列id name
		if (dataOption.contains(DT_object)) {
			Map<String, Type> objectFields = SqlType.getFields(beanClass,FieldType.TObject);
			List<String> objectColumns = new ArrayList<String>();
			objectColumns.addAll(RsqlUtils.obtainSKeyColumnsObject(objectFields).keySet());
			Map<String, FbColumn> fbColumns = new HashMap<String, FbColumn>();
			// 如果开启了外联
			// 如Staff中fb=”CatrOncdCertificate,oncdCertificate,CatrStaff.laborer= CatrOncdCertificate.wife;”
			// 格式为：外联表名，映射过来的虚拟属性,外联连接条件(需要使用的外联表属性|需要使用的外联表属性|需要使用的外联表属性)
			if (null != fb) {
				String[] fbs = fb.split(";");
				for (String fbCur : fbs) {
					String[] fbCurParts = fbCur.split(",");
					objectColumns.add(fbCurParts[1]);// 第二个才是虚拟列名。
					fbColumns.put(fbCurParts[1], new FbColumn(fbCurParts[0],	fbCurParts[1], fbCurParts[2]));
				}
			}

			TableAliasIndex taidx = new TableAliasIndex();// 用于控制数据库虚拟表明的序号。
			for (String objectColumn : objectColumns) {
				Class<?> fieldClass;
				String jioned_Tb, jioned_Tb_alias;
				if (fbColumns.containsKey(objectColumn)) {// 手动外联表
					FbColumn fbColumn = fbColumns.get(objectColumn);
					fieldClass = spaceConfig.getOrmBeanClass(fbColumn.fBeanName);
					jioned_Tb = StringHelper.getClassSimpleName(fieldClass);
					jioned_Tb_alias = StringHelper.getAbbreviation(fieldClass) + taidx.index++;
					part_jion_sb.append("\r\nLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias));
					part_jion_sb.append("\r\n\tON 1=1 ");
					for (FbColumnOn on : fbColumn.ons) {
						part_jion_sb.append(on.joinOper).append(" ")
						.append(dialect.quoteFullName(jioned_Tb_alias, on.outerTableField))
						.append("=")
						.append(dialect.quoteFullName(tableAliasName, on.innerTableField))
						.append(" ");
					}

				} else {// 自动外联表
					// LHY 2015-2-17 如果已经指定了需要查询的列，则进行相关约束
					if(hasDcs && dtCols.indexOf(objectColumn+";")<0){
						continue;
					}
					
					fieldClass = (Class<?>) objectFields.get(objectColumn);
					jioned_Tb = StringHelper.getClassSimpleName(fieldClass);
					jioned_Tb_alias = StringHelper.getAbbreviation(fieldClass) + taidx.index++;
					part_jion_sb.append("\r\nLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias));
					part_jion_sb.append("\r\n\tON ");
					part_jion_sb.append(dialect.quoteFullName(jioned_Tb_alias, SYS_id))
					.append("=")
					.append(dialect.quoteFullName(tableAliasName, objectColumn))
					.append(" ");
				}

				// 外联的基本列 id name
				part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, SYS_id, objectColumn+"."+SYS_id)).append(",\r\n\t");
				part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, SYS_name, objectColumn+"."+SYS_name)).append(",\r\n\t");

				// 如果需要添加扩展列则再次添加
				if (ext && extColumnPaths.containsKey(objectColumn)) {
					WherePart wp = new WherePart(part_where);
					extendColumn(fieldClass, part_jion_sb, part_column_sb,
							jioned_Tb_alias, taidx, objectColumn, objectColumn,
							extColumnPaths.get(objectColumn), tableAliasName, wp,
							namedParams);
					part_where = wp.wherePart;
				}

				// 修正扩展列的where查询条件比如job.name job.department中的job需要对应表的alias名字
				for (SqlBeanNamedParam sqlBeanNamedParam : namedParams) {
					//LHY 14-5-1  发现的bug，此处应该用表的别名。
					// dialect.quoteKey(beanName) -》dialect.quoteKey(tableAliasName)
					String wh_tag = dialect.quoteKey(tableAliasName) + "\\." + dialect.openQuote()+objectColumn + "\\.";
					if (sqlBeanNamedParam.getName().startsWith(
							objectColumn + ".")) {
						part_where = part_where.replaceAll(wh_tag,
								dialect.quoteKey(jioned_Tb_alias) + "."+dialect.closeQuote());
					}
				}
			}

		}
		
		// 代码与代码名称映射功能添加。 
		dealWithCodeRef(beanClass, tableAliasName, part_column_sb);
		
		//		if (dataOption.contains(DT_collection)) {
		//		}
		//		if (dataOption.contains(DT_map)) {
		//		}

		part_column = part_column_sb.delete(part_column_sb.length() - 4,part_column_sb.length()).toString();
		part_jion = part_jion_sb.toString();

		sqlString.append(prefix).append(part_column).append(part_from).append(
				part_jion).append(part_where)
				// .append(part_order)
				;

		// 删除多余空间
		namedParams.trimToSize();

		sqlBean.init(cvo, sqlString.toString(), namedParams);
		return sqlBean;
	}
	
	@SuppressWarnings("unused")
	private static boolean dealWithCodeRef(Class<?> beanClass, String tableAliasName, StringBuilder part_column_sb){
		// 代码与代码名称映射功能添加。 
		// 此处是【值映射】
		// 支持三种方式:
		//1.表中字段的代码映射功能来源于DataDic系统数据字典表
		//2.表中字段的代码映射功能来源于本地表的某两个字段，一个字段用于保存于本表本字段中，另一个用于前端页面显示
		//3.表中字段的代码映射功能来源于每个服务提供的查询 TODO 此功能很底层，暂不实现
		if(true){ // 开启代码映射功能，默认开启
			//获取需要代码映射的字段
			Map<String, ColumnType> RefColumns = RsqlUtils.obtainSKeyColumnsRef(beanClass, SqlType.getFields(beanClass,FieldType.TBase)); // 值映射在数据库中都是保存于base列中的

			for(String fieldName:RefColumns.keySet()){
				//从该字段中判断是以上那种类型
				ColumnType ct = RefColumns.get(fieldName);
				if(EditType.CodeRef == ct.editType){
					//通过预读取采用encode函数减少
					StringBuilder sb = obtainRsqlDecodePart(tableAliasName, fieldName, ct);
					part_column_sb.append(sb).append(",\r\n\t");
				}
			}
			return RefColumns.size()>0;
		}else{
			return false;
		}
	}
	
	private static Map<String, ColumnType> obtainSKeyColumnsRef(
			Class<?> beanClass, Map<String, Type> fields) {
		Map<String, Type> baseFields = SqlType.getFields(beanClass,FieldType.TBase);
		Map<String, ColumnType> rets = new HashMap<String, ColumnType>();
		for(String field:baseFields.keySet()){
			Element ele = ReflectUtil.getAnnotation(beanClass, field, Element.class);
			if(null!=ele && EditType.CodeRef == ele.edittype()){
				ColumnType ct = obtainSQLType(baseFields.get(field), field, beanClass);
				ct.editType = EditType.CodeRef;
				ct.codeRefBean = ele.CodeRefBean();
				ct.codeRefCodeColumn = ele.CodeRefCodeColumn();
				ct.codeRefCodeType = Judgment.nullOrBlank(ele.CodeRefCodeType())?field:ele.CodeRefCodeType();
				ct.codeRefDescColumn = ele.CodeRefDescColumn();
				ct.codeRefTypeColumn = ele.CodeRefTypeColumn();
				ct.codeRefFilters = ele.CodeRefFilters();
				rets.put(field, ct );
			}
		}
		
		return rets;
	}
	/**
	 * @param orgnSql
	 * @return
	 */
	public static String replaceRefCode(String orgnSql){
		// 条件匹配  表.列,别名,匹配表,匹配表条件列,条件,（条件咧=条件）,被匹配列,被匹配名
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		final char t = dialect.openQuote() ;
		final  String RefCodeRegx = "RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		final  String RefCodeRegx5 = "RefCode\\(substr\\((\\w+)\\."+t+"(\\w+)"+t+",0,2\\),"+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		final  String RefCodeRegx6 = "RefCode\\(substr\\((\\w+)\\."+t+"(\\w+)"+t+",0,2\\),"+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 无条件匹配     表.列,别名,匹配表,被匹配列,被匹配名
		final String RefCodeRegx2="RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 条件无别名匹配     表.列,匹配表,匹配表条件列,条件,（条件咧=条件）,被匹配列,被匹配名
		final  String RefCodeRegx3 = "RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 无条件无别名匹配    表.列 , 匹配表,被匹配列,被匹配名
		final String RefCodeRegx4="RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 省 查到市
		MatchResult rs;
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String refTypeColumn = rs.group(5);
			String refCodeType = rs.group(6);
			String codeColumn = rs.group(7);
			String descColumn = rs.group(8);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx5, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String refTypeColumn = rs.group(5);
			String refCodeType = rs.group(6);
			String codeColumn = rs.group(7);
			String descColumn = rs.group(8);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,true);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx5, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx2, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String codeColumn = rs.group(5);
			String descColumn = rs.group(6);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,null,null,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx2,decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx3, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String refTypeColumn = rs.group(4);
			String refCodeType = rs.group(5);
			String codeColumn = rs.group(6);
			String descColumn = rs.group(7);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx3, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx6, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String refTypeColumn = rs.group(4);
			String refCodeType = rs.group(5);
			String codeColumn = rs.group(6);
			String descColumn = rs.group(7);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,true);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx6, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx4, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String codeColumn = rs.group(4);
			String descColumn = rs.group(5);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,null,null,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx4, decodeSqlPart.toString(),1);
		}
		return orgnSql;
	}
	public static void main(String[] args) {
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		final char t = dialect.openQuote() ;
		String sql= "RefCode(substr(atin."+t+"cityCode"+t+",0,2),"+t+"aaa"+t+","+t+"CityCode"+t+","+t+"code"+t+","+t+"110100"+t+","+t+"name"+t+","+t+"code"+t+")";
		replaceRefCode(sql);
	}

	private static StringBuilder obtainDecodeSqlPart(
			final String fieldTable,  //表明
			final String fieldName,		// 表列
			final String displayName,	// 表别名
			final String codeTableName,	// 关联表名
			final String refTypeColumn, // 关联列 的筛选    用于筛选
			final String refCodeType,	// 筛选的值
			final String codeColumn,	// 关联列
			final String descColumn,		// 关联列的值
			final boolean hasSubStr //需要decode的sql中存在substr函数
			){
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		@SuppressWarnings("unchecked")
		Class<Modelable> clazz = (Class<Modelable>) spaceConfig.getOrmBeanClass(codeTableName);
		DbRvo rvo = ContainerFactory.getSession().query(new DbCvo<Modelable>(clazz){
			private static final long serialVersionUID = -1322957928866739488L;
			@Override
			public void initRules(Modelable t) {
				if(null!=refTypeColumn && null!=refCodeType)
					addRule(refTypeColumn, cn.remex.db.WhereRuleOper.eq, refCodeType);
			}
		});
		Map<String, String> map = rvo.obtainMap(codeColumn, descColumn);

		Assert.isTrue(map!=null && map.size()>0, "配置的值参照中没有相应的参数！请配置人员查阅数据表:"+codeTableName);
		return  dialect.obtainDecodeSQL(map, hasSubStr?"substr("+dialect.quoteFullName(fieldTable, fieldName)+",0,2)":dialect.quoteFullName(fieldTable, fieldName), displayName);
	}
	/**
	 * 
	 * 代码映射功能，在select中使用的时本函数，在sqlstring中使用的{@link ReplaceRefCode#replaceRefCode(String)}
	 * 
	 * @param curTableAliasName
	 * @param fieldName
	 * @param ct
	 * @return
	 */
	private static StringBuilder obtainRsqlDecodePart(
			final String curTableAliasName,final String fieldName,
			final ColumnType ct
			){
		//final EditType editType = ct.editType; //LHY 没有使用到 2015-7-24
		final String DataDicTb = StringHelper.getClassSimpleName(ct.codeRefBean);
		final String typeColumn = ct.codeRefTypeColumn;
		final String codeType = ct.codeRefCodeType;
		final String codeColumn = ct.codeRefCodeColumn;
		final String descColumn = ct.codeRefDescColumn;
		final String filters = ct.codeRefFilters;
		
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		@SuppressWarnings("unchecked")
		Class<Modelable> clazz = (Class<Modelable>) spaceConfig.getOrmBeanClass(DataDicTb);
		DbRvo rvo = ContainerFactory.getSession().query(new DbCvo<Modelable>(clazz){
			private static final long serialVersionUID = -1322957928866739488L;
			@Override
			public void initRules(Modelable t) {
				//如果通过filters约束要搜索的数据字典的字段，则filters有限
				if(!Judgment.nullOrBlank(filters)){
					SqlBeanWhere sbw = JsonHelper.toJavaObject(filters, SqlBeanWhere.class);
					addGroup(sbw);
				}else{//默认通过dataType=来匹配搜索
					addRule(typeColumn, cn.remex.db.WhereRuleOper.eq, codeType);
				}
			}
		});
		Map<String, String> map = rvo.obtainMap(codeColumn, descColumn);
		
		Assert.isTrue(map!=null && map.size()>0, "配置的值参照中没有相应的参数！请配置人员查阅数据表:"+DataDicTb);
		
		
		
		StringBuilder sb = dialect.obtainDecodeSQL(map, dialect.quoteFullName(curTableAliasName, fieldName), "RMX_"+DataDicTb+"_"+fieldName);
//		= new StringBuilder("DECODE(").append(dialect.quoteFullName(curTableAliasName, fieldName));
//		for(String code:map.keySet()){
//			sb.append(",").append(dialect.quoteAsString(code)).append(",").append(dialect.quoteAsString(map.get(code)));
//		}
////LHY 因为标识符太长，可能导致无法使用，减少一个非重要的字段。需要前端使用的地方一起修改。
////		sb.append(") ").append(dialect.quoteKey("RMX_"+editType.toString()+"_"+DataDicTb+"_"+fieldName));
//		sb.append(") ").append(dialect.quoteKey("RMX_"+DataDicTb+"_"+fieldName));
		return sb;
		
	}
	
	/*
	删除测试代码。 LHY 2015-7-24
	private static StringBuilder obtainDecodeSqlPart(
			final String fieldTable,
			final String fieldName,
			final String displayName,
			final String codeTableName,
			final String refTypeColumn,
			final String refCodeType,
			final String codeColumn,
			final String descColumn
			){
//		final EditType editType = ct.editType;
//		final String DataDicTb = StringHelper.getClassSimpleName(ct.codeRefBean);
//		final String typeColumn = ct.codeRefTypeColumn;
//		final String codeType = ct.codeRefCodeType;
//		final String codeColumn = ct.codeRefCodeColumn;
//		final String descColumn = ct.codeRefDescColumn;
		
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		@SuppressWarnings("unchecked")
		Class<Modelable> clazz = (Class<Modelable>) spaceConfig.getOrmBeanClass(codeTableName);
		DbRvo rvo = ContainerFactory.getSession().query(new DbCvo<Modelable>(clazz){
			private static final long serialVersionUID = -1322957928866739488L;
			@Override
			public void initRules(Modelable t) {
				addRule(refTypeColumn, WhereRuleOper.eq, refCodeType);
			}
		});
		Map<String, String> map = rvo.obtainMap(codeColumn, descColumn);
		
		Assert.isTrue(map!=null && map.size()>0, "配置的值参照中没有相应的参数！请配置人员查阅数据表:"+codeTableName);
		
		StringBuilder sb = new StringBuilder("DECODE(").append(dialect.quoteFullName(fieldTable, fieldName));
		for(String code:map.keySet()){
			sb.append(",").append(dialect.quoteAsString(code)).append(",").append(dialect.quoteAsString(map.get(code)));
		}
		sb.append(") \"").append(displayName).append("\"");
		return sb;
		
	}*/
	/*
	删除测试代码。 LHY 2015-7-24
	private final static String RefCodeRegx = "RefCode\\((\\w+)\\.(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+)\\)";
	
	private static String replaceRefCode(String orgnSql){
	 MatchResult rs;
	while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx, null))){
		String fieldTable = rs.group(1);
		String fieldName = rs.group(2);
		String displayName = rs.group(3);
		String codeTbName = rs.group(4);
		String refTypeColumn = rs.group(5);
		String refCodeType = rs.group(6);
		String codeColumn = rs.group(7);
		String descColumn = rs.group(8);
		
		StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn);
		
		orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx, decodeSqlPart.toString(), -1);
	}

	return orgnSql;
	
}
	public static void main(String[] args) {
		String sql = " SAFSADFASDFAS   RefCode(AtinPolicy.bbb,aaa,SysCode,codeType,vehicleCoverage,code,codeName) ";
		RemexApplication.getContext();
		String sql1 = replaceRefCode(sql);
		System.out.println(sql1);
	}*/
	
	
	/**
	 * 生成直接执行sql语句的bean，sql语句支持:开头的命名参数
	 * @param cvo
	 * @return SqlBean
	 * @rmx.call {@link SqlBean#getInstance(DbCvo)}
	 */
	public static <T extends Modelable> SqlBean<T> createSqlStringSqlBean(final DbCvo<T> cvo) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		String sqlString = cvo.getSqlString();
		
		//对数据库字典进行转换
		sqlString = replaceRefCode(sqlString);
		
		String regex= dialect.obtainSelectRegex();
		MatchResult mr = StringHelper.match(sqlString, regex,null);
		@SuppressWarnings("unused")
		String tableAliasName = null,tableName=null;
		if(null!=mr){
			tableName = mr.group(1);
			tableAliasName = mr.group(2);
			cvo.$S(PN_bn, tableName);//解析获取sql中的表名。
		}
		
		SqlBean<T> sqlBean = new SqlBean<T>(cvo);
		ArrayList<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();
//		sqlBean.setTableName(tableAliasName==null || "WHERE".equals(tableAliasName.toUpperCase())?tableName:tableAliasName);
		
		TreeMap<Integer, String> paramIndexs = RsqlUtils.obtainNamedParamIndexs(sqlString);
		for(Integer paramIdx:paramIndexs.keySet()){
			namedParams.add(new SqlBeanNamedParam(-1, paramIndexs.get(paramIdx), Types.CHAR, null));
		}
		// 删除多余空间
		namedParams.trimToSize();
		sqlBean.init(cvo, sqlString, namedParams);
		sqlBean.setBeanName(tableName);
		return sqlBean;
	}

	/**
	 * 生成直接执行Update的sql语句的bean，sql语句支持:开头的命名参数
	 * @param cvo
	 * @return SqlBean
	 * @rmx.call {@link SqlBean#getInstance(DbCvo)}
	 */
	public static <T extends Modelable> SqlBean<T> createUpdateSqlBean(final DbCvo<T> cvo) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		SqlBean<T> sqlBean = new SqlBean<T>(cvo);

		String dataOption_ = cvo.getDataType();
		String dataOption = null != dataOption_ ? dataOption_ : "";
		String dtCols = cvo.getDataColumns();

		String beanName = cvo.getBeanName();
//		sqlBean.setTableName(beanName);
		Class<?> beanClass=sqlBean.getBeanClass();
		String tableAliasName = "T";

		List<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();

		// 定义对象类型数据的查询sql
		String prefix = "UPDATE "+dialect.quoteKey(beanName)+" "+tableAliasName+ " SET ";
		String part_colsetval;
		String part_where = Judgment.nullOrBlank(cvo.getId())?
				"\r\n  " + cvo.getSqlBeanWhere().toSQL(tableAliasName, namedParams, 0)
				:"\r\nWHERE " + dialect.quoteKey(SYS_id) + "= :"+SYS_id;


		StringBuilder part_colsetval_sb = new StringBuilder();

		StringBuilder sqlString = new StringBuilder();

		// 插入系统配置列
		for (String column : SysModifyColumns.keySet()) {
			part_colsetval_sb.append("\r\n\t").append(dialect.quoteKey(column)).append("= :" + column + ",");
			namedParams.add(new SqlBeanNamedParam(-1, column, SysModifyColumns.get(column).type, null));
		}

		// 定义更新基本数据的SQL
		Map<String, Type> baseFields = SqlType.getFields(beanClass,FieldType.TBase);
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, baseFields);
		Map<String, ColumnType> baseCols = RsqlUtils.restrictSKeyColumns(dtCols,baseColumns);

		// 添加基本列数据
		for (String column : baseCols.keySet()) {
			part_colsetval_sb.append("\r\n\t").append(dialect.quoteKey(column)).append("= :" + column + ",");
			namedParams.add(new SqlBeanNamedParam(-1, column, baseCols.get(column).type, null));
		}

		if (dataOption.contains(DT_object)) {
			Map<String, Type> objectFields = SqlType.getFields(beanClass, FieldType.TObject);
			Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(objectFields);
			Map<String, ColumnType> objectCols = RsqlUtils.restrictSKeyColumns(dtCols, objectColumns);
			for (String column : objectCols.keySet()) {
				part_colsetval_sb.append("\r\n\t").append(dialect.quoteKey(column)).append("= :" + column + ",");
				namedParams.add(new SqlBeanNamedParam(-1, column, Types.CHAR, null));// 所有对象引用都是整型
			}
		}

		// 务必加上id
		namedParams.add(new SqlBeanNamedParam(-1, SYS_id, Types.CHAR, null));

		part_colsetval = part_colsetval_sb.deleteCharAt(part_colsetval_sb.length() - 1).toString();// 删除最后一个逗号

		sqlString.append(prefix).append(part_colsetval).append(part_where);

		sqlBean.init(cvo, sqlString.toString(), namedParams);
		return sqlBean;
	}

	//	/**
	//	 * 不管是哪一种，都会自动将包含下划线"_"的分割字符串变成首字母小写,分割单词首字母大写的变量指代字母 如:prsn_name变成prsnName
	//	 *
	//	 * @param word
	//	 * @return
	//	 */
	//	@Deprecated
	//	static protected String parseWord(final String word) {
	//		String[] words = word.split("_");
	//		StringBuilder sb = new StringBuilder();
	//		sb.append(words[0]);
	//		for (int i = 1; i < words.length; i++) {
	//			sb.append(words[i].substring(0, 1).toUpperCase());
	//			sb.append(words[i].substring(1));
	//		}
	//		return sb.toString();
	//	}

	//	public static String obtainSQLWhere(String beanName,
	//			SqlBeanWhere where, List<SqlBeanNamedParam> namedParams) {
	//		StringBuilder result = new StringBuilder("");
	//		if (where.is_search() && where.getGroupOp() != null
	//				&& where.getRules() != null
	//				&& where.getRules().size() != 0) {
	//			// 多字段的组合查询
	//			String groupOp = where.getGroupOp();
	//			result.append("WHERE ");
	//			int i = 0;
	//			for (SqlBeanWhereRule wDetail : where.getRules()) { // 循环处理所有的查询条件
	//				i++;
	//				result.append(obtainSQLWhereOperation(beanName, wDetail
	//						.getField(), wDetail.getField() + i, wDetail.getOp()));
	//
	//				namedParams.add(new SqlBeanNamedParam(-1, wDetail.getField()
	//						+ i, Types.CHAR, null));
	//
	//				if (i < where.getRules().size())
	//					result.append(" " + groupOp + " ");
	//			}
	//		} else if (where.is_search()
	//				&& where.getSearchField() != null
	//				&& where.getSearchOper() != null
	//				&& where.getSearchString() != null) {
	//			// 单字段组合
	//			result.append("WHERE ").append(
	//					obtainSQLWhereOperation(beanName, where
	//							.getSearchField(), where.getSearchField(),
	//							where.getSearchOper()));
	//			namedParams.add(new SqlBeanNamedParam(-1, where
	//					.getSearchField(), Types.CHAR, null));
	//		}
	//		return result.toString();
	//	}

	//	/**
	//	 *
	//	 * @param sField
	//	 *            字段名称
	//	 * @param sOper
	//	 *            操作名称
	//	 * @param sParamName
	//	 *            参数名称，采用i递增，避免冲突
	//	 * @return
	//	 */
	//	public static String obtainSQLWhereOperation(String beanName,
	//			String sField, String sParamName, String sOper) {
	//		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
	//		if (sField == null || sField.trim().length() == 0 || sOper == null
	//				|| sOper.trim().length() == 0)
	//			return "";
	//		StringBuilder cont = new StringBuilder();
	//		// 添加beanName避免数据库表的字段名冲突
	//		cont.append(dialect.openQuote()).append(beanName).append(dialect.closeQuote()).append(".").append(dialect.openQuote());
	//
	//		if (sOper.trim().equals("eq")) // 等于
	//			cont.append(sField).append(dialect.closeQuote()).append("= :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("ne")) // 不等于
	//			cont.append(sField).append(dialect.closeQuote()).append(" !=  :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("lt")) // 小于
	//			cont.append(sField).append(dialect.closeQuote()).append(" <  :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("le")) // 小于等于
	//			cont.append(sField).append(dialect.closeQuote()).append(" <=  :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("gt")) // 大于
	//			cont.append(sField).append(dialect.closeQuote()).append(" >  :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("ge")) // 大于等于
	//			cont.append(sField).append(dialect.closeQuote()).append(" >=  :").append(sParamName).append(" ");
	//		else if (sOper.trim().equals("bw")) // 以...开始
	//			cont.append(sField).append(dialect.closeQuote()).append(" LIKE  :").append(sParamName).append(
	//					"+'%' ");
	//		else if (sOper.trim().equals("bn")) // 不以...开始
	//			cont.append(sField).append(dialect.closeQuote()).append(" NOT LIKE  :").append(sParamName)
	//					.append("+'%' ");
	//		else if (sOper.trim().equals("ew")) // 以...结束
	//			cont.append(sField).append(dialect.closeQuote()).append(" LIKE '%'+ :").append(sParamName)
	//					.append(" ");
	//		else if (sOper.trim().equals("en")) // 不以...结束
	//			cont.append(sField).append(dialect.closeQuote()).append(" NOT LIKE '%'+ :").append(sParamName)
	//					.append(" ");
	//		else if (sOper.trim().equals("cn")) // 包含
	//			cont.append(sField).append(dialect.closeQuote()).append(" LIKE '%'+ :").append(sParamName)
	//					.append(" +'%' ");
	//		else if (sOper.trim().equals("nc")) // 不包含
	//			cont.append(sField).append(dialect.closeQuote()).append(" NOT LIKE '%'+ :").append(sParamName)
	//					.append(" +'%' ");
	//		else
	//			// 默认是包含
	//			cont.append(sField).append(dialect.closeQuote()).append(" LIKE '%'+ :").append(sParamName)
	//					.append(" +'%' ");
	//		// else if(sOper.trim().equals("in")) //'ew','en','cn','nc'。
	//		// result.append(sField).append(" NOT LIKE '").append(
	//		// sValue).append("%' ");
	//		// 包括'in','ni',不知道什么意思，没写
	//
	//		return cont.toString();
	//	}

}

class FbColumn {
	protected String fBeanAlias;
	protected String fBeanName;
	protected ArrayList<FbColumnOn> ons = new ArrayList<FbColumnOn>();

	/**
	 * @param fBeanName 外键的表名
	 * @param fBeanAlias 外键表的虚拟名
	 * @param fBeanOn on字符串OR:
	 */
	public FbColumn(final String fBeanName, final String fBeanAlias, final String fBeanOn) {
		super();
		this.fBeanName = fBeanName;
		this.fBeanAlias = fBeanAlias;
		for (String on : fBeanOn.split("\\|")) {
			String join = "AND";
			if (on.startsWith("OR:")) {
				join = "OR";
			}
			on = on.substring(on.indexOf(':') < 0 ? 0 : on.indexOf(':') + 1);
			String[] onParts = on.split("=");
			if (onParts[0].startsWith(fBeanAlias) && onParts[0].indexOf('.')>=0 ) {
				this.ons.add(new FbColumnOn(onParts[1],
						onParts[0].split("\\.")[1], join));
			} else {
				this.ons.add(new FbColumnOn(onParts[0],
						onParts[1].split("\\.")[1], join));
			}

		}
	}
}

class FbColumnOn {
	/**
	 * 本表的
	 */
	protected String innerTableField;// 本表的
	protected String joinOper;
	/**
	 * fb，外表的列
	 */
	protected String outerTableField;

	public FbColumnOn(final String nOn, final String wOn, final String join) {
		super();
		this.innerTableField = nOn;
		this.outerTableField = wOn;
		this.joinOper = join;
	}
}

class TableAliasIndex {
	protected int index = 0;
}

class WherePart {
	protected String wherePart = "";

	public WherePart(final String partWhere) {
		this.wherePart = partWhere;
	}
}

class ColumnType {
	int length;
	String sqlType;
	int type ;
	EditType editType;
	Class<?> codeRefBean;
	String codeRefTypeColumn;
	String codeRefCodeColumn;
	String codeRefDescColumn;
	String codeRefCodeType;
	String codeRefFilters;
	

	/**
	 * @param type @see {@link Types}
	 */
	public ColumnType(int type,int length) {
		this.type = type;
		this.length = length;
	}


	public ColumnType(Column sta) {
		this.length = sta.length();//长度
		this.sqlType = sta.columnDefinition();//暂时不用，用于记录数据字段类型的字符串
		this.type = sta.type();//Types下的变量，java定义的数据字段类型
	}
	
}
