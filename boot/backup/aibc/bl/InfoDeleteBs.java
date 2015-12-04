package zbh.aibc.bl;

import java.util.HashMap;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.model.member.CustomerVehicle;
import anbox.aibc.model.member.DeliveryInfo;
import zbh.aias.model.member.Member;
import anbox.aibc.model.order.ProductOrder;
import zbh.aias.model.quotation.AtinQuotation;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import zbh.remex.util.Judgment;

/**车辆、询价、订单删除
 * @author zhangaiguo
 * @since 2015-02-04
 */
@BsAnnotation
public class InfoDeleteBs implements AiwbConsts{
	/**删除客户车辆*/
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execVehicleDel(BsCvo bsCvo, BsRvo bsRvo) {
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody();
		CustomerVehicle vehicle = ContainerFactory.getSession().queryBeanById(CustomerVehicle.class, mCvo.getId());
		
		if(null == vehicle){
			bsRvo.setExtend(new MemberExtend(false, "未查到数据!"));
			return bsRvo;
		}
		
		//判断权限
		if(!uid.equals(vehicle.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
			return bsRvo;
		}
		
		vehicle.setDisplayStatus(DisplayStatus_N);
		ContainerFactory.getSession().store(vehicle, "displayStatus");
		bsRvo.setExtend(new MemberExtend(true, "成功删除!"));
		return bsRvo;
	}

	/**删除客户询价单*/
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execQuotationDel(BsCvo bsCvo, BsRvo bsRvo) {
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody();
		AtinQuotation quotation = ContainerFactory.getSession().queryBeanById(AtinQuotation.class, mCvo.getId());
		
		if(null == quotation){
			bsRvo.setExtend(new MemberExtend(false, "未查到数据!"));
			return bsRvo;
		}
		
		//判断权限
		if(!uid.equals(quotation.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
			return bsRvo;
		}
		
		quotation.setDisplayStatus(DisplayStatus_N);
		ContainerFactory.getSession().store(quotation, "displayStatus");
		bsRvo.setExtend(new MemberExtend(true, "成功删除!"));
		return bsRvo;
	}
	
	/**删除客户订单*/
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execOrderDel(BsCvo bsCvo, BsRvo bsRvo) {
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody();
		ProductOrder order = ContainerFactory.getSession().queryBeanById(ProductOrder.class, mCvo.getId());
		
		if(null == order){
			bsRvo.setExtend(new MemberExtend(false, "未查到数据!"));
			return bsRvo;
		}
		
		//权限判断
		if(!m.getUsername().equals(order.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
			return bsRvo;
		}
		
		order.setDisplayStatus(DisplayStatus_N);
		ContainerFactory.getSession().store(order, "displayStatus");
		bsRvo.setExtend(new MemberExtend(true, "成功删除!"));
		return bsRvo;
	}
	
	/**取消订单*/
	@RsqlTransaction
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execOrderCancel(BsCvo bsCvo, BsRvo bsRvo) {
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody();
		ProductOrder order = ContainerFactory.getSession().queryBeanById(ProductOrder.class, mCvo.getId());
		
		if(null == order){
			bsRvo.setExtend(new MemberExtend(false, "未查到数据!"));
			return bsRvo;
		}
		
		//权限判断
		if(!m.getUsername().equals(order.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
			return bsRvo;
		}
		
		order.setOrderStatus(OrderStatus.OSCP.toString());//订单取消状态
		ContainerFactory.getSession().store(order, "orderStatus");//保存ProductOrder表的订单状态
		
		
		//更新ProductOrderDetail表
		@SuppressWarnings("unchecked")
		Map<String,String> icm = (Map<String,String>)AiwbUtils.getBean("Order_SQL");
		String sql = icm.get("OrderCancel");
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("productOrder", order.getId());
		ContainerFactory.getSession().executeUpdate(sql, params);
		
		
		bsRvo.setExtend(new MemberExtend(true, "订单取消成功!"));
		return bsRvo;
	}
	
	/**删除客户递送信息*/
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execDeliveryDel(BsCvo bsCvo, BsRvo bsRvo) {
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody();
		DeliveryInfo di = ContainerFactory.getSession().queryBeanById(DeliveryInfo.class, mCvo.getId());//TODO 是否多此一举
		if(null == di){
			bsRvo.setExtend(new MemberExtend(false, "未查到数据!"));
			return bsRvo;
		}
		
		//判断权限
		if(!m.getUsername().equals(di.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
			return bsRvo;
		}
		
		ContainerFactory.getSession().deleteById(DeliveryInfo.class, mCvo.getId());
		bsRvo.setExtend(new MemberExtend(true, "成功删除!"));
		return bsRvo;
	}
	
}
