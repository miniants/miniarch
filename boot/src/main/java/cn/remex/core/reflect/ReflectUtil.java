package cn.remex.core.reflect;

import cn.remex.RemexConstants;
import cn.remex.core.CoreSvo;
import cn.remex.core.aop.AOPCaller;
import cn.remex.core.aop.AOPFactory;
import cn.remex.core.reflect.CodeMapper.CodeMapItem;
import cn.remex.core.util.*;
import net.sf.cglib.proxy.MethodProxy;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

//import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 本类的主要目的是封装{@link java.lang.reflect}反射机制的功能<br>
 * 主要采用了"map对"映射的方法进行的缓存，极大提供了反射的效率。
 * @author Hengyang Liu
 * @since 2012-10-17
 */
public class ReflectUtil implements RemexConstants {

	/**
	 * @rmx.summary SPFeature缩写自:SetPropertiesFeature<br>
	 * 该枚举变量用于copyProperties，标识属性拷贝过程中的不同附加功能
	 */
	public enum SPFeature {
		/**
		 * 设置javabean 属性时，在需要String 参数的方法的入参为null时，转化为""
		 */
		ConvertNull2EmptyString,
		/**
		 * 设置javabean 属性时，在需要String 参数的方法的入参为0/0.0时，转化为""
		 */
		ConvertZero2EmptyString,

		/**
		 * 赋值时对null使用。比如从源对象A中通过getName获得值null，将赋值给B.setName(null)。
		 * 此时原来B无论Name是否为null将被覆盖。
		 * <p>
		 * 此特性有效时将终止排在其后的其他特性
		 */
		EnableNullValue,
		/**
		 * 赋值时忽略空字符串,“”
		 * <p>
		 * 此特性有效时将终止排在其后的其他特性
		 */
		IgnoreEmptyStringValue,
		
		/**
		 * 仅仅只是用前缀名取值，即忽略非前缀取值。
		 */
		JustUseNamePreffix,
		/**
		 * 忽略属性或方法的大小写。
		 */
		IgnoreCase,
		/**
		 * 复制id and dataStatus
		 */
		CopyIdAndDataStatus,
		/**
		 * 复制id
		 */
		CopyId,
		/**
		 * 复制dataStatus
		 */
		CopyDataStatus,
		/**
		 * 深度复制，目前仅支持object/Collection/Map对象,即不允许直接被复制的两个对象为集合<br>
		 * <font style='color:red'>陷阱：如果被复制对象的任意属性或子属性 包含对自身的引用 调用该属性进行复制时会产生死循环</font>
		 */
		DeeplyCopy,
		/**
		 * 删除目标对象中map/collection中不存在于源对象的item.<br>
		 * 规则：<br>
		 * 1.如果目标map中的key不存在于源map，则删除<br>
		 * 2.如果目标中的collection数量大于源，多出部分删除
		 * 3.此功能必须开启DeeplyCopy
		 * 4.默认情况下，不删除。
		 */
		RemoveTargetGatherItemWhenSourceExclude
	}
	
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, Field>> AllField = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, Field>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, List<Method>>> AllMethods = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, List<Method>>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>> AllGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>> AllSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>> AllUserGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>> AllUserSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>>();
	
	final static private ArrayList<Class<?>> SimpleType = new ArrayList<Class<?>>();
	final static private ArrayList<Class<?>> NumeralTypes = new ArrayList<Class<?>>();
	private	static Class<?> ModelableClass;
	private static boolean modelableSupport = false;
	static{
		NumeralTypes.add(int.class);
		NumeralTypes.add(long.class);
		NumeralTypes.add(double.class);
		NumeralTypes.add(float.class);
		NumeralTypes.add(short.class);
		NumeralTypes.add(Integer.class);
		NumeralTypes.add(Long.class);
		NumeralTypes.add(Double.class);
		NumeralTypes.add(Float.class);
		NumeralTypes.add(Short.class);

		SimpleType.addAll(NumeralTypes);
		SimpleType.add(boolean.class);
		SimpleType.add(char.class);
		SimpleType.add(byte.class);
		SimpleType.add(Boolean.class);
		SimpleType.add(Character.class);
		SimpleType.add(Byte.class);
		SimpleType.add(String.class);
		
		
		try {
			ModelableClass = Class.forName("cn.remex.db.rsql.model.Modelable");
			modelableSupport = true;
		} catch (ClassNotFoundException e) {
			logger.warn("ReflectUtil没有找到Modelable类型，故无法提供相关功能!", e);
		}
	}
	/**
	 * 获取实际类型参数
	 * @param type 类型
	 * @return 类型数组
	 * @call Remex2-db 下的RsqlCore.createCollectionTable
	 * @call Remex2-db 下的RqlType.isTCollection
	 * @call Remex2-db 下的RqlType.isTMap
	 * @call Remex2-db 下的RqlType.obtainSKeyCollectionTableColumns
	 */
	public static Type[] getActualTypeArguments(Type type){
		return (Type[]) ReflectUtil.invokeMethod("getActualTypeArguments", type);
	}

	/**
	 * 获取hashCode
	 * @rmx.summary 即无论参数c1,c2的在传参的位置，只要两个类是确定的，返回的hashcode值一样。<br>如果额外的属性extraType,不为空，该属性将作为第三个参数计算hash值连接在上述哈希值之后。<br>
	 * @param c1 类
	 * @param c2 类
	 * @param extraType  动态的参数，用于hash计算(可不传)
	 * @return String 三个参数的hashCode
	 */
	public static String hashCode(Class<?> c1,Class<?> c2,String extraType ){
		Class<?> clazz1 = null ,clazz2 = null;
		try {
			 clazz1 =  Class.forName(StringHelper.getClassName( c1));
			 clazz2 =  Class.forName(StringHelper.getClassName( c2));
		} catch (ClassNotFoundException e) {
			// Never arrived here
			e.printStackTrace();
		}
		int hc1 = clazz1.hashCode(),hc2=clazz2.hashCode(),hc3=(extraType==null?0:extraType.hashCode());
		return hc1>hc2?
			new StringBuilder(String.valueOf(hc1)).append(String.valueOf(hc2)).append(hc3).toString()
			:
			new StringBuilder(String.valueOf(hc2)).append(String.valueOf(hc1)).append(hc3).toString();

	}
	/**
	 * 获取hashCode
	 * @rmx.summary 是有顺序的，传参位置不同计算出的hashCode值也不同
	 * @param c1 类
	 * @param c2 类
	 * @param extraType  动态的参数，用于hash计算(可不传)
	 * @return 三个参数的hashCode
	 * @rmx.call ReflectContext.obtainReflectContext
	 * @rmx.call ReflectConfigure.setFieldMapper
	 */
	public static String hashCodeWithOrder(Class<?> c1,Class<?> c2,String... extraType ){
		Class<?> clazz1 = null ,clazz2 = null;
		try {
			clazz1 =  Class.forName(StringHelper.getClassName( c1));
			clazz2 =  Class.forName(StringHelper.getClassName( c2));
		} catch (ClassNotFoundException e) {
			// Never arrived here
			e.printStackTrace();
		}
		int hc1 = clazz1.hashCode(),hc2=clazz2.hashCode();
		StringBuilder ret = new StringBuilder().append(hc1).append(hc2);
		if(extraType!=null)
			for(String s: extraType){
				ret.append(null==s?0:s.hashCode());
			}
		else
			ret.append(0);
		return	ret.toString();
	}
	/**
	 * 获取hashCode
	 * @rmx.summary 根据Class的hashCode获得两个类无方向的哈希码组合。<br>
	 * @param c1 类
	 * @param c2 类
	 * @return 两个参数的hashCode
	 * TODO 暂未用
	 */
	public static String hashCode(Class<?> c1,Class<?> c2){
		return hashCode(c1, c2, null);
	}
	
