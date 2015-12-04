package cn.remex.db.model.cert;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

public class AuthResource extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2449554491146009510L;










	private String AuthenticateURI;
	@Column(length = 200)
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
