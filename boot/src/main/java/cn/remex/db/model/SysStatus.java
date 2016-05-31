package cn.remex.db.model;

import cn.remex.db.rsql.RsqlConstants.SysStatusEnum;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(/*uniqueConstraints={
		@UniqueConstraint(columnNames = { "key","status"}
		)
}*/)
public class SysStatus extends ModelableImpl{
	private String key;
	private SysStatusEnum status;
	private double progressRate;
	@Column(length = 300)
	private String desc;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getProgressRate() {
		return progressRate;
	}

	public void setProgressRate(double progressRate) {
		this.progressRate = progressRate;
	}

	public SysStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SysStatusEnum status) {
		this.status = status;
	}

	public String getDesc() {

		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
