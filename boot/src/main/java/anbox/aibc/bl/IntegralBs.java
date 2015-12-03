package anbox.aibc.bl;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.AncarConfig;
import anbox.aibc.appBeans.login.LoginExtend;
import anbox.aibc.appBeans.mmbCenter.Cashout;
import anbox.aibc.appBeans.mmbCenter.CashoutRvo;
import anbox.aibc.appBeans.mmbCenter.IntegralCvo;
import anbox.aibc.appBeans.mmbCenter.IntegralRvo;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.quotation.QuotationExtend;
import anbox.aibc.model.member.CashoutApplication;
import anbox.aibc.model.member.IntegralBeanDetail;
import anbox.aibc.model.member.IntegralCoinDetail;
import anbox.aibc.model.member.Member;
import cn.com.qqbx.aitp.aiws.xmlbeans.cashout.ReqCashout;
import cn.com.qqbx.aitp.aiws.xmlbeans.cashout.ResCashout;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.db.sql.SqlBeanWhere;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.DateHelper;
import cn.remex.util.Judgment;

/**
 * 会员积分
 * 
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class IntegralBs implements AiwbConsts {
	/**获取卡币明细
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = MmbCenterCvo.class, bsRvoBodyClass = IntegralRvo.class, bsCvoExtendClass = MemberExtend.class, bsRvoExtendClass = MemberExtend.class)
	public BsRvo execCoin(BsCvo bsCvo, BsRvo bsRvo) {
		// 获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if (null == curMember) {
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		// 获取jsapp请求数据
		final MemberExtend extend = bsCvo.getExtend();
		//查询卡币
		DbRvo coinRvo = ContainerFactory.getSession().query(new DbCvo<IntegralCoinDetail>(IntegralCoinDetail.class) {
			private static final long serialVersionUID = -7217536947099474510L;
			@Override
			public void initRules(IntegralCoinDetail t) {
				setDoCount(true);setDoPaging(true);
				setRowCount(extend.getRowCount() < 20 ? extend.getRowCount() : 20);
				setPagination(extend.getPagination() < 1 ? 0 : extend.getPagination());
				setDataType("bd");
				addRule("" + t.getMemberId(), WhereRuleOper.eq, curMember.getId());
			}
		});
		
		IntegralRvo integralRvo = new IntegralRvo();
		integralRvo.setBeanCount(curMember.getBeanCount()+"");
		integralRvo.setCoinCount(curMember.getCoinCount()+"");
		integralRvo.setCoinDetails(coinRvo.getMapRows());
		bsRvo.setBody(integralRvo);
		extend.setStatus(true);
		extend.setMsg("Coin信息查询完毕");
		extend.setPageCount(AiwbUtils.obtainPageCount(coinRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(coinRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
	
	@BsAnnotation(bsCvoBodyClass = IntegralCvo.class, bsRvoBodyClass = IntegralRvo.class, 
			bsCvoExtendClass = MemberExtend.class, bsRvoExtendClass = MemberExtend.class)
	public BsRvo applyCoinCashout(BsCvo bsCvo, BsRvo bsRvo) {
		// 获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if (null == curMember) {
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		// 获取jsapp请求数据
		final IntegralCvo integralCvo = bsCvo.getBody();
		
		if("BankCard".equals(integralCvo.getTransType())){
			if(Judgment.nullOrBlank(integralCvo.getBankCardNumber()) || Judgment.nullOrBlank(integralCvo.getBankOutlets())){
				bsRvo.setExtend(new MemberExtend(false, "银行卡号、网点不能为空！"));
				return bsRvo;
			}
		}
		if("Alipa".equals(integralCvo.getTransType())){
			if(Judgment.nullOrBlank(integralCvo.getZfbAccount())){
				bsRvo.setExtend(new MemberExtend(false, "支付宝账号不能为空！"));
				return bsRvo;
			}
		}
		
		if (!AncarConfig.checkPhoneCheckToken(integralCvo.getMobile(), integralCvo.getCheckCode(), integralCvo.getPhoneCheckToken())) {
			bsRvo.setExtend(new LoginExtend(false, "手机验证码失效!"));
			return bsRvo;
		}
		//查询卡币
		DbRvo caRvo = ContainerFactory.getSession().query(new DbCvo<CashoutApplication>(CashoutApplication.class) {
			private static final long serialVersionUID = -2159285090832913133L;
			@Override
			public void initRules(CashoutApplication t) {
				setDoCount(true);setDoPaging(true);setRowCount(1);setPagination(1);
				setDataType("bd");
				//当前用户有受理中的，则拒绝。
				addRule("" + t.getApplyMember(), WhereRuleOper.eq, curMember.getId());
				SqlBeanWhere sqlBeanWhere = new SqlBeanWhere();
				addGroup(sqlBeanWhere );
				sqlBeanWhere.setGroupOp(WhereGroupOp.OR);
				sqlBeanWhere.addRule(""+t.getApplyStatus(),WhereRuleOper.eq,ApplyCashoutStatus.Accept.toString());
				sqlBeanWhere.addRule(""+t.getApplyStatus(),WhereRuleOper.eq,ApplyCashoutStatus.Submit.toString());
			}
		});

		if(caRvo.getRecordCount()>0){				//当前用户有受理中的，则拒绝。
			bsRvo.setExtend(new MemberExtend(false, "当前已存在申请提现的申请，请不要重复提交申请！"));
			return bsRvo;
		}
		CashoutApplication ca = new CashoutApplication();
		ReflectUtil.copyProperties(ca,integralCvo);
		ca.setApplyMember(curMember.getId());
		ca.setApplyStatus(ApplyCashoutStatus.Submit);
		ca.setApplyTime(DateHelper.getNow());
		ca.setBankCardNumber(integralCvo.getBankCardNumber());
		ca.setTransCashAmount(integralCvo.getAmountOfMoney());
		ca.setBankOutlets(integralCvo.getBankOutlets());
		ContainerFactory.getSession().store(ca);
		//发送业管系统
		ReqCashout reqCashout = new ReqCashout();
		ReflectUtil.copyProperties(reqCashout, integralCvo);
		reqCashout.setCashoutId(ca.getId());
		reqCashout.setApplyMember(curMember.getUsername());
		reqCashout.setApplyTime(DateHelper.getNow());
		reqCashout.setTransCashAmount(integralCvo.getAmountOfMoney());
		reqCashout.setApplyStatus("N"); //
		
		ResCashout resCashout = new ResCashout();
		try {
			resCashout = (ResCashout)AiwbUtils.invokeService("AitpCashoutBs", "execute", reqCashout, null, "ZHDX","");
		} catch (Exception e) {
			bsRvo.setExtend(new QuotationExtend(false, AiwbUtils.transErrorMsg(e.getMessage())));			// TODO: handle exception
		}
		
		
		bsRvo.setExtend(new MemberExtend(true, "提现申请已提交，我们将于10个工作日处理完毕！"));
		return bsRvo;
	}
	
	/**获取卡豆明细
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = MmbCenterCvo.class, bsRvoBodyClass = IntegralRvo.class, bsCvoExtendClass = MemberExtend.class, bsRvoExtendClass = MemberExtend.class)
	public BsRvo execBean(BsCvo bsCvo, BsRvo bsRvo) {
		// 获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if (null == curMember) {
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		// 获取jsapp请求数据
		final MemberExtend extend = bsCvo.getExtend();
		//查询卡币
		DbRvo beanRvo = ContainerFactory.getSession().query(new DbCvo<IntegralBeanDetail>(IntegralBeanDetail.class) {
			private static final long serialVersionUID = -7217536947099474510L;
			@Override
			public void initRules(IntegralBeanDetail t) {
				setDoCount(true);setDoPaging(true);
				setRowCount(extend.getRowCount() < 20 ? extend.getRowCount() : 20);
				setPagination(extend.getPagination() < 1 ? 0 : extend.getPagination());
				setDataType("bd");
				addRule("" + t.getMemberId(), WhereRuleOper.eq, curMember.getId());
			}
		});
		
		IntegralRvo integralRvo = new IntegralRvo();
		integralRvo.setBeanCount(curMember.getBeanCount()+"");
		integralRvo.setCoinCount(curMember.getCoinCount()+"");
		integralRvo.setBeanDetails(beanRvo.getMapRows());
		bsRvo.setBody(integralRvo);
		extend.setStatus(true);
		extend.setMsg("Coin信息查询完毕");
		extend.setPageCount(AiwbUtils.obtainPageCount(beanRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(beanRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
	/**提现明细明细
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	@BsAnnotation(bsCvoBodyClass = MmbCenterCvo.class, bsRvoBodyClass = CashoutRvo.class, bsCvoExtendClass = MemberExtend.class, bsRvoExtendClass = MemberExtend.class)
	public BsRvo execCash(BsCvo bsCvo, BsRvo bsRvo) {
		// 获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if (null == curMember) {
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		// 获取jsapp请求数据
		final MemberExtend extend = bsCvo.getExtend();
		//查询卡币
		DbRvo cashRvo = ContainerFactory.getSession().query(new DbCvo<CashoutApplication>(CashoutApplication.class) {
			private static final long serialVersionUID = -7217536947099474510L;
			@Override
			public void initRules(CashoutApplication t) {
				setDoCount(true);setDoPaging(true);
				setRowCount(extend.getRowCount() < 20 ? extend.getRowCount() : 20);
				setPagination(extend.getPagination() < 1 ? 0 : extend.getPagination());
				setDataType("bd");
				addRule("" + t.getApplyMember(), WhereRuleOper.eq, curMember.getId());
			}
		});
		List<CashoutApplication> cashoutApplications = cashRvo.obtainBeans();
		List<Cashout> cashouts = new ArrayList<Cashout>();
		for(CashoutApplication cashoutApplication : cashoutApplications){
			Cashout cashout = new Cashout();
			ReflectUtil.copyProperties(cashout,cashoutApplication);
			cashouts.add(cashout);
		}
		CashoutRvo cashoutRvo = new CashoutRvo();
		cashoutRvo.setCashouts(cashouts);
		bsRvo.setBody(cashoutRvo);
		extend.setStatus(true);
		extend.setMsg("Coin信息查询完毕");
		extend.setPageCount(AiwbUtils.obtainPageCount(cashRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(cashRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
	
	public BsRvo cashoutFinish(BsCvo bsCvo, BsRvo bsRvo) {
		ReqCashout reqCashout = bsCvo.getBody(ReqCashout.class);
		String id = reqCashout.getCashoutId();
		String applyStatus = reqCashout.getApplyStatus();
		CashoutApplication cashoutApplication = ContainerFactory.getSession().queryBeanById(CashoutApplication.class,id);
		if("Y".equals(applyStatus)){
			cashoutApplication.setApplyStatus(ApplyCashoutStatus.Finished);
		}else{
			cashoutApplication.setApplyStatus(ApplyCashoutStatus.Valid);
		}
		ContainerFactory.getSession().store(cashoutApplication);
		return bsRvo;
		
	}
}
