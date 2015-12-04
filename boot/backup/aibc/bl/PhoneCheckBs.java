package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AncarConfig;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.appBeans.login.PhoneCheckCvoAndRvo;
import zbh.aibc.AncarConfig;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.appBeans.login.PhoneCheckCvoAndRvo;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import zbh.remex.util.Assert;

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
