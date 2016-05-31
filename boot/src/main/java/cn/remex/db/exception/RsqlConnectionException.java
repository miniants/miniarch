package cn.remex.db.exception;

public class RsqlConnectionException extends RsqlException {
	private static final long serialVersionUID = -2772697311814651255L;


	public RsqlConnectionException(String errorCode, String msg) {
		super(errorCode, msg);
	}

	public RsqlConnectionException(String errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}
}
