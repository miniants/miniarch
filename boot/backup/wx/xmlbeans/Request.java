package zbh.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Request")
public class Request {
	
	@XStreamAlias("Head")
	private RequestHead head;
	@XStreamAlias("Body")
	private RequestBody body;
	
	public RequestHead getHead() {
		return head;
	}
	public void setHead(RequestHead head) {
		this.head = head;
	}
	public RequestBody getBody() {
		return body;
	}
	public void setBody(RequestBody body) {
		this.body = body;
	}
}
