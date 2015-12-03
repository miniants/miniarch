/**
 * 
 */
package cn.remex;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;

/**
 * @author Hengyang Liu
 * @since 2012-4-3
 *
 */
public interface RemexConstants {
	public class TimeLogger{}
	
	public static Logger logger = Logger.getLogger(RemexConstants.class);
	public static boolean loggerDebug = logger.isDebugEnabled();
	public static Logger timeLogger = Logger.getLogger(cn.remex.RemexConstants.TimeLogger.class);

	public static PatternCompiler compiler = new Perl5Compiler();
}
