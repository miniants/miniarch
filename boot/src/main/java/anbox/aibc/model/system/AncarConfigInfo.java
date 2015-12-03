package anbox.aibc.model.system;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class AncarConfigInfo  extends ModelableImpl{
	private static final long serialVersionUID = -6640276922645608102L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String confType;//配置类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String cityCode;//城市
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String key;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
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
