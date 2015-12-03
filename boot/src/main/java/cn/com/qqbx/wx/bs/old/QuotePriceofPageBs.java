package cn.com.qqbx.wx.bs.old;
//package cn.com.qqbx.wx.bs;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCoverageInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqQuotePriceInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
//import cn.com.qqbx.wx.appbean.AllInfoOfPrice;
//import cn.com.qqbx.wx.appbean.ResultBean;
//import cn.com.qqbx.wx.appbean.RiskInfo;
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
//public class QuotePriceofPageBs implements Bs {
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
//		String noDutyInfoFlag;
//		final String licenseNo; 
//		final String engineNo; 
//		final String frameNo; 
//		String owner;
//		String ownerCertNo;
//		String insurer;
//		String insurerCertNo;
//		String insurered;
//		String insureredCertNo;
//		String openId; 
//		
//		RiskInfo riskInfo = bsCvo.getBody(RiskInfo.class);
//		licenseNo = riskInfo.getLicenseNo();
//		engineNo = riskInfo.getEngineNo();
//		frameNo = riskInfo.getFrameNo();
//		openId = riskInfo.getOpenId();
//		owner = riskInfo.getOwner();
//		ownerCertNo = riskInfo.getOwnerCertNo();
//		insurer = riskInfo.getInsurer();
//		insurerCertNo = riskInfo.getInsurerCertNo();
//		insurered = riskInfo.getInsurered();
//		insureredCertNo	= riskInfo.getInsureredCertNo();
//				
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
//		noDutyInfoFlag = riskInfo.getNoDutyInfoFlag();
//		DbRvo res = ContainerFactory.getSession().query(new DbCvo<Vehicle>(Vehicle.class){
//			private static final long serialVersionUID = -2291709702597738740L;
//
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
//			
//		
//		
//		
//		if("1".equals(trafficInsuInfo)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("TCI");
//			reqCoverageInfo.setCoverageCode("BZ");
//			reqCoverageInfo.setCoverageName("交强险");
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(carLossInfo))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("A");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("机动车损险");
//			reqCoverageInfo.setAmount(String.valueOf(vehicles.get(0).getPurchasePrice()));
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0").equals(thirdInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("B");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("第三者责任险");
//			reqCoverageInfo.setAmount(thirdInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(motorInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("G1");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("机动车盗抢险");
//			reqCoverageInfo.setAmount(String.valueOf(vehicles.get(0).getActualValue()));
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(passengerDriverInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("D3");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("车上人员责任险(司机)");
//			reqCoverageInfo.setUnitAmount(passengerDriverInsurance);
//			reqCoverageInfo.setQuantity("1");
//			reqCoverageInfo.setAmount(passengerDriverInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(passengerInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("D4");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("车上人员责任险(乘客)");
//			reqCoverageInfo.setUnitAmount(passengerInsurance);
//			reqCoverageInfo.setQuantity("4");
//			reqCoverageInfo.setAmount(String.valueOf(Integer.parseInt(passengerInsurance)*4));
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0".equals(glassInsurance))){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("F");
//			reqCoverageInfo.setModeCode("1");
//			reqCoverageInfo.setCoverageName("玻璃单独破碎险");
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(scratchesInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("L");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("车身划痕损失险");
//			reqCoverageInfo.setAmount(scratchesInsurance);
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if(!("0").equals(burnSelfInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("Z");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("自燃损失险");
//			reqCoverageInfo.setAmount(String.valueOf(vehicles.get(0).getPurchasePrice()));
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(enginInsurance)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("X1");
//			reqCoverageInfo.setDeductibleFlag("Y");
//			reqCoverageInfo.setCoverageName("发动机特别损失险");
//			reqCoverageInfos.add(reqCoverageInfo);
//		}if("1".equals(noDutyInfoFlag)){
//			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//			reqCoverageInfo.setPrdtKindCode("VCI");
//			reqCoverageInfo.setCoverageCode("M");
//			reqCoverageInfo.setCoverageName("不计免赔险");
//			reqCoverageInfos.add(reqCoverageInfo);
//	}
//		
//		try{
//			AllInfoOfPrice resQuotePriceInfo = quotePriceInfo(riskInfo.getInsuCom(),riskInfo.getCityCode(),reqCoverageInfos,vehicles.get(0),owner,ownerCertNo,insurer,insurerCertNo,insurered,insureredCertNo,openId);
//			if("1".equals(noDutyInfoFlag)){
//				resQuotePriceInfo.setIsNoDutyInfoFlag("1");
//			}else{
//				resQuotePriceInfo.setIsNoDutyInfoFlag("0");
//			}
//			
//			bsRvo.setBody(resQuotePriceInfo);
//			bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		}catch (Exception e) {
//			e.printStackTrace();
//			bsRvo.setExtend(new Extend(false, e.toString()));
//		}
//		return bsRvo;
//	}
//
//
//	
//		private static AllInfoOfPrice quotePriceInfo(String insuCom, String cityCode,List<ReqCoverageInfo> reqCoverageInfos,Vehicle vehicle, String owner, String ownerCertNo, String insurer, String insurerCertNo, String insurered, String insureredCertNo,String openId){
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
////		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
////		reqPolicyEndDate.setCityCode(cityCode);
////		reqPolicyEndDate.setInsuCom(insuCom);
////		cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo vehicleInfo = new cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo();
////		ReflectUtil.copyProperties(vehicleInfo, reqVehicleInfo);
////		reqPolicyEndDate.setVehicleInfo(vehicleInfo );
////		ResPolicyEndDate resPolicyEndDate = (ResPolicyEndDate) WxUtil.invokeService("AitpQueryPolicyEndDateBs", "queryLastContEndDate", reqPolicyEndDate, "ZHDX", "TEST");
////
////		
////		
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
//		
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
//
//
//}
//	