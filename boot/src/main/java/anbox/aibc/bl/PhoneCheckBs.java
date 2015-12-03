package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.login.LoginExtend;
import anbox.aibc.appBeans.login.PhoneCheckCvoAndRvo;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.util.Assert;

/**
 * @author zhangaiguo
 *@since 2015-01-19
 */
@BsAnnotation
public class PhoneCheckBs   implements AiwbConsts{
	@BsAnnotation(bsRvoBodyClass=PhoneCheckCvoAndRvo.class,bsCvoBodyClass=PhoneCheckCvoAndRvo.class,
			bsCvoExtendClass=LoginExtend.class,bsRvoExtendClass=LoginExtend.class)
	public BsRvo sendPhoneCheckCode(BsCvo bsCvo, BsRvo bsRvo) {
		PhoneCheckCvoAndRvo phoneCheckRvo =bsCvo.getBody();
		
		String phoneNo =phoneCheckRvo.getPhoneNo();
		Assert.notNullAndEmpty(phoneNo, "手机号不能为空");
		//把code发短信发出去。
		String token = AncarConfig.createPhoneCheckToken(phoneNo);
		phoneCheckRvo.setPhoneCheckToken(token+"");
		bsRvo.setBody(phoneCheckRvo);
		

		bsRvo.setExtend(new LoginExtend(true,"OK"));
		return bsRvo;
	}
	

	
	
	

}
