package cn.remex.core;

import cn.remex.RemexConstants;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lanchenghao
 * @since 2013-7-23下午7:50:27
 * 系统当前状态变量，如session，用户，线程等
 */
public class CoreSvo {
	private final static ThreadLocal<Map<String, Object>> localParams = new ThreadLocal<Map<String,Object>>();
	private static Class<?> ActionContextClass = null;
	private static Class<?> ActionSessionClass = null;
	private static Method ActionContextGetContext = null;
	private static Method ActionContextGetSession = null;
	private static Class<?> ServletActionContextClass = null;
//	private static Class<?> HttpServletRequestClass = null;
//	private static Class<?> HttpServletResponseClass = null;
	private static Method GetRequest = null;
//	private static Method GetCookie = null;
	private static Method GetResponse = null;
//	private static Method SetCookie = null;
	private static Method ActionSessionPut = null;
	private static Method ActionSessionGet = null;
	static{
		try {
			ActionContextClass = Class.forName("com.opensymphony.xwork2.ActionContext");
			ActionContextGetContext = ActionContextClass.getMethod("getContext");
			ActionContextGetSession = ActionContextClass.getMethod("getSession");
			
			ActionSessionClass = Class.forName("org.apache.struts2.dispatcher.SessionMap");
			
			ServletActionContextClass = Class.forName("org.apache.struts2.ServletActionContext");
			GetRequest = ServletActionContextClass.getMethod("getRequest");
			GetResponse = ServletActionContextClass.getMethod("getResponse");
//			
//			HttpServletRequestClass = Class.forName("javax.servlet.http.HttpServletRequest");
//			GetCookie = HttpServletRequestClass.getMethod("GetCookie");
//			
//			HttpServletResponseClass = Class.forName("javax.servlet.http.HttpServletResponse");
//			SetCookie = HttpServletResponseClass.getMethod("SetCookie");
			
			ActionContextGetSession = ActionContextClass.getMethod("getSession");
			
			for(Method m : ActionSessionClass.getMethods()){
				if(m.getName().equals("put")){
					ActionSessionPut = m;
				}else	if(m.getName().equals("get")){
					ActionSessionGet = m;
				}
			}
			
			
		} catch (Throwable e) {
			RemexConstants.logger.warn("项目目前不支持web session!",e);
		}
	}
	
	/**
	 * 将值从当前线程取出
	 * @param key
	 * @return
	 */
	public static Object $VL(String key) {
		
		Map<String, Object> map = localParams.get();
		if(null==map){
			map = new HashMap<String, Object>();
			localParams.set(map);
		}
		return map.get(key);
	}

	/**
	 * 将值存入当前线程
	 * @param key
	 * @param value
	 */
	public static void $SL(String key, Object value) {
		Map<String, Object> map = localParams.get();
		if(null==map){
			map = new HashMap<String, Object>();
			localParams.set(map);
		}
		map.put(key, value);
	}

	public static Object $VS(String key) {
		Assert.notNull(ActionContextClass, "没有初始化会话容器！");
		
		Object context = ReflectUtil.invokeMethod(ActionContextGetContext, ActionContextClass);
		Object session = ReflectUtil.invokeMethod(ActionContextGetSession, context);
		return ReflectUtil.invokeMethod(ActionSessionGet, session,key);
	}

	public static void $SS(String key, Object value) {
		Assert.notNull(ActionContextClass, "没有初始化会话容器！");
		Object context = ReflectUtil.invokeMethod(ActionContextGetContext, ActionContextClass);
		Object session = ReflectUtil.invokeMethod(ActionContextGetSession, context);
		ReflectUtil.invokeMethod(ActionSessionPut, session,key,value);
	}
	
	
	/**
	 * 获取cookies
	 * @param key
	 * @return
	 */
	public static Object $VC(String key) {
		Assert.notNull(ServletActionContextClass, "没有初始化会话容器！只能在Struts Web容器中使用该功能。");
		
		HttpServletRequest request = (HttpServletRequest) ReflectUtil.invokeMethod(GetRequest, ServletActionContextClass);
		Cookie[] cookies = request.getCookies();
		
		//没有优化，应该向前端一样优化一下存取。考虑到cookies的使用量不大暂时循环使用。 TODO
		try {
	    if (cookies != null) {  
	        for (Cookie cookie : cookies) {  
	            if (key.equals(cookie.getName())) {  
										return URLDecoder.decode(cookie.getValue(),"UTF-8");
	            }
	        }  
	    }  
		} catch (UnsupportedEncodingException e) {
			RemexConstants.logger.error("Cookies读取转码失败！", e);
		}  
		
		return null;
	}
	
	/**
	 * 设置cookies
	 * 默认为会话有效期，path 为/
	 * @param key
	 * @param value
	 */
	public static void $SC(String name, String value) {
		Assert.notNull(ServletActionContextClass, "没有初始化会话容器！只能在Struts Web容器中使用该功能。");
		Cookie cookie;
		try {
			String v = URLEncoder.encode(value, "UTF-8");
			cookie = new Cookie(name, v);
			cookie.setPath("/");
			cookie.setVersion(1);
			$SC(cookie);
		} catch (UnsupportedEncodingException e) {
			RemexConstants.logger.error("Cookies设置失败！", e);
		}
	}
	public static void $SC(Cookie cookie) {
		Assert.notNull(ServletActionContextClass, "没有初始化会话容器！只能在Struts Web容器中使用该功能。");
		HttpServletResponse response = (HttpServletResponse) ReflectUtil.invokeMethod(GetResponse, ServletActionContextClass);
		response.addCookie(cookie);
	}
	
	
	

}
