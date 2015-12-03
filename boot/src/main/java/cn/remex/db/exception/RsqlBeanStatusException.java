package cn.remex.db.exception;


public class RsqlBeanStatusException extends RsqlException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8876758797471720645L;

	public RsqlBeanStatusException(final String msg) {
		super(msg);
	}

	public RsqlBeanStatusException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
