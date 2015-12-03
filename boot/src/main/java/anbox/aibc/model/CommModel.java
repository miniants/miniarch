package anbox.aibc.model;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class CommModel   extends ModelableImpl{
	private static final long serialVersionUID = -6912324784421351545L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String displayStatus;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String openId;
	public String getDisplayStatus() {
		return displayStatus;
	}
	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
