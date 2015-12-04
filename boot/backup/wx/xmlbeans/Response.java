package zbh.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Response")
public class Response {

	@XStreamAlias("Head")
	private ResponseHead head;
	@XStreamAlias("Body")
	private ResponseBody body;
	public ResponseHead getHead() {
		return head;
	}
	public void setHead(ResponseHead head) {
		this.head = head;
	}
	public ResponseBody getBody() {
		return body;
	}
	public void setBody(ResponseBody body) {
		this.body = body;
	}
}
