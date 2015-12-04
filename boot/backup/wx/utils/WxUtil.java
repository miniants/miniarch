package zbh.wx.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import zbh.smstp.smsws.xmlbean.ReqShortMsg;
import zbh.smstp.smsws.xmlbean.Request;
import zbh.smstp.smsws.xmlbean.RequestBody;
import zbh.smstp.smsws.xmlbean.RequestHead;
import zbh.wx.AiwcConsts;
import zbh.wx.text.HttpHelper;
import zbh.wx.text.XmlHelper;
import zbh.wx.utils.menu.AccessToken;
import zbh.wx.utils.menu.Menu;
import zbh.wx.xmlbeans.CustomMessage;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.Extend;
import zbh.remex.bs.Head;
import zbh.remex.exception.JSONException;
import zbh.remex.soa.client.SoaClient;
import zbh.remex.util.Arith;
import zbh.remex.util.DateHelper;
import zbh.remex.util.FileHelper;
import zbh.remex.util.Judgment;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/** 
 * 公众平台通用接口工具类 
 *  
 * @author 
 * @date 
 */  
public class WxUtil implements AiwcConsts {  
	private static Logger log = LoggerFactory.getLogger(WxUtil.class);  
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            jsonObject = JSONObject.fromObject(buffer.toString());  
        } catch (ConnectException ce) {  
            log.error("Weixin server connection timed out.");  
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return jsonObject;  
    }  
    
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";  
    
    /** 
     * 获取access_token 
     *  
     * @param appid 凭证 
     * @param appsecret 密钥 
     * @return 
     */  
    public static AccessToken getAccessToken(String appid, String appsecret) {  
        AccessToken accessToken = null;  
      
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                accessToken = new AccessToken();  
                accessToken.setToken(jsonObject.getString("access_token"));  
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
            } catch (JSONException e) {  
                accessToken = null;  
                // 获取token失败  
                log.error("获取token失败");  
            }  
        }  
        return accessToken;  
    }  

    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";  
    public static String openId_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";  
    public static String custom_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    public static String oauth_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static String obtainIpOfWx_url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
    
    /** 
     * 创建菜单 
     *  
     * @param menu 菜单实例 
     * @param accessToken 有效的access_token 
     * @return 0表示成功，其他值表示失败 
     */  
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;  
      
        // 拼装创建菜单的url  
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
        // 将菜单对象转换成json字符串  
        String jsonMenu = JSONObject.fromObject(menu).toString();  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);  
      
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("创建菜单失败");
            }  
        }  
      
        return result;  
    } 
    
    public static boolean validateLicenseNo(String licenseNo){
		Pattern p  = Pattern.compile("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{1,6}");
		if(licenseNo==null){
			return false;
		}else{
			Matcher m = p.matcher(licenseNo);
			if(m.find()){
				System.out.println("yes");
				return true;
			}else{
				System.out.println("no");
				return false;
			}
		}
	}
    public static boolean validateCertNo(String certNo){
    	Pattern p = Pattern.compile("[0-9]{18}"); 
    	if(certNo==null){
			return false;
		}else{
    		Matcher m = p.matcher(certNo);
    		if(m.find()){
    			System.out.println("yes");
    			return true;
    		}else{
    			System.out.println("no");
    			return false;
    		}
		}
    }
    
    public static boolean validateFrameEngineNo(String frameEngineNo){
    	Pattern p = Pattern.compile("^[A-Za-z0-9]{17}+,[A-Za-z0-9]+"); 
    	if(frameEngineNo==null){
    		return false;
    	}else{
    		Matcher m = p.matcher(frameEngineNo);
    		if(m.find()){
    			System.out.println("yes");
    			return true;
    		}else{
    			System.out.println("no");
    			return false;
    		}
    	}
    }
    public static boolean validatePhoneNo(String phoneNo){
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
    	if(phoneNo==null){
    		return false;
    	}else{
    		Matcher m = p.matcher(phoneNo);
    		if(m.find()){
    			System.out.println("yes");
    			return true;
    		}else{
    			System.out.println("no");
    			return false;
    		}
    	}
    }
    
    public static boolean validateIdentifyNo(String identifyNo,String identifyNoByUser){
    	if(identifyNoByUser.equals(identifyNo)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static String sendIdentifyNo(String phoneNo){
		//随机生成六位数字验证码
		String s="";
		while(s.length()<4){
			s+=(int)(Math.random()*10);
		}
		Request request = new Request();
		RequestHead reqHead = new RequestHead();
		RequestBody reqBody = new RequestBody();
		request.setHead(reqHead);
		ReqShortMsg reqShortMsg = new ReqShortMsg();
		reqShortMsg.setPhoneNo("18500234213");
		reqShortMsg.setMsgContent(s);
		reqBody.setReqShortMsg(reqShortMsg);
		request.setBody(reqBody);
		String reqXml = XmlHelper.objectToXml(request);
		System.out.println(reqXml);
		HttpHelper.sendXml("http://192.168.80.30:7005/Smsp/remex/RemexHttpXmlAction.action?bs=SendShortMsgBs", reqXml);
		return s;
    }
    
  	public static int obtainPageCount(int rd,int rw){
  		double pageCouunt=0;
  		String res = Arith.div(rd, rw)+"";
  		String intPart = res.split("\\.")[0];
  		String decPart = res.split("\\.")[1];
  		if(decPart.length()==1 && decPart.charAt(0)==0){
  			pageCouunt = Arith.add(intPart, 0);
  		}else{
  			pageCouunt = Arith.add(intPart, 1);
  			
  		}
  		return (int)pageCouunt;
  	}
  	
    
    public static boolean validateEnginNo(String enginNo){
    	Pattern p = Pattern.compile("^[A-HJ-NP-Za-hj-np-z0-9\\-]{6,10}$"); 
    	if(enginNo==null){
    		return false;
    	}else{
    		Matcher m = p.matcher(enginNo);
    		if(m.find()){
    			System.out.println("yes");
    			return true;
    		}else{
    			System.out.println("no");
    			return false;
    		}
    	}
    }
    
    public static boolean validateFrameNo(String frameNo){
    	Pattern p = Pattern.compile("^[A-HJ-NP-Za-hj-np-z0-9]{17}$"); 
    	if(frameNo==null){
    		return false;
    	}else{
    		Matcher m = p.matcher(frameNo);
    		if(m.find()){
    			System.out.println("yes");
    			return true;
    		}else{
    			System.out.println("no");
    			return false;
    		}
    	}
    }
    
    
    public static JSONObject obtainUserInfo(String openId) { 
    	
        String appId = "wx16f616bf5fa97471";  
        String appSecret = "1a3e97a49bd883f4bced4a73aeb460a3";  
        // 调用接口获取access_token  
        AccessToken at = WxUtil.getAccessToken(appId, appSecret);  
        String url = openId_url.replace("OPENID", openId).replace("ACCESS_TOKEN", at.getToken());  
        JSONObject jsonObject = httpRequest(url, "POST","");  
        return jsonObject;  
    } 
    
    public static void customMessage(CustomMessage cm) {
    	//TODO
    	
    	
    	AccessToken accessToken = getAccessToken("wx16f616bf5fa97471","1a3e97a49bd883f4bced4a73aeb460a3");
    	String url = custom_url.replace("ACCESS_TOKEN", accessToken.getToken());  
        String json = JSONObject.fromObject(cm).toString();  
        JSONObject jsonObject = httpRequest(url, "POST", json);  
    } 
    
    public static String oauth(String code) {  
    	//TODO
    	String url = oauth_url.replace("SECRET", "1a3e97a49bd883f4bced4a73aeb460a3").replace("APPID", "wx16f616bf5fa97471").replace("CODE", code); 
//    	String json = JSONObject.fromObject(cm).toString();  
    	JSONObject jsonObject = httpRequest(url, "POST","");  
    	String openId = (String) jsonObject.get("openid");
    	return openId;
    } 
    public static JSONObject obtainIpOfWX() {  
    	AccessToken accessToken = getAccessToken("wx16f616bf5fa97471","1a3e97a49bd883f4bced4a73aeb460a3");
    	String url = obtainIpOfWx_url.replace("ACCESS_TOKEN", accessToken.getToken());  
        JSONObject jsonObject = httpRequest(url, "POST", "");
        return jsonObject;
    } 
    
    public static zbh.com.qqbx.aitp.aiws.xmlbeans.Request setReqVehicleInfoXml(String transType,Object bodyData,String transChannel,String userName){
    	zbh.com.qqbx.aitp.aiws.xmlbeans.Request request = new zbh.com.qqbx.aitp.aiws.xmlbeans.Request();
    	zbh.com.qqbx.aitp.aiws.xmlbeans.RequestHead reqHead = new zbh.com.qqbx.aitp.aiws.xmlbeans.RequestHead();
    	zbh.com.qqbx.aitp.aiws.xmlbeans.RequestBody reqBody = new zbh.com.qqbx.aitp.aiws.xmlbeans.RequestBody();
    	
    	request.setHead(reqHead);
    	request.setBody(reqBody);
		reqHead.setTransDate(DateHelper.getNow("yyyy-MM-dd HH:mm:ss"));
		reqHead.setTransNo("0000000000000000000030611");
		reqHead.setChannelOper(userName);   //当前系统操作员
		reqHead.setTransChannel(transChannel);//行销渠道
		reqHead.setTransType(transType); //交易类型
		
		reqBody.setBodyData(bodyData);
		return request;
    }
public static Object invokeService(String serviceName,String transType, Object reqBody,Extend extend,String transChannel, String username){
		
//		SIPubTools.saveXMLFile("Request", SIConst.transType_saveOnlineCustomer, ("MSDX".equals(transChannel)?"A00001":"A00002"),	"a", XmlHelper.objectToXml(reqBody), transNo);
    	//FileHelper.saveFileContent("D:\\req_111"+transType+".xml", zbh.remex.util.XmlHelper.marshall(reqBody));
		BsRvo rvo;
		try{
			Head reqHead = new Head();
			reqHead.setTransChannel(transChannel);
			reqHead.setChannelUser(username);
			reqHead.setChannelUser("admin");
			reqHead.setChannelPwd("admin");
			reqHead.setChannelOper(username);
			reqHead.setTransType(transType);
//			AiwbUtils.saveXmlFile();
			rvo = SoaClient.invokeService("remex:soa://" + serviceName + ":execute", reqHead, reqBody, extend,null,0);
//			AiwbUtils.saveXmlile();

		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		if (!rvo.getExtend().isStatus()) {
			throw new RuntimeException(rvo.getExtend().getMsg());
		}
		//TODO 判断异常信息——rvo.getExtend().getErrorCode(), rvo.getExtend().getErrorMsg()
		
		return rvo.getBody();
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
			baseXmlFilePath = AiwcConsts.XmlFilePath_Win;
		}
		// LINUX 无盘符定义文件层级关系
		else if (curSystem.toUpperCase().startsWith("LINUX")) {
			baseXmlFilePath = AiwcConsts.XmlFilePath_Linux;
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
	
}

