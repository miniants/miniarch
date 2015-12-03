package cn.remex.db.exception;


public class RsqlTypeException extends RsqlException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7238790019439446650L;

	private int sqlType;

	public RsqlTypeException(final int sqlType,final String msg) {
		super(msg);
		this.sqlType=sqlType;
	}

	public int getSqlType() {
		return this.sqlType;
	}

}
