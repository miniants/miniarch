package zbh.aibc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.AncarConfig;
import zbh.aibc.appBeans.insureConfirm.InsureConfirmCvo;
import zbh.aibc.appBeans.insureConfirm.InsureConfirmExtend;
import zbh.aibc.appBeans.insureConfirm.InsureConfirmRvo;
import anbox.aibc.model.member.DeliveryInfo;
import zbh.aias.model.member.Member;
import anbox.aibc.model.order.ProductOrder;
import zbh.aias.model.order.ProductOrderDetail;
import anbox.aibc.model.policy.AtinPolicy;
import anbox.aibc.model.product.StockKeepingUnit;
import zbh.aias.model.quotation.AtinQttnCustomer;
import anbox.aibc.model.quotation.AtinQuotation;
import zbh.com.qqbx.aitp.aiws.xmlbeans.insuconfirm.ReqInsureConfirmInfo;
import zbh.com.qqbx.aitp.aiws.xmlbeans.insuconfirm.ResInsureConfirmInfo;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.reflect.ReflectUtil.SPFeature;
import zbh.remex.util.Judgment;

/**投保确认
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class InsureConfirmBs  implements AiwbConsts{
	@SuppressWarnings("unchecked")
	Map<String,String> icm = (Map<String,String>) AiwbUtils.getBean("Vehicle_SQL");
	
	@BsAnnotation(bsCvoBodyClass=InsureConfirmCvo.class,bsRvoBodyClass=InsureConfirmRvo.class,
			bsCvoExtendClass=InsureConfirmExtend.class,bsRvoExtendClass=InsureConfirmExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		Member member = AiwbUtils.getSessionMember();
//		if(null ==member){
//			InsureConfirmExtend insureConfirmExtend = new InsureConfirmExtend(false,TipsMsg_insureConfirmNeedLogin);
//			insureConfirmExtend.setLoginFlag("N");
//			bsRvo.setExtend(insureConfirmExtend);
//			return bsRvo;
//		}
		
		final Member member = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(member)){
			InsureConfirmExtend insureConfirmExtend = new InsureConfirmExtend(false,TipsMsg_insureConfirmNeedLogin);
			insureConfirmExtend.setLoginFlag("N");
			bsRvo.setExtend(insureConfirmExtend);
			return bsRvo;
		}
		//TODO 根据id和quoteNo查
		InsureConfirmCvo icCvo = bsCvo.getBody();
		String flowNo = icCvo.getFlowNo();
		String bizQuoteNo =  icCvo.getQuoteNo();
		AtinQuotation atinQuotation = ContainerFactory.getSession().queryBeanById(AtinQuotation.class,bizQuoteNo);
		if(QuotationStatus.QSYI.toString().equals(atinQuotation.getQuotationStatus())){//已投保的询价单不能再次投保
			bsRvo.setExtend(new InsureConfirmExtend(false, "已投保询价单不能重复投保！"));
			return bsRvo;
		}
		
		//-------------投保成功 存保单-----------//
		ReqInsureConfirmInfo reqInsureConfirmInfo = new ReqInsureConfirmInfo();
		ReflectUtil.copyProperties(reqInsureConfirmInfo, icCvo);
		reqInsureConfirmInfo.setQuoteNo(atinQuotation.getAitpQuotationNo());
		//reqInsureConfirmInfo.setQuoteNo(icCvo.getQuoteNo());
		//reqInsureConfirmInfo.setInsuCom("I00002");
		//reqInsureConfirmInfo.setCityCode("110000");
		
		ResInsureConfirmInfo resInsureConfirmInfo;
		try {
			resInsureConfirmInfo = (ResInsureConfirmInfo) AiwbUtils.invokeService("AitpInsureConfirmBs", "beginInsure", reqInsureConfirmInfo,null, "ZHDX",flowNo);
		} catch (Exception e) {
			String msg = AiwbUtils.transErrorMsg(e.getMessage());
			bsRvo.setExtend(new InsureConfirmExtend(false,msg));
			return bsRvo;
		}
		//TODO 存入保单表
		AtinPolicy atinPolicy = new AtinPolicy();
		
		atinPolicy.setQuoteNo(bizQuoteNo);
//		AtinQuotation atinQuotation = new AtinQuotation();
//		atinQuotation = ContainerFactory.getSession().pickUp(atinQuotation);
		ReflectUtil.copyProperties(atinPolicy, atinQuotation, SPFeature.DeeplyCopy);
		ReflectUtil.copyProperties(atinPolicy, resInsureConfirmInfo, SPFeature.DeeplyCopy);
//		atinPolicy.setAitpPolicyNo(resInsureConfirmInfo.get); TODO 平台投保返回接口需要添加平台的内部保单号字段
		atinPolicy.setPolicyStatus(PolicyStatus.PSAU.toString());
		atinPolicy.setPayStatus(PayStatus.APSNP.toString());
		atinPolicy.setAitpPolicyNo(resInsureConfirmInfo.getInnerPlcyNo());
		atinPolicy.setAtinQttnId(bizQuoteNo);//保存应用端询价单号
		ContainerFactory.getSession().store(atinPolicy);
		//-------------------------投保单 end-------------//
		
		//-------------------------将查询到的上年止期保存到客户车辆表中-------------//
		String updateSql = icm.get("EndDate").replaceAll(":tciEndDate", atinPolicy.getTciEndDate()).replaceAll(":vciEndDate", atinPolicy.getVciEndDate());
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("licenseNo", atinPolicy.getLicenseNo());
		params.put("engineNo", atinPolicy.getEngineNo());
		params.put("frameNo", atinPolicy.getFrameNo());
		params.put("openId", member.getUsername());
		ContainerFactory.getSession().executeUpdate(updateSql, params);
		
		
		//-------------------------生成并保存订单------//
		//TODO 存入订单表
		ProductOrder productOrder = new ProductOrder();
		ProductOrderDetail productOrderDetail = new ProductOrderDetail();
		productOrderDetail.setPolicyNo(atinPolicy.getId());
		productOrderDetail.setOrderPremium(atinPolicy.getPremium());//订单明细费用
		productOrderDetail.setOrderStatus(String.valueOf(OrderStatus.OSNP));//订单状态
		productOrderDetail.setPolicyNo(atinPolicy.getId());
		productOrderDetail.setProductBrandCode("");//产品品牌编码
		productOrderDetail.setProductBrandName("");//产品品牌名称
		productOrderDetail.setOrderDetailDesc(new StringBuilder().append(atinPolicy.getOwner()).append(" ")
				.append(atinPolicy.getLicenseNo()).append(" ")
				.append(AiwbUtils.obtainInsuComNameCn(atinPolicy.getInsuCom())).append("车险保单").toString());
		
		
		
		StockKeepingUnit sku = AncarConfig.obtainSKU(atinPolicy.getInsuCom());
		productOrderDetail.setSkuCode(sku.getCode());//获取产品编码
		productOrderDetail.setSkuName(sku.getName());//名称
		productOrderDetail.setSkuDesc(sku.getDesc());//描述
		
		
		productOrder.setInsuCom(atinPolicy.getInsuCom());//行销渠道
		productOrder.setOrderPremium(atinPolicy.getPremium());//订单总费用
		productOrder.setTransChannel("");//行销渠道
		productOrder.setPayType("");//支付方式
		productOrder.setPayOrderId("");
		productOrder.setSupplierCode("");//供应商代码
		productOrder.setSupplierName("");//供应商名称
		productOrder.setDetailQuantity(1);//订单明细数量
		productOrder.setPayOrderId("");//付费订单号
		productOrder.setBizOrderId("");//业务订单号
		productOrder.setChargeComCode("");//收费商代码
		productOrder.setChargeComName("");//收费商名称
		productOrder.setChargeComUrl("");//收费商链接	
		productOrder.setOrderStatus(String.valueOf(OrderStatus.OSNP));//订单状态
//		productOrder.setInsuredName(insuredName)
		List<AtinQttnCustomer> atinCustomers = atinQuotation.getAtinCustomers();
		for(int i=0;i<atinCustomers.size();i++){
			if("1".equals(atinCustomers.get(i).getPersonType())){
				productOrder.setInsuredName(atinCustomers.get(i).getName());
			}
		}
		List<ProductOrderDetail> orderDetails = new ArrayList<ProductOrderDetail>();
		orderDetails.add(productOrderDetail);
		productOrder.setOrderDetails(orderDetails);
		productOrder.setOpenId(member.getUsername());
		productOrder.setMember(member);
		productOrder.setDisplayStatus(DisplayStatus_Y);
		ContainerFactory.getSession().store(productOrder);
		//-------------------------订单 end-------------//
		
		//-------------------------更新该投保的询价单-----//
		atinQuotation.setQuotationStatus(QuotationStatus.QSYI.toString());//更新状态为已投保
		ContainerFactory.getSession().store(atinQuotation,"quotationStatus");
		
		//获取寄送信息
		@SuppressWarnings("rawtypes")
		List ds = new ArrayList(); 
		if("Y".equals(resInsureConfirmInfo.getInsureSuccessFlag())){
			ds = ContainerFactory.getSession().query(new DbCvo<DeliveryInfo>(DeliveryInfo.class){
				private static final long serialVersionUID = 26920592398311552L;
				@Override
				public void initRules(DeliveryInfo t) {
					setDoCount(true);setDoPaging(true);setRowCount(10);setPagination(1);
					addRule(t.getOpenId(), WhereRuleOper.eq, member.getUsername());
				}
			}).obtainBeans();
		}
		
		//返回结果
		InsureConfirmRvo icRvo = new InsureConfirmRvo();
		ReflectUtil.copyProperties(icRvo, resInsureConfirmInfo);
		icRvo.setOrderNo(productOrder.getId());
		icRvo.setOrderPremium(productOrder.getOrderPremium());
		
		icRvo.setDeliveryInfos(ds);
		bsRvo.setBody(icRvo);
		bsRvo.setExtend(new InsureConfirmExtend("Y".equals(icRvo.getInsureSuccessFlag()), AiwbUtils.transRespMsg(icRvo.getInsureFailReason())));
		return bsRvo;
	}
}
