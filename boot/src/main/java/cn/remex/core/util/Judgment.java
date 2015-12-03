package cn.remex.core.util;
/**
 *	判断一个对象是不是空
 */
public class Judgment {
	/**
	 * 判断一个对象是null或""
	 * @param vStr java对象
	 * @return boolean 布尔值
	 */
	public static boolean nullOrBlank (Object vStr){
		if(null == vStr || "".equals(vStr) )
			return true;
		return false;
	}
	/**
	 * 判断一个对象是否是0
	 * @param vStr  java对象
	 * @return boolean 布尔值
	 */
	public static boolean isZero (Object vStr){
		if(Arith.sub(0, vStr) == 0)
			return true;
		return false;
	}
	/**
	 * 判断一个对象是否为empty
	 * @param vStr java对象
	 * @return boolean 布尔值
	 */
	public static boolean notEmpty (Object vStr){
		if(null != vStr && vStr.toString().trim().length()>0 )
			return true;
		return false;
	}
}
