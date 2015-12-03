package anbox.aibc.appBeans.mmbCenter;

import anbox.aibc.AiwbConsts.OrderFilters;

public class MmbCenterCvo {
	private String reqInfoType;//请求的info类型，须跟请求的bs保持一致
	private String id;//请求详情的id
	
	//一以下信息用于订单的查询
	private OrderFilters searchFlag;//筛选条件,未到期、无效、承保等
	private String orderSearchEle;
	public String getReqInfoType() {
		return reqInfoType;
	}
	public void setReqInfoType(String reqInfoType) {
		this.reqInfoType = reqInfoType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OrderFilters getSearchFlag() {
		return searchFlag;
	}
	public void setSearchFlag(OrderFilters searchFlag) {
		this.searchFlag = searchFlag;
	}
	public String getOrderSearchEle() {
		return orderSearchEle;
	}
	public void setOrderSearchEle(String orderSearchEle) {
		this.orderSearchEle = orderSearchEle;
	}
	
}
