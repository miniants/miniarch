package cn.com.qqbx.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("SendPicsInfo")
public class SendPicsInfo {
	@XStreamAlias("Count")
	private String count; // 
	@XStreamAlias("PicList")
	private PicList picList;
	@XStreamAlias("EventKey")
	private String eventKey;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public PicList getPicList() {
		return picList;
	}
	public void setPicList(PicList picList) {
		this.picList = picList;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
	
}
