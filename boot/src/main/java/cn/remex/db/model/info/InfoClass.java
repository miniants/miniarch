package cn.remex.db.model.info;

import cn.remex.db.rsql.model.ModelableImpl;

public class InfoClass extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InfoClass parent;
	public InfoClass getParent() {
		return this.parent;
	}
	public void setParent(final InfoClass parent) {
		this.parent = parent;
	}

}
