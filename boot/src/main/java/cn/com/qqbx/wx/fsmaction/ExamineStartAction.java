package cn.com.qqbx.wx.fsmaction;

import cn.remex.fsm.FsmAction;
import cn.remex.fsm.FsmResult;
import cn.remex.sam.SamConstant.SamMessageType;

public class ExamineStartAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	@Override
	public FsmResult execute() {
		FsmResult fsmResult;
		fsmResult = new FsmResult(SamMessageType.TEXT,"核审输入手机号",fsmContext);
		return fsmResult;
	}
	

}
