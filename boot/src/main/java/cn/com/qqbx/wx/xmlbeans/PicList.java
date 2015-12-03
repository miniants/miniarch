package cn.com.qqbx.wx.xmlbeans;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PicList")
public class PicList {
	@XStreamAlias("item")
	private List<Item> item ; // 

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	
}
