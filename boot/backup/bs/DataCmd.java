package cn.remex.db.bs;

import java.util.List;

import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.sql.SqlBeanOrder;
import cn.remex.db.sql.SqlBeanWhere;

import cn.remex.web.service.BsCvo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  本类为数据库操作的操作属性，对数据库操作相关的指令集合
 */
@XStreamAlias("DataCmd")
public class DataCmd extends BsCvo{
	private static final long serialVersionUID = -2492551936708750503L;
	private WhereGroupOp groupOper; //不必传
	private String  id; //根据传入的id 删除数据
	private String ids;// id 的复数
	private String sord;//排序-正序asc，倒序desc
	private String sidx;
	private Class<?>  beanClass;
	private String beanName;//缩写bn
	private List<SqlBeanOrder> orders ; // 不必传 返回排序结果默认为（id） 排序
	private String sqlString; // 不必传 根据sql语句进行查询，需要判断sql语句的类型
	private SqlBeanWhere filters = new SqlBeanWhere();
	private boolean search = false;
	private String searchField = null;
	private String searchOper = "eq";
	private String searchString = null;
	
	private int pagination = 1;
	private int rowCount = 15;// 此必须设置默认值 lhy 2014-9-28
	private int pageCount = 1;// 此必须设置默认值 lhy 2014-11-17
	private boolean doPaging = true;// 此必须设置默认值 lhy 2014-9-28
	private boolean doCount = true;// 此必须设置默认值 lhy 2014-9-28
	
	/** 数据列类型控制，指定可操作的数据类型（包括基本型，对象型，集合型）
	 *@rmx.summary bd仅返回基本列数据，od返回外键对象的id及name，oed返回外键对象的其他字段（ec指定） */
	private String dataType;
	
	/** 需要使用的基本的数据列  指出哪些列可以插入数据*/
	private String dataColumns;
	
	/**需要使用的外键对象的数据列  */
	private String exColumn;
	
	/**连接外键表的功能，具体描述待补充，外联的一对一关系表*/
	private String foreignBean;
	
	private String uniqueFields;//复制时的唯一性约束列中，生成随机数的列
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Class<?> getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
//	public List<T> getDatas() {
//		return datas;
//	}
//	public void setDatas(List<T> datas) {
//		this.datas = datas;
//	}
//	public T getData() {
//		return data;
//	}
//	public void setData(T data) {
//		this.data = data;
//	}
	
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public WhereGroupOp getGroupOper() {
		return groupOper;
	}
	public void setGroupOper(WhereGroupOp groupOper) {
		this.groupOper = groupOper;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSqlString() {
		return sqlString;
	}
	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}
	public String getDataColumns() {
		return dataColumns;
	}
	public void setDataColumns(String dataColumns) {
		this.dataColumns = dataColumns;
	}
	public String getExColumn() {
		return exColumn;
	}
	public void setExColumn(String exColumn) {
		this.exColumn = exColumn;
	}
	public String getForeignBean() {
		return foreignBean;
	}
	public void setForeignBean(String foreignBean) {
		this.foreignBean = foreignBean;
	}
	public List<SqlBeanOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<SqlBeanOrder> orders) {
		this.orders = orders;
	}
	public SqlBeanWhere getFilters() {
		return filters;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	public int getPagination() {
		return pagination;
	}
	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
	public boolean isDoPaging() {
		return doPaging;
	}
	public void setDoPaging(boolean doPaging) {
		this.doPaging = doPaging;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public boolean isDoCount() {
		return doCount;
	}
	public void setDoCount(boolean doCount) {
		this.doCount = doCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getUniqueFields() {
		return uniqueFields;
	}
	public void setUniqueFields(String uniqueFields) {
		this.uniqueFields = uniqueFields;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
}
