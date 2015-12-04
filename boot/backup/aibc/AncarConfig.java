package zbh.aibc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anbox.aibc.integral.LoginIntegralSaver;
import anbox.aibc.integral.QuotePriceIntegralSaver;
import anbox.aibc.integral.RegisterIntegralSaver;
import anbox.aibc.integral.SignOrderIntegralSaver;
import zbh.aibc.integral.SpreadIntegralSaver;
import anbox.aibc.model.config.IntegralConfig;
import anbox.aibc.model.member.InvitationCode;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.member.PhoneCheckCode;
import anbox.aibc.model.product.StockKeepingUnit;
import zbh.aibc.integral.SpreadIntegralSaver;
import zbh.com.qqbx.cmsp.sms.xmlbean.ReqShortMessage;
import zbh.smstp.smsws.xmlbean.ReqShortMsg;
import zbh.smstp.smsws.xmlbean.Request;
import zbh.smstp.smsws.xmlbean.RequestBody;
import zbh.smstp.smsws.xmlbean.RequestHead;
import cn.remex.core.RemexRefreshable;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.net.http.HttpHelper;
import zbh.remex.soa.client.SoaClient;
import zbh.remex.util.DateHelper;
import zbh.remex.util.Judgment;
import zbh.remex.util.XmlHelper;

public class AncarConfig implements AiwbConsts,RemexRefreshable{
	
	/**积分配置*/
	public static Map<String ,IntegralConfig> integralConfigMap = new HashMap<String, IntegralConfig>();
	/**SKU配置*/
	public static Map<String ,StockKeepingUnit> skuMap = new HashMap<String, StockKeepingUnit>();
	
	/**获取积分比例,主要用于签单
	 * @param integralWay 积分途径
	 * @param cityCode 城市
	 * @param skuCode
	 * @param integralType 积分类型
	 * @return
	 */
	public static double obtainIntegralRate(final String skuCode, final IntegralWay integralWay, final IntegralType integralType){//TODO transChannel
		String key = (integralWay+skuCode+integralType).hashCode()+"";
		IntegralConfig ic = integralConfigMap.get(key);
		if(null == ic){
			ic = new IntegralConfig();
			ic.setSkuCode(skuCode);
			ic.setIntegralType(integralType);
			ic = ContainerFactory.getSession().pickUp(ic);
			if(Judgment.nullOrBlank(ic.getId())){
				AiwbUtils.throwException("EI0001","未查到符合条件的积分配置，检索为："+integralWay+","+skuCode+","+integralType);
			}
			integralConfigMap.put(key, ic);
		}
		return ic.getRate();
	}
	
	/**获取积分数-用于询价、注册、登录、推广等场景
	 *
	 * @param integralWay 积分途径 {@link IntegralWay}
	 * @param integralType 积分类型 coin/bean  {@link IntegralType}
	 * @return
	 * 
	 * <br>
	 *  TODO 渠道和类型在InteralConfig中的配置需要通过RefCode数据声明，连接到数据字典中。
	 */
	public static int obtainIntegralCount(IntegralWay integralWay, IntegralType integralType){
		String key = (integralWay+""+integralType).hashCode()+"";
		IntegralConfig ic = integralConfigMap.get(key);
		if(null == ic){
			ic = new IntegralConfig();
			ic.setIntegralWay(integralWay);
			ic.setIntegralType(integralType);
			ic = ContainerFactory.getSession().pickUp(ic);
			if(Judgment.nullOrBlank(ic.getId())){
				AiwbUtils.throwException("EI0001","未查到符合条件的积分amount，检索为："+integralWay+","+integralType);
			}
			integralConfigMap.put(key, ic);
		}
		return ic.getAmount();
	}
	
	public static String obtainSKUCode(final String insuCom){
		return obtainSKU(insuCom).getCode();
	}
	
