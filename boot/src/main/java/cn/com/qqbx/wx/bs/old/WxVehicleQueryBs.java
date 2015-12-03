package cn.com.qqbx.wx.bs.old;
//package cn.com.qqbx.wx.bs;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import net.sf.json.JSONObject;
//
//import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
//import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
//import cn.com.qqbx.aitp.aiws.xmlbeans.autoinsuinfo.ReqAutoInsuInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.autoinsuinfo.ResAutoInsuInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqCustomerInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqPolicyEndDate;
//import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ResPolicyEndDate;
//import cn.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
//import cn.com.qqbx.wx.appbean.ResultBean;
//import cn.com.qqbx.wx.appbean.WxVehicle;
//import cn.com.qqbx.wx.appbean.WxVehicleList;
//import cn.com.qqbx.wx.model.WxUser;
//import cn.com.qqbx.wx.model.wxvehicle.Vehicle;
//import cn.com.qqbx.wx.utils.WxUtil;
//import cn.remex.bs.Bs;
//import cn.remex.bs.BsCvo;
//import cn.remex.bs.BsRvo;
//import cn.remex.bs.Extend;
//import cn.remex.db.ContainerFactory;
//import cn.remex.db.DbCvo;
//import cn.remex.db.DbRvo;
//import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
//import cn.remex.reflect.ReflectUtil;
//import cn.remex.reflect.ReflectUtil.SPFeature;
//import cn.remex.util.DateHelper;
//import cn.remex.util.Judgment;
//
//public class WxVehicleQueryBs implements Bs {
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
////				addRule(v.getOpenId(), WhereRuleOper.cn,openId);
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
//		
//		
//		ReqAutoInsuInfo reqAutoInsuInfo = new ReqAutoInsuInfo();
//		reqAutoInsuInfo.setIdAgntCom("123");
//		reqAutoInsuInfo.setProductClass("AI");
//		reqAutoInsuInfo.setCityCode("110000");
//		reqAutoInsuInfo.setBirthday("");
//		reqAutoInsuInfo.setCertNo(ownerCertNo);
//		reqAutoInsuInfo.setCertType("1");
//		reqAutoInsuInfo.setIdExecCom("123");
//		reqAutoInsuInfo.setSex("1");
//		reqAutoInsuInfo.setName("xiong");
////		ResAutoInsuInfo resAutoInsuInfo =(ResAutoInsuInfo) WxUtil.invokeService("AitpAutoInsuInfoBs", "queryInsuranceInfo", reqAutoInsuInfo, "ZHDX", "TEST");
//
//		//ReflectUtil.copyProperties(wxVehicle,reqVehicleInfo,SPFeature.IgnoreEmptyStringValue);
//		ReflectUtil.copyProperties(reqVehicleInfo,wxVehicle,SPFeature.IgnoreEmptyStringValue);
//		
//		
//		try {
//			ResVehicleModelData resVehicleModelDate =(ResVehicleModelData) WxUtil.invokeService("AitpQueryVehicleInfoBs", "beginCarQuery", reqVehicleInfo,null, "ZHDX", "TEST");
//			/*****************多辆车******************/
//			if(resVehicleModelDate.getVehicleInfos().size()>1){
//				WxVehicleList wxVehicles = new WxVehicleList();
//				List<WxVehicle> wxVehicleList = new ArrayList<WxVehicle>();
//				for(int i=0;i<resVehicleModelDate.getVehicleInfos().size();i++){
//					WxVehicle vehicle = new WxVehicle();
//					ReflectUtil.copyProperties(vehicle, resVehicleModelDate.getVehicleInfos().get(i));
//					wxVehicleList.add(vehicle);
//					wxVehicles.setWxVehicles(wxVehicleList);
//				}
//				bsRvo.setBody(wxVehicles);
//				bsRvo.setExtend(new ResultBean(true, "操作成功"));
//			}else{
//				
//			
//			
//			
//			/*****************多辆车end******************/
//			
//			
//			
//			
//			ReflectUtil.copyProperties(wxVehicle,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//			//查询上年止期 得出保险起期 赋给wxVehicle 存库
//			ResPolicyEndDate policyEndDate = policyEndDate(wxVehicle);
//			
//			wxVehicle.setVciLastEndDate(policyEndDate.getVciLastEndDate());
//			wxVehicle.setTciLastEndDate(policyEndDate.getTciLastEndDate());
//			String tciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(policyEndDate.getTciLastEndDate())?new Date():DateHelper.parseDate(policyEndDate.getTciLastEndDate()), 1));
//			String vciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(policyEndDate.getVciLastEndDate())?new Date():DateHelper.parseDate(policyEndDate.getVciLastEndDate()), 1));
//			//查询实际价值  赋给wxVehicle 存库
//			wxVehicle.setTciStartDate(tciStartDate);
//			wxVehicle.setVciStartDate(vciStartDate);
//			ResActualValue actualValue = actualValue(wxVehicle);
//			String value = actualValue.getActualValue();
//			wxVehicle.setActualValue(Double.parseDouble(value));//TODO 是否会丢失精度
//			
//			
//			DbRvo res = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class){
//				private static final long serialVersionUID = -2291709702597738740L;
//				@Override
//				public void initRules(Vehicle v) {
////			setSearch(true);
//					//设置查询条件
//					addRule(v.getLicenseNo(), WhereRuleOper.cn,licenseNo);
//					addRule(v.getEngineNo(), WhereRuleOper.cn,engineNo);
//					addRule(v.getFrameNo(), WhereRuleOper.cn,frameNo);
//				}
//			});
//			List<Vehicle> vehicles = res.obtainBeans();
//			if(vehicles.size()==1){
//				Vehicle a = vehicles.get(0);
//				ReflectUtil.copyProperties(a,wxVehicle);
//				ReflectUtil.copyProperties(a,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//				a.setActualValue(wxVehicle.getActualValue());
//				ContainerFactory.getSession().store(a);
//			}else{
//				Vehicle vehicle = new Vehicle();
//				ReflectUtil.copyProperties(vehicle,wxVehicle);
//				ReflectUtil.copyProperties(vehicle,resVehicleModelDate.getVehicleInfos().get(0),SPFeature.IgnoreEmptyStringValue);
//				ContainerFactory.getSession().store(vehicle);
//			}
//			bsRvo.setBody(wxVehicle);
//			bsRvo.setExtend(new ResultBean(true, "操作成功"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			bsRvo.setExtend(new Extend(false, e.getMessage()));
//		}
//		
//		return bsRvo;
//	}
//	
//	public static ResPolicyEndDate policyEndDate(WxVehicle vehicle){
//		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
//		cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo reqVehicleInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo();
//		ReqCustomerInfo reqCustomerInfo = new ReqCustomerInfo();
//		List <ReqCustomerInfo> reqCustomerInfos = new ArrayList<ReqCustomerInfo>();
//		ReflectUtil.copyProperties(reqVehicleInfo,vehicle);
//		reqVehicleInfo.setTransferFlag("0");
//		reqVehicleInfo.setRunMile("20000");
//		
//		reqVehicleInfo.setRunArea("02");
//		reqVehicleInfo.setSpecialModelFlag("0");
//		reqPolicyEndDate.setCityCode("110000");
//		reqPolicyEndDate.setInsuCom(WxConst.insuCom);
//		reqPolicyEndDate.setTaxStatus("0");
//		reqCustomerInfo.setPersonType("2");
//		reqCustomerInfo.setPersonClass("0");
//		reqCustomerInfo.setCustomerType("1");
//		reqCustomerInfo.setVehicleRelation("1");
//		reqCustomerInfo.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo.setSex("1");
//		reqCustomerInfo.setCertType("01");
//		reqCustomerInfo.setCertNo(reqVehicleInfo.getOwnerCertNo());
//		reqCustomerInfos.add(reqCustomerInfo);
//		ReqCustomerInfo reqCustomerInfo1 = new ReqCustomerInfo();
//		
//		reqCustomerInfo1.setPersonType("1");
//		reqCustomerInfo1.setPersonClass("0");
//		reqCustomerInfo1.setCustomerType("1");
//		reqCustomerInfo1.setVehicleRelation("1");
//		reqCustomerInfo1.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo1.setSex("1");
//		reqCustomerInfo1.setCertType("01");
//		reqCustomerInfo1.setCertNo(reqVehicleInfo.getOwnerCertNo());
//		reqCustomerInfos.add(reqCustomerInfo1);
//		
//		ReqCustomerInfo reqCustomerInfo2 = new ReqCustomerInfo();
//		reqCustomerInfo2.setPersonType("0");
//		reqCustomerInfo2.setPersonClass("0");
//		reqCustomerInfo2.setCustomerType("1");
//		reqCustomerInfo2.setVehicleRelation("1");
//		reqCustomerInfo2.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo2.setSex("1");
//		reqCustomerInfo2.setCertType("01");
//		reqCustomerInfo2.setCertNo(reqVehicleInfo.getOwnerCertNo());
//		reqCustomerInfos.add(reqCustomerInfo2);
//		reqPolicyEndDate.setVehicleInfo(reqVehicleInfo);
//		reqPolicyEndDate.setAtinCustomers(reqCustomerInfos);
//	//	Request request = WxUtil.setReqVehicleInfoXml("queryLastContEndDate", reqPolicyEndDate,"ZHDX","001");
//	//	String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqPolicyEndDate.class,request);
//	//	String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQueryPolicyEndDateBs";
//	//	String responseXml = HttpHelper.sendXml(url,requestXml );
//		
//		ResPolicyEndDate resPolicyEndDate =(ResPolicyEndDate) WxUtil.invokeService("AitpQueryPolicyEndDateBs", "queryLastContEndDate", reqPolicyEndDate,null, "ZHDX", "TEST");
//	
//	return resPolicyEndDate;
//}
//	private static ResActualValue actualValue(WxVehicle vehicle){
//		ReqActualValue reqActualValue = new ReqActualValue();
//		reqActualValue.setCityCode("110000");
//		reqActualValue.setInsuCom(WxConst.insuCom);
//		reqActualValue.setPurchasePrice(String.valueOf(vehicle.getPurchasePrice()));
//		reqActualValue.setEnrollDate(vehicle.getEnrollDate());
////		reqActualValue.setVciStartDate(vehicle.getVciLastEndDate());
//		reqActualValue.setVciStartDate("2014-12-08");
//		reqActualValue.setUseType("8A");
//		reqActualValue.setVehicleKind("A0");
//		reqActualValue.setSeatCount(String.valueOf(vehicle.getSeatCount()));
//		reqActualValue.setVehicleTonnage("0.0");
//	
////	Request request = WxUtil.setReqVehicleInfoXml("queryCarRealPrice", reqActualValue,"ZHDX","001");
////	String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqActualValue.class,request);
//////	String responseXml = HttpHelper.sendXml("http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpActualValueBs", requestXml );
////	String responseXml = FileHelper.getFileContent("G:\\d.xml"); 
////	Response response =  AitpUtils.unmarshall(Response.class,
////			ResponseBody.class,ResActualValue.class, responseXml);
//	
//		ResActualValue resActualValue =(ResActualValue) WxUtil.invokeService("AitpActualValueBs", "queryCarRealPrice", reqActualValue,null,"ZHDX", "TEST");
//
//	return resActualValue; 
//}
//	
//}
//	