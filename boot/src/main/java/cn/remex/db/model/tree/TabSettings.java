package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

public class TabSettings extends ModelableImpl {

		   
		/**
	 * 
	 */
	private static final long serialVersionUID = -6318317190054855527L;
		private String classname;
		private RjqParams rjqParams;
		
		private String editUrl;

		@Element(label = "classname")
		public String getClassname() {
			return this.classname;
		}
		
		public RjqParams getRjqParams() {
			return this.rjqParams;
		}
		
		@Element(label = "editUrl")
		public String getEditUrl() {
			return this.editUrl;
		}
		public void setClassname(String classname) {
			this.classname = classname;
		}
		
		@Element(label = "rjqParams")
		public void setRjqParams(RjqParams rjqParams) {
			this.rjqParams = rjqParams;
		}
		
		public void setEditUrl(String editUrl) {
			this.editUrl = editUrl;
		}

}
