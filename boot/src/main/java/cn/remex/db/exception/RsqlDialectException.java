package cn.remex.db.exception;

public class RsqlDialectException extends RsqlException {

	public RsqlDialectException(String errorCode, String msg) {
		super(errorCode, msg);
	}

	public RsqlDialectException(String errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}
}
