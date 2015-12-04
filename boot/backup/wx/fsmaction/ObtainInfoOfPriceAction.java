package zbh.wx.fsmaction;

import java.util.HashMap;
import java.util.Map;

import zbh.com.qqbx.aitp.AitpUtils;
import zbh.com.qqbx.aitp.aiws.xmlbeans.Request;
import zbh.com.qqbx.aitp.aiws.xmlbeans.RequestBody;
import zbh.com.qqbx.aitp.aiws.xmlbeans.Response;
import zbh.com.qqbx.aitp.aiws.xmlbeans.ResponseBody;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqCustomerInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqPolicyEndDate;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ResPolicyEndDate;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCoverageInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqQuotePriceInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmResult;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.sam.SamConstant.SamMessageType;
import zbh.remex.util.JsonHelper;

public class ObtainInfoOfPriceAction extends FsmAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5095989983271737959L;

	@Override
	public FsmResult execute() {
		
		Map<String, Object> flowContextParams = JsonHelper.toJavaObject(fsmContext.getCurFlowContext().getContextParams(), HashMap.class);
		String licenseNo = flowContextParams.get("licenseNo").toString();
		String engineNo = flowContextParams.get("engineNo").toString();
		String frameNo = flowContextParams.get("frameNo").toString();
		String openId = fsmContext.getUserId();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("请选择险种套餐").append("\n");
		buffer.append("A.基本型").append("\n");
		buffer.append("B.豪华型").append("\n");
//		buffer.append("C.自选型").append("\n");
		buffer.append(" 自选套餐如下").append("\n");
		buffer.append(" 1.交强险+车船税").append("\n");
		buffer.append(" 2.机动车损失险").append("\n");
		buffer.append("    保额为新车购置价").append("\n");
		buffer.append(" 3.机动车盗抢险").append("\n");
		buffer.append("    1.10万").append("\n");
		buffer.append("    2.20万").append("\n");
		buffer.append("    3.30万").append("\n");
		buffer.append("    4.50万").append("\n");
		buffer.append("    5.100万").append("\n");
		buffer.append(" 4.机动车盗抢保险").append("\n");
		buffer.append("    保额为车辆实际价值").append("\n");
		buffer.append(" 5.车上人员责任险(司机)").append("\n");
		buffer.append("    保额1座*10000").append("\n");
		buffer.append(" 6.车上人员责任险(乘客)").append("\n");
		buffer.append("    保额4座*10000").append("\n");
		buffer.append(" 7.玻璃单独破碎险").append("\n");
		buffer.append("    1.国产玻璃").append("\n");
		buffer.append("    2.进口玻璃").append("\n");
		buffer.append(" 8.车身划痕损失险").append("\n");
		buffer.append("    1.2000").append("\n");
		buffer.append("    2.5000").append("\n");
		buffer.append("    3.10000").append("\n");
		buffer.append("    4.20000").append("\n");
		buffer.append(" 9.自燃损失险").append("\n");
		buffer.append("    保额为车辆实际价值").append("\n");
		buffer.append(" 10.发动机特别损失险").append("\n");
		buffer.append("  默认必须投保不计免赔").append("\n");
		buffer.append("回复?显示帮助信息").append("\n");
		buffer.append("若操作不方便请点击如下链接：http://www.qqbx.com.zbh/wx/wx/page/method.html?"+openId+"&"+licenseNo+"&"+engineNo+"&"+frameNo).append("\n");
		//业务逻辑
		
		FsmResult fsmResult = new FsmResult(SamMessageType.TEXT,buffer.toString(),fsmContext);
		return fsmResult;
	}
	
	
}
