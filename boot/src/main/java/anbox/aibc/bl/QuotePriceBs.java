package anbox.aibc.bl;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.quotation.QuotationExtend;
import anbox.aibc.appBeans.quotation.QuotePriceCvo;
import anbox.aibc.appBeans.quotation.QuotePriceIntegralCvo;
import anbox.aibc.appBeans.quotation.QuotePriceRvo;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.quotation.AtinQuotation;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCustomerInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqQuotePriceInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqVehicleInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.reflect.ReflectUtil;
import cn.remex.reflect.ReflectUtil.SPFeature;
import cn.remex.util.CollectionUtils;

/**精确报价
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class QuotePriceBs implements AiwbConsts{

	@BsAnnotation(bsCvoBodyClass=QuotePriceCvo.class,bsRvoBodyClass=QuotePriceRvo.class,
			bsCvoExtendClass=QuotationExtend.class,bsRvoExtendClass=QuotationExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		final String uid =AiwbUtils.getSessionMemberId();

		QuotePriceCvo qpCvo = bsCvo.getBody();
		String flowNo = qpCvo.getFlowNo();
		ReqQuotePriceInfo reqQuotePriceInfo = new ReqQuotePriceInfo();
		ReflectUtil.copyProperties(reqQuotePriceInfo, qpCvo,SPFeature.DeeplyCopy);
		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
		AiwbUtils.obtainDefaultVehicleInfo(reqVehicleInfo);
		ReflectUtil.copyProperties(reqVehicleInfo, qpCvo.getVehicleInfo());
//		if(!PackageType.Optional.equals(qpCvo.getPackageType())){
//			List<ReqCoverageInfo> cvgs = WxUtil.obtainCoverages(qpCvo.getPackageType(), qpCvo);
//			reqQuotePriceInfo.setAtinCoverages(cvgs);
//		}
		
/*		
		reqVehicleInfo.setTransferFlag("0");//过户车标志,0-非过户车,1-过户车
		reqVehicleInfo.setLicenseColor("01");//号牌底色,01-蓝,02-黑,03-白,04-黄,05-白蓝,99-其他
		reqVehicleInfo.setLicenseType("02");//号牌类型,01-大型汽车,02-小型汽车,(03,04,05,06)-(使馆,领馆,境外,外籍),
		//(07-14)-各类摩托拖拉机等农用车,15-挂车,16-教练车,17-试验车,(20-22)-各种临时,24/25-警摩/大型警车,详见码表
		reqVehicleInfo.setRunArea("02");//行驶区域,01-出入境,02-中国境内,03-省内,04-场内,05-固定路线
		reqVehicleInfo.setSpecialModelFlag("0");//古老特异车标志,0-否,1-是
//		reqVehicleInfo.setNewVehicleFlag("0");//新车标志,0-否,1-是
		reqVehicleInfo.setEcdemicVehicleFlag("0");//外地车标志,0-本地,1-外地
		//车辆种类,A-客车,H-货车,G-挂车，A0-客车，A02—6-9座，A03—10-19座，A04—20-35座，A05—>=36，
		reqVehicleInfo.setVehicleKind("A0");
		
		*//**使用性质,8A-家庭自用,8B-企业非营业,8C-党政机关/企事业,8D-非营业个人,9A-营业出租租赁,
		 * 9B-营业城市公交,9C-营业公路客运,9D-营业货运,9E-营业旅游
		 *//*
		reqVehicleInfo.setUseType("8A");
		*//**车辆类型代码,B开头/G开头—重/中/轻型普通/集装/厢/罐式挂车,D11/D12-无/有轨电车,H开头—各类货车,
		 * M及以下开头-各类摩托车/农用车/作业车,K开头-各类客车；K33-轿车,详见码表1.35
		 *//*
		reqVehicleInfo.setVehicleType("K33");
		//证件类型,01-身份证,02-户口本,03-护照,04-军官证,05-驾照,06-返乡证,10组织机构代码证
		reqVehicleInfo.setOwnerCertType("01");
		
		*//**行驶里程,20000—小于30000公里(北京),60000—>=30000公里(北京),20000—小于30000公里(非北京),
		 * 40000—30000-50000(非北京),60000—>50000(非北京),
		 *//*
		reqVehicleInfo.setRunMile("20000");
		reqVehicleInfo.setTotalRunMile("15000");//总行驶里程,无编码
*/		
		
		
		
		reqQuotePriceInfo.setAgntComTransNo("1409458030019");//行销机构报价单号
		
		
		//完税状态,0-正常缴税,1-拒绝纳税,2-已缴纳(完税),3-免税/不征,4-补税并纳税,5-缓税,6-欠税
		reqQuotePriceInfo.setTaxStatus("0");
		
		//燃料种类,A-汽油,B-柴油,C-电,D-混合油,E-天然气,F-液化石油气,L-甲醇,M-乙醇,N-太阳能,O-混合动力,Z-其他,0-燃油(非北京)
		if("110000".equals(reqQuotePriceInfo.getCityCode())){
			reqQuotePriceInfo.setFuelType("A");
		}else{
			reqQuotePriceInfo.setFuelType("0");
		}
		//交强险投保标志
		reqQuotePriceInfo.setTciFlag(null != CollectionUtils.listToMap(qpCvo.getAtinCoverages(), "coverageCode").get("BZ") ? "1" : "0");
		//TODO 是否需要判断是否投保交强险,若不投交强险,该标志是否不需要置值
		reqQuotePriceInfo.setTravelTaxFlag(TravelTaxFlag_Y);//设置是否缴纳车船税标志,依附于交强险
		
		reqQuotePriceInfo.setTciAnswer("");//交强险过户车问题答案
		reqQuotePriceInfo.setVciAnswer("");//商业险过户车问题答案
		List<ReqCustomerInfo> reqCustomerInfos = new ArrayList<ReqCustomerInfo>();
		
		for(int i=0;i<qpCvo.getAtinCustomers().size();i++){
			ReqCustomerInfo reqCustomerInfo = new ReqCustomerInfo();
			ReflectUtil.copyProperties(reqCustomerInfo, qpCvo.getAtinCustomers().get(i), SPFeature.DeeplyCopy);
			reqCustomerInfo.setPersonClass("0");//关系人类别，0-自然人，1-机关，2-企业
			reqCustomerInfo.setCustomerType("1");//客户类型，1-个人，2-单位客户
			reqCustomerInfo.setVehicleRelation("1");//车辆所有关系，1-所有人，2-使用者，3-管理人，9-其他
			String cn = reqCustomerInfo.getCertNo();
			//18位身份证号的倒数第二位、15位身份证号的倒数第一位,奇数为男,偶数为女。"2"为女,"1"为男
			reqCustomerInfo.setSex((cn.length()==18?Integer.parseInt(cn.substring(16, 17)):(cn.length()==15?Integer.parseInt(cn.substring(15)):1))%2==0?"2":"1");
			reqCustomerInfo.setCertType("01");//证件类型,01-身份证,02-户口本,03-护照,04-军官证,05-驾照,06-返乡证,10组织机构代码证
			reqCustomerInfos.add(reqCustomerInfo);
		}
		
		reqQuotePriceInfo.setAtinCustomers(reqCustomerInfos);
		reqQuotePriceInfo.setVehicleInfo(reqVehicleInfo);
		ResQuotePriceInfo resQuotePriceInfo;
		try {
			resQuotePriceInfo = (ResQuotePriceInfo) AiwbUtils.invokeService("AitpQuotePriceBs", "beginQuotePrice", reqQuotePriceInfo, null, "ZHDX",flowNo);
		} catch (Exception e) {
			bsRvo.setExtend(new QuotationExtend(false, AiwbUtils.transErrorMsg(e.getMessage())));
			return bsRvo;
		}
		
		AtinQuotation atinQuotation = new AtinQuotation();
		atinQuotation.setDisplayStatus(DisplayStatus_Y);//默认在客户信息列表中显示，客户删除后不显示
		ReflectUtil.copyProperties(atinQuotation, reqQuotePriceInfo, SPFeature.DeeplyCopy,SPFeature.IgnoreEmptyStringValue);
		ReflectUtil.copyProperties(atinQuotation, reqQuotePriceInfo.getVehicleInfo());
		ReflectUtil.copyProperties(atinQuotation, resQuotePriceInfo, SPFeature.DeeplyCopy,SPFeature.IgnoreEmptyStringValue);
		atinQuotation.setAitpQuotationNo(resQuotePriceInfo.getQuoteNo());
		//设置交强和商业的保单止期
		atinQuotation.setTciEndDate(AiwbUtils.obtainEndDate(atinQuotation.getTciStartDate()));
		atinQuotation.setVciEndDate(AiwbUtils.obtainEndDate(atinQuotation.getVciStartDate()));
		
		atinQuotation.setOpenId(uid);//设置用户id
		
		//登录用户设置，若登录则保存/更新客户车辆信息 TODO
		atinQuotation.setMember(AiwbUtils.getLoginedMember());
		
		atinQuotation.setQuotationStatus(QuotationStatus.QSNI.toString());//报价单初始状态
		ContainerFactory.getSession().store(atinQuotation);
		
		QuotePriceRvo qpRvo = new QuotePriceRvo();
		ReflectUtil.copyProperties(qpRvo, resQuotePriceInfo, SPFeature.DeeplyCopy);
		qpRvo.setQuoteNo(atinQuotation.getId());
		
//		//询价获取积分
//		Member m = AiwbUtils.getLoginedMember();
//		if(null != m){
//			AncarConfig.saveIntegral(m,IntegralWay.QuotePrice,atinQuotation.getLicenseNo()+"询价的奖励");//要求赠送
//		}
		
		bsRvo.setBody(qpRvo);
		bsRvo.setExtend(new QuotationExtend(true, "OK"));
		return bsRvo;
	}
	
	//询价积分
	@BsAnnotation(bsCvoBodyClass=QuotePriceIntegralCvo.class,bsRvoExtendClass=QuotationExtend.class)
	public BsRvo execQuotePriceIntegral(BsCvo bsCvo, BsRvo bsRvo) {
		QuotePriceIntegralCvo qpiCvo = bsCvo.getBody();
		//询价获取积分
		Member m = AiwbUtils.getLoginedMember();
		if(null != m){
			AncarConfig.saveIntegral(m,IntegralWay.QuotePrice,qpiCvo.getLicenseNo()+"询价的奖励");//要求赠送
		}
		bsRvo.setExtend(new QuotationExtend(true, "OK"));
		return bsRvo;
	}
}
