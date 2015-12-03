package cn.com.qqbx.wx.text;

/* 
 * �� �� �� : HttpHelper.java
 * CopyRright (c) since 2013: 
 * �ļ���ţ� 
 * �� �� �ˣ�Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * ��    �ڣ� 2013-6-21
 * �� �� �ˣ� 
 * ��   �ڣ� 
 * ��   ���� 
 * �� �� �ţ� 1.0
 */


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;


import cn.remex.exception.NetException;
import cn.remex.exception.RIOException;
import cn.remex.exception.StringHandleException;
import cn.remex.util.Assert;

/**
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2013-6-21
 * 
 */
public final class HttpHelper {
	/**
	 * @param request
	 * @return
	 */
	public static String getPostBody(HttpServletRequest request) {
		byte[] buffer = new byte[1024]; // ��ݻ�����
		int count = 0; // ÿ���������ʵ����ݳ���
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // ������ݴ�Ŷ���
		try {
			BufferedInputStream input = new BufferedInputStream(request.getInputStream());
			while ((count = input.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
		} catch (IOException e) {
			throw new RIOException("��HttpServletRequest�ж�ȡ���쳣��", e);
		}
		try {
//			// �õ�һ��byte����,�ṩ��ƽ̨
			String content =baos.toString("UTF-8"); 
			return content;
		} catch (UnsupportedEncodingException e) {
			throw new StringHandleException("��ȡPost������ʱ������UTF-8ת�뷢���쳣!", e);
		}
	}

	public static String sendXml(String url, String requestXml) {

		Assert.notNull(requestXml, "���͵�xml�������ݲ���ΪNull��");
		String responseStr = null;
		BufferedInputStream input = null; // ������,���ڽ�����������
		BufferedOutputStream output = null ;
		try {
			// ����һ��HttpURLConnection
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "text/html");
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpConnection.setRequestProperty("contentType", "utf-8");
			
			
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setAllowUserInteraction(true);
			httpConnection.connect();

			// �������
			output = new BufferedOutputStream(httpConnection.getOutputStream());
			byte[] buffer = requestXml.getBytes( "UTF-8"); // ƽ̨��ɵ�XML��
			output.write(buffer);
			output.flush();

			// �������
			InputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
			buffer = new byte[1024]; // ��ݻ�����
			int count = 0; // ÿ���������ʵ����ݳ���
			ByteArrayOutputStream streamXML = new ByteArrayOutputStream(); // ������ݴ�Ŷ���
			input = new BufferedInputStream(inputStream);
			while ((count = input.read(buffer)) != -1) {
				streamXML.write(buffer, 0, count);
			}
			byte[] iXMLData = streamXML.toByteArray(); // �õ�һ��byte����,�ṩ��ƽ̨
			httpConnection.disconnect();
			responseStr = new String(iXMLData,"UTF-8");
			Assert.notNull(responseStr, "�Է�������Ӧ����Ϊnull!");
		} catch (Exception e) {
			throw new NetException("HttpPostXmlʧ�ܣ�urlΪ��" + url, e);
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

}
