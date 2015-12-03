package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.ActualValue.ActualValueCvo;
import anbox.aibc.appBeans.ActualValue.ActualValueExtend;
import anbox.aibc.appBeans.ActualValue.ActualValueRvo;
import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.reflect.ReflectUtil;

/**车辆实际价值
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class VehicleActualValueBs implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass=ActualValueCvo.class,bsRvoBodyClass=ActualValueRvo.class,
			bsCvoExtendClass=ActualValueExtend.class,bsRvoExtendClass=ActualValueExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		ActualValueCvo avCvo = bsCvo.getBody();
		String flowNo = avCvo.getFlowNo();
		ReqActualValue reqActualValue = new ReqActualValue();
		ReflectUtil.copyProperties(reqActualValue, avCvo);
		try {
			ResActualValue resActualValue =(ResActualValue) AiwbUtils.invokeService("AitpActualValueBs", "queryCarRealPrice", reqActualValue,null,"ZHDX",flowNo);
			ActualValueRvo actualValueRvo = new ActualValueRvo();
			actualValueRvo.setActualValue(resActualValue.getActualValue());
			actualValueRvo.setInsuCom(avCvo.getInsuCom());
			bsRvo.setBody(actualValueRvo);
			bsRvo.setExtend(new ActualValueExtend(true, "OK"));
		} catch (Exception e) {
			bsRvo.setExtend(new ActualValueExtend(false, e.toString()));
		}
		return bsRvo;
	}
}
