package cn.remex.db.model.info;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import java.sql.Types;

public class Info extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InfoClass infoClass;
	@Column(type=Types.CLOB, length = 100, columnDefinition = " ")
	private String infoContent;
	@Column(type=Types.CLOB, length = 100, columnDefinition = " ")
	private String infoSummary;
	private boolean indexShow;//主页显示
	@Column(type=Types.CHAR, length = 25, columnDefinition = " ")
	private String publishDate;
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String showImg;
	private int showOrder;
	@Column(type=Types.CHAR, length = 100, columnDefinition = " ")
	private String src;
	@Column(type=Types.CHAR, length = 80, columnDefinition = " ")
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
