package zbh.aias.model;

import cn.remex.db.rsql.model.ModelableImpl;
import javax.persistence.Column;

import java.sql.Types;

public class CommModel   extends ModelableImpl{
	private static final long serialVersionUID = -6912324784421351545L;
	@Column(length = 20)
	private String displayStatus;
	@Column(length = 50)
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
