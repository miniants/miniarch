package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aias.model.member.Member;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

/**注销
 * @author zhangaiguo
 *@since 2015-01-16
 */
@BsAnnotation
public class LogoutBs  implements AiwbConsts{
	@BsAnnotation(bsRvoExtendClass=LoginExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, "已经退出!"));
			return bsRvo;
		}
		AiwbUtils.setSessionMember(null);
		bsRvo.setExtend(new LoginExtend(true,"成功注销!"));
		return bsRvo;
	}

}
