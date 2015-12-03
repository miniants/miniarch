/**
 * 
 */
package cn.remex.db.exception;

import cn.remex.core.exception.NestedException;

/**
 * @author Hengyang Liu
 * @since 2012-4-16
 *
 */
public class RsqlDialectException extends NestedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5706405491395408883L;

	public RsqlDialectException(final String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public RsqlDialectException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
