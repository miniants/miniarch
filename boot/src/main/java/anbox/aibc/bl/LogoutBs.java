package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.login.LoginExtend;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;

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
