package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.login.LoginExtend;
import anbox.aibc.appBeans.login.RegisterCvo;
import anbox.aibc.appBeans.login.RegisterRvo;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.Judgment;

/**
 * 注册
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation()
public class RegisterBs implements AiwbConsts{
	@RsqlTransaction
	@BsAnnotation(bsCvoBodyClass = RegisterCvo.class, bsRvoBodyClass = RegisterRvo.class, bsCvoExtendClass = LoginExtend.class, bsRvoExtendClass = LoginExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		
		// 获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if (null != curMember) {
			bsRvo.setExtend(new MemberExtend(false, "请先退出!"));
			return bsRvo;
		}

		RegisterCvo registerCvo = bsCvo.getBody();
		registerCvo.setUsername(registerCvo.getMobile());
		String username = registerCvo.getUsername();
		String memberType = registerCvo.getMemberType();
		if (null == memberType || null == username || null == registerCvo.getPassword() || null == registerCvo.getMobile() ||
				Judgment.nullOrBlank(registerCvo.getCheckCode()) || Judgment.nullOrBlank(registerCvo.getPhoneCheckToken())) {
			bsRvo.setExtend(new LoginExtend(false, AiwbConsts.TipsMsg_logined));
			return bsRvo;
		}
		
/*		if (username.contains("@") || username.matches("^\\d+$")) {
			bsRvo.setExtend(new LoginExtend(false, "用户名不能有@符号且不能全部为数字"));
			return bsRvo;
		}*/

		String registerType = registerCvo.getRegisterType();
		/*//邀请渠道验证
		if (!(AiwbConsts.RegisterType.InvitationCode.toString().equals(registerType) || 
				AiwbConsts.RegisterType.InvitationLink.toString().equals(registerType) || 
				AiwbConsts.RegisterType.QRCode.toString().equals(registerType)) ||
				Judgment.nullOrBlank(registerCvo.getInvitedCode())) {
			bsRvo.setExtend(new LoginExtend(false, "目前系统仅支持邀请注册，请联系会员获取邀请码、邀请链接、邀请二维码，谢谢您的关注!"));
			return bsRvo;
		}*/
		
		final Member member = new Member();
		if(memberType==MemberType.Business.toString()){
			if(null == registerCvo.getIdNo()){
				bsRvo.setExtend(new LoginExtend(false, AiwbConsts.TipsMsg_logined));
				return bsRvo;
			}
			member.setLevel(memberLevel.Small.toString());
		}
		// 手机验证码校验
		if (!AncarConfig.checkPhoneCheckToken(registerCvo.getMobile(), registerCvo.getCheckCode(), registerCvo.getPhoneCheckToken())) {
			bsRvo.setExtend(new LoginExtend(false, "手机验证码失效!"));
			return bsRvo;
		}

		// 邀请码校验
		final String code = registerCvo.getInvitedCode();
		ReflectUtil.copyProperties(member, registerCvo);// 密码已经过前端加密
		Member supMember = null;
		if(!(AiwbConsts.RegisterType.Normal.toString().equals(registerType))){
			supMember = AncarConfig.checkInviteCode(member, code, registerType);//没有要求人则邀请码无效
			if (null==supMember ) {
				bsRvo.setExtend(new LoginExtend(false, "邀请码无效!"));
				return bsRvo;
			}
		}

		// 手机号，用户名都不能重复，或的关系
		DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<Member>(Member.class) {
			private static final long serialVersionUID = 2898420261870286557L;
			@Override
			public void initRules(Member t) {//手机号，用户名都不能重复
				setDoCount(true);setDoPaging(true);setRowCount(10);setPagination(1); // 分页参数 TODO
				addRule(t.getUsername(), WhereRuleOper.eq, member.getUsername());
				addRule(t.getMobile(), WhereRuleOper.eq, member.getMobile());
				setGroupOp(WhereGroupOp.OR);
			}
		});
		
		int count = dbRvo.getRecords();
		if (count == 0) {
			member.setRegisterType(registerType);
			
			member.setInvitedCode(code);
			member.setSupLine(supMember);
			ContainerFactory.getSession().store(member);// 保存注册信息
			//到此注册成功方可继续其他积分类操作。
			
			//保存注册卡豆bean积分
			AncarConfig.saveIntegral(member,AiwbConsts.IntegralWay.Register);//要求赠送
			if(!(AiwbConsts.RegisterType.Normal.toString().equals(registerType))){
				//保存要求卡币coin积分
				AncarConfig.saveIntegral(supMember,AiwbConsts.IntegralWay.Spread,member.getUsername()+"注册");//要求赠送
			}
			
			// 保存当前登录成功的用户至session回话中
			AiwbUtils.setSessionMember(member);
			// 记录登录日志
			AiwbUtils.saveLoginLog(username,AiwbConsts.Flag.Y,"注册成功并登录");
			
			/*InvitationCode invitationCode = (InvitationCode)(ContainerFactory.getSession().query(new DbCvo<InvitationCode>(InvitationCode.class) {
				private static final long serialVersionUID = 7668907569692101528L;
				@Override
				public void initRules(InvitationCode t) {
					addRule(t.getInvitationCode(), WhereRuleOper.eq, code);
					addRule(t.getStatus(), WhereRuleOper.eq, InvitationCodeStatus.Effective.toString());
				}
			}).obtainBeans()).get(0);
			invitationCode.setStatus(InvitationCodeStatus.Used.toString());
			ContainerFactory.getSession().store(invitationCode,"status");*/
			
			//bs报文
			RegisterRvo registerRvo = new RegisterRvo();
			registerRvo.setUserId(member.getId());
			registerRvo.setUsername(member.getUsername());
			registerRvo.setNickname(member.getNickname());
			bsRvo.setBody(registerRvo);
			bsRvo.setExtend(new LoginExtend(true, "注册成功!"));
		} else {// 其他情况
			bsRvo.setExtend(new LoginExtend(false, "用户名或手机号已被注册，请更换信息!"));
			return bsRvo;
		}
		return bsRvo;
	}

}
