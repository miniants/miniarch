package cn.remex.core.exception;

public class IllegalBsArgumentException extends InvalidOperException {

	private static final long serialVersionUID = -2930572752154585569L;
	private String bs;
	/**
	 * 构造带指定详细消息的新IllegalBsArgumentException异常。
	 * @param oper 操作符
	 * @param bs bs名称
	 */
	public IllegalBsArgumentException(final String oper,final String bs) {
		super("操作"+oper+"需要具体的bs名称，此处bs名称"+bs+"无效!");
		this.oper=oper;
		this.bs=bs;
	}
	/**
	 * 构造带指定详细消息和原因的新IllegalBsArgumentException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。 
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public IllegalBsArgumentException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub"如果oper执行execute，必须指定业务执行单元Bs。请显式指定bs及其指令bsCmd[可选]。"
	}

	public String getBs() {
		return this.bs;
	}

}
