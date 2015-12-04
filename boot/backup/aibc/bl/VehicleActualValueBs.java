package zbh.aibc.bl;

import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.ActualValue.ActualValueCvo;
import zbh.aibc.appBeans.ActualValue.ActualValueExtend;
import zbh.aibc.appBeans.ActualValue.ActualValueRvo;
import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.ActualValue.ActualValueCvo;
import zbh.aibc.appBeans.ActualValue.ActualValueExtend;
import zbh.aibc.appBeans.ActualValue.ActualValueRvo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import zbh.remex.reflect.ReflectUtil;

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
			ResActualValue resActualValue =(ResActualValue) AiwbUtils.invokeService("AitpActualValueBs", "queryCarRealPrice", reqActualValue, null, "ZHDX", flowNo);
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
