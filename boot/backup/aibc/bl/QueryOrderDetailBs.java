package zbh.aibc.bl;

import java.util.List;

import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.OrderInfo;
import zbh.aias.model.member.Member;
import zbh.aias.model.order.ProductOrder;
import zbh.aias.model.order.ProductOrderDetail;
import zbh.aias.model.policy.AtinPolicy;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.reflect.ReflectUtil.SPFeature;

/**客户订单
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class QueryOrderDetailBs  implements AiwbConsts {
	
	@BsAnnotation(bsCvoBodyClass=OrderInfo.class,bsRvoBodyClass=OrderInfo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		OrderInfo orderInfo = bsCvo.getBody();
		final String productOrderId=orderInfo.getOrderNo();
		DbRvo dbRvo = ContainerFactory.getSession().query(
				new DbCvo<ProductOrderDetail>(ProductOrderDetail.class){
					private static final long serialVersionUID = 7204273675550902536L;
					@Override
					public void initRules(ProductOrderDetail t) {//会员数据权限查询条件
						addRule(t.getProductOrder()+"", WhereRuleOper.eq, productOrderId);
					}
		});
		List<ProductOrderDetail> productOrderDetails = dbRvo.obtainBeans();
		String policyNo = productOrderDetails.get(0).getPolicyNo();
		AtinPolicy atinPolicy = ContainerFactory.getSession().queryBeanById(AtinPolicy.class, policyNo);
		ReflectUtil.copyProperties(orderInfo, atinPolicy, SPFeature.DeeplyCopy);
		ProductOrder productOrder = ContainerFactory.getSession().queryBeanById(ProductOrder.class,productOrderId);
		orderInfo.setOrderStatus(productOrder.getOrderStatus());
		bsRvo.setBody(orderInfo);
		bsRvo.setExtend(new MemberExtend(true, "OK"));
		return bsRvo;
	}
}
