package anbox.aibc.model.product;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class StockKeepingUnit  extends ModelableImpl{
	private static final long serialVersionUID = 1171038277070783433L;
	private Product product;//产品品牌
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String  type;   //类型，区分交强险和商业险
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String  code;   //编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String  name;   //名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String  desc;   //描述
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
