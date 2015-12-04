/**
 * 
 */
package zbh.aibc.integral;

import java.util.Map;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import anbox.aibc.AncarConfig;
import anbox.aibc.model.member.IntegralCoinDetail;
import anbox.aibc.model.member.Member;
import cn.remex.db.ContainerFactory;
import zbh.remex.util.DateHelper;

/** 
 * @author liuhengyang 
 * @date 2015-2-10 下午7:26:09
 * @version 版本号码
 * @TODO 描述
 */
public class SpreadIntegralSaver extends IntegralSaver {

	private static final long serialVersionUID = 8697343064277856057L;

	public void saveIntegral(Member me, Map<String,Object>params) {
		String desc = (String) params.get("0");//调用积分时约定的。
		
		IntegralWay way = IntegralWay.Spread;
		int addCount = AncarConfig.obtainIntegralCount(way, IntegralType.Coin);
		int bc = me.getCoinCount();
		int ac = bc+addCount;

		IntegralCoinDetail integralCoinDetail = new IntegralCoinDetail();
		integralCoinDetail.setType(IntegralType.Coin);// 卡豆
		integralCoinDetail.setMemberId(me.getId());
		integralCoinDetail.setMemberUsername(me.getUsername());
		integralCoinDetail.setWay(way);
		integralCoinDetail.setWayDesc(integralWayMap.get(way));
		integralCoinDetail.setAmount(addCount+"");// 此处可正可负
		integralCoinDetail.setBeforeBalance(bc+"");
		integralCoinDetail.setAfterBalance(ac+"");
		integralCoinDetail.setSettleTime(DateHelper.getNow());
		integralCoinDetail.setDesc(desc);
		ContainerFactory.getSession().store(integralCoinDetail);

		me.setCoinCount(ac);
		ContainerFactory.getSession().store(me, "coinCount");
	}

}
