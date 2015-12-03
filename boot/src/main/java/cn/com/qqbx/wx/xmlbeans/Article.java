package cn.com.qqbx.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("item")
public class Article {
	@XStreamAlias("Title")
	private String title; // 
	@XStreamAlias("Description")
	private String description; // 

	@XStreamAlias("Url")
	private String url; // 
	@XStreamAlias("PicUrl")
	private String picurl; // 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
