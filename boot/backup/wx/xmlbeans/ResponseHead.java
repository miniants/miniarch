package zbh.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 响应报文头结点
 *@since 20130725
 *@author zhangaiguo
 *
 */
@XStreamAlias("Head")
public class ResponseHead {
	private String transChannel;  //交易渠道
	/**
	 *交易类型 
	 *01-车型车辆查询；02-精确报价；03-投保确认；04-核保状态；05-承保确认； 
	 */
	private String transType;     //交易类型
	private String transNo;       //交易流水号
	private String transDate;     //交易日期
	private String encryptFlag;   //是否加密
	private String encryptType;   //加密类型
	private String errorCode;     //错误代码
	private String errorMessage;  //错误信息
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
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
