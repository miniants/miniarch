package cn.com.qqbx.smstp.smsws.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 请求报文头结点
 *@since 20130725
 *@author zhangaiguo
 *
 */
@XStreamAlias("RequestHead")
public class RequestHead {
	private String transNo;
	/**
	 *交易类型 
	 *01-车型车辆查询；02-精确报价；03-投保确认；04-核保状态；05-承保确认； 
	 */
	private String transType;
	private String operator;					//用户名
	private String operatorKey;				//密码
	private String encryprFlag;				//是否加密
	private String encryptType;				//加密类型
	private String transDate;
	private String uKey;
	private String macID;						//机器码
	private String compIP;
	private String comCode;
	/**
	 * 请求渠道代码
	 * 01-中海联合;02-网销；03-电销；04-手机移动投保；05-快递；
	 */
	private String transChannel;

	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatorKey() {
		return operatorKey;
	}
	public void setOperatorKey(String operatorKey) {
		this.operatorKey = operatorKey;
	}
	public String getEncryprFlag() {
		return encryprFlag;
	}
	public void setEncryprFlag(String encryprFlag) {
		this.encryprFlag = encryprFlag;
	}
	public String getEncryptType() {
		return encryptType;
	}
	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getuKey() {
		return uKey;
	}
	public void setuKey(String uKey) {
		this.uKey = uKey;
	}
	public String getMacID() {
		return macID;
	}
	public void setMacID(String macID) {
		this.macID = macID;
	}
	public String getCompIP() {
		return compIP;
	}
	public void setCompIP(String compIP) {
		this.compIP = compIP;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String requestTypeCode) {
		this.transChannel = requestTypeCode;
	}
	
}
