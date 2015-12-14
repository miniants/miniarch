/**
 * 
 */
package cn.remex.db.rsql;

import cn.remex.core.exception.InvalidCharacterException;
import cn.remex.core.reflect.ReflectFeatureStatus;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.MapHelper;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.appbeans.BeanVo;
import cn.remex.db.appbeans.MapVo;
import cn.remex.db.model.SysSerialNumber;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.SqlType.FieldType;

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangyang
 * 
 */
public class RsqlRvo  extends DbRvo  {
	public static Pattern simpleChian = Pattern
			.compile("([_\\w\u4e00-\u9fa5]+\\.)+([_\\w\u4e00-\u9fa5]+)");
	/**
	 * 
	 */
	private static final long serialVersionUID = 3568677065938784490L;

	/**
	 * 私有接口
	 * @param rs
	 *            要获取Clob的集合
	 * @param columnIndex
	 *            要读取的列序号
	 * @return String
	 * @rmx.call {@link RsqlRvo#saveData(ResultSet, DbCvo)}
	 */
	public static String getClobColumn(final ResultSet rs, final int columnIndex) {
		try {
			// 读取CLOB中的内容
			Clob clob = rs.getClob(columnIndex);
			if (null == clob) {
				return "null";
			}

			StringBuilder ret = new StringBuilder();
			Reader is = clob.getCharacterStream();

			if (null == is) {
				return "null";
			}

			// 读取CLOB内容
			char cbuf[] = new char[256];
			int c;
			while ((c = is.read(cbuf)) > 0) {
				ret.append(cbuf, 0, c);
			}
			return ret.toString();

		} catch (Exception e) {
			return "发生异常，无法读取CLOB的内容！";
		}
	}

	private Class<?> beanClass;
	private int effectRowCount;
	private String id = null;
	private Map<Integer, Map<String, List<List<Object>>>> mapRowsCache;
	/** 用来保存操作状态的 */
	private boolean OK = false;
	private int pagination;
	private int recordCount;
	private int rowCount;
	private List<List<Object>> rows;
	private DbCvo<?> rsqlCvo;
	private List<String> titles;


	/**
	 * 本构造函数用于非数据库操作对象{@link RsqlDao}的相关操作返回的结果集<br>
	 *@rmx.summary  用到本构造函数的情况是没有数据操作，但需要返回一个结果的时候用的。 如
	 *   initType
	 *            <li>if initType equals {@link RsqlRvo#INIT_RVO_QUERY} 则初始化
	 *            {@link #queryResult}<br> <li>else if it equals
	 *            {@link RsqlRvo#INIT_RVO_UPDATE} 则初始化 {@link #updateResult}<br>
	 * 
	 *            注意{@link UpdateResult#id}初始为null
	 *            {@link UpdateResult#effectRowCount}初始为0
	 * {@link RsqlContainer#store(Modelable, DbCvo)}中的部分过程
	 * 
	 * @see cn.remex.db.rsql.RsqlContainer
	 */
	public RsqlRvo() {
		super();
	}

	public <T extends Modelable> RsqlRvo(final DbCvo<T> rsqlCvo2) {
		this.rsqlCvo = rsqlCvo2;
		this.beanClass = rsqlCvo2.getBeanClass();
	}

	public Class<?> getBeanClass() {
		return this.beanClass;
	}

	/**
	 * 读取当前页数据中单元格(rowNO,columnNO)中的数据
	 * @param rowNO
	 * @param columnNO
	 * @return String
	 * @exception ArrayIndexOutOfBoundsException
	 *                index is out of range (index &lt; 0 || index &gt;=
	 *                size()).
	 * @rmx.call {@link RsqlRvo#getColumn(int, Class)}
	 * @rmx.call {@link RsqlRvo#getColumn(int)}
	 * @rmx.call {@link SysSerialNumber#createSerialNumber(Class, String)}
	 */
	@Override
	public Object getCell(final int rowNO, final int columnNO) {
		if (null != this.rows && null != this.titles
				&& columnNO < this.titles.size()) {
			return this.rows.get(rowNO).get(columnNO);
		}
		return null;
	}

	/**
	 * 通过指定是第几行来检索一个条目并获得该条目needColumnName列的cell的值
	 * @param rowIndex >0
	 * @param needColumnName
	 * @return list
	 * @rmx.call {@link RsqlContainer#getPK(Modelable)}
	 * @rmx.call {@link RsqlRvo#setValue(String poolName, Class<?> clazz,String expr, Object targetbean, Object value,  int rowIndex, ReflectFeatureStatus status}
	 */
	@Override
	public Object getCell(final int rowIndex, final String needColumnName) {
		int index = getTitleIndex(needColumnName);
		if (index < 0) {
			return null;
		}
		return this.rows.get(rowIndex).get(index);
	}

