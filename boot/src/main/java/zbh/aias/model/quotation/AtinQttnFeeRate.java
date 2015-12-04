package zbh.aias.model.quotation;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;
/**
 * 询价单费率信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public class AtinQttnFeeRate extends ModelableImpl{
	private static final long serialVersionUID = 7911755683505215561L;
	private AtinQuotation atinQuotation;
	@Column(length = 10)
	private String code;           //费率因子编码
	@Column(length = 50)
	private String name;           //费率因子名称
	@Column(length = 10)
	private String option;         //费率因子选择项编码
	@Column(length = 100)
	private String optionDesc;     //费率因子选择项描述
	private double value;          //费率因子值
	private double minValue;       //费率因子下限值
	private double maxValue;       //费率因子上限值

	public AtinQuotation getAtinQuotation() {
		return atinQuotation;
	}
	public void setAtinQuotation(AtinQuotation atinQuotation) {
		this.atinQuotation = atinQuotation;
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
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getOptionDesc() {
		return optionDesc;
	}
	public void setOptionDesc(String optionDesc) {
		this.optionDesc = optionDesc;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
}
