package zbh.aibc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.vehicle.InsuVehicleInfoRvo;
import zbh.aibc.appBeans.vehicle.VehicleModel;
import zbh.aibc.appBeans.vehicle.VehicleModelCvo;
import zbh.aibc.appBeans.vehicle.VehicleModelRvo;
import zbh.aibc.appBeans.vehicle.VehicleQueryExtend;
import anbox.aibc.model.member.CustomerVehicle;
import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.vehicle.*;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehiclemodel.ReqVehicleModel;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehiclemodel.ResVehicleModelList;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.reflect.ReflectUtil.SPFeature;
import zbh.remex.util.Judgment;

/**
 * 车型查询
 * 
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class VehicleModelQueryBs implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass = VehicleModelCvo.class, bsRvoBodyClass = VehicleModelRvo.class, bsCvoExtendClass = VehicleQueryExtend.class, bsRvoExtendClass = VehicleQueryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		final VehicleModelCvo vmCvo = bsCvo.getBody();
		VehicleModelRvo vmRvo = new VehicleModelRvo();
		List<VehicleModel> vehicleModels = new ArrayList<VehicleModel>();

		final String uid = AiwbUtils.getSessionMemberId();
		
		boolean flag = false;

		// 1先查本库
		DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<CustomerVehicle>(CustomerVehicle.class) {
			private static final long serialVersionUID = 1865012222355749887L;

			@Override
			public void initRules(CustomerVehicle t) {
				setDoCount(true);
				setDoPaging(true);
				setRowCount(10);
				setPagination(1); // 分页参数 TODO
				addRule(t.getFrameNo(), WhereRuleOper.eq, vmCvo.getFrameNo());
				addRule(t.getOpenId(), WhereRuleOper.eq, uid);
			}
		});
		CustomerVehicle vehicle = dbRvo.getRecords() >= 1 ? (CustomerVehicle) dbRvo.obtainBeans().get(0) : new CustomerVehicle();
		flag = dbRvo.getRecords() > 0 && (!Judgment.nullOrBlank(vehicle.getBrandName()) && vehicle.getPurchasePrice() != 0);
		vehicle.setFrameNo(vmCvo.getFrameNo());
		vehicle.setLicenseNo(vmCvo.getLicenseNo());
		vehicle.setEngineNo(vmCvo.getEngineNo());
		vehicle.setOpenId(uid);
		ContainerFactory.getSession().store(vehicle,"licenseNo;frameNo;engineNo;openId");
		if(flag){
			VehicleModel vehicleModel = new VehicleModel();
			ReflectUtil.copyProperties(vehicleModel, vehicle);
			vehicleModels.add(vehicleModel);
		}
		
		
		// 2查保险公司
		if (!flag) {
			/****************/
			ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
			vmCvo.setInsuCom(Judgment.nullOrBlank(vmCvo.getInsuCom()) ? "I00002" : vmCvo.getInsuCom());
			ReflectUtil.copyProperties(reqVehicleInfo, vmCvo);
			reqVehicleInfo.setLocalSearchFlag("Y"); // 是否本地库查询标志

			// 下面的代码必须补充注释!!
			if ("1".equals(vmCvo.getNewVehicleFlag()) && "I00001".equals(vmCvo.getInsuCom())) {
				reqVehicleInfo.setSeatCount("5");
				reqVehicleInfo.setVehicleTypeDesc(vmCvo.getBrandName());
				reqVehicleInfo.setOwnerCertType("01");
			}
			reqVehicleInfo.setVehicleType(AiwbConsts.vehicleType);// 车辆类型
			reqVehicleInfo.setVehicleKind(AiwbConsts.vehicleKind);// 车辆种类
			reqVehicleInfo.setEcdemicVehicleFlag(AiwbConsts.ecdemicVehicleFlag);// 外地车标志
			reqVehicleInfo.setLicenseColor(AiwbConsts.licenseColor);// 号牌颜色
			reqVehicleInfo.setLicenseType(AiwbConsts.licenseType);// 号牌种类
			reqVehicleInfo.setUseType(AiwbConsts.useType);// 使用性质
			ResVehicleModelData resVehicleModelDate = null;
			InsuVehicleInfoRvo insuVehicleInfoRvo = new InsuVehicleInfoRvo();
			try {
				resVehicleModelDate = (ResVehicleModelData) AiwbUtils.invokeService("AitpQueryVehicleInfoBs", "beginCarQuery", reqVehicleInfo, null, "ZHDX","");

				insuVehicleInfoRvo.setInsuCom("");
				insuVehicleInfoRvo.setIqvCount(resVehicleModelDate.getVehicleInfos().size());
				Map<String, String> map = new HashMap<String, String>();
				map.put("insuVehicles", "vehicleInfos");
				ReflectUtil.copyProperties(insuVehicleInfoRvo, resVehicleModelDate, map, SPFeature.DeeplyCopy);

				for (ResVehicleInfo rvi : resVehicleModelDate.getVehicleInfos()) {
					VehicleModel vehicleModel = new VehicleModel();
					ReflectUtil.copyProperties(vehicleModel, rvi);
					vehicleModels.add(vehicleModel);
				}
				flag = vehicleModels.size() > 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 3查阳光车型库
		if (!flag) {
			/*******************/
			// 根据情况调用服务,本库没有，或者排量、新车购置价为0时都去查一次。
			if (dbRvo.getRecords() == 0 || (vehicle.getExhaustCapacity() == 0 || vehicle.getPurchasePrice() == 0)) {

				ReqVehicleModel reqVehicleModel = new ReqVehicleModel();
				reqVehicleModel.setSearchType("1");// 1代表根据车架号查询
				ReflectUtil.copyProperties(reqVehicleModel, vmCvo);
				if(reqVehicleModel.getBrandName()==""){
					reqVehicleModel.setBrandName(null);
				}
				ResVehicleModelList resVehicleModelList = (ResVehicleModelList) AiwbUtils.invokeService("AitpVehicleModelBs", "execute", reqVehicleModel, null, "ZHDX","");
				for (int i = 0; i < resVehicleModelList.getResVehicleModels().size(); i++) {
					VehicleModel vehicleModel = new VehicleModel();
					ReflectUtil.copyProperties(vehicleModel, resVehicleModelList.getResVehicleModels().get(i));
					vehicleModels.add(vehicleModel);
				}
			}
		}

		vmRvo.setVehicleModels(vehicleModels);
		vmRvo.setVmCount(vehicleModels.size());
		bsRvo.setBody(vmRvo);
		bsRvo.setExtend(new VehicleQueryExtend(true, "OK"));

		return bsRvo;
	}
}
