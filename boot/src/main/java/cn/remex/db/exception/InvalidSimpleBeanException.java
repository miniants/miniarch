package cn.remex.db.exception;

import cn.remex.core.exception.BeansException;


public class InvalidSimpleBeanException extends BeansException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2991145291030167695L;

	public InvalidSimpleBeanException(final String msg) {
		super(msg);
	}

	public InvalidSimpleBeanException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
