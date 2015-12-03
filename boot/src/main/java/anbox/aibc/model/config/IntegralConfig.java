package anbox.aibc.model.config;

import java.sql.Types;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"skuCode","integralWay","integralType"}
				)
})
public class IntegralConfig  extends ModelableImpl{
	private static final long serialVersionUID = -5808399687999988416L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String transChannel;//渠道
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String cityCode;//城市
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String skuCode;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String skuName;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private IntegralWay integralWay;//积分途径，登录、注册、报价、出单、推荐
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private IntegralType integralType;//积分类型，卡币/卡豆
	private double rate;//比例
	private int amount;//数量
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public IntegralWay getIntegralWay() {
		return integralWay;
	}
	public void setIntegralWay(IntegralWay integralWay) {
		this.integralWay = integralWay;
	}
	public IntegralType getIntegralType() {
		return integralType;
	}
	public void setIntegralType(IntegralType integralType) {
		this.integralType = integralType;
	}
	
}
