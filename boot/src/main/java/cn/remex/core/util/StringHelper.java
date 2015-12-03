package cn.remex.core.util;

import cn.remex.RemexConstants;
import cn.remex.core.exception.StringHandleException;
import org.apache.oro.text.regex.*;

import java.lang.reflect.Type;
import java.util.HashMap;
/**
 *	String 处理工具类
 */
public class StringHelper implements RemexConstants {
	private static HashMap<String, Pattern> patterns = new HashMap<String, Pattern>();

	private static HashMap<Class<?>, String> ClassAbbreviation = new HashMap<Class<?>, String>();

	/**
	 * 设置字符串等宽，及对齐方式
	 * @param sourString 要处理的String
	 * @param width 列宽
	 * @param textAlign left or right 左或右
	 * @return String 字符串
	 */
	public static String aequilate(final String sourString, final int width, final String... textAlign) {
		boolean textAlignLeft = "left".equals(textAlign != null && textAlign.length > 0 ? textAlign[0] : "left") ? true
				: false;
		StringBuilder sb = new StringBuilder(sourString);
		if (sourString.length() < width) {
			for (int i = width - sourString.length(); i > 0; i--) {
				if (textAlignLeft) {
					sb.append(" ");
				} else {
					sb.insert(0, " ");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 将字符串中的半角数字替换为全角数字
	 * @param string 要处理的字符串
	 * @return String 处理后的字符串
	 */
	public static String fomatString(final String string) {
		return string.replaceAll("0", "０").replaceAll("1", "１").replaceAll("2", "２").replaceAll("3", "３")
				.replaceAll("4", "４").replaceAll("5", "５").replaceAll("6", "６").replaceAll("7", "７")
				.replaceAll("8", "８").replaceAll("9", "９");
	}

	/**
	 * 取类名的所有大写字母
	 * @param type 类名
	 * @return String 返回的字符串
	 */
	public static String getAbbreviation(final Type type) {
		if (type instanceof Class<?>) {
			String abbr = ClassAbbreviation.get(type);
			if (null == abbr) {
				String sn = getClassSimpleName(type);
				abbr = sn.replaceAll("[^A-Z]", "");
				ClassAbbreviation.put((Class<?>) type, abbr);
			}
			return abbr;
		} else {
			throw new StringHandleException("因为此Type非Class类型，无法为Type" + type.toString() + "类指定缩写！");
			// return null;//never arrived here
		}

	}
	/**
	 * 为类简名
	 * @param type 类名
	 * @return String 返回的类简名
	 */
	
	public static String getClassSimpleName(final Type type) {
		if (type instanceof Class<?>) {
			String sn = ((Class<?>) type).getSimpleName();
			return sn.split("\\$\\$EnhancerByCGLIB\\$\\$")[0];
		} else {
			throw new StringHandleException("因为此Type非Class类型，无法为Type" + type.toString() + "类简名！");
			// return null;//never arrived here
		}

	}
	/**
	 * 获取类名
	 * @param type 类名
	 * @return String 字符串
	 */
	public static String getClassName(final Type type) {
		if (type instanceof Class<?>) {
			String sn = ((Class<?>) type).getName();
			return sn.split("\\$\\$EnhancerByCGLIB\\$\\$")[0];
		} else {
			throw new StringHandleException("因为此Type非Class类型，无法为Type" + type.toString() + "类简名！");
			// return null;//never arrived here
		}

	}
	/**
	 * 字符串第一个字母转为小写
	 * @param string 字符串
	 * @return String 转化结果
	 */
	public static String lowerFirstLetter(final String string) {
		StringBuilder sb = new StringBuilder();
		return sb.append(string.substring(0, 1).toLowerCase()).append(string.substring(1)).toString();
	}

	/**
	 * 验证字符串是否匹配正则表达式
	 * @param string 被测试的字符串
	 * @param regex 匹配的正则表达式
	 * @param notMatchExceptionMsg  没有匹配时报异常的信息
	 * @return　MatchResult 匹配操作的结果
	 */
	public static MatchResult match(final String string, final String regex, final String notMatchExceptionMsg) {
		PatternMatcher m = new Perl5Matcher();
		Pattern p = obtainPattern(regex);
		if (!m.contains(string, p) && null != notMatchExceptionMsg) {
			throw new StringHandleException(notMatchExceptionMsg);
		}
		return m.getMatch();
	}
	private static synchronized Pattern obtainPattern(String regex){
		Pattern p = patterns.get(regex);
		if (null == p) {
			try {
				p = compiler.compile(regex);
				patterns.put(regex, p);
			} catch (MalformedPatternException e) {
				throw new StringHandleException("连接正则表达式发生异常！", e);
			}
		}	
		return p;
	}
	
	/**
	 * @param string 
	 * @param searchRegx
	 * @param replaceRegx 替换的表达式，支持$1来获取查找到的组号。
	 * @param replaceTimes -1 替换所有，否则替换replaceTimes次
	 * @return
	 */
	public static String substitute(final String string,final String searchRegx,final String replaceRegx,final int replaceTimes){
		PatternMatcher m = new Perl5Matcher();
		Pattern sp = obtainPattern(searchRegx);
		String result = Util.substitute(m, sp, new Perl5Substitution(replaceRegx), string, replaceTimes);
		return result;
	}
	/**
	 * 将字符串指定长度后三位用...代替,并将半角替换为全角数字
	 * @param string 字符串
	 * @param maxLength 最大长度
	 * @return 处理后的string
	 */
	public static String substringByMaxLength(String string, final int maxLength) {
		if (string.length() > maxLength) {
			string = string.substring(0, maxLength - 3) + "...";
		}
		return string.replaceAll("0", "０").replaceAll("1", "１").replaceAll("2", "２").replaceAll("3", "３")
				.replaceAll("4", "４").replaceAll("5", "５").replaceAll("6", "６").replaceAll("7", "７")
				.replaceAll("8", "８").replaceAll("9", "９");
	}
	/**
	 * 设置起始位置截取字符串，并将半角数字替换为全角数字
	 * @param string 要处理的字符串
	 * @param start 起始位置
	 * @param end 终止
	 * @return String 处理后的字符串
	 */
	public static String substringByStartAndEnd(String string, final int start, int end) {
		if (start < 0 || end <= start) {
			return "字符长度设置错误!";
		}
		if (end > string.length()) {
			end = string.length();
		}
		string = string.substring(start, end);
		return string.replaceAll("0", "０").replaceAll("1", "１").replaceAll("2", "２").replaceAll("3", "３")
				.replaceAll("4", "４").replaceAll("5", "５").replaceAll("6", "６").replaceAll("7", "７")
				.replaceAll("8", "８").replaceAll("9", "９");
	}
	/**
	 * 将字符串首字母转为大写
	 * @param string 要处理的字符串
	 * @return String 处理后的字符串
	 */
	public static String upperFirstLetter(final String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

}
