package anbox.aibc.bl;

import java.util.HashMap;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import anbox.aibc.AiwbConsts.OrderStatus;
import anbox.aibc.AncarConfig;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.order.ProductOrder;
import anbox.aibc.model.order.ProductOrderDetail;
import anbox.aibc.model.policy.AtinPolicy;
import cn.com.qqbx.cpsp.chargews.xmlbeans.resultnotice.ReqChargeResultNotice;
import cn.com.qqbx.cpsp.chargews.xmlbeans.resultnotice.ResChargeResultNotice;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.Extend;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.core.RemexApplication;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.util.Judgment;

@BsAnnotation()
public class AiwcChargeResultBs {
	@RsqlTransaction
	@BsAnnotation(bsCvoBodyClass = ReqChargeResultNotice.class, bsRvoBodyClass = ResChargeResultNotice.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		System.out.println("CPSP发来支付确认通知");
		ReqChargeResultNotice reqChargeResultNotice = bsCvo.getBody();

		// 根据应用端内部保单号查询保单
		for (String atinPolicyId : reqChargeResultNotice.getBizOrderId().split("\\|")) {
			final String orderNo = reqChargeResultNotice.getBizNo();// BizNo是应用端的订单号
			ProductOrder productOrder = ContainerFactory.getSession().queryBeanById(ProductOrder.class, orderNo);
			if (productOrder == null) {
				// 应该将没有查到保单信息的的异常情况需要保存到相关日志里面。 TODO
				AiwbConsts.logger.error("CPSP发来支付确认通知，但应用端没有查到订单信息：" + orderNo);
				System.out.println("CPSP发来支付确认通知，但应用端没有查到订单信息：" + orderNo);
				bsRvo.setExtend(new Extend(false, "应用端没有查到订单信息！"));
				return bsRvo;
				// AitpAssert.throwAitpException(DataErrorCode.DC002, "平台未查到保单信息");
				// //AITP的comm类不能用于交易的核心代码 TODO
			}
			List<ProductOrderDetail> productOrderDetails = productOrder.getOrderDetails();
			ProductOrderDetail productOrderDetail = productOrderDetails.get(0); // TODO 目前只有一个保单，后续需要多个
			String bizPolicyNo = productOrderDetail.getPolicyNo();//获取应用端保单号
			AtinPolicy atinPolicy = ContainerFactory.getSession().queryBeanById(AtinPolicy.class,bizPolicyNo); //各家应用端保单号查询应用端保单表
			String licenseNo = atinPolicy.getLicenseNo();
			if(!atinPolicyId.equals(atinPolicy.getAitpPolicyNo())){  //TODO 如果一个订单两个保单号  第一个保单号处理失败  第二个保单号不会再处理
				AiwbConsts.logger.error("CPSP发来支付确认通知，但应用端订单明细对应的保单号与之不符，保单号为：" + atinPolicyId);
				System.out.println("CPSP发来支付确认通知，但应用端订单明细对应的保单号与之不符，保单号为：" + atinPolicyId);
				bsRvo.setExtend(new Extend(false, "应用端订单明细对应的保单号与之不符！"));
				return bsRvo;
			}
			System.out.println("CPSP发来支付确认通知，保单号为：" + atinPolicyId);

			if ("ACH".equals(reqChargeResultNotice.getStatus())) {
				//先更新订单的状态为已支付OSYP
				productOrder.setOrderStatus(OrderStatus.OSYP.toString());
				ContainerFactory.getSession().store(productOrder,"orderStatus");
				if (productOrderDetails.size() == 1) {
					productOrderDetail.setOrderStatus(OrderStatus.OSYP.toString());
					ContainerFactory.getSession().store(productOrderDetail,"orderStatus");
				}else{
					//TODO 目前订单明细和订单是1:1的，如果不等于1，需要记录异常
				}

				//取消相关订单及订单明细的有效状态
				cancelOtherOrder(orderNo,licenseNo);
				
				//所有订单状态保存工作完成后，获取签单积分
				Member m = productOrder.getMember();
				if(null != m && !Judgment.nullOrBlank(m.getId())){
					AncarConfig.saveIntegral(m, IntegralWay.SignOrder,productOrderDetail);
				}else{
					//TODO 异常不能抛出，记录到系统的报警日志中
				}
			} else {
				productOrder.setOrderStatus(OrderStatus.OSFP.toString());
				ContainerFactory.getSession().store(productOrder,"orderStatus");
				if (productOrderDetails.size() == 1) {
					productOrderDetail.setOrderStatus(OrderStatus.OSFP.toString());
					ContainerFactory.getSession().store(productOrderDetail,"orderStatus");
				}
			}

		}
		// 返回信息
		ResChargeResultNotice resChargeResultNotice = new ResChargeResultNotice();
		resChargeResultNotice.setResultCode("Y");// 结果代码
		resChargeResultNotice.setResultMessage("成功通知平台扣费结果");// 结果信息
		bsRvo.setBody(resChargeResultNotice);
		bsRvo.setExtend(new Extend(true, "服务成功！"));
		return bsRvo;
	}
	
	

	public static void main(String... s){
		RemexApplication.getContext();
		new AiwcChargeResultBs().cancelOtherOrder("SNPO2015020900000000000702", "京GJD399");
		ProductOrder productOrder = ContainerFactory.getSession().queryBeanById(ProductOrder.class, "SNPO2015020900000000000702");
		ProductOrderDetail productOrderDetail =productOrder.getOrderDetails().get(0);
		AncarConfig.saveIntegral(productOrder.getMember(), IntegralWay.SignOrder,productOrderDetail);
		
	}
	
	private void cancelOtherOrder(String orderNo,final String licenseNo){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("licenseNo", licenseNo);
		//必须先更新订单信息，订单信息中依赖订单明细的OSNP状态进行更新。
		DbRvo dbRvo2 = ContainerFactory.getSession().executeUpdate("SQL_updateProductOrderStatus_OSIP", params );
		//其次更新订单明细
		DbRvo dbRvo1 = ContainerFactory.getSession().executeUpdate("SQL_updateProductOrderDetailStatus_OSIP", params );
		//最后更新报价
		DbRvo dbRvo3 = ContainerFactory.getSession().executeUpdate("SQL_updateAitnQuotationStatus_QSIQ", params );
		AiwbConsts.logger.info("SQL_updateProductOrderDetailStatus_OSIP:"+licenseNo+dbRvo1.getEffectRowCount());
		AiwbConsts.logger.info("SQL_updateProductOrderStatus_OSIP:"+licenseNo+dbRvo2.getEffectRowCount());
		AiwbConsts.logger.info("SQL_updateAitnQuotationStatus_QSIQ:"+licenseNo+dbRvo3.getEffectRowCount());
		
		
	}
}
