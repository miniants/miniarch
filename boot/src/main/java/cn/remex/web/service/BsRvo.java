package cn.remex.web.service;

import cn.remex.core.exception.ServiceCode;

import java.io.Serializable;

/**
 * 业务服务结果装配返回
 */
public class BsRvo implements Serializable{
	private static final long serialVersionUID = 4613569092879127908L;
	private String msg;
	private String code = ServiceCode.FAIL;
	private Object body;

	private String rt; //jsp json redirect
	private String rv;
	private String rp;

	public BsRvo(){}

	public BsRvo(String code, String msg) {
		this.msg = msg;
		this.code = code;
	}
	public BsRvo(String code, String msg, Object body) {
		this.msg = msg;
		this.code = code;
		this.body = body;
	}

	public BsRvo(String code, String msg, Object body, String rv, String rt) {
		this.msg = msg;
		this.code = code;
		this.body = body;
		this.rv = rv;
		this.rt = rt;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRv() {
		return rv;
	}

	public void setRv(String rv) {
		this.rv = rv;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getRp() {
		return rp;
	}

	public void setRp(String rp) {
		this.rp = rp;
	}
}
