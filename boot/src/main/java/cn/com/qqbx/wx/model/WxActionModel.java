package cn.com.qqbx.wx.model;

import java.sql.Types;
import java.util.List;

import javax.persistence.OneToMany;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
import cn.remex.fsm.model.FsmLink;

public class WxActionModel extends ModelableImpl{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6262515722444441392L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 32, sqlType = " ")
	private String name;//名字
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String className;//全类名
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String instroduction;//功能说明
	@OneToMany(mappedBy="wxAction")
	private List<FsmLink> fsmLinks;//有向弧
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getInstroduction() {
		return instroduction;
	}
	public void setInstroduction(String instroduction) {
		this.instroduction = instroduction;
	}
	public List<FsmLink> getWxDirectedArcs() {
		return fsmLinks;
	}
	public void setWxDirectedArcs(List<FsmLink> fsmLinks) {
		this.fsmLinks = fsmLinks;
	}
}
