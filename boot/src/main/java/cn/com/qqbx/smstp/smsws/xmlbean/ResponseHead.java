package cn.com.qqbx.smstp.smsws.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 响应报文头结点
 *@since 20130725
 *@author zhangaiguo
 *
 */
@XStreamAlias("ResponseHead")
public class ResponseHead {
	private String transNo;
	/**
	 *交易类型 
	 *01-车型车辆查询；02-精确报价；03-投保确认；04-核保状态；05-承保确认； 
	 */
	private String transType;
	private String operator;
	private String operatorKey;
	private String encryptFlag;
	private String encryptType;
	private String transDate;
	/**
	 * 请求渠道代码
	 * 01-中海联合;02-网销；03-电销；04-手机移动投保；05-其他；
	 */
	private String responseTypeCode;
	private String errorCode;
	private String errorMessage;
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
	public String getEncryptFlag() {
		return encryptFlag;
	}
	public void setEncryptFlag(String encryptFlag) {
		this.encryptFlag = encryptFlag;
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
	public String getResponseTypeCode() {
		return responseTypeCode;
	}
	public void setResponseTypeCode(String responseTypeCode) {
		this.responseTypeCode = responseTypeCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
