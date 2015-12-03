package cn.com.qqbx.wx.bs;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import cn.com.qqbx.wx.appbean.WechatPara;
import cn.com.qqbx.wx.appbean.WxCheck;
import cn.com.qqbx.wx.utils.WxCheckUtil;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.com.qqbx.wx.utils.WxXmlHelper;
import cn.com.qqbx.wx.xmlbeans.CustomMessage;
import cn.com.qqbx.wx.xmlbeans.ReqBaseMessage;
import cn.com.qqbx.wx.xmlbeans.ResBaseMessage;
import cn.remex.bs.Bs;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.Head;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.fsm.FsmConfiguration;
import cn.remex.fsm.FsmController;
import cn.remex.fsm.FsmResult;
import cn.remex.fsm.model.FsmContext;
import cn.remex.reflect.ReflectUtil;
import cn.remex.sam.SamConstant.SamMessageType;
import cn.remex.sam.model.SamMessage;

public class WxServiceBs implements Bs {
	//
	// @Override
	// public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
	// // TODO Auto-generated method stub
	// System.out.println("aaaa");
	//
	// // WSVCLi.do()
	// // WechatPara wechatPara = bsCvo.getBody();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
	// ReqBaseMessage reqBaseMessage = bsCvo.getBody(ReqBaseMessage.class);
	// // 微信加密签名
	// WxCheck wxCheck = bsCvo.getBody(WxCheck.class);
	// if(null == wxCheck){
	// //认证逻辑
	// // BsRvo bsRvo1 = executeCheck(bsCvo.toFormCvo(), bsRvo);
	// BsRvo bsRvo1 = executeCheck(bsCvo, bsRvo);
	//
	// ReflectUtil.copyProperties(bsRvo, bsRvo1);
	// return bsRvo;
	// }else{
	// //主业务逻辑;
	// }
	//
	//
	//
	// bsRvo.getHead().setRt("xmlBody");
	// bsRvo.getHead().setRv("xml");
	// return bsRvo;
	// }
	public BsRvo executeCheck(BsCvo bsCvo, BsRvo bsRvo) {
		// TODO Auto-generated method stub
		System.out.println("");
		System.setProperty("javax.net.debug", "ssl,handshake");
		// 微信加密签名
		WxCheck wxCheck = bsCvo.getBody(WxCheck.class);

		String signature = wxCheck.getSignature();
		// 时间戳
		String timestamp = wxCheck.getTimestamp();
		// 随机数
		String nonce = wxCheck.getNonce();
		// 随机字符串
		String echostr = wxCheck.getEchostr();

		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (WxCheckUtil.checkSignature(signature, timestamp, nonce)) {
			bsRvo.setBody(echostr);
			// bsRvo.setResponseBody(echostr);
		}
		Head head = new Head();
		head.setRt("textStream");
		return bsRvo;
	}

	Container session = ContainerFactory.getSession();
	static final Map<String, String> operMap = new HashMap<String, String>();

	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		ReqBaseMessage reqBaseMessage = bsCvo.getBody(ReqBaseMessage.class);
		final WechatPara wechatPara = new WechatPara();
		final String msgType = reqBaseMessage.getMsgType();
		String value = null;
//		String content = reqBaseMessage.getContent();
		// String content="31";
		final String openId = reqBaseMessage.getFromUserName();
		String event = reqBaseMessage.getEvent();
		final String eventKey = reqBaseMessage.getEventKey();
		ReflectUtil.copyProperties(wechatPara, reqBaseMessage);
		bsCvo.setBody(wechatPara);
		final String userId = openId;
		
