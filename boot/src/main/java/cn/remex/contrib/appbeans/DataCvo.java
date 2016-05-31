package cn.remex.contrib.appbeans;

import cn.remex.core.util.JsonHelper;
import cn.remex.core.util.Judgment;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Order;
import cn.remex.db.sql.Where;
import cn.remex.web.service.BsCvo;

import java.util.List;

public class DataCvo extends BsCvo {
	public<T extends ModelableImpl> DbCvo<T> obtainDbCvo(Class<T> modelClass){
		DbCvo ret = ContainerFactory.getSession().createDbCvo(modelClass)//.orderBy(t->t.getId(),Sort.ASC)
				.rowCount(this.getRowCount()).page(this.getPagination())
				;
		if(!Judgment.nullOrBlank(extColumn)){
			ret.setDataType("bdodoed");
			ret.putExtColumn(extColumn);
		}

		if(!Judgment.nullOrBlank(filters)){
			ret.getFilter().addGroup(JsonHelper.toJavaObject(filters, Where.class));
//			ret.filter(JsonHelper.toJavaObject(filters, Where.class));
		}

		//用于同时支持对象型Filter和字符串型Filter，这两个之间的关系必须是AND，默认就是，否则可能出现权限漏洞。
		if(this.getFilter()!=null){
			if(ret.getFilter()!=null){
				ret.getFilter().addGroup(this.getFilter());
			}else
				ret.filter(this.getFilter());
		}

		//排序
		if(orders!=null && orders.size()>0){
			for(Order order:orders) {
				ret.orderBy(order.getSidx(), order.getSord());
			}
		}else if(!Judgment.nullOrBlank(this.getSidx()) && !Judgment.nullOrBlank(this.getSord())){
			ret.orderBy(this.getSidx(), this.getSord());
		}

		//检查dbCvo中非sqlColumn来的filter是否添加响应的sqlColumn TODO 这个方式不对，应该用filterBy的机制
//		ret.getFilter().everyRule(c -> ret._getRootColumn().withColumn(c.getField()));

		return  ret;
	}

	private static final long serialVersionUID = -724854943935644108L;
	private int rowCount=10;
	private int pagination=1;
	private int pageCount=0;
	private int recordCount=0;
	private String sidx ="id" ;
	private String sord = "desc";
	private List<Order> orders = null;
	private String extColumn;

	private Where filter;
	private String filters;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getExtColumn() {
		return extColumn;
	}

	public void setExtColumn(String extColumn) {
		this.extColumn = extColumn;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
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

	public Where getFilter() {
		return filter;
	}

	public void setFilter(Where filter) {
		this.filter = filter;
	}
}