	/**
	 * 通过指定第几行并指定检索目标值columnIndex的值=columnValue<br>
	 * 来检索满足条件的所有条目所有cells的值，并指定需要的column的index
	 * @param columnIndex
	 * @param columnValue
	 * @param needColumnIndex
	 * @return list
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 */
	@Override
	public List<Object> getCells(final int columnIndex,
			final String columnValue, final int needColumnIndex) {
		List<List<Object>> rows = getRows(columnIndex, columnValue);
		ArrayList<Object> cells = new ArrayList<Object>();
		if (needColumnIndex >= 0 && null != rows) {
			for (List<Object> row : rows) {
				cells.add(row.get(needColumnIndex));
			}
		}
		cells.trimToSize();
		return cells;
	}

	/**
	 * 通过指定第几行并指定检索目标值columnIndex的值=columnValue<br>
	 * 来检索满足条件的所有条目所有cells的值，并指定需要的column名称
	 * @param columnIndex
	 * @param columnValue
	 * @param needColumnName
	 * @return list
	 * @rmx.call {@link RsqlCore#modifyColumn}
	 */
	public List<Object> getCells(final int columnIndex, final String columnValue,
			final String needColumnName) {
		return getCells(columnIndex, columnValue,
				getTitleIndex(needColumnName));
	}
	
	/**
	 * 通过columnName=columnValue<br>
	 * 来检索满足条件的所有条目所有cells的值，并指定需要的column的index
	 * @param columnName
	 * @param columnValue
	 * @param needColumnIndex
	 * @return list
	 */
	public List<Object> getCells(final String columnName, final String columnValue,
			final int needColumnIndex) {
		return getCells(getTitleIndex(columnName), columnValue,
				needColumnIndex);
	}
	
	
	/**
	 * 通过columnName=columnValue来检索满足条件的所有条目所有cells的值，并指定需要的column名称
	 * @param columnName
	 * @param columnValue
	 * @param needColumnName
	 * @return list
	 */
	public List<Object> getCells(final String columnName, final String columnValue,
			final String needColumnName) {
		return getCells(getTitleIndex(columnName), columnValue,
				getTitleIndex(needColumnName));
	}
	
	/**
	 * 获取当前页数据中的某一列的数据
	 * @rmx.summary 数据类型默认为String，如果需要转化，可用同名函数getColumn(int index,Class clazz)
	 * @param index
	 * @return ArrayList<String>
	 * @exception ArrayIndexOutOfBoundsException
	 *                index is out of range (index &lt; 0 || index &gt;=
	 *                getCountFromColumn().
	 * @rmx.call {@link RsqlRvo#getColumn(String)}
	 */
	@Override
	public List<Object> getColumn(final int index) {
		ArrayList<Object> column = new ArrayList<Object>();
		int curCount = getRowCount();
		for (int i = 0; i < curCount; i++) {
			column.add(getCell(i, index));
		}
		column.trimToSize();
		return column;
	}
	

	/**
	 * 获取当前页数据中的某一列的数据
	 * @rmx.summary 数据类型指定为clazz，
	 * @param index
	 * @return ArrayList<String>
	 * @exception ArrayIndexOutOfBoundsException
	 *                index is out of range (index &lt; 0 || index &gt;=
	 *                getCountFromColumn().
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getColumn(final int index, final Class<T> clazz) {
		ArrayList<T> column = new ArrayList<T>();
		int curCount = getRowCount();
		for (int i = 0; i < curCount; i++) {
			column.add((T) ReflectUtil.caseObject(clazz, getCell(i, index)));
		}
		column.trimToSize();
		return column;
	}

	/**
	 * 通过列明来检索该列的内容。
	 * @param needColumnName
	 * @return List
	 */
	@Override
	public List<Object> getColumn(final String needColumnName) {
		int index = getTitleIndex(needColumnName);
		if (index < 0) {
			return null;
		} else {
			return getColumn(index);
		}
	}

	@Override
	public int getEffectRowCount() {
		return this.effectRowCount;
	}
	@Override
	public List<Map<String, Object>> getGridData() {
		return this.getMapRows();
	}
	@Override
	public String getId() {
		return this.id;
	}