		if(("event".equals(msgType) && "subscribe".equals(event))||"text".equalsIgnoreCase(msgType)){
			 long createTime = new Date().getTime() / 1000;
			 String toUserName = wechatPara.getToUserName();
			 String fromUserName = wechatPara.getFromUserName();
			 ResBaseMessage rsbm = new ResBaseMessage();
			 rsbm.setToUserName(fromUserName);
			 rsbm.setFromUserName(toUserName);
			 rsbm.setMsgType("text");
			 rsbm.setCreateTime(createTime);
			 String content = "亲，\n我姗姗来迟，但是绝对新鲜。盟友众多、实时比价、在线出单，为广大车主及代理人服务。这就是我，安卡车险！" +
			 		"<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.cn/WxSys/wx/page/cf_atin.html&response_type=code&scope=snsapi_base&state=2#wechat_redirect'>点击进入安卡比价</a>";
					 
			 rsbm.setContent(content);
			 String responseBody = WxXmlHelper.marshall(rsbm,true);
			 System.out.println(responseBody);
			 bsRvo.getHead().setRt("textStream");
			 bsRvo.setBody(responseBody);
			 return bsRvo;
		}else if(true){
			 long createTime = new Date().getTime() / 1000;
			 String toUserName = wechatPara.getToUserName();
			 String fromUserName = wechatPara.getFromUserName();
			 ResBaseMessage rsbm = new ResBaseMessage();
			 rsbm.setToUserName(fromUserName);
			 rsbm.setFromUserName(toUserName);
			 rsbm.setMsgType("text");
			 rsbm.setCreateTime(createTime);
			 String content = ""; 
			 rsbm.setContent(content);
			 String responseBody = WxXmlHelper.marshall(rsbm,true);
			 System.out.println(responseBody);
			 bsRvo.getHead().setRt("textStream");
			 bsRvo.setBody(responseBody);
			 return bsRvo;
		}
		
		
		
		
		/*if("event".equals(msgType) && "CLICK".equals(event)){
			value = eventKey;
		}else if("text".equals(msgType)){
			value = reqBaseMessage.getContent();
		}else if("event".equals(msgType) && "subscribe".equals(event)){
			value = "你好";
		}*/
		

		String msgKey = obtainMsgKey(value);
		boolean isUserActive;
		synchronized (this) {
			isUserActive = isUserActive(userId);
			pushMsg(userId, msgKey, value);
		}

