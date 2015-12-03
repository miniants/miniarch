package cn.remex.db.exception;

public class RsqlInitException extends RsqlException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2772697311814651255L;

	private String beanName;

	public RsqlInitException(final String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public RsqlInitException(final String beanName, final String msg) {
		super("数据库RemexSql初始化Bean"+beanName+"异常: "+ msg);
	}

	public RsqlInitException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public String getBeanName() {
		return this.beanName;
	}

}
