package zbh.aias.model.system;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;

public class AncarConfigInfo  extends ModelableImpl{
	private static final long serialVersionUID = -6640276922645608102L;
	@Column(length = 30)
	private String confType;//配置类型
	@Column(length = 10)
	private String cityCode;//城市
	@Column(length = 20)
	private String key;
	@Column(length = 30)
	private String value;
	
	public String getConfType() {
		return confType;
	}
	public void setConfType(String confType) {
		this.confType = confType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
