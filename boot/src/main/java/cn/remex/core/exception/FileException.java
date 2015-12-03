package cn.remex.core.exception;

public class FileException extends NestedException {

	private static final long serialVersionUID = -4066745120401393995L;
	/**
	 * 构造带指定详细消息的新IllegalArgumentException异常。
	 * @param msg 异常信息
	 */
	public FileException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造带指定详细消息和原因的新IllegalArgumentException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。 
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}


}
