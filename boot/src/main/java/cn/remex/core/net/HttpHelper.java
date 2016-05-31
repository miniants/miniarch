/* 
 * 文 件 名 : HttpHelper.java
 * CopyRright (c) since 2013: 
 * 文件编号： 
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-6-21
 * 修 改 人： 
 * 日   期： 
 * 描   述： 
 * 版 本 号： 1.0
 */

package cn.remex.core.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.remex.core.exception.ServiceCode;

import cn.remex.core.exception.NetException;
import cn.remex.core.exception.RIOException;
import cn.remex.core.exception.StringHandleException;
import cn.remex.core.util.Assert;

/**
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2013-6-21
 * 
 */
public final class HttpHelper {
	

	public static String sendXml(String url, String requestXml) {
		return sendXml(url, requestXml, "UTF-8");
	}
	public static String sendXml(String url, String requestXml,String charset) {

		Assert.notNull(requestXml, ServiceCode.FAIL, "发送的xml报文内容不能为Null！");
		String responseStr = null;
		BufferedInputStream input = null; // 输入流,用于接收请求的数据
		BufferedOutputStream output = null ;
		try {
			// 建立一个HttpURLConnection
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "text/html");
			httpConnection.setRequestProperty("Accept-Charset", charset);
			httpConnection.setRequestProperty("contentType", charset);
			
			
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setAllowUserInteraction(true);
			httpConnection.connect();

			// 发送数据
			output = new BufferedOutputStream(httpConnection.getOutputStream());
			byte[] buffer = requestXml.getBytes( charset); // 平台生成的XML串
			output.write(buffer);
			output.flush();

			// 接收数据
			InputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
			buffer = new byte[1024]; // 数据缓冲区
			int count = 0; // 每个缓冲区的实际数据长度
			ByteArrayOutputStream streamXML = new ByteArrayOutputStream(); // 请求数据存放对象
			input = new BufferedInputStream(inputStream);
			while ((count = input.read(buffer)) != -1) {
				streamXML.write(buffer, 0, count);
			}
			byte[] iXMLData = streamXML.toByteArray(); // 得到一个byte数组,提供给平台
			httpConnection.disconnect();
			responseStr = new String(iXMLData,charset);
			Assert.notNull(responseStr, ServiceCode.FAIL, "对方服务相应报文为null!");
		} catch (Exception e) {
			throw new NetException("HttpPostXml失败，url为：" + url, e);
		} finally {
			if (input != null) {
				try {
					input.close();
					output.flush();
					output.close();
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		}
		return responseStr;

	}

	public static String obtainHttpPack(InputStream input) {
		byte[] buffer = new byte[1024]; // 数据缓冲区
		int count = 0; // 每个缓冲区的实际数据长度
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 请求数据存放对象
		try {
			BufferedInputStream bfInput = new BufferedInputStream(input);
			while ((count = bfInput.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
		} catch (IOException e) {
			throw new RIOException("从HttpServletRequest中读取流异常！", e);
		}
		try {
//			// 得到一个byte数组,提供给平台
			String content =baos.toString("UTF-8"); 
			return content;
		} catch (UnsupportedEncodingException e) {
			throw new StringHandleException("读取Post请求报文时，进行UTF-8转码发生异常!", e);
		}
	}
}
