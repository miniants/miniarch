package cn.com.qqbx.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 消息基类
 * 
 */
@XStreamAlias("xml")
public class ReqBaseMessage {
	@XStreamAlias("ToUserName")
	private String toUserName; // 开发者微信号
	@XStreamAlias("FromUserName")
	private String fromUserName; // 发送方账号(一个OpenID)
	@XStreamAlias("CreateTime")
	private long createTime; // 消息创建时间（整型）
	@XStreamAlias("MsgType")
	private String msgType; // 消息类型（文本、图片、链接等）
	@XStreamAlias("MsgId")
	private long msgId; // 消息id，64位整型
	@XStreamAlias("Content")
	private String content;	//回复的消息内容
	@XStreamAlias("Event")
	private String event; // 事件类型
	@XStreamAlias("EventKey")
	private String eventKey; // 事件key值
	@XStreamAlias("Ticket")
	private String ticket; // 事件key值
	@XStreamAlias("MediaId")
	private String mediaId;
	@XStreamAlias("Format")
	private String format;
	@XStreamAlias("Recognition")
	private String recognition;
	@XStreamAlias("PicUrl")
	private String picUrl;
	@XStreamAlias("SendPicsInfo")
	private SendPicsInfo sendPicsInfo;
	@XStreamAlias("Encrypt")
	private String encrypt;
	
		

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public SendPicsInfo getSendPicsInfo() {
		return sendPicsInfo;
	}

	public void setSendPicsInfo(SendPicsInfo sendPicsInfo) {
		this.sendPicsInfo = sendPicsInfo;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

}
