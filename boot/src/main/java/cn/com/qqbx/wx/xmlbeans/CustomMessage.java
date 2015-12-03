package cn.com.qqbx.wx.xmlbeans;


public class CustomMessage {

	private String touser; // 开发者微信号
	private String msgtype; // 消息类型（文本、图片、链接等）
	private News news;
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public News getNews() {
		return news;
	}
	public void setNews(News news) {
		this.news = news;
	}
	


}
