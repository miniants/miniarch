package cn.remex.db.sql;

import java.io.Serializable;

public class SqlBeanOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7696684589175710168L;
	private String sidx; 	//排序字段
	private String sord;	//排序类型 ASC或者DESC
	private boolean sortable = false;

	public SqlBeanOrder() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param sortable 是否排序，默认为false
	 * @param sidx 排序字段
	 * @param sord 排序类型 ASC或者DESC
	 */
	public SqlBeanOrder(final boolean sortable, final String sidx, final String sord) {
		super();
		this.sortable = sortable;
		this.sidx = sidx;
		this.sord = sord;
	}
	public String getSidx() {
		return this.sidx;
	}

	public String getSord() {
		return this.sord;
	}
	public boolean isSortable() {
		return this.sortable;
	}
	public String obtainKey() {
		StringBuilder builder = new StringBuilder();
		builder.append("sidx-");
		builder.append(this.sidx);
		builder.append("-PN_sord-");
		builder.append(this.sord);
		return builder.toString();
	}
	public void setSidx(final String sidx) {
		this.sidx = sidx;
	}
	public void setSord(final String sord) {
		this.sord = sord;
	}
	public void setSortable(final boolean sortable) {
		this.sortable = sortable;
	}

}

