package zbh.wx;

import org.apache.log4j.Logger;

public interface AiwcConsts {
	public static Logger logger = Logger.getLogger(AiwcConsts.class);
	
	public static String UID="UID";//用户id
	public static String TID="TID";//用户跟踪id
	public static String ST="ST";//安全登录的Token 
	public static String TS="TS";//服务器下发的时间戳
	
	public static String TipsMsg_noLogin="尚未登录，请先进行登录!";
	public static String TipsMsg_logined="注册必填信息不能为空!";
	public static String TipsMsg_invalidCode="邀请码已经过期!";
	public static String TipsMsg_insureConfirmNeedLogin="为保障您的权益，登录后方可投保确认，请先登录!系统将在3秒后自动转入登录页面";
	
	
	public static String  UserSessionKey = "AncarUserSessionKey";
	
	public static String TransChannel_wx = "Ancar_wx";
	public static String TransChannel_web = "Ancar_web";
	public static String XmlFilePath_Win = "/home/appadmin/Ancar_web/InsuXmls";
	public static String XmlFilePath_Linux = "D:/AppRoot/Ancar_web/InsuXmls";
	
	public static String PrdtKind_VCI = "VCI";
	public static String PrdtKind_TCI = "TCI";
   /**是否缴纳车船税  0:否 ，1：是*/
   public static final String TravelTaxFlag_N = "N";   
   public static final String TravelTaxFlag_Y = "Y";
	
	//key存放路径
//	static String Rsa_RootPath = "/config/";
	static String Rsa_RootPath = "D:\\";
	static String Rsa_PublicKeyPath = Rsa_RootPath+"publicKey.keystore";
	static String Rsa_PrivateKeyPath = Rsa_RootPath+"privateKey.keystore";
	static String Rsa_KeyPariPath = Rsa_RootPath+"key.keystore";
	
	/**保单未支付状态，人工/自动核保通过、未支付*/
	public static String PolicyStatus_toPay = PolicyStatus.PSAU.toString()+","+PolicyStatus.PSMU.toString()+
			","+PolicyStatus.PSNP.toString();
	/**客户可以支付的保单支付状态，未支付、支付失败*/
	public static String PayStatus_toPay = OrderStatus.OSNP.toString()+","+OrderStatus.OSFP.toString();
	
	//关系人类型
	public static String personType_app = "2";
	public static String personType_ins = "1";
	public static String personType_owner = "0";
	//车辆信息常量
	public static String vehicleType = "K33";
	public static String vehicleKind = "A0";
	public static String ecdemicVehicleFlag = "0";
	public static String licenseColor = "01";
	public static String licenseType = "02";
	public static String useType = "8A";
	public static String transferFlag = "0";
	public static String runArea = "02";
	public static String specialModelFlag = "0";
	public static String runMile = "20000";
	public static String totalRunMile = "15000";
	public static String agntComTransNo = "1409458030019";
	public static String ownerCertType = "01";
	
	
	//时间类型
	public static String DateFormat_date="yyyy-MM-dd"; 
	public static String DateFormat_time="yyyy-MM-dd hh:mm:ss"; 
	
	//推广邀请注册
	/**邀请码长度*/
	public static int RandomCodeLength = 6;
	/**注册码有效期*/
	public static int  ValidityPeriod= 30;
	/**注册方式
	 * @author zhangaiguo
	 */
	public enum RegisterType{
		/**常规注册，非邀请注册*/
		Normal,
		/**邀请码*/
		InvitationCode,
		/**注册链接*/
		InvitationLink,
		/**二维码*/
		QRCode
	}
	
	/**邀请码状态
	 * @author zhangaiguo
	 */
	public enum InvitationCodeStatus{
		/**有效*/
		Effective,
		/**失效*/
		Invalid,
		/**已经使用*/
		Used
	}
	
	/**级别
	 * @author zhangaiguo
	 */
	public enum memberLevel{
		/**超级安*/
		Super,
		/**大安*/
		Big,
		/**小安*/
		Small
	}
	
	
	/**套餐类型
	 * @author zhangaiguo
	 */
	public enum PackageType{
		/**豪华型*/
		Luxury,
		/**经济型*/
		Economical,
		/**自选型*/
		Optional
	}
	/**订单详情保单 类型
	 * @author zhangaiguo
	 */
	public enum OrderType{
		/**车险保单*/
		AtinPolicy
	}
	
	/**只有两种状态的标志位
	 * @author zhangaiguo
	 */
	public enum Flag{
		Y,
		N
	}
	
