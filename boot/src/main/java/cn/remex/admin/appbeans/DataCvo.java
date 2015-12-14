package cn.remex.admin.appbeans;

import cn.remex.web.service.BsCvo;

public class DataCvo extends BsCvo {
	private int rowCount=10;
	private int pagination=1;
	private int pageCount=0;
	private int recordCount=0;
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
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
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
