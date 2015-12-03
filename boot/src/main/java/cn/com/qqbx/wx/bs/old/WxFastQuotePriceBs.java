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
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCoverageInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqQuotePriceInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
//import cn.com.qqbx.wx.appbean.AllInfoOfPrice;
//import cn.com.qqbx.wx.appbean.ResultBean;
//import cn.com.qqbx.wx.appbean.WxVehicle;
//import cn.com.qqbx.wx.appbean.WxVehicleList;
//import cn.com.qqbx.wx.model.WxUser;
//import cn.com.qqbx.wx.model.wxquotation.AtinQttnCoverage;
//import cn.com.qqbx.wx.model.wxquotation.AtinQuotation;
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
//public class WxFastQuotePriceBs implements Bs {
//	
//	@Override
//	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		WxVehicle wxVehicle = bsCvo.getBody(WxVehicle.class);
//		String pack = wxVehicle.getPack();
//		String insuCom = wxVehicle.getInsuCom();
//		final String licenseNo = wxVehicle.getLicenseNo();
//		final String engineNo = wxVehicle.getEngineNo();
//		final String frameNo = wxVehicle.getFrameNo();
//		String openId = wxVehicle.getOpenId();
//		String owner = wxVehicle.getOwner();
//		String ownerCertNo = wxVehicle.getOwnerCertNo();
//		String brandName = wxVehicle.getBrandName();
//		String enrollDate = wxVehicle.getEnrollDate();
//		String cityCode = "110000"; // TODO 页面获取
//		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
//		reqVehicleInfo.setInsuCom(WxConst.insuCom);//
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
//		ResVehicleModelData resVehicleModelDate = new ResVehicleModelData();
////		if("Y".equals(wxVehicle.getIsOneCar())){
//			ReflectUtil.copyProperties(wxVehicle,reqVehicleInfo,SPFeature.IgnoreEmptyStringValue);
//			resVehicleModelDate =(ResVehicleModelData) WxUtil.invokeService("AitpQueryVehicleInfoBs", "beginCarQuery", reqVehicleInfo,null, "ZHDX", "TEST");
////		}else{
//			ReflectUtil.copyProperties(resVehicleModelDate, wxVehicle);
////		}
//		
//		
//		
//		/****************多辆车*********************/
//		if(resVehicleModelDate.getVehicleInfos().size()>1){
//			WxVehicleList wxVehicles = new WxVehicleList();
//			List<WxVehicle> wxVehicleList = new ArrayList<WxVehicle>();
//			for(int i=0;i<resVehicleModelDate.getVehicleInfos().size();i++){
//				WxVehicle vehicle = new WxVehicle();
//				ReflectUtil.copyProperties(vehicle, resVehicleModelDate.getVehicleInfos().get(i));
//				wxVehicleList.add(vehicle);
//				wxVehicles.setWxVehicles(wxVehicleList);
//			}
//			bsRvo.setBody(wxVehicles);
//			bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		}else{
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
//			private static final long serialVersionUID = -2291709702597738740L;
//			@Override
//			public void initRules(Vehicle v) {
////				setSearch(true);
//				//设置查询条件
//				addRule(v.getLicenseNo(), WhereRuleOper.cn,licenseNo);
//				addRule(v.getEngineNo(), WhereRuleOper.cn,engineNo);
//				addRule(v.getFrameNo(), WhereRuleOper.cn,frameNo);
//			}
//		});
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
//			
//			List<ReqCoverageInfo> reqCoverageInfos = new ArrayList<ReqCoverageInfo>();
//			
//			if("base".equals(pack)){
//				
//			
//				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//				reqCoverageInfo.setPrdtKindCode("TCI");
//				reqCoverageInfo.setCoverageCode("BZ");
//				reqCoverageInfo.setCoverageName("交强险");
//				reqCoverageInfos.add(reqCoverageInfo);
//				
//				ReqCoverageInfo reqCoverageInfo1 = new ReqCoverageInfo();
//				reqCoverageInfo1.setPrdtKindCode("VCI");
//				reqCoverageInfo1.setCoverageCode("A");
//				reqCoverageInfo1.setDeductibleFlag("Y");
//				reqCoverageInfo1.setCoverageName("机动车损险");
//				reqCoverageInfo1.setAmount(String.valueOf(wxVehicle.getPurchasePrice()));
//				reqCoverageInfos.add(reqCoverageInfo1);
//				ReqCoverageInfo reqCoverageInfo2 = new ReqCoverageInfo();
//				reqCoverageInfo2.setPrdtKindCode("VCI");
//				reqCoverageInfo2.setCoverageCode("B");
//				reqCoverageInfo2.setDeductibleFlag("Y");
//				reqCoverageInfo2.setCoverageName("第三者责任险");
//				reqCoverageInfo2.setAmount("200000");
//				reqCoverageInfos.add(reqCoverageInfo2);
//				ReqCoverageInfo reqCoverageInfo4 = new ReqCoverageInfo();
//				reqCoverageInfo4.setPrdtKindCode("VCI");
//				reqCoverageInfo4.setCoverageCode("D3");
//				reqCoverageInfo4.setDeductibleFlag("Y");
//				reqCoverageInfo4.setCoverageName("车上人员责任险(司机)");
//				reqCoverageInfo4.setUnitAmount("10000");
//				reqCoverageInfo4.setQuantity("1");
//				reqCoverageInfo4.setAmount("10000");
//				reqCoverageInfos.add(reqCoverageInfo4);
//				ReqCoverageInfo reqCoverageInfo5 = new ReqCoverageInfo();
//				reqCoverageInfo5.setPrdtKindCode("VCI");
//				reqCoverageInfo5.setCoverageCode("D4");
//				reqCoverageInfo5.setDeductibleFlag("Y");
//				reqCoverageInfo5.setCoverageName("车上人员责任险(乘客)");
//				reqCoverageInfo5.setUnitAmount("10000");
//				reqCoverageInfo5.setQuantity("4");
//				reqCoverageInfo5.setAmount("40000");
//				reqCoverageInfos.add(reqCoverageInfo5);
////				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
////				reqCoverageInfo.setPrdtKindCode("VCI");
////				reqCoverageInfo.setCoverageCode("F");
////				reqCoverageInfo.setModeCode("1");
////				reqCoverageInfo.setCoverageName("玻璃单独破碎险");
////				reqCoverageInfos.add(reqCoverageInfo);
////				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
////				reqCoverageInfo.setPrdtKindCode("VCI");
////				reqCoverageInfo.setCoverageCode("L");
////				reqCoverageInfo.setDeductibleFlag("Y");
////				reqCoverageInfo.setCoverageName("车身划痕损失险");
////				reqCoverageInfo.setAmount(scratchesInsurance);
////				reqCoverageInfos.add(reqCoverageInfo);
////				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
////				reqCoverageInfo.setPrdtKindCode("VCI");
////				reqCoverageInfo.setCoverageCode("Z");
////				reqCoverageInfo.setDeductibleFlag("Y");
////				reqCoverageInfo.setCoverageName("自燃损失险");
////				reqCoverageInfo.setAmount(String.valueOf(vehicles.get(0).getPurchasePrice()));
////				reqCoverageInfos.add(reqCoverageInfo);
////				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
////				reqCoverageInfo.setPrdtKindCode("VCI");
////				reqCoverageInfo.setCoverageCode("X1");
////				reqCoverageInfo.setDeductibleFlag("Y");
////				reqCoverageInfo.setCoverageName("发动机特别损失险");
////				reqCoverageInfos.add(reqCoverageInfo);
//				ReqCoverageInfo reqCoverageInfo6 = new ReqCoverageInfo();
//				reqCoverageInfo6.setPrdtKindCode("VCI");
//				reqCoverageInfo6.setCoverageCode("M");
//				reqCoverageInfo6.setCoverageName("不计免赔险");
//				reqCoverageInfos.add(reqCoverageInfo6);
//			}else if("invk".equals(pack)){
//
//				
//				
//				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//				reqCoverageInfo.setPrdtKindCode("TCI");
//				reqCoverageInfo.setCoverageCode("BZ");
//				reqCoverageInfo.setCoverageName("交强险");
//				reqCoverageInfos.add(reqCoverageInfo);
//				
//				ReqCoverageInfo reqCoverageInfo1 = new ReqCoverageInfo();
//				reqCoverageInfo1.setPrdtKindCode("VCI");
//				reqCoverageInfo1.setCoverageCode("A");
//				reqCoverageInfo1.setDeductibleFlag("Y");
//				reqCoverageInfo1.setCoverageName("机动车损险");
//				reqCoverageInfo1.setAmount(String.valueOf(wxVehicle.getPurchasePrice()));
//				reqCoverageInfos.add(reqCoverageInfo1);
//				ReqCoverageInfo reqCoverageInfo2 = new ReqCoverageInfo();
//				reqCoverageInfo2.setPrdtKindCode("VCI");
//				reqCoverageInfo2.setCoverageCode("B");
//				reqCoverageInfo2.setDeductibleFlag("Y");
//				reqCoverageInfo2.setCoverageName("第三者责任险");
//				reqCoverageInfo2.setAmount("500000");
//				reqCoverageInfos.add(reqCoverageInfo2);
//				ReqCoverageInfo reqCoverageInfo3 = new ReqCoverageInfo();
//				reqCoverageInfo3.setPrdtKindCode("VCI");
//				reqCoverageInfo3.setCoverageCode("G1");
//				reqCoverageInfo3.setDeductibleFlag("Y");
//				reqCoverageInfo3.setCoverageName("机动车盗抢险");
//				reqCoverageInfo3.setAmount(String.valueOf(wxVehicle.getActualValue()));
//				reqCoverageInfos.add(reqCoverageInfo3);
//				ReqCoverageInfo reqCoverageInfo4 = new ReqCoverageInfo();
//				reqCoverageInfo4.setPrdtKindCode("VCI");
//				reqCoverageInfo4.setCoverageCode("D3");
//				reqCoverageInfo4.setDeductibleFlag("Y");
//				reqCoverageInfo4.setCoverageName("车上人员责任险(司机)");
//				reqCoverageInfo4.setUnitAmount("10000");
//				reqCoverageInfo4.setQuantity("1");
//				reqCoverageInfo4.setAmount("10000");
//				reqCoverageInfos.add(reqCoverageInfo4);
//				ReqCoverageInfo reqCoverageInfo5 = new ReqCoverageInfo();
//				reqCoverageInfo5.setPrdtKindCode("VCI");
//				reqCoverageInfo5.setCoverageCode("D4");
//				reqCoverageInfo5.setDeductibleFlag("Y");
//				reqCoverageInfo5.setCoverageName("车上人员责任险(乘客)");
//				reqCoverageInfo5.setUnitAmount("10000");
//				reqCoverageInfo5.setQuantity("4");
//				reqCoverageInfo5.setAmount("40000");
//				reqCoverageInfos.add(reqCoverageInfo5);
//				ReqCoverageInfo reqCoverageInfo6 = new ReqCoverageInfo();
//				reqCoverageInfo6.setPrdtKindCode("VCI");
//				reqCoverageInfo6.setCoverageCode("F");
//				reqCoverageInfo6.setModeCode("1");
//				reqCoverageInfo6.setCoverageName("玻璃单独破碎险");
//				reqCoverageInfos.add(reqCoverageInfo6);
//				ReqCoverageInfo reqCoverageInfo7 = new ReqCoverageInfo();
//				reqCoverageInfo7.setPrdtKindCode("VCI");
//				reqCoverageInfo7.setCoverageCode("L");
//				reqCoverageInfo7.setDeductibleFlag("Y");
//				reqCoverageInfo7.setCoverageName("车身划痕损失险");
//				reqCoverageInfo7.setAmount("10000");
//				reqCoverageInfos.add(reqCoverageInfo7);
//				ReqCoverageInfo reqCoverageInfo8 = new ReqCoverageInfo();
//				reqCoverageInfo8.setPrdtKindCode("VCI");
//				reqCoverageInfo8.setCoverageCode("Z");
//				reqCoverageInfo8.setDeductibleFlag("Y");
//				reqCoverageInfo8.setCoverageName("自燃损失险");
//				reqCoverageInfo8.setAmount(String.valueOf(wxVehicle.getActualValue()));
//				reqCoverageInfos.add(reqCoverageInfo8);
//				ReqCoverageInfo reqCoverageInfo9 = new ReqCoverageInfo();
//				reqCoverageInfo9.setPrdtKindCode("VCI");
//				reqCoverageInfo9.setCoverageCode("X1");
//				reqCoverageInfo9.setDeductibleFlag("Y");
//				reqCoverageInfo9.setCoverageName("发动机特别损失险");
//				reqCoverageInfos.add(reqCoverageInfo9);
//				ReqCoverageInfo reqCoverageInfo10 = new ReqCoverageInfo();
//				reqCoverageInfo10.setPrdtKindCode("VCI");
//				reqCoverageInfo10.setCoverageCode("M");
//				reqCoverageInfo10.setCoverageName("不计免赔险");
//				reqCoverageInfos.add(reqCoverageInfo10);
//			}
//			
//				try{
//					AllInfoOfPrice resQuotePriceInfo = quotePriceInfo(insuCom,wxVehicle.getCityCode(),reqCoverageInfos,vehicles.get(0),owner,ownerCertNo,owner,ownerCertNo,owner,ownerCertNo,openId);
//					bsRvo.setBody(resQuotePriceInfo);
//					bsRvo.setExtend(new ResultBean(true, "操作成功"));
//				}catch (Exception e) {
//					e.printStackTrace();
//					bsRvo.setExtend(new Extend(false, e.toString()));
//				}
//		}
//		
//		/*****************************************/
//		
//		
//		
//		
//			return bsRvo;
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
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
//		reqActualValue.setVciStartDate("2014-12-08");
//		reqActualValue.setUseType("8A");
//		reqActualValue.setVehicleKind("A0");
//		reqActualValue.setSeatCount(String.valueOf(vehicle.getSeatCount()));
//		reqActualValue.setVehicleTonnage("0.0");
//	
//	
//		ResActualValue resActualValue =(ResActualValue) WxUtil.invokeService("AitpActualValueBs", "queryCarRealPrice", reqActualValue,null,"ZHDX", "TEST");
//
//	return resActualValue; 
//}
//	private static AllInfoOfPrice quotePriceInfo(String insuCom, String cityCode,List<ReqCoverageInfo> reqCoverageInfos,Vehicle vehicle, String owner, String ownerCertNo, String insurer, String insurerCertNo, String insurered, String insureredCertNo,String openId){
//		ReqQuotePriceInfo reqQuotePriceInfo = new ReqQuotePriceInfo();
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo reqVehicleInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo();
//		List<cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo> reqCustomerInfos = new ArrayList<cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo>(); //客户信息
//		reqQuotePriceInfo.setAgntComTransNo("1409458030019");
//		reqQuotePriceInfo.setCityCode(cityCode);
//		reqQuotePriceInfo.setInsuCom(insuCom);
//		reqQuotePriceInfo.setTaxStatus("0");
//		reqQuotePriceInfo.setFuelType("A");
//		
//		ReflectUtil.copyProperties(reqVehicleInfo, vehicle);
//		reqVehicleInfo.setTransferFlag("0");
//		reqVehicleInfo.setRunArea("02");
//		reqVehicleInfo.setSpecialModelFlag("0");
//		reqVehicleInfo.setNewVehicleFlag("0");
//		reqVehicleInfo.setEcdemicVehicleFlag("0");
//		reqVehicleInfo.setVehicleKind("A0");
//		reqVehicleInfo.setUseType("8A");
//		reqVehicleInfo.setVehicleType("K33");
//		reqVehicleInfo.setOwnerCertType("01");
//		reqVehicleInfo.setRunMile("20000");
//		reqVehicleInfo.setTotalRunMile("15000");
//		reqQuotePriceInfo.setVehicleInfo(reqVehicleInfo);
//		
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		reqCustomerInfo.setPersonType("2");
//		reqCustomerInfo.setPersonClass("0");
//		reqCustomerInfo.setCustomerType("1");
//		reqCustomerInfo.setVehicleRelation("1");
//		reqCustomerInfo.setName(insurer);
//		reqCustomerInfo.setSex("1");
//		reqCustomerInfo.setCertType("01");
//		reqCustomerInfo.setCertNo(insurerCertNo);
//		reqCustomerInfos.add(reqCustomerInfo);
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo1 = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		
//		reqCustomerInfo1.setPersonType("1");
//		reqCustomerInfo1.setPersonClass("0");
//		reqCustomerInfo1.setCustomerType("1");
//		reqCustomerInfo1.setVehicleRelation("1");
//		reqCustomerInfo1.setName(insurered);
//		reqCustomerInfo1.setSex("1");
//		reqCustomerInfo1.setCertType("01");
//		reqCustomerInfo1.setCertNo(insureredCertNo);
//		reqCustomerInfos.add(reqCustomerInfo1);
//		
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo2 = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		reqCustomerInfo2.setPersonType("0");
//		reqCustomerInfo2.setPersonClass("0");
//		reqCustomerInfo2.setCustomerType("1");
//		reqCustomerInfo2.setVehicleRelation("1");
//		reqCustomerInfo2.setName(owner);
//		reqCustomerInfo2.setSex("1");
//		reqCustomerInfo2.setCertType("01");
//		reqCustomerInfo2.setCertNo(ownerCertNo);
//		reqCustomerInfos.add(reqCustomerInfo2);
//		
//		
//		reqQuotePriceInfo.setTciFlag("1");
//		reqQuotePriceInfo.setTciAnswer("");
//		reqQuotePriceInfo.setVciAnswer("");
//		
//		String tciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(vehicle.getTciLastEndDate())?new Date():DateHelper.parseDate(vehicle.getTciLastEndDate()), 1));
//		String vciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(vehicle.getVciLastEndDate())?new Date():DateHelper.parseDate(vehicle.getVciLastEndDate()), 1));
//		reqQuotePriceInfo.setTciStartDate(tciStartDate);
//		reqQuotePriceInfo.setVciStartDate(vciStartDate);
//		
//		reqQuotePriceInfo.setTravelTaxFlag("Y");
//		reqQuotePriceInfo.setAtinCoverages(reqCoverageInfos);
//		reqQuotePriceInfo.setAtinCustomers(reqCustomerInfos);
//		AtinQuotation atinQuotation = new AtinQuotation();
//		
//		final ResQuotePriceInfo rq = (ResQuotePriceInfo) WxUtil.invokeService("AitpQuotePriceBs", "beginQuotePrice", reqQuotePriceInfo,null, "ZHDX", "TEST");
//		
//		ReflectUtil.copyProperties(atinQuotation, reqQuotePriceInfo,SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(atinQuotation, rq,SPFeature.DeeplyCopy,SPFeature.IgnoreEmptyStringValue);
//		List<AtinQttnCoverage> wxCoverages = new ArrayList<AtinQttnCoverage>();
//		ResQuotePriceInfo resQuotePriceInfo = rq;
////		ReflectUtil.copyProperties(wxCoverages, resQuotePriceInfo.getAtinCoverages(),SPFeature.DeeplyCopy);
//		
//		for(int i=0;i<resQuotePriceInfo.getAtinCoverages().size();i++){
//			AtinQttnCoverage wxQttnCoverage = new AtinQttnCoverage();
//			ReflectUtil.copyProperties(wxQttnCoverage, resQuotePriceInfo.getAtinCoverages().get(i),SPFeature.DeeplyCopy);
//			wxCoverages.add(wxQttnCoverage);
//		}
//		AllInfoOfPrice allInfoOfPrice = new AllInfoOfPrice();
//		ReflectUtil.copyProperties(allInfoOfPrice, reqQuotePriceInfo,SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(allInfoOfPrice, (ResQuotePriceInfo) rq,SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(allInfoOfPrice,reqVehicleInfo);
//		atinQuotation.setOpenId(openId);
//		atinQuotation.setRead("N");
//		ContainerFactory.getSession().store(atinQuotation);
//		if("I00004".equals(allInfoOfPrice.getInsuCom())){
//			allInfoOfPrice.setInsurComName("富德车险");
//		}else if("I00002".equals(allInfoOfPrice.getInsuCom())){
//			allInfoOfPrice.setInsurComName("英大车险");
//		}else if("I00003".equals(allInfoOfPrice.getInsuCom())){
//			allInfoOfPrice.setInsurComName("国寿车险");
//		}else if("I00001".equals(allInfoOfPrice.getInsuCom())){
//			allInfoOfPrice.setInsurComName("太保车险");
//		}
//		DbRvo res = ContainerFactory.getSession().query(new DbCvo<AtinQuotation>(AtinQuotation.class){
//			private static final long serialVersionUID = -2291709702597738740L;
//
//			@Override
//			public void initRules(AtinQuotation v) {
////				setSearch(true);
//				//设置查询条件
//				addRule(v.getQuoteNo(), WhereRuleOper.cn,rq.getQuoteNo());
//			}
//		});
//			List<AtinQuotation> atinQuotations = res.obtainBeans();
//		allInfoOfPrice.setId(atinQuotations.get(0).getId());
//		return allInfoOfPrice; 
//	}
//	
//}
//	