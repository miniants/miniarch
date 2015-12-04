package zbh.wx.fsmaction;

import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmResult;
import zbh.remex.sam.SamConstant.SamMessageType;

public class OptionalPackagePriceAction extends FsmAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5095989983271737959L;

	@Override
	public FsmResult execute() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("自选套餐如下").append("\n");
		buffer.append("1.交强险+车船税").append("\n");
		buffer.append("2.机动车损失险").append("\n");
		buffer.append("    保额为新车购置价").append("\n");
		buffer.append("3.机动车盗抢险").append("\n");
		buffer.append("    a.10万").append("\n");
		buffer.append("    b.20万").append("\n");
		buffer.append("    c.30万").append("\n");
		buffer.append("    d.50万").append("\n");
		buffer.append("    e.100万").append("\n");
		buffer.append("4.机动车盗抢保险").append("\n");
		buffer.append("    保额为车辆实际价值").append("\n");
		buffer.append("5.车上人员责任险(司机)").append("\n");
		buffer.append("    保额1座*10000").append("\n");
		buffer.append("6.车上人员责任险(乘客)").append("\n");
		buffer.append("    保额4座*10000").append("\n");
		buffer.append("7.玻璃单独破碎险").append("\n");
		buffer.append("    f.国产玻璃").append("\n");
		buffer.append("    g.进口玻璃").append("\n");
		buffer.append("8.车身划痕损失险").append("\n");
		buffer.append("    h.2000").append("\n");
		buffer.append("    i.5000").append("\n");
		buffer.append("    j.10000").append("\n");
		buffer.append("    k.20000").append("\n");
		buffer.append("自燃损失险").append("\n");
		buffer.append("    保额为车辆实际价值").append("\n");
		buffer.append("9.发动机特别损失险").append("\n");
		buffer.append("  默认必须投保不计免赔").append("\n");
		buffer.append("回复?显示帮助信息").append("\n");
		//业务逻辑
		FsmResult fsmResult = new FsmResult(SamMessageType.TEXT,buffer.toString(),fsmContext);
		return fsmResult;
	}
	

}
