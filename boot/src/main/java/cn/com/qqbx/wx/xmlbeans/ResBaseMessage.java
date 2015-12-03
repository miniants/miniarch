package cn.com.qqbx.wx.xmlbeans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 消息基类
 * 
 * @author guoshaopeng
 * @since 2014-2-26
 */
public class ResBaseMessage {
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
	@XStreamAlias("ArticleCount")
	private int articleCount; // 
	@XStreamAlias("Articles")
	private List<Article> articles; // 
	
	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
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
