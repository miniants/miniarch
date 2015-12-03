package anbox.aibc;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;

import anbox.aibc.appBeans.quotation.QuotePriceCvo;
import anbox.aibc.model.member.Member;
import anbox.aibc.model.quotation.AtinQuotation;
import anbox.aibc.model.system.LoginLog;
import anbox.aibc.model.vehicle.Vehicle;
import cn.com.qqbx.aitp.AitpAssert;
import cn.com.qqbx.aitp.ErrorCodeEnum;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ReqCoverageInfo;
import cn.com.qqbx.cmsp.email.xmlbean.ReqEmail;
import cn.remex.bs.BsRvo;
import cn.remex.bs.Extend;
import cn.remex.bs.Head;
import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.model.sys.SysSerialNumber;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.quartz.SchedulerHandler;
import cn.remex.reflect.ReflectUtil;
import cn.remex.soa.client.SoaClient;
import cn.remex.util.Arith;
import cn.remex.util.DateHelper;
import cn.remex.util.FileHelper;
import cn.remex.util.Judgment;

/**
 * @author zhangaiguo
 *@since 2015-01-08
 */
public class AiwbUtils implements AiwbConsts{
	public static void throwException(String errorCode,String errorMsg){
		throw new AibcException(errorMsg, errorMsg);
	}
	public static Object invokeService(String serviceName,String transType, Object reqBody,Extend extend,String transChannel,String flowNo){
		String userId = getSessionMemberId();
//		SIPubTools.saveXMLFile("Request", SIConst.transType_saveOnlineCustomer, ("MSDX".equals(transChannel)?"A00001":"A00002"),	"a", XmlHelper.objectToXml(reqBody), transNo);
    	//FileHelper.saveFileContent("D:\\req_111"+transType+".xml", cn.remex.util.XmlHelper.marshall(reqBody));
		BsRvo rvo;
		try{
			Head reqHead = new Head();
			reqHead.setTransChannel(transChannel);
			reqHead.setChannelUser("aaaa");
			reqHead.setChannelUser("admin");
			reqHead.setChannelPwd("admin");
			reqHead.setChannelOper("aaaa");
			reqHead.setFlowNo(flowNo);
			reqHead.setTransType(transType);
//			AiwbUtils.saveXmlFile();
			rvo = SoaClient.invokeService("remex:soa://" + serviceName + ":execute", reqHead, reqBody, extend,null,0);
//			AiwbUtils.saveXmlile();

		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		if (!rvo.getExtend().isStatus()) {
			throw new RuntimeException(Judgment.nullOrBlank(rvo.getExtend().getMsg()) ?"SOA服务异常，返回空异常信息!":"SOA服务异常，异常信息："+rvo.getExtend().getMsg());
		}
		//TODO 判断异常信息——rvo.getExtend().getErrorCode(), rvo.getExtend().getErrorMsg()
		
		return rvo.getBody();
	}
	
	public static List<ReqCoverageInfo> obtainCoverages(String packageType, QuotePriceCvo priceCvo){
		if(packageType.equals(PackageType.Optional)){
			return  null;
		}
		List<ReqCoverageInfo> cvgs = new ArrayList<ReqCoverageInfo>();
		if((PackageType.Economical.toString()+","+PackageType.Luxury.toString()).contains(packageType)){
			ReqCoverageInfo coverage_BZ = new ReqCoverageInfo();//车损
			coverage_BZ.setCoverageCode(CoverageCode.BZ.toString());
			coverage_BZ.setCoverageName("交强险");
			coverage_BZ.setPrdtKindCode(PrdtKind_TCI);
			cvgs.add(coverage_BZ);
			
			ReqCoverageInfo coverage_A = new ReqCoverageInfo();//车损
			coverage_A.setAmount(priceCvo.getVehicleInfo().getPurchasePrice()+"");
			coverage_A.setCoverageCode(CoverageCode.A.toString());
			coverage_A.setCoverageName("车辆损失险");
			coverage_A.setDeductibleFlag("Y");
			coverage_A.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_A);
			
			ReqCoverageInfo coverage_B = new ReqCoverageInfo();//三者
			coverage_B.setAmount("200000");
			coverage_B.setCoverageCode(CoverageCode.B.toString());
			coverage_B.setCoverageName("三者责任险");
			coverage_B.setDeductibleFlag("Y");
			coverage_B.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_B);
			
			ReqCoverageInfo coverage_G1 = new ReqCoverageInfo();//盗抢
			coverage_G1.setAmount(priceCvo.getActualValue());
			coverage_G1.setCoverageCode(CoverageCode.G1.toString());
			coverage_G1.setCoverageName("盗抢险");
			coverage_G1.setDeductibleFlag("Y");
			coverage_G1.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_G1);
		}
		
		if(packageType.equals(PackageType.Luxury)){
			ReqCoverageInfo coverage_D3 = new ReqCoverageInfo();//司机
			coverage_D3.setAmount("10000");
			coverage_D3.setCoverageCode(CoverageCode.D3.toString());
			coverage_D3.setCoverageName("车上人员(司机)险");
			coverage_D3.setDeductibleFlag("Y");
			coverage_D3.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_D3);
			
			ReqCoverageInfo coverage_D4 = new ReqCoverageInfo();//乘客
			coverage_D4.setAmount(Arith.mul("10000", Arith.sub(priceCvo.getVehicleInfo().getSeatCount(), 1))+"");
			coverage_D4.setUnitAmount("10000");
			coverage_D4.setCoverageCode(CoverageCode.D4.toString());
			coverage_D4.setCoverageName("车上人员(乘客)险");
			coverage_D4.setDeductibleFlag("Y");
			coverage_D4.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_D4);
			
			
			ReqCoverageInfo coverage_F = new ReqCoverageInfo();//玻璃
