package cn.remex.db.model.cert;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import java.sql.Types;

public class AuthPrivilege extends ModelableImpl {
	public final static String CHR = "CHR";//仅本路径
	public final static String FHD = "FHD";//禁止全目录
	public final static String FTD = "FTD";//禁止本目录
	public final static String NULL = "";//未定义
	public final static String PHD = "PHD";//许可全目录
	public final static String PTD = "PTD";//许可本目录
	public final static String VTP = "VTP";//路径参数验证
	/**
	 * 
	 */
	private static final long serialVersionUID = -3838966727785060351L;

	private AuthResource authResource;
	
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String note;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String privilegeKey;


	public AuthPrivilege(final String name) {
		super(name);
	}
	protected AuthPrivilege() {
	}

	public AuthResource getAuthResource() {
		return this.authResource;
	}

	public String getNote() {
		return this.note;
	}

	public String getPrivilegeKey() {
		return this.privilegeKey;
	}

	public void setAuthResource(final AuthResource authResource) {
		this.authResource = authResource;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public void setPrivilegeKey(final String privilegeKey) {
		this.privilegeKey = privilegeKey;
	}

	public String validate() {
		if(VTP != this.privilegeKey){
			return this.privilegeKey;
		}
		return NULL;
	}

}
