package cn.remex.db.model.cert;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import java.sql.Types;

public class AuthResource extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2449554491146009510L;










	private String AuthenticateURI;
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String note;
	public AuthResource(final String name) {
		super(name);
	}

	protected AuthResource() {
	}










	public String getAuthenticateURI() {
		return this.AuthenticateURI;
	}
	public String getNote() {
		return this.note;
	}
	public void setAuthenticateURI(final String authenticateURI) {
		this.AuthenticateURI = authenticateURI;
	}
	public void setNote(final String note) {
		this.note = note;
	}

}
