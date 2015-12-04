package zbh.aibc.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import anbox.aibc.appBeans.vehicle.InsuVehicleInfoCvo;
import anbox.aibc.appBeans.vehicle.InsuVehicleInfoRvo;
import anbox.aibc.appBeans.vehicle.VehicleQueryExtend;
import anbox.aibc.model.member.CustomerVehicle;
import zbh.aias.model.vehicle.Vehicle;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
import cn.remex.RemexConstants;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.reflect.ReflectUtil.SPFeature;
import zbh.remex.util.Judgment;
import zbh.remex.util.XmlHelper;

/**
 * 保险公司车辆查询
 * 
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class InsuVehicleQueryBs implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass = InsuVehicleInfoCvo.class, bsRvoBodyClass = InsuVehicleInfoRvo.class, bsCvoExtendClass = VehicleQueryExtend.class, bsRvoExtendClass = VehicleQueryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		final InsuVehicleInfoCvo insuVehicleInfoCvo = bsCvo.getBody();
		RemexConstants.logger.info(XmlHelper.marshall(insuVehicleInfoCvo));
		if(Judgment.nullOrBlank(insuVehicleInfoCvo.getInsuCom()) || Judgment.nullOrBlank(insuVehicleInfoCvo.getCityCode()) 
				|| Judgment.nullOrBlank(insuVehicleInfoCvo.getFrameNo()) ||Judgment.nullOrBlank(insuVehicleInfoCvo.getEngineNo())){
			bsRvo.setExtend(new VehicleQueryExtend(false, "车辆信息不准确"));
			return bsRvo;
		}
		
		
		String flowNo = insuVehicleInfoCvo.getFlowNo();
		// 判断是否有用户登录，若登录则保存/更新客户车辆信息 
		// final Member member = AiwbUtils.getSessionMember();
		final String uid = AiwbUtils.getSessionMemberId();
		CustomerVehicle customerVehicle = null;
		if (!Judgment.nullOrBlank(uid)) {// TODO 判断当前会话
			List<CustomerVehicle> customerVehicles = ContainerFactory.getSession().query(new DbCvo<CustomerVehicle>(CustomerVehicle.class) {
				private static final long serialVersionUID = 6877811822291608252L;

				@Override
				public void initRules(CustomerVehicle t) {// TODO 客户车辆的唯一性约束条件？？？
					addRule(t.getOpenId(), WhereRuleOper.eq, uid);
					if(!"1".equals(insuVehicleInfoCvo.getNewVehicleFlag())){
						addRule(t.getLicenseNo(), WhereRuleOper.eq, insuVehicleInfoCvo.getLicenseNo());
					}
					addRule(t.getEngineNo(), WhereRuleOper.eq, insuVehicleInfoCvo.getEngineNo());
					addRule(t.getFrameNo(), WhereRuleOper.eq, insuVehicleInfoCvo.getFrameNo());
				}

			}).obtainBeans();

			if (customerVehicles.size() >= 1) {
				customerVehicle = customerVehicles.get(0);
				customerVehicle.setDisplayStatus(DisplayStatus_Y);
				ReflectUtil.copyProperties(customerVehicle, insuVehicleInfoCvo);
				customerVehicle.setInsuCom(insuVehicleInfoCvo.getInsuCom());

			} else {
				customerVehicle = new CustomerVehicle();
				customerVehicle.setDisplayStatus(DisplayStatus_Y);
				// customerVehicle.setMember(member);
				customerVehicle.setOpenId(uid);// 这个比直接存member更好，两个都保留。
				customerVehicle.setInsuCom(insuVehicleInfoCvo.getInsuCom());
				ReflectUtil.copyProperties(customerVehicle, insuVehicleInfoCvo);

			}
			ContainerFactory.getSession().store(customerVehicle);
		}else{
			bsRvo.setExtend(new VehicleQueryExtend(false, "客户端请求数据异常！", "E200001", "客户端请求数据异常！", null));
			return bsRvo;
		}

		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
		AiwbUtils.obtainDefaultVehicleInfo(reqVehicleInfo);
		ReflectUtil.copyProperties(reqVehicleInfo, insuVehicleInfoCvo);
		reqVehicleInfo.setLocalSearchFlag("Y"); // 是否本地库查询标志

		// 下面的代码必须补充注释!!
		if ("1".equals(insuVehicleInfoCvo.getNewVehicleFlag()) && "I00001".equals(insuVehicleInfoCvo.getInsuCom())) {
			reqVehicleInfo.setSeatCount("5");
			reqVehicleInfo.setVehicleTypeDesc(insuVehicleInfoCvo.getBrandName());
			reqVehicleInfo.setOwnerCertType("01");//证件类型
		}
//		reqVehicleInfo.setVehicleType(AiwbConsts.vehicleType);// 车辆类型
//		reqVehicleInfo.setVehicleKind(AiwbConsts.vehicleKind);// 车辆种类
//		reqVehicleInfo.setEcdemicVehicleFlag(AiwbConsts.ecdemicVehicleFlag);// 外地车标志
//		reqVehicleInfo.setLicenseColor(AiwbConsts.licenseColor);// 号牌颜色
//		reqVehicleInfo.setLicenseType(AiwbConsts.licenseType);// 号牌种类
//		reqVehicleInfo.setUseType(AiwbConsts.useType);// 使用性质
		ResVehicleModelData resVehicleModelDate = null;
		InsuVehicleInfoRvo insuVehicleInfoRvo = new InsuVehicleInfoRvo();
		try {
			resVehicleModelDate = (ResVehicleModelData) AiwbUtils.invokeService("AitpQueryVehicleInfoBs", "beginCarQuery", reqVehicleInfo, null, "ZHDX",flowNo);

			List<Vehicle> vehicles = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class) {
				private static final long serialVersionUID = -2291709702597738740L;

				@Override
				public void initRules(Vehicle v) {// TODO 车辆的唯一性约束条件？？？
				// setSearch(true);
					// 设置查询条件
					setDoCount(true);
					setDoPaging(true);
					setRowCount(10);
					setPagination(1); // 分页参数 TODO
					addRule(v.getLicenseNo(), WhereRuleOper.cn, insuVehicleInfoCvo.getLicenseNo());
					addRule(v.getEngineNo(), WhereRuleOper.cn, insuVehicleInfoCvo.getEngineNo());
					addRule(v.getFrameNo(), WhereRuleOper.cn, insuVehicleInfoCvo.getFrameNo());
				}
			}).obtainBeans();
			Vehicle vehicle;
			if (vehicles.size() == 1) {
				vehicle = vehicles.get(0);
				ReflectUtil.copyProperties(vehicle, insuVehicleInfoCvo);
				ReflectUtil.copyProperties(vehicle, resVehicleModelDate.getVehicleInfos().get(0), SPFeature.IgnoreEmptyStringValue);
				// ContainerFactory.getSession().store(vehicle);//TODO 车辆查询完毕更新车辆信息还是？？;
			} else {
				vehicle = new Vehicle();
				ReflectUtil.copyProperties(vehicle, insuVehicleInfoCvo);
				ReflectUtil.copyProperties(vehicle, resVehicleModelDate.getVehicleInfos().get(0), SPFeature.IgnoreEmptyStringValue);
				ContainerFactory.getSession().store(vehicle);
				
			}
			
			ReflectUtil.copyProperties(customerVehicle, vehicle);
			ReflectUtil.copyProperties(customerVehicle, resVehicleModelDate.getVehicleInfos().get(0), SPFeature.IgnoreEmptyStringValue);
			customerVehicle.setOpenId(uid);// 这个比直接存member更好，两个都保留。
//			Member m = AiwbUtils.getSessionMember();
//			if(null != m.getId()){
//				customerVehicle.setMember(m);
//			}
			ContainerFactory.getSession().store(customerVehicle);

			insuVehicleInfoRvo.setInsuCom(insuVehicleInfoCvo.getInsuCom());
			insuVehicleInfoRvo.setIqvCount(resVehicleModelDate.getVehicleInfos().size());
			Map<String, String> map = new HashMap<String, String>();
			map.put("insuVehicles", "vehicleInfos");
			ReflectUtil.copyProperties(insuVehicleInfoRvo, resVehicleModelDate, map, SPFeature.DeeplyCopy);
		} catch (Exception e) {
			// TODO 临时处理，列入后台的BL层解决
			String msg = AiwbUtils.transErrorMsg(e.getMessage());
			e.printStackTrace();
			bsRvo.setExtend(new VehicleQueryExtend(false, msg, "E100001", msg, null));
			return bsRvo;
		}
		
		
		bsRvo.setBody(insuVehicleInfoRvo);
		bsRvo.setExtend(new VehicleQueryExtend(true, "OK"));

		return bsRvo;
	}
}
