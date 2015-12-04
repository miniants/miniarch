package zbh.wx.text;

import zbh.smstp.smsws.xmlbean.ReqShortMsg;
import zbh.smstp.smsws.xmlbean.Request;
import zbh.smstp.smsws.xmlbean.RequestBody;
import zbh.smstp.smsws.xmlbean.RequestHead;

public class SendMsgTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Request request = new Request();
		RequestHead reqHead = new RequestHead();
		RequestBody reqBody = new RequestBody();
		request.setHead(reqHead);
		ReqShortMsg reqShortMsg = new ReqShortMsg();
		reqShortMsg.setPhoneNo("18500234213");
		reqShortMsg.setMsgContent("123443");
		reqBody.setReqShortMsg(reqShortMsg);
		request.setBody(reqBody);
		String reqXml = XmlHelper.objectToXml(request);
		String respXml = HttpHelper.sendXml("http://192.168.80.30:7005/Smsp/remex/RemexHttpXmlAction.action?bs=SendShortMsgBs", reqXml);
	}

}
