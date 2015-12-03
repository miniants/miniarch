package anbox.aibc.bl;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.order.OrderSubmitCvo;
import anbox.aibc.appBeans.order.OrderSubmitExtend;
import anbox.aibc.appBeans.order.OrderSubmitRvo;
import anbox.aibc.model.order.ProductOrder;
import anbox.aibc.model.order.ProductOrderDetail;
import anbox.aibc.model.policy.AtinPolicy;
import cn.com.qqbx.cpsp.chargews.xmlbeans.businessextend.ReqBusinessExtend;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ReqUnifyOrderProduct;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ReqUnifyPlaceOrder;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ResUnifyPlaceOrder;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.reflect.ReflectUtil;

/**订单提交
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class OrderSubmitBs  implements AiwbConsts{
	@BsAnnotation(bsCvoBodyClass=OrderSubmitCvo.class,bsRvoBodyClass=OrderSubmitRvo.class,
			bsCvoExtendClass=OrderSubmitExtend.class,bsRvoExtendClass=OrderSubmitExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		OrderSubmitCvo orderSubmitCvo = bsCvo.getBody();
		String chargeComCode = orderSubmitCvo.getChargeComCode();
		String tradeType;
		if("WX".equals(chargeComCode)){
			tradeType="JSAPI";
		}else{
			tradeType="DEFAULT";
		}
		//报文拼装需要使用的临时变量
		double orderPremium = 0;
		String pfPolicyNo = null;//平台端保单号
		final String bizOrderNo = orderSubmitCvo.getOrderNo();//应用端的订单号
		String bizDesc;//用户在支付页面看到的订单描述信息
		String insuComCn;
		String insuCom;
		
		//内部数据审核及查询
		
		//确认存在订单
		ProductOrder order = ContainerFactory.getSession().queryBeanById(ProductOrder.class, bizOrderNo);
		if(null==order){
			bsRvo.setExtend(new OrderSubmitExtend(false , "下单对应的订单不存在！"));	
			return bsRvo;
		}
		
		if(OrderStatus.OSYP.toString().equals(order.getOrderStatus())){
			bsRvo.setExtend(new OrderSubmitExtend(false , "对应的订单已支付，不可重复支付！"));	
			return bsRvo;
		}
		//此处更新订单表中的地址
		order.setPostContact(orderSubmitCvo.getRcvName());
		order.setPostAddress(orderSubmitCvo.getRcvPostAddress());
		order.setPostMobile(orderSubmitCvo.getRcvMobile());
		
		//确认订单明细
		order.getOrderDetails();
		List<ProductOrderDetail> productOrderDetails=order.getOrderDetails();
		if(productOrderDetails.size()>=0){
			//TODO 从库中拿出的数据copy到reqUnifyPlaceOrder中
			//因为目前车险只有一个订单明细，所以直接去0号订单，以后成为多订单或购物车时，需要循环获取。
			orderPremium = productOrderDetails.get(0).getOrderPremium();
			String bizPolicyNo = productOrderDetails.get(0).getPolicyNo();//获取应用端保单号
			AtinPolicy atinPolicy = ContainerFactory.getSession().queryBeanById(AtinPolicy.class,bizPolicyNo); //各家应用端保单号查询应用端保单表
			insuCom=atinPolicy.getInsuCom();
			insuComCn = AiwbUtils.obtainInsuComNameCn(atinPolicy.getInsuCom());
			bizDesc = productOrderDetails.get(0).getOrderDetailDesc();
			/*bizDesc.append(atinPolicy.getOwner()).append(" ")
			.append(atinPolicy.getLicenseNo()).append(" ")
			.append(insuComCn).append("车险保单");*/
			
			pfPolicyNo = atinPolicy.getAitpPolicyNo();//获取平台端保单号
		}else{
			bsRvo.setExtend(new OrderSubmitExtend(false , "订单信息异常！"));	
			return bsRvo;
		}
		
		//发起订单请求部分
		ReqUnifyPlaceOrder reqUnifyPlaceOrder = new ReqUnifyPlaceOrder();
		ReflectUtil.copyProperties(reqUnifyPlaceOrder, orderSubmitCvo);
		reqUnifyPlaceOrder.setSuppllierCode(insuCom);//供应商代码
		reqUnifyPlaceOrder.setSuppllierName(insuComCn);//供应商名称
		reqUnifyPlaceOrder.setTradeType(tradeType);
		//平台端保单号（应用端订单明细中的保单号）
		reqUnifyPlaceOrder.setBizOrderId(pfPolicyNo);
		//应用端和cpsp交互的订单号
		reqUnifyPlaceOrder.setBizNo(bizOrderNo);
		reqUnifyPlaceOrder.setBizDesc(bizDesc);//用户在支付页面看到的订单描述信息
		reqUnifyPlaceOrder.setChargeComCode(chargeComCode);
		reqUnifyPlaceOrder.setUserId(orderSubmitCvo.getUserId());
		reqUnifyPlaceOrder.setName(orderSubmitCvo.getUserId());//无会员名时同账号
		
		//产品明细
		List<ReqUnifyOrderProduct> reqUnifyOrderProducts = new ArrayList<ReqUnifyOrderProduct>();
		ReqUnifyOrderProduct reqUnifyOrderProduct = new ReqUnifyOrderProduct();
		reqUnifyOrderProduct.setPrdtNo("");//从BL中的产品功能获取，SKU
		reqUnifyOrderProduct.setPrdtType("AI");
		reqUnifyOrderProduct.setPrdtName("");
		reqUnifyOrderProduct.setPrdtQuantity("1");
		reqUnifyOrderProduct.setPrdtAmount(orderPremium+"");//订单金额应该从数据中中读取，和前端核对后在提交 TODO orderPremium
		reqUnifyOrderProducts.add(reqUnifyOrderProduct);
		//添加到顶节点
		reqUnifyPlaceOrder.setUnifyOrderProducts(reqUnifyOrderProducts);
		
		//准备调用服务
		ReqBusinessExtend extend = new ReqBusinessExtend(true, "");
		extend.setTransChannel("MBWX");
		extend.setCustomerIp("127.0.0.1");
		boolean status = false;
		String msg="INIT";
		ResUnifyPlaceOrder rs = null;
		try{
			rs = (ResUnifyPlaceOrder) AiwbUtils.invokeService("CpspUnifyPlaceOrderBs", "execute", reqUnifyPlaceOrder,extend, "ZHDX","");
			status = true;
			order.setOrderStatus(OrderStatus.OSSP.toString());
			ContainerFactory.getSession().store(order);
			msg="订单确认成功！";
		}catch (Exception e) {
			e.printStackTrace();
			status = false;
			msg=e.toString();
		}
		bsRvo.setBody(rs);
		bsRvo.setExtend(new OrderSubmitExtend(status , AiwbUtils.transRespMsg(msg)));	
		return bsRvo;
	}
}
