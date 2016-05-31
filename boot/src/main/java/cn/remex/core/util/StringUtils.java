/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.remex.core.util;

import java.util.*;

/**
 * 多种多样的集合工具类
 * Miscellaneous {@link String} utility methods.
 *
 * <p>Mainly for internal use within the framework; consider
 * <a href="http://jakarta.apache.org/commons/lang/">Jakarta's Commons Lang</a>
 * for a more comprehensive suite of String utilities.
 *
 * <p>This class delivers some simple functionality that should really
 * be provided by the core Java <code>String</code> and {@link StringBuffer}
 * classes, such as the ability to {@link #replace} all occurrences of a given
 * substring in a target string. It also provides easy-to-use methods to convert
 * between delimited strings, such as CSV strings, and collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @since 16 April 2001
 * @see org.apache.commons.lang.StringUtils
 */
public abstract class StringUtils {

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	private static final String FOLDER_SEPARATOR = "/";

	private static final String TOP_PATH = "..";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";


	//---------------------------------------------------------------------
	// General convenience methods for working with Strings
	//---------------------------------------------------------------------

	/**
	 * 添加所提供的字符串到提供的数组，如果数组为空，则创建一个数组，有值，则将string添加到数组中，
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 * @param array the array to append to (can be <code>null</code>) 所提供的数组，可以为空
	 * @param str the String to append  要添加的字符串
	 * @return the new array (never <code>null</code>) 返回一个新的数组，不可能为null
	 */
	public static String[] addStringToArray(final String[] array, final String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] {str};
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * 
	 * Apply the given relative path to the given path,
	 * assuming standard Java folder separation (i.e. "/" separators);
	 * @param path the path to start from (usually a full file path)
	 * @param relativePath the relative path to apply
	 * (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(final String path, final String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		}
		else {
			return relativePath;
		}
	}

