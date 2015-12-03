package cn.remex.admin.appbeans;


import cn.remex.db.model.cert.AuthUser;
import cn.remex.web.service.BsCvo;

public class AdminBsCvo extends BsCvo {
	private String tmpl;//文章模板
	private AuthUser body;

	public AuthUser getBody() {
		return body;
	}

	public void setBody(AuthUser body) {
		this.body = body;
	}

	public String getTmpl() {
		return tmpl;
	}

	public void setTmpl(String tmpl) {
		this.tmpl = tmpl;
	}
	
	
}
