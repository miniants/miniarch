package zbh.aias.model.product;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.sql.Types;
import java.util.List;

public class Product extends ModelableImpl{
	private static final long serialVersionUID = -3108751175460560834L;
	@OneToMany(mappedBy="product", cascade={CascadeType.PERSIST})
	private List<StockKeepingUnit> stockKeepingUnits;// 产品库存
	@Column(length = 30)
	private String spCode;//标准产品分类编码
	@Column(length = 50)
	private String spName;//标准产品分类名称
	@Column(length = 40)
	private String brand;//产品所属品牌
	@Column(length = 10)
	private String type;//区分产险、寿险等
	@Column(length = 30)
	private String code;// 产品编码
	@Column(length = 50)
	private String name;//产品名称
	@Column(length = 50)
	private String desc;//描述
	public List<StockKeepingUnit> getStockKeepingUnits() {
		return stockKeepingUnits;
	}
	public void setStockKeepingUnits(List<StockKeepingUnit> stockKeepingUnits) {
		this.stockKeepingUnits = stockKeepingUnits;
	}
	public String getSpCode() {
		return spCode;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
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
