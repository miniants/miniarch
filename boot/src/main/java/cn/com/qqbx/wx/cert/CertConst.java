package cn.com.qqbx.wx.cert;

import org.apache.log4j.Logger;

public interface CertConst {
	public class TimeLogger{};
	public static Logger logger = Logger.getLogger(CertConst.class);
	public static boolean loggerDebug = logger.isDebugEnabled();
}
