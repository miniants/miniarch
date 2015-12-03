package cn.com.qqbx.wx.bs.old;
//package cn.com.qqbx.wx.bs;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.com.qqbx.aitp.AitpUtils;
//import cn.com.qqbx.aitp.aiws.xmlbeans.Request;
//import cn.com.qqbx.aitp.aiws.xmlbeans.RequestBody;
//import cn.com.qqbx.aitp.aiws.xmlbeans.Response;
//import cn.com.qqbx.aitp.aiws.xmlbeans.ResponseBody;
//import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
//import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
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
//import cn.com.qqbx.wx.appbean.RiskInfo;
//import cn.com.qqbx.wx.model.wxquotation.AtinQttnCoverage;
//import cn.com.qqbx.wx.model.wxquotation.AtinQuotation;
//import cn.com.qqbx.wx.text.HttpHelper;
//import cn.com.qqbx.wx.utils.WxUtil;
//import cn.remex.bs.Bs;
//import cn.remex.bs.BsCvo;
//import cn.remex.bs.BsRvo;
//import cn.remex.db.ContainerFactory;
//import cn.remex.reflect.ReflectUtil;
//import cn.remex.reflect.ReflectUtil.SPFeature;
//import cn.remex.util.FileHelper;
//
//public class QuotePriceWxBs implements Bs {
//	
//	@Override
//	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		
//		
//		List<ReqCoverageInfo> reqCoverageInfos = new ArrayList<ReqCoverageInfo>();
//		
//		String trafficInsuInfo;
//		String carLossInfo;
//		String thirdInsurance;
//		String motorInsurance;
//		String passengerDriverInsurance;
//		String passengerInsurance;
//		String glassInsurance;
//		String scratchesInsurance;
//		String burnSelfInsurance;
//		String enginInsurance;
//		String licenseNo; 
//		String engineNo; 
//		String frameNo; 
//		String openId; 
//		
//		RiskInfo riskInfo = bsCvo.getBody(RiskInfo.class);
//		licenseNo = riskInfo.getLicenseNo();
//		engineNo = riskInfo.getEngineNo();
//		frameNo = riskInfo.getFrameNo();
//		openId = riskInfo.getOpenId();
//		
//		trafficInsuInfo = riskInfo.getTrafficInsuInfo();
//		carLossInfo = riskInfo.getCarLossInfo();
//		thirdInsurance = riskInfo.getThirdInsurance();
//		motorInsurance = riskInfo.getMotorInsurance();
//		passengerDriverInsurance = riskInfo.getPassengerDriverInsurance();
//		passengerInsurance = riskInfo.getPassengerInsurance();
//		glassInsurance = riskInfo.getGlassInsurance();
//		scratchesInsurance = riskInfo.getScratchesInsurance();
//		burnSelfInsurance = riskInfo.getBurnSelfInsurance();
//		enginInsurance = riskInfo.getEnginInsurance();
////		ResVehicleModelData vehicle = vehicleModelData(licenseNo,engineNo,frameNo);
//		ResVehicleModelData vehicle = vehicleModelData("京JX5595","66140388","LSGTC52U96Y079507");
////		ResPolicyEndDate resPolicyEndDate = policyEndDate(vehicle);
//		ResActualValue resActualValue = actualValue(vehicle);
//		if("1".equals(trafficInsuInfo)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("TCI");
//			reqCoverageInfo.setCoverageCode("BZ");
//		}if(!("0".equals(carLossInfo))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("A");
//			reqCoverageInfo.setDeductibleFlag("Y");
////			reqCoverageInfo.setAmount("新车购置价");
//			reqCoverageInfo.setAmount(vehicle.getVehicleInfos().get(0).getPurchasePrice());
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0").equals(thirdInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("B");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setAmount(thirdInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(motorInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("G1");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setAmount("车辆实际价值");
//			reqCoverageInfo.setAmount(resActualValue.getActualValue());
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(passengerDriverInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("D3");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setUnitAmount(passengerDriverInsurance);
//			reqCoverageInfo.setQuantity("1");
//			reqCoverageInfo.setAmount(passengerDriverInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(passengerInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("D4");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setUnitAmount(passengerInsurance);
//			reqCoverageInfo.setQuantity("4");
//			reqCoverageInfo.setAmount(passengerInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(glassInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("F");
//			reqCoverageInfo.setModeCode("1");
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(scratchesInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("L");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setAmount(scratchesInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0").equals(burnSelfInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("Z");
//			reqCoverageInfo.setDeductibleFlag("Y");
////			reqCoverageInfo.setAmount("车辆实际价值");
//			reqCoverageInfo.setAmount(resActualValue.getActualValue());
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(enginInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("X1");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfos.add(reqCoverageInfo);
//		}
//		
////		ResQuotePriceInfo resQuotePriceInfo = quotePriceInfo(reqCoverageInfos);
//		AllInfoOfPrice resQuotePriceInfo = quotePriceInfo(reqCoverageInfos,vehicle);
//		bsRvo.setBody(resQuotePriceInfo);
//		bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		return bsRvo;
//	}
//
//	public static ResVehicleModelData vehicleModelData(String licenseNo,String engineNo,String frameNo){
//		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
//		reqVehicleInfo.setInsuCom("I00004");//
//		reqVehicleInfo.setVehicleType("K33");
//		reqVehicleInfo.setVehicleKind("A0");
//		reqVehicleInfo.setNewVehicleFlag("0");
//		reqVehicleInfo.setEngineNo(engineNo);                      
//		reqVehicleInfo.setFrameNo(frameNo);
//		reqVehicleInfo.setEcdemicVehicleFlag("0");
//		reqVehicleInfo.setOwner("兰成浩");
//		reqVehicleInfo.setLicenseNo(licenseNo);
//		reqVehicleInfo.setLicenseColor("01");
//		reqVehicleInfo.setLicenseType("02");
//		reqVehicleInfo.setUseType("8A");
//		reqVehicleInfo.setCityCode("110000");
//		Request request = WxUtil.setReqVehicleInfoXml("beginCarQuery", reqVehicleInfo,"ZHDX","001");
//		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqVehicleInfo.class,request);
//		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQueryVehicleInfoBs";
////		String responseXml = HttpHelper.sendXml(url,requestXml );
//		String responseXml = FileHelper.getFileContent("G:\\a.xml"); 
//		
//		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResVehicleModelData.class, responseXml);
//		return (ResVehicleModelData) response.getBody().getBodyData(); 
//	}
//	
//	public static ResPolicyEndDate policyEndDate(ResVehicleModelData vehicle){
//		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
//		cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo reqVehicleInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo();
//		ReqCustomerInfo reqCustomerInfo = new ReqCustomerInfo();
//		List <ReqCustomerInfo> reqCustomerInfos = new ArrayList<ReqCustomerInfo>();
//		ReflectUtil.copyProperties(reqVehicleInfo,vehicle.getVehicleInfos().get(0));
//		reqVehicleInfo.setTransferFlag("0");
//		reqVehicleInfo.setRunArea("02");
//		reqVehicleInfo.setSpecialModelFlag("0");
//		reqPolicyEndDate.setCityCode("110000");
//		reqPolicyEndDate.setInsuCom("I00004");
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
//		Request request = WxUtil.setReqVehicleInfoXml("queryLastContEndDate", reqPolicyEndDate,"ZHDX","001");
//		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqPolicyEndDate.class,request);
//		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQueryPolicyEndDateBs";
//		String responseXml = HttpHelper.sendXml(url,requestXml );
//		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResPolicyEndDate.class, responseXml);
//		return (ResPolicyEndDate) response.getBody().getBodyData(); 
//	}
//	
//	private static ResActualValue actualValue(ResVehicleModelData vehicle){
//		ReqActualValue reqActualValue = new ReqActualValue();
//		reqActualValue.setCityCode("110000");
//		reqActualValue.setInsuCom("I00004");
////		reqActualValue.setPurchasePrice("85900.0");
//		reqActualValue.setPurchasePrice(vehicle.getVehicleInfos().get(0).getPurchasePrice());
////		reqActualValue.setEnrollDate("2006-08-16");
//		reqActualValue.setEnrollDate(vehicle.getVehicleInfos().get(0).getEnrollDate());
//		reqActualValue.setVciStartDate("2014-11-21");
//		reqActualValue.setUseType("8A");
//		reqActualValue.setVehicleKind("A0");
//		reqActualValue.setSeatCount(vehicle.getVehicleInfos().get(0).getSeatCount());
//		reqActualValue.setVehicleTonnage("0.0");
//		
//		Request request = WxUtil.setReqVehicleInfoXml("queryCarRealPrice", reqActualValue,"ZHDX","001");
//		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqActualValue.class,request);
////		String responseXml = HttpHelper.sendXml("http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpActualValueBs", requestXml );
//		String responseXml = FileHelper.getFileContent("G:\\d.xml"); 
//		Response response =  AitpUtils.unmarshall(Response.class,
//				ResponseBody.class,ResActualValue.class, responseXml);
//		return (ResActualValue) response.getBody().getBodyData(); 
//	}
//	
////	private static ResQuotePriceInfo quotePriceInfo(List<ReqCoverageInfo> reqCoverageInfos){
//		private static AllInfoOfPrice quotePriceInfo(List<ReqCoverageInfo> reqCoverageInfos,ResVehicleModelData vehicle){
//		ReqQuotePriceInfo reqQuotePriceInfo = new ReqQuotePriceInfo();
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo reqVehicleInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo();
//		List<cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo> reqCustomerInfos = new ArrayList<cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo>(); //客户信息
//		
//		reqQuotePriceInfo.setCityCode("110000");
//		reqQuotePriceInfo.setInsuCom("I00004");
//		reqQuotePriceInfo.setTaxStatus("0");
//		reqQuotePriceInfo.setFuelType("A");
//		
//		ReflectUtil.copyProperties(reqVehicleInfo, vehicle.getVehicleInfos().get(0));
//		reqVehicleInfo.setTransferFlag("0");
//		reqVehicleInfo.setRunArea("02");
//		reqVehicleInfo.setSpecialModelFlag("0");
//		reqVehicleInfo.setNewVehicleFlag("0");
//		reqVehicleInfo.setEcdemicVehicleFlag("0");
//		reqVehicleInfo.setVehicleKind("A0");
//		reqVehicleInfo.setUseType("8A");
//		reqVehicleInfo.setVehicleType("K33");
//		reqQuotePriceInfo.setVehicleInfo(reqVehicleInfo);
//		
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		reqCustomerInfo.setPersonType("2");
//		reqCustomerInfo.setPersonClass("0");
//		reqCustomerInfo.setCustomerType("1");
//		reqCustomerInfo.setVehicleRelation("1");
//		reqCustomerInfo.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo.setSex("1");
//		reqCustomerInfo.setCertType("01");
//		reqCustomerInfo.setCertNo("120224199103136411");
//		reqCustomerInfos.add(reqCustomerInfo);
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo1 = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		
//		reqCustomerInfo1.setPersonType("1");
//		reqCustomerInfo1.setPersonClass("0");
//		reqCustomerInfo1.setCustomerType("1");
//		reqCustomerInfo1.setVehicleRelation("1");
//		reqCustomerInfo1.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo1.setSex("1");
//		reqCustomerInfo1.setCertType("01");
//		reqCustomerInfo1.setCertNo("120224199103136411");
//		reqCustomerInfos.add(reqCustomerInfo1);
//		
//		cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo2 = new cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
//		reqCustomerInfo2.setPersonType("0");
//		reqCustomerInfo2.setPersonClass("0");
//		reqCustomerInfo2.setCustomerType("1");
//		reqCustomerInfo2.setVehicleRelation("1");
//		reqCustomerInfo2.setName(reqVehicleInfo.getOwner());
//		reqCustomerInfo2.setSex("1");
//		reqCustomerInfo2.setCertType("01");
//		reqCustomerInfo2.setCertNo("120224199103136411");
//		reqCustomerInfos.add(reqCustomerInfo2);
//		
//		
//		reqQuotePriceInfo.setTciFlag("1");
//		reqQuotePriceInfo.setTciAnswer("");
//		reqQuotePriceInfo.setVciAnswer("");
//		reqQuotePriceInfo.setTciStartDate("2014-11-26");
//		reqQuotePriceInfo.setVciStartDate("2014-11-26");
//		
//		reqQuotePriceInfo.setTravelTaxFlag("Y");
//		reqQuotePriceInfo.setAtinCoverages(reqCoverageInfos);
//		reqQuotePriceInfo.setAtinCustomers(reqCustomerInfos);
//		AtinQuotation atinQuotation = new AtinQuotation();
//		
////		ResQuotePriceInfo rq = (ResQuotePriceInfo) WxUtil.invokeService("AitpQuotePriceBs", "beginQuotePrice", reqQuotePriceInfo, "ZHDX","001");
//		
//		
//		Request request = WxUtil.setReqVehicleInfoXml("beginQuotePrice", reqQuotePriceInfo,"ZHDX","001");
////		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqQuotePriceInfo.class,request);
//		String requestXml = FileHelper.getFileContent("G:\\b.xml"); 
//		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQuotePriceBs";
////		String responseXml = HttpHelper.sendXml(url,requestXml );
//		String responseXml = FileHelper.getFileContent("G:\\aa.xml");
//		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResQuotePriceInfo.class, responseXml);
//		ResQuotePriceInfo rq = (ResQuotePriceInfo)response.getBody().getBodyData();
//		ReflectUtil.copyProperties(atinQuotation, reqQuotePriceInfo,SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(atinQuotation, (ResQuotePriceInfo) response.getBody().getBodyData(),SPFeature.DeeplyCopy);
//		List<AtinQttnCoverage> wxCoverages = new ArrayList<AtinQttnCoverage>();
//		ResQuotePriceInfo resQuotePriceInfo = (ResQuotePriceInfo) response.getBody().getBodyData();
////		ReflectUtil.copyProperties(wxCoverages, resQuotePriceInfo.getAtinCoverages(),SPFeature.DeeplyCopy);
//		
//		for(int i=0;i<resQuotePriceInfo.getAtinCoverages().size();i++){
//			AtinQttnCoverage wxQttnCoverage = new AtinQttnCoverage();
//			ReflectUtil.copyProperties(wxQttnCoverage, resQuotePriceInfo.getAtinCoverages().get(i),SPFeature.DeeplyCopy);
//			wxCoverages.add(wxQttnCoverage);
//		}
//		AllInfoOfPrice allInfoOfPrice = new AllInfoOfPrice();
//		ReflectUtil.copyProperties(allInfoOfPrice, reqQuotePriceInfo,SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(allInfoOfPrice, (ResQuotePriceInfo) response.getBody().getBodyData(),SPFeature.DeeplyCopy);
//		ReflectUtil.copyProperties(allInfoOfPrice,reqVehicleInfo);
//		ContainerFactory.getSession().store(atinQuotation);
//		return allInfoOfPrice; 
//	}
//
//
//
//}
//	