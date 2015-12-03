package anbox.aibc.model.policy;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

/**
 * 车险保单费率信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public class AtinPlcyFeeRate extends ModelableImpl{
	private static final long serialVersionUID = -7743258518443243449L;
	private AtinPolicy atinPolicy;
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String code;              //费率因子编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String name;              //费率因子名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String option;            //费率因子选择项编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String optionDesc;        //费率因子选择项描述
	private double value;             //费率因子值
	private double minValue;          //费率因子下限值
	private double maxValue;          //费率因子上限值

	public AtinPolicy getAtinPolicy() {
		return atinPolicy;
	}
	public void setAtinPolicy(AtinPolicy atinPolicy) {
		this.atinPolicy = atinPolicy;
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
