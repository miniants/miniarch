package zbh.wx.fsmaction;

import java.util.HashMap;
import java.util.Map;

import zbh.smstp.smsws.xmlbean.ReqShortMsg;
import zbh.smstp.smsws.xmlbean.Request;
import zbh.smstp.smsws.xmlbean.RequestBody;
import zbh.smstp.smsws.xmlbean.RequestHead;
import zbh.wx.text.HttpHelper;
import zbh.wx.text.XmlHelper;
import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmResult;
import zbh.remex.fsm.model.FsmState;
import zbh.remex.sam.SamConstant.SamMessageType;

public class SmsVerifyAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	Map<String,String> carInfoMap = new HashMap<String,String>();
	@Override
	public FsmResult execute() {
		FsmState a = fsmContext.getCurState();
		String b = fsmLink.getName();
		//随机生成六位数字验证码
		String s="";
		while(s.length()<6){
			s+=(int)(Math.random()*10);
		}
		System.out.println(s);
		

		
		Request request = new Request();
		RequestHead reqHead = new RequestHead();
		RequestBody reqBody = new RequestBody();
		request.setHead(reqHead);
		ReqShortMsg reqShortMsg = new ReqShortMsg();
		reqShortMsg.setPhoneNo("18500234213");
		reqShortMsg.setMsgContent(s);
		reqBody.setReqShortMsg(reqShortMsg);
		request.setBody(reqBody);
		String reqXml = XmlHelper.objectToXml(request);
		System.out.println(reqXml);
		HttpHelper.sendXml("http://192.168.80.30:7005/Smsp/remex/RemexHttpXmlAction.action?bs=SendShortMsgBs", reqXml);
		//业务逻辑
		FsmResult fsmResult = new FsmResult(SamMessageType.TEXT,"进入发验证码action,状态："+b,fsmContext);
		return fsmResult;
	}

}
