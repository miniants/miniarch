package cn.remex.db.model.cert;

import cn.remex.db.model.SysUri;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.struts2.json.annotations.JSON;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"username","password"}
				)
})
public class AuthUser extends ModelableImpl{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3851128250538182731L;
	/**登陆名*/
	@Column(length = 20)
	private String username;
	/** 登陆密码*/
	@JSONField(serialize=false)
	@Column(length = 30)
	private String password;
	/** 所属角色*/
	@ManyToMany(mappedBy="users",targetEntity=AuthRole.class)
	private List<AuthRole> roles;
	private Map<String, SysUri> uriAuthMap = new HashMap<String, SysUri>();
	private String effectFlag;		//用户是否有效
	private String effectPeriod;			//用户有效期
	public AuthUser() {
		super("undefined");
	}
	public AuthUser(final String name) {
		super(name);
		setName(name);
		setUsername(name);
	}
	@Element(label="密码")
	public String getPassword() {
		return this.password;
	}
	@Element(label="所属角色")
	public List<AuthRole> getRoles() {
		return this.roles;
	}
	@Element(label="用户名")
	public String getUsername() {
		return this.username;
	}
	public void setPassword(final String password) {
		this.password = password;
	}
	public void setRoles(final List<AuthRole> roles) {
		this.roles = roles;
	}
	public void setUsername(final String username) {
		this.username = username;
		super.setName(username);
	}
	public Map<String, SysUri> obtainUriAuthMap() {
		return uriAuthMap;
	}
	public void putUriAuthMap(Map<String, SysUri> uriAuthMap) {
		this.uriAuthMap = uriAuthMap;
	}
	public String getEffectFlag() {
		return effectFlag;
	}
	public void setEffectFlag(String effectFlag) {
		this.effectFlag = effectFlag;
	}
	public String getEffectPeriod() {
		return effectPeriod;
	}
	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
	}
	
}
