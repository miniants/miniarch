package anbox.aibc.bl;

import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.OrderInfo;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.order.ProductOrder;
import anbox.aibc.model.order.ProductOrderDetail;
import anbox.aibc.model.policy.AtinPolicy;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.reflect.ReflectUtil;
import cn.remex.reflect.ReflectUtil.SPFeature;

/**客户订单
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class QueryOrderDetailBs  implements AiwbConsts{
	
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
