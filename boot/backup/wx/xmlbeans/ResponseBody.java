package zbh.wx.xmlbeans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Body")
public class ResponseBody {
	
	/**
	 * 通用的报文节点。节点名称不能修改。
	 * 
	 * 详见{@link AitpUtils#marshallRespose(Response)}
	 */
	private Object bodyData;
	
	
	/**
	 * 通用的报文节点getter。节点名称不能修改。
	 * 
	 * 详见{@link AitpUtils#marshallRespose(Response)}
	 */
	public Object getBodyData() {
		return bodyData;
	}
	/**
	 * 通用的报文节点setter。节点名称不能修改。
	 * 
	 * 详见{@link AitpUtils#marshallRespose(Response)}
	 */
	public void setBodyData(Object bodyData) {
		this.bodyData = bodyData;
	}
}