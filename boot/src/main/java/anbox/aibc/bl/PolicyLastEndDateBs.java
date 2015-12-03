package anbox.aibc.bl;

import java.util.HashMap;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.policyLastEndDate.EndDateCvo;
import anbox.aibc.appBeans.policyLastEndDate.EndDateExtend;
import anbox.aibc.appBeans.policyLastEndDate.EndDateRvo;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqPolicyEndDate;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ResPolicyEndDate;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.reflect.ReflectUtil;

/**上年止期查询
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class PolicyLastEndDateBs  implements AiwbConsts{
	@SuppressWarnings("unchecked")
	private static Map<String,String> ledm = (Map<String,String>)AiwbUtils.getBean("Vehicle_SQL");
	
	@BsAnnotation(bsCvoBodyClass=EndDateCvo.class,bsRvoBodyClass=EndDateRvo.class,
			bsCvoExtendClass=EndDateExtend.class,bsRvoExtendClass=EndDateExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		
		EndDateCvo edCvo = bsCvo.getBody();
		String flowNo = edCvo.getFlowNo();
		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
		AiwbUtils.obtainDefaultVehicleInfo(reqVehicleInfo);
		
//		reqVehicleInfo.setTransferFlag("0"); //过户车标志
//		reqVehicleInfo.setRunMile("20000");	//行驶里程
//		reqVehicleInfo.setRunArea("02");	//行驶里程
//		reqVehicleInfo.setSpecialModelFlag("0");//古老特异车型
//		reqVehicleInfo.setLicenseType(AiwbConsts.licenseType);//号牌种类
//		reqVehicleInfo.setLicenseColor(AiwbConsts.licenseColor);//号牌种类
//		reqVehicleInfo.setVehicleKind(AiwbConsts.vehicleKind);//车辆种类

		ReflectUtil.copyProperties(reqVehicleInfo, edCvo.getVehicleInfo());
		reqPolicyEndDate.setInsuCom(edCvo.getInsuCom());
		reqPolicyEndDate.setTaxStatus("0");//车船税状态
		reqPolicyEndDate.setVehicleInfo(reqVehicleInfo);
		reqPolicyEndDate.setCityCode(edCvo.getCityCode());
		
		try {
			ResPolicyEndDate resPolicyEndDate =(ResPolicyEndDate) AiwbUtils.invokeService("AitpQueryPolicyEndDateBs", "queryLastContEndDate", reqPolicyEndDate,null, "ZHDX",flowNo);
			String tciLastEndDate = resPolicyEndDate.getTciLastEndDate();
			String vciLastEndDate = resPolicyEndDate.getVciLastEndDate();
			//将查询到的上年止期保存到客户车辆表中

			String updateSql = ledm.get("LastEndDate").replaceAll(":tciLastEndDate", tciLastEndDate).replaceAll(":vciLastEndDate", vciLastEndDate);
			HashMap<String, Object> params = new HashMap<String,Object>();
			params.put("licenseNo", reqVehicleInfo.getLicenseNo());
			params.put("engineNo", reqVehicleInfo.getEngineNo());
			params.put("frameNo", reqVehicleInfo.getFrameNo());
			params.put("openId", AiwbUtils.getSessionMemberId());
			ContainerFactory.getSession().executeUpdate(updateSql, params);
			
			EndDateRvo edRvo = new EndDateRvo();
			edRvo.setInsuCom(edCvo.getInsuCom());
			edRvo.setTciLastEndDate(tciLastEndDate);
			edRvo.setVciLastEndDate(vciLastEndDate);
			bsRvo.setBody(edRvo);
			bsRvo.setExtend(new EndDateExtend(true, "OK"));
		} catch (Exception e) {
			bsRvo.setExtend(new EndDateExtend(false, e.toString()));
		}
		return bsRvo;
	}
}
