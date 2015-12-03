package anbox.aibc.bl;

import java.util.Calendar;
import java.util.Date;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.RandomUtils;
import anbox.aibc.appBeans.login.AncarSpreadRvo;
import anbox.aibc.appBeans.login.LoginExtend;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.model.member.InvitationCode;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.util.DateHelper;

/**
 * @author zhangaiguo
 *@since 2015-01-19
 */
@BsAnnotation
public class InvitationCodeBs   implements AiwbConsts{
	@BsAnnotation(bsRvoBodyClass=AncarSpreadRvo.class,
			bsCvoExtendClass=LoginExtend.class,bsRvoExtendClass=LoginExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		String code = RandomUtils.createRandomCode(RandomCodeLength, false);//true表示包含字母
		AncarSpreadRvo ancarSpreadRvo = new AncarSpreadRvo();
		ancarSpreadRvo.setInvitationCode(code);
		bsRvo.setBody(ancarSpreadRvo);
		InvitationCode invitationCode = new InvitationCode();
		invitationCode.setMember(curMember);
		invitationCode.setInvitationCode(code);
		invitationCode.setGenerationTime(DateHelper.getNow());
		invitationCode.setStatus(InvitationCodeStatus.Effective.toString());
		invitationCode.setValidityPeriod(ValidityPeriod);
//		invitationCode.setInvalidTime(DateHelper.formatDate(DateHelper.getDiffTargetDate(null,ValidityPeriod),DateFormat_time));//等待core更新
		invitationCode.setInvalidTime(DateHelper.formatDate(getDiffTargetDate(null,ValidityPeriod),DateFormat_time));
		ContainerFactory.getSession().store(invitationCode);
		bsRvo.setExtend(new LoginExtend(true,"OK"));
		return bsRvo;
	}
	private static Date getDiffTargetDate(Calendar cal,int diffDay){//设置有效期30天
		if(null == cal){
			cal = Calendar.getInstance();
		}
//		cal.set(cal.DAY_OF_YEAR, cal.get(cal.DAY_OF_YEAR)+diffDay);
		cal.add(Calendar.DATE, diffDay);
		return cal.getTime();
	}
}
