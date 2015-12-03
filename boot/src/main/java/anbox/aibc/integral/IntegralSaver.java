/**
 * 
 */
package anbox.aibc.integral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import anbox.aibc.AiwbConsts.IntegralWay;
import anbox.aibc.model.member.Member;

/** 
 * @author liuhengyang 
 * @date 2015-2-10 下午7:24:24
 * @version 版本号码
 * @TODO 描述
 */
public abstract class IntegralSaver implements Serializable {
	private static final long serialVersionUID = 5281966130845429944L;
	static Map<IntegralWay,String> integralWayMap = new HashMap<IntegralWay,String>();
	static {
		integralWayMap.put(IntegralWay.Login,"登录");
		integralWayMap.put(IntegralWay.Register,"注册");
		integralWayMap.put(IntegralWay.QuotePrice,"询价");
		integralWayMap.put(IntegralWay.SignOrder,"签单");
		integralWayMap.put(IntegralWay.Spread,"ancar推广");
	}
	public abstract void saveIntegral(Member me, Map<String,Object>params);
}
