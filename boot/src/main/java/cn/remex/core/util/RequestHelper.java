package cn.remex.core.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * 请求帮助类
 */

public class RequestHelper {
	/**
	 *  获取属性名的值,如果属性名为string，则返回string类型的值，如果为文件，则返回文件路径
	 * @param name 属性名
	 * @param params Map形式的参数集
	 * @return 获取的值
	 */
	@SuppressWarnings("unchecked")
	static public <T> T getParameter(final String name,final Map<String, Object> params){
		if(null == params || null == params.get(name)){
			return null;
		}else{
			Object o = params.get(name);
			if(o instanceof String[]) {
				return (T) ((String[])o)[0].toString().trim();
			} else if(o instanceof File[]) {
				return (T) ((File[])o)[0];
			} else {
				return (T) o;
			}
		}
	}
	
	public static String getClientIP(HttpServletRequest request) {
		if(null==request)
			return "";
	    String ip = request.getHeader("x-forwarded-for"); 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    }

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP"); 

	    }

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	}
}
