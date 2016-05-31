package cn.remex.core;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lanchenghao
 * @since 2013-7-23下午7:50:27
 * 系统当前状态变量，如session，用户，线程等
 */
public class CoreSvo {
    public final static ThreadLocal<Map<String, Object>> localParams = new ThreadLocal<Map<String,Object>>();
    public final static String HTTP_REQUEST_KEY ="CORESVO_HTTP_REQUEST";
    public final static String HTTP_RESPONSE_KEY="CORESVO_HTTP_RESPONSE";
    public final static String HTTP_COOKIES="CORESVO_HTTP_COOKIES";
    public final static String HTTP_SESSION="CORESVO_HTTP_SESSION";
	public static void initHttp(HttpServletRequest request, HttpServletResponse response) {
		putLocal(HTTP_REQUEST_KEY, request);
		putLocal(HTTP_RESPONSE_KEY, response);

		Map<String, Cookie> cookies = new HashMap<>();
		if(null!=request.getCookies())
			for (Cookie cookie : request.getCookies()) {
				cookies.put(cookie.getName(), cookie);
			}
		putLocal(HTTP_COOKIES, cookies);
		putLocal(HTTP_SESSION, request.getSession());
	}

	public static void destoryHttp(){
        putLocal(HTTP_REQUEST_KEY, null);
        putLocal(HTTP_RESPONSE_KEY, null);
        putLocal(HTTP_COOKIES, null);
        putLocal(HTTP_SESSION, null);
	}
	/**
	 * 将值从当前线程取出
	 * @param key
	 * @return
	 */
	public static Object valLocal(String key) {
		
		Map<String, Object> map = localParams.get();
		if(null==map){
			map = new HashMap<>();
			localParams.set(map);
		}
		return map.get(key);
	}

	/**
	 * 将值存入当前线程
	 * @param key
	 * @param value
	 */
	public static void putLocal(String key, Object value) {
		Map<String, Object> map = localParams.get();
		if(null==map){
			map = new HashMap<>();
			localParams.set(map);
		}
		map.put(key, value);
	}

	//session
	public static Object valSession(String key) {
		HttpSession session = (HttpSession) valLocal(HTTP_SESSION);
		Assert.notNull(session, ServiceCode.FAIL, "没有初始化Cookies会话容器，请调用CoreSvo.initHttp()！");
		return session.getAttribute(key);
	}

	public static void putSession(String key, String value) {
		HttpSession session = (HttpSession) valLocal(HTTP_SESSION);
		Assert.notNull(session, ServiceCode.FAIL, "没有初始化Cookies会话容器，请调用CoreSvo.initHttp()！");
		session.setAttribute(key, value);
	}
	
	
	/**
	 * 获取cookies
	 * @param key
	 * @return
	 */
	public static Cookie valCookie(String key) {
        Map<String, Cookie> cookies = (Map<String, Cookie>) valLocal(HTTP_COOKIES);
        Assert.notNull(cookies, ServiceCode.FAIL, "没有初始化Cookies会话容器，请调用CoreSvo.initHttp()！");
        return cookies.get(key);
	}
    public static String valCookieValue(String key) {
        Cookie cookie = valCookie(key);
        return cookie!=null?cookie.getValue():null;
    }


    /**
	 * 设置cookies
	 * 默认为会话有效期，path 为/
	 * @param name
	 * @param value
	 */
	public static void putCookie(String name, String value) {
		try {
			String v = value!=null?URLEncoder.encode(value, "UTF-8"):value;
			Cookie cookie = new Cookie(name, v);
			cookie.setPath("/");
			cookie.setVersion(1);

			Map<String,Cookie> cookies = (Map<String, Cookie>) valLocal(HTTP_COOKIES);
			cookies.put(name, cookie);
			putCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			RemexConstants.logger.error("Cookies设置失败！", e);
		}
	}
	public static void putCookie(Cookie cookie) {
		HttpServletResponse response = (HttpServletResponse) valLocal(HTTP_RESPONSE_KEY);
		Assert.notNull(response, ServiceCode.FAIL,  "没有初始化Cookies会话容器，请调用CoreSvo.initHttp()！");

        Map<String,Cookie> cookies = (Map<String, Cookie>) valLocal(HTTP_COOKIES);
        if(null == cookie.getValue()) { //删除cookie
            cookies.remove(cookie.getName());
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }else {
            cookies.put(cookie.getName(), cookie);
            response.addCookie(cookie);
        }

    }
	
	
	

}
