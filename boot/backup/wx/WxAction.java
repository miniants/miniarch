package zbh.wx;

import zbh.remex.fsm.model.FsmContext;

public interface WxAction {
	public String obtainAction(FsmContext fsmContext,String sourceMsg);
}
