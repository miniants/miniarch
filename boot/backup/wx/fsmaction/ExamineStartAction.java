package zbh.wx.fsmaction;

import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmResult;
import zbh.remex.sam.SamConstant.SamMessageType;

public class ExamineStartAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	@Override
	public FsmResult execute() {
		FsmResult fsmResult;
		fsmResult = new FsmResult(SamMessageType.TEXT,"核审输入手机号",fsmContext);
		return fsmResult;
	}
	

}
