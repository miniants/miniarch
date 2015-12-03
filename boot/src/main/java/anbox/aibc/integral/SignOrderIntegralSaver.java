/**
 * 
 */
package anbox.aibc.integral;

import java.math.BigDecimal;
import java.util.Map;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import anbox.aibc.AncarConfig;
import anbox.aibc.model.member.IntegralCoinDetail;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.order.ProductOrderDetail;
import cn.remex.db.ContainerFactory;
import cn.remex.util.Arith;
import cn.remex.util.DateHelper;

/**
 * @author liuhengyang
 * @date 2015-2-10 下午7:26:09
 * @version 版本号码
 * @TODO 描述
 */
public class SignOrderIntegralSaver extends IntegralSaver {

	private static final long serialVersionUID = 8352219505514600954L;

	public void saveIntegral(Member me, Map<String, Object> params) {
		ProductOrderDetail pod = (ProductOrderDetail) params.get("0");// 调用积分时约定的。
		String skuCode = pod.getSkuCode();
		IntegralWay way = IntegralWay.SignOrder;
		Object base = pod.getOrderPremium();


		// 根据配置获取本次卡豆/卡币数量
		double cRrate = AncarConfig.obtainIntegralRate(skuCode, way, IntegralType.Coin);
		int addCount = new BigDecimal(Arith.mul(base, cRrate)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int bc = me.getCoinCount();//之前的
		int ac = bc+addCount;//之后的
		
		
		IntegralCoinDetail integralCoinDetail = new IntegralCoinDetail();
		integralCoinDetail.setType(IntegralType.Coin);// 卡币
		integralCoinDetail.setMemberId(me.getId());
		integralCoinDetail.setMemberUsername(me.getUsername());
		integralCoinDetail.setWay(way);
		integralCoinDetail.setWayDesc(integralWayMap.get(way));
		integralCoinDetail.setAmount(addCount + "");// 此处可正可负
		integralCoinDetail.setBeforeBalance(bc + "");
		integralCoinDetail.setAfterBalance(ac + "");
		integralCoinDetail.setSettleTime(DateHelper.getNow());
		integralCoinDetail.setDesc(pod.getOrderDetailDesc());
		ContainerFactory.getSession().store(integralCoinDetail);

		
//		double bRrate = 0;// AncarConfig.obtainIntegralRate(skuCode,way ,
//		// IntegralType.Bean);
//		int addBeanCount = new BigDecimal(Arith.mul(base, bRrate)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
//		int bb = me.getBeanCount();
//		IntegralBeanDetail integralBeanDetail = new IntegralBeanDetail();
//		integralBeanDetail.setType(IntegralType.Bean);// 卡币
//		integralBeanDetail.setMemberId(me.getId());
//		integralBeanDetail.setMemberUsername(me.getUsername());
//		integralBeanDetail.setWay(way);
//		integralBeanDetail.setWayDesc(integralWayMap.get(way));
//		integralBeanDetail.setAmount(addBeanCount + "");// 此处可正可负
//		integralBeanDetail.setBeforeBalance(bb + "");
//		integralBeanDetail.setAfterBalance(me.getBeanCount() + "");
//		integralBeanDetail.setSettleTime(DateHelper.getNow());
//		integralBeanDetail.setDesc(pod.getOrderDetailDesc());
//		ContainerFactory.getSession().store(integralBeanDetail);
//		me.setBeanCount(me.getBeanCount() + addBeanCount);

		me.setCoinCount(ac);
		ContainerFactory.getSession().store(me, "coinCount");
	}

}
