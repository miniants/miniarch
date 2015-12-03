/**
 * 
 */
package cn.remex.core.exception;


/**
 * @author Hengyang Liu
 * @since 2012-4-2
 *
 */
public class JSONException extends NestedException {

	private static final long serialVersionUID = 6069541685144086900L;
	/**
	 * 构造带指定详细消息的新JSONException异常。
	 * @param msg 异常信息
	 */
	public JSONException(final String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造带指定详细消息和原因的新JSONException异常。
 	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。
	 * @param msg 异常信息
	 * @param cause 产生原因
	 */
	public JSONException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
