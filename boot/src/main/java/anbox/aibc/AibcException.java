/**
 * 
 */
package anbox.aibc;

import cn.remex.exception.NestedException;

/** 
 * @author liuhengyang 
 * @date 2015-2-11 下午9:33:35
 * @version 版本号码
 * @TODO 描述
 */
public class AibcException extends NestedException {

	private static final long serialVersionUID = -4052765991438237473L;

	public AibcException(String errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
		// TODO Auto-generated constructor stub
	}

	public AibcException(String errorCode, String msg) {
		super(errorCode, msg);
		// TODO Auto-generated constructor stub
	}

	public AibcException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public AibcException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
