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

package cn.remex.web.service;

import cn.remex.core.exception.NestedException;

/**
 * 业务bs异常
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-17
 *
 */
public class BsException extends NestedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3862624615087073544L;

	/**
	 * @param msg
	 */
	public BsException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public BsException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
