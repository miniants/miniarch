//package cn.com.qqbx.wx.utils;
//
//import net.sf.json.JSONObject;
//import cn.com.qqbx.wx.utils.menu.AccessToken;
//
//public class ObtainUserId {
//	public static void main(String args[]){
//	        // 第三方用户唯一凭证  
//	        String appId = "wx16f616bf5fa97471";  
//	        // 第三方用户唯一凭证密钥  
//	        String appSecret = "1a3e97a49bd883f4bced4a73aeb460a3";  
//	  
//	        // 调用接口获取access_token  
//	        AccessToken at = WxUtil.getAccessToken(appId, appSecret);  
//	  
//	        if (null != at) {  
//	            // 调用接口创建菜单  
//	            JSONObject result = WxUtil.obtainUserInfo(at.getToken(), "");
//	            System.out.println(result);
//	            // 判断菜单创建结果  
//	        }  
//	}
//}
