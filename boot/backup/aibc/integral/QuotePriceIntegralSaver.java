/**
 * 
 */
package zbh.aibc.integral;

import java.util.Map;

import zbh.aibc.AncarConfig;
import zbh.aias.model.member.IntegralBeanDetail;
import zbh.aias.model.member.Member;
import cn.remex.db.ContainerFactory;
import zbh.aibc.AiwbConsts;
import zbh.remex.util.DateHelper;

/** 
 * @author liuhengyang 
 * @date 2015-2-10 下午7:26:09
 * @version 版本号码
 * @TODO 描述
 */
public class QuotePriceIntegralSaver extends IntegralSaver {

	private static final long serialVersionUID = 2401333828159510229L;

	public void saveIntegral(Member me, Map<String,Object>params) {
		String desc = (String) params.get("0");//调用积分时约定的。

		AiwbConsts.IntegralWay way = AiwbConsts.IntegralWay.QuotePrice;
		int addCount = AncarConfig.obtainIntegralCount(way, AiwbConsts.IntegralType.Bean);
		int bb = me.getBeanCount();
		int ab = bb+addCount;

		// 存日志 //TODO　获取积分的具体算法应该记录
		IntegralBeanDetail integralBeanDetail = new IntegralBeanDetail();
		integralBeanDetail.setType(AiwbConsts.IntegralType.Bean);// 卡豆
		integralBeanDetail.setMemberId(me.getId());
		integralBeanDetail.setMemberUsername(me.getUsername());
		integralBeanDetail.setWay(way);
		integralBeanDetail.setWayDesc(integralWayMap.get(way));
		integralBeanDetail.setAmount(addCount+"");// 此处可正可负
		integralBeanDetail.setBeforeBalance(bb+"");
		integralBeanDetail.setAfterBalance(ab+"");
		integralBeanDetail.setSettleTime(DateHelper.getNow());
		integralBeanDetail.setDesc(desc);
		ContainerFactory.getSession().store(integralBeanDetail);

		me.setBeanCount(ab);
		ContainerFactory.getSession().store(me, "beanCount");
	}

}
