package cn.com.qqbx.wx.fsmaction;

import java.util.HashMap;
import java.util.Map;

import cn.remex.fsm.FsmAction;
import cn.remex.fsm.FsmResult;
import cn.remex.fsm.model.FsmState;
import cn.remex.sam.SamConstant.SamMessageType;

public class QuotePriceInfoAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	Map<String,String> carInfoMap = new HashMap<String,String>();
	@Override
	public FsmResult execute() {
		FsmState a = fsmContext.getCurState();
		String b = fsmLink.getName();
		
		//业务逻辑
		FsmResult fsmResult = new FsmResult(SamMessageType.TEXT,"进入获取报价信息action,状态："+b+"请输入车牌号",fsmContext);
		return fsmResult;
	}

}
