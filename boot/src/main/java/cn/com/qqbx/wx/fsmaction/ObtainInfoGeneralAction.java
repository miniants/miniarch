package cn.com.qqbx.wx.fsmaction;

import java.util.HashMap;
import java.util.Map;

import cn.com.qqbx.wx.utils.WxUtil;
import cn.remex.fsm.FsmAction;
import cn.remex.fsm.FsmConstant.FsmFlowType;
import cn.remex.fsm.FsmResult;
import cn.remex.fsm.model.FsmState;
import cn.remex.sam.SamConstant.SamMessageType;
import cn.remex.util.JsonHelper;

public class ObtainInfoGeneralAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	static Map<String,String> map = new HashMap<String,String>();
//	static Map<String, String> carInfoMap;
//	static{
//		carInfoMap = new HashMap<String,String>();
//		carInfoMap.put("licenseNo","");
//		carInfoMap.put("certNo","");
//	}
//	Map<String,String> wxMap = RemexApplication.getBean("Wechat");
//	static Map<String,String> wxExamineMap = RemexApplication.getBean("WxExamine");
//	static String nextContent = null;
//	static String jiaoyan=null;
	@Override
	public FsmResult execute() {
		
		Map<String, Object> flowContextParams = JsonHelper.toJavaObject(fsmContext.getCurFlowContext().getContextParams(), HashMap.class);
		flowContextParams.putAll(this.fsmActionParams);
		fsmContext.getCurFlowContext().setContextParams(JsonHelper.toJsonString(flowContextParams));
		
		FsmResult fsmResult = null;
		FsmState a = fsmContext.getCurState();
		String flowName = fsmContext.getCurFlowContext().getName();
		String b = fsmLink.getName();
		final String msgInfo = String.valueOf(samMessage.getMessage());
		FsmFlowType flowType = fsmLink.getFlowType();
		if(WxUtil.validateLicenseNo(msgInfo)){
			
//			DbRvo res = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class){
//				@Override
//				public void initRules(Vehicle v) {
//					setSearch(true);
//					//设置查询条件
//					addRule(v.getLicenseNo(), WhereRuleOper.cn,msgInfo);
//				}
//			});
//			List<Vehicle> vehicles = res.obtainBeans();
//			
//			if(vehicles.size()>0){
//				String frameNo = vehicles.get(0).getFrameNo();
//				String engineNo = vehicles.get(0).getEngineNo();
//				flowContextParams.put("frameNo", frameNo);
//				flowContextParams.put("enginNo", engineNo);
//				fsmContext.getCurFlowContext().setContextParams(JsonHelper.toJsonString(flowContextParams));
//			}
			
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"20005",fsmContext);
//			carInfoMap.put("licenseNo", text);
//			wxMap.put("licenseNo", text);
		}
		
		if(WxUtil.validateCertNo(msgInfo)){
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"30005",fsmContext);
//			carInfoMap.put("certNo", text); 
//			wxMap.put("certNo", text);
		}
		
		if(WxUtil.validateEnginNo(msgInfo)){
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"40003",fsmContext);
		}
		
		if(WxUtil.validateFrameNo(msgInfo)){
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"50006",fsmContext);
		}
//		
		String identifyNo = null;
//		
		if(WxUtil.validatePhoneNo(msgInfo)){
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"60001",fsmContext);
			identifyNo = WxUtil.sendIdentifyNo(msgInfo);
			map.put("identifyNo", identifyNo);
		}
		if(WxUtil.validateIdentifyNo(map.get("identifyNo"),msgInfo)){
			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"70005",fsmContext);		
//			fsmLink.setSemanticIndex(70005L);
		}
//		
//		
//		if(validataMapNull(carInfoMap) && "报价流程".equals(flowName) && (null ==jiaoyan)){
//			System.out.println(flowName);
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"30001",fsmContext);
//		}else if(validataMapNull(wxMap)&& "1".equals(jiaoyan)){
//			System.out.println(flowType);
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"70005",fsmContext);
//			//TODO 存库
//			jiaoyan = "2";
//			//wxMap.clear();
//		}else if("报价流程".equals(flowName) && "2".equals(jiaoyan)){
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"30008",fsmContext);
//		}
//		else{
//			//业务逻辑
//			System.out.println(flowName);
//			fsmResult = new FsmResult(SamMessageType.TEXT,"进入获取信息action,状态："+b+"流程："+flowName+obtainResMsg(wxMap,text),fsmContext);
//		}
		
		
		
		
		
		return fsmResult;
	}
	
//	public static boolean validataMapNull(Map<String,String> map){
//		Boolean mapNull = true;
//		
//		for(String key:map.keySet()){
//			if("".equals(map.get(key))){
//				mapNull = false;
//				break;
//			}
//		}
//		return mapNull;
//	}
//	public static String obtainResMsg(Map<String,String> map,String reqMsg){
//		String resMsg = null;
//		for(String key:map.keySet()){
//			if("".equals(map.get(key))){
//				nextContent = wxExamineMap.get(key);
//				resMsg="核审：您好,您输入的是"+reqMsg+"请输入"+nextContent;
//				break;
//			}
//		}
//		return resMsg;
//	}

}
