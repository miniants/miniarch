package cn.remex.core.exception;

public class IllegalOperException extends InvalidOperException {

	private static final long serialVersionUID = 7938540912104314396L;
	/**
	 * 构造带指定详细消息的新IllegalOperException异常。
	 * @param oper 操作符
	 */
	public IllegalOperException(final String oper) {
		super("无效的操作符："+oper);
	}
	/**
	 * 构造带指定详细消息和原因的新IllegalOperException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。 
	 * @param oper 操作符
	 * @param msg 异常信息
	 */
	public IllegalOperException(final String oper, final String msg) {
		super("无效的操作符："+oper+": "+ msg);
		this.oper=oper;
	}
}
