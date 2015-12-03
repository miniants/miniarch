package cn.remex.core.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
/**
 * 配置文件读取类
 */
public class PropertyReader {
	/**
	 * java读取配置文件
	 * @param o 对象
	 * @param propertyFile  配置文件名
	 * @return Properties 属性集
	 */
	public static Properties getProperties(final Object o, final String propertyFile) {
		InputStream is = o.getClass().getResourceAsStream(propertyFile);
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (Exception e) {
			System.err.println("Can't read the properties file. "
					+ "Make sure "+propertyFile+" is in the CLASSPATH");
		}
		return properties;

	}

	/**
	 * 将配置文件转换成hashMap返回
	 * Retrieve the properties as Hashtable
	 * @param o 对象
	 * @param propertyFile 配置文件名
	 * @return a hashtable 哈希表
	 */
	public static HashMap<String, String> getPropertiesHashMap(final Object o, final String propertyFile) {
		Properties properties = getProperties(o, propertyFile);
		Enumeration<?> propNames = properties.propertyNames();
		HashMap<String, String> hashMap = new HashMap<String, String>();
		while (propNames.hasMoreElements()) {
			String key = (String) propNames.nextElement();
			String value = properties.getProperty(key);
			hashMap.put(key, value);
		}
		return hashMap;
	}
}
