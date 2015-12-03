package cn.remex.db.sql;

import cn.remex.core.cache.DataCacheCloneable;
import cn.remex.core.cache.DataCachePool;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.db.DbCvo;
import cn.remex.db.cert.DataAccessConfiguration;
import cn.remex.db.exception.IllegalSqlBeanArgumentException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.model.SerialNoGenerator;

import java.sql.Types;
import java.util.*;

/** 
 * @author liuhengyang 
 * @version 版本号码
 * 
 * 本模块用于保存通过DbCvo生成的，可被Dao复用的Sql信息。主要包括Sql语句，命名参数。<br>
 * 其他参数化变量的值，查询条件的值，排序等均再DbCvo中动态指定。
 */
public class SqlBean<T extends Modelable> implements DataCacheCloneable {

	@SuppressWarnings("rawtypes")
	private static Map<String, DataCachePool> remexSqlBeanPools = new HashMap<String, DataCachePool>();

	static public void clearCache() {
		remexSqlBeanPools.clear();
	}

	/**
	 * 
	 //获取sqlBean，此操作会从缓存中读取一个sqlbean，
	 * //如果没有缓存则根据参数构建一个bean，并放入缓存，新建仅为一次，其他的都为克隆 //创建过程也是级联的。依次为
	 * //1.RemexSqlCvo->new SqlBean()->new SqlOption()->new SqlBeanOrder()
	 * //2.remexSqlOption.init(rscvo.getCvo());
	 * 
	 * @param <T>
	 * 
	 * @param dbCvo
	 * @return SqlBean
	 * @rmx.call {@link DbCvo#initParam()}
	 */
	public static <T extends Modelable> SqlBean<T> getInstance(
			final DbCvo<T> dbCvo) {
		SqlBean<T> sqlBean = null;
		String defName = SqlBean.obtianDefName(dbCvo);
		@SuppressWarnings("unchecked")
		DataCachePool<SqlBean<T>> remexSqlBeanPool = remexSqlBeanPools
				.get(defName);
		if (remexSqlBeanPool == null) {
			SqlOper oper = dbCvo.getOper();
			if (SqlOper.list.equals(oper) || SqlOper.view.equals(oper)) {
				sqlBean = RsqlUtils.createSelectSqlBean(dbCvo);
			} else if (SqlOper.add.equals(oper)) {
				sqlBean = RsqlUtils.createInsertSqlBean(dbCvo);
			} else if (SqlOper.edit.equals(oper)) {
				sqlBean = RsqlUtils.createUpdateSqlBean(dbCvo);
			} else if (SqlOper.del.equals(oper)) {
				sqlBean = RsqlUtils.createDeleteSqlBean(dbCvo);
			} else if (SqlOper.sql.equals(oper)) {
				sqlBean = RsqlUtils.createSqlStringSqlBean(dbCvo);
			} else {
				throw new IllegalSqlBeanArgumentException("oper 操作指令错误！");
			}

			remexSqlBeanPool = DataCachePool.createPool(sqlBean);

			//sqlBeanName中应该生成了和defName一致的名称否则应该报错
			Assert.isTrue(defName.equals(sqlBean.getName()), "创建SqlBean缓存时,发生检索名与创建缓存的key不一致，应该是程序异常！");
			remexSqlBeanPools.put(defName, remexSqlBeanPool); 
		}
		sqlBean = remexSqlBeanPool.get();

		// 初始化sqlBean的参数，每次调用都会执行，效率必须过关
		// 初始化参数是级联的。一次为RemexSqlCvo->SqlBean->SqlParam
		// sqlBean.initParam(rscvo);
		return sqlBean;
	}

	/**
	 * 根据cvo中的参数，也就是request或者object生成的cvo来定义一个唯一的鉴别名称 必要的时候会在initParam中重新定义一些参数
	 * @param <T>
	 * @param dbCvo
	 * @return String
	 * @throws Exception
	 */
	static public <T extends Modelable> String obtianDefName(
			final DbCvo<T> dbCvo) {
		StringBuilder defName = new StringBuilder();
		// PN_bn一般为表明，从前端获取的，如果是sql，在createSqlStringSqlBean中通过正则表达式解析获得的。
		// rscvo.getBeanName()，一般也是表明，如果为sql，则是sql的哈希值。此值默认在RsqlCvo中初始化
		defName.append(dbCvo.getBeanName()).append(dbCvo.getBeanName())
				.append("_oper[").append(dbCvo.getOper()).append("]_dt[")
				.append(dbCvo.getDataType()).append("]_fb[")
				.append(dbCvo.getForeignBean()).append("]_dc[")
				.append(dbCvo.getDataColumns()).append("]_ec[")
				.append(dbCvo.getExtColumn()).append("]_")
				.append(dbCvo.getSqlBeanWhere().obtainKey());
		return defName.toString();
	}

