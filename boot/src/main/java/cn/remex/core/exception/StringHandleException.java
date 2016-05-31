package cn.remex.core.exception;

public class StringHandleException extends NestedException {

	private static final long serialVersionUID = -4732372171741578021L;
	/**
	 * 构造带指定详细消息的新StringHandleException异常。
	 * @param msg 异常信息
	 */
	public StringHandleException(final String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造带指定详细消息和原因的新StringHandleException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。 
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public StringHandleException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
