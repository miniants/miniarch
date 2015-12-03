package cn.com.qqbx.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("item")
public class Item {
	@XStreamAlias("PicMd5Sum")
	private String picMd5Sum; // 

	public String getPicMd5Sum() {
		return picMd5Sum;
	}

	public void setPicMd5Sum(String picMd5Sum) {
		this.picMd5Sum = picMd5Sum;
	}
	
}
