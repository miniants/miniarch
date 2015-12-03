package cn.com.qqbx.smstp.smsws.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class RequestBody {	
	
	
	@XStreamAlias("ShortMsg")
	private ReqShortMsg reqShortMsg;

	public ReqShortMsg getReqShortMsg() {
		return reqShortMsg;
	}

	public void setReqShortMsg(ReqShortMsg reqShortMsg) {
		this.reqShortMsg = reqShortMsg;
	}

	
	
	
}
