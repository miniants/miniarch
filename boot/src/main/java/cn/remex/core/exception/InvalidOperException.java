package cn.remex.core.exception;


public class InvalidOperException extends NestedException {
	
	private static final long serialVersionUID = 7938540912104314396L;
	protected String oper;
	/**
	 * 构造带指定详细消息的新InvalidOperException异常。
	 * @param msg 异常信息
	 */
	public InvalidOperException(final String msg) {
		super(msg);
	}
	/**
	 * 构造带指定详细消息和原因的新InvalidOperException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public InvalidOperException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public String getOper() {
		return this.oper;
	}

}
