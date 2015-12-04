package zbh.wx.fsmaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zbh.com.qqbx.aitp.AitpUtils;
import zbh.com.qqbx.aitp.aiws.xmlbeans.Request;
import zbh.com.qqbx.aitp.aiws.xmlbeans.RequestBody;
import zbh.com.qqbx.aitp.aiws.xmlbeans.Response;
import zbh.com.qqbx.aitp.aiws.xmlbeans.ResponseBody;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqCustomerInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqPolicyEndDate;
import zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ResPolicyEndDate;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCoverageInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqQuotePriceInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.vehicle.ResVehicleModelData;
import zbh.wx.text.HttpHelper;
import zbh.wx.utils.WxUtil;
import zbh.wx.xmlbeans.Article;
import zbh.wx.xmlbeans.CustomMessage;
import zbh.wx.xmlbeans.News;
import zbh.remex.fsm.FsmAction;
import zbh.remex.fsm.FsmConstant.FsmFlowType;
import zbh.remex.fsm.FsmResult;
import zbh.remex.fsm.model.FsmState;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.sam.SamConstant.SamMessageType;
import zbh.remex.util.FileHelper;
import zbh.remex.util.JsonHelper;

public class BeginQuotePriceAction extends FsmAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5095989983271737959L;
//	static final String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=";
	
	@Override
	public FsmResult execute() {
		
		String msgInfo = String.valueOf(samMessage.getMessage());
		Map<String, Object> flowContextParams = JsonHelper.toJavaObject(fsmContext.getCurFlowContext().getContextParams(), HashMap.class);
		List<ReqCoverageInfo> reqCoverageInfos = new ArrayList<ReqCoverageInfo>();
		
		ResVehicleModelData vehicle = vehicleModelData(flowContextParams.get("licenseNo").toString(),flowContextParams.get("engineNo").toString(),flowContextParams.get("frameNo").toString());
		ResPolicyEndDate resPolicyEndDate = policyEndDate(vehicle);
		ResActualValue resActualValue = actualValue(vehicle);
		
		if("A".equals(msgInfo)){
			ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
			reqCoverageInfo.setPrdtKindCode("TCI");
			reqCoverageInfo.setCoverageCode("BZ");
			reqCoverageInfos.add(reqCoverageInfo);
//			quotePriceInfo(reqCoverageInfos);
			
		}else if("B".equals(msgInfo)){
			
		}else{
			String [] ps = msgInfo.split(",");
			for(String riskInfo : ps){
				ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
				switch (Integer.parseInt(riskInfo)) {
				case 1:
					reqCoverageInfo.setPrdtKindCode("TCI");
					reqCoverageInfo.setCoverageCode("BZ");
					break;
				case 2:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("A");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount(vehicle.getVehicleInfos().get(0).getPurchasePrice());
					break;
				case 31:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("B");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("100000");
					break;
				case 32:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("B");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("200000");
				case 33:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("B");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("300000");
					break;
				case 34:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("B");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("500000");
					break;
				case 35:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("B");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("1000000");
					break;
				case 4:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("G1");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount(resActualValue.getActualValue());
					break;
				case 5:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("D3");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setUnitAmount("10000");
					reqCoverageInfo.setQuantity("1");
					reqCoverageInfo.setAmount("10000");
					break;
				case 6:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("D4");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setUnitAmount("10000");
					reqCoverageInfo.setQuantity("4");
					reqCoverageInfo.setAmount("10000");
					break;
				case 71:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("F");
					reqCoverageInfo.setModeCode("1");
					break;
				case 72:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("F");
					reqCoverageInfo.setModeCode("2");
					break;
				case 81:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("L");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("2000");
					break;
				case 82:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("L");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("5000");
					break;
				case 83:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("L");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("10000");
					break;
				case 84:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("L");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount("20000");
					break;
				case 9:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("Z");
					reqCoverageInfo.setDeductibleFlag("Y");
					reqCoverageInfo.setAmount(resActualValue.getActualValue());
					break;
				case 10:
					reqCoverageInfo.setPrdtKindCode("VCI");
					reqCoverageInfo.setCoverageCode("X1");
					reqCoverageInfo.setDeductibleFlag("Y");
					break;

				default:
					break;
				}
				reqCoverageInfos.add(reqCoverageInfo);
			}
		}
		
		
		
		
		ResQuotePriceInfo resQuotePriceInfo = quotePriceInfo(reqCoverageInfos,vehicle);
		String insuCom = resQuotePriceInfo.getInsuCom();
		String premium = resQuotePriceInfo.getPremium();
		String quoteNo = resQuotePriceInfo.getQuoteNo();
		
		FsmResult fsmResult = null;
		String msg;
			List<Article> articleList = new ArrayList<Article>();
			Article article = new Article();
			CustomMessage cm = new CustomMessage();
			article.setTitle("保险公司报价信息");
			article.setPicurl("http://www.qqbx.com.zbh/wx/wx/images/test.jpg");
			articleList.add(article);
			
			Article article1 = new Article();
			msg = insuCom+"报价:总保费"+premium;
			article1.setTitle(msg);
			article1.setDescription("车险投保车险投保车险投保");
			article1.setPicurl("http://www.qqbx.com.zbh/wx/wx/images/guoshou.jpg");
			article1.setUrl("www.qqbx.com.zbh/wx/wx/page/Test.html?"+fsmContext.getUserId()+"&"+quoteNo);
			articleList.add(article1);
			cm.setMsgtype("news");
			News news = new News();
			news.setArticles(articleList);
			cm.setNews(news);
			System.out.println(JsonHelper.toJsonString(cm));
		    fsmResult = new FsmResult(SamMessageType.JsonObj,cm,fsmContext);
	
			return fsmResult;
	}
	
	public static ResVehicleModelData vehicleModelData(String licenseNo,String engineNo,String frameNo){
		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
		reqVehicleInfo.setInsuCom("I00004");//
		reqVehicleInfo.setVehicleType("K33");
		reqVehicleInfo.setVehicleKind("A0");
		reqVehicleInfo.setNewVehicleFlag("0");
		reqVehicleInfo.setEngineNo(engineNo);                      
//		reqVehicleInfo.setEngineNo("66140388");                      
		reqVehicleInfo.setFrameNo(frameNo);
//		reqVehicleInfo.setFrameNo("LSGTC52U96Y079507");
		reqVehicleInfo.setEcdemicVehicleFlag("0");
		reqVehicleInfo.setOwner("兰成浩");
		reqVehicleInfo.setLicenseNo(licenseNo);
//		reqVehicleInfo.setLicenseNo("京JX5595");
		reqVehicleInfo.setLicenseColor("01");
		reqVehicleInfo.setLicenseType("02");
		reqVehicleInfo.setUseType("8A");
		reqVehicleInfo.setCityCode("110000");
		Request request = WxUtil.setReqVehicleInfoXml("beginCarQuery", reqVehicleInfo,"ZHDX","001");
		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqVehicleInfo.class,request);
