package anbox.aibc.model.product;

import java.sql.Types;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class Product extends ModelableImpl{
	private static final long serialVersionUID = -3108751175460560834L;
	@OneToMany(mappedBy="product", cascade={CascadeType.PERSIST})
	private List<StockKeepingUnit> stockKeepingUnits;// 产品库存
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String spCode;//标准产品分类编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String spName;//标准产品分类名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String brand;//产品所属品牌
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String type;//区分产险、寿险等
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String code;// 产品编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String name;//产品名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
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
