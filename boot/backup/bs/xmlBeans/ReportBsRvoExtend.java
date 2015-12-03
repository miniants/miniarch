package cn.remex.db.bs.xmlBeans;


public class ReportBsRvoExtend extends Extend {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8869559874756121588L;
	public ReportBsRvoExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	public ReportBsRvoExtend(boolean status, String msg) {
		super(status, msg);
	}
	public ReportBsRvoExtend() {
		super(false, "服务未提供具体信息！");
	}
	private int rowCount; //返回行数
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
	
}
