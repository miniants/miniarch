package cn.remex.core.exception;

import cn.remex.core.util.ObjectUtils;

public abstract class BeansException extends NestedException {
	
	private static final long serialVersionUID = -975486486082445010L;
	protected String beanName;

	/**
	 * 构造带指定详细消息的新BeansException异常。
	 * Create a new BeansException with the specified message.
	 * @param msg the detail message 异常信息
	 */
	public BeansException(final String msg) {
		super(msg);
	}

	/**
	 * 构造带指定详细消息和原因的BeansException异常。
	 * 注意，与 cause 相关的详细消息不是 自动合并到这个异常的详细消息中的。 
	 * Create a new BeansException with the specified message
	 * and root cause.
	 * @param msg the detail message 异常信息
	 * @param cause the root cause 产生原因
	 */
	public BeansException(final String msg, final Throwable cause) {
		super(msg, cause);
	}


	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeansException)) {
			return false;
		}
		BeansException otherBe = (BeansException) other;
		return getMessage().equals(otherBe.getMessage()) &&
				ObjectUtils.nullSafeEquals(getCause(), otherBe.getCause());
	}

	public String getBeanName() {
		return this.beanName;
	}

	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}

}

