package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "type","code" }) // 同一个类型的代码不能重复
})
public class SysCode extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5450844183308334612L;
	@Column(length=50)
	private String type;
	@Column(length=100)
	private String desc;
	@Column(length=50)
	private String code;
	@Column(length=50)
	private String title;
	@Column(length=500)
	private String option;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
}
