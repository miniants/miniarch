package cn.remex.db.exception;

import cn.remex.core.exception.NestedException;

public class RsqlException extends NestedException  {
	private static final long serialVersionUID = -8607666674635611653L;

	public RsqlException(String errorCode, String msg) {
		super(errorCode, msg);
	}

	public RsqlException(String errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}
}
