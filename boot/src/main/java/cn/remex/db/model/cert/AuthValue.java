package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

import javax.persistence.Column;
@DataAccessScope(scope=DataAccessScope.everyone)
public class AuthValue extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1461748452524828978L;
	@Column(length = 200)
	private String paramValue;
	@Column(length = 200)
	private String valueDesc;
	@Element(edittype=EditType.select,editoptions="{value:{forbidden:'禁止',permit:'允许'}}")
	@Column(length = 20)
	private String verifyFlag;
	@Element(edittype=EditType.select,editoptions="{value:{f:'禁止访问',r:'只读',rw:'可读可写'}}")
	@Column(length = 10)
	private String dataAuth;
	@Column(length = 20)
	private String dataAuthDesc;
	@Column(length = 20)
	private String verifyOper;
	private AuthParam authParam;
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getDataAuth() {
		return dataAuth;
	}
	public void setDataAuth(String dataAuth) {
		this.dataAuth = dataAuth;
	}
	public String getDataAuthDesc() {
		return dataAuthDesc;
	}
	public void setDataAuthDesc(String dataAuthDesc) {
		this.dataAuthDesc = dataAuthDesc;
	}
	public String getVerifyOper() {
		return verifyOper;
	}
	public void setVerifyOper(String verifyOper) {
		this.verifyOper = verifyOper;
	}
	public AuthParam getAuthParam() {
		return authParam;
	}
	public void setAuthParam(AuthParam authParam) {
		this.authParam = authParam;
	}
	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	public String getValueDesc() {
		return valueDesc;
	}
	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}
	
}