	/**
	 * 根据两列的内容，前一个为key，后一个为value返回一个以此构成的Map
	 * @param keyColumn
	 * @param valueColumn
	 * @return TreeMap
	 * @throws Exception
	 * @rmx.call {@link RsqlRvo#obtainMap(String, String)}
	 */
	@Override
	public TreeMap<String, String> getMapFromColumns(final String keyColumn,
			final String valueColumn) {
		// int keyIndex = this.getTitleIndex(keyColumn);
		// int valueIndex = this.getTitleIndex(valueColumn);
		// if (keyIndex < 0 || valueIndex < 0) {
		// return null;
		// }
		//
		// TreeMap<String, String> map = new TreeMap<String, String>();
		// int curCount = this.getRowCount();
		// for (int i = 0; i < curCount; i++) {
		// map.put(String.valueOf(this.getCell(i, keyIndex)), String
		// .valueOf(this.getCell(i, valueIndex)));
		// }
		/* 将以上功能扩展 */

		return this.getMapFromColumns(keyColumn, valueColumn, null, null);

	}

	/**
	 * 根据restrainColumn中的值=restrainValue从结果集中筛选响应的行，<br>
	 * 再输出两列的内容，前一个为key，后一个为value返回一个以此构成的Map
	 * @param keyColumn
	 * @param valueColumn
	 * @return Map<String,String>
	 * @throws Exception
	 * @rmx.call {@link RsqlRvo#getMapFromColumns(String, String)}
	 * @rmx.call {@link RsqlRvo#obtainMap(String, String, String, String)}
	 */
	@Override
	public TreeMap<String, String> getMapFromColumns(final String keyColumn,
			final String valueColumn, final String restrainColumn,
			final String restrainValue) {
		int keyIndex = this.getTitleIndex(keyColumn);
		int valueIndex = this.getTitleIndex(valueColumn);
		if (keyIndex < 0 || valueIndex < 0) {
			return null;
		}

		List<List<Object>> curRows = null == restrainColumn
				&& null == restrainValue ? this.rows : this.getRows(
				restrainColumn, restrainValue);

		TreeMap<String, String> map = new TreeMap<String, String>();
		if (null != curRows)
			for (List<Object> row : curRows) {
				map.put(String.valueOf(row.get(keyIndex)),
						String.valueOf(row.get(valueIndex)));
			}
		return map;
	}
	
	/**
	 * 获取结果集的map对象<br>
	 * 此方法将数据转化为key-val对应的数据结构。<br>
	 * 其中特别的把以.id 结尾的数据 单独赋值给外键。为了方便前段页面处理。
	 * @rmx.summary 如：Person表中有外键属性staff。则<br>
	 * staff.id=P00001<br>
	 * map中另外存了一份：staff=P00001<br>
	 * jqGrid中用上次数据了。
	 * @return list
	 * @rmx.call {@link RsqlRvo#getGridData()}
	 */
	@Override
	public List<Map<String, Object>> getMapRows() {
		if (null == this.rows) {
			return new ArrayList<Map<String, Object>>();
		}

		ArrayList<Map<String, Object>> alist = new ArrayList<Map<String, Object>>();
		Map<String, Type> fields = SqlType.getFields(beanClass, FieldType.TObject);
		Map<String, Integer> objectFields = new HashMap<String,Integer>(4);
		Map<Integer,String> objectIDFields = new HashMap<Integer, String>(4);
		for (int tIdx=0,size=this.titles.size();tIdx<size;tIdx++) {
			String title = this.titles.get(tIdx);
			if(title.endsWith(".id"))objectIDFields.put(Integer.valueOf(tIdx),title.substring(0, title.length()-3));
			//LHY 2014-9-24 识别出Object列,在下面赋值时将其转化为一个子Map
			if(fields.containsKey(title))objectFields.put(title,Integer.valueOf(tIdx));
		}
			
		for (List<Object> row : this.rows) {
			HashMap<String, Object> tcMap = new HashMap<String, Object>();
			int idx = 0;
			for (String title : this.titles) {
				if(objectFields.containsKey(title)){
					//LHY 2014-9-24 识别出Object列,在下面赋值时将其转化为一个子Map
					HashMap<String, Object> objMap = new HashMap<String, Object>(4);
					objMap.put(RsqlConstants.SYS_id, row.get(idx++));
					objMap.put(RsqlConstants.SYS_dataStatus, RsqlConstants.DS_part);
					tcMap.put(title, objMap);
				}else{
					tcMap.put(title, row.get(idx++));
				}
			}
			for (Integer tIdx : objectIDFields.keySet()){
				tcMap.put(objectIDFields.get(tIdx), row.get(tIdx));
			}
			alist.add(tcMap);
		}

		alist.trimToSize();
		return alist;
	}
	

