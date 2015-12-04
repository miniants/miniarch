package zbh.wx.bs.old;
//package zbh.com.qqbx.wx.bs;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import ReqShortMsg;
//import Request;
//import RequestBody;
//import RequestHead;
//import WechatPara;
//import zbh.com.qqbx.wx.model.WxUser;
//import WxUtil;
//import WxXmlHelper;
//import ResBaseMessage;
//import zbh.remex.bs.BsCvo;
//import RemexApplication;
//import Container;
//import ContainerFactory;
//import DbCvo;
//import DbRvo;
//import RsqlConstants.WhereRuleOper;
//
//public class WxExamineBs {
//	private BsCvo bsCvo;
//	public WxExamineBs(final BsCvo bsCvo) {
//		this.bsCvo = bsCvo;
//	}
//	Container session = ContainerFactory.getSession();
//public String controller(){
//	Map<String,String> wxMap = RemexApplication.getBean("Wechat");
//	Map<String,String> wxExamineMap = RemexApplication.getBean("WxExamine");
//	long createTime = new Date().getTime()/1000;
////	WechatPara wechatPara = (WechatPara)cvo.getBean();
//	WechatPara wechatPara = bsCvo.getBody(WechatPara.class);
//	String toUserName = wechatPara.getToUserName();
//	String fromUserName = wechatPara.getFromUserName();
//	ResBaseMessage rsbm = new ResBaseMessage();
//	rsbm.setToUserName(fromUserName);
//	rsbm.setFromUserName(toUserName);
//	rsbm.setCreateTime(createTime);
//	String responseBody = null;
//	final String reqText = wechatPara.getContent();
//	if(WxUtil.validateLicenseNo(reqText)){
//		wxMap.put("licenseNo", reqText);
//		rsbm.setMsgType("text");
//		
////		DbRvo res = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class){
////			@Override
////			public void initRules(Vehicle v) {
////				setSearch(true);
////				//设置查询条件
////				addRule(v.getLicenseNo(), WhereRuleOper.zbh,reqText);
////			}
////		});
////		List<Vehicle> vehicles = res.obtainBeans();
//		
////		if(vehicles.size()>0){
////			String frameNo = vehicles.get(0).getFrameNo();
////			String engineNo = vehicles.get(0).getEngineNo();
////			wxMap.put("frameEngineNo",frameNo+","+engineNo);
////		}
////		
//		String nextContent = null;
//		for(String key:wxMap.keySet()){
//			if("".equals(wxMap.get(key))){
//				nextContent = wxExamineMap.get(key);
//				rsbm.setContent("核审：您好,您输入的车牌号是"+reqText+"请输入"+nextContent);
//				break;
//			}
//		}
//		
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}else if(WxUtil.validateCertNo(reqText)){
//		wxMap.put("certNo", reqText);
//		rsbm.setMsgType("text");
//		String nextContent = null;
//		for(String key:wxMap.keySet()){
//			if("".equals(wxMap.get(key))){
//				nextContent = wxExamineMap.get(key);
//				rsbm.setContent("核审：您好,您输入的身份证号是"+reqText+"请输入"+nextContent);
//				break;
//			}
//		}
//		
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}else if(WxUtil.validateFrameEngineNo(reqText)){
//		wxMap.put("frameEngineNo", reqText);
//		rsbm.setMsgType("text");
//		String nextContent = null;
//		for(String key:wxMap.keySet()){
//			if("".equals(wxMap.get(key))){
//				nextContent = wxExamineMap.get(key);
//				rsbm.setContent("核审：您好,您输入的是"+reqText+"请输入"+nextContent);
//				break;
//			}
//		}
//		
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}else if("CLICK".equals(wechatPara.getEvent())){
//		rsbm.setMsgType("text");
//		rsbm.setContent("核审：您好,由于您是未注册用户，请输入车牌号");
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}else if(WxUtil.validatePhoneNo(reqText)){
//		
//		Request request = new Request();
//		RequestHead reqHead = new RequestHead();
//		RequestBody reqBody = new RequestBody();
//		request.setHead(reqHead);
//		request.setBody(reqBody);
//		ReqShortMsg reqShortMsg = new ReqShortMsg();
//		reqShortMsg.setPhoneNo(reqText);
//		reqShortMsg.setMsgContent("1234");
////		String reqXml = XmlHelper.objectToXml(request);
//		
//		wxMap.put("phoneNo", reqText);
//		rsbm.setMsgType("text");
//		String nextContent = null;
//		for(String key:wxMap.keySet()){
//			if("".equals(wxMap.get(key))){
//				nextContent = wxExamineMap.get(key);
//				rsbm.setContent("核审：您好,您输入的手机号是"+reqText+"请输入"+nextContent);
//				break;
//			}
//		}
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}
//	else{
//		rsbm.setMsgType("text");
//		rsbm.setContent("核审：输入信息有误，请重新输入");
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}
//	
//	if(validataMapNull(wxMap)){
//		WxUser wxUser = new WxUser();
//		wxUser.setCertNo(wxMap.get("certNo"));
//		wxUser.setOpenId(fromUserName);
//		String[] ps = wxMap.get("frameEngineNo").split(",");
//		String frameNo = ps[0];
//		String engineNo = ps[1];
//		session.store(wxUser);
//		rsbm.setMsgType("text");
//		rsbm.setContent("核审：核审通过");
//		responseBody = WxXmlHelper.marshall(rsbm, true);
//	}
//	return responseBody;
//	}
//
//public static boolean validataMapNull(Map<String,String> map){
//	Boolean mapNull = true;
//	for(String key:map.keySet()){
//		if("".equals(map.get(key))){
//			mapNull = false;
//			break;
//		}
//	}
//	return mapNull;
//}
//
//}
