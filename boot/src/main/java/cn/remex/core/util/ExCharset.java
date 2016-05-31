package cn.remex.core.util;

import java.io.UnsupportedEncodingException;

public class ExCharset {
	/**
	 * 默认从ISO-8859-1转换到GB2312
	 * @param s 字符串
	 * @return 返回的字符串
	 */
	public static String getStringExCharset(final String s){
		try {
			return new String(s.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	/**
	 * 从perCharset 转换到 toCharset
	 * @param s 字符串
	 * @param perCharset
	 * @param toCharset
	 * @return 字符串
	 */
	public static String getStringExCharset(final String s,final String perCharset,final String toCharset){
		try {
			return new String(s.getBytes(perCharset), toCharset);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	private boolean exCharset = true;

	private String perCharset = "ISO-8859-1";

	private String toCharset = "GB2312";

	/**
	 * @return the perCharset
	 */
	public String getPerCharset() {
		return this.perCharset;
	}

	/**
	 * 将ISO-8859-1转化为GB2312
	 * @param s 字符串
	 * @return 字符串
	 */
	public String getStringInCharset(final String s) {
		if (this.exCharset) {
			try {
				return new String(s.getBytes(this.perCharset), this.toCharset);
			} catch (Exception e) {
				return "字符编码错误！";
			}
		} else {
			return s;
		}
	}

	/**
	 * @return the toCharset
	 */
	public String getToCharset() {
		return this.toCharset;
	}
	/**
	 * @param perCharset
	 *            the perCharset to set
	 */
	public void setPerCharset(final String perCharset) {
		this.perCharset = perCharset;
		if (this.toCharset.equals(this.perCharset)) {
			this.exCharset = false;
		}
	}

	/**
	 * @param toCharset
	 *            the toCharset to set
	 */
	public void setToCharset(final String toCharset) {
		this.toCharset = toCharset;
		if (this.toCharset.equals(this.perCharset)) {
			this.exCharset = false;
		}
	}

}