	/**会员请求查看的数据类型，分为客户车辆信息、询价单信息、订单信息
	 * @author zhangaiguo
	 */
	public enum ReqInfoType{
		/**会员主页mmbHome*/
		MC,
		/**个人资料personal-查看个人资料*/
		PS_View,
		/**个人资料personal-修改个人资料*/
		PS_Update,
		/**个人资料personal-更新密码*/
		PS_UP,
		/**客户车辆customerVehicle*/
		CV,
		/**客户询价单customerQuotation*/
		CQ,
		/**customerOrder*/
		CO,
		/**MemberDelivery-View*/
		MD_View,
		/**MemberDelivery-Edit*/
		MD_Edit
	}
	
	/**条款编码
	 * @author zhangaiguo
	 */
	public enum CoverageCode{
		/**交强险 */
		BZ,
		/**车损险*/
		A,
		/**三者*/
		B,
		/**车上人员（司机）*/
		D3,
		/**车上人员（乘客）**/
		D4,
		/**盗抢险*/
		G1,
		/**车身划痕险*/
		L,
		/**自燃险*/
		Z,
		/**单独玻璃破碎险*/
		F,
		/**发动机特大损失险**/
		X1,
		/**不计免赔**/
		M
	}
	

	/**
	 * 关联单标志
	 * @author zhangaiguo
	 * @since  2015-01-12
	 */
	public enum RelationFlag{
		/**关联单*/
		RP,
		/**单交强*/
		ST,
		/**单商业*/
		SC	
	}
	
	/**保单状态
	 * @author zhangaiguo
	 *
	 */
	public enum PolicyStatus{
		/**未核保*/
		PSNU,
		/**中海人工核保通过*/
		PSMU,
		/**自动核保通过*/
		PSAU,
		/**未支付*/
		PSNP,
		/**已支付*/
		PSYP,
		/**出单*/
		PSYI,
		/**已撤单**/
		PSYC
	}
	
	/**
	 * @author zhangaiguo
	 *支付状态
	 */
	public enum PayStatus{
		/**行销未收费*/
		APSNP,
		/**行销收费中*/
		APSWP,
		/**行销收费成功*/
		APSYP,
		/**行销收费失败*/
		APSFP,
		/**行销收费失效*/
		APSIP,
		/**保险公司未支付*/
		EPSNP,
		/**保险公司支付中*/
		EPSWP,
		/**保险公司支付成功*/
		EPSYP,
		/**保险公司支付失败*/
		EPSFP,
		/**保险公司支付失效*/
		EPSIP
	}
	
	/**订单状态
	 * @author zhangaiguo
	 */
	public enum OrderStatus{
		/**未支付*/
		OSNP,
		/**已支付*/
		OSYP,
		/**支付失败*/
		OSFP,
		/**订单取消*/
		OSCP
	}
	
	/**保单类别
	 * @author zhangaiguo
	 */
	public enum PolicyClass{
		/**车险*/
		AutoInsurance,
		/**车主人身意外险*/
		OwnerAccident
	}
	
	/**
	 * @author zhangaiguo
	 *业务交易类型枚举、通过该字符串（key）去配置信息中获取值（value）
	 */
	public enum TransType{
		/**初始化保险公司和客户*/
		queryInsuComs,
		/** 车辆查询 */
		beginCarQuery,
		/**上年止期查询*/
		queryLastContEndDate,
		/**实际价值查询*/
		queryCarRealPrice,
		/** 报价 */
		beginQuotePrice,
		/** 投保确认 */
		beginInsure,
		/**保存递送信息*/
		saveDeliveryInfo,
		/**提交订单*/
		submitOrder,
		/**核保状态查询*/
		queryProposalContState,
		/**承保确认*/
		policyConfirm,
	
	}
	
	public enum AitpBsNames{
		/**初始化查询保险公司保存用户信息**/
		AitpAutoInsuInfoBs,
		/**保险公司车辆查询*/
		AitpQueryVehicleInfoBs,
		/**查询保单上年止期*/
		AitpQueryPolicyEndDateBs,
		/**查询车辆实际价值**/
		AitpActualValueBs,
		/**精确报价*/
		AitpQuotePriceBs,
		/**投保确认*/
		AitpInsureConfirmBs,
		/**保存递送信息*/
		AitpSaveDeliveryInfoBs,
		/***订单提交*/
		AitpSubmitOrderBs
	}

}