	/**
	 * @param insuCom
	 * @param ptype 产品类型
	 * @param stype sku类型
	 * @return
	 */
	public static StockKeepingUnit obtainSKU(final String insuCom){
		String key = insuCom.hashCode()+"";
		StockKeepingUnit sku = skuMap.get(key);
		if(null == sku){
			String sql="select s.* from \"StockKeepingUnit\" s  left join \"Product\" p on s.\"product\" = p.\"id\"  where p.\"brand\"=:brand ";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("brand", insuCom);
			DbRvo dbRvo = ContainerFactory.getSession().executeQuery(sql, map);
			List<StockKeepingUnit> skus = dbRvo.obtainBeans(StockKeepingUnit.class);
			if(1!=skus.size()){
				AiwbUtils.throwException("ES0001","未查到符合条件的产品或存在多条数据，查询产品编码为："+insuCom);
			}
			skuMap.put(key, sku=skus.get(0));
		}
		return sku;
	}
	
	public void refresh() {
		integralConfigMap = new HashMap<String, IntegralConfig>();
		//获取积分配置
		String sql = "select * from  \"IntegralConfig\"";
		List<IntegralConfig> integralConfigs =  ContainerFactory.getSession().executeQuery(sql,new HashMap<String, Object>()).obtainBeans(IntegralConfig.class);
//		List<IntegralConfig> integralConfigs = ContainerFactory.getSession().query(new DbCvo<IntegralConfig>(IntegralConfig.class){
//			private static final long serialVersionUID = 1178563205261831603L;
//			@Override
//			public void initRules(IntegralConfig t) {
//				setRowCount(500);
//				setDataType("bd");
//			}
//		}).obtainBeans();
		for(IntegralConfig ic:integralConfigs){
			integralConfigMap.put((ic.getIntegralWay()+ic.getSkuCode()+ic.getIntegralType()).hashCode()+"",ic);
		}
		
		//获取产品
		skuMap = new HashMap<String, StockKeepingUnit>();
		String sql1 = "select * from  \"StockKeepingUnit\" ";
		List<StockKeepingUnit> skus  =  ContainerFactory.getSession().executeQuery(sql1,new HashMap<String, Object>()).obtainBeans(StockKeepingUnit.class);
//		List<StockKeepingUnit> skus = ContainerFactory.getSession().query(new DbCvo<StockKeepingUnit>(StockKeepingUnit.class){
//			private static final long serialVersionUID = 1178563205261831603L;
//			@Override
//			public void initRules(StockKeepingUnit t) {
//				setRowCount(500);
//				setDataType("bd");
//			}
//		}).obtainBeans();
		for(StockKeepingUnit sku:skus){
			skuMap.put(sku.getCode(),sku);
		}
	}
	
//public static void  saveSignedOrderIntegral(ProductOrder productOrder, ProductOrderDetail productOrderDetail){
//	int integralCount = AncarConfig.obtainIntegralCount(productOrderDetail.getOrderPremium()+"",
//			IntegralWay.SignOrder.toString(),productOrderDetail.getSkuCode(),AiwbConsts.IntegralType.Coin.toString());
//	Member m = productOrder.getMember();
//	if(null==m){
//		//TODO 异常不能抛出，记录到系统的报警日志中
//		//AitpAssert.throwAitpException(DataErrorCode.DC002,"未查到订单所属代理人");
//	}
//	//存总数
//	int bic = m.getIntegralCount();
//	m.setIntegralCount(m.getIntegralCount()+integralCount);
//	int aic = m.getIntegralCount();
//	//存日志 //TODO　获取积分的具体算法应该记录
//	IntegralCoinDetail integralDetail = new IntegralCoinDetail();
//	integralDetail.setType(IntegralType.Coin);//卡币
//	integralDetail.setMemberId(m.getId());
//	integralDetail.setMemberUsername(m.getUsername());
//	integralDetail.setWay(IntegralWay.SignOrder);
//	integralDetail.setWayDesc("签单");
//	integralDetail.setAmout(String.valueOf(integralCount));//此处一定为正数
//	integralDetail.setBeforeBalance(String.valueOf(bic));
//	integralDetail.setAfterBalance(String.valueOf(aic));
//	integralDetail.setSettleTime(DateHelper.getNow());
//	integralDetail.setDesc(productOrderDetail.getOrderDetailDesc());
//	ContainerFactory.getSession().store(integralDetail);
//	ContainerFactory.getSession().store(m,"coinCount");
//}


