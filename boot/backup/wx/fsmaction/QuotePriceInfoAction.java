package zbh.wx.fsmaction;

import java.util.HashMap;
import java.util.Map;

import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmResult;
import zbh.remex.fsm.model.FsmState;
import zbh.remex.sam.SamConstant.SamMessageType;

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
