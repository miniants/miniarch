package zbh.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 请求报文头结点
 *@since 20130725
 *@author zhangaiguo
 *
 */
@XStreamAlias("Head")
public class RequestHead {
	private String transChannel;            //交易渠道编码
	private String transType;				//交易类型
	private String transNo;                 //交易流水号
	private String transDate;				//交易日期
	/**
	 *交易类型 
	 *01-车型车辆查询；02-精确报价；03-投保确认；04-核保状态；05-承保确认； 
	 */
	private String channelOper;				//系统当前的操作员
	private String channelUser;				//用户名
	private String channelPwd;				//密码
	private String encryprFlag;				//是否加密
	private String encryptType;				//加密类型
	private String traderMac;			    //商户机器码
	private String traderIP;                //商户IP
	private String customerMac;             //客户机器码
	private String customerIP;              //客户IP
	/**
	 * 请求渠道代码
	 * 01-中海联合;02-网销；03-电销；04-手机移动投保；05-其他；
	 */

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
	public String getChannelOper() {
		return channelOper;
	}
	public void setChannelOper(String channelOper) {
		this.channelOper = channelOper;
	}
	public String getChannelUser() {
		return channelUser;
	}
	public void setChannelUser(String channelUser) {
		this.channelUser = channelUser;
	}
	public String getChannelPwd() {
		return channelPwd;
	}
	public void setChannelPwd(String channelPwd) {
		this.channelPwd = channelPwd;
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
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String requestTypeCode) {
		this.transChannel = requestTypeCode;
	}
	public String getTraderMac() {
		return traderMac;
	}
	public void setTraderMac(String traderMac) {
		this.traderMac = traderMac;
	}
	public String getTraderIP() {
		return traderIP;
	}
	public void setTraderIP(String traderIP) {
		this.traderIP = traderIP;
	}
	public String getCustomerMac() {
		return customerMac;
	}
	public void setCustomerMac(String customerMac) {
		this.customerMac = customerMac;
	}
	public String getCustomerIP() {
		return customerIP;
	}
	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}
}
