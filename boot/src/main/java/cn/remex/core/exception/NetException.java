/* 
* 文 件 名 : NetException.java
* CopyRright (c) since 2013: 
* 文件编号： 
* 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
* 日    期： 2013-6-17
* 修 改 人： 
* 日   期： 
* 描   述： 
* 版 本 号： 1.0
*/ 

package cn.remex.core.exception;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-17
 *
 */
public class NetException extends NestedException {

	private static final long serialVersionUID = 3862624615087073544L;

	/**
	 * 构造带指定详细消息的新NetException异常。
	 * @param msg 异常信息
	 */
	public NetException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造带指定详细消息和原因的新NetException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public NetException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