	/**
	 * 两个对象类型判断
	 * @rmx.summary 如果两个type类型完全一致，或者类似于boolean与Boolean的关系，则判断类型一致。复杂的泛型无法判断，父类与子类也不做判断。
	 * @param type1 基本类型
	 * @param type2 基本类型
	 * @return boolean 判断结果
	 * TODO 未用
	 */
	final public static boolean baseTypeEqual(final Type type1, final Type type2) {
		return type1 == type2
				|| ((type1 == int.class || type1 == Integer.class) && (type2 == int.class || type2 == Integer.class))
				|| ((type1 == double.class || type1 == Double.class) && (type2 == double.class || type2 == Double.class))
				|| ((type1 == long.class || type1 == Long.class) && (type2 == long.class || type2 == Long.class))
				|| ((type1 == float.class || type1 == Float.class) && (type2 == float.class || type2 == Float.class))
				|| ((type1 == short.class || type1 == Short.class) && (type2 == short.class || type2 == Short.class))
				|| ((type1 == byte.class || type1 == Byte.class) && (type2 == byte.class || type2 == Byte.class))
				|| ((type1 == char.class || type1 == Character.class) && (type2 == char.class || type2 == Character.class));
	}
	/**
	 * 对象转换为基础类型
	 * @rmx.summary 包括包装类 包括String,如果欲转换类型不为其中则抛出异常
	 * @param type 希望case成的目标类型。
	 * @param o 需要case的对象
	 * @param instanceArgs 如果在case过程中需要新建相关对象，则传入instanceArgs参数。[0]为构造方法运行的对象；[1]为构造方法;[2-]构造方法所需的参数列表
	 * @return Object 返回转化后的类型
	 * @rmx.note 20140620 lanchenghao 添加入参中type的类型检查 非合法类型则抛出异常
	 * @rmx.call ReflectUtil.invokeSetterWithDefaultTypeCoventer
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object caseObject(final Type type, final Object o, final Object... instanceArgs) {
		if (null == o || o.getClass() == type) 
			return o;

		/** 一下是类型不匹配时的操作 **/
		final Class<? extends Object> sc = o.getClass();
		if(type == String.class) {
			if (sc == String[].class) {
				final StringBuilder ret = new StringBuilder();
				for (final String v : (String[]) o) {
					ret.append(v).append(",");
				}
				ret.deleteCharAt(ret.length() - 1);
				return ret.toString();
			} else {
				return o.toString();
			}
		}else if(type == String.class && o instanceof Date) {
			return DateHelper.formatDate((Date)o);
		}else if(o.getClass().isArray() && !((Class<?>)type).isArray()){//来源是数组，目标不是数组则，按此规则转化
			Object[] os = (Object[])o;
			if(os.length==1 ){
				return caseObject(type, os[0]);
			}else
				throw new ReflectionException("无法将包含两个元素的数组转化为一个对象！CaseObject失败！");
			
		}else if(Class.class.isAssignableFrom(type.getClass()) && ((Class<?>) type).isAssignableFrom(o.getClass())){  // 子类父类的关系，或代理类的关系
			return o;
		}else if(Enum.class.isAssignableFrom((Class<?>)type)){
			return Enum.valueOf((Class)type, String.valueOf(o));
		}else if(o instanceof String && modelableSupport && ModelableClass.isAssignableFrom((Class<?>)type)){//数据库类
			if(Judgment.nullOrBlank(o))
				return null;
			Object t;
			if(instanceArgs==null || instanceArgs.length==0 || instanceArgs[0]==null){
				t=invokeNewInstance((Class) type);
			}else{
				t=invokeMethod((Method) instanceArgs[1], instanceArgs[0], Arrays.copyOfRange(instanceArgs,2,instanceArgs.length));
			}
			invokeSetter("id", t, o);
			return t;
		} else if (type == boolean.class || type == Boolean.class) {// boolean值比较特殊
			if (sc == boolean.class || sc == Boolean.class)return o;
			return "1".equals(o) || "on".equals(o) || "yes".equals(o)
					|| "true".equals(o) || "On".equals(o) || "Yes".equals(o)
					|| "True".equals(o) || "ON".equals(o) || "YES".equals(o)
					|| "TRUE".equals(o) || Boolean.parseBoolean(o.toString());
		} else if (type == int.class || type == Integer.class) {
			if (sc == int.class || sc == Integer.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).intValue();
		} else if (type == double.class || type == Double.class) {
			if (sc == double.class || sc == Double.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).doubleValue();
		} else if (type == long.class || type == Long.class) {
			if (sc == long.class || sc == Long.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).longValue();
		} else if (type == float.class || type == Float.class) {
			if (sc == float.class || sc == Float.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).floatValue();
		} else if (type == short.class || type == Short.class) {
			if(sc == short.class || sc == Short.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).shortValue();
		} else if (type == byte.class || type == Byte.class) {
			if (sc == byte.class || sc == Byte.class)return o;
			final String str = o.toString();
			return Double.valueOf(str.equals("") ? "0" : str).byteValue();
		} else if (type == char.class || type == Character.class) {
			if(sc == char.class || sc == Character.class)return o;
			final String str = o.toString();
			if (str.length() == 0) {
				return 0;
			} else {
				return o.toString().toCharArray()[0];
			}
		}else {
			throw new ReflectionException("指定的类型不合法,应为基础类型/对象为类型的子类/接口的实现。");
		}
	}


	/**
	 * 判断是否是简单类型八个基本+String+Enum
	 * @rmx.summary 如果传入类的class为8个基础类型之一则返回true 否则 false
	 * @param c 类
	 * @return boolean
	 * TODO 暂未用 
	 */
	public static boolean isSimpleType(final Class<?> c) {
		return SimpleType.contains(c) || Enum.class.isAssignableFrom(c);
	}

	/**
	 * 判断是否是数字类型
	 * @param c 类
	 * @return boolean 判断返回值
	 * @rmx.call ReflectUtil.invokeSetterWithDefaultTypeCoventer
	 * @see ReflectUtil#NumeralTypes 参考数字类型有哪些
	 */
	public static boolean isNumeralType(final Class<?> c) {
		return NumeralTypes.contains(c);
	}

	/**
	 * 判断输入的类是否是最基本的数据类。
	 * @rmx.summary 这些类型包括:int,long,boolean,byte,char,short,float,double
	 * @param c 类
	 * @return 判断返回值
	 * @rmx.call Assert.isFieldValueEqueals
	 */
	public static boolean isBaseType(final Class<?> c) {
		if (c == int.class || c == long.class || c == boolean.class || c == byte.class || c == char.class || c == short.class || c == float.class || c == double.class) {
			return true;
		}
		return false;
	}
	/**
	 * 判断两个{@link Type}参数是否完全相等。
	 * @rmx.summary 以下情况则返回true <li>1.当都是Class<?>并且完全相等 <li>2.如果是{@link Map}、{@link List}、{@link Collection}等<br> ParameterizedTypeImpl,复杂类型则需RawType和ActualTypeArguments完全相等
	 * @param type1 传入类型1
	 * @param type2 传入类型2
	 * @return 以下情况则返回true <li>1.当都是Class<?>并且完全相等 <li>2.如果是{@link Map}、
	 * TODO 未用
	 */
	public static boolean evaluateEqual(final Type type1, final Type type2) {
		if (type1 instanceof Class<?> && type2 instanceof Class<?>
				&& type1 == type2) {
			return true;
		} else {
			try{
				final Class<?> rt1 = (Class<?>) ReflectUtil.invokeMethod("getRawType", type1);
				final Class<?> rt2 = (Class<?>) ReflectUtil.invokeMethod("getRawType", type2);
				// 判断基础类型一致
				if (rt1 != rt2) {
					return false;
				}
				// 判断泛型个数必须一致
				final Type[] at1 = (Type[]) ReflectUtil.getActualTypeArguments( type1);
				final Type[] at2 = (Type[]) ReflectUtil.getActualTypeArguments( type2);
				if (at1.length != at2.length) {
					return false;
				}
				
				// 逐个比较泛型是否相等
				for (int i = 0; i < at1.length; i++) {
					if (at1[i] != at2[i]) {
						return false;
					}
				}
				
				// 至此则基础类型和泛型均相等
				return true;
			}catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 获取全部属性
	 * @param clazz 所要获取属性的类
	 * @return 以类为key 属性map为value的集合
	 * @rmx.call ReflectUtil.getAllFields
	 * @rmx.call ReflectUtil.getField
	 */
	public static ReadOnlyMap<String, Field> getAllFields(final Class<?> clazz) {
		ReadOnlyMap<String, Field> fields = AllField.get(clazz);
		if (null != fields) {
			return fields;
		}

		fields = new ReadOnlyMap<String, Field>();

		//递归获取父类的属性
		if(Object.class != clazz.getSuperclass()){
			Class<?> sup = clazz.getSuperclass();
			ReadOnlyMap<String, Field> supFields = getAllFields(sup) ;
			fields.putAll(supFields);
		}
		
		final Field[] AutualFields = clazz.getDeclaredFields();
		for (final Field f : AutualFields) {
			fields.put(f.getName(), f);
		}
		

		AllField.put(clazz, fields);

		return fields;
	}

	public static ReadOnlyMap<String, List<Method>> getAllMethods(final Class<?> clazz) {
		ReadOnlyMap<String, List<Method>> methods = AllMethods.get(clazz);
		if (null != methods) {
			return methods;
		}

		final Method[] methodArr = clazz.getMethods();
		methods = new ReadOnlyMap<String, List<Method>>();
		for (final Method m : methodArr) {
			List<Method> ml = methods.get(m.getName());
			if(ml == null){
				ml = new ArrayList<>();
				methods.put(m.getName(),ml);
			}
			if(!ml.contains(m))ml.add(m);
		}

		AllMethods.put(clazz, methods);

		return methods;
	}
	/**
	 * 获取参数类所有属性的getter方法
	 * @rmx.summary 检索对象中所有的get方法，其参数类型仅有一个即被检索的对象类型<br>返回本类型的所有get方法映射表。含boolean型结果的is方法，包括继承过来的。 结果为{@link Map}<br>其中，key是get方法所规范的属性名，如getName()的name,value为对应的{@link Method}
	 * @param clazz 类
	 * @return 返回本类型的所有get方法映射表
	 */
	public static Map<String, Method> getAllGetters(final Class<?> clazz) {
		ReadOnlyMap<String, Method> getters = AllGetter.get(clazz);
		if (null != getters) {
			return getters;
		}

		final Method[] methods = clazz.getMethods();
		getters = new ReadOnlyMap<String, Method>();
		for (final Method m : methods) {
			if (m.getGenericParameterTypes().length == 0) {
				if (m.getName().startsWith("get")) {
					getters.put(StringHelper.lowerFirstLetter(m.getName()
							.substring(3)), m);
				} else if (m.getName().startsWith("is")
						&& (m.getReturnType() == boolean.class || m
								.getReturnType() == Boolean.class)) {
					getters.put(StringHelper.lowerFirstLetter(m.getName()
							.substring(2)), m);
				}
			}
		}

		AllGetter.put(clazz, getters);

		return getters;
	}

	/**
	 * 获取类所有属性的setter方法
	 * @rmx.summary 检索某类中所有的setter方法<br>返回所有setter方法映射表。
	 * <br>包括从它父类继承过来的。 <br>在返回的映射表中，key是属性名称，如setName(String Name)的name,value为对应的setter方法
	 * @param clazz 类
	 * @return 返回本类型的所有setter方法映射表
	 */
	public static Map<String, Method> getAllSetters(final Class<?> clazz) {
		ReadOnlyMap<String, Method> setters = AllSetter.get(clazz);
		if (null != setters) {
			return setters;
		}

		final Method[] methods = clazz.getMethods();
		setters = new ReadOnlyMap<String, Method>();
		for (Method m : methods) {
			if (m.getName().startsWith("set")
					&& m.getGenericParameterTypes().length == 1
					&& m.getReturnType() == void.class) {
				String fieldName = StringHelper.lowerFirstLetter(m.getName().substring(3));
				if(null !=getGetter(clazz, fieldName) && getGetter(clazz, fieldName).getReturnType() != m.getGenericParameterTypes()[0])//如果有getter，则根据getter的返回类型配置setter
					try {
						m = clazz.getMethod(m.getName(), getGetter(clazz, fieldName).getReturnType());
					} catch (Exception e) {
						logger.warn("在初始化"+clazz.getName()+"时，没有找到与getter方法相匹配类型的set方法，这是一个重要提示。setter为"+m.toGenericString()+";getter为"+getGetter(clazz, fieldName).toGenericString());
					} 
				setters.put(fieldName,m);
			}
		}

		AllSetter.put(clazz, setters);

		return setters;
	}
	
	/**
	 * 获取拥有相应getter的setter集合
	 * @param clazz
	 * @return
	 */
	public static Map<String,Method> getAllUserSetters(final Class<?> clazz){
		ReadOnlyMap<String, Method> setters = AllUserSetter.get(clazz);
		if (null != setters) {
			return setters;
		}else{
			obtainUserGettersAndSetters(clazz);
			setters = AllUserSetter.get(clazz);
		}
		return setters;
	}
	/**
	 * 获取拥有相应应setter的getter集合
	 * @param clazz
	 * @return
	 */
	public static Map<String,Method> getAllUserGetters(final Class<?> clazz){
		ReadOnlyMap<String, Method> getters = AllUserGetter.get(clazz);
		if (null != getters) {
			return getters;
		}else{
			obtainUserGettersAndSetters(clazz);
			getters = AllUserGetter.get(clazz);
		}
		return getters;
	}
	private static void obtainUserGettersAndSetters(final Class<?> clazz){
		Map<String, Method> _as = getAllSetters(clazz);
		Map<String, Method> _ag = getAllGetters(clazz);
		ReadOnlyMap<String, Method> setters = new ReadOnlyMap<String, Method>();
		ReadOnlyMap<String, Method> getters = new ReadOnlyMap<String, Method>();
		for(String field:_as.keySet()){
			if(_ag.containsKey(field)){
				setters.put(field, _as.get(field));
				getters.put(field, _ag.get(field));
			}
		}
		
		AllUserSetter.put(clazz, setters);
		AllUserGetter.put(clazz, getters);
	}

	/**
	 * 获取单个属性
	 * @rmx.summary 根据目标类和属性名称 获取该属性Field
	 * @param clazz 类
	 * @param fieldName 属性名称 
	 * @return 返回fieldName对应的类的Field属性
	 */
	public static Field getField(final Class<?> clazz, final String fieldName) {
		return getAllFields(clazz).get(fieldName);
	}

	/**
	 * 获取单个属性对应的getter方法
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return 返回类属性名称对应的get方法
	 */
	public static Method getGetter(final Class<?> clazz, final String fieldName) {
		return getAllGetters(clazz).get(fieldName);
	}
	/**
	 * 根据类中的方法名及参数来获取准确的方法
	 * @param clazz 类
	 * @param methodName 方法名称
	 * @return 返回方法名称对应的Method
	 */
	public static Method getMethod(final Class<?> clazz, final String methodName,Class<?>... parameterTypeArr) {
		Method method = null;
		try{
			method  = clazz.getMethod(methodName, parameterTypeArr);
		}catch (Exception e) {
			handleReflectionException(e);
		}
		return method;
	}
	/**
	 * 根据类中的方法名来搜索方法
	 * @param clazz 类
	 * @param methodName 方法名称
	 * @return 返回方法名称对应的Method
	 */
	public static Method getMethod(final Class<?> clazz, final String methodName) {
		Method method = null;
		try{
			List<Method> methods = getAllMethods(clazz).get(methodName);
			if(null == methods || methods.size()==0) return null;
			method = methods.get(0);
		}catch (Exception e) {
			handleReflectionException(e);
		}
		return method;
	}

	/**
	 * 获得List声明的对应的泛型类型
	 * @param type
	 * @return 泛型类型
	 * TODO 未用
	 */
	public static Class<?> getListActualType(final Type type) {
		try {
//			final ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) field;
//			final Type[] types = typeImpl.getActualTypeArguments();
			
			final Type[] types = (Type[]) ReflectUtil.getActualTypeArguments( type);

			if(types[0].getClass().getName().contains("ParameterizedTypeImpl")){
				ParameterizedTypeImpl impl = (ParameterizedTypeImpl) types[0];
				return impl.getRawType();
			}else{
				return (Class<?>) types[0];
			}
		} catch (final Exception e) {
			handleUnexpectedException(e);
		}
		return null;
	}

	/**
	 * 获取单个属性对应的setter方法
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return 返回类属性名称对应的set方法
	 */
	public static Method getSetter(final Class<?> clazz, final String fieldName) {
		return getAllSetters(clazz).get(fieldName);
	}

	/**
	 * 根据当前的上下文map来获取目标类的set方法。
	 * @rmx.summary 根据第一个参数获取setters集合后，该集合元素数目和第二个传入参数的map比较<br>
	 * 取数目最小的作为循环查找的新map,如果第一个参数对应的setters集合的key包含新map中的key<br>
	 * 那么就把该key 和该key在setters中对应的方法 放到一个新的map中，最后返回这个map;
	 * @param clazz 类
	 * @param context map key:属性名称 value:对应属性setter方法
	 * @return 
	 */
	public static Map<String, Method> getSettersInContext(final Class<?> clazz,
			final Map<String, Object> context) {
		final Map<String, Method> retSetters = new HashMap<String, Method>();
		Map<String, ? extends Object> map;
		final Map<String, Method> setters = getAllSetters(clazz);
		map = setters.size() < context.size() ? setters : context;// 选择小的map循环查找
		for (final String key : map.keySet()) {
			if (setters.containsKey(key)) {
				retSetters.put(key, setters.get(key));
			}
		}
		return retSetters;
	}


	protected static AOPFactory AopBeanFactory = new AOPFactory(
			new AOPCaller(null) {
				@Override
				public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
					// 查找
					String name = method.getName();
					if (name.startsWith("get")
							&& method.getParameterTypes().length == 0) {// predicate
						// 中用get的方法来获得sql中需要操作的field
						Consumer c = (Consumer) CoreSvo.valLocal(String.valueOf(obj.hashCode()));
						c.accept(StringHelper.lowerFirstLetter(name.substring(3)));
						return null;
					}
					// 只是为了截获属性名称,不用真实的调用
					return proxy.invokeSuper(obj, args);
				}
			});
	public static <T> T createAopBean(Class<T> beanClass){
		return AopBeanFactory.getBean(beanClass);
	}
	public static void eachFieldWhenGet(Object o, Consumer predicatConsumer, Consumer c) {
		Object aopBean;
		if(o instanceof Class){
			aopBean = AopBeanFactory.getBean((Class)o);
		}else if(o.getClass().toString().indexOf("$$EnhancerByCGLIB$$")>0){
			aopBean = o;
		}else{
			aopBean = AopBeanFactory.getBean(o.getClass());
		}
		CoreSvo.putLocal(String.valueOf(aopBean.hashCode()), c);

		predicatConsumer.accept(aopBean);

		CoreSvo.putLocal(String.valueOf(aopBean.hashCode()), null);

	}

	/**
	 * 
	 * @param ex
	 */
	public static void handleInvocationTargetException(
			final InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	/**
	 * 映射异常
	 * @param ex
	 */
	public static void handleReflectionException(final Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("未找到此方法: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("无权访问此方法: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		handleUnexpectedException(ex);
	}

	/**
	 * 抛出未知异常
	 * @param ex
	 */
	public static void handleUnexpectedException(final Throwable ex) {
		// Needs to avoid the chained constructor for JDK 1.4 compatibility.
		final IllegalStateException isex = new IllegalStateException(ex);
		throw isex;
	}
	/**
	 * 根据clazz调用默认的无参构造函数生成相对应实例
	 * @param clazz 所要生成实例的类
	 * @return 返回该实例
	 */
	public static <T> T invokeNewInstance(Class<T> clazz){
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ReflectionException("该属性所对应的类没有默认构造函数，"+clazz.toString());
		}
	} 
	/**
	 * 根据类全名调用默认的无参构造函数生成相对应实例
	 * @param classFullName 所要生成实例的类
	 * @return 返回该实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeNewInstance(String classFullName){
		try {
			return (T) Class.forName(classFullName).newInstance();
		} catch (Exception e) {
			throw new ReflectionException("创建类的实例失败:"+classFullName);
		}
	} 
	/**
	 * 根据属性名称调用get并返回值。
	 * @param fieldName java bean 的属性名称。将根据此属性的名称调用相对应的getter方法。
	 * @param target java bean的实例。
	 * @return 返回
	 */
	static public Object invokeGetter(final String fieldName, final Object target) {
		Method getter = ReflectUtil.getAllGetters(target.getClass()).get(fieldName);
		if(null == getter)
			throw new ReflectionException("目标"+target+"中没有该属性对应的getter："+fieldName);
		return ReflectUtil.invokeMethod(getter,	target);
	}

	/**
	 * 执行方法
	 * @rmx.summary 用提供的参数在目标对象上调用具体的 {@link Method}<br>如果是静态{@link Method}， 目标对象可以为空
	 * <code>null</code>。<br>
	 * <p>
	 * 抛出的异常将通过 {@link #handleReflectionException}处理。<br>
	 * @param method 需调用的方法
	 * @param target 方法的目标实例，如果是静态方法可为空。
	 * @param args 传参 (可以为 <code>null</code>)
	 * @return 返回的结果
	 */
	public static Object invokeMethod(final Method method, final Object target,
			final Object... args) {
		if(null == method)
			throw new ReflectionException("调用目标"+target+"的方法时，Method对象不能为空!");
	
		try {
			return method.invoke(target, args);
		} catch (final Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
	public static Object applyMethod(final Method method, final Object target, final Object[] params){
		if(null == method)
			throw new ReflectionException("调用目标"+target+"的方法时，Method对象不能为空!");

		try {
			return method.invoke(target, params);
		} catch (final Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
	/**
	 * 执行方法
	 * @param methodName 对象方法名字符串。如getName
	 * @param target 目标对象
	 * @param args 对象参数列表
	 * @return
	 */
	public static Object invokeMethod(final String methodName, final Object target,
			final Object... args) {
		try {
			Class<?>[] parameterTypes = null;
			if (null != args) {
				ArrayList<Class<?>> parameterTypeArr = null;
				parameterTypeArr  = new ArrayList<Class<?>>(4);
				for (Object arg : args) {
					parameterTypeArr.add(arg.getClass());
				}
				parameterTypes = parameterTypeArr.toArray(new Class<?>[]{});
			}
			Method method = target.getClass().getMethod(methodName,
					parameterTypes);
			return method.invoke(target, args);
		} catch (final Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
	/**
	 * 根据属性名称调用Set方法
	 * @param fieldName 属性名称
	 * @param target 该属性所属的对象
	 * @param value 该属性的数据类型
	 */
	static public void invokeSetter(final String fieldName, final Object target,
			final Object value) {
		Method setter = ReflectUtil.getAllSetters(target.getClass()).get(fieldName);
		if(null == setter)
			throw new ReflectionException("目标"+target+"中没有该属性对应的setter："+fieldName);

		ReflectUtil.invokeMethod(setter, target, value);
	}
	/**
	 * 根据属性名称调用Set方法
	 * @rmx.summary 忽略没有set方法的操作<br>
	 * @param fieldName 属性名称
	 * @param target 目标对象
	 * @param value 欲转换的value对象
	 */
	static public void invokeSetterIgnoreNoSetter(final String fieldName, final Object target,
			final Object value) {
		Class<?> clazz = target.getClass();
		Map<String, Method> allSetters = ReflectUtil.getAllSetters(clazz);
		if (allSetters.containsKey(fieldName))
			invokeSetterWithDefaultTypeCoventer(target, allSetters.get(fieldName), value, new ReflectFeatureStatus(null));
	}
	private static Map<Method,Type> NoSuchSetter = new HashMap<Method, Type>();
	/**
	 * 调用setter方法对target赋值
	 * @rmx.summary 调用默认的类型转化器，将value转化为适当的类型后<br>
	 * @param setter 对象的setter方法
	 * @param dest 目标对象
	 * @param value 欲转换的value对象
	 * @param status 附件功能
	 */
	@SuppressWarnings({ "unchecked"})
	public static void invokeSetterWithDefaultTypeCoventer(final Object dest,
			Method setter, Object value, final ReflectFeatureStatus status) {
//		ReflectFeatureStatus status = new ReflectFeatureStatus(features);
		final Class<?> pt = setter.getParameterTypes()[0];
		//非空值的简单对象可以从子类向父类复制
		if((null== status||!status.isSPFeatureOn) && null!=value && isSimpleType(pt)){
			invokeMethod(setter, dest, ReflectUtil.caseObject(pt, value));
			if(status.isDebugEnabled)logger.debug("SetValue: ["+setter.getName()+(setter.getName().length()<15?"]\t\t":"]\t")+" with ["+value+"] for "+dest.getClass().getSimpleName());
			return;
		}

//		final boolean featuresOn = null != features && features.length > 0;
//		boolean enableNullValue = false;
//		boolean enableIgnoreEmptyStringValue = false;
//		boolean enableDeeplyCopy = false;
		// 特性功能
//		if (featuresOn) {
//			for (final SPFeature feature : features) {
//				switch (feature) {
//				case ConvertNull2EmptyString:// 将null转化为空字符串
//					if (null == value
//							&& setter.getParameterTypes()[0] == String.class) {
//						value = "";
//					}
//					break;
//				case IgnoreEmptyStringValue:// 赋值时忽略空字符串,“”
//					enableIgnoreEmptyStringValue = true;
//					break;
//				case EnableNullValue: // 赋值时将对null使用。比如从源对象A中通过getName获得值null，将赋值给B.setName(null)。
//										// 此时原来B无论Name是否为null将被覆盖。
//					if (null == value) {
//						enableNullValue = true;
//					}
//					break;
//				case DeeplyCopy:
//					enableDeeplyCopy = true;
//				default:
//					break;
//				}
//			}
//		}// 此处不能有else

		if(pt == String.class
				&&(
					(status.ConvertNull2EmptyString && null == value)
					||(status.ConvertZero2EmptyString && null != value && isNumeralType(value.getClass()) && (0-Double.valueOf(value.toString())) == 0)
					)
			)
			value="";
		
		/**
		 * 如果不开启空字符串特性功能 默认将进行全值赋值 即不进行null->""转化，并无论value是何值，将覆盖dest相应的值。
		 */
		if(status.IgnoreEmptyStringValue && "".equals(value)
				&& pt == String.class){
			if(status.isDebugEnabled)logger.debug("SetValue: ["+setter.getName()+(setter.getName().length()<15?"]\t\t":"]\t")+" ignore [EMPTYSTRING] for "+dest.getClass().getSimpleName());
		}else if (null != value || status.EnableNullValue) {
			if(NoSuchSetter.containsKey(setter) && null!=value &&NoSuchSetter.get(setter)==value.getClass()){
				logger.debug("-----目标对象"+dest+"set方法无此重载："+setter.getName()+"(" +value.getClass()+")");
				return;
			}else{
				Object nValue;
				//对简单对象和复杂对象进行分别得值预处理
				if(isSimpleType(pt) || 
						(!status.enableDeeplyCopy && !Collection.class.isAssignableFrom((Class<?>)pt) && !Map.class.isAssignableFrom((Class<?>)pt))
					){
					//简单对象case即可
					nValue = ReflectUtil.caseObject(pt, value);
				}else{
					//复杂对象有三类：Map Collection Object
					
					if(!status.enableDeeplyCopy){
						return;
					}
					
					if(Collection.class.isAssignableFrom(pt) && value instanceof Collection<?>){//集合对象
						/* LHY 2014-9-25 深度复制时，collection目标对象完全被新collection中的对象替换而导致原始item值不可控的丢失。
						 * 本次修复，新增标志RemoveTargetGatherItemWhenSourceExclued，当它为true时删除源对象中没有的item，源和目标都有的则通过copy来复制
						Collection<Object> field = (Collection<Object>) invokeNewInstance(value.getClass());
						Type pt2 = setter.getGenericParameterTypes()[0];
						Class<?> targetItemClass = obtainCollectionRawType(pt2);
						for(Object item:(Collection<?>)value){
							if(null == item)continue;
							Object itemNew = invokeNewInstance(targetItemClass);
							ReflectUtil.copyProperties(itemNew, item, status.features);
							field.add(itemNew);
						}
						*/
						//以下内容为修复 2014-9-29
						Collection<Object> field = (Collection<Object>) invokeGetter(StringHelper.lowerFirstLetter(setter.getName().substring(3)), dest);
						if(null==field) field = (Collection<Object>) invokeNewInstance(value.getClass());
						Type pt2 = setter.getGenericParameterTypes()[0];
						Class<?> targetItemClass = obtainCollectionRawType(pt2);
						Collection<Object> collection = (Collection<Object>)value;

						//TODO 转化为数组进行匹配。Collection很难搞定。
						Object[] arr_c = collection.toArray();
						Object[] arr_f = field.toArray();
						int arr_c_len=arr_c.length,arr_f_len=arr_f.length,
								max=arr_c_len>arr_f_len?arr_c_len:arr_f_len;
						boolean itemIsSimple = isSimpleType(targetItemClass ) || Enum.class.isAssignableFrom(targetItemClass);
						Object[] arr_f_new = new Object[max];
						for(int i=0;i<max;i++){
							if(i<arr_c_len && null!=arr_c[i]){//L1来源有元素
								if(!itemIsSimple){	//L1.1非简单类型
									Object curItem = (i>=arr_f_len || null == arr_f[i])?invokeNewInstance(targetItemClass) : arr_f[i];
									ReflectUtil.copyProperties(curItem, arr_c[i], status.features);
									arr_f_new[i] = curItem;
								}else{ 							//L1.2 简单类型
									arr_f_new[i] = arr_f[i];
								}
							}else{ //L2 来源没有元素
								if(i<arr_f_len && !status.RemoveTargetGatherItemWhenSourceExclude){
									arr_f_new[i] = arr_f[i];
								}else{
									//此处删除元素或者不予赋值;
								}
							}
						}
						field.clear();
						for(Object item:arr_f_new){
							if(null!=item)
								field.add(item);
						}
						//2014-9-29 end
						
						value = field;//深度复制到是新的对象
					}else if (Map.class.isAssignableFrom(pt)&& value instanceof Map<?,?>){//Map对象
						/* LHY 2014-9-25 深度复制时，map目标对象完全被新map中的对象替换而导致原始item值不可控的丢失。
						 * 本次修复，新增标志RemoveTargetGatherItemWhenSourceExclued，当它为true时删除源对象中没有的item，源和目标都有的则通过copy来复制
						Map<Object,Object> field = (Map<Object,Object>) invokeNewInstance(value.getClass());
						Map<Object, Object> map = ((Map<Object,Object>) value);
						for(Object key:map.keySet()){
							Object item = map.get(key);
							if(null == item)continue;
							Object itemNew = invokeNewInstance(item.getClass());
							ReflectUtil.copyProperties(itemNew,item , status.features);
							field.put(key,itemNew);
						}
						*/
						//以下内容为修复 2014-9-25
						Map<Object,Object> field = (Map<Object,Object>) invokeGetter(StringHelper.lowerFirstLetter(setter.getName().substring(3)), dest);
						if(null == field) field = (Map<Object,Object>) invokeNewInstance(value.getClass());
						Map<Object, Object> map = ((Map<Object,Object>) value);
						Type pt2 = setter.getGenericParameterTypes()[0];
						Class<?> targetValueClass = (Class<?>) getActualTypeArguments(pt2)[1];
						boolean valueIsSimple = isSimpleType(targetValueClass ) || Enum.class.isAssignableFrom(targetValueClass);
						for(Object key:map.keySet()){
							Object item_m = map.get(key);
							Object item_f = field.get(key);
							
							if(null == item_m && null == item_f){
								continue;
							}else if(null != item_m){
								if(valueIsSimple){
									item_f = item_m;
								}else{
									if(null == item_f) item_f = invokeNewInstance(targetValueClass);
									ReflectUtil.copyProperties(item_f, item_m, status.features);
								}
								field.put(key,item_f);
							}else {
								throw new ReflectionException("深度复制时，map数据不符合规则！");
							}
						}
						if(status.RemoveTargetGatherItemWhenSourceExclude){//如果目标中的对象在来源中没有，则删除
							List<Object> keys = new ArrayList<Object>(); 
							for(Object key:field.keySet()){
								if(!map.containsKey(key))keys.add(key);
							}
							for(Object key:keys)field.remove(key);
						}
						//2014-9-25 end
						
						value = field;//深度复制到是新的对象
					}else{//普通Object对象
						/**
						 * BUG号:
						 * 2014-04-17
						 * 深度复制中,原object 类型的属性值被置空的bug修复
						 * 
						Object object = null;

						object = invokeNewInstance(pt);
						 * 
						 * 
						 * **/
						Object object = invokeGetter(StringHelper.lowerFirstLetter(setter.getName().substring(3)), dest);	//从源对象中读取object属性是否为空
						if(null == object) 																						//为空时将初始化一个新对象。
							object = invokeNewInstance(pt);
						/***bug修复结束**/
						
						ReflectUtil.copyProperties(object, value, status.features);
						value = object;//深度复制到是新的对象
					}// end for 复杂对象有三类：Map Collection Object
					
					nValue = value;
				}
				
				try{
					ReflectUtil.invokeMethod(setter, dest, nValue); 
//					ReflectUtil.invokeMethod(setter, dest, ReflectUtil.caseObject(pt, value));
				}catch (final IllegalArgumentException e) {
					//如果setter有重载，在此处理
					logger.warn("-----目标对象"+dest+"set方法有重载："+setter.getName()+"(" +setter.getParameterTypes()[0]+")");
					try {
						if(null!=value) {
							setter = dest.getClass().getMethod(setter.getName(), value.getClass());
							//用常用的setter来更新缓存 TODO 将来需要做一个统计函数。
//							ReflectUtil.getAllSetters(dest.getClass()).put(StringHelper.lowerFirstLetter(setter.getName().substring(3)), setter);
						}
					} catch (final NoSuchMethodException e1) {
						NoSuchSetter.put(setter, value.getClass());
						logger.warn("-----目标对象"+dest+"set方法中无法找到如此重载："+setter.getName()+"(" +value.getClass()+")");
						//如果没有这个方法，说明类型不对不用设置
						return;
					}
					ReflectUtil.invokeMethod(setter, dest,ReflectUtil.caseObject(pt, value));
				}
				if(status.isDebugEnabled)logger.debug("SetValue: ["+setter.getName()+(setter.getName().length()<15?"]\t\t":"]\t")+" with ["+value+"] for "+dest.getClass().getSimpleName());
			}
		}else{
			if(status.isDebugEnabled)logger.debug("SetValue: ["+setter.getName()+(setter.getName().length()<15?"]\t\t":"]\t")+" ignore [null] for "+dest.getClass().getSimpleName());
		}
	}

	/**
	 * 从Type获取类名。 
	 * @rmx.summary 凡是Class<?>可返回Class<?> 如果是Map、Set等的Type，则有RawType返回Class<?>
	 * @param type
	 * @return 相应类
	 */
	public static Class<?> obtainClass(final Type type) {
		Class<?> clazz;
		if (type instanceof Class<?>) {
			clazz = (Class<?>) type;
		} 
//		else if (type instanceof ParameterizedTypeImpl) {
//			final ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) type;
//			clazz = typeImpl.getRawType();
//		} else {
//			return null;
//		}
		else{
			
			clazz = (Class<?>) invokeMethod("getRawType", type);
		}
		return clazz;
	}
	/**
	 * 获取集合中实际元素的泛型类型
	 * @param type
	 */
	public static Class<?> obtainCollectionRawType(final Type type) {
			Class<?> clazz;
//			if (type instanceof ParameterizedTypeImpl) {
//				final ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) type;
//				clazz = (Class<?>) typeImpl.getActualTypeArguments()[0];
//			} else {
//				return null;
//			}
			
			clazz = (Class<?>) ReflectUtil.getListActualType( type);


			return clazz;
	}
	/**
	 * 抛出运行时异常
	 * @param ex
	 */
	public static void rethrowRuntimeException(final Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		handleUnexpectedException(ex);
	}

	/**
	 * 设置属性值
	 * 本方法自动跟map参数properties中的key的值调用参数o的对应set方法，对o进行赋值。
	 * @param target 要赋值的javabean
	 * @param source key-value对应的map。
	 * @see SPFeature
	 */
	public static void setProperties(final Object target,final Map<String, Object> source,
			final SPFeature... features) {
		if(logger.isDebugEnabled())logger.debug("=====即将从Map"+source+"赋值给"+target);
		final Map<String, Method> setters = getSettersInContext(target.getClass(),
				source);
		final Map<String, ?> forMap,containsMap;
		if(source.size()>setters.size()){//为了提高循环的效率，进行map的大小判断，小的进行循环
			forMap = setters;
			containsMap=source;
		}else{
			containsMap = setters;
			forMap=source;
		}
		
		ReflectFeatureStatus status = new ReflectFeatureStatus(features);
		for (final String key : forMap.keySet()) {
			if (containsMap.containsKey(key)) {
				try {
					// 此处直接输入Method效率更高
					invokeSetterWithDefaultTypeCoventer(target, setters.get(key), source.get(key), status);
				} catch (final Exception e) {
					handleReflectionException(e);
				}
			}
		}
	}
	/**
	 * 对象赋值
	 * @rmx.summary 将源对象中与目标对象属性名相同的值进行复制。 方向是源对象orig的属性值复制到目标对象相应属性的值<br>
	 * 如果属性有映射，请列入methodMap中<br>
	 * @param target 目标对象
	 * @param source 源对象
	 * @param fieldMapType 属性映射 的映射种类，可以不传
	 * @param features
	 * @see SPFeature
	 */
	public static void copyProperties(final Object target, final Object source,
			final String fieldMapType,final String valueMapType,
			final SPFeature... features){
		Assert.notNull(target, "入参错误!目标对象为空!");
		Assert.notNull(source, "入参错误!源对象为空!");
		copyProperties(target, source,
				//TODO 测试完后使用第二段代码
				/*1.
				ReflectContextDefaultFactory.getFieldMap(target.getClass(), source.getClass(),fieldMapType),//进行属性映射的时候，因为是根据目标对象的set方法进行配置值，所以targetKey是key
				ReflectContextDefaultFactory.getCodeMap(target.getClass(),source.getClass()),	//进行值映射的时候，因为只能拿到来源的code，所以sourceCode是key且其必须唯一,但方向依然是t-s
				/*/
				ReflectConfigure.obtainReflectContext(target.getClass(), source.getClass(),fieldMapType,valueMapType),
				//*/
				features);
	}
	public static void copyProperties(final Object target, final Object source,
			final String fieldMapType,
			final SPFeature... features){
		copyProperties(target, source,
				//TODO 测试完后使用第二段代码
				/*1.
				ReflectContextDefaultFactory.getFieldMap(target.getClass(), source.getClass(),fieldMapType),//进行属性映射的时候，因为是根据目标对象的set方法进行配置值，所以targetKey是key
				ReflectContextDefaultFactory.getCodeMap(target.getClass(),source.getClass()),	//进行值映射的时候，因为只能拿到来源的code，所以sourceCode是key且其必须唯一,但方向依然是t-s
				/*/
				ReflectConfigure.obtainReflectContext(target.getClass(), source.getClass(),fieldMapType,null),
				//*/
				features);
	}
	/**
	 * 对象赋值
	 * @rmx.summary 将源对象中与目标对象属性名相同的值进行复制。 方向是源对象orig的属性值复制到目标对象相应属性的值<br>
	 * 如果属性有映射，请列入methodMap中<br>
	 * @param target 目标对象
	 * @param source 源对象
	 * @param features
	 * @see SPFeature
	 */
	public static void copyProperties(final Object target, final Object source,final SPFeature... features){
		copyProperties(target, source,
				//TODO 测试完后使用第二段代码
				/*1.
				ReflectContextDefaultFactory.getFieldMap(target.getClass(), source.getClass()),//进行属性映射的时候，因为是根据目标对象的set方法进行配置值，所以targetKey是key
				ReflectContextDefaultFactory.getCodeMap(target.getClass(),source.getClass()),	//进行值映射的时候，因为只能拿到来源的code，所以sourceCode是key且其必须唯一,但方向依然是t-s
				/*/
				ReflectConfigure.obtainReflectContext(target.getClass(), source.getClass(),null,null),
				//*/
				features);
	}
	/**
	 * 对象赋值
	 * @rmx.summary 将源对象中与目标对象属性名相同的值进行复制。 方向是源对象orig的属性值复制到目标对象相应属性的值<br>
	 * 如果属性有映射，请列入methodMap中<br>
	 * @param target 目标对象
	 * @param source 源对象
	 * @param methodMap set方法的属性名为key,get方法的属性名为value
	 * @param features
	 */
	public static void copyProperties(final Object target, final Object source,final Map<String,String>methodMap,final SPFeature... features) {
		copyProperties(target, source, methodMap, null, features);
	}
	/**
	 * 对象赋值
	 * @rmx.summary 将源对象中与目标对象属性名相同的值进行复制。 方向是源对象orig的属性值复制到目标对象相应属性的值<br>
	 * 如果属性有映射，请列入methodMap中<br>
	 * @param target 目标对象
	 * @param source 源对象
	 * @param methodMap set方法的属性名为key,get方法的属性名为value
	 * @param valueMap 
	 * @param features
	 */
	public static void copyProperties(final Object target, final Object source,
			final Map<String,String>methodMap,
			final Map<String,CodeMapItem>valueMap,
			final SPFeature... features) {
		if(logger.isDebugEnabled())logger.debug("=====即将从源对象"+source+"查找与目标对象"+target+"set方法对应的get方法进行赋值。");
		Assert.notNull(target);
		Assert.notNull(source);
		Assert.isSimpleBean(target);
		Assert.isSimpleBean(source);
		
		ReflectFeatureStatus status = new ReflectFeatureStatus(features);
		status.enableMethodMap = null!=methodMap && methodMap.size()>0;
		status.enableCodeMap = null!=valueMap && valueMap.size()>0;

		final Map<String, Method> origGetters = getAllGetters(source.getClass());
		Map<String, Method> destSetters = getAllSetters(target.getClass());
		
		//默认情况不对remex的ORMBeans的id，datastatus进行赋值
		if (!status.CopyIdAndDataStatus && modelableSupport && ModelableClass.isAssignableFrom(target.getClass())) {
				HashMap<String, Method> destSettersNoIdAndDataStatus = new HashMap<String, Method>();
				destSettersNoIdAndDataStatus.putAll(destSetters);
				destSettersNoIdAndDataStatus.remove("id");
				destSettersNoIdAndDataStatus.remove("dataStatus");
				destSetters = destSettersNoIdAndDataStatus;
		}

		// 赋值
		String getKey;
		CodeMapItem codeMapItem;
		for (final String setKey : destSetters.keySet()) {
			//默认情况get方法和set方法对应的属性名称是一致的。
			getKey=setKey;
			//如果有属性映射，则重新查找get方法对应的属性名称。
			if(status.enableMethodMap && methodMap.containsKey(setKey)){
				getKey = methodMap.get(setKey);
			}
			
			
			// 赋值
			if (origGetters.containsKey(getKey)) {
				/**原来的代码效率略低*/
//				Object value = invokeGetter(getKey, source);
//				Method setter = getSetter(target.getClass(), setKey);
//				invokeSetterWithDefaultTypeCoventer(target, setter, value,
//						features);
				/**这是优化后的代码，直接从当前map获得method*/
				Object value = ReflectUtil.invokeMethod(origGetters.get(getKey), source);
				//如果有得了code映射，。
				if(status.enableCodeMap && (codeMapItem = valueMap.get(setKey))!=null){
					value = codeMapItem.obtainCode(value);
				}
				invokeSetterWithDefaultTypeCoventer(target,destSetters.get(setKey), value, status);
			}else{
				//if(logger.isDebugEnabled())logger.debug("-----set所需要的方法"+key+"不在源对象"+source+"中，赋值目标对象为"+target);
			}
		}
	}
	
	/**
	 * 对象赋值
	 * @param target 目标对象
	 * @param source 源对象
	 * @param reflectContext 映射上下文
	 * @param features 附件功能
	 */
	private static void copyProperties(final Object target, final Object source,
			final ReflectContext reflectContext,
			final SPFeature... features) {
		if(logger.isDebugEnabled())logger.debug("=====即将从源对象"+source+"查找与目标对象"+target+"set方法对应的get方法进行赋值。");
		Assert.notNull(target);
		Assert.notNull(source);
		Assert.isSimpleBean(target);
		Assert.isSimpleBean(source);
		
		ReflectFeatureStatus status = new ReflectFeatureStatus(features);
		status.enableMethodMap = reflectContext.enableFieldMap;
		Map<String,String> methodMap = status.enableMethodMap ? reflectContext.getFieldMap() : null;
		status.enableCodeMap = reflectContext.enableCodeMap;
		Map<String, Map<String, String>> codeMap = status.enableCodeMap ? reflectContext.getCodeMap() : null;

		final Map<String, Method> origGetters = getAllGetters(source.getClass());
		Map<String, Method> destSetters = getAllSetters(target.getClass());
		
		//默认情况不对remex的ORMBeans的id，datastatus进行赋值
		if (!status.CopyIdAndDataStatus && modelableSupport && ModelableClass.isAssignableFrom(target.getClass())) {
				HashMap<String, Method> destSettersNoIdAndDataStatus = new HashMap<String, Method>();
				destSettersNoIdAndDataStatus.putAll(destSetters);
				destSettersNoIdAndDataStatus.remove("id");
				destSettersNoIdAndDataStatus.remove("dataStatus");
				destSetters = destSettersNoIdAndDataStatus;
		}

		// 赋值
		String getKey;
		for (final String setKey : destSetters.keySet()) {
			//默认情况get方法和set方法对应的属性名称是一致的。
			getKey=setKey;
			//如果有属性映射，则重新查找get方法对应的属性名称。
			if(status.enableMethodMap && methodMap .containsKey(setKey)){
				getKey =methodMap.get(setKey);
			}
			
			
			// 赋值
			if (origGetters.containsKey(getKey)) {
				/**原来的代码效率略低*/
//				Object value = invokeGetter(getKey, source);
//				Method setter = getSetter(target.getClass(), setKey);
//				invokeSetterWithDefaultTypeCoventer(target, setter, value,
//						features);
				/**这是优化后的代码，直接从当前map获得method*/
				Object value = ReflectUtil.invokeMethod(origGetters.get(getKey), source);
				//如果有得了code映射，。
				if(status.enableCodeMap && codeMap.containsKey(setKey)){
					Object targetValue = codeMap.get(setKey).get(value);
					if(null!=targetValue) value = targetValue;
				}
				invokeSetterWithDefaultTypeCoventer(target,destSetters.get(setKey), value, status);
			}else{
				//if(logger.isDebugEnabled())logger.debug("-----set所需要的方法"+key+"不在源对象"+source+"中，赋值目标对象为"+target);
			}
		}
	}

	/**
	 * 设置属性值
	 * @rmx.summary 本方法实现逐一从目标对象target中查找必要的set方法，并通过该set方法的规范属性名称<br>
	 * 从source对象的getValueMethod中获得值并填充到target对象中。<br>
	 * target拥有一个方法setName(),则本方法将使用name，调用source的取值方法，即本参数中的getValueMethod<br>
	 * @param source
	 * @param getValueMethod
	 *            source 的取值方法。比如getParameter(String key)
	 * @param target
	 * @param features
	 *            {@link SPFeature}
	 * @param methodKeyMap
	 * 			set方法的属性名为key,索引的方法的属性名为value
	 * @param namePreffixs
	 *            修正名字的前缀，可以最多传递多个，
	 */
	public static void setProperties(final Object target, final Object source,
			final Method getValueMethod, final SPFeature[] features,final Map<String,String>methodKeyMap, final String... namePreffixs) {
		if(logger.isDebugEnabled())logger.debug("=====即将从源对象"+source+"通过方法"+getValueMethod.getName()+"取值给"+target);

		Assert.notNull(target);
		Assert.notNull(source);
		Assert.notNull(getValueMethod);
		
		ReflectFeatureStatus status = new ReflectFeatureStatus(features);
		status.enableMethodMap = null!=methodKeyMap && methodKeyMap.size()>0;
//		final boolean methodKeyMapOn = null!=methodKeyMap && methodKeyMap.size()>0;

		final Map<String, Method> setters = getAllSetters(target.getClass());
//		final boolean namePreffixEnable = namePreffixs != null
//				&& namePreffixs.length > 0;
//		final boolean featuresOn = null != features && features.length > 0;
//		boolean JustNamePreffix = false,IgnoreCase = false;
//		
//		if(featuresOn)
//			for(final SPFeature feature:features){
//				if(feature == SPFeature.JustUseNamePreffix){
//					JustNamePreffix = namePreffixEnable && featuresOn;
//				}else if(feature == SPFeature.IgnoreCase){
//					IgnoreCase = true;
//				}
//			}

		for (String key : setters.keySet()) {
			try {
				Object value = null;
				final Method setter = setters.get(key);
				// 字段映射 必须在value = invokeMethod(getValueMethod, source, key);前面。
				if(status.enableMethodMap && methodKeyMap.containsKey(key)){
					key = methodKeyMap.get(key);
				}
				//如果忽略大小写
				if(status.IgnoreCase){
					key = key.toUpperCase();
				}
				
				if (status.JustUseNamePreffix) {
					// 前缀功能
					for (final String namePreffix : namePreffixs) {
						value = invokeMethod(
								getValueMethod,
								source,
								namePreffix
										+ StringHelper.upperFirstLetter(key));
						if (value != null) {
							break;
						}
					}
				}else{
					// 不使用前缀时的。
					value = invokeMethod(getValueMethod, source, key);
				}
				// 此处赋值
				invokeSetterWithDefaultTypeCoventer(target, setter, value,
						status);
			} catch (final Exception e) {
				handleReflectionException(e);
			}
		}
	}

	/**
	 * 此方法重载。
	 * <p>
	 * 默认从源对象取值的方法的参数为String.class
	 * 例如：source名为getValueMethodName的方法的唯一参数类型为String.class
	 * <p>
	 * 默认无需特性功能. 如果不开启特性功能 默认将进行全值赋值 即不进行null->""转化，并无论value是何值，将覆盖dest相应的值。
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName, final String... nameFixs) {
		final SPFeature[] features = null;
		setProperties(target, source, getValueMethodName, features, nameFixs);

	}

	/**
	 * @param target
	 * @param source
	 * @param getValueMethodName
	 * @param feature
	 * @param nameFixs
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName, final SPFeature feature, final String... nameFixs) {
		final SPFeature[] features = { feature };
		setProperties(target, source, getValueMethodName, features, nameFixs);

	}

	/**
	 * @param target
	 * @param source
	 * @param getValueMethodName
	 * @param features
	 * @param nameFixs
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName, final SPFeature[] features, final String... nameFixs) {
		try {
			setProperties(
					target,
					source,
					source.getClass().getMethod(getValueMethodName,
							String.class), features,null, nameFixs);
		} catch (final Exception e) {
			handleReflectionException(e);
		}
	}
	/**
	 * @param target
	 * @param source
	 * @param getValueMethodName
	 * @param methodKeyMap
	 * @param nameFixs
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName,final Map<String,String>methodKeyMap, final String... nameFixs) {
		final SPFeature[] features = null;
		setProperties(target, source, getValueMethodName, features,methodKeyMap, nameFixs);

	}

	/**
	 * @param target
	 * @param source
	 * @param getValueMethodName
	 * @param feature
	 * @param methodKeyMap
	 * @param nameFixs
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName, final SPFeature feature,final Map<String,String>methodKeyMap, final String... nameFixs) {
		final SPFeature[] features = { feature };
		setProperties(target, source, getValueMethodName, features, methodKeyMap,nameFixs);

	}

	/**
	 * 设置属性值
	 * @rmx.summary 将源对象的属性值拷贝给目标对象对应的属性<br>
	 * @param target 目标对象
	 * @param source 源对象
	 * @param getValueMethodName
	 * @param features 附件功能 
	 * @param methodKeyMap 方法map
	 * @param nameFixs 待确认
	 */
	public static void setProperties(final Object target, final Object source,
			final String getValueMethodName, final SPFeature[] features,final Map<String,String>methodKeyMap, final String... nameFixs) {
		try {
			setProperties(
					target,
					source,
					source.getClass().getMethod(getValueMethodName,
							String.class), features,methodKeyMap, nameFixs);
		} catch (final Exception e) {
			handleReflectionException(e);
		}
	}
	
	
	/**
	 * 将空转换为""
	 * @rmx.summary 将目标对象中为null的属性转换为""
	 * @param target 目标对象
	 */
	public static void convertNull2String(final Object target){
		final Map<String, Method> getters = ReflectUtil.getAllGetters(target.getClass());
		for(final String key:getters.keySet()){
			final Method getter = getters.get(key);
			if(null == invokeMethod(getter , target)
					&& getter.getReturnType()==String.class){
				invokeSetter(key, target, "");
			}
		}
	}
	static public <T extends Annotation> T getMethodAnnotation(final Class<?> clazz,final String methodName,final Class<T> annotationClass){
		Method m = getMethod(clazz, methodName);
        if(null!=m){
            T anno = m.getAnnotation(annotationClass);
            if(null!=anno) {
                return anno;
            }
        }
		return null;
	}
	static public <T extends Annotation> T getAnnotation(final Class<?> clazz,final Class<T> annotationClass){
		return clazz.getAnnotation(annotationClass);
	}

	static public <T extends Annotation> T getAnnotation(final Method method, final Class<T> annotationClass) {
		return method.getAnnotation(annotationClass);
	}
	/**
	 * 获取注解类
	 * @rmx.summary 根据getter，setter,field的顺序取查找annotationClass
	 * 
	 * LHY 2015-9-13 field必须是最后。因为子类可以通过重载方法(getter/setter)来正获取自定义的anno。
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param annotationClass
	 * @return T
	 */
	static public <T extends Annotation> T getAnnotation(final Class<?> clazz,final String fieldName,final Class<T> annotationClass){
		T anno;
		Field f;
		Method m;
		f= ReflectUtil.getField(clazz,fieldName);
		m=ReflectUtil.getGetter(clazz,fieldName);
		if(null!=m){
			anno = m.getAnnotation(annotationClass);
			if(null!=anno) {
				return anno;
			}
		}
		m=ReflectUtil.getSetter(clazz,fieldName);
		if(null!=m){
			anno = m.getAnnotation(annotationClass);
			if(null!=anno) {
				return anno;
			}
		}
		if(null!=f){
			anno = f.getAnnotation(annotationClass);
			if(null!=anno) {
				return anno;
			}
		}
		return null;
	
	}

}
