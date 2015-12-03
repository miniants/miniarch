package cn.remex.db.bs;

import java.io.Serializable;


/**
 * 此类为数据库操作返回的公共逻辑属性
 */
public class DataBsRvoHead implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6294673740175922013L;
	private String id;
	private String doPaging;
	private String doCount;
	private int pagination;
	private int rowCount;
	private int recordCount;
	private String msg;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDoPaging() {
		return doPaging;
	}
	public void setDoPaging(String doPaging) {
		this.doPaging = doPaging;
	}
	public String getDoCount() {
		return doCount;
	}
	public void setDoCount(String doCount) {
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}