package cn.remex.db.model.info;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

public class Info extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InfoClass infoClass;
	@Column(length = 100, columnDefinition = "CLOB")
	private String infoContent;
	@Column(length = 100, columnDefinition = "CLOB")
	private String infoSummary;
	private boolean indexShow;//主页显示
	@Column(length = 25)
	private String publishDate;
	@Column(length = 200)
	private String showImg;
	private int showOrder;
	@Column(length = 100)
	private String src;
	@Column(length = 80)
	private String title;
	public boolean isIndexShow() {
		return indexShow;
	}
	public void setIndexShow(boolean indexShow) {
		this.indexShow = indexShow;
	}
	public InfoClass getInfoClass() {
		return this.infoClass;
	}
	public String getInfoContent() {
		return this.infoContent;
	}
	public String getPublishDate() {
		return this.publishDate;
	}

	public String getShowImg() {
		return this.showImg;
	}
	public int getShowOrder() {
		return this.showOrder;
	}
	public String getSrc() {
		return this.src;
	}
	public String getTitle() {
		return this.title;
	}
	public void setInfoClass(final InfoClass infoClass) {
		this.infoClass = infoClass;
	}
	public void setInfoContent(final String infoContent) {
		this.infoContent = infoContent;
	}
	public void setPublishDate(final String publishDate) {
		this.publishDate = publishDate;
	}
	public void setShowImg(final String showImg) {
		this.showImg = showImg;
	}
	public void setShowOrder(final int showOrder) {
		this.showOrder = showOrder;
	}
	public void setSrc(final String src) {
		this.src = src;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	public String getInfoSummary() {
		return infoSummary;
	}
	public void setInfoSummary(String infoSummary) {
		this.infoSummary = infoSummary;
	}


}