		Thread t;
		if (!isUserActive) {
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					TreeMap<String, Object> userMsgTree = msgMap.get(userId);
					int i = userMsgTree.size();
					while (i > 0) {
						String curMsgKey = userMsgTree.firstKey();
						try {
							doTransit(userId, curMsgKey,msgType,eventKey);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							userMsgTree.remove(curMsgKey);
							i = userMsgTree.size();
						}
					}
				}
			});
			t.start();

		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FsmResult resultMessage = popMsgResult(userId, msgKey);
		boolean flag = null == resultMessage;

		// TODO 如果resultMessage为空，新开一个线程调用服务接口推送消息
		final String context = value;
		if (flag) {
			resultMessage = new FsmResult(SamMessageType.TEXT, "系统处理中请稍后", null);
			
			
			Thread thread;
			if (!isUserActive) {
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						SamMessage samMessage = SamMessage.getInstance(SamMessageType.TEXT, context);
						FsmContext fsmContext = FsmController.obtainFsmContext(userId);
						FsmResult result = FsmController.transit(samMessage, fsmContext);
						SamMessage a = result.getMessage();
						CustomMessage customMessage = (CustomMessage) a.getMessage();
						String fromUserName = wechatPara.getFromUserName();
						customMessage.setTouser(fromUserName);
						WxUtil.customMessage(customMessage);
					}
				});
				thread.start();

			}
		}
		 if(resultMessage.getMessage().getType()==SamMessageType.TEXT){
			 long createTime = new Date().getTime() / 1000;
			 String toUserName = wechatPara.getToUserName();
			 String fromUserName = wechatPara.getFromUserName();
			 ResBaseMessage rsbm = new ResBaseMessage();
			 rsbm.setToUserName(fromUserName);
			 rsbm.setFromUserName(toUserName);
			 rsbm.setMsgType("text");
			 rsbm.setCreateTime(createTime);
			 rsbm.setContent(String.valueOf((resultMessage.getMessage().getMessage())));
			 String responseBody = WxXmlHelper.marshall(rsbm,true);
			 System.out.println(responseBody);
			 bsRvo.getHead().setRt("textStream");
			 bsRvo.setBody(responseBody);
		 }else if(resultMessage.getMessage().getType() == SamMessageType.JsonObj){
			 	CustomMessage customMessage = (CustomMessage) resultMessage.getMessage().getMessage();
				String fromUserName = wechatPara.getFromUserName();
				customMessage.setTouser(fromUserName);
				WxUtil.customMessage(customMessage);
		 }else if(resultMessage.getMessage().getType() == SamMessageType.XmlObj){
			 String toUserName = wechatPara.getToUserName();
			 String fromUserName = wechatPara.getFromUserName();
			 ResBaseMessage rsbm = (ResBaseMessage) resultMessage.getMessage().getMessage();
			 rsbm.setToUserName(fromUserName);
			 rsbm.setFromUserName(toUserName);
			 String responseBody = WxXmlHelper.marshall(rsbm,true);
			 System.out.println(responseBody);
			 bsRvo.getHead().setRt("textStream");
			 bsRvo.setBody(responseBody);
			 System.out.println("暂时没有地方用到");
			 
		 }
		
		
		return bsRvo;
	}

	private static HashMap<String, TreeMap<String, Object>> msgMap = new HashMap<String, TreeMap<String, Object>>();
	private static HashMap<String, TreeMap<String, FsmResult>> msgResultMap = new HashMap<String, TreeMap<String, FsmResult>>();

	private String obtainMsgKey(String context) {
		return "" + System.currentTimeMillis() + context.hashCode();
	}

	private boolean isUserActive(String userId) {
		return msgMap.get(userId) != null && msgMap.get(userId).size() > 0;
	}

	private void pushMsg(String userId, String msgKey, String content) {
		TreeMap<String, Object> userMsgMap = msgMap.get(userId);
		if (null == userMsgMap) {
			userMsgMap = new TreeMap<String, Object>();
			msgMap.put(userId, userMsgMap);
		}
		userMsgMap.put(msgKey, content);
	}

	private TreeMap<String, Object> getUserMsg(String userId) {
		TreeMap<String, Object> userMsgMap = msgMap.get(userId);
		if (null == userMsgMap) {
			userMsgMap = new TreeMap<String, Object>();
			msgMap.put(userId, userMsgMap);
		}
		return userMsgMap;
	}

	private TreeMap<String, FsmResult> getUserMsgResult(String userId) {
		TreeMap<String, FsmResult> userMsgMap = msgResultMap.get(userId);
		if (null == userMsgMap) {
			userMsgMap = new TreeMap<String, FsmResult>();
			msgResultMap.put(userId, userMsgMap);
		}
		return userMsgMap;
	}

	private FsmResult popMsgResult(String userId, String msgKey) {
		TreeMap<String, FsmResult> userMsgMap = msgResultMap.get(userId);
		if (null == userMsgMap) {
			userMsgMap = new TreeMap<String, FsmResult>();
			msgResultMap.put(userId, userMsgMap);
		}
		FsmResult ret = userMsgMap.get(msgKey);

		msgResultMap.get(userId).remove(msgKey);

		return ret;
	}

	private void doTransit(String userId, String msgKey, String msgType, String eventKey) {

		TreeMap<String, Object> userMsgMap = getUserMsg(userId);
		Object content = userMsgMap.get(msgKey);
		SamMessage samMessage = SamMessage.getInstance(SamMessageType.TEXT, content);
//		if("text".equals(msgType)){
//			samMessage = SamMessage.getInstance(SamMessageType.TEXT, content);
//		}else if("event".equals(msgType)){
//			samMessage = SamMessage.getInstance(SamMessageType.CLICK, "11");
//		}
		// SamMessage samMessage = SamMessage.getInstance(msgType, content);
		FsmContext fsmContext = FsmController.obtainFsmContext(userId);
		FsmResult result = FsmController.transit(samMessage, fsmContext);

		SamMessage resultMessage = result.getMessage();
		switch (resultMessage.getType()) {
		case TEXT:
			System.out.println(resultMessage.getMessage());
			break;
		// case IMAGE:
		// System.out.println(resultMessage.getMessage());
			
		default:
			break;
		}

		getUserMsgResult(userId).put(msgKey, result);
	}

	

	public static void main(String... args) throws UnsupportedEncodingException {
		List<String> fsmActionPackages = new ArrayList<String>();
		fsmActionPackages.add("cn.com.qqbx.wx.fsmaction");
		new FsmConfiguration().setFsmActionPackages(fsmActionPackages);
		String message = "我的车牌号是京A12345";
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("请输入:");
			message = input.next();
			try {
				test(new String(message.getBytes("GBK"), "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!message.equals("exit"));
		System.out.println("程序结束!");
	}

	public static void test(String message) {
		String userId = "userID11111";

		SamMessage samMessage = SamMessage.getInstance(SamMessageType.TEXT, message);
		FsmContext fsmContext = FsmController.obtainFsmContext(userId);
		FsmResult result = FsmController.transit(samMessage, fsmContext);

		SamMessage resultMessage = result.getMessage();
		switch (resultMessage.getType()) {
		case TEXT:
			System.out.println(resultMessage.getMessage());
			break;

		default:
			break;
		}

	}
}
