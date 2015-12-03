package cn.remex.db.bs;


public class DataResult extends Extend {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4478786596811183160L;
	public DataResult(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	public DataResult(boolean status, String msg) {
		super(status, msg);
	}
	public DataResult() {
		super(false, "服务未提供具体信息！");
	}
	private int rowCount; //返回行数
	private int pageCount = 1;// 此必须设置默认值 lhy 2014-11-17
	private int effectRowCounts; //影响行数
	private int pagination = 1;
	private int recordCount;
	private boolean doPaging = false;
	private boolean doCount = false;
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void setErrorCode(String errorCode) {
		if(DataBsConst.BsStatus.Success.toString().equals(errorCode))
			setStatus(true);
		super.setErrorCode(errorCode);
	}
	public int getEffectRowCounts() {
		return effectRowCounts;
	}
	public void setEffectRowCounts(int effectRowCounts) {
		this.effectRowCounts = effectRowCounts;
	}
	public int getPagination() {
		return pagination;
	}
	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
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
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
