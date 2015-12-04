package zbh.wx.bs.old;
//package zbh.com.qqbx.wx.bs;
//
//import java.util.List;
//
//import zbh.com.qqbx.aitp.aiws.xmlbeans.autoinsuinfo.ReqAutoInsuInfo;
//import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
//import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
//import ResultBean;
//import WxVehicle;
//import zbh.com.qqbx.wx.model.wxvehicle.Vehicle;
//import WxUtil;
//import zbh.remex.bs.Bs;
//import zbh.remex.bs.BsCvo;
//import zbh.remex.bs.BsRvo;
//import ContainerFactory;
//import DbCvo;
//import DbRvo;
//import RsqlConstants.WhereRuleOper;
//import zbh.remex.reflect.ReflectUtil;
//import zbh.remex.reflect.ReflectUtil.SPFeature;
//
//public class VehicleQueryBs implements Bs {
//	
//	@Override
//	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		WxVehicle wxVehicle = bsCvo.getBody(WxVehicle.class);
//		final String licenseNo = wxVehicle.getLicenseNo();
//		final String engineNo = wxVehicle.getEngineNo();
//		final String frameNo = wxVehicle.getFrameNo();
//		String owner = wxVehicle.getOwner();
//		String ownerCertNo = wxVehicle.getOwnerCertNo();
//		String brandName = wxVehicle.getBrandName();
//		String enrollDate = wxVehicle.getEnrollDate();
//		String cityCode = "110000"; // TODO 页面获取
//		String insuCom=WxConst.insuCom;// TODO 页面获取
//		
//		/********************网页授权获取用户信息部分****************************/
//		
////		String code = wxVehicle.getCode();
////		final String openId = WxUtil.oauth(code);
////		wxVehicle.setOpenId(openId);
////		
////		JSONObject json = WxUtil.obtainUserInfo(openId);
////		String sex=String.valueOf(json.get("sex"));
////		String nickName = (String) json.get("nickname");
////		String city = (String) json.get("city");
////		
////		DbRvo resp = ContainerFactory.getSession().query(new DbCvo<WxUser>(WxUser.class){
////			private static final long serialVersionUID = -2291709702597738740L;
////
////			@Override
////			public void initRules(WxUser v) {
//////				setSearch(true);
////				//设置查询条件
////				addRule(v.getOpenId(), WhereRuleOper.zbh,openId);
////			}
////		});
////			List<WxUser> wxUsers = resp.obtainBeans();
////			if(wxUsers.size()==1){
////				WxUser w = wxUsers.get(0);
////				w.setSex(sex);
////				w.setNickName(nickName);
////				w.setCity(city);
////				ContainerFactory.getSession().store(w);
////			}else{
////				WxUser wxUser = new WxUser();
////				wxUser.setOpenId(openId);
////				wxUser.setSex(sex);
////				wxUser.setNickName(nickName);
////				wxUser.setCity(city);
////				ContainerFactory.getSession().store(wxUser);
////			}
////			
////			
//			
//		
//		
//		
//		/******************************************************************/
//		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
//		reqVehicleInfo.setInsuCom(insuCom);//
//		reqVehicleInfo.setCityCode(cityCode);
//		reqVehicleInfo.setLocalSearchFlag("Y");
//		reqVehicleInfo.setVehicleType("K33");
//		reqVehicleInfo.setVehicleKind("A0");
//		reqVehicleInfo.setNewVehicleFlag("0");
//		reqVehicleInfo.setEngineNo(engineNo);                      
//		reqVehicleInfo.setFrameNo(frameNo);
//		reqVehicleInfo.setEcdemicVehicleFlag("0");
//		reqVehicleInfo.setOwner(owner);
//		reqVehicleInfo.setLicenseNo(licenseNo);
//		reqVehicleInfo.setLicenseColor("01");
//		reqVehicleInfo.setLicenseType("02");
//		reqVehicleInfo.setUseType("8A");
//		reqVehicleInfo.setOwnerCertNo(ownerCertNo);
////		ReqAutoInsuInfo reqAutoInsuInfo = new ReqAutoInsuInfo();
////		reqAutoInsuInfo.setIdAgntCom("123");
////		reqAutoInsuInfo.setProductClass("AI");
////		reqAutoInsuInfo.setCityCode("110000");
////		reqAutoInsuInfo.setBirthday("");
////		reqAutoInsuInfo.setCertNo(ownerCertNo);
////		reqAutoInsuInfo.setCertType("1");
////		reqAutoInsuInfo.setIdExecCom("123");
////		reqAutoInsuInfo.setSex("1");
////		reqAutoInsuInfo.setName("xiong");
//		ReflectUtil.copyProperties(reqVehicleInfo,wxVehicle,SPFeature.IgnoreEmptyStringValue);
//		ResVehicleModelData resVehicleModelDate =(ResVehicleModelData) WxUtil.invokeService("AitpQueryVehicleInfoBs", "beginCarQuery", reqVehicleInfo,null, "ZHDX", "TEST");
//		ReflectUtil.copyProperties(wxVehicle,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//			
//		DbRvo res = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class){
//			private static final long serialVersionUID = -2291709702597738740L;
//			@Override
//			public void initRules(Vehicle v) {
////			setSearch(true);
//				//设置查询条件
//				addRule(v.getLicenseNo(), WhereRuleOper.zbh,licenseNo);
//				addRule(v.getEngineNo(), WhereRuleOper.zbh,engineNo);
//				addRule(v.getFrameNo(), WhereRuleOper.zbh,frameNo);
//			}
//		});
//		List<Vehicle> vehicles = res.obtainBeans();
//		if(vehicles.size()==1){
//			Vehicle a = vehicles.get(0);
//			ReflectUtil.copyProperties(a,wxVehicle);
//			ReflectUtil.copyProperties(a,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//			a.setActualValue(wxVehicle.getActualValue());
//			ContainerFactory.getSession().store(a);
//		}else{
//			Vehicle vehicle = new Vehicle();
//			ReflectUtil.copyProperties(vehicle,wxVehicle);
//			ReflectUtil.copyProperties(vehicle,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//			ContainerFactory.getSession().store(vehicle);
//		}
//		bsRvo.setBody(wxVehicle);
//		bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		
//		return bsRvo;
//	}
//	
//}
//	