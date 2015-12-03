package cn.remex.db.exception;

import cn.remex.core.exception.NestedException;

public class RsqlException extends NestedException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8607666674635611653L;

	public RsqlException(final String msg) {
		super(msg);
	}

	public RsqlException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
