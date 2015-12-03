package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.SecurityCvo;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.util.Judgment;

@BsAnnotation
public class SecurityBs implements AiwbConsts{
	/**更换手机验证手机有效性
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = SecurityCvo.class, bsCvoExtendClass = MemberExtend.class, 
			bsRvoExtendClass = MemberExtend.class)
	public BsRvo execCheckMobile(BsCvo bsCvo, BsRvo bsRvo) {
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		SecurityCvo securityCvo = bsCvo.getBody();
		if(!securityCvo.getCheckMobile().equals(securityCvo.getOldMobile()) || !securityCvo.getCheckMobile().equals(securityCvo.getNewMobile())){
			bsRvo.setExtend(new MemberExtend(false, "验证手机号与输入的手机号不一致"));
			return bsRvo;
		}
		
		if(Judgment.nullOrBlank(securityCvo.getCheckCode()) || Judgment.nullOrBlank(securityCvo.getPhoneCheckToken()) || 
				Judgment.nullOrBlank(securityCvo.getCheckMobile())){
			bsRvo.setExtend(new MemberExtend(false, "验证码或已绑定手机号码为空!"));
			return bsRvo;
		}
		
		if(!m.getMobile().equals(securityCvo.getOldMobile())){
			bsRvo.setExtend(new MemberExtend(false, "与注册的手机号不一致"));
			return bsRvo;
		}
		
		if(!AncarConfig.checkPhoneCheckToken(securityCvo.getCheckMobile(), securityCvo.getCheckCode(), 
				securityCvo.getPhoneCheckToken())){
			bsRvo.setExtend(new MemberExtend(false, "手机验证码失效!"));
			return bsRvo;
		}
		bsRvo.setExtend(new MemberExtend(true, "手机验证码通过"));
		return bsRvo;
	}
	
	/**更换手机
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = SecurityCvo.class, bsCvoExtendClass = MemberExtend.class, 
			bsRvoExtendClass = MemberExtend.class)
	public BsRvo execChangeMobile(BsCvo bsCvo, BsRvo bsRvo) {
		final Member m = AiwbUtils.getLoginedMember();
		BsRvo br = execCheckMobile(bsCvo,bsRvo);//验证验证码、旧手机号码的合法性
		if(!br.getExtend().isStatus())return br;
		
		SecurityCvo securityCvo = bsCvo.getBody();
		m.setMobile(securityCvo.getNewMobile());
		ContainerFactory.getSession().store(m,"mobile");
		bsRvo.setExtend(new MemberExtend(true, "修改完毕"));
		return bsRvo;
	}
}
