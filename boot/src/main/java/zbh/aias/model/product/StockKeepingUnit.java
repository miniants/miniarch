package zbh.aias.model.product;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;

public class StockKeepingUnit  extends ModelableImpl{
	private static final long serialVersionUID = 1171038277070783433L;
	private Product product;//产品品牌
	@Column(length = 10)
	private String  type;   //类型，区分交强险和商业险
	@Column(length = 30)
	private String  code;   //编码
	@Column(length = 50)
	private String  name;   //名称
	@Column(length = 50)
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
