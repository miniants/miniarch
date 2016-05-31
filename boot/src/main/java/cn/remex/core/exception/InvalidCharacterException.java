package cn.remex.core.exception;
/**
 * 
 * @author Hengyang Liu
 * @since 2012-4-1
 *
 */
public class InvalidCharacterException extends NestedException {


	private static final long serialVersionUID = 5744091949541218139L;
	/**
	 * 构造带指定详细消息的新InvalidCharacterException异常。
	 * @param msg 异常信息
	 */
	public InvalidCharacterException(final String msg) {
		super(msg);
	}

	/**
	 * 构造带指定详细消息和原因的新InvalidCharacterException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public InvalidCharacterException(final String msg, final Throwable cause) {
		super(msg, cause);
	}


}
