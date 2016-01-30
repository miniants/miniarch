/*
 * 文 件 名 : SerialNoGenerator.java
 * CopyRright (c) since 2013:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-3-2
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */

package cn.remex.db.rsql.model;

import cn.remex.core.reflect.ReflectUtil;

import java.util.HashMap;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-3-2
 *
 */
public interface SerialNoGenerator {
	/**
	 * 主键map
	 */
	static final HashMap<Class<?>, Integer> ORM_CURMAX_ID = new HashMap<Class<?>, Integer>();
	static final HashMap<Class<?>, Integer> ORM_ID_NUMLEN = new HashMap<Class<?>, Integer>();
	
	/**
	 * 此函数自动生成数据库表中的主键。<br>
	 * @rmx.summary 注意流水号必须是再末尾。<br>
	 * {@link ModelableImpl#obtainAbbreviation()}获得主键id的格式。
	 * 如："%1$s%2$tY%2$tm%2$td%3$09d"中的%3$09d为流水号。
	 * @return String
	 */
	public String generateId();
	/**
	 * @return String
	 */
	public String obtainAbbreviation();
	
	/**
	 * 可重载此方式改变主键生成的格式。Remex提供两个参数%1是表缩写，%2日期，%1是自动递增的序列号。
	 * @rmx.summary 默认的字符串是
	 * %1$sM%2$tY%2$tm%2$td%3$09d
	 * @see String#format(String, Object...);
	 * @return String
	 */
	public String obtainIdFormat();
	/**
	 * @return String
	 */
	public String obtainSerialNoFormat();

	public Object get(String key);

}