	/**记录积分详情
	 * @param me 会员
	 * @param amount 本次积分数，添加为+，减少为-
	 * @param way 积分获取途径
	 * @param type 积分类型，卡币/卡豆
	 */
	public static void  saveIntegral(Member me, IntegralWay way,Object... params){
		HashMap<String, Object> map = new HashMap<String, Object>();
		int i=0;
		for(Object o:params){
			map.put(""+(i++), o);
		}
		
		saveIntegral(me, way, map);
	}
	public static void  saveIntegral(Member me, IntegralWay way,Map<String,Object> params){
		if(null==me || Judgment.nullOrBlank(me.getId())){
			//TODO 异常不能抛出，记录到系统的报警日志中
			AiwbUtils.throwException("EI0001","未获取到有效的积分所属代理人信息");
		}
		switch (way) {
		case QuotePrice:
			new QuotePriceIntegralSaver().saveIntegral(me, params);
			break;
		case Login:
			new LoginIntegralSaver().saveIntegral(me, params);
			break;
		case SignOrder:
			new SignOrderIntegralSaver().saveIntegral(me, params);
			break;
		case Register:
			new RegisterIntegralSaver().saveIntegral(me, params);
			break;
		case Spread:
			new SpreadIntegralSaver().saveIntegral(me, params);
			break;

		default:
			break;
		}
	}
	public static String createPhoneCheckToken(final String phone) {
		String code = RandomUtils.createRandomCode(PhoneCheckCodeLen, false);// true表示包含字母
		String token = (code.hashCode() + "secret" + phone).hashCode() + "";
		final String s = "【安卡车险】您会员注册验证码" + code + ",有效时间5分钟。如非本人操作请忽略。联系电话010-51271768。";

//		new Thread(new Runnable() {
//			public void run() {
//				ReqShortMessage reqShortMsg = new ReqShortMessage();
//				reqShortMsg.setPhone(phone);
//				reqShortMsg.setMsgContent(s);
//				reqShortMsg.setTransChannel("ANCAR");
//				try{
//					SoaClient.invokeService("remex:soa://ShortMessageBs:execute", reqShortMsg, null);
//				}catch (Exception e) {
//					logger.error("提示异常信息没有转换的邮件发送服务异常！",e);
//				}
//			}
//		}).start();

		
//短信发送方式修改为安卡自己的短信网关
		Request request = new Request();
		RequestHead reqHead = new RequestHead();
		RequestBody reqBody = new RequestBody();
		request.setHead(reqHead);
		ReqShortMsg reqShortMsg = new ReqShortMsg();
		reqShortMsg.setPhoneNo(phone);
		reqShortMsg.setMsgContent(s);
		reqBody.setReqShortMsg(reqShortMsg);
		request.setBody(reqBody);
		String reqXml = XmlHelper.marshall(request);
		System.out.println(reqXml);
		// AiwbUtils.invokeService("SendShortMsgBs", "execute",
		// reqShortMsg,null,"ZHDX");
		HttpHelper.sendXml("http://10.16.166.80:7005/Smsp/remex/RemexHttpXmlAction.action?bs=SendShortMsgBs", reqXml);
//		
//		

		PhoneCheckCode phoneCheckCode = new PhoneCheckCode();
		phoneCheckCode.setPhoneNo(phone);
		phoneCheckCode.setCode(code);
		phoneCheckCode.setGenerationTime(DateHelper.getNow());
		phoneCheckCode.setStatus(CodeStatus.Effective.toString());
		phoneCheckCode.setValidityPeriod(PhoneCheckValidityPeriod);
		// invitationCode.setInvalidTime(DateHelper.formatDate(DateHelper.getDiffTargetDate(null,ValidityPeriod),DateFormat_time));//等待core更新
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, PhoneCheckValidityPeriod);
		phoneCheckCode.setInvalidTime(DateHelper.formatDate(cal.getTime(), DateFormat_time));
		ContainerFactory.getSession().store(phoneCheckCode);
		return token;
	}
	public static boolean checkPhoneCheckToken(String phone, String code, String token) {
		PhoneCheckCode checkCode = new PhoneCheckCode();
		checkCode.setCode(code);
		checkCode.setPhoneNo(phone);
		List<PhoneCheckCode> pccs = ContainerFactory.getSession().list(checkCode);
		PhoneCheckCode checkCode2 = null;
		if(pccs.size()>0){
			checkCode2 = pccs.get(0);
		}
		//如果数据库中不存在或者有效期超过指定时间5分钟，则失效。
		if (null == checkCode2 || DateHelper.parseDate(checkCode2.getInvalidTime(), DateFormat_time).getTime() < (new Date().getTime())) {
			if(null!=checkCode2){
				checkCode2.setStatus(AiwbConsts.InvitationCodeStatus.Invalid.toString());
				ContainerFactory.getSession().store(checkCode,"status");
			}
			return false;
		}

		checkCode2.setStatus(AiwbConsts.InvitationCodeStatus.Used.toString());
		ContainerFactory.getSession().store(checkCode,"status");
		//验证数据token有效性
		String token1 = (code.hashCode() + "secret" + phone).hashCode() + "";
		return token1.equals(token);
	}

	/**
	 * @param member 注册用户的信息，尚未保存到数据库中
	 * @param code 邀请码
	 * @param registerType 邀请类型
	 * @return null 如果邀请无效则返回null，有效则返回对应的上线。
	 * <br>
	 * 除了邀请码的状态更新以外，其他数据库保存操作不适合放在此处。
	 * <br>
	 * 邀请码状态的更新含义为，凡是使用过（可能注册不成功）均失效。（有待讨论：失效条件、邀请码重复问题） TODO
	 */
	public static Member checkInviteCode(Member member, final String code, String registerType) {
		// 推广邀请处理
		if (Judgment.nullOrBlank(code) || RegisterType.Normal.toString().equals(registerType)) {
			return null;
		}
		InvitationCode invitationCode = new InvitationCode();
		invitationCode.setInvitationCode(code);
	// 目前的涉及邀请码可能重复 影响数据库读取条件的设定。 TODO
		DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<InvitationCode>(InvitationCode.class) {
			private static final long serialVersionUID = 7668907569692101528L;
			@Override
			public void initRules(InvitationCode t) {
				addRule(t.getInvitationCode(), WhereRuleOper.eq, code);
				addRule(t.getStatus(), WhereRuleOper.eq, InvitationCodeStatus.Effective.toString());
			}
		});

		// 目前的涉及邀请码可能重复 TODO
		if (dbRvo.getRecordCount() == 1) {
			invitationCode = ((InvitationCode) dbRvo.obtainBeans().get(0));

			if (DateHelper.parseDate(invitationCode.getInvalidTime()).getTime() < (new Date().getTime())) {
				// 过期设置为失效
				invitationCode.setStatus(InvitationCodeStatus.Invalid.toString());
				ContainerFactory.getSession().store(invitationCode,"status");
				return null;
			}

			Member belongedMmb = invitationCode.getMember();
			if (null == belongedMmb) {
				// 没有匹配上用户，无效
				return null;
			}

			//匹配上，则更新保存状态
//			invitationCode.setStatus(InvitationCodeStatus.Used.toString());
//			ContainerFactory.getSession().store(invitationCode,"status");
			return belongedMmb;
		} else {
			// 数据问题
			return null;
			// bsRvo.setExtend(new LoginExtend(false,TipsMsg_invalidCode));
		}
	}
}
