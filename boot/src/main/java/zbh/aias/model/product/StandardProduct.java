package zbh.aias.model.product;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;

public class StandardProduct  extends ModelableImpl{
	private static final long serialVersionUID = -5282401207153874944L;
	@Column(length = 30)
	private String code;//产品编码
	@Column(length = 50)
	private String name;//产品名称
	@Column(length = 50)
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