	/**
	 * 将一个数组转换成一个string，默认分隔符为","
	 * Convenience method to return a String array as a CSV String.
	 * E.g. useful for <code>toString()</code> implementations.
	 * @param arr the array to display 数组
	 * @return the delimited String 带分隔符的string
	 */
	public static String arrayToCommaDelimitedString(final Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	/**
	 * 将一个数组转换成一个带分隔符的string，分隔符可以设置
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param arr the array to display 数组
	 * @param delim the delimiter to use (probably a ",") 分隔符
	 * @return the delimited String 处理后的字符串
	 */
	public static String arrayToDelimitedString(final Object[] arr, final String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Capitalize a <code>String</code>, changing the first letter to
	 * upper case as per {@link Character#toUpperCase(char)}.
	 * No other letters are changed.
	 * @param str the String to capitalize, may be <code>null</code>
	 * @return the capitalized String, <code>null</code> if null
	 */
	public static String capitalize(final String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * 
	 * Normalize the path by suppressing sequences like "path/.." and
	 * inner simple dots.
	 * <p>The result is convenient for path comparison. For other uses,
	 * notice that Windows separators ("\") are replaced by simple slashes.
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String cleanPath(final String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * 将一个集合转换成带分隔符的字符串，分隔符为","
	 * Convenience method to return a Collection as a CSV String.
	 * E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display 集合
	 * @return the delimited String 处理的字符串
	 */
	public static String collectionToCommaDelimitedString(final Collection<String> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * 将一个集合转换成带分隔符的字符串，可以设置分隔符
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display 集合
	 * @param delim the delimiter to use (probably a ",") 要设置的分隔符
	 * @return the delimited String 处理后的string
	 */
	public static String collectionToDelimitedString(final Collection<String> coll, final String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * 将一个集合转换成带分隔符的string,元素用前缀后缀包围
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display 集合
	 * @param delim the delimiter to use (probably a ",") 分隔符
	 * @param prefix the String to start each element with 前缀
	 * @param suffix the String to end each element with  后缀
	 * @return the delimited String 处理后的string
	 */
	public static String collectionToDelimitedString(final Collection<String> coll, final String delim, final String prefix, final String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * 将一个带分隔符的string转换成set。注意，会去掉重复的元素
	 * Convenience method to convert a CSV string list to a set.
	 * Note that this will suppress duplicates.
	 * @param str the input String 要处理的string
	 * @return a Set of String entries in the list 集合
	 */
	public static Set<String> commaDelimitedListToSet(final String str) {
		Set<String> set = new TreeSet<String>();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	/**
	 * 将一个带分隔符的list转化成一个包含string元素的数组
	 * Convert a CSV list into an array of Strings.
	 * @param str the input String 要处理的string
	 * @return an array of Strings, or the empty array in case of empty input 数组可以为空
	 */
	public static String[] commaDelimitedListToStringArray(final String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * 将所提供的两个字符串型数组合并成一个
	 * Concatenate the given String arrays into one,
	 * with overlapping array elements included twice. 重复的元素会重复保留，顺序也会保留
	 * <p>The order of elements in the original arrays is preserved.
	 * @param array1 the first array (can be <code>null</code>) 第一个数组
	 * @param array2 the second array (can be <code>null</code>) 第二个数组
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>) 处理后的数组
	 */
	public static String[] concatenateStringArrays(final String[] array1, final String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}


	/**
	 * 检查所提供的字符序列是否包含有空格
	 * Check whether the given CharSequence contains any whitespace characters.
	 * @param str the CharSequence to check (may be <code>null</code>) 待检查的字符序列
	 * @return <code>true</code> if the CharSequence is not empty and 布尔值
	 * contains at least 1 whitespace character
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean containsWhitespace(final CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查所给字符串是否包含空格
	 * Check whether the given String contains any whitespace characters.
	 * @param str the String to check (may be <code>null</code>) 要处理的字符串 
	 * @return <code>true</code> if the String is not empty and
	 * contains at least 1 whitespace character 字符串不为空并且至少包含一个空格
	 * @see #containsWhitespace(CharSequence)
	 */
	public static boolean containsWhitespace(final String str) {
		return containsWhitespace((CharSequence) str);
	}

	/**
	 * 计算所给子符串在字符串中的个数
	 * Count the occurrences of the substring in string s.
	 * @param str string to search in. Return 0 if this is null. 子字符串  ，为空返回0
	 * @param sub string to search for. Return 0 if this is null. 被查询的字符串 ，为空返回0
	 */
	public static int countOccurrencesOf(final String str, final String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * 删除字符串中所有出现的子字符串
	 * Delete all occurrences of the given substring.
	 * @param inString the original String  字符串
	 * @param pattern the pattern to delete all occurrences of 要删除的字符串
	 * @return the resulting String 处理后的字符串
	 */
	public static String delete(final String inString, final String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * 
	 * Delete any character in a given String.
	 * @param inString the original String
	 * @param charsToDelete a set of characters to delete.
	 * E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(final String inString, final String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * 
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of potential
	 * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(final String str, final String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of potential
	 * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @param charsToDelete a set of characters to delete. Useful for deleting unwanted
	 * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(final String str, final String delimiter, final String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] {str};
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}


	//---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	//---------------------------------------------------------------------

	/**
	 * 校验所给字符串是否以特殊的后缀结尾，忽略大小写
	 * Test if the given String ends with the specified suffix,
	 * ignoring upper/lower case.
	 * @param str the String to check 要检查的字符串
	 * @param suffix the suffix to look for 后缀
	 * @see java.lang.String#endsWith
	 */
	public static boolean endsWithIgnoreCase(final String str, final String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * 从所给路径中抽取文件名
	 * Extract the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * @param path the file path (may be <code>null</code>) 文件路径
	 * @return the extracted filename, or <code>null</code> if none 返回文件名，为空返回null
	 */
	public static String getFilename(final String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
	}

	/**
	 * 从所给路径中截取文件扩展名
	 * Extract the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "txt".
	 * @param path the file path (may be <code>null</code>) 文件路径
	 * @return the extracted filename extension, or <code>null</code> if none 扩展名，空返回null
	 */
	public static String getFilenameExtension(final String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(sepIndex + 1) : null;
	}

	/**
	 * 检查所给字节序列既不是null也不是长度为0，注意，包含空格也会返回true
	 * Check that the given CharSequence is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
	 * <p><pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>) 字节序列
	 * @return <code>true</code> if the CharSequence is not null and has length 不为空且长度不为0 返回true
	 * @see #hasText(String)
	 */
	public static boolean hasLength(final CharSequence str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 检查所给字符串既不是null也不是长度为0，注意，包含空格也会返回true
	 * Check that the given String is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a String that purely consists of whitespace.
	 * @param str the String to check (may be <code>null</code>) 字符串
	 * @return <code>true</code> if the String is not null and has length 如果不为空且有长度返回true
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(final String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * 检查所给字符序列是否存在实际值，特别强调:只有当string不为null，且长度大于0，且至少包含一个不为空格的字节才返回true
	 * Check whether the given CharSequence has actual text.
	 * More specifically, returns <code>true</code> if the string not <code>null</code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>) 要处理的字符序列
	 * @return <code>true</code> if the CharSequence is not <code>null</code>,  布尔值
	 * its length is greater than 0, and it does not contain whitespace only
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(final CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查所给字符串是否存在实际值，特别强调:只有当string不为null，且长度大于0，且至少包含一个不为空格的字节才返回true
	 * Check whether the given String has actual text.
	 * More specifically, returns <code>true</code> if the string not <code>null</code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * @param str the String to check (may be <code>null</code>) 字符串
	 * @return <code>true</code> if the String is not <code>null</code>, its length is
	 * greater than 0, and it does not contain whitespace only 布尔值
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(final String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * 将所给字符串型的数组合并成一个，并却掉重复元素
	 * Merge the given String arrays into one, with overlapping
	 * array elements only included once.
	 * <p>The order of elements in the original arrays is preserved
	 * (with the exception of overlapping elements, which are only
	 * included on their first occurence).
	 * @param array1 the first array (can be <code>null</code>) 数组1 
	 * @param array2 the second array (can be <code>null</code>) 数组2 
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)处理后的新数组
	 */
	public static String[] mergeStringArrays(final String[] array1, final String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List<String> result = new ArrayList<String>();
		result.addAll(Arrays.asList(array1));
		for (String element : array2) {
			String str = element;
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * 
	 * Parse the given <code>localeString</code> into a {@link Locale}.
	 * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
	 * @param localeString the locale string, following <code>Locale's</code>
	 * <code>toString()</code> format ("en", "en_UK", etc);
	 * also accepts spaces as separators, as an alternative to underscores
	 * @return a corresponding <code>Locale</code> instance
	 */
	public static Locale parseLocaleString(final String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = parts.length > 0 ? parts[0] : "";
		String country = parts.length > 1 ? parts[1] : "";
		String variant = "";
		if (parts.length >= 2) {
			// There is definitely a variant, and it is everything after the country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return language.length() > 0 ? new Locale(language, country, variant) : null;
	}

	/**
	 * 比较两个标准形式的文件路径
	 * Compare two paths after normalization of them.
	 * @param path1 first path for comparison	文件路径1 
	 * @param path2 second path for comparison  文件路径2  
	 * @return whether the two paths are equivalent after normalization 布尔值
	 */
	public static boolean pathEquals(final String path1, final String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * 将字符串用单引号引用
	 * Quote the given String with single quotes.
	 * @param str the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"),
	 * or <code>null<code> if the input was <code>null</code> 处理后的字符串 ，为空返回null
	 */
	public static String quote(final String str) {
		return str != null ? "'" + str + "'" : null;
	}

	/**
	 * 将所给字符串转化成用单引号引用，对象的话直接返回
	 * Turn the given Object into a String with single quotes
	 * if it is a String; keeping the Object as-is else.
	 * @param obj the input Object (e.g. "myString") 对象
	 * @return the quoted String (e.g. "'myString'"),
	 * or the input object as-is if not a String 字符串转化成用单引号引用，对象的话直接返回
	 */
	public static Object quoteIfString(final Object obj) {
		return obj instanceof String ? quote((String) obj) : obj;
	}

	/**
	 * 从所给数组中移除重复的string，同时排序
	 * Remove duplicate Strings from the given array.
	 * Also sorts the array, as it uses a TreeSet.
	 * @param array the String array 数组
	 * @return an array without duplicates, in natural sort order 排序且无重复元素的集合
	 */
	public static String[] removeDuplicateStrings(final String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set<String> set = new TreeSet<String>();
		for (String element : array) {
			set.add(element);
		}
		return toStringArray(set);
	}

	/**
	 * 将所给字符串中的一个子字符串替换成另一个子字符串
	 * Replace all occurences of a substring within a string with
	 * another string.
	 * @param inString String to examine 原始字符串
	 * @param oldPattern String to replace 即将被替换的
	 * @param newPattern String to insert 要插入的
	 * @return a String with the replacements 处理后的字符串
	 */
	public static String replace(final String inString, final String oldPattern, final String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the param of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * 将所给的数组排序后返回，空则返回一个新的空数组
	 * Turn given source String array into sorted array.
	 * @param array the source array 数组
	 * @return the sorted array (never <code>null</code>) 排序后的数组
	 */
	public static String[] sortStringArray(final String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}


	//---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	//---------------------------------------------------------------------

	/**
	 * 从字符串中分离第一个分隔符，使结果中不包含分隔符，返回一个字符串数组
	 * Split a String at the first occurrence of the delimiter.
	 * Does not include the delimiter in the result.
	 * @param toSplit the string to split 字符串
	 * @param delimiter to split the string up with 分隔符
	 * @return 一个两个元素的字符串数组,第一个为分隔符前面的，第二个为分隔符后面的， (每一个都不包含分隔符，);如果没有分隔符则第二个元素为null
	 * a two element array with param 0 being before the delimiter, and
	 * param 1 being after the delimiter (neither element includes the delimite
	 * or <code>null</code> if the delimiter wasn't found in the given input String
	 */
	public static String[] split(final String toSplit, final String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] {beforeDelimiter, afterDelimiter};
	}

	/**
	 * 将一个数组通过分隔符分割成一个属性集，左边是属性，右边是值，在加入属性集前会去掉空格
	 * Take an array Strings and split each element based on the given delimiter.
	 * A <code>Properties</code> instance is then generated, with the left of the
	 * delimiter providing the key, and the right of the delimiter providing the value.
	 * <p>Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 * @param array the array to process 数组
	 * @param delimiter to split each element using (typically the equals symbol) 分隔符
	 * @return a <code>Properties</code> instance representing the array contents,
	 * or <code>null</code> if the array to process was null or empty 属性集，没有值返回null
	 */
	public static Properties splitArrayElementsIntoProperties(final String[] array, final String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * 
	 * Take an array Strings and split each element based on the given delimiter.
	 * A <code>Properties</code> instance is then generated, with the left of the
	 * delimiter providing the key, and the right of the delimiter providing the value.
	 * <p>Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 * @param array the array to process
	 * @param delimiter to split each element using (typically the equals symbol)
	 * @param charsToDelete one or more characters to remove from each element
	 * prior to attempting the split operation (typically the quotation mark
	 * symbol), or <code>null</code> if no removal should occur
	 * @return a <code>Properties</code> instance representing the array contents,
	 * or <code>null</code> if the array to process was <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(
			final String[] array, final String delimiter, final String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (String element2 : array) {
			String element = element2;
			if (charsToDelete != null) {
				element = deleteAny(element2, charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * 检查所提供的字符串是否是以特殊的前缀开始,忽略大小写
	 * Test if the given String starts with the specified prefix,
	 * ignoring upper/lower case.
	 * @param str the String to check 字符串
	 * @param prefix the prefix to look for 前缀
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(final String str, final String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * 去掉所给路径中文件名的扩展名
	 * Strip the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "mypath/myfile".
	 * @param path the file path (may be <code>null</code>) 路径
	 * @return the path with stripped filename extension,
	 * or <code>null</code> if none 去掉扩展名的文件路径，没有返回null
	 */
	public static String stripFilenameExtension(final String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(0, sepIndex) : path;
	}

	/**
	 * 检查所给子字符序列是否在字符序列中所提供的位置
	 * Test whether the given string matches the given substring
	 * at the given param.
	 * @param str the original string (or StringBuffer) 字符序列
	 * @param index the param in the original string to start matching against 起始位置
	 * @param substring the substring to match at the given param 子字符序列
	 */
	public static boolean substringMatch(final CharSequence str, final int index, final CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(final String str, final String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String
	 * was <code>null</code>)
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(
			final String str, final String delimiters, final boolean trimTokens, final boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * 通过使用 HTTP 语言头结点判断 RFC 3306复合的语言标签
	 * Determine the RFC 3066 compliant language tag,
	 * as used for the HTTP "Accept-Language" header.
	 * @param locale the Locale to transform to a language tag 地区化的语言标记
	 * @return the RFC 3066 compliant language tag as String  RFC 3306复合的语言标签
	 */
	public static String toLanguageTag(final Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}

	/**
	 * 将所提供的集合复制到一个字符串数组中，集合中必须只包含string元素
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy 集合
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>) 字符串型数组，传入null则返回null
	 */
	public static String[] toStringArray(final Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * 将所提供的枚举类复制到一个字符串数组
	 * Copy the given Enumeration into a String array.
	 * The Enumeration must contain String elements only.
	 * @param enumeration the Enumeration to copy 枚举类
	 * @return the String array (<code>null</code> if the passed-in
	 * Enumeration was <code>null</code>) 字符串数组 ，传入null则返回null
	 */
	public static String[] toStringArray(final Enumeration<?> enumeration) {
		if (enumeration == null) {
			return null;
		}
		List<?> list = Collections.list(enumeration);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 去掉所给字符串中所有的空格 ，包括起始的和中间的，
	 * Trim <i>all</i> whitespace from the given String:
	 * leading, trailing, and inbetween characters.
	 * @param str the String to check  截取前的字符串 
	 * @return the trimmed String 截取后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(final String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			}
			else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * 将所给字符型数组中的元素都删除空格，每个元素都调用<code>String.trim()</code>
	 * Trim the elements of the given String array,
	 * calling <code>String.trim()</code> on each of them.
	 * @param array the original String array 数组
	 * @return the resulting array (of the same size) with trimmed elements 同样大小的处理后数组
	 */
	public static String[] trimArrayElements(final String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = element != null ? element.trim() : null;
		}
		return result;
	}

	/**
	 * 删除所给字符串中指定的字符
	 * Trim all occurences of the supplied leading character from the given String.
	 * @param str the String to check 字符串
	 * @param leadingCharacter the leading character to be trimmed 要删除的字符
	 * @return the trimmed String 处理后的字符串
	 */
	public static String trimLeadingCharacter(final String str, final char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * 删除所给字符串起始位置的空格
	 * Trim leading whitespace from the given String.
	 * @param str the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(final String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * 删除所给字符串中所有要删除的字符
	 * Trim all occurences of the supplied trailing character from the given String.
	 * @param str the String to check 字符串
	 * @param trailingCharacter the trailing character to be trimmed 要删除的字符
	 * @return the trimmed String 处理后的字符串
	 */
	public static String trimTrailingCharacter(final String str, final char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * 删除所给字符串结尾的空格 
	 * Trim trailing whitespace from the given String.
	 * @param str the String to check 字符串
	 * @return the trimmed String 处理后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(final String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * 删除字符串起始和结尾的空格
	 * Trim leading and trailing whitespace from the given String.
	 * @param str the String to check 字符串
	 * @return the trimmed String 处理后的字符串
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimWhitespace(final String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * TODO
	 * 遗弃一个string，改变第一个字母为小写，其他字母不变
	 * Uncapitalize a <code>String</code>, changing the first letter to
	 * lower case as per {@link Character#toLowerCase(char)}.
	 * No other letters are changed.
	 * @param str the String to uncapitalize, may be <code>null</code> 将被遗弃的string，可能为null
	 * @return the uncapitalized String, <code>null</code> if null 没有被利用的string，如果null返回null
	 */
	public static String uncapitalize(final String str) {
		return changeFirstCharacterCase(str, false);
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example,
	 * "this.name.is.qualified", returns "qualified".
	 * @param qualifiedName the qualified name
	 */
	public static String unqualify(final String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example,
	 * "this:name:is:qualified" returns "qualified" if using a ':' separator.
	 * @param qualifiedName the qualified name
	 * @param separator the separator
	 */
	public static String unqualify(final String qualifiedName, final char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	private static String changeFirstCharacterCase(final String str, final boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		}
		else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

}


