package cn.remex.db.bs.xmlBeans;

import java.util.HashMap;
import java.util.Map;

import cn.remex.db.bs.ReportConst.ReportSqlType;

public class ReportBsCvoExtend extends Extend {

	/**
	 * 
	 */
	private static final long serialVersionUID = 613981528147786557L;
	private Map<String,Object> params = new HashMap<String, Object>();
	private String reportSqlName;
	private ReportSqlType sqlType = ReportSqlType.sqlParam;
	public ReportBsCvoExtend() {
		super(false, null);
	}
	
	public Map<String,Object>  getParams() {
		return params;
	}
	public String getReportSqlName() {
		return reportSqlName;
	}
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	public void setReportSqlName(String reportSqlName) {
		this.reportSqlName = reportSqlName;
	}

	public ReportSqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(ReportSqlType sqlType) {
		this.sqlType = sqlType;
	}
	
//	private String beanName;
//	private List<DataColumn> dataColumns;
//	private List<GroupByColumn> groupByColumns;
//	private List<HavingColumn> havingColumns;
//	private List<OrderByColumn> orderByColumns;
//	
//	
//	public String getBeanName() {
//		return beanName;
//	}
//	public void setBeanName(String beanName) {
//		this.beanName = beanName;
//	}
//	public List<DataColumn> getDataColumns() {
//		return dataColumns;
//	}
//	public void setDataColumns(List<DataColumn> dataColumns) {
//		this.dataColumns = dataColumns;
//	}
//	public List<GroupByColumn> getGroupByColumns() {
//		return groupByColumns;
//	}
//	public void setGroupByColumns(List<GroupByColumn> groupByColumns) {
//		this.groupByColumns = groupByColumns;
//	}
//	public List<HavingColumn> getHavingColumns() {
//		return havingColumns;
//	}
//	public void setHavingColumns(List<HavingColumn> havingColumns) {
//		this.havingColumns = havingColumns;
//	}
//	public List<OrderByColumn> getOrderByColumns() {
//		return orderByColumns;
//	}
//	public void setOrderByColumns(List<OrderByColumn> orderByColumns) {
//		this.orderByColumns = orderByColumns;
//	}
//	
	
}
