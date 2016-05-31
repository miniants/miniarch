package cn.remex.core.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class URLHelper {
	/**
	 * 按照utf-8的编码方式进行编码
	 * @param urlAndParameter
	 * @return
	 */
	static public String encodeParameter(final String urlAndParameter){
		return encodeParameter(urlAndParameter,"UTF-8");
	}
	/**
	 * 按照传入的编码方式对请求数据url和parameter进行编码
	 * @param urlAndParameter 请求内容
	 * @param enc 编码方式
	 * @return 返回字符串
	 */
	static public String encodeParameter(final String urlAndParameter, final String enc) {
		// 如果出现多个？分割符则原样返回
		if (urlAndParameter.indexOf("?") != urlAndParameter.lastIndexOf("?")) {
			return urlAndParameter;
		}
		String[] s = urlAndParameter.split("\\?");
		String url = s[0], paramString = "";
		if (s.length == 2) {
			paramString = s[1];
		}
		String[] params = paramString.split("&");
		String newParams = "";
		for (String param : params) {
			if (newParams != "") {
				newParams = newParams + "&";
			}
			String[] temp = param.split("=");
			newParams = newParams + temp[0] + "=";
			if (temp.length == 2) {
				try {
					newParams = newParams + URLEncoder.encode(temp[1], enc);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return url + "?" + newParams;

	}
	/**
	 * 获取请求数据中的属性
	 * @param request 请求数据
	 * @param param 属性名
	 * @return 字符串
	 */
	static public String getParameter(final HttpServletRequest request, final String param) {
		String qs = request.getQueryString();
		String value = "";
		try {
			if (qs.indexOf(param) >= 0) {
				value = qs.split(param + "=")[1];
				value = value.split("&")[0];
				value = URLDecoder.decode(value, "GB2312");
			}
		} catch (Exception e) {
			value = "";
		}

		return value;

	}
}
