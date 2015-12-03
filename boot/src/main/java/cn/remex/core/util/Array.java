package cn.remex.core.util;

import java.util.Arrays;

public class Array {

	// public static void main(String[] args){
	// HashMap<String, String> hashMapConfig =
	// PropertyReader.getPropertiesHashMap(new Array(), "./default.properties");
	// String value;
	// String [][]a = null;
	// if((value = hashMapConfig.get("htmlStringReplacements"))!=null)
	// a = getStringArray(value);
	// System.out.print(a.toString());
	// }

	final static String D1ARRAYM_REGX = "\"\\s*\\,\\s*\"";
	// 删除二维数组中一维字符串的前后多余字符串{{、}}
	final static String D1ARRAYSE_REGX = "^\\s*\"|\"\\s*$";
	final static String D2ARRAYM_REGX = "\\s*\\}\\s*\\,\\s*\\{\\s*";
	// 删除二维数组字符串的前后多余字符串{{、}}
	final static String D2ARRAYSE_REGX = "^\\s*\\{\\s*\\{\\s*|\\s*}\\s*}\\s*";

	/**
	 * 按照定义一维数组的方式来读取一维数组
	 * 
	 * @param src
	 * @return String[]
	 */
	public static String[] getStringD1Array(final String src) {
		return src.replaceAll(D1ARRAYSE_REGX, "").split(D1ARRAYM_REGX);
	}

	/**
	 * 按照定义二维数组的方式来读取二维数组
	 * 
	 * @param src
	 * @return String[][]
	 */
	public static String[][] getStringD2Array(final String src) {

		// 生成二维数组的一维表达方式
		String[] strArrayD2 = src.replaceAll(Array.D2ARRAYSE_REGX, "").split(
				D2ARRAYM_REGX);
		int len2 = strArrayD2.length;
		// 如果二维数组为空则放弃
		if (len2 <= 0) {
			return null;
		}
		// 生产二维数组中第一个一维数组
		String[] strArrayD1 = strArrayD2[0].replaceAll(D1ARRAYSE_REGX, "")
				.split(D1ARRAYM_REGX);
		int len1 = strArrayD1.length;
		// 如果二维数组中第一一维数组为空则放弃
		if (len1 <= 0) {
			return null;
		}
		// 生成二维数组
		String[][] array = new String[len2][len1];

		for (int i = 0; i < len2; i++) {
			array[i] = strArrayD2[i].replaceAll(D1ARRAYSE_REGX, "").split(
					D1ARRAYM_REGX);
		}
		return array;
	}

	/**
	 * 本函数将参数中的所有byte数组连接起来组成一个新的byte数组。
	 * 
	 * @param first
	 * @param rest
	 * @return
	 */
	public static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
}
