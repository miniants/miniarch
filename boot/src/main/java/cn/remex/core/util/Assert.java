/*
 * 文 件 名 : Assert.java
 * CopyRright (c) since 2012:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2012-10-18
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */
package cn.remex.core.util;

import cn.remex.RemexConstants;
import cn.remex.core.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * 本断言类用于在运行时检查异常和错误。
 * 例如，如果一个公共方法，它的一行代码中的某个变量不能为空，此类可以用来验证。<br>
 * 这个类是类似的断言主张。 如果一个变量值被视为无效， （通常）就会抛出一个
 * <code>IllegalArgumentException</code> 。
 * 比如说,Assert.notNull（obj）
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2012-5-18
 */

public abstract class Assert implements RemexConstants {

	/**
	 * 断言提供的字符串中不包含子字符串
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;);
	 * </pre>
	 * @param textToSearch
	 *            the text to search
	 *            被搜索的字符串
	 * @param substring
	 *            the substring to find within the text
	 *            子字符串
	 */
	public static void doesNotContain(final String textToSearch, final String substring) {
		doesNotContain(textToSearch, substring, "字符出参数中不含有子字符串 [" + substring
				+ "]");
	}

	/**
	 * 断言提供的字符串中不包含子字符串
	 * Assert that the given text does not contain the given substring.
	 * 示例:
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;, &quot;Name must not contain 'rod'&quot;);
	 * </pre>
	 * 
	 * @param textToSearch
	 *            the text to search
	 *            被搜索的字符串
	 * @param substring
	 *            the substring to find within the text
	 *            子字符串
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            断言返回的异常信息
	 */
	public static void doesNotContain(final String textToSearch, final String substring,
			final String message) {
		if (StringUtils.hasLength(textToSearch)
				&& StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言字符串不为空,即既不是null,也不是空字符串
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 * <pre class="code">
	 * Assert.hasLength(name);
	 * </pre>
	 * 
	 * @param text
	 *            字符串
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(final String text) {
		hasLength(text, "此处字符串长度，不能为null或者为空字符串！");
	}

	/**
	 * 断言字符串不为空,即既不是null,也不是空字符串
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 * <pre class="code">
	 * Assert.hasLength(name, &quot;Name must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            字符串
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            断言返回的异常信息
	 * @see StringUtils#hasLength
	 * @throws IllegalArgumentException
	 */
	public static void hasLength(final String text, final String message) {
		if (!StringUtils.hasLength(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言字符串含有内容，不能为null或者为空字符串！
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 *            字符串
	 * @see StringUtils#hasText
	 */
	public static void hasText(final String text) {
		hasText(text, "此处字符串含有内容，不能为null或者为空字符串！");
	}

	/**
	 * 断言字符串含有内容，不能为null或者为空字符串！
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            字符串
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            断言返回的异常信息
	 * @see StringUtils#hasText
	 */
	public static void hasText(final String text, final String message) {
		if (!StringUtils.hasText(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 用来判断一个类superType和另一个类subType是否相同或是另一个类的子类或接口。
	 * 区别于instanceof,instanceof是用来判断一个对象实例是否是一个类或接口的或其之类子接口的实例。(不能用于判断两个接口)
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is
	 * <code>true</code>.
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 * 
	 * @param superType
	 *            the super type to check
	 *            父类
	 * @param subType
	 *            the sub type to check
	 *            子类
	 * @throws IllegalArgumentException
	 *             if the classes are not assignable
	 *             如果两个类没有父子关系
	 */
	public static void isAssignable(final Class<?> superType, final Class<?> subType) {
		isAssignable(superType, subType, "");
	}
	/**
	 * 用来判断一个类superType和另一个类subType是否相同或是另一个类的子类或接口。
	 * 格式为:
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is
	 * <code>true</code>.
	 * </pre>
	 * 
	 * @param superType
	 *            the super type to check against
	 *            父类
	 * @param subType
	 *            the sub type to check
	 *            子类
	 * @param message
	 *            a message which will be prepended to the message produced by
	 *            the function itself, and which may be used to provide context.
	 *            It should normally end in a ": " or ". " so that the function
	 *            generate message looks ok when prepended to it.
	 *            异常返回的信息
	 * @throws IllegalArgumentException
	 *             if the classes are not assignable
	 *             不是父子关系抛出的异常
	 */
	public static void isAssignable(final Class<?> superType, final Class<?> subType,
			final String message) {
		notNull(superType, "类型检查不能允许为null！");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException(message + subType + "不是继承于"
					+ superType);
		}
	}

	/**
	 * 通过调用所有规范的getter/setter来比较其值是否相等。如果只有setter或者getter的属性不进行比较。
	 * 进行之比较的类型有9个（8个基本类型和String）
	 * 其他对象进行引用比较
	 * @param obj1  对象1
	 * @param obj2 对象2
	 */
	public static void isFieldValueEquals(final Object obj1, final Object obj2) {
		Map<String, Method> obj1Gettrs = ReflectUtil.getAllGetters(obj1
				.getClass());
		Map<String, Method> obj1Settrs = ReflectUtil.getAllGetters(obj1
				.getClass());
		Map<String, Method> obj2Gettrs = ReflectUtil.getAllGetters(obj2
				.getClass());
		Map<String, Method> obj2Settrs = ReflectUtil.getAllGetters(obj2
				.getClass());
		boolean notEqual = false;
		StringBuilder sb = new StringBuilder();

		for (String key : obj1Gettrs.keySet()) {
			if (obj1Settrs.containsKey(key) && obj2Gettrs.containsKey(key)
					&& obj2Settrs.containsKey(key)) {
				if(obj1Gettrs.get(key).getReturnType().equals(obj2Gettrs.get(key).getReturnType())){
					Object value1 = ReflectUtil.invokeGetter(key, obj1);
					Object value2 = ReflectUtil.invokeGetter(key, obj2);
					sb.append("\n");
					//有相同的方法
					if (!(value1 == value2
							|| value1 != null && value1.equals(value2)
							|| value1 != null && ReflectUtil.isBaseType(value1.getClass())
							|| value2 != null && ReflectUtil.isBaseType(value2.getClass())
							)) {
						notEqual = true;
						sb.append(String.format("属性[%1s]:%2s不等于%3s",
								StringHelper.aequilate(String.valueOf(key), 25,"right"),
								StringHelper.aequilate(String.valueOf(value1), 25,"right"),
								StringHelper.aequilate(String.valueOf(value2), 25)));
					}else{
						sb.append(String.format("属性[%1s]:%2s == %3s",
								StringHelper.aequilate(String.valueOf(key), 25,"right"),
								StringHelper.aequilate(String.valueOf(value1), 25,"right"),
								StringHelper.aequilate(String.valueOf(value2), 25)));
					}
				}

			}
		}
		if(notEqual){
			RuntimeException e = new RuntimeException("判断"+obj1+"的值与"+obj2+"的值不相等！");
			logger.error(e);
			logger.error(sb);
			throw e;
		}else{
			logger.info(sb);
		}

	}

	/**
	 * 断言测试对象是否为某个类的实例
	 * 示例:
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 * Assert that the provided object is an instance of the provided class.
	 * 
	 * @param clazz
	 *            the required class
	 *            java类对象
	 * @param obj
	 *            the object to check
	 *            java对象
	 * @throws IllegalArgumentException
	 *             if the object is not an instance of clazz
	 *             不是实例抛出的异常
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(final Class<?> clazz, final Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	/**
	 * 断言测试对象是否为某个类的实例
	 * 示例:
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 * Assert that the provided object is an instance of the provided class.
	 * 
	 * @param type
	 *            the type to check against
	 *            
	 * @param obj
	 *            the object to check
	 *            java对象
	 * @param message
	 *            a message which will be prepended to the message produced by
	 *            the function itself, and which may be used to provide context.
	 *            It should normally end in a ": " or ". " so that the function
	 *            generate message looks ok when prepended to it.
	 *            返回的异常信息
	 * @throws IllegalArgumentException
	 *             if the object is not an instance of clazz
	 *             不是实例抛出的异常
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(final Class<?> type, final Object obj, final String message) {
		notNull(type, "类型检查不能允许为null！");
		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException(message + "对象的类 ["
					+ (obj != null ? obj.getClass().getName() : "null")
					+ "] 必须为类型 " + type);
		}
	}

	/**
	 * 判断一个对象是否为空
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">
	 * Assert.isNull(value);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 *            java对象
	 * @throws IllegalArgumentException
	 *             if the object is not <code>null</code>
	 *             抛出的异常
	 */
	public static void isNull(final Object object) {
		isNull(object, "此处对象必须为空！");
	}

	/**
	 * 断言一个java对象为null
	 * Assert that an object is <code>null</code> .
	 * 示例:
	 * <pre class="code">
	 * Assert.isNull(value, &quot;The value must be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            返回的异常信息
	 * @throws IllegalArgumentException
	 *            if the object is not <code>null</code>
	 */
	public static void isNull(final Object object, final String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}
	/**
	 * 断言对象为空
	 * @param object
	 * @param message
	 * @param exceptionClass
	 */
	public static void isNull(final Object object, final String message,final Class<? extends RuntimeException> exceptionClass) {
		if (object != null) {
			throw createRuntimeException(message, exceptionClass);
		}
	}
	/**
	 * 断言对象为一个简单Object对象
	 * @param object 对象
	 */
	public static void isSimpleBean(final Object object) {
		if (object instanceof Map || object instanceof Collection) {
			throw new IllegalArgumentException(
					"此处必须是简单的Object对象。不能为Map或者Collection！");
		}
	}

	/**
	 * 断言此处必须为真
	 * 示例:
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0);
	 * </pre>
	 * 
	 * @param expression
	 *            一个逻辑表达式
	 * @throws IllegalArgumentException
	 *             如果表达式为<code>false</code>
	 */
	public static void isTrue(final boolean expression) {
		isTrue(expression, "断言此处必须为真！");
	}

	/**
	 * 断言此处必须为真,是false,抛异常
	 * Assert a boolean expression, throwing
	 * <code>IllegalArgumentException</code> if the test result is
	 * <code>false</code>.
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 *            布尔表达式
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            断言失败返回的异常信息
	 * @throws IllegalArgumentException
	 *             if expression is <code>false</code>
	 *             表达式为false,抛出的异常
	 */
	public static void isTrue(final boolean expression, final String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言一个数组没有空对象。强调:数组为空,不报错
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * <pre class="code">
	 * Assert.noNullElements(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 *            数组
	 * @throws IllegalArgumentException
	 *             if the object array contains a <code>null</code> element
	 *             含有null 元素, 抛异常
	 */
	public static void noNullElements(final Object[] array) {
		noNullElements(array, "此处数组array不能包含任何null对象！");
	}

	/**
	 * 断言一个数组没有空对象。强调:数组为空,不报错
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * <pre class="code">
	 * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 *            数组
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            返回的异常信息
	 * @throws IllegalArgumentException
	 *             if the object array contains a <code>null</code> element 抛出的异常
	 */
	public static void noNullElements(final Object[] array, final String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	/**
	 * 断言集合中有元素;不能为空且至少有一个元素
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * 
	 * @param collection
	 *            the collection to check
	 *            集合
	 * @throws IllegalArgumentException
	 *             if the collection is <code>null</code> or has no elements
	 *             集合为空或没有子元素抛出的异常
	 */
	public static void notEmpty(final Collection<?> collection) {
		notEmpty(collection, "此处数组Collection至少应该包含1个对象！");
	}

	/**
	 * 断言集合中有元素;不能为空且至少有一个元素
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * 
	 * @param collection
	 *            the collection to check
	 *            集合
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            返回异常信息
	 * @throws IllegalArgumentException
	 *            if the collection is <code>null</code> or has no elements
	 *            集合为空或没有子元素抛出的异常
	 */
	public static void notEmpty(final Collection<?> collection, final String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言Map至少应该包含1个对象
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">
	 * Assert.notEmpty(map);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 *            数组Map
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 *             抛出的异常
	 */
	public static void notEmpty(final Map<?, ?> map) {
		notEmpty(map, "此处数组Map至少应该包含1个对象！");
	}

	/**
	 * 断言Map至少应该包含1个对象
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 *            数组Map
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            返回异常信息
	 * @throws IllegalArgumentException
	 *            if the map is <code>null</code> or has no entries
	 *            抛出的异常 
	 */
	public static void notEmpty(final Map<?, ?> map, final String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言一个数组不能为空且至少含有一个对象
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">
	 * Assert.notEmpty(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 *            数组
	 * @throws IllegalArgumentException
	 *             if the object array is <code>null</code> or has no elements
	 *             抛出的异常
	 */
	public static void notEmpty(final Object[] array) {
		notEmpty(array, "此处数组至少应该包含1个对象！");
	}

	/**
	 * 断言一个数组不能为空且至少含有一个对象
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 *            数组
	 * @param message
	 *            the exception message to use if the assertion fails
	 *            返回异常信息
	 * @throws IllegalArgumentException
	 *            if the object array is <code>null</code> or has no elements
	 *            抛出的异常
	 */
	public static void notEmpty(final Object[] array, final String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言一个对象不能为空 <code>null</code> .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 *            java对象
	 * @throws IllegalArgumentException
	 *             if the object is <code>null</code>
	 *             抛出异常
	 */
	public static void notNull(final Object object) {
		notNull(object, "参数不能为空！");
	}

	/**
	 * 断言对象不能为空
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            需要检查的对象
	 * @param message
	 *            提供将输出异常提示信息
	 * @throws IllegalArgumentException
	 *            如果对象为<code>null</code>
	 */
	public static void notNull(final Object object, final String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	/**
	 * 断言对象不为空
	 * @param object
	 * @param message
	 * @param exceptionClass
	 */
	public static void notNull(final Object object, final String message,final Class<? extends RuntimeException> exceptionClass) {
		if (object == null) {
			throw createRuntimeException(message, exceptionClass);
		}
	}
	/**
	 * 自定义异常返回异常信息
	 * @param message 异常信息
	 * @param exceptionClass 自定义异常类型
	 * @return RuntimeException
	 */
	private static RuntimeException createRuntimeException( final String message,final Class<? extends RuntimeException> exceptionClass){
		try {
			return exceptionClass.getConstructor(String.class).newInstance(message);
		} catch (Exception e) {
			return new IllegalArgumentException("自定义异常类"+exceptionClass.getName()+"无法构建，调用默认异常IllegalArgumentException，处理断言。此处异常信息为："+message);
		}
	}
	/**
	 * 断言是否是true,可抛出自定义异常
	 * @param expression 布尔表达式
	 * @param message 异常信息
	 * @param exceptionClass  异常类型
	 */
	public static void isTrue(final boolean expression, final String message,final Class<? extends RuntimeException> exceptionClass) {
		if (!expression) {
			throw createRuntimeException(message, exceptionClass);
		}
	}
	/**
	 * 断言对象不为null且不为empty
	 * @param object string字符串
	 * @param message 异常信息
	 */
	public static void notNullAndEmpty(final String object, final String message) {
		if (object == null || "".equals(object.trim())) {
			throw new IllegalArgumentException(message);
		}
	}
	/**
	 * 断言对象不为null且不为empty
	 * @param object java对象
	 * @param message 异常信息
	 */
	public static void notNullAndEmpty(final Object obj, final String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}
	/**
	 * 断言一个布尔类型的表达式，如果结果是false,抛出IllegalStateException;
	 * 如果断言失败,想抛出IllegalArgumentException,请调用{@link #isTrue(boolean)}
	 * Assert a boolean expression, throwing {@link IllegalStateException} if
	 * the test result is <code>false</code>.
	 * <p>
	 * Call {@link #isTrue(boolean)} if you wish to throw
	 * {@link IllegalArgumentException} on an assertion failure.
	 * 
	 * <pre class="code">
	 * Assert.state(id == null);
	 * </pre>
	 * 
	 * @param expression
	 *           一个布尔表达式
	 * @throws IllegalStateException
	 *             if the supplied expression is <code>false</code>
	 *             如果提供的表达式是false
	 */
	public static void state(final boolean expression) {
		state(expression, "此处状态必须为true!");
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 * 断言一个布尔类型的表达式，如果结果是false,抛出IllegalStateException;
	 * 如果断言失败,想抛出IllegalArgumentException,请调用{@link #isTrue(boolean)}
	 * <pre class="code">
	 * Assert.state(id == null, &quot;The id property must not already be initialized&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            布尔表达式
	 * @param message
	 *            如果断言失败抛出的异常信息
	 * @throws IllegalStateException
	 *             表达式是<code>false</code>则抛出异常
	 */
	public static void state(final boolean expression, final String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}

}
