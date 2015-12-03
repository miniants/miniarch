package cn.com.qqbx.wx;

import cn.remex.fsm.model.FsmContext;

public interface WxAction {
	public String obtainAction(FsmContext fsmContext,String sourceMsg);
}
