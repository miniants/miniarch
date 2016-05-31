package cn.remex.core.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {
	/**
	 * 以MD5的方式进行加密
	 * @param str 字符串
	 * @return string
	 */
	public static String EncodeByMD5(final String str) { // 确定计算方法
		MessageDigest md5;
		String newstr = "";
		try {
			/**
			 * MD2 MD5
                        SHA-1
                        SHA-256
                        SHA-384
                        SHA-512
			 */
			md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newstr;
	}
	/**
	 * 以SHA1的方式进行加密
	 * @param str 字符串
	 * @return string
	 */
	public static String EncodeBySHA1(final String str) { // 确定计算方法
		MessageDigest md5;
		String newstr = "";
		try {
			/**
			 * MD2 MD5
                        SHA-1
                        SHA-256
                        SHA-384
                        SHA-512
			 */
			md5 = MessageDigest.getInstance("SHA-1");
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newstr;
	}

	/**
	 * 主方法
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(final String[] args) throws NoSuchAlgorithmException,
	UnsupportedEncodingException {
		// TODO Auto-generated method stub
		System.out.println(Encoder.EncodeByMD5("Have a good timHave a good timHave a good tim__@#$%^&*()e！"));
		System.out.println(Encoder.EncodeBySHA1("Have a good timHave a good timHave a good tim__@#$%^&*()e！"));
	}

}
