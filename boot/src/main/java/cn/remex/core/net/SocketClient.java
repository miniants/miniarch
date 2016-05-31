/*
 * 文 件 名 : SocketClient.java
 * CopyRright (c) since 2013:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-2-3
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */
package cn.remex.core.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import cn.remex.core.exception.NetException;

public class SocketClient {
	public static int doSocketRequest(String host,int port,byte[] packet,byte[] result){
		if(result.length>10240){
			throw new NetException("为了确保资源，目前仅支持一次读取10K byte的数据");
		}
		int l;
		try {
			Socket socket = new Socket(host, port);
			OutputStream os = socket.getOutputStream();
			os.write(packet);
			InputStream is = socket.getInputStream();
			l = is.read(result);
			socket.close();
			return l;
		} catch (UnknownHostException e) {
			throw new NetException("无法连接到服务器", e);
		} catch (IOException e) {
			throw new NetException("socket IO错误!", e);
		}
	}
	
	
	public static int doSocketRequest(
			String proxy,int proxyPort,
			String host,int port,
			byte[] packet,byte[] result){
		if(result.length>10240){
			throw new NetException("为了确保资源，目前仅支持一次读取10K byte的数据");
		}
		int l;
		try {
			SocketAddress sa = new InetSocketAddress(proxy, proxyPort);
//			SocketAddress sa = new InetSocketAddress("198.203.200.29", 5001);
			Socket socket = new Socket(new Proxy(Proxy.Type.SOCKS, sa ));
			SocketAddress bindpoint = new InetSocketAddress(host,port);
//			SocketAddress bindpoint = new InetSocketAddress("195.203.160.118", 9891);
			socket.connect(bindpoint);
			
//			Socket socket = new Socket(host, port);
			OutputStream os = socket.getOutputStream();
			os.write(packet);
			InputStream is = socket.getInputStream();
			l = is.read(result);
			socket.close();
			return l;
		} catch (UnknownHostException e) {
			throw new NetException("无法连接到服务器", e);
		} catch (IOException e) {
			throw new NetException("socket IO错误!", e);
		}
	}
}
