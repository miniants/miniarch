package cn.remex.web.service;

import java.io.Serializable;

import javax.validation.Valid;



/**
 * 业务服务结果装配返回
 */
public class BsRvo implements Serializable{
	private static final long serialVersionUID = 4613569092879127908L;
	private boolean status = false;
	private String msg;
	private String code;
	private Object body;

	private String rt;
	private String rv;

    /**
     */
	public BsRvo() {
		this.status = true;
	}

    /**
     * @param status
     */
	public BsRvo(boolean status) {
		this.status = status;
	}

    /**
     * @param status
     * @param body
     */
    public BsRvo(boolean status, Object body) {
        this.status = status;
		this.body = body;
    }

    /**
     * @param status
     * @param msg
     * @param code
     */
	public BsRvo(boolean status, String msg, String code) {
		this.status = status;
		this.msg = msg;
		this.code = code;
	}

    /**
     * @param status
     * @param msg
     * @param code
     * @param body
     */
	public BsRvo(boolean status, String msg, String code, Object body) {
		this.status = status;
		this.msg = msg;
		this.code = code;
		this.body = body;
	}
	public BsRvo(boolean status, String msg, String code, Object body,String rv, String rt) {
		this.status = status;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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
}
