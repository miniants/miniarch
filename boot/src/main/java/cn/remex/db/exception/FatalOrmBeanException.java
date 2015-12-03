package cn.remex.db.exception;

import cn.remex.core.exception.BeansException;


public class FatalOrmBeanException extends BeansException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2991145291030167695L;

	public FatalOrmBeanException(final String msg) {
		super(msg);
	}

	public FatalOrmBeanException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
