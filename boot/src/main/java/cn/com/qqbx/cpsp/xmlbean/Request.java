package cn.com.qqbx.cpsp.xmlbean;

import cn.remex.bs.Extend;
import cn.remex.bs.Head;

public class Request {
	
	private Head head;
	private Object body;
	private Extend extend;
	public Head getHead() {
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public Extend getExtend() {
		return extend;
	}
	public void setExtend(Extend extend) {
		this.extend = extend;
	}
	
	
}
