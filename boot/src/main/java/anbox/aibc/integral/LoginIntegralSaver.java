/**
 * 
 */
package anbox.aibc.integral;

import java.util.Map;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import anbox.aibc.AncarConfig;
import anbox.aibc.model.member.IntegralBeanDetail;
import anbox.aibc.model.member.Member;
import cn.remex.db.ContainerFactory;
import cn.remex.util.DateHelper;

/**
 * @author liuhengyang
 * @date 2015-2-10 下午7:26:09
 * @version 版本号码
 * @TODO 描述
 */
public class LoginIntegralSaver extends IntegralSaver {

	private static final long serialVersionUID = -5219115625539445512L;

	public void saveIntegral(Member me, Map<String,Object>params) {
		
		IntegralWay way = IntegralWay.Login;
		int addCount = AncarConfig.obtainIntegralCount(way, IntegralType.Bean);
		int bb = me.getBeanCount();
		int ab = bb+addCount;

		// 存日志 //TODO　获取积分的具体算法应该记录
		IntegralBeanDetail integralBeanDetail = new IntegralBeanDetail();
		integralBeanDetail.setType(IntegralType.Bean);// 卡豆
		integralBeanDetail.setMemberId(me.getId());
		integralBeanDetail.setMemberUsername(me.getUsername());
		integralBeanDetail.setWay(way);
		integralBeanDetail.setWayDesc(integralWayMap.get(way));
		integralBeanDetail.setAmount(addCount+"");// 此处可正可负
		integralBeanDetail.setBeforeBalance(bb+"");
		integralBeanDetail.setAfterBalance(ab+"");
		integralBeanDetail.setSettleTime(DateHelper.getNow());
		integralBeanDetail.setDesc("登录获取积分");
		ContainerFactory.getSession().store(integralBeanDetail);
		
		me.setBeanCount(ab);
		ContainerFactory.getSession().store(me, "beanCount");
	}

}
