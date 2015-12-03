package cn.remex.db.exception;

public class RsqlDBExecuteException extends RsqlException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2343460170661679027L;
	private String beanNamedParams;

	private String sqlString;

	public RsqlDBExecuteException(final String msg) {
		super(msg);
	}
	public RsqlDBExecuteException(final String sqlString, final String beanNamedParams,final String msg) {
		super("执行Sql：\r\n"+sqlString+"\r\n参数为：\r\n"+beanNamedParams.toString()+"\r\n信息："+msg);
		this.sqlString=sqlString;
		this.beanNamedParams=beanNamedParams;
	}

	public RsqlDBExecuteException(final String sqlString, final String beanNamedParams,final String msg,final Throwable cause) {
		super("\r\n执行Sql：\r\n"+sqlString+"\r\n参数为：\r\n"+beanNamedParams.toString()+"\r\n信息："+msg,cause);
		this.sqlString=sqlString;
		this.beanNamedParams=beanNamedParams;
	}
	public RsqlDBExecuteException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}
	public String getBeanNamedParams() {
		return this.beanNamedParams;
	}
	public String getSqlString() {
		return this.sqlString;
	}


}
