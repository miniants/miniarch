//package cn.com.qqbx.wx.fsmaction;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import zbh.com.qqbx.wx.model.WxUser;
//import CoreCvo;
//import CoreSvo;
//import RemexApplication;
//import ContainerFactory;
//import DbCvo;
//import DbRvo;
//import RsqlConstants.WhereRuleOper;
//import zbh.remex.fsm.FsmAction;
//import zbh.remex.fsm.FsmResult;
//import zbh.remex.fsm.FsmConstant.FsmFlowType;
//import zbh.remex.fsm.model.FsmState;
//import zbh.remex.sam.SamConstant.SamMessageType;
//import zbh.remex.util.JsonHelper;
//
//public class CheckInfoAction extends FsmAction {
//	
//	private static final long serialVersionUID = -8888030654683132854L;
//	
//	static Map<String,String> WxPriceInfoCheck = RemexApplication.getBean("WxPriceInfoCheck");
//	@Override
//	public FsmResult execute() {
//		FsmResult fsmResult = null;
//		FsmState a = fsmContext.getCurState();
//		String flowName = fsmContext.getCurFlowContext().getName();
//		String b = fsmLink.getName();
//		String msgInfo = String.valueOf(samMessage.getMessage());
//		FsmFlowType flowType = fsmLink.getFlowType();
//		Map<String, Object> flowContextParams = JsonHelper.toJavaObject(fsmContext.getCurFlowContext().getContextParams(), HashMap.class);
////		Iterator keys = wxExamineMap.keySet().iterator();
////		while(keys.hasNext()){
////			String key = (String) keys.next();
////			for(String flowContextParamsKey:flowContextParams.keySet()){
////				if(key != flowContextParamsKey){
////					break;
////				}
////			}
////		}
//		String valueIsNullKey = null;
//		for(String key:WxPriceInfoCheck.keySet()){
//			if(!flowContextParams.keySet().contains(key)){
//				valueIsNullKey = key;
//				break;
//			}
//		}
//		
////		DbRvo res = ContainerFactory.getSession().query(new DbCvo<WxUser>(WxUser.class){
////			@Override
////			public void initRules(WxUser w) {
////				setSearch(true);
////				//设置查询条件
////				addRule(w.getOpenId(), WhereRuleOper.eq,fsmContext.getUserId());
////			}
////		});
////		List<WxUser> wxUsers = res.obtainBeans();
////		WxUser wxUser = wxUsers.get(0);
//		
//		WxUser wx = new WxUser();
//		wx.setOpenId(fsmContext.getUserId());
//		WxUser wxUser = ContainerFactory.getSession().pickUp(wx);
//		String examine = wxUser.getExamine();
//		
//		if(flowContextParams.get("licenseNo")!=null && 
//				flowContextParams.get("certNo")!=null && 
//				flowContextParams.get("engineNo")!=null && 
//				flowContextParams.get("frameNo")!=null && 
//				null ==examine && ("报价流程".equals(flowName))
//				){
//			
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"80005",fsmContext);
//		}else if("1".equals(examine)&&flowContextParams.get("licenseNo")!=null && 
//				flowContextParams.get("certNo")!=null && 
//				flowContextParams.get("engineNo")!=null && 
//				flowContextParams.get("frameNo")!=null ){
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"80006",fsmContext);
//		}else{
//			fsmResult = new FsmResult(SamMessageType.TEXT,"请输入"+WxPriceInfoCheck.get(valueIsNullKey),fsmContext);
//		}
//		return fsmResult;	
//	}
//
//}