//		coverage_F.setModeCode(priceCvo.getVehicleInfo().getImportFlag()); TODO
			coverage_F.setModeCode("1");
			coverage_F.setModeName("国产玻璃");
			coverage_F.setCoverageCode(CoverageCode.F.toString());
			coverage_F.setCoverageName("玻璃险");
			coverage_F.setDeductibleFlag("Y");
			coverage_F.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_F);
			
			ReqCoverageInfo coverage_L = new ReqCoverageInfo();//划痕
			coverage_L.setAmount("2000");
			coverage_L.setCoverageCode(CoverageCode.L.toString());
			coverage_L.setCoverageName("划痕险");
			coverage_L.setDeductibleFlag("Y");
			coverage_L.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_F);
			
			ReqCoverageInfo coverage_Z = new ReqCoverageInfo();//自燃
			coverage_Z.setAmount(priceCvo.getActualValue());
			coverage_Z.setCoverageCode(CoverageCode.L.toString());
			coverage_Z.setCoverageName("自燃险");
			coverage_Z.setDeductibleFlag("Y");
			coverage_Z.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_Z);
			
			ReqCoverageInfo coverage_X1 = new ReqCoverageInfo();//涉水
//		coverage_X1.setAmount(priceCvo.getActualValue());
			coverage_X1.setCoverageCode(CoverageCode.X1.toString());
			coverage_X1.setCoverageName("发动机特别损失险");
			coverage_X1.setDeductibleFlag("Y");
			coverage_X1.setPrdtKindCode(PrdtKind_VCI);
			cvgs.add(coverage_X1);
			
		}
		ReqCoverageInfo coverage_M = new ReqCoverageInfo();//不计免赔
		coverage_M.setCoverageCode(CoverageCode.M.toString());
		coverage_M.setCoverageName("不计免赔");
		coverage_M.setPrdtKindCode(PrdtKind_VCI);
		cvgs.add(coverage_M);
		return cvgs;
	}
	
	/**
	 * 保存xml报文至本地资源库
	 * @param transDirection 交易方向：请求或响应
	 * @param TransType		交易类型
	 * @param curOperator	系统当前操作者
	 * @param transNo			交易流水号
	 * @param XMLObject		要保存的对象String switchDirection, String switchType,
			String comCode, String curOperator, Object XMLObject,
	 */
	public static void saveXmlFile(String insuCom, String mangCom,String transType,String transDirection, String transNo, String Oper, Object XMLObject,String charset) {
		final String curSystem = System.getProperty("os.name");
		String baseXmlFilePath = "";
		if (curSystem.toUpperCase().startsWith("WIN")) {
			baseXmlFilePath = XmlFilePath_Win;
		}
		// LINUX 无盘符定义文件层级关系
		else if (curSystem.toUpperCase().startsWith("LINUX")) {
			baseXmlFilePath = XmlFilePath_Linux;
		}
//		String insuComName = AitpConfig.obtainConfigValue(STR_ConfClass_Aitp, STR_InsuComXmlPath, aitpCommParam.getInsuCom());
		String xmlFilePath = baseXmlFilePath + File.separatorChar 
				+ insuCom +File.separatorChar								//保险公司
				+ mangCom +File.separatorChar								//管理机构
				+ DateHelper.getNow("yyyyMMdd") +File.separatorChar								//管理机构日期
				+ Oper + "-"							//系统操作员
				+ transType + "-"								//业务交易类型
				+ transDirection + "-"						//交易方向：request、response
				+ (Judgment.nullOrBlank(transNo)? "TransNoMissing":transNo)	//15位交易码
				+ ".xml";
		// 创建报文要保存的文件夹及保存文件
		FileHelper.saveFileContent(xmlFilePath,XMLObject,charset);
	}
	
	public static Member checkMember(Object obj){
		Member curMember = new Member();//TODO 获取当前session用户,还是从传入的id获取，两者是否需要对比
		ReflectUtil.invokeSetter("id", curMember, ReflectUtil.invokeGetter("userId", obj));
		ReflectUtil.invokeSetter("username", curMember, ReflectUtil.invokeGetter("username", obj));
//		curMember.setId(mmbCenterCvo.getUserId());
//		curMember.setUsername(mmbCenterCvo.getUsername());
		curMember = ContainerFactory.getSession().pickUp(curMember);
		if(Judgment.nullOrBlank(curMember.getId())){
			return null;
		}
		return curMember;
	}
	
	/**获取登录日志
	 * @param username
	 * @return
	 */
	public static void saveLoginLog(String username,Flag loginStatuFlag,String msg){
		//登录日志记录
		LoginLog log = new LoginLog();
		log.setLogMsg(msg);
		log.setClientIp(AiwbUtils.getClientIp());//获取客户端ip地址
		log.setLoginTime(DateHelper.getNow());
		log.setLoginChannel(TransChannel_web);//登录渠道，微信端/网页端
		log.setUsername(username);
		log.setLoginSuccFlag(loginStatuFlag.toString());//默认没有登录成功
		ContainerFactory.getSession().store(log);//保存登录日志
	}
	
	/**获取客户端登录id
	 * @author zhangaiguo
	 * @return
	 */
	public static String getClientIp(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = request.getRemoteAddr();
		return ip;
	}
	
	/**获取客户端mac地址
	 * @return
	 */
	public static String getClientMac(){
//		HttpServletRequest request = ServletActionContext.getRequest();
		String mac = "";
		return mac;
	}
	
	/**保存session，当注销时member为空
	 * @param member
	 */
	public static void setSessionMember(Member member){
		if(null!=member){
			String name = member.getUsername();
			long now = System.currentTimeMillis();
		//三段-分割的格式
			String st = new String(Base64.encodeBase64(name.getBytes()))
					+"-"+new String(Base64.encodeBase64(String.valueOf(now).getBytes()))
					+"-"+new String(Base64.encodeBase64(String.valueOf(now+name.hashCode()).getBytes()));
			st = st.replaceAll("=", "");
			//根据cookie取，cookie要加密 TODO
			CoreSvo.$SC(AiwbConsts.UID, name);
			CoreSvo.$SC(AiwbConsts.TS, String.valueOf(now));
			CoreSvo.$SC(AiwbConsts.ST, st);
		}else{
			Cookie cUID = new Cookie(AiwbConsts.UID, null);
			cUID.setPath("/");
			cUID.setMaxAge(-1);
			CoreSvo.$SC(cUID);

			Cookie cTS = new Cookie(AiwbConsts.TS, null);
			cTS.setPath("/");
			cTS.setMaxAge(-1);
			CoreSvo.$SC(cTS);

			Cookie cST = new Cookie(AiwbConsts.ST, null);
			cST.setPath("/");
			cST.setMaxAge(-1);
			CoreSvo.$SC(cST);
		}
		//CoreSvo.$SS(UserSessionKey,member);
	}
	
	
	/**获取登录用户
	 * @return
	 */
	public static Member getLoginedMember(){
		Member m = getSessionMember();
		if(null == m || null==m.getId()){
			return null;
		}
		return m;
	}
	
	/**获取session会话
	 * @return
	 */
	public static Member getSessionMember(){
		//根据cookie取，cookie要加密 TODO
		String st = (String) CoreSvo.$VC(AiwbConsts.ST);
		String ts = (String) CoreSvo.$VC(AiwbConsts.TS);
		final String name = (String) CoreSvo.$VC(AiwbConsts.UID);
		if(Judgment.nullOrBlank(st) || Judgment.nullOrBlank(name)){
			return null;
		}
		String[] sts = st.split("-");
		if(sts.length!=3)//三段-分割的格式
			return null;
		//第三段检测合法性
		long sts3 = Long.parseLong(new String(Base64.decodeBase64(sts[2])));
		long tsl = Long.parseLong(ts);
		if(tsl != sts3-name.hashCode()){
			return null;
		}
		
		Member member = new Member();//TODO 根据用户名数据库查询还是根据用户名查询后校验密码
		member.setUsername(name);
		DbRvo rvo = ContainerFactory.getSession().query(new DbCvo<Member>(Member.class){
			private static final long serialVersionUID = 6694568981993841069L;
			@Override
			public void initRules(Member t) {
				addRule(t.getUsername(), WhereRuleOper.eq, name);
				setDataType("bd");
			}
		});
		List<Member> ms = rvo.obtainBeans();
		if(rvo.getRecords()>1){
			AitpAssert.throwAitpException(ErrorCodeEnum.FunctionErrorCode.FO001, "查询到多条用户信息");
		}else if(rvo.getRecords()==1){
			member = ms.get(0);
		}
//		member = ContainerFactory.getSession().pickUp(member);		//(Member) CoreSvo.$VS(UserSessionKey);
		return member; 
	}
	/**获取session会话
	 * @return
	 */
	public static String getSessionMemberId(){
		//根据cookie取，cookie要加密 TODO
		//考虑通过适当方法优化，实际不需要每次都进行解密验证操作。 TODO
		Member m = getSessionMember();
		return m==null?((String) CoreSvo.$VC(AiwbConsts.UID)):m.getUsername(); 
	}
	/**获取session会话
	 * @return
	 */
	public static String getSessionGuestTempId(){
		return RandomUtils.createRandomCode(10, true)+String.format("%1$03d", Integer.valueOf(SysSerialNumber.createSerialNumber(Member.class, "GUESTTEMPID").toString()));
	}

	/**
	 * 如果没有登录会自动添加一个临时用户
	 */
	public static void setSessionGuestTempId() {
		Object uid = CoreSvo.$VC(AiwbConsts.UID);
		if (Judgment.nullOrBlank(uid)) {
			CoreSvo.$SC(AiwbConsts.UID, AiwbUtils.getSessionGuestTempId());
		}
	}

	public static int obtainPageCount(int rd,int rw){
		double pageCouunt=0;
		String res = Arith.div(rd, rw)+"";
		String intPart = res.split("\\.")[0];
		String decPart = res.split("\\.")[1];
		if(decPart.length()==1 && decPart.substring(0,1).equals("0")){
			pageCouunt = Arith.add(intPart, 0);
		}else{
			pageCouunt = Arith.add(intPart, 1);
			
		}
		return (int)pageCouunt;
	}
	
	
	public static String transErrorMsg(String em) {
		final String msg;
		if (em.indexOf("未查到有效的执行机构映射关系") >= 0 || em.indexOf("map为空!，检索为") >= 0) {
			msg = "未开通此地区服务";
		} else if (em.indexOf("保险起期") >= 0 || em.indexOf("交强险投保日距离") >=0|| em.indexOf("生效时间必须晚于系") >=0|| em.indexOf("投保日期不能早于当") >=0 ) {
			msg = "此车未到可投保的时间";
		} else if (em.indexOf("最少投保其中一个基本险") >= 0
				||em.indexOf("险种选择不计免赔率") >= 0
				||em.indexOf("不计免赔特约条款必须投保相应的主险") >= 0
				) {
			msg = "条款选择有误";
		} else if (em.indexOf("已经在其他公司投保了同类型") >= 0) {
			msg = "该车已经投保";
		} else if (em.indexOf("太保链接异常") >= 0 ||em.indexOf("调用后端服务失败") >= 0 || em.indexOf("系统异常，请联系技术人员") >= 0) {
			msg = "保险公司报价服务超时";
		} else if (em.indexOf("投保查询次数") >= 0) {
			msg = "保险公司报价服务次数限制";
		} else if (em.indexOf("请在平台返回的车型列表中选择对应的车型") >= 0) {
			msg = "车型信息不符合该保险公司的复核要求，如需投保请联系客服订制投保方案。";
		} else if (em.indexOf("投保单状态:核保通过 投保单系统来源代码:第三方平台 经") >= 0) {
			msg = "该车已经本公司相关渠道确认投保，目前尚未出单。如是您本人重复提交投保请联系客服进行撤单。";
		} else {
			int idx = em.lastIndexOf("Exception:");
			final String msg1 = em.substring(idx+1);
			new Thread(new Runnable() {
				public void run() {
					ReqEmail reqEmail = new ReqEmail();
					reqEmail.setTos(new String[]{"liuhy@qqbx.com.cn"});
					reqEmail.setContent("ANCAR服务提示，异常信息:"+msg1+"没有经过转换，请确定该异常信息的显示方式与内容后与ANCAR开发人员联系！");
					reqEmail.setTitle("ANCAR服务提示：发现没有转换的异常信息");
					try{
						SoaClient.invokeService("remex:soa://EmailBs:execute", reqEmail, null);
					}catch (Exception e) {
						logger.error("提示异常信息没有转换的邮件发送服务异常！",e);
					}
				}
			}).start();
			
			msg="不符合报价要求。";
		}
		return msg;
	}
	
	/**核保信息转换信息
	 * @param msg 保险公司返回核保信息
	 * @return
	 */
	public static String transRespMsg(String msg){
		if(Judgment.nullOrBlank(msg)){
			return "";
		}
		String resMsg="";
		if(msg.indexOf("该车已经本公司相关渠道确认投保，目前尚未出单")>=0 || msg.indexOf("重复提交投保")>=0){
			resMsg = "该单已经投保";
		}if(msg.indexOf("提供服务主机状态均不正常")>=0 || msg.indexOf("Cpsp")>=0){
			resMsg = "支付系统服务异常!";
		}else{
			resMsg=msg;
		}
		return resMsg;
	}
	
	static Map<String, String> insuComMap = new HashMap<String, String>();
	static{
		insuComMap.put("I00001", "太保财险");
		insuComMap.put("I00002", "英大财险");
		insuComMap.put("I00003", "富德财险");
		insuComMap.put("I00004", "阳光财险");
	}
	public static String obtainInsuComNameCn(String insuCom){
		return insuComMap .get(insuCom);
	}
	
	/**获取车辆保险止期
	 * @param startDate 起保日期
	 * @return
	 */
	public static String obtainEndDate(String startDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateHelper.parseDate(startDate, DateFormat_date));
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DATE, -1);
		return DateHelper.formatDate(cal.getTime(), "yyyy-MM-dd"); 
	}
	
	/**获取spring配置bean
	 * @param name
	 * @return
	 */
	public static Object getBean(String name){
		return RemexApplication.getBean(name);
	}
	
	/**获取车辆默认信息
	 * @param o
	 */
	public static void obtainDefaultVehicleInfo(Object o){
		Vehicle dv = new Vehicle();
		dv.setTransferFlag("0");//过户车标志,0-非过户车,1-过户车
		dv.setLicenseColor(AiwbConsts.licenseColor);//号牌底色,01-蓝,02-黑,03-白,04-黄,05-白蓝,99-其他
		dv.setLicenseType(AiwbConsts.licenseType);//号牌类型,01-大型汽车,02-小型汽车,(03,04,05,06)-(使馆,领馆,境外,外籍),
		//(07-14)-各类摩托拖拉机等农用车,15-挂车,16-教练车,17-试验车,(20-22)-各种临时,24/25-警摩/大型警车,详见码表
		dv.setRunArea("02");//行驶区域,01-出入境,02-中国境内,03-省内,04-场内,05-固定路线

		dv.setSpecialModelFlag("0");//古老特异车标志,0-否,1-是
//		dv.setNewVehicleFlag("0");//新车标志,0-否,1-是
		dv.setEcdemicVehicleFlag("0");//外地车标志,0-本地,1-外地
		
		/**使用性质,8A-家庭自用,8B-企业非营业,8C-党政机关/企事业,8D-非营业个人,9A-营业出租租赁,
		 * 9B-营业城市公交,9C-营业公路客运,9D-营业货运,9E-营业旅游
		 */
		dv.setUseType("8A");
		
		//车辆种类,A-客车,H-货车,G-挂车，A0-客车，A02—6-9座，A03—10-19座，A04—20-35座，A05—>=36，
		dv.setVehicleKind(AiwbConsts.vehicleKind);
		
		/**车辆类型代码,B开头/G开头—重/中/轻型普通/集装/厢/罐式挂车,D11/D12-无/有轨电车,H开头—各类货车,
		 * M及以下开头-各类摩托车/农用车/作业车,K开头-各类客车；K33-轿车,详见码表1.35
		 */
		dv.setVehicleType("K33");
		//证件类型,01-身份证,02-户口本,03-护照,04-军官证,05-驾照,06-返乡证,10组织机构代码证
		dv.setOwnerCertType("01");
		
		/**行驶里程,20000—小于30000公里(北京),60000—>=30000公里(北京),20000—小于30000公里(非北京),
		 * 40000—30000-50000(非北京),60000—>50000(非北京),
		 */
		dv.setRunMile("20000");
		dv.setTotalRunMile("15000");//总行驶里程,无编码
		
		dv.setImportFlag("B");
		dv.setBodyColor("07");
		dv.setAlarmFlag("1");
		ReflectUtil.copyProperties(o, dv);
	}
	
	/**获取询价单默认信息
	 * @param o
	 */
	public static void obtainDefaultQuotationInfo(Object o){
		AtinQuotation dap = new AtinQuotation();
		
	}
}

