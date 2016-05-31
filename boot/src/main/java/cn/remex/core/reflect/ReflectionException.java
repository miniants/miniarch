/**
 * 
 */
package cn.remex.core.reflect;

import cn.remex.core.exception.NestedException;

/**
 * 映射异常
 * 凡是映射操作的异常均抛出该异常
 * @author Hengyang Liu
 * @since 2012-4-4
 */
public class ReflectionException extends NestedException{
	private static final long serialVersionUID = 3914255777816768151L;

	/**
	 * 在抛出异常时,标注异常提示信息
	 * @param msg 人工标注异常提示信息
	 */
	public ReflectionException(final String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 在抛出异常时，标注异常提示信息并在控制台输出堆栈信息
	 * @param msg 人工标注异常提示信息
	 * @param cause 异常堆栈信息
	 */
	public ReflectionException(final String msg, final Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
