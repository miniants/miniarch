package cn.com.qqbx.smstp.smsws.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class ResponseBody {

	
	@XStreamAlias("ShortMsg")
	private  ResShortMsg resShortMsg;

	public ResShortMsg getResShortMsg() {
		return resShortMsg;
	}

	public void setResShortMsg(ResShortMsg resShortMsg) {
		this.resShortMsg = resShortMsg;
	}
	
	
	

}