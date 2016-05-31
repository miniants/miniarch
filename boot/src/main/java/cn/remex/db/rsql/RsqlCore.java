/**
 * 
 */
package cn.remex.db.rsql;


import cn.remex.core.RemexRefreshable;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.RsqlConnectionException;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.transactional.RsqlDefine;
import cn.remex.db.sql.ColumnType;
import cn.remex.db.sql.FieldType;
import cn.remex.db.sql.SqlType;
import org.springframework.beans.factory.DisposableBean;

import javax.persistence.*;
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
	 * 设置是否去数据库验证主键正确否。
	 */
	static boolean checkPKFromDataBase = false;
	/**
	 * 向表中添加一个数据列。
	 * @param dialect 方言
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param columnType 列属性
	 */
	private static void AlterAddColumn(final Dialect dialect, final String tableName, final String columnName, final ColumnType columnType) {
        String sqlString = "ALTER TABLE " + dialect.quoteKey(tableName) + " ADD " + dialect.quoteKey(columnName) + "" +
                dialect.obtainSQLTypeString(columnType.type, columnType.length);
        ContainerFactory.getSession().executeUpdate(sqlString, null);
	}
	private static void AlterModifyColumn(final Dialect dialect, final String tableName, final String columnName, final ColumnType columnType, String renameFrom) {
		// alter table tablename modify  columnName varchar(len);
		String sqlString;
		if(Judgment.nullOrBlank(renameFrom)){
	        sqlString = "ALTER TABLE " + dialect.quoteKey(tableName) + " MODIFY " + dialect.quoteKey(columnName) + "" +
			        dialect.obtainSQLTypeString(columnType.type, columnType.length);
        }else{
	        sqlString = dialect.renameColumnSql().replaceAll(":tableName",dialect.quoteKey(tableName)).replaceAll(":oldColumnName",dialect.quoteKey(renameFrom)).replaceAll(":newColumnName",dialect.quoteKey(columnName))+" "+dialect.obtainSQLTypeString(columnType.type, columnType.length);
        }

        ContainerFactory.getSession().executeUpdate(sqlString, null);
	}
	/**
	 * 本程序用于自动创建JavaBean对应的关系型数据表基本结构 仅支持Int nvarchar两种数据类型
	 */
	public static void createBaseTable(final Dialect dialect, final String tableName, final Class<?> beanClass) {
		//创建
		Map<String, Type> baseFields = SqlType.getFields(beanClass, FieldType.TBase);
		Map<String, Type> objectFields = SqlType.getFields(beanClass, FieldType.TObject);
		Map<String, ColumnType> partSysColumns = new HashMap<>();
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, baseFields);
		Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(objectFields);

		String prefix = "\r\nCREATE TABLE " + dialect.quoteKey(tableName) + " (\r\n";
		String suffix = "\r\n)\r\n";
		StringBuilder content = new StringBuilder();

		// 系统数据列
		content.append("		").append(dialect.quoteKey(SYS_id)).append(" ").append(dialect.obtainSQLTypeString(Types.CHAR, 50)).append(" NOT NULL PRIMARY KEY,\r\n");
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
	public static void renameTable(final Dialect dialect, final String oldTableName, final String newTableName) {
		String sql = dialect.renameTableSql();
		ContainerFactory.getSession().executeUpdate(sql.replaceAll(":oldTableName",dialect.quoteKey(oldTableName)).replaceAll(":newTableName",dialect.quoteKey(newTableName)), null);

	}
	/**
	 * 此函数建立bean中List/Set/Vector关联的表
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
			if (SqlType.isTCollection(type1)) {
				throw new RsqlConnectionException(beanName, "不支持深层Collection映射，数据库ORM映射创建失败！");
			} else if (type1 == Object.class) {
				throw new RsqlConnectionException(beanName, "不支持Object 原始类型直接Map映射，数据库ORM映射创建失败！");
			}
		}

        content.append("		").append(dialect.quoteKey(SYS_id)).append(" ").append(dialect.obtainSQLTypeString(Types.CHAR, 50)).append(" NOT NULL PRIMARY KEY,\r\n");
        // *****************添加bean中的表连接属性，定义为int，并随后建立表外键连接
		for (String objectColumn : columns.keySet()) {
			ColumnType ct =columns.get(objectColumn);
			content.append("		").append(dialect.quoteKey(objectColumn)).append(dialect.obtainSQLTypeString(ct.type,ct.length)).append(" NOT NULL,\r\n");
		}

		String mid = content.toString();
		mid = mid.substring(0, mid.length() - 3);
		ContainerFactory.getSession().executeUpdate(prefix + mid + suffix, null);
	}
	private static void modifyColumn(String tableName, DbRvo columnNames, ColumnType ct, String columnName, Dialect dialect,String oldColumnName){//如果没有修改 必须将oldColumnName = columnName

		List<Object> cns = columnNames.getCells(0, columnName, 0);//存在的列名，要么有一个要么没有
		if (cns.size() == 0 && columnName.equals(oldColumnName)) { //没有,也不是从别的列重命名过来的。
			AlterAddColumn(dialect, tableName, columnName, ct);
			logger.info("现已完成为名为" + tableName + "的表添加列" + columnName);
		}else{ // 有
			String newColumnName = columnName;
			boolean notRename = cns.size() == 1;
			oldColumnName = notRename?columnName:oldColumnName;
			String type = columnNames.getCells(0, oldColumnName, "DATA_TYPE").get(0).toString();
			Object length = columnNames.getCells(0, oldColumnName, "DATA_LENGTH").get(0);
			Object precision = columnNames.getCells(0, oldColumnName, "NUMERIC_PRECISION").get(0);
			Object scale = columnNames.getCells(0, oldColumnName, "NUMERIC_SCALE").get(0);
			String sqlTypeStr = dialect.obtainSQLTypeString(ct.type, ct.length).trim();
			
			if(notRename&&ct.type == Types.CLOB) return;

			if(notRename&&(type.equalsIgnoreCase("INT") && sqlTypeStr.equalsIgnoreCase("INTEGER"))){
				logger.info("Types.INT 对应为 INTEGER，无需重构类型!");
			}else if(notRename&&ct.type == Types.BOOLEAN && type.equalsIgnoreCase("VARCHAR") && String.valueOf(length).equals("5")) {
				logger.info("Types.BOOLEAN 对应为 VARCHAR(5)，无需重构类型!");
			}else if(notRename&&ct.type == Types.DOUBLE && type.equalsIgnoreCase("DOUBLE")
					&& String.valueOf(ct.length).equalsIgnoreCase(String.valueOf(precision)) && String.valueOf(2).equalsIgnoreCase(String.valueOf(scale))) { //暂时默认都两位
				logger.info("Types.DOUBLE 对应为 DOUBLE("+precision+","+scale+")，无需重构类型!,暂未检查精度，默认为2位");
			}else if(notRename&&ct.type == Types.NUMERIC && (type.equalsIgnoreCase("NUMERIC") || type.equalsIgnoreCase("DECIMAL"))
					&& String.valueOf(ct.length).equalsIgnoreCase(String.valueOf(precision)) && String.valueOf(2).equalsIgnoreCase(String.valueOf(scale))) { //暂时默认都两位
				logger.info("Types.NUMERIC 对应为 "+type.toUpperCase()+"("+precision+","+scale+")，无需重构类型!,暂未检查精度，默认为0位");
			}else if(!notRename || !sqlTypeStr.split("[ \t(]")[0].equalsIgnoreCase(type)
					|| !String.valueOf(ct.length).equalsIgnoreCase(String.valueOf(length))
					) { // 长度和类型不同时需修改
				logger.info("正为名为" + tableName + "的表修改列" + newColumnName+" from "+oldColumnName+":"+type+" "+length+" ->" +dialect.obtainSQLTypeString(ct.type, ct.length));
				AlterModifyColumn(dialect, tableName, newColumnName, ct, oldColumnName);
				logger.info("现已完成为名为" + tableName + "的表修改列" + newColumnName+" from "+oldColumnName+":"+type+" "+length+" ->" +dialect.obtainSQLTypeString(ct.type, ct.length));
			}
		}
	
	}
	/**
	 * 此函数一检查了base object在内的基本表数据列
	 */
	public static void refreshORMBaseTables(final RDBSpaceConfig spaceConfig) {
		Map<String, Class<?>> ormBeans = spaceConfig.getOrmBeans();
		Dialect dialect = spaceConfig.getDialect();
		logger.debug("▄▄▄▄▄▄▄▄▄▄◢RemexDB的ORM组件正在检查数据库中的 基本表BaseTables◣▄▄▄▄▄▄▄▄▄▄");
		// 此语句仅适用于mssql查询数据库中表名
		Container session = ContainerFactory.getSession();
		DbRvo tableNames = session.executeQuery(dialect.obtainSQLSelectTableNames(), null);

		for (String beanName : ormBeans.keySet()) {
			refreshThisBaseTable(dialect,ormBeans,beanName,tableNames,spaceConfig.getSpaceName());
		}
		logger.debug("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄END▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

	}

	public static void refreshThisBaseTable(Dialect dialect,Map<String, Class<?>> ormBeans, String beanName, DbRvo tableNames,String spaceName) {
		String tableName = dialect.needLowCaseTableName()? beanName.toLowerCase():beanName;
		//检查是否重命名
		RsqlDefine rsqlDefine = ormBeans.get(beanName).getAnnotation(RsqlDefine.class);

		if (!Judgment.nullOrBlank(rsqlDefine) && !Judgment.nullOrBlank(rsqlDefine.renameFrom()) && tableNames.getCells(0, rsqlDefine.renameFrom(), 0).size() == 1) {//非空则重命名
			renameTable(dialect, rsqlDefine.renameFrom(), tableName);
			logger.info("修改表名from " + rsqlDefine.renameFrom()+" to " + tableName + "完成！");
			ORMBaseTablesModify(tableName, ormBeans.get(beanName), dialect, spaceName);
		} else if (tableNames.getCells(0, tableName, 0).size() != 1) { //非空则重命名
			createBaseTable(dialect, tableName, ormBeans.get(beanName));
			logger.info("创建名为" + tableName + "的表完成！");
		} else {
			ORMBaseTablesModify(tableName, ormBeans.get(beanName), dialect, spaceName);
		}
	}

	private static void ORMBaseTablesModify(String tableName,Class<?> beanClass, Dialect dialect, String spaceName){

		logger.debug("数据库中存在名为" + tableName + "的表！跳过创建阶段，进行表结构检查！");

		// 添加需要检查的列s
		List<Map<String, ColumnType>> columnsList = new ArrayList<>();
		Map<String, ColumnType> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
		columnsList.add(sysColumns);// 检查系统列
		Map<String, ColumnType> baseColumns = RsqlUtils.obtainSKeyColumnsBase(beanClass, SqlType.getFields(beanClass, FieldType.TBase));
		columnsList.add(baseColumns);// 检查基础数据列
		Map<String, ColumnType> objectColumns = RsqlUtils.obtainSKeyColumnsObject(SqlType.getFields(beanClass, FieldType.TObject));
		columnsList.add(objectColumns);// 检查一对一数据列
		
		// 获取数据库中对应表的列s
		HashMap<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put(PN_rowCount, "0");
		DbRvo columnNames = ContainerFactory.getSession(spaceName).executeQuery(dialect.obtainSQLSelectTablesColumnNames(tableName), params);
		
		for (Map<String, ColumnType> columns : columnsList) {
			for (String columnName : columns.keySet()) {
				RsqlDefine rsqlDefine = ReflectUtil.getAnnotation(beanClass, columnName, RsqlDefine.class);
				String oldColumnName = rsqlDefine==null || Judgment.nullOrBlank(rsqlDefine.renameFrom())?columnName:rsqlDefine.renameFrom();
				modifyColumn(tableName, columnNames, columns.get(columnName), columnName, dialect,oldColumnName);
			}
		}
	
	}
	/**
	 * 此函数一检查了collection object在内的外键表CollectionTables
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
				RsqlDefine curBeanRsqlDefine = ReflectUtil.getAnnotation(beanClass, RsqlDefine.class);
				RsqlDefine curFieldrsqlDefine = ReflectUtil.getAnnotation(beanClass, fieldName, RsqlDefine.class);



				String oldTableName = (Judgment.nullOrBlank(curBeanRsqlDefine) || Judgment.nullOrBlank(curBeanRsqlDefine.renameFrom()))
						&&(Judgment.nullOrBlank(curFieldrsqlDefine) ||Judgment.nullOrBlank(curFieldrsqlDefine.renameFrom())) ?
						tableName : RsqlUtils.obtainSKeyCollectionTableName(dialect,
						Judgment.nullOrBlank(curBeanRsqlDefine) || Judgment.nullOrBlank(curBeanRsqlDefine.renameFrom()) ?beanName:curBeanRsqlDefine.renameFrom(),
						Judgment.nullOrBlank(curFieldrsqlDefine) || Judgment.nullOrBlank(curFieldrsqlDefine.renameFrom()) ?fieldName:curFieldrsqlDefine.renameFrom());

				if (!(tableNames.getCells(0, tableName, 0).size() == 1)
						&& !(tableNames.getCells(0, oldTableName, 0).size() == 1) ) {//没有找到表名，用renameFrom也没有找到表名
					Class<?> targetClass = ReflectUtil.getListActualType(field);

					OneToMany otm = ReflectUtil.getAnnotation(beanClass, fieldName, OneToMany.class);
					if (otm != null) {// 此为一对多，没有注明一对多则新建中间表,表示多对多
						Map<String, Type> fbFields = SqlType.getFields(targetClass, FieldType.TObject);
						String mappedField = otm.mappedBy();// 指明了OneToMany关系中，外键由多方保管更新，则需要核对多方是否有这个外键
						if (!fbFields.containsKey(mappedField)) {
							throw new RsqlException(ServiceCode.RSQL_INIT_ERROR, "OneToMany映射错误，在多方[ " + targetClass.toString() + " ]未设置外键[ "
									+ mappedField + " ]维护关系。可能是getter/setter的名称设置有误！");
						} else if (!fbFields.get(mappedField).equals(beanClass)) {
							throw new RsqlException(ServiceCode.RSQL_INIT_ERROR, "OneToMany映射错误，在多方设置的属性[ "+mappedField+" ]类型有误，应该为："+beanClass);
						}
					} else {
						Map<String, Type> fbFields = SqlType.getFields(beanClass, FieldType.TCollection);
						//不配置ManyToMany、或者显式配置ManyToMany时，本类为多对多方的主方，负责维护中间表
						ManyToMany mtm = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);
						Class<?> fbBeanClass;
						boolean isweihufang;

						if(null!=mtm){
							String mappedField = mtm.mappedBy();// 必须成对出现,不指定时则只有List属性的一方维护此多对多关系
							fbBeanClass = ReflectUtil.getListActualType(fbFields.get(fieldName));
							Class te = mtm.targetEntity();//只能在主方指定，且类型不能错误
							isweihufang = fbBeanClass.equals(te);//如果有注解则TargetEntity有指定


							ManyToMany fk_mtm = ReflectUtil.getAnnotation(targetClass, mappedField, ManyToMany.class);
							if(fk_mtm !=null){
								Assert.isTrue(fieldName.equals(fk_mtm.mappedBy()), ServiceCode.FAIL, "多对多外键对象的属性的ManyToMany注解的mappedBy"+fk_mtm.mappedBy()+"必须与当前模型的list属性名"+fieldName+"一致");
								Assert.isTrue((void.class.equals(te) && fk_mtm.targetEntity().equals(beanClass))
												|| (void.class.equals(fk_mtm.targetEntity()) && te.equals(fbBeanClass)), ServiceCode.FAIL,
										"在多对多关系中，targetEntity只能由维护方指定，指定的值为外键对象的类型:targetEntity="+te+";beanClass="+beanClass+";fbBeanClass="+fbBeanClass
								);
							}
						}else{
							// TODO 后面多对多需要显示声明
							isweihufang =true;
//							for(Annotation fban: fbBeanClass.getAnnotations()){
//								Class<? extends Annotation> fbanc = fban.annotationType();
//								if(fbanc.isAssignableFrom(ManyToMany.class)){
//									isweihufang = !fieldName.equals(((ManyToMany) fban).mappedBy()); //当前注解为null，且对方任何mappedBy指向本属性则不是维护方
//								}
//							}
						}


						//只有当前属性注解为空且未被外键对象mappedBy引用 或者 注解不为null且指定了targetEntity时本属性为维护方
						if(isweihufang){
							createCollectionTable(dialect, beanName, fieldName, field);
							logger.info("创建名为" + tableName + "的表完成！ ");
						}
					}
				} else {
					logger.debug("数据库中存在名为" + tableName +" / " +oldTableName + "的表！跳过创建阶段，进行表结构检查！");

					if(tableNames.getCells(0, tableName, 0).size() == 0  && tableNames.getCells(0, oldTableName, 0).size() == 1 && !tableName.equals(oldTableName)){ //新的没有，就得有，且新旧名字不一样
						renameTable(dialect, oldTableName,tableName);
					}

					HashMap<String, Object> params = new HashMap<>();
					params.put("tableName", tableName);
					DbRvo columnNames = session.executeQuery(dialect.obtainSQLSelectTablesColumnNames(tableName), params);

					List<Map<String, ColumnType>> columnsList = new ArrayList<>();
					Map<String, ColumnType> sysColumns = RsqlUtils.obtainSKeyColumnsSys();
					columnsList.add(sysColumns);// 检查系统列
					Map<String, ColumnType> neededColumns = RsqlUtils.obtainSKeyCollectionTableColumns(beanName, fieldName, field);
					columnsList.add(neededColumns);// 添加这个表的必需列

					for (Map<String, ColumnType> columns : columnsList) {
						for (String columnName : columns.keySet()) {
							//获取renameFrom
							Class<?> fieldClass = columnName.startsWith("P_")?beanClass:ReflectUtil.getListActualType(field);
							RsqlDefine filedRsqlDefine = ReflectUtil.getAnnotation(fieldClass, RsqlDefine.class);
							boolean notRename = filedRsqlDefine==null || Judgment.nullOrBlank(filedRsqlDefine.renameFrom());
							String fieldOldColumnName = notRename?columnName:filedRsqlDefine.renameFrom();
							fieldOldColumnName = columnName.startsWith("P_")||columnName.startsWith("F_") ? (notRename?columnName:columnName.split("_")[0]+"_"+fieldOldColumnName) :columnName;
							modifyColumn(tableName, columnNames, columns.get(columnName), columnName, dialect, fieldOldColumnName);
						}
					}
				}
			}
		}
		logger.debug("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄END▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

	}
	/**
	 * 此函数一检查数据库中的约束
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
			RsqlUtils.SysColumns.keySet().stream()
					.filter(col -> indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, col), dialect.obtainSQLIndexNameField()).size() == 0 && !SYS_id.equals(col))
					.forEach(col -> session.execute(dialect.obtainIndexSql(beanName, col), null));
			SqlType.getFields(beanClass, FieldType.TObject).keySet().stream()
					.filter(col -> indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, col), dialect.obtainSQLIndexNameField()).size() == 0 && !RsqlUtils.SysColumns.containsKey(col))
					.forEach(col -> session.execute(dialect.obtainIndexSql(beanName, col), null));

			Table table_anno = ReflectUtil.getAnnotation(beanClass, Table.class);

            if(null != table_anno){
                //检查自定配置索引 TODO 暂未实现,用于生成自定义的索引
                Index[] idxs = table_anno.indexes();
                for(Index idx:idxs){
					String indexName = idx.name();
					String listCols = idx.columnList();
				}
//                Map<String, Type> fields = SqlType.getFields(beanClass, FieldType.TBase);
//                for (String fieldName : fields.keySet()) {
//                    if(indexNames.getCells(dialect.obtainSQLIndexNameField(), dialect.obtainIndexName(beanName, fieldName), dialect.obtainSQLIndexNameField()).size() == 0) {
//                        Column anno = ReflectUtil.getAnnotation(beanClass, fieldName, Column.class);
//                        if(null!=anno && anno.param()) {
//                            session.execute(dialect.obtainIndexSql(beanName, fieldName), null);
//                        }
//                    }
//
//                }

                //检查唯一性索引
                UniqueConstraint[] ucs = table_anno.uniqueConstraints();
                for(UniqueConstraint uc:ucs){
                    String[] cols = uc.columnNames();
                    String name = dialect.obtainIndexName(beanName, cols);//约束名称可以自定义，也可以由系统根据方言自动生成。
                    if(cols.length > 0 && indexNames.getCells(dialect.obtainSQLIndexNameField(), name, dialect.obtainSQLIndexNameField()).size() == 0){
                        session.execute(dialect.obtainConstraintSql(beanName, name,cols), null);
                    }
                }
            }

			//检查OneToOne、OneToManay、ManyToMany中的级联
			
			
			
		}
		
	}
	/**
	 * 指定是否从进行数据库查询来确定id主键的合法性。
	 */
	static void setCheckPKFromDatabase(final boolean checkPKFromDataBase) {
		RsqlCore.checkPKFromDataBase = checkPKFromDataBase;
	}

	/**
	 * 初始化 也用于外界调用以便重新初始化参数 设计中已经考虑的外界注入式的池初始
	 */
	public static void reset(final boolean... rebuildDB) {
		RDBManager.reset(null!=rebuildDB && rebuildDB[0]);
	}
    @Override
	public void refresh() {
		logger.info("刷新Rsql配置，重新加载jdbc驱动、配置！");
		reset(false);
	}
	@Override
	public void destroy() throws Exception {
		logger.info("注销数据Rsql！");
		RDBManager.destroy();
	}
}