	@Override
	public int getPagination() {
		return this.pagination;
	}
	@Override
	public int getRecordCount() {
		return this.recordCount;
	}

	@Override
	public int getRecords() {
		return getRecordCount();
	}
	@Override
	public int getRowCount() {
		return this.rowCount;
	}
	@Override
	public List<List<Object>> getRows() {
		return this.rows;
	}

	/**
	 * 通过columnIndex对应值=columnValue来检索一个条目,已经通过缓存进行优化了。
	 * @param columnIndex
	 * @param columnValue
	 * @return 如果没有则返回null
	 * @rmx.call {@link RsqlRvo#getCells(int, String, int)}
	 * @rmx.call {@link RsqlCore#refreshORMConstraints(RDBSpaceConfig)}
	 */
	@Override
	public List<List<Object>> getRows(final int columnIndex,
			final String columnValue) {
		if (columnIndex < 0) {
			return null;
		}
		if (null == this.mapRowsCache)
			this.mapRowsCache = new HashMap<Integer, Map<String, List<List<Object>>>>();
		Map<String, List<List<Object>>> curMapRowsCache = this.mapRowsCache
				.get(columnIndex);
		String curKey = columnIndex + "_" + columnValue;
		if (null == curMapRowsCache) {
			curMapRowsCache = new HashMap<String, List<List<Object>>>();
			this.mapRowsCache.put(columnIndex, curMapRowsCache);
		} else {
			List<List<Object>> needRows = curMapRowsCache.get(curKey);
			return needRows == null || needRows.size() == 0 ? null : needRows;
		}

		List<List<Object>> cycRows;

		for (List<Object> row : this.rows) {
			Object v = row.get(columnIndex);
			String cycKey = columnIndex + "_" + v;

			// 缓存
			if ((cycRows = curMapRowsCache.get(cycKey)) == null) {
				cycRows = new ArrayList<List<Object>>();
				curMapRowsCache.put(cycKey, cycRows);
			}
			cycRows.add(row);
			cycRows = null;
			cycKey = null;
		}

		List<List<Object>> needRows = curMapRowsCache.get(curKey);
		// for (int i = 0, max = this.rows.size(); i < max; i++) {
		// Object v = this.rows.get(i).get(columnIndex);
		// if (null != v && v.equals(columnValue)) {
		// needRows.add(this.rows.get(i));
		// }
		// }
		// needRows.trimToSize();
		return needRows == null || needRows.size() == 0 ? null : needRows;
	}

	/**
	 * 通过columnName=columnValue来检索一个条目
	 * @param columnName
	 * @param columnValue
	 * @return List
	 */
	@Override
	public List<List<Object>> getRows(final String columnName,
			final String columnValue) {
		return getRows(getTitleIndex(columnName), columnValue);
	}

	/**
	 * 获得该列明是第几列
	 * @param columnName
	 * @return int
	 * @rmx.call {@link RsqlRvo#getColumn(String)}
	 * @rmx.call {@link RsqlRvo#getMapFromColumns(String, String, String, String)}
	 * @rmx.call {@link RsqlRvo#getRows(String, String)}
	 */
	@Override
	public int getTitleIndex(String columnName) {
		columnName = columnName.toLowerCase();
		int i = 0, max = 0;
		for (max = this.titles.size(); i < max; i++) {
			if (this.titles.get(i).toLowerCase().equals(columnName)) {
				break;
			}
		}
		return i == max ? -1 : i;
	};
	
	@Override
	public List<String> getTitles() {
		return this.titles;
	}
	@Override
	public Map<String, String> getUserData() {
		return new HashMap<String, String>();
	}

