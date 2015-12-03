package cn.remex.db.exception;

public class RsqlExecuteException extends RsqlException{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8607666674635611653L;
	private Class<?> ormBeanClass;
	private Long ormBeanObjectId;

	public RsqlExecuteException(final Class<?> ormBeanClass,final Long ormBeanObjectId,final String msg) {
		super("RemexSql 发生异常:"+ormBeanObjectId+"@"+ormBeanClass+": "+msg);
		this.ormBeanClass=ormBeanClass;
		this.ormBeanObjectId=ormBeanObjectId;
	}

	public RsqlExecuteException(final Class<?> ormBeanClass,final String ormBeanObjectId,final String msg) {
		super("RemexSql 发生异常:"+ormBeanObjectId+"@"+ormBeanClass+": "+msg);
		this.ormBeanClass=ormBeanClass;
		this.ormBeanObjectId=Long.parseLong(ormBeanObjectId);
	}

	public RsqlExecuteException(final String msg) {
		super(msg);
	}

	public RsqlExecuteException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public Class<?> getOrmBeanClass() {
		return this.ormBeanClass;
	}

	public Long getOrmBeanObjectId() {
		return this.ormBeanObjectId;
	}

}
