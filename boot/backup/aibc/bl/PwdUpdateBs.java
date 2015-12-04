package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.AncarConfig;
import zbh.aibc.RSAUtils11;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.PwdCvo;
import zbh.aibc.appBeans.mmbCenter.PwdRvo;
import zbh.aias.model.member.Member;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;

/**修改密码
 * @author zhangaiguo
 * @since 2015-01-16
 */
@BsAnnotation
public class PwdUpdateBs   implements AiwbConsts{
	@BsAnnotation(bsCvoBodyClass=PwdCvo.class,bsRvoBodyClass=PwdRvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		PwdCvo pwdCvo = bsCvo.getBody();
		
		// 手机验证码校验
		if (!AncarConfig.checkPhoneCheckToken(pwdCvo.getMobile(), pwdCvo.getCheckCode(), pwdCvo.getPhoneCheckToken())) {
			bsRvo.setExtend(new LoginExtend(false, "手机验证码失效!"));
			return bsRvo;
		}
		
		MemberExtend memberExtend = new MemberExtend(false, "");
		//校验并更新密码
		String curPwd = RSAUtils11.decrypt(Rsa_PrivateKeyPath, curMember.getPassword());
		String oriPwd = RSAUtils11.decrypt(Rsa_PrivateKeyPath, pwdCvo.getOriginPassword());
//		String newPwd = RSAUtils11.decrypt(Rsa_PublicKeyPath, pwdCvo.getNewPassword());
		if(curPwd.equals(oriPwd)){//校验传入的原密码与库存密码的明文是否一致
			//TODO 更新密码时是否需要校验原密码与新密码的一致性
			curMember.setPassword(pwdCvo.getNewPassword());
			ContainerFactory.getSession().store(curMember, "password");
			memberExtend.setStatus(true);
			memberExtend.setMsg("密码更新成功!");
		}else{
			memberExtend.setStatus(false);
			memberExtend.setMsg("原密码错误!");
		}
		bsRvo.setExtend(memberExtend);
		return bsRvo;
	}
}
