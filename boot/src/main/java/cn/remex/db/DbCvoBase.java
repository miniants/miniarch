package cn.remex.db;

import cn.remex.core.CoreCvo;
import cn.remex.core.CoreSvo;
import cn.remex.core.aop.AOPCaller;
import cn.remex.core.aop.AOPFactory;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.reflect.ReflectUtil.SPFeature;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Param;
import cn.remex.core.util.StringHelper;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.NamedParam;
import cn.remex.db.sql.Order;
import cn.remex.db.sql.SqlColumn;
import cn.remex.db.sql.Where;
import cn.remex.db.sql.WhereRuleOper;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.oro.text.regex.MatchResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbCvoBase<T extends Modelable> extends CoreCvo {
	public static final String PredicateBean_Invoked_Field = DbCvoBase.class.getName() + ".PredicateBean_Invoked_Field";

	protected static AOPFactory DbCvoFactory = new AOPFactory(
			new AOPCaller(null) {
				@Override
				public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
					// 查找
					String name = method.getName();
					if (name.startsWith("get")
							&& method.getParameterTypes().length == 0) {// predicate
						// 中用get的方法来获得sql中需要操作的field
						if (null != CoreSvo.valLocal(PredicateBean_Invoked_Field)) {
							CoreSvo.putLocal(PredicateBean_Invoked_Field, null);
							throw new RsqlException(ServiceCode.RSQL_SQL_ERROR, "请在addrule方法的参数中直接使用变量或值，或者至少保证其参数不能是自动查询数据库的，否则将在rule的生成逻辑中出现嵌套错误！");
						} else {
							CoreSvo.putLocal(PredicateBean_Invoked_Field, StringHelper.lowerFirstLetter(name.substring(3)));
						}

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
	protected boolean _hasDataColums;
	protected boolean _hasGroupBy = false;
	protected boolean _hasAggregateBy = false;
	protected Param _paramIndex = null;
	protected Param _tableIndex = null;
	protected String _tableAliasName = null;
	protected List<NamedParam> _namedParams;
	protected String _sqlString;
	protected String _prettySqlString;
	protected String _litterSqlString;
	protected boolean _isSubStatment;
	protected Container container;
	protected String spaceName;
	/**
	 * 用来保存操作状态的
	 */
	protected boolean OK = false;
	protected SqlOper oper = SqlOper.list;
	protected SqlColumn<T, T, T> rootColumn;
	/**
	 * 用于通过切面捕获lambda表达式中调用get方法而获得field的.
	 */
	protected T _aopBean;
	protected String _searchById;
	//====内部属性======================//
	//基于lambda开发的数据操作功能而新增的属性，后面将以上功能逐步迁移过来结构化
	protected Modelable bean;
	protected Class<T> beanClass;
	protected String beanName;
	protected String dataColumns;        //TODO 正在完善，目前已经对base，object，list有效，但逻辑尚未完全验证。
	protected String dataType = RsqlConstants.DT_base + RsqlConstants.DT_object; // pickup querybyid均默认查询对象属性。
	protected boolean deleteByWhere;
	protected boolean doCount = false;
	protected boolean doPaging = false;
	protected String extColumn;
	//protected String foreignBean;
	protected String id;
	protected boolean isInit = false;
	protected List<Order> orders = new ArrayList<Order>();
	protected int pagination = 1;
	protected String poolName;
	protected int recordCount;
	protected String returnType;//返回的数据类型
	protected int rowCount = 100;
	protected boolean search = false;
	protected boolean sortable = false;
	protected Where<T, T> filter = new Where();
	protected String sqlString;
	protected String subList;
	protected boolean updateByWhere;

	public static <T> T createAOPBean(Class<T> beanClass) {
		return DbCvoFactory.getBean(beanClass);
	}

	/**
	 * 官方的写法：%[argument_<font color=green>param</font>$][flags][width][.precision]conversion
	 * <p>
	 * <p>平台改进为：%[argument_<font color=red>name</font>$][flags][width][.precision]conversion
	 * <p>
	 * <p>为安全期间，本方法仅支持替换 字符串、数字、布尔值
	 * conversion的取值为
	 * <li>布尔值b
	 * <li>字符串s,Unicode 字符:c
	 * <li>十进制整数:d
	 * <li>十进制浮点:f
	 * <p>
	 * <pre>
	 * 标志	常规	字符	整数	浮点	日期/时间	说明
	 * '-'	y	y	y	y	y	结果将是左对齐的。
	 * '#'	y1	-	y3	y	-	结果应该使用依赖于转换类型的替换形式
	 *
	 * '+'	-	-	y4	y	-	结果总是包括一个符号
	 * '  '	-	-	y4	y	-	对于正值，结果中将包括一个前导空格
	 * '0'	-	-	y	y	-	结果将用零来填充
	 * ','	-	-	y2	y5	-	结果将包括特定于语言环境的组分隔符
	 * '('	-	-	y4	y5	-	结果将是用圆括号括起来的负数
	 * 1 取决于 Formattable 的定义。
	 * 2 只适用于 'd' 转换。
	 * 3 只适用于 'o'、'x' 和 'X' 转换。
	 * 4 对 BigInteger 应用 'd'、'o'、'x' 和 'X' 转换时，或者对 byte 及 Byte、short 及 Short、int 及 Integer、long 及 Long 分别应用 'd' 转换时适用。
	 * 5 只适用于 'e'、'E'、'f'、'g' 和 'G' 转换。
	 * @param orgnSql
	 * @param params
	 * @return
	 * 参阅官方文档 <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax">conversion</a>
	 */
	public static String StringParameterize(String orgnSql, Map<String, Object> params) {
//		 orgnSql = "%sss$s   ";
//		 stringParamRegx = "%((\\w{2,})\\$)?([\\-#+ 0,\\(]?)(\\d*?)(\\.(\\d*))?([bscdf])";
		MatchResult rs;
		int count = 0;
		List<Object> args = new ArrayList<Object>();
		while (null != (rs = StringHelper.match(orgnSql, stringParamRegx, null))) {
			count++;
			String pname = rs.group(2);//参数名
			args.add(params.get(pname));
			orgnSql = StringHelper.substitute(orgnSql, stringParamRegx, "%" + count + "\\$" + stringParamRegxForReplace, 1);
		}
		String s = String.format(orgnSql, args.toArray());

		return s;

	}

	public List<NamedParam> _getNamedParams() {
		return _namedParams;
	}

	public Param<Integer> _getParamIndex() {
		return _paramIndex;
	}

	public String _getPrettySqlString() {
		return this._prettySqlString;
	}

	public SqlColumn<T, T, T> _getRootColumn() {
		return rootColumn;
	}

	public String _getSpaceName() {
		return this.spaceName;
	}

	public String _getSqlString() {
		return this._sqlString;
	}

	public String _getTableAliasName() {
		return _tableAliasName;
	}

	public Param<Integer> _getTableIndex() {
		return _tableIndex;
	}

	public boolean _isHasAggregateBy() {
		return _hasAggregateBy;
	}

	public boolean _isHasDataColums() {
		return _hasDataColums;
	}

	public boolean _isHasGroupBy() {
		return _hasGroupBy;
	}

	public boolean _isSubStatment() {
		return _isSubStatment;
	}

	public T _obtainAOPBean() {
		return _aopBean != null ? _aopBean : (_aopBean = ReflectUtil.createAopBean(beanClass));
	}

	public void _setHasAggregateBy(boolean _hasAggregateBy) {
		this._hasAggregateBy = _hasAggregateBy;
	}

	public void _setHasGroupBy(boolean b) {
		_hasGroupBy = b;
	}

	public void _setNamedParams(List<NamedParam> namedParams) {
		this._namedParams = namedParams;
	}

	public void _setParamIndex(Param<Integer> paramIndex) {
		this._paramIndex = paramIndex;
	}

	public void _setSpaceName(final String poolName) {
		this.spaceName = poolName;
	}

	public void _setSqlString(String sqlString) {
		this._sqlString = sqlString;
	}

	public void _setTableAliasName(String tableAliasName) {
		this._tableAliasName = tableAliasName;
	}

	public void _setTableIndex(Param<Integer> tableIndex) {
		this._tableIndex = tableIndex;
	}

	/**
	 * @param where
	 */
	public void addGroup(Where where) {
		this.search = true;
		this.filter.addGroup(where);
	}

	public void addOrder(final boolean sortable, final Object sidx,
	                     final String sord) {
		this.sortable = sortable;
		String rf = obtainPredicateBeanField(sidx);
		this.orders.add(new Order(sortable, rf, sord));
	}

	/**
	 * 此方法通过切面，在目标对象属性调用getter时， 捕获属性的名称作为sql语句中where条件的参数。
	 * 在一次whererule的创建中只能用一次。
	 *
	 * @param field 如果线程中没有截获到getter方法调用,将使用本参数当做属性
	 * @return String
	 */
	public static String obtainPredicateBeanField(final Object field) {
		String fieldName = (String) CoreSvo.valLocal(PredicateBean_Invoked_Field);
		if (fieldName == null && field instanceof String) {
			Assert.notNull(field, ServiceCode.FAIL, "SqlPredicate没有查询可必要的属性！");
			fieldName = (String) field;
		}
		CoreSvo.putLocal(PredicateBean_Invoked_Field, null);
		return fieldName;
	}

	/**
	 * 向默认组中新增一条where判断规则。
	 *
	 * @param field
	 * @param ruleOper
	 * @param value
	 * @see WhereRuleOper
	 */
	public void addRule(final String field, final WhereRuleOper ruleOper,
	                    final Object value) {
		this.search = true;
		this.filter.addRule(field, ruleOper, value);
	}

//	public boolean check() {
//		Assert.isTrue(isInit, ServiceCode.FAIL,  "在执行RsqlDao.execute*(DbCvo)方法前必须调用DbCvo.initParams()!");
//
//		if (isOK())
//			return isOK();
//		else {
//			// \\uoo3f是问号 '?'的转义
//			// 本语句的功能是检查
//			// poolName不能为空；sqlString不能为空；sqlString中需要设置的参数(即包含的'?')
//			// 必须与与之对应的设置参数param的个数一致
//			boolean OK = this.spaceName != null
//					&& this.sqlBean.getSqlString() != null;
////			String dataOption = this.sqlBean.getDataOption();---20140617
//			// int i = (" "+ this.sqlBean.getSqlString() +
//			// " ").split("\\u003F").length - 1;
//			if (! (SqlOper.add.equals(this.oper) || SqlOper.edit.equals(this.oper))){//---20140617
//				OK = OK	&& this.sqlBean.namedParamsIsReady();
//			}
//			setOK(OK);
//			return OK;
//		}
//	}

	public void clear() {
		// 回收机制
//		this.sqlBean.clear();
//		this.sqlBean = null;
	}

	public Modelable getBean() {
		return this.bean;
	}

	public void setBean(Modelable dbBean) {
		this.bean = dbBean;
	}

	public Class<T> getBeanClass() {
		return this.beanClass;
	}

	public void setBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
	}

	;

	public String getBeanName() {
		return this.beanName;
	}

	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
		this.spaceName = this.container.getSpaceName();
	}

	public String getDataColumns() {
		return this.dataColumns;
	}

	public void setDataColumns(final String dataColumns) {
		this._hasDataColums = true;
		this.dataColumns = dataColumns;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(final String dataType) {
		this.dataType = dataType;
	}

	public Where<T,T> getFilter() {
		return this.filter;
	}

	public void setFilter(Where filter) {
		/**
		 * 通过ReflectUtils替换默认的SqlBeanWhere中的值。
		 *
		 * @param filter
		 */
		ReflectUtil.copyProperties(this.filter, filter, SPFeature.DeeplyCopy);
		this.search = this.filter.isSearch();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		$S(RsqlConstants.SYS_id, id);
		this.id = id;
	}

	public SqlOper getOper() {
		return oper;
	}

	public void setOper(SqlOper oper) {
		this.oper = oper;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(final List<Order> orders) {
		this.orders = orders;
	}

	public int getPagination() {
		return this.pagination;
	}

	public void setPagination(final int pagination) {
		this.setDoPaging(pagination > 0);
		this.setDoCount(pagination > 0);
		this.pagination = pagination;
	}

	public int getRecordCount() {
		return this.recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public String getReturnType() {
		return returnType;
	}

	public int getRowCount() {
		return this.rowCount;
	}

	public void setRowCount(final int rowCount) {
		this.setDoPaging(rowCount > 0);
		this.setDoCount(rowCount > 0);
		this.rowCount = rowCount;
	}

	public String getSqlString() {
		return this.sqlString;
	}

	public boolean isDoCount() {
		return this.doCount;
	}

	public void setDoCount(boolean doCount) {
		this.doCount = doCount;
	}

	public boolean isDoPaging() {
		return this.doPaging;
	}

	public void setDoPaging(boolean doPaging) {
		this.doPaging = doPaging;
	}

	public boolean isUpdateByWhere() {
		return updateByWhere;
	}

	public void setUpdateByWhere(boolean updateByWhere) {
		this.updateByWhere = updateByWhere;
	}

	@Override
	public void putParameters(Map<String, Object> map) {
		if ($V("oper") != null) this.oper = $V("oper");
		if ($V("id") != null) this.id = $V("id");
		map.put(RsqlConstants.SYS_dataStatus, RsqlConstants.DS_managed);//datastatus列必须处理。

		super.putParameters(map);
	}

	public Container ready() {
		return this.container;
	}

}
