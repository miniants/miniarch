package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

import java.sql.Types;
@DataAccessScope(scope=DataAccessScope.everyone)
public class AuthValue extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1461748452524828978L;
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String paramValue;
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String valueDesc;
	@Element(edittype=EditType.select,editoptions="{value:{forbidden:'禁止',permit:'允许'}}")
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String verifyFlag;
	@Element(edittype=EditType.select,editoptions="{value:{f:'禁止访问',r:'只读',rw:'可读可写'}}")
	@Column(type=Types.CHAR, length = 10, columnDefinition = " ")
	private String dataAuth;
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String dataAuthDesc;
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
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
