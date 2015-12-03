package cn.remex.db.model.view;


import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import java.sql.Types;


public class JqgEditOption extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3079068997285934420L;

	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String title;
	@Column(type=Types.CHAR, length = 100, columnDefinition = " ")
	private String value;
	public JqgEditOption() {
	}
	public JqgEditOption(final String name, final String value, final String title) {
		super(name);
		this.title = title;
		this.value = value;
	}
	public String getTitle() {
		return this.title;
	}
	public String getValue() {
		return this.value;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	public void setValue(final String value) {
		this.value = value;
	}

}
