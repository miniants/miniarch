package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

public class MonitorLog extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5520734455586978448L;
	private String username;			//登录用户名
	private String operateDate;		//操作时间
	private String operateObj;		//操作对象——数据库中的表
	private String operateData;		//操作数据——操作数据对象
	private String operator;					//操作符
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getOperateObj() {
		return operateObj;
	}
	public void setOperateObj(String operateObj) {
		this.operateObj = operateObj;
	}
	public String getOperateData() {
		return operateData;
	}
	public void setOperateData(String operateData) {
		this.operateData = operateData;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