//		String requestXml = FileHelper.getFileContent("G:\\a.xml"); 
		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQueryVehicleInfoBs";
		String responseXml = HttpHelper.sendXml(url,requestXml );
		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResVehicleModelData.class, responseXml);
		return (ResVehicleModelData) response.getBody().getBodyData(); 
	}
	
	public static ResPolicyEndDate policyEndDate(ResVehicleModelData vehicle){
		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
		zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo reqVehicleInfo = new zbh.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo();
		ReqCustomerInfo reqCustomerInfo = new ReqCustomerInfo();
		List <ReqCustomerInfo> reqCustomerInfos = new ArrayList<ReqCustomerInfo>();
		ReflectUtil.copyProperties(reqVehicleInfo,vehicle.getVehicleInfos().get(0));
		reqVehicleInfo.setTransferFlag("0");
		reqVehicleInfo.setRunArea("02");
		reqVehicleInfo.setSpecialModelFlag("0");
		reqPolicyEndDate.setCityCode("110000");
		reqPolicyEndDate.setInsuCom("I00004");
		reqPolicyEndDate.setTaxStatus("0");
		reqCustomerInfo.setPersonType("2");
		reqCustomerInfo.setPersonClass("0");
		reqCustomerInfo.setCustomerType("1");
		reqCustomerInfo.setVehicleRelation("1");
		reqCustomerInfo.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo.setSex("1");
		reqCustomerInfo.setCertType("01");
		reqCustomerInfo.setCertNo(reqVehicleInfo.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo);
		ReqCustomerInfo reqCustomerInfo1 = new ReqCustomerInfo();
		
		reqCustomerInfo1.setPersonType("1");
		reqCustomerInfo1.setPersonClass("0");
		reqCustomerInfo1.setCustomerType("1");
		reqCustomerInfo1.setVehicleRelation("1");
		reqCustomerInfo1.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo1.setSex("1");
		reqCustomerInfo1.setCertType("01");
		reqCustomerInfo1.setCertNo(reqVehicleInfo.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo1);
		
		ReqCustomerInfo reqCustomerInfo2 = new ReqCustomerInfo();
		reqCustomerInfo2.setPersonType("0");
		reqCustomerInfo2.setPersonClass("0");
		reqCustomerInfo2.setCustomerType("1");
		reqCustomerInfo2.setVehicleRelation("1");
		reqCustomerInfo2.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo2.setSex("1");
		reqCustomerInfo2.setCertType("01");
		reqCustomerInfo2.setCertNo(reqVehicleInfo.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo2);
		reqPolicyEndDate.setVehicleInfo(reqVehicleInfo);
		reqPolicyEndDate.setAtinCustomers(reqCustomerInfos);
		Request request = WxUtil.setReqVehicleInfoXml("queryLastContEndDate", reqPolicyEndDate,"ZHDX","001");
		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqPolicyEndDate.class,request);
		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQueryPolicyEndDateBs";
		String responseXml = HttpHelper.sendXml(url,requestXml );
		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResPolicyEndDate.class, responseXml);
		return (ResPolicyEndDate) response.getBody().getBodyData(); 
	}
	
	private static ResActualValue actualValue(ResVehicleModelData vehicle){
		ReqActualValue reqActualValue = new ReqActualValue();
		reqActualValue.setCityCode("110000");
		reqActualValue.setInsuCom("I00004");
//		reqActualValue.setPurchasePrice("85900.0");
		reqActualValue.setPurchasePrice(vehicle.getVehicleInfos().get(0).getPurchasePrice());
//		reqActualValue.setEnrollDate("2006-08-16");
		reqActualValue.setEnrollDate(vehicle.getVehicleInfos().get(0).getEnrollDate());
		reqActualValue.setVciStartDate("2014-11-21");
		reqActualValue.setUseType("8A");
		reqActualValue.setVehicleKind("A0");
		reqActualValue.setSeatCount(vehicle.getVehicleInfos().get(0).getSeatCount());
		reqActualValue.setVehicleTonnage("0.0");
		
		Request request = WxUtil.setReqVehicleInfoXml("queryCarRealPrice", reqActualValue,"ZHDX","001");
		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqActualValue.class,request);
		String responseXml = HttpHelper.sendXml("http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpActualValueBs", requestXml );
		Response response =  AitpUtils.unmarshall(Response.class,
				ResponseBody.class,ResActualValue.class, responseXml);
		return (ResActualValue) response.getBody().getBodyData(); 
	}
	
	private static ResQuotePriceInfo quotePriceInfo(List<ReqCoverageInfo> reqCoverageInfos,ResVehicleModelData vehicle){
		ReqQuotePriceInfo reqQuotePriceInfo = new ReqQuotePriceInfo();
		zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo reqVehicleInfo = new zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo();
		List<zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo> reqCustomerInfos = new ArrayList<zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo>(); //客户信息
		
		reqQuotePriceInfo.setCityCode("110000");
		reqQuotePriceInfo.setInsuCom("I00004");
		reqQuotePriceInfo.setTaxStatus("0");
		reqQuotePriceInfo.setFuelType("A");
		
		ReflectUtil.copyProperties(reqVehicleInfo, vehicle.getVehicleInfos().get(0));
		reqVehicleInfo.setTransferFlag("0");
		reqVehicleInfo.setRunArea("02");
		reqVehicleInfo.setSpecialModelFlag("0");
		reqVehicleInfo.setNewVehicleFlag("0");
		reqVehicleInfo.setEcdemicVehicleFlag("0");
		reqVehicleInfo.setVehicleKind("A0");
		reqVehicleInfo.setUseType("8A");
		reqVehicleInfo.setVehicleType("K33");
		reqQuotePriceInfo.setVehicleInfo(reqVehicleInfo);
		
		zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo = new zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
		reqCustomerInfo.setPersonType("2");
		reqCustomerInfo.setPersonClass("0");
		reqCustomerInfo.setCustomerType("1");
		reqCustomerInfo.setVehicleRelation("1");
		reqCustomerInfo.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo.setSex("1");
		reqCustomerInfo.setCertType("01");
		reqCustomerInfo.setCertNo("120224199103136411");
		reqCustomerInfos.add(reqCustomerInfo);
		zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo1 = new zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
		
		reqCustomerInfo1.setPersonType("1");
		reqCustomerInfo1.setPersonClass("0");
		reqCustomerInfo1.setCustomerType("1");
		reqCustomerInfo1.setVehicleRelation("1");
		reqCustomerInfo1.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo1.setSex("1");
		reqCustomerInfo1.setCertType("01");
		reqCustomerInfo1.setCertNo("120224199103136411");
		reqCustomerInfos.add(reqCustomerInfo1);
		
		zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo reqCustomerInfo2 = new zbh.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo();
		reqCustomerInfo2.setPersonType("0");
		reqCustomerInfo2.setPersonClass("0");
		reqCustomerInfo2.setCustomerType("1");
		reqCustomerInfo2.setVehicleRelation("1");
		reqCustomerInfo2.setName(reqVehicleInfo.getOwner());
		reqCustomerInfo2.setSex("1");
		reqCustomerInfo2.setCertType("01");
		reqCustomerInfo2.setCertNo("120224199103136411");
		reqCustomerInfos.add(reqCustomerInfo2);
		
//		ReqCoverageInfo reqCoverageInfo = new ReqCoverageInfo();
//		reqCoverageInfo.setPrdtKindCode("TCI");
//		reqCoverageInfo.setCoverageCode("BZ");
//		reqCoverageInfos.add(reqCoverageInfo);
		
		reqQuotePriceInfo.setTciFlag("1");
		reqQuotePriceInfo.setTciAnswer("");
		reqQuotePriceInfo.setVciAnswer("");
		reqQuotePriceInfo.setTciStartDate("2014-11-26");
		reqQuotePriceInfo.setVciStartDate("2014-11-26");
		
		reqQuotePriceInfo.setTravelTaxFlag("Y");
		reqQuotePriceInfo.setAtinCoverages(reqCoverageInfos);
		reqQuotePriceInfo.setAtinCustomers(reqCustomerInfos);
		
		Request request = WxUtil.setReqVehicleInfoXml("beginQuotePrice", reqQuotePriceInfo,"ZHDX","001");
		String requestXml = AitpUtils.marshall(Request.class,RequestBody.class,ReqQuotePriceInfo.class,request);
//		String requestXml = FileHelper.getFileContent("G:\\b.xml"); 
		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQuotePriceBs";
		String responseXml = HttpHelper.sendXml(url,requestXml );
		Response response = AitpUtils.unmarshall(Response.class, ResponseBody.class, ResQuotePriceInfo.class, responseXml);
		return (ResQuotePriceInfo) response.getBody().getBodyData(); 
	}
}
