package cn.remex.db.exception;

import cn.remex.core.exception.InvalidOperException;

public class IllegalSqlBeanArgumentException extends InvalidOperException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2930572752154585569L;

	public IllegalSqlBeanArgumentException(final String msg) {
		super(msg);
	}


}