	@SuppressWarnings("unchecked")
	static protected <T extends Modelable> void freeInstance(
			final SqlBean<T> sqlBean) {
		remexSqlBeanPools.get(sqlBean.getName()).add(sqlBean);
	}

	private String beanName;
	private Class<?> beanClass;
	private String name;

	private int settedParamCount;
	private String litterSqlString;
	private String sqlString;
	private String prettySqlString;
	private HashMap<String, SqlBeanNamedParam> namedParamMap;
	private List<SqlBeanNamedParam> namedParams;
	/**
	 * 仅有的构造函数，所有的逻辑必须严格处理
	 * 
	 * @param dbCvo
	 */
	public SqlBean(final DbCvo<T> dbCvo) {
		super();
		ReflectUtil.copyProperties(this, dbCvo,new HashMap<String, String>());//必须调用空map的映射方法，避免数据库递归调用去查询映射。
		this.beanName = dbCvo.getBeanName();
		this.beanClass = dbCvo.getBeanClass();
		// 配置where
		this.namedParams = new ArrayList<SqlBeanNamedParam>();
		this.namedParamMap = new HashMap<String, SqlBeanNamedParam>();
		this.clear();
	}

	public void addNamedParam(String paramName, int type, Object value) {
		SqlBeanNamedParam param = new SqlBeanNamedParam(-1, paramName, type,
				value);
		this.namedParams.add(param);
		this.namedParamMap.put(paramName, param);
	}

	/**
	 * 新增一个
	 * 
	 * @param paramName
	 */
	public void addNamedParam(String paramName, String value) {
		addNamedParam(paramName, Types.CHAR, value);
	}

	public void clear() {
		// 清空参数
		for (SqlBeanNamedParam sqlNamedParam : this.namedParams) {
			sqlNamedParam.setValue(null);
		}

		this.settedParamCount = 0;

		if (this.name != null) {
			freeInstance(this);
		}

	}

	@Override
	public SqlBean<T> clone() throws CloneNotSupportedException {
		ArrayList<SqlBeanNamedParam> sqlNamedParamsCloned = new ArrayList<SqlBeanNamedParam>();
		HashMap<String, SqlBeanNamedParam> namedParamMapCloned = new HashMap<String, SqlBeanNamedParam>();
		for (SqlBeanNamedParam param : this.namedParams) {
			SqlBeanNamedParam paramCloned = param.clone();
			sqlNamedParamsCloned.add(paramCloned);
			namedParamMapCloned.put(paramCloned.getName(), paramCloned);
		}
		// 删除多余空间
		sqlNamedParamsCloned.trimToSize();
		this.settedParamCount = 0;
		@SuppressWarnings("unchecked")
		SqlBean<T> cloned = (SqlBean<T>) super.clone();
		cloned.namedParams = sqlNamedParamsCloned;
		cloned.namedParamMap = namedParamMapCloned;
		return cloned;
	}

	public Class<?> getBeanClass() {
		return this.beanClass;
	}

	public String getBeanName() {
		return this.beanName;
	}

	public String getLitterSqlString() {
		return this.litterSqlString;
	}

	public String getName() {
		return this.name;
	}

	public HashMap<String, SqlBeanNamedParam> getNamedParamMap() {
		return this.namedParamMap;
	}

	public List<SqlBeanNamedParam> getNamedParams() {
		return this.namedParams;
	}

	public String getPrettySqlString() {
		return this.prettySqlString;
	}

	public int getSettedParamCount() {
		return this.settedParamCount;
	}

	/**
	 * @return String
	 */
	public String getSqlString() {
		return this.sqlString;
	}

	public void init(final DbCvo<T> dbCvo, final String sqlString,
			final List<SqlBeanNamedParam> namedParams) {
		this.namedParams = new ArrayList<SqlBeanNamedParam>();
		this.initSqlString(sqlString, namedParams);// 这个必须用方法
		this.name = obtianDefName(dbCvo);
	}

