package cn.remex.core.exception;

public abstract class NestedException extends RuntimeException {

	private static final long serialVersionUID = -5222171049433335216L;
	private String errorCode;
	//private String errorMsg;//用默认的super.message

	/**
	 * Construct a <code>NestedRuntimeException</code> with the specified detail message.
	 * 构造带指定详细消息的新NestedRuntimeException异常。
	 * @param msg the detail message 异常信息
	 */
	public NestedException(final String msg) {
		super(msg);
	}
	public NestedException(final String errorCode,final String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	/**
	 * 构造一个包含异常信息和产生原因的NestedRuntimeException
	 * Construct a <code>NestedRuntimeException</code> with the specified detail message
	 * and nested exception.
	 * @param msg the detail message 异常信息
	 * @param cause the nested exception 产生原因
	 */
	public NestedException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
	public NestedException(final String errorCode,final String msg, final Throwable cause) {
		super(msg, cause);
		this.errorCode = errorCode;
	}


	/**
	 * 检查异常是否包含所提供的异常类型
	 * 或者所给类型自身或 自身包含一个所提供类的嵌套原因
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause
	 * of the given type.
	 * @param exType the exception type to look for 异常类
	 * @return whether there is a nested exception of the specified type  布尔值
	 */
	public boolean contains(final Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedException) {
			return ((NestedException) cause).contains(exType);
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}


	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 * 返回异常信息,如果有嵌套异常,则返回包括嵌套异常的信息
	 */
	@Override
	public String getMessage() {
		return buildMessage(super.getMessage(), getCause());
	}

	/**
	 * Retrieve the most specific cause of this exception, that is,
	 * either the innermost cause (root cause) or this exception itself.
	 * <p>Differs from {@link #getRootCause()} in that it falls back
	 * to the present exception if there is no root cause.
	 * 获取异常产生最根本的原因,即最底层的原因或异常本身
	 * 区别于 {@link #getRootCause()},如果没有根原因则返回到当前异常
	 * @return the most specific cause (never <code>null</code>) 最明确的异常产生原因,不可能为空
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return rootCause != null ? rootCause : this;
	}

	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * 如果存在,获取异常最底层的原因
	 * @return the innermost exception, or <code>null</code> if none
	 *  最底层的异常、或null
	 * @since 2.0
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Build a message for the given base message and root cause.
	 * 构建一个带有基本信息和跟原因的信息
	 * @param message the base message 基本信息
	 * @param cause the root cause 根原因
	 * @return the full exception message 所有的异常信息
	 */
	public static String buildMessage(final String message, final Throwable cause) {
		if (cause != null) {
			StringBuffer buf = new StringBuffer();
			if (message != null) {
				buf.append(message).append("\r\n");
			}
			buf.append("Caused by: ").append(cause);
			return buf.toString();
		}
		else {
			return message;
		}
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}