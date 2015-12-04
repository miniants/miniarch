package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DataAccessScope(scope=DataAccessScope.everyone)
public class AuthParam extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1889024500467572760L;
	@Column(length = 50)
	private String paramName;
//	@Element(edittype=EditType.select,editoptions="{value:{forbidden:'禁止',permit:'允许',uncertainty:'待验证'}}")
//	@Column(length = 20)
	private String verifyFlag;
	@OneToMany(mappedBy="authParam")
	private List<AuthValue> authValues;
	private AuthUri authUri;
	private Map<String, AuthValue> authValueMap = new HashMap<String, AuthValue>();
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public List<AuthValue> getAuthValues() {
		return authValues;
	}
	public void setAuthValues(List<AuthValue> authValues) {
		this.authValues = authValues;
	}
	public Map<String, AuthValue> obtainAuthValueMap() {
		return authValueMap;
	}
	public void putAuthValueMap(Map<String, AuthValue> authValueMap) {
		this.authValueMap = authValueMap;
	}
	public AuthUri getAuthUri() {
		return authUri;
	}
	public void setAuthUri(AuthUri authUri) {
		this.authUri = authUri;
	}
	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	
}
