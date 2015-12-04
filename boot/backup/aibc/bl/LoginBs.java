package zbh.aibc.bl;

import java.util.regex.Pattern;

import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.AncarConfig;
import zbh.aibc.RSAUtils11;
import zbh.aibc.appBeans.AncarExtend;
import zbh.aibc.appBeans.login.LoginCvo;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.appBeans.login.LoginRvo;
import zbh.aibc.appBeans.login.PwdResetCvoAndRvo;
import zbh.aias.model.member.Member;
import zbh.aias.model.system.LoginLog;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.util.DateHelper;
import zbh.remex.util.Judgment;

/**登录
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class LoginBs  implements AiwbConsts {
	/**验证注册的用户名或手机
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = PwdResetCvoAndRvo.class,bsRvoBodyClass=PwdResetCvoAndRvo.class,
			bsCvoExtendClass = AncarExtend.class,bsRvoExtendClass = AncarExtend.class)
	public BsRvo execRegInfo(BsCvo bsCvo, BsRvo bsRvo) {
		Member m = AiwbUtils.getLoginedMember();
		if(!Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new AncarExtend(false, "已经登录"));
			return bsRvo;
		}
		
		PwdResetCvoAndRvo pwdResetCvo = bsCvo.getBody();
		
		if(Judgment.nullOrBlank(pwdResetCvo.getUsername()) && Judgment.nullOrBlank(pwdResetCvo.getCheckMobile())){
			bsRvo.setExtend(new AncarExtend(false, "请输入用户名或注册手机!"));
			return bsRvo;
		}
		
		m = new Member();
		if(!Judgment.nullOrBlank(pwdResetCvo.getUsername())){
			m.setUsername(pwdResetCvo.getUsername());
		}else if(!Judgment.nullOrBlank(pwdResetCvo.getCheckMobile())){
			m.setMobile(pwdResetCvo.getCheckMobile());
		}
		m = ContainerFactory.getSession().pickUp(m);
		
		if(Judgment.nullOrBlank(m.getId())){
			bsRvo.setExtend(new AncarExtend(false, "未查到注册的用户名或手机号"));
			return bsRvo;
		}
		
		PwdResetCvoAndRvo pwdResetRvo = new PwdResetCvoAndRvo();
		pwdResetRvo.setCheckMobile(m.getMobile());
		bsRvo.setBody(pwdResetRvo);
		bsRvo.setExtend(new AncarExtend(true, "手机验证通过"));
		return bsRvo;
	}
	
	
	@BsAnnotation(bsCvoBodyClass=LoginCvo.class,bsRvoBodyClass=LoginRvo.class,
			bsCvoExtendClass=LoginExtend.class,bsRvoExtendClass=LoginExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		AiwbUtils.setSessionMember(null);//置空
		LoginCvo loginCvo = bsCvo.getBody();
		final String username = loginCvo.getUsername();
		boolean isTelPhone=Pattern.matches("^[0-9]*$",username); 
		
		//请求信息为空判断
		if(Judgment.nullOrBlank(username) || Judgment.nullOrBlank(loginCvo.getPassword())){
			//登录日志
			String msg = "登录失败，用户名/密码错误";
			AiwbUtils.saveLoginLog(username,Flag.N,msg );
			bsRvo.setExtend(new LoginExtend(false, msg));
			return bsRvo;
		}
		
		Member member = new Member();//TODO 根据用户名数据库查询还是根据用户名查询后校验密码
		if(isTelPhone){
			//登录信息校验
			member.setMobile(username);
		}else{
			member.setUsername(username);
		}
		
//		member.setPassword(loginCvo.getPassword());
		member = ContainerFactory.getSession().pickUp(member);
		if(Judgment.nullOrBlank(member.getId())){
			String msg = "登录失败，用户名不存在或密码错误";
			AiwbUtils.saveLoginLog(username,Flag.N,msg );
			bsRvo.setExtend(new LoginExtend(false, msg));
			return bsRvo;
		}
		if(!RSAUtils11.decrypt(AiwbConsts.Rsa_PrivateKeyPath, member.getPassword()).equals(
				RSAUtils11.decrypt(AiwbConsts.Rsa_PrivateKeyPath, loginCvo.getPassword()))){
			String msg = "登录失败，用户名/密码错误";
			AiwbUtils.saveLoginLog(username,Flag.N,msg );
			bsRvo.setExtend(new LoginExtend(false, msg));
			return bsRvo;
		}
		
		LoginRvo loginRvo = new LoginRvo();
		loginRvo.setUserId(member.getId());
		loginRvo.setUsername(member.getUsername());
		loginRvo.setNickname(member.getNickname());
		bsRvo.setBody(loginRvo);
		bsRvo.setExtend(new LoginExtend(true,"登录成功!"));
		
		//登录获取积分——每天登录获取一次
		DbRvo  dbRvo = ContainerFactory.getSession().query(new DbCvo<LoginLog>(LoginLog.class){
			private static final long serialVersionUID = 3311942264152232470L;
			@Override
			public void initRules(LoginLog t) {
				setDoCount(true);setDoPaging(true);setRowCount(1);setPagination(1);
				addRule(t.getUsername(), WhereRuleOper.eq, username);
				addRule(t.getCreateTime(), WhereRuleOper.cn, DateHelper.getNow(DateFormat_date));
			}
		});
		if(dbRvo.getRecords()==0){
			AncarConfig.saveIntegral(member, IntegralWay.Login, "登录的奖励");//要求赠送
		}
		
		AiwbUtils.saveLoginLog(username,Flag.Y,"SUCCESS");
		
		//保存当前登录成功的用户至session回话中
		AiwbUtils.setSessionMember(member);
		return bsRvo;
	}
	
	@BsAnnotation(bsCvoBodyClass=LoginCvo.class,bsRvoBodyClass=LoginRvo.class,
			bsCvoExtendClass=LoginExtend.class,bsRvoExtendClass=LoginExtend.class)
	public BsRvo loginOfWxId(BsCvo bsCvo, BsRvo bsRvo) {
		
		Member m = AiwbUtils.getLoginedMember();
		if(!Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new AncarExtend(false, "已经登录"));
			return bsRvo;
		}
		LoginCvo loginCvo = bsCvo.getBody();
		String wxCode = loginCvo.getWxCode();
		Member member = new Member();
		member.setWxCode(wxCode);
		member = ContainerFactory.getSession().pickUp(member);
		if(!Judgment.nullOrBlank(member.getId())){
			LoginRvo loginRvo = new LoginRvo();
			loginRvo.setUserId(member.getId());
			loginRvo.setUsername(member.getUsername());
			loginRvo.setNickname(member.getNickname());
			bsRvo.setBody(loginRvo);
			bsRvo.setExtend(new LoginExtend(true,"登录成功!"));
			AiwbUtils.saveLoginLog(member.getUsername(),Flag.Y,"SUCCESS");
			AiwbUtils.setSessionMember(member);
		}else{
			String msg = "登录失败";
			AiwbUtils.saveLoginLog(member.getUsername(),Flag.N,msg );
			bsRvo.setExtend(new LoginExtend(false, msg));
			return bsRvo;
		}
		
		//保存当前登录成功的用户至session回话中
		return bsRvo;
	}
	
	@BsAnnotation(bsCvoBodyClass=LoginCvo.class,bsRvoBodyClass=LoginRvo.class,
			bsCvoExtendClass=LoginExtend.class,bsRvoExtendClass=LoginExtend.class)
	public BsRvo bindingOfWxId(BsCvo bsCvo, BsRvo bsRvo) {
		
		Member m = AiwbUtils.getLoginedMember();
		if(!Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new AncarExtend(false, "已经登录"));
			return bsRvo;
		}
		LoginCvo loginCvo = bsCvo.getBody();
		String wxCode = loginCvo.getWxCode();
		String username=loginCvo.getUsername();
		String password=loginCvo.getPassword();
		Member member = new Member();
		member.setWxCode(wxCode);
		member = ContainerFactory.getSession().pickUp(member);
		if(Judgment.nullOrBlank(member.getId())){
			boolean isTelPhone=Pattern.matches("^[0-9]*$",username);
			Member member1 = new Member();//TODO 根据用户名数据库查询还是根据用户名查询后校验密码
			if(isTelPhone){
				member1.setMobile(username);
			}else{
				member1.setUsername(username);
			}
			member1.setPassword(password);
			Member member2 = new Member();
			member2 = ContainerFactory.getSession().pickUp(member1);
			if(Judgment.nullOrBlank(member2.getId())){
				String msg = "绑定失败，用户名不存在或密码错误";
				bsRvo.setExtend(new LoginExtend(false, msg));
				return bsRvo;
			}else{
				if(Judgment.nullOrBlank(member2.getWxCode())){
					member2.setWxCode(wxCode);
					ContainerFactory.getSession().store(member2);
					bsRvo.setExtend(new LoginExtend(true,"绑定成功!"));
				}else{
					String msg = "绑定失败，该账户已绑定微信号";
					bsRvo.setExtend(new LoginExtend(false, msg));
				}
			}
		}else{
			String msg = "该微信号已经绑定过其他用户";
			bsRvo.setExtend(new LoginExtend(false, msg));
			return bsRvo;
		}
		//保存当前登录成功的用户至session回话中
		return bsRvo;
	}
}
