//package cn.com.qqbx.wx.fsmaction;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import zbh.com.qqbx.wx.model.WxUser;
//import RemexApplication;
//import ContainerFactory;
//import zbh.remex.fsm.FsmAction;
//import zbh.remex.fsm.FsmResult;
//import zbh.remex.fsm.FsmConstant.FsmFlowType;
//import zbh.remex.fsm.model.FsmState;
//import zbh.remex.sam.SamConstant.SamMessageType;
//import zbh.remex.util.JsonHelper;
//
//public class ExamineInfoCheckAction extends FsmAction {
//	
//	private static final long serialVersionUID = -8888030654683132854L;
//	
//	static Map<String,String> WxExamineInfoCheck = RemexApplication.getBean("WxExamineInfoCheck");
//	@Override
//	public FsmResult execute() {
//		FsmResult fsmResult = null;
//		Map<String, Object> flowContextParams = JsonHelper.toJavaObject(fsmContext.getCurFlowContext().getContextParams(), HashMap.class);
//		String valueIsNullKey = null;
//		for(String key:WxExamineInfoCheck.keySet()){
//			if(!flowContextParams.keySet().contains(key)){
//				valueIsNullKey = key;
//				break;
//			}
//		}
//		
//		if(flowContextParams.get("phoneNo")!=null && 
//			flowContextParams.get("identifyNo")!=null){
//			WxUser wxUser = new WxUser();
//			wxUser.setExamine("1");
//			wxUser.setOpenId(fsmContext.getUserId());
//			ContainerFactory.getSession().store(wxUser);
//			fsmResult = new FsmResult(SamMessageType.SemanticIndex,"80001",fsmContext);
//		}else{
//			fsmResult = new FsmResult(SamMessageType.TEXT,"请输入"+WxExamineInfoCheck.get(valueIsNullKey),fsmContext);
//		}
//		return fsmResult;	
//	}
//
//}