	public void initParam(final DbCvo<T> dbCvo) {
//		// 配置param的值
		// 处理where约束条件
		if (dbCvo.isSearch()) {
			SqlBeanWhere curWhere = dbCvo.getSqlBeanWhere();
			// curWhere.toSQL(dbCvo.getBeanName(),
			// new ArrayList<SqlBeanNamedParam>(), 0);
			if (curWhere.isFilter()) {
				for (SqlBeanWhereRule rule : curWhere.getAllRules()) {
					dbCvo.$S(rule.getParamName(), rule.getData());
				}
			} else {
				dbCvo.$S(curWhere.getSearchField(), curWhere.getSearchString());
			}
		}

		// 配置参数

		// 初始化sql语句中需要的命名参数
		for (SqlBeanNamedParam namedParam : this.getNamedParams()) {
			// if (params.containsKey(namedParam.getName())) {
			String key = namedParam.getName();
			if (dbCvo.containsKey(key)) {
				Object paramValue = dbCvo.$V(key);
				this.settedParamCount = this.getSettedParamCount() + 1;
				namedParam.setValue(paramValue);
			}
		}

		// insert 语句添加自动主键
		if (null != dbCvo.getBean()
				&& this.sqlString.substring(0, 6).equalsIgnoreCase("INSERT")) {
			String id = ((SerialNoGenerator) dbCvo.getBean()).generateId();
			// dbCvo.$S(SYS_id, id);
			dbCvo.getBean().setId(id);
			dbCvo.setId(id);
			this.namedParamMap.get(RsqlConstants.SYS_id).setValue(id);
		}
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	// /**
	// * 用于程序里直接执行查询的。
	// *
	// * @param parameters
	// * @param namedParamss
	// * @throws Exception
	// */
	// public void initParam(final Map<String, Object> parameters, final
	// List<SqlBeanNamedParam> namedParams){
	// Cvo cvo = new Cvo();
	// this.namedParams = namedParams;
	// where = new SqlBeanWhere();
	// cvo.setParameters(parameters);
	// initParam(cvo);
	// }

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setLitterSqlString(String litterSqlString) {
		this.litterSqlString = litterSqlString;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setSpaceName(String spaceName) {
//		this.spaceName = spaceName;
//	}
//
//	public void setTableName(String tableName) {
//		this.tableName = tableName;
//	}

	@Override
	public String toString() {
		return this.name;
	}

	private void initSqlString(final String sqlString,
			final List<SqlBeanNamedParam> namedParams) {
		this.sqlString = sqlString;
		// 初始化sqlString
		// 排序必须放在这里没有办法
		this.prettySqlString = DataAccessConfiguration.assignAuditedSql(this);
		this.sqlString = this.prettySqlString.replaceAll("\\s+", " ");
		this.litterSqlString = this.sqlString.length() > 20 ? this.sqlString
				.substring(0, 20) + "..." : this.sqlString;

		// 先初始化参数名称和类型
		if (null != namedParams) {
			for (SqlBeanNamedParam param : namedParams) {
				SqlBeanNamedParam paramNew = new SqlBeanNamedParam(-1,
						param.getName(), param.getType(), null);
				this.namedParams.add(paramNew);
				this.namedParamMap.put(paramNew.getName(), paramNew);
			}
		}
		// 在初始化参数的序号
		TreeMap<Integer, String> paramIndexs = RsqlUtils.obtainNamedParamIndexs(this.sqlString);
		for (SqlBeanNamedParam param : this.namedParams) {
			Integer okIdx = null;
			for(Integer idx:paramIndexs.keySet()){
				if(param.getName().equals(paramIndexs.get(idx))){
					okIdx = idx;
					break;
				}
			}
			if(null!=okIdx){
				paramIndexs.remove(okIdx);
				param.setIndex(okIdx);
			}
			
		}

		// 调用RsqlAcessEngine将sql转化为通过验证数据权限的sql
		// TODO

		this.sqlString = RsqlUtils.obtainNamedSql(this.sqlString);
	}

	public boolean namedParamsIsReady() {
		
		int namedParamCount = (" " + getSqlString() + " ").split("\\u003F").length - 1;
		
		return namedParamCount <= getSettedParamCount();
	}

}
