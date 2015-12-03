package anbox.aibc.model.product;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class StandardProduct  extends ModelableImpl{
	private static final long serialVersionUID = -5282401207153874944L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String code;//产品编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String name;//产品名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String desc;//描述
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
