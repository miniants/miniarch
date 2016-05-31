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
	Logger logger = Logger.getLogger(RemexConstants.class);
	boolean loggerDebug = logger.isDebugEnabled();

	PatternCompiler compiler = new Perl5Compiler();

	enum UserType{
		SYSTEM , ADMIN , B_USER , C_USER
	}
}
