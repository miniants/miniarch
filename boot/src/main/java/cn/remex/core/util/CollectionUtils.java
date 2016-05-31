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

import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;

import java.util.*;
import java.util.function.Consumer;

/**
 * 多种多样的集合工具类
 * Miscellaneous collection utility methods.
 * Mainly for internal use within the framework.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 1.1.3
 */
public abstract class CollectionUtils {

	/**
	 * Convert the supplied array into a List. A primitive array gets
	 * converted into a List of the appropriate wrapper type.
	 * <p>A <code>null</code> source value will be converted to an
	 * empty List.
	 * 将数组转化为List
	 * @param source the (potentially primitive) array  数组
	 * @return the converted List result  返回List
	 * @see ObjectUtils#toObjectArray(Object)
	 */
	public static List<Object> arrayToList(final Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	/**
	 * Check whether the given Enumeration contains the given element.
	 * 判断枚举类是否包含一个对象
	 * @param enumeration the Enumeration to check  枚举类
	 * @param element the element to look for  对象
	 * @return <code>true</code> if found, <code>false</code> 包含返回true,反之,false
	 */
	public static boolean contains(final Enumeration<?> enumeration, final Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Iterator contains the given element.
	 * 判断Iterator是否包含一个对象
	 * @param iterator the Iterator to check	Iterator
	 * @param element the element to look for	一个对象
	 * @return <code>true</code> if found, <code>false</code> else	包含返回true,反之,false
	 */
	public static boolean contains(final Iterator<?> iterator, final Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return <code>true</code> if any element in '<code>candidates</code>' is
	 * contained in '<code>source</code>'; otherwise returns <code>false</code>.
	 * 判断一个集合中包含另一个集合,返回true 或 false
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static boolean containsAny(final Collection<?> source, final Collection<?> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object name : candidates) {
			if (source.contains(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>Enforces the given instance to be present, rather than returning
	 * <code>true</code> for an equal element as well.
	 * 判断一个集合中包含另一个集合,返回true 或 false
	 * @param collection the Collection to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean containsInstance(final Collection<?> collection, final Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Return the first element in '<code>candidates</code>' that is contained in
	 * '<code>source</code>'. If no element in '<code>candidates</code>' is present in
	 * '<code>source</code>' returns <code>null</code>. Iteration order is
	 * {@link Collection} implementation specific.
	 * 返回在candidates中包含的第一个source对象,如果没有,返回null
	 * @param source the source Collection 元集合
	 * @param candidates the candidates to search for 要查找的集合范围
	 * @return the first present object, or <code>null</code> if not found 返回一个对象或null
	 */
	public static Object findFirstMatch(final Collection<?> source, final Collection<?> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Find a single value of the given type in the given Collection.
	 * 从所给集合中找到一个值
	 * @param collection the Collection to search 要搜索的集合
	 * @param type the type to look for java类型
	 * @return a value of the given type found if there is a clear match,
	 * or <code>null</code> if none or more than one such value found
	 * 存在一个或多个
	 */
	public static Object findValueOfType(final Collection<?> collection, final Class<?> type) {
		if (isEmpty(collection)) {
			return null;
		}
		Object value = null;
		for (Object obj : collection) {
			if (type == null || type.isInstance(obj)) {
				if (value != null) {
					// More than one value found... no clear single value.
					return null;
				}
				value = obj;
			}
		}
		return value;
	}

	/**
	 * Find a single value of one of the given types in the given Collection:
	 * searching the Collection for a value of the first type, then
	 * searching for a value of the second type, etc.
	 * 在所提供的集合中根据所给类型找到一个值，接着查找第二种类型的值,以此类推...
	 * @param collection the collection to search 要被搜索的集合
	 * @param types the types to look for, in prioritized order 类型
	 * @return a value of one of the given types found if there is a clear match,
	 * or <code>null</code> if none or more than one such value found
	 * 如果存在所提供的类，返回对象，反之，返回null
	 */
	public static Object findValueOfType(final Collection<?> collection, final Class<?>[] types) {
		if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * 判断一个集合是否只包含一个唯一的对象
	 * @param collection the Collection to check 被搜索的集合
	 * @return <code>true</code> if the collection contains a single reference or
	 * multiple references to the same instance, <code>false</code> else 如果包含返回true,反之false
	 */
	public static boolean hasUniqueObject(final Collection<?> collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Object elem : collection) {
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			}
			else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return <code>true</code> if the supplied Collection is <code>null</code>
	 * or empty. Otherwise, return <code>false</code>.
	 * 判断集合为null或empty,返回true或false
	 * @param collection the Collection to check 集合
	 * @return whether the given Collection is empty  true 或 false
	 */
	public static boolean isEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Return <code>true</code> if the supplied Map is <code>null</code>
	 * or empty. Otherwise, return <code>false</code>.
	 * 判断所给集合是否为null 或empty,返回true 或false
	 * @param map the Map to check 被检查的Map 
	 * @return whether the given Map is empty	 true 或 false
	 */
	public static boolean isEmpty(final Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Merge the given array into the given Collection.
	 * 将数组加入到集合中
	 * @param array the array to merge (may be <code>null</code>) 要添加的数组
	 * @param collection the target Collection to merge the array into 集合
	 */
	public static void mergeArrayIntoCollection(final Object array, final Collection<Object> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection must not be null");
		}
		Object[] arr = ObjectUtils.toObjectArray(array);
		for (Object element : arr) {
			collection.add(element);
		}
	}

	/**
	 * Merge the given Properties instance into the given Map,
	 * copying all properties (key-value pairs) over.
	 * <p>Uses <code>Properties.propertyNames()</code> to even catch
	 * default properties linked into the original Properties instance.
	 * 将属性值放入map中
	 * @param props the Properties instance to merge (may be <code>null</code>) 属性值
	 * @param map the target Map to merge the properties into  Map对象
	 */
	public static void mergePropertiesIntoMap(final Properties props, final Map<String, String> map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				map.put(key, props.getProperty(key));
			}
		}
	}
	
	
	
	/**
	 * List转Map工具方法
	 * 原理:指定list中对象的某个属性为转换后map的key,这个对象为map的value
	 * @author lanchenghao
	 * @param <T> 对象的类型
	 * @since 201310
	 * @param list:欲转换list
	 * @param key:指定list中对象某个属性，作为转换后map的key
	 * @return Map<Object,T>:转换后的map
	 */
	public static <T> Map<Object,T> listToMap(List<T> list,String key) {
		Map<Object,T> map = new HashMap<Object,T>();
		for(T o :list){
			/**将指定的属性值作为key,对象值为o放入map @see ReflectUtil*/
			map.put(ReflectUtil.invokeGetter(key, o), o);//invoke:执行list中对象的getter方法
		}
		return map;		
	}
	/**
	 * 获取符合访问链的beanlist都查询出来。
	 * @param bean java对象
	 * @param fields 属性值
	 * @param pids 	父id
	 * @param level 层级数
	 */
	public static void invokeListField(Object bean, String[] fields, String[] pids,int level){
		Object subBean = bean;
		if(fields.length<=level)return;
		String r = fields[level];
		@SuppressWarnings("unchecked")
		List<Object> sbs = (List<Object>) ReflectUtil.invokeGetter(r, subBean);
		
		if(level<pids.length){//如果指定了父id则根据数据链来查询
			sbs = new ArrayList<Object>();
			Object o = CollectionUtils.listToMap(sbs, "id").get(pids[level]);
			sbs.add(o);
		}
		//把所有符合访问链的beanlist都查询出来。
		for(Object sb:sbs) {
			invokeListField(sb,fields,pids,(level+1));
		}
	}
	/**
	 * List转Map工具方法,主要用于对象属性映射
	 * 原理:指定list中对象的某个(对象的)属性为转换后map的key,某个(对象的)属性为map的value
	 * @author 张爱国
	 * @param <T>
	 * @param <T> 对象的类型
	 * @since 201310
	 * @param list:欲转换list
	 * @return Map<Object,T>:转换后的map
	 */
	public static <T> Map<String, String> listToMap(List<T> list, String keyName, String valueName) {
		Map<String, String> map = new HashMap<String, String>();
		Assert.notNull(list,  ServiceCode.FAIL, "list不能为空" + keyName);
		if (list.size() == 0) {
			return map;
		}
		T o = list.get(0);

		Assert.notNull(ReflectUtil.getGetter(o.getClass(), keyName),  ServiceCode.FAIL, "list转map时key必须为bean的属性" + keyName);
		Assert.notNull(ReflectUtil.getGetter(o.getClass(), valueName),  ServiceCode.FAIL, "list转map时value必须为bean的属性" + valueName);
		for (T t : list) {
			/** 将指定的属性值作为key,对象值为o放入map @see ReflectUtil */
			String key = (String) ReflectUtil.invokeGetter(keyName, t);
			String value = (String) ReflectUtil.invokeGetter(valueName, t);
			map.put(key, value);
		}
		// map.entrySet().retainAll(map);
		return map;
	}
	
	/**
	 * Map转list工具方法
	 * @author lanchenghao
	 * @since 201310
	 * 原理:遍历map中的键值对，将值(对象object)放入该对象类型的list中
	 * @param <T> 传入的list中对象类型
	 * @param map 欲转换为list的map
	 * @return List<T> 所转换的list 其中的对象为原map中对象的类型 无须强制转换
	 */
	public static <T> List<T> mapToList(Map<Object,T> map){
		List<T> list = new ArrayList<T>();
		for(Object key:map.keySet()){
			list.add((T) map.get(key));
		}
		return list;
	}
	
	/**
	 * 获取list中对象中指定属性keyName的值，每个值以，分割。
	 * @param list
	 * @param cp 通过lamba表达式获取get方法对应的field属性
	 * @return
	 */
	public static <T> String obtainColumnStr(List<T> list, Consumer<T> cp){
		if(null == list || list.size()==0)
			return "";

		Param<String> fieldNameParam = new Param<>(null);
		ReflectUtil.eachFieldWhenGet(list.get(0), bean -> cp.accept((T) bean), fieldName -> fieldNameParam.param = fieldName);

		StringBuilder sb = new StringBuilder();
		for(T t:list){
			sb.append(ReflectUtil.invokeGetter(fieldNameParam.param, t)).append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
}
