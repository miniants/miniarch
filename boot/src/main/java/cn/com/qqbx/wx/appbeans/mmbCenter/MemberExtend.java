package cn.com.qqbx.wx.appbeans.mmbCenter;

import java.util.ArrayList;
import java.util.List;

import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.sql.SqlBeanWhere;
import cn.com.qqbx.wx.appbeans.AncarExtend;

public class MemberExtend extends AncarExtend{
	private static final long serialVersionUID = -7442591488714533281L;
	
	public MemberExtend() {
		super(false, "");
	}
	public MemberExtend(boolean status, String msg) {
		super(status, msg);
	}
	public MemberExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	private String id;
	private boolean doPaging;//是否分页
	private boolean doCount;
	private boolean search;//搜索标志
	private int pagination;//页码
	private int rowCount;//每页行数
	private int recordCount;//总记录数
	private int pageCount;//总记录数
	private SqlBeanWhere filters;//查询条件
	private WhereGroupOp groupOp = WhereGroupOp.AND; // 多字段查询时分组类型，主要是AND或者OR
	private List<SqlBeanWhere> groups = new ArrayList<SqlBeanWhere>();// 多组组合
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDoPaging() {
		return doPaging;
	}
	public void setDoPaging(boolean doPaging) {
		this.doPaging = doPaging;
	}
	public boolean isDoCount() {
		return doCount;
	}
	public void setDoCount(boolean doCount) {
		this.doCount = doCount;
	}
	public int getPagination() {
		return pagination;
	}
	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	public SqlBeanWhere getFilters() {
		return filters;
	}
	public void setFilters(SqlBeanWhere filters) {
		this.filters = filters;
	}
	public WhereGroupOp getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(WhereGroupOp groupOp) {
		this.groupOp = groupOp;
	}
	public List<SqlBeanWhere> getGroups() {
		return groups;
	}
	public void setGroups(List<SqlBeanWhere> groups) {
		this.groups = groups;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
