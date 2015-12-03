package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.AncarExtend;
import anbox.aibc.appBeans.login.PwdResetCvoAndRvo;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.util.Judgment;

@BsAnnotation
public class PwdResetBs implements AiwbConsts{
	
	/**修改密码
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = PwdResetCvoAndRvo.class, bsCvoExtendClass = AncarExtend.class, 
			bsRvoExtendClass = AncarExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		Member m = AiwbUtils.getLoginedMember();
		if(!Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new AncarExtend(false, "已经登录"));
			return bsRvo;
		}
		
		PwdResetCvoAndRvo pwdResetCvo = bsCvo.getBody();
		if(Judgment.nullOrBlank(pwdResetCvo.getCheckCode()) || Judgment.nullOrBlank(pwdResetCvo.getPhoneCheckToken()) || 
				Judgment.nullOrBlank(pwdResetCvo.getCheckMobile())){
			bsRvo.setExtend(new AncarExtend(false, "验证码或已绑定手机号码为空,请获取验证码!"));
			return bsRvo;
		}
		
		m = new Member();
		m.setMobile(pwdResetCvo.getCheckMobile());
		m = ContainerFactory.getSession().pickUp(m);
		
		if(Judgment.nullOrBlank(m.getId())){
			bsRvo.setExtend(new AncarExtend(false, "手机号未注册!"));
			return bsRvo;
		}
		
		if(!AncarConfig.checkPhoneCheckToken(pwdResetCvo.getCheckMobile(), pwdResetCvo.getCheckCode(), 
				pwdResetCvo.getPhoneCheckToken())){
			bsRvo.setExtend(new AncarExtend(false, "手机验证码失效!"));
			return bsRvo;
		}
		
		m.setPassword(pwdResetCvo.getPassword());
		ContainerFactory.getSession().store(m,"password");
		bsRvo.setExtend(new AncarExtend(true, "手机验证通过"));
		return bsRvo;
	}
}
