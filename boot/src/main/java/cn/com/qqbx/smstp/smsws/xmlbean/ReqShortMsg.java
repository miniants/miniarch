package cn.com.qqbx.smstp.smsws.xmlbean;

/**
 * @since 20130813
 * @author zhangaiguo
 * 
 * 发送短信 
 * */
public class ReqShortMsg {
		private String transChannel;	//交易方式
		private String transType;			//交易类型
		private String transNo;				//交易码
		private String sender;				//发送人姓名
		private String receiver;			//收件人姓名
		private String phoneNo;			//手机号
		private String msgContent;		//短信内容
		private String sendTime;			//发送时间
		private String reserved1;			//预留
		private String reserved2;
		private String reserved3;
		public String getTransChannel() {
			return transChannel;
		}
		public void setTransChannel(String transChannel) {
			this.transChannel = transChannel;
		}
		public String getTransType() {
			return transType;
		}
		public void setTransType(String transType) {
			this.transType = transType;
		}
		public String getTransNo() {
			return transNo;
		}
		public void setTransNo(String transNo) {
			this.transNo = transNo;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		public String getMsgContent() {
			return msgContent;
		}
		public void setMsgContent(String msgContent) {
			this.msgContent = msgContent;
		}
		public String getSendTime() {
			return sendTime;
		}
		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}
		public String getReserved1() {
			return reserved1;
		}
		public void setReserved1(String reserved1) {
			this.reserved1 = reserved1;
		}
		public String getReserved2() {
			return reserved2;
		}
		public void setReserved2(String reserved2) {
			this.reserved2 = reserved2;
		}
		public String getReserved3() {
			return reserved3;
		}
		public void setReserved3(String reserved3) {
			this.reserved3 = reserved3;
		}
}
