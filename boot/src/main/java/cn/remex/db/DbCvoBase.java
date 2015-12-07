package cn.remex.db;

import cn.remex.core.CoreCvo;
import cn.remex.core.CoreSvo;
import cn.remex.core.aop.AOPCaller;
import cn.remex.core.aop.AOPFactory;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.reflect.ReflectUtil.SPFeature;
import cn.remex.core.util.Assert;
import cn.remex.core.util.StringHelper;
import cn.remex.db.exception.RsqlInitException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.*;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.oro.text.regex.MatchResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbCvoBase<T extends Modelable> extends CoreCvo {
	public static final String PredicateBean_Invoked_Field = DbCvoBase.class.getName()+".PredicateBean_Invoked_Field";

    /**
     * 此工厂用于通过innerClass方式,new DbCvo(){ publilc void initRule(T t){}; }中为initRules提供被代理的T
     */
	protected static AOPFactory InnerDbCvoArgBeanFactory = new AOPFactory(
			new AOPCaller(null) {
				private static final long serialVersionUID = 5835742402538111065L;

				@Override
				public Object intercept(final Object obj, final Method method,
						final Object[] args, final MethodProxy proxy)
						throws Throwable {
					// 查找
					String name = method.getName();
					if (name.startsWith("get")
							&& method.getParameterTypes().length == 0) {// predicate
																		// 中用get的方法来获得sql中需要操作的field
						Assert.isNull(CoreSvo.$VL(PredicateBean_Invoked_Field),
								"请在addrule方法的参数中直接使用变量或值，或者至少保证其参数不能是自动查询数据库的，否则将在rule的生成逻辑中出现嵌套错误！");
						CoreSvo.$SL(PredicateBean_Invoked_Field,StringHelper.lowerFirstLetter(name.substring(3)));

					}
					// 返回真实的调用
					return proxy.invokeSuper(obj, args);
				}
			});
	protected static AOPFactory DbCvoFactory = new AOPFactory(
			new AOPCaller(null) {
				@Override
				public Object intercept(final Object obj, final Method method,
										final Object[] args, final MethodProxy proxy)
						throws Throwable {
					// 查找
					String name = method.getName();
					if (name.startsWith("get")
							&& method.getParameterTypes().length == 0) {// predicate
						// 中用get的方法来获得sql中需要操作的field
						Assert.isNull(CoreSvo.$VL(PredicateBean_Invoked_Field),
								"请在addrule方法的参数中直接使用变量或值，或者至少保证其参数不能是自动查询数据库的，否则将在rule的生成逻辑中出现嵌套错误！");
						CoreSvo.$SL(PredicateBean_Invoked_Field,StringHelper.lowerFirstLetter(name.substring(3)));

					}
					// 只是为了截获属性名称,不用真实的调用
					return null;
				}
			});
	/**
	 * 用于匹配StringParam化的sql语句中的参数。
	 * <li>原整个sql字符串:group(0)
	 * <li>含有$后缀的参数 :group(1)
	 * <li>参数名 :group(2)
	 * <li>参数名 :group(3)  Flag
	 * <li>最小长度:group(4) width
	 * <li>最大长度:group(6) precision
	 * <li>conversion:group(7)
	 */
	protected static String stringParamRegx = "%((\\w{2,})\\$)?([\\-#+ 0,\\(]?)(\\d*?)(\\.(\\d*))?([bscdf])";
	protected static String stringParamRegxForReplace = "$3$4$5$7";


	/**
	 * 此方法通过切面，在目标对象属性调用getter时， 捕获属性的名称作为sql语句中where条件的参数。
	 * 在一次whererule的创建中只能用一次。
	 *
	 * @param field 如果线程中没有截获到getter方法调用,将使用本参数当做属性
	 * @return String
	 */
	public static String obtainPredicateBeanField(final Object field) {
		String fieldName = (String) CoreSvo.$VL(PredicateBean_Invoked_Field);
		if (fieldName == null && field instanceof String) {
			Assert.notNull(field, "SqlPredicate没有查询可必要的属性！");
			fieldName = (String) field;
		}
		CoreSvo.$SL(PredicateBean_Invoked_Field,null);
		return fieldName;
	}
	/**
	 * 官方的写法：%[argument_<font color=green>index</font>$][flags][width][.precision]conversion
	 *
	 * <p>平台改进为：%[argument_<font color=red>name</font>$][flags][width][.precision]conversion
	 *
	 * <p>为安全期间，本方法仅支持替换 字符串、数字、布尔值
	 * conversion的取值为
	 * <li>布尔值b
	 * <li>字符串s,Unicode 字符:c
	 * <li>十进制整数:d
	 * <li>十进制浮点:f
	 *
	 * <pre>
标志	常规	字符	整数	浮点	日期/时间	说明
'-'	y	y	y	y	y	结果将是左对齐的。
'#'	y1	-	y3	y	-	结果应该使用依赖于转换类型的替换形式
'+'	-	-	y4	y	-	结果总是包括一个符号
'  '	-	-	y4	y	-	对于正值，结果中将包括一个前导空格
'0'	-	-	y	y	-	结果将用零来填充
','	-	-	y2	y5	-	结果将包括特定于语言环境的组分隔符
'('	-	-	y4	y5	-	结果将是用圆括号括起来的负数
1 取决于 Formattable 的定义。
2 只适用于 'd' 转换。
3 只适用于 'o'、'x' 和 'X' 转换。
4 对 BigInteger 应用 'd'、'o'、'x' 和 'X' 转换时，或者对 byte 及 Byte、short 及 Short、int 及 Integer、long 及 Long 分别应用 'd' 转换时适用。
5 只适用于 'e'、'E'、'f'、'g' 和 'G' 转换。
	 * @param orgnSql
	 * @param params
	 * @return
	 * 参阅官方文档 <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax">conversion</a>
	 */
	public static String StringParameterize(String orgnSql,Map<String,Object> params){
//		 orgnSql = "%sss$s   ";
//		 stringParamRegx = "%((\\w{2,})\\$)?([\\-#+ 0,\\(]?)(\\d*?)(\\.(\\d*))?([bscdf])";
		 MatchResult rs;
		int count=0;
		List<Object> args = new ArrayList<Object>();
		while(null!=(rs = StringHelper.match(orgnSql, stringParamRegx, null))){
			count++;
			String pname = rs.group(2);//参数名
			args.add(params.get(pname));
			orgnSql = StringHelper.substitute(orgnSql, stringParamRegx, "%"+count+"\\$"+stringParamRegxForReplace, 1);
		}
		String s = String.format(orgnSql, args.toArray());

		return s;

	}
	protected boolean _hasDataColums;

	protected String _searchById;
	protected Modelable bean;

	protected Class<T> beanClass;
	protected String beanName;

	protected String dataColumns;		//TODO 正在完善，目前已经对base，object，list有效，但逻辑尚未完全验证。
	protected String dataType = RsqlConstants.DT_base + RsqlConstants.DT_object; // pickup querybyid均默认查询对象属性。
	protected boolean deleteByWhere;
	protected boolean doCount = false;

	protected boolean doPaging = false;
	protected String extColumn;
	protected String foreignBean;
	protected String id;
	protected boolean isInit = false;
	/**
	 * 用来保存操作状态的
	 */
	protected boolean OK = false;
	protected SqlOper oper = SqlOper.list;
	protected List<SqlBeanOrder> orders = new ArrayList<SqlBeanOrder>();

	protected int pagination;
	protected String poolName;
	protected int recordCount;
	protected String returnType;//返回的数据类型
	protected int rowCount;
	protected boolean search = false;
	protected boolean sortable = false;
	protected String spaceName;

	protected SqlBean<T> sqlBean;
	protected Container container;

	protected SqlBeanWhere sqlBeanWhere = new SqlBeanWhere();

	protected String sqlString;
	protected String subList;

	protected boolean updateByWhere;

    public boolean _isHasDataColums() {
		return _hasDataColums;
	}

	/**
	 * @param sqlBeanWhere
	 */
	public void addGroup(SqlBeanWhere sqlBeanWhere) {
		this.search = true;
		this.sqlBeanWhere.addGroup(sqlBeanWhere);
	}

	public void addOrder(final boolean sortable, final Object sidx,
			final String sord) {
		this.sortable = sortable;
		String rf = obtainPredicateBeanField(sidx);
		this.orders.add(new SqlBeanOrder(sortable, rf, sord));
	}

	/**
	 * 向默认组中新增一条where判断规则。
	 * @param field
	 * @param ruleOper
	 * @param value
	 * @see WhereRuleOper
	 */
	public void addRule(final String field, final WhereRuleOper ruleOper,
			final String value) {
		this.search = true;
		this.sqlBeanWhere.addRule(field, ruleOper, value);
	}

	public Container ready(){
		return this.container;
	}

	public boolean check() {
		Assert.isTrue(isInit, "在执行RsqlDao.execute*(DbCvo)方法前必须调用DbCvo.initParams()!");
		
		if (isOK())
			return isOK();
		else {
			// \\uoo3f是问号 '?'的转义
			// 本语句的功能是检查
			// poolName不能为空；sqlString不能为空；sqlString中需要设置的参数(即包含的'?')
			// 必须与与之对应的设置参数param的个数一致
			boolean OK = this.spaceName != null
					&& this.sqlBean.getSqlString() != null;
//			String dataOption = this.sqlBean.getDataOption();---20140617
			// int i = (" "+ this.sqlBean.getSqlString() +
			// " ").split("\\u003F").length - 1;
			if (! (SqlOper.add.equals(this.oper) || SqlOper.edit.equals(this.oper))){//---20140617
				OK = OK	&& this.sqlBean.namedParamsIsReady();
			}
			setOK(OK);
			return OK;
		}
	}
	
	public void clear() {
		// 回收机制
		this.sqlBean.clear();
		this.sqlBean = null;
	}
	public void setContainer(Container container) {
		this.container = container;
	}
	public String get_searchById() {
		return _searchById;
	}

	public Modelable getBean() {
		return this.bean;
	}
	public Class<T> getBeanClass() {
		return this.beanClass;
	}

	public String getBeanName() {
		return this.beanName;
	};
	public String getDataColumns() {
		return this.dataColumns;
	}

	public String getDataType() {
		return this.dataType;
	}
	public String getExtColumn() {
		return this.extColumn;
	}

	public String getForeignBean() {
		return this.foreignBean;
	}
	public String getId() {
		return this.id;
	}

	public SqlOper getOper() {
		return oper;
	}
	public List<SqlBeanOrder> getOrders() {
		return this.orders;
	}

	public int getPagination() {
		return this.pagination;
	}
	public String getPoolName() {
		return poolName;
	}

	public int getRecordCount() {
		return this.recordCount;
	}
	public String getReturnType() {
		return returnType;
	}

	public int getRowCount() {
		return this.rowCount;
	}
	public String getSpaceName() {
		return this.spaceName;
	}

	public SqlBean<T> getSqlBean() {
		return this.sqlBean;
	}
	public SqlBeanWhere getSqlBeanWhere() {
		return this.sqlBeanWhere;
	}
	
	public String getSqlString() {
		return this.sqlString;
	}
	public String getSubList() {
		return subList;
	}

    /**
     * init方法仅且必须被所有DbCvo的构造函数调用.
     * @param clazz
     */
	@SuppressWarnings("unchecked")
	public void init(final Class<T> clazz) {
		Class<T> clazz1=clazz;
		if(null != clazz1){
			try {
				clazz1 = (Class<T>) Class.forName(StringHelper.getClassName(clazz1));
			} catch (ClassNotFoundException e) {
				throw new RsqlInitException("初始化DbCvo时,beanClass异常！",e);
			}
			T t = InnerDbCvoArgBeanFactory.getBean(clazz1);
			initRules(t);
			this.beanName = StringHelper.getClassSimpleName(clazz1);
		}else{
			this.beanName = "sqlString_" + sqlString.hashCode();
		}

		this.beanClass = clazz1;

		this.spaceName = null == this.spaceName ? RDBManager.getLocalSpaceConfig().getSpace() : this.spaceName;
	}

    /**
     * 用于被init方法调用.该方法可以以inner方式重载实现.
     * @param t
     */
	public void initRules(T t) {}

	public boolean isDeleteByWhere() {
		return deleteByWhere;
	}

	public boolean isDoCount() {
		return this.doCount;
	}

	public boolean isDoPaging() {
		return this.doPaging;
	}

	public boolean isOK() {
		return this.OK;
	}
	public boolean isSearch() {
		return this.search;
	}

	public boolean isSortable() {
		return this.sortable;
	}
	public boolean isUpdateByWhere() {
		return updateByWhere;
	}

	/**
	 * @return Map
	 */
	public Map<String, Object> obtainParameters() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(RsqlConstants.PN_search, String.valueOf(this.search));
		params.put(RsqlConstants.PN_oper, this.oper);
		params.put(RsqlConstants.PN_dt, this.dataType);
		params.put(RsqlConstants.PN_ec, this.extColumn);

		params.put(RsqlConstants.PN_rowCount, String.valueOf(this.rowCount));
		params.put(RsqlConstants.PN_pagination, String.valueOf(this.pagination));
		params.put(RsqlConstants.PN_bn, this.beanName);
		params.put(RsqlConstants.PN_id, this.id);
		if (null != this.dataColumns) {
			params.put(RsqlConstants.PN_dc, this.dataColumns);
		}
		if (this.search) {
			params.put(RsqlConstants.PN_search, "true");
			params.put(RsqlConstants.PN_filterBean, this.sqlBeanWhere);
		}
		if (this.sortable) {// 此处新增多列排序功能 TODO
			params.put(RsqlConstants.PN_sidx, this.orders.get(0).getSidx());
			params.put(RsqlConstants.PN_sord, this.orders.get(0).getSord());
		}
		return params;
	}
	@Override
	public void putParameters(Map<String, Object> map) {
		if($V("oper")!=null)this.oper = $V("oper");
		if($V("id")!=null)this.id = $V("id");
		map.put(RsqlConstants.SYS_dataStatus,RsqlConstants.DS_managed);//datastatus列必须处理。
		
		super.putParameters(map);
	}

	public void set_searchById(String _searchById) {
		this._searchById = _searchById;
	}
	public void setBean(Modelable dbBean) {
		this.bean = dbBean;
	}

	public void setBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}

	public void setDataColumns(final String dataColumns) {
		this._hasDataColums = true;
		this.dataColumns = dataColumns;
	}
	public void setDataType(final String dataType) {
		this.dataType = dataType;
	}
	
	public void setDeleteByWhere(boolean deleteByWhere) {
		this.deleteByWhere = deleteByWhere;
	}
	public void setDoCount(boolean doCount) {
		this.doCount = doCount;
	}

	public void setDoPaging(boolean doPaging) {
		this.doPaging = doPaging;
	}

	public void setExtColumn(final String extColumn) {
		this.extColumn = extColumn;
	}

	public void setForeignBean(String foreignBean) {
		this.foreignBean = foreignBean;
	}

	public void setGroupOp(WhereGroupOp groupOp) {
		this.sqlBeanWhere.setGroupOp(groupOp);
	}

	public void setId(final String id) {
		$S(RsqlConstants.SYS_id, id);
		this.id = id;
	}

	public void setOK(final boolean status) {
		this.OK = status;
	}
	

	public void setOper(SqlOper oper) {
		this.oper = oper;
	}

	public void setOrders(final List<SqlBeanOrder> orders) {
		this.orders = orders;
	}

	public void setPagination(final int pagination) {
		this.pagination = pagination;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public void setRowCount(final int rowCount) {
		this.rowCount = rowCount;
	}

	public void setSearch(final boolean search) {
		this.search = search;
	}

	public void setSortable(final boolean sortable) {
		this.sortable = sortable;
	}
	public void setSpaceName(final String poolName) {
		this.spaceName = poolName;
	}
	/**
	 * 通过ReflectUtils替换默认的SqlBeanWhere中的值。
	 * @param sqlBeanWhere
	 */
	public void setSqlBeanWhere(SqlBeanWhere sqlBeanWhere) {
		ReflectUtil.copyProperties(this.sqlBeanWhere, sqlBeanWhere,SPFeature.DeeplyCopy);
		this.search = this.sqlBeanWhere.isSearch();
	}
	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}
	public void setSubList(String subList) {
		this.subList = subList;
	}
	public void setUpdateByWhere(boolean updateByWhere) {
		this.updateByWhere = updateByWhere;
	}

	/************************************************************/

}