	public boolean isOK() {
		return this.OK;
	}
	/* (non-Javadoc)
	 * @see cn.remex.db.DbRvo#assignRow(java.lang.Object)
	 */
	@Override
	public <T extends Modelable> void assignRow(T bean) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) bean.getClass();
		obtainBeans(clazz ,true,bean);
	}
	
	/**
	 * @return list
	 * @throws Exception
	 * @rmx.call {@link RsqlContainer#list(Modelable)}
	 * @rmx.call {@link RsqlContainer#queryBeanById(Class, Object)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> List<T> obtainBeans() {
		List<?> a = obtainBeans(this.beanClass,true,null);
		return (List<T>) a;
	}

	@Override
	public Map<String, String> obtainMap(final String keyColumn,
			final String valueColumn) {
		return getMapFromColumns(keyColumn, valueColumn);
	}
	
	@Override
	public Map<String, String> obtainMap(String keyColumn, String valueColumn,
			String restrainColumn, String restrainValue) {
		return getMapFromColumns(keyColumn, valueColumn, restrainColumn,
				restrainValue);
	}

	@Override
	public <T> List<T> obtainObjects(final Class<T> clazz) {
		return obtainBeans(clazz,false,null);
//		List<Map<String, Object>> list = getMapRows();
//		List<T> objects = new ArrayList<T>();
//
//		for (Map<String, Object> map : list) {
//			try {
//				T t = clazz.newInstance();
//				ReflectUtil.setProperties(t, map);
//				objects.add(t);
//			} catch (Exception e) {
//				ReflectUtil.handleReflectionException(e);
//			}
//		}
//
//		return objects;
	}

	@Override
	public <T> Map<String, T> obtainObjectsMap(final String columnName,
			final Class<T> clazz) {
		List<T> beans = this.obtainObjects(clazz);

		Map<String, T> ret = new HashMap<String, T>();

		for (T bean : beans) {
			ret.put(ReflectUtil.invokeGetter(columnName, bean).toString(), bean);
		}
		return ret;

	}

	// public <T> Map<String, T> obtainBeansMap(String columnName,
	// Class<T> clazz) throws Exception {
	// Map<String, T> map = new HashMap<String, T>();
	// Map<String, Method> baseSetter = SqlType.getSetters(clazz,
	// SqlType.TBase);
	// // List<Method> useSetters = getUseSetters(clazz, titles);
	// // 逐行放入数据
	// int rowCount=rows.size(),columnCount=titles.size();
	// for (int i = 0; i < rowCount; i++) {
	// T bean = clazz.newInstance();
	// map.put(getCell(i, columnName).toString(), bean);
	// }
	// for(int i = 0; i < columnCount; i++){
	// String key = titles.get(i);
	// List<Object> row = rows.get(i);
	// Method setter = baseSetter.get(key);
	// if(null!=setter){
	// for(int j = 0; j < rowCount; j++){
	// ReflectUtil.invokeSetterWithDefaultTypeCoventer(setter, map.get(key),
	// row.get(j));
	// }
	// }
	// }
	//
	//
	// // List<Object> r = rows.get(i);
	// // putValueToBean(r, bean, useSetters);
	// return map;
	// }
	/**
	 * 内部函数 用于根据RemexSqlCvo的要求把ResultSet里面的数据保存下来
	 * @rmx.summary 目前存在的问题：保存条目的总数this.allSize的时候是用的rs.last()&rs.getRow()效率极低。
	 * @param rs
	 * @param dbCvo
	 * @throws SQLException
	 * @throws Exception
	 * @rmx.call {@link RsqlDao#executeQuery(DbCvo)}
	 */
	public <T extends Modelable> void saveData(final ResultSet rs, final DbCvo<T> dbCvo)
			throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		// SqlBean<T> sqlBean = dbCvo.getSqlBean();

		// 读取所有列总数
		int columnCount = rsmd.getColumnCount();
		// 设置列名

		this.titles = new ArrayList<String>();
		for (int m = 0; m < columnCount; m++) {
//			System.out.println(rsmd.getColumnLabel(m + 1));
//			System.out.println(rsmd.getColumnName(m + 1));
			this.titles.add(rsmd.getColumnLabel(m + 1));
		}

		// 读取所有条目总数
		// long time = System.currentTimeMillis();
		// //*/
		// rs.last();
		// queryResult.setRecordCount(rs.getRow());
		// rs.beforeFirst();
		// /*/ if(sqlBean.getBeanName().startsWith("sqlString_")){
		// queryResult.setRecordCount(1);
		// }else{
		// Dialect d = RDBManager.getLocalSpaceConfig().getDialect();
		// RsqlCvo cvo = new
		// RsqlCvo("SELECT COUNT("+d.quoteKey("id")+") as "+d.quoteKey("recordCount")+" FROM "
		// + d.quoteKey(rsqlCvo.getBeanName()), null);
		// // RsqlCvo cvo = new
		// RsqlCvo("SELECT COUNT(*) as "+d.quoteKey("recordCount")+" FROM (" +
		// sqlBean.getSqlString()+")", rsqlCvo.getCvo().getParameters());
		// QueryResult RecordCountResult =
		// RsqlDao.getDefaultRemexDao().executeQuery(cvo).getQueryResult();
		// queryResult.setRecordCount(Integer.parseInt(RecordCountResult.getCell(0,
		// 0).toString()));
		// }
		// //*/
		// // 读取数据
		// // 如果RowCount==0说明在配置文件中是0并且表示所有数据全部读取
		// // 此时startID==endId==0
		// int startId = sqlBean.getRowCount() * (sqlBean.getPagination() - 1);
		// int endId = sqlBean.getRowCount() * sqlBean.getPagination();
		// /*/
		// if (startId != 0) {
		// rs.absolute(startId);
		// }
		// /*/
		// for(int i=0;i<startId;i++){
		// rs.next();
		// }
		// //*/
		// appendMsg("Read RowCount[").append(queryResult.getRecordCount()).append("] took:[")
		// .append(System.currentTimeMillis() - time).append("ms]");

		this.rows = new ArrayList<List<Object>>(dbCvo.getRowCount());

		// for (int i = startId; rs.next() && (i < endId || endId == 0); i++) {
		while (rs.next()) {
			List<Object> currentRow = new ArrayList<Object>(this.titles.size());
			for (int m = 1; m <= columnCount; m++) {
				// 此处可以设计以用于匹配各类数据
				int ct = rsmd.getColumnType(m);
				Object value = null;
				if (ct == Types.CLOB) {
					value = getClobColumn(rs, m);
				} else {
					value = rs.getObject(m);
				}

				currentRow.add(value);

				// 此处以bean的方式保存数据

				// 这是默认的if(sqlBean.isNeedBaseData())

			}
			this.rows.add(currentRow);
		}

		// 保存一些必要的辅助数据;
		setRowCount(this.rows.size());
		if (getRecordCount() == 0)// 总数据条数为0时需要将本次查询的所有行数设置为总查询数量
			setRecordCount(getRowCount());
		// pageId由页面指定给bean，bean给cvo，cvo保存至rvo
		setPagination(dbCvo.getPagination());

	}

	public void setBeanClass(final Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public void setEffectRowCount(final int effectRowCount) {
		this.effectRowCount = effectRowCount;
	}

	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param clazz
	 * @param returnModel 是否返回代理的bean
	 * @param targetBean 是否将值赋值给特定的一个对象。TODO 后续继续优化
	 * @return
	 */
	private <T> List<T> obtainBeans(Class<T> clazz,boolean returnModel,T targetBean) {
		if (this.rows == null)
			return new ArrayList<T>();
		RsqlCore.setLocalAutoStoreObjectFlied(false);// 此处是数据库操作，禁用自动保存的标志
		RsqlCore.setLocalAutoFecthObjectFiled(false);// 此处是数据库操作，禁用自动读取的标志
		ArrayList<T> list = new ArrayList<T>();
		// 逐行放入数据
		int rowCount = this.rows.size(), columnCount = this.titles.size();
		//int idIdx = this.titles.indexOf("id");
		RDBSpaceConfig spaceConfig = RDBManager.getSpaceConfig(this.rsqlCvo.getSpaceName());
		Method method = ReflectUtil.getMethod(RDBSpaceConfig.class, "getDBBean", Class.class);
		//ReflectFeatureStatus status = new ReflectFeatureStatus(null);
		boolean isModel = Modelable.class.isAssignableFrom(clazz); //LHY 2015-9-24 
		boolean _returnModel = returnModel && isModel ;//LHY 2015-2-17 
		for (int i = 0; i < rowCount; i++) {
			List<Object> row = this.rows.get(i);
			//Object id = row.get(idIdx);// id
			T bean;
			if(null!=targetBean){
				bean = targetBean;
			}else if(_returnModel){
				@SuppressWarnings("unchecked")
				T bean1 = (T)spaceConfig.getDBBean(clazz);
				bean = bean1;
			}else{
				bean = ReflectUtil.invokeNewInstance(clazz);
			}
			
			//关联必要的DBRVO
			if(isModel){
				((ModelableImpl)bean)._setDbRvo(this);
				((ModelableImpl)bean)._setRowNo(i);
			}

			for (int j = 0; j < columnCount; j++) {
				String expr = this.titles.get(j);
				Object value = row.get(j);
				//setValue(this.rsqlCvo.getSpaceName(), clazz, t, bean, value, i, status);
				
				if(null==value || "RN".equals(expr) || expr.startsWith("RMX_")) {
					continue;
				}
				if(returnModel)
					MapHelper.evalFlatValueToObjectField(bean, expr, value,spaceConfig,method);
				else
					MapHelper.evalFlatValueToObjectField(bean, expr, value);
			}
			if(_returnModel)
				((Modelable) bean).setDataStatus(rsqlCvo._isHasDataColums()?RsqlConstants.DS_part:RsqlConstants.DS_managed);//LHY 2015-2-17 
			
			if(null!=targetBean)//目前仅支持将第一行复制给一个特定的对象。
				return list;
			
			list.add(bean);
		}

		// 删除多余空间
		list.trimToSize();
		RsqlCore.setLocalAutoStoreObjectFlied(true);
		RsqlCore.setLocalAutoFecthObjectFiled(true);
		return list;
	}

	/**
	 * 根据表达式expr在查找属性，对类型为clazz的bean对应的属性赋值value。<br>
	 * <P>
	 * @rmx.summary expr的格式可以为标准的链式访问结构。<br>
	 * 以cn.remex.model.Person举例：
	 * <li>
	 * 1.简单值的赋值 <code>setValue(Person.class,"name",person,"张三丰");</code><br>
	 * 等价于<code>person.setName("张三丰");</code>
	 * <li>
	 * 2.类型为对象的属性赋值 <br>
	 * 2013-2-14 LHY 修订 思路：<BR>
	 * 1.如果此时bean中的属性expr没有值，则需要根据value（此时应为被赋值对象的id）从数据库中获取对象并赋值。<BR>
	 * 2.如果此时bean中的属性expr有值，则分两种情况。<BR>
	 * 1)已存在的对象的主键id和被赋值对象的id一致，则判断两个对象为同一个对象，不用赋值；<BR>
	 * 2)如果不一致，则从数据库中取值后赋值。<BR>
	 * </P>
	 * @param poolName
	 * @param clazz bean的类型，有可能targetbean为空，所以此处clazz不能为空。
	 * @param expr	要复制的链式字符串，如staff.name,name等。一般由属性"."组成。其中RN是关键字，作为oracle或者其他sql中的行号
	 * @param targetbean	要复制的目标bean
	 * @param value	要赋的值。如果是基本类型，则是数据本身。如果是外键表、对象，则是对象的主键值。
	 * @param rowIndex 当前数据来源的行号，用于创建object属性时获取object属性的id
	 * @param status 
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void setValue(final String poolName, final Class<?> clazz,
			final String expr, final Object targetbean, final Object value,
			final int rowIndex, ReflectFeatureStatus status) {
		if(null==value || "RN".equals(expr) || expr.startsWith("RMX_")) {
			return;
		}
		// 简单赋值，等价于object.setter，且赋值类型为九个基本类型。expr为属性的名称。
		Method setter = SqlType.getSetters(clazz, FieldType.TBase).get(expr);
		if (null != setter) {
			// ReflectUtil.invokeSetterWithDefaultTypeCoventer(setter, bean,
			// value);
			ReflectUtil.invokeSetterWithDefaultTypeCoventer(targetbean, setter,
					value, status);
			return;
		}
		// 简单赋值，等价于object.setter，且赋值类型为Object类型。expr为属性的名称。此时的value应为对应的bean的主键id。
		setter = SqlType.getSetters(clazz, FieldType.TObject).get(expr);
		if (null != setter) {
			Object pk = value; // 因为外键表再数据表中的值是其主键，要么为默认值-1要么为主键值
			Class<?> fieldType = (Class<?>) SqlType.getFields(clazz,
					FieldType.TObject).get(expr);
			Object objectValue = ReflectUtil.invokeMethod(
					SqlType.getGetters(clazz, FieldType.TObject).get(expr),
					targetbean);
			/**
			 * 2013-3-3LHY 修订1 思路：
			 * 1.如果此时bean中的属性expr没有值，则需要根据value（此时应为被赋值对象的id）从数据库中获取对象并赋值。
			 * 2.如果此时bean中的属性expr有值
			 * ，则分两种情况。1)已存在的对象的主键id和被赋值对象的id一致，则判断两个对象为同一个对象
			 * ，不用赋值；2)如果不一致，则从数据库中取值后赋值。 try { if(null==value){ value =
			 * RsqlCore.getDBBean( fieldType);
			 * ReflectUtil.invokeMethod(fieldType
			 * .getMethod(SYS_Method_setDataStatus, String.class), value,
			 * DS_beanOnlyId); }
			 * ReflectUtil.invokeMethod(fieldType.getMethod(SYS_Method_setId,
			 * Long.class), value, Long.parseLong(id.toString())); } catch
			 * (Exception e) { ReflectUtil.handleReflectionException(e); }
			 * ReflectUtil.invokeMethod(setter, bean, value);
			 */
			if (null == objectValue && !"-1".equals(pk)) {
				// .如果此时bean中的属性expr没有值，也没有主键， 则创建一个新的
				objectValue = RsqlCore.createDBBean(fieldType);
				((Modelable) objectValue).setDataStatus(RsqlConstants.DS_part);
				((Modelable) objectValue).setId(pk.toString());
			}
			// LHY 完成了Object自动获取的功能
			// else if (null != objectValue &&
			// pk.equals(ReflectUtil.invokeGetter(SYS_id, objectValue))) {
			// //objectValue = objectValue;
			// } else {
			// // 如果expr属性没有值，但有主键，则尝试从缓存中获取
			// // 如果expr属性有值，但主键不一致则从缓存中获取
			// /**
			// * LHY 2013-6-23
			// * 缓存暂时不用
			// objectValue =
			// RDBManager.getPool(poolName).getSpaceConfig().getDBBean(fieldType,
			// pk);
			// */
			// }
			ReflectUtil.invokeMethod(setter, targetbean, objectValue);
			// TODO 此时的bean为beanNew状态。可能会有隐患
			// 2013-3-3 LHY 修订1 END
			return;
		}
		// 属性为Object的链式赋值。类似person.getJob().setName("工作");可以用person.job.name
		Matcher matcher = simpleChian.matcher(expr);
		if (matcher.find()) {
			int idx = expr.indexOf('.');
			String exprHead = expr.substring(0, idx);// 前缀，如Staff类的属性有person，Person有属性name。此时为person.name中的person
			// 确保object属性非空
			Object beanHead = ReflectUtil.invokeMethod(
					SqlType.getGetters(clazz, FieldType.TObject).get(exprHead),
					targetbean);
			if (null == beanHead) {
				Object beanHeadId = getCell(rowIndex, exprHead + ".id");
				beanHeadId = null == beanHeadId ? "-1" : beanHeadId;
				setValue(poolName, clazz, exprHead, targetbean, beanHeadId,
						rowIndex, status); // 如果目标bean该object属性没有对象，则根据去id去创建一个
				beanHead = ReflectUtil.invokeGetter(exprHead, targetbean);
			}

			// 后续表达式回调
			String exprNext = expr.substring(idx + 1);
			if (exprNext.length() > 0) {
				Class<?> fieldType = (Class<?>) SqlType.getFields(clazz,
						FieldType.TObject).get(exprHead);
				if (null != beanHead)
					setValue(poolName, fieldType, exprNext, beanHead, value,
							rowIndex, status);
			}

		} else {
			throw new InvalidCharacterException("含有无效字符" + expr
					+ "，无法为bean设置value");
		}
	}

	protected void setPagination(final int pageCount) {
		this.pagination = pageCount;
	}

	protected void setRecordCount(final int recordCount) {
		this.recordCount = recordCount;
	}

	protected void setRowCount(final int rowCount) {
		this.rowCount = rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Modelable> List<T> obtainBeans(Class<? extends Modelable> modelClass) {
		return (List<T>) obtainBeans(modelClass, true,null);
	}

	@Override
	public List<?> obtainObjects() {
		List<?> list = obtainObjects(this.beanClass);
		return list;
	}

	public DbCvo<?> _getRsqlCvo() {
		return rsqlCvo;
	}

	@Override
	public <T> BeanVo<T> obtainBeanVo(Class<T> clazz) {
		BeanVo<T> beanVo = new BeanVo<T>();
		beanVo.setBeans(obtainObjects(clazz));
		setPagination(getPagination());
		setRowCount(getRowCount());
		setRecordCount(getRecordCount());
		return beanVo ;
	}

	@Override
	public MapVo obtainMapVo() {
		MapVo mapVo = new MapVo();
		mapVo.setRows(getMapRows());
		setPagination(getPagination());
		setRowCount(getRowCount());
		setRecordCount(getRecordCount());
		return null;
	}
}
