package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.AncarExtend;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;

/**专门用于判断用户是否登录的bs，主要用于前端导航的用户显示
 * @author zhangaiguo
 *@since 2015-01-21
 */
@BsAnnotation
public class LoginJudgmentBs implements AiwbConsts{
	@BsAnnotation(bsRvoBodyClass=AncarExtend.class,
			bsCvoExtendClass=AncarExtend.class,bsRvoExtendClass=AncarExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new AncarExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		AncarExtend ancarExtend = new AncarExtend(true, "OK");
		bsRvo.setExtend(ancarExtend);
		ancarExtend.setNickname(curMember.getNickname());
		ancarExtend.setUsername(curMember.getUsername());
		bsRvo.setBody(ancarExtend);
		return bsRvo;
	}
}
