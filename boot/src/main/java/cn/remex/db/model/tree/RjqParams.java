package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;


public class RjqParams  extends ModelableImpl{
		/**
	 * 
	 */
	private static final long serialVersionUID = 6573965427126120747L;
		/*private String dc;*/
		private String addPageLabel;
		private String addPageUri;
		private String editPageLabel;
		private String editPageUri;
		
		private String bs;
		private String oper;
		private String editUrl;



	/*	@Element(label = "dc")
		public String getDc() {
			return this.dc;
		}*/

		@Element(label = "添加窗口名")
		public String getAddPageLabel() {
			return this.addPageLabel;
		}
		@Element(label = "添加目标窗口路径")
		public String getAddPageUri() {
			return this.addPageUri;
		}
		@Element(label = "编辑窗口名")
		public String getEditPageLabel() {
			return this.editPageLabel;
		}
		@Element(label = "编辑目标窗口路径")
		public String getEditPageUri() {
			return this.editPageUri;
		}
		@Element(label = "service")
		public String getBs() {
			return this.bs;
		}
		@Element(label = "oper")
		public String getOper() {
			return this.oper;
		}
		@Element(label = "editUrl")
		public String getEditUrl() {
			return this.editUrl;
		}
		
	/*	public void setDc(String dc) {
			this.dc = dc;
		}*/
		
		public void setAddPageLabel(String addPageLabel) {
			this.addPageLabel = addPageLabel;
		}
		
		public void setAddPageUri(String addPageUri) {
			this.addPageUri = addPageUri;
		}
		
		public void setEditPageLabel(String editPageLabel) {
			this.editPageLabel = editPageLabel;
		}
		
		public void setEditPageUri(String editPageUri) {
			this.editPageUri = editPageUri;
		}
		
		public void setBs(String bs) {
			this.bs = bs;
		}
		
		public void setOper(String oper) {
			this.oper = oper;
		}
		
		public void setEditUrl(String editUrl) {
			this.editUrl = editUrl;
		}
}
