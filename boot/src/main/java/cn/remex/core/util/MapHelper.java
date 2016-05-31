package cn.remex.core.util;

import cn.remex.core.exception.ServiceCode;
import cn.remex.core.exception.InvalidCharacterException;
import cn.remex.core.reflect.ReflectFeatureStatus;
import cn.remex.core.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.util.*;
/**
 * Map处理工具类
 */
/** 
 * @author liuhengyang 
 * @date 2014-9-24 下午2:31:15
 * @version 版本号码
 * @TODO 描述
 */
public class MapHelper {

	/**
	 * <br>将数据扁平化变成一个简单的二维map列表,如:
	 * <br>a-1
	 * <br>b-2
	 * <br>c-LHY
	 * <br>d.id-SN001
	 * <br>d.name-test
	 * 
	 * <p>规则如下：
	 * <li>1.基本类型、String、Enum作为base数据处理。
	 * <li>2.Object,Map 当做统一的object数据处理，通过.来引用属性。 Map的key必须支持toString()
	 * <li>3.Collection 当做数组处理，通过[n]引用属性。
	 * <li>4.特殊类型通过toString()方法处理。
	 * </p>
	 * <br>
	 * <p>要点:
	 * <li>1.只能序列化对象在存在getter方法的属性
	 * 
	 * </p>
	 * @param o
	 * @return map 返回一个扁平的Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> objectToFlat(Object o){
		return (Map<String,String>)_toFlatData(o, "", null);
	}
	
	
	/**
	 * 根据对象的结构生成一个树形结构的maptree
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> objectToMapTree(Object o){
		return (Map<String, Object>) _toMapTree(o);
	}
	private static Method instanceMehtod;
	static{
		try {
			instanceMehtod = ReflectUtil.class.getMethod("invokeNewInstance", Class.class);
		} catch (Exception e) {
			throw new RuntimeException("获取工厂方法失败！",e);
		}
	}
	/**
	 * 将key-value列表转化为一个对象。
	 * key可是是包含".""[]"的表达式。
	 * 
	 * @param t
	 * @param map
	 */
	public static void objectFromFlat(Object t,Map<String,Object> map){
		for(String key:map.keySet()){
			Object value = map.get(key);
			evalFlatValueToObjectField(t, key, value,ReflectUtil.class,instanceMehtod);
		}
	}
	/**
	 * 将key-value列表转化为一个对象。
	 * key可是是包含".""[]"的表达式。
	 * 
	 * 如果需要生成新的对象，采用默认的ReflectUtils.invokeNewInstance
	 * 
	 * @param t
	 * @param flatKey
	 * @param value
	 */
	public static void evalFlatValueToObjectField(Object t, String flatKey, Object value){
		evalFlatValueToObjectField(t, flatKey, value, ReflectUtil.class, instanceMehtod);
	}
	/**
	 * 根据flatkey设置目标对象的值。
	 * 
	 * @param t
	 * @param flatKey
	 * @param value
	 * @param instanceFactory
	 * @param instanceMethod
	 */
	public static void evalFlatValueToObjectField(Object t, String flatKey, Object value, Object instanceFactory, Method instanceMethod){
		try{
			_flat2Object(t, flatKey, value,instanceFactory,instanceMethod);
		}catch (Exception e) {
			throw new RuntimeException("evalFlatValueToObjectField失败！",e);
		}
	}
	
	public static void objectFromMapTree(Object t,Map<String,String> map){
		throw new RuntimeException("MapTree 2 Object 功能尚未实现！");
	}
	
	/**
	 * 将对象的编制为Map，Map中的key为属性名称，value为属性对应的值。空值(null)将被忽略
	 * 
	 * @param obj java对象
	 * @return {@link Map}
	 */
	public static Map<String, Object> toMap(Object obj) {
		Assert.notNull(obj, ServiceCode.FAIL, "需要转化的对象不可以为空");

		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Method> getters = ReflectUtil.getAllGetters(obj.getClass());

		for (String fieldName : getters.keySet()) {
			Object value = ReflectUtil
					.invokeMethod(getters.get(fieldName), obj);
			if (null != value)
				map.put(fieldName, value);
		}

		return map;

	}

	public static Map<String, String> toStringMap(Object obj) {
		Assert.notNull(obj, ServiceCode.FAIL,  "需要转化的对象不可以为空");

		Map<String, String> map = new HashMap<>();

		Map<String, Method> getters = ReflectUtil.getAllGetters(obj.getClass());

		for (String fieldName : getters.keySet()) {
			Object value = ReflectUtil
					.invokeMethod(getters.get(fieldName), obj);
			if (null != value)
				map.put(fieldName, String.valueOf(value));
		}

		return map;

	}

	/**
	 * 将根据参数 {@link Class} clazz 将Map转化为其一个实例。将根据class的属性从map的key中取值并赋值。
	 * <li>Class必须有默认的无参构造函数。
	 * <li>从map中赋值时，null值，参数类型无法case或者无法转化的值将被忽略。
	 * <li>使用{@link ReflectUtil#invokeSetterWithDefaultTypeCoventer(Object, Method, Object, ReflectFeatureStatus)}进行set赋值。
	 * @param map Map值
	 * @param clazz java类
	 * @return clazz的一个实例
	 */
	public static <T> T toObject(Map<String, Object> map, Class<T> clazz) {
		Assert.notNull(map, ServiceCode.FAIL,  "需要生产对象的Map不可以为null");


		Map<String, Method> setters = ReflectUtil.getAllSetters(clazz);
		
		T dest = ReflectUtil.invokeNewInstance(clazz);

		ReflectFeatureStatus status = new ReflectFeatureStatus(null);
		for (String fieldName : setters.keySet()) {
			Object value = map.get(fieldName);
			if (null != value){
				Method setter = setters.get(fieldName);
				ReflectUtil.invokeSetterWithDefaultTypeCoventer(dest, setter , value, status);
			}
		}

		return dest;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void _flat2Object(Object t,String nextPath, Object value, Object instanceFactory, Method instanceMethod) throws Exception{
		Class<?> clazz = t.getClass();
		int ci=-1,li=-1,li_r=-1;
		ci=nextPath.indexOf('.');
		if(((li=nextPath.indexOf("["))>ci || li==-1) && ci>0 ){//L1属性此时是个对象
			String fieldName = nextPath.substring(0,ci);
			Object _v = ReflectUtil.invokeGetter(fieldName, t);
			
			if(null == _v){
				Method setter = ReflectUtil.getSetter(clazz, fieldName);
				Class<?> pt = setter.getParameterTypes()[0];
				_v = instanceMethod.invoke(instanceFactory,pt); 
				ReflectUtil.invokeSetter(fieldName, t, _v);
			}
			_flat2Object(_v, nextPath.substring(ci+1), value,instanceFactory,instanceMethod);
		}else if(li>0){//L2属性此时是个数组
			String fieldName = nextPath.substring(0,li);
			Object _v = ReflectUtil.invokeGetter(fieldName, t);
			
			Method setter = ReflectUtil.getSetter(clazz, fieldName);
			Class<?> pt = setter.getParameterTypes()[0];
			if(null == _v){
				if(null == _v && List.class.isAssignableFrom(pt)){ // 仅支持list类型
					if(pt.isInterface()){
						_v = new ArrayList();
					}else if(pt.isAnonymousClass()){
						_v = instanceMethod.invoke(instanceFactory,pt); 
					}
					ReflectUtil.invokeSetter(fieldName, t, _v);
				}
			}
			List _l = (List)_v;
			String arrayIndex = nextPath.substring(li+1, (li_r=nextPath.indexOf("]")));
			int n = Judgment.nullOrBlank(arrayIndex)?-1:Integer.parseInt(arrayIndex); //LHY 2015-1-16 新增对没有下表[] 的支持
			
			//右中括号后面只会出现三种情况，1)结束，2).引用，3)[新数组引用
			if(li==nextPath.length()){
				//nextPath结束
				_l.set(n, value);
				return;
			}
			String l_nextFlag = nextPath.substring(li_r+1, li_r+2); // . or [
			if(".".equals(l_nextFlag)){
				Object _l_v;
				if(n>_l.size()||0==_l.size() ||n<0){//没有值 //当中括号为[]时n<0
					for(int i=_l.size();i<=n;i++){
						_l.add(null);
					}
					Method getter = ReflectUtil.getGetter(clazz.getName().contains("$$EnhancerByCGLIB$$")?clazz.getSuperclass():clazz, fieldName);
					Class<?> _fc = ReflectUtil.getListActualType(getter.getGenericReturnType());
					_l_v = instanceMethod.invoke(instanceFactory,_fc);
					if(n==-1) _l.add(_l_v); else _l.set(n, _l_v); //当中括号为[]时直接添加
				}else {
					_l_v = _l.get(n);
				}
				_flat2Object(_l_v, nextPath.substring(li_r+(n>0?2:1)), value, instanceFactory, instanceMethod);
			}else if("[".equals(l_nextFlag)){ // 数组嵌套
				throw new InvalidCharacterException("暂不支持数组嵌套！");
			}else{
				throw new InvalidCharacterException("非法字符："+nextPath);
			}
		}else{//基本类型 需要case
			Method setter = ReflectUtil.getSetter(clazz, nextPath);
			if(null==setter || null == value)return;


			Class fieldClass = setter.getParameterTypes()[0];
			if(ReflectUtil.isSimpleType(fieldClass)) {
				ReflectUtil.invokeMethod(setter, t, ReflectUtil.caseObject(fieldClass, value, instanceFactory, instanceMethod, fieldClass));
			}else{

				value = value instanceof String?value:value.getClass().isArray() && ((Object[]) value).length>0?((Object[]) value)[0].toString():null;
				if(!Judgment.nullOrBlank(value)){//json 字符串
					if( ((String) value).startsWith("{")){
						Object o = JsonHelper.toJavaObject(value.toString(), fieldClass);
						ReflectUtil.invokeMethod(setter, t, o);
					}else if( ((String) value).startsWith("[") ){
						Object o = JsonHelper.toJavaObject(value.toString(), setter.getGenericParameterTypes()[0]);
						ReflectUtil.invokeMethod(setter, t, o);
					}
				}
			}
		} 
	}
	
	private static Object _toFlatData(Object o,String root,Map<String,String> rootMap){
		Assert.notNull(o, ServiceCode.FAIL, "需要扁平化的对象不能为空！");
		
		Class<?> clazz = o.getClass();
		Map<String,String> map = null==rootMap?new LinkedHashMap<String, String>():rootMap;
		
		if(Collection.class.isAssignableFrom(clazz)){
			Collection<?> _c = (Collection<?>)o;
			int i=0;
			for(Object value:_c){
				_toFlatData(value, new StringBuilder(root).append("[").append(i++).append("]").toString(), map);
			}
		}else if(Map.class.isAssignableFrom(clazz)){
			Map<?,?> _m = ((Map<?,?>)o);
			for(Object key:_m.keySet()){
				_toFlatData(_m.get(key), new StringBuilder(root).append(Judgment.nullOrBlank(root)?"":".").append(key).toString(), map);
			}
		}else if(ReflectUtil.isSimpleType(clazz) || Enum.class.isAssignableFrom(clazz)){ // 属性是简单类型,非Map及Collection类型
			map.put(new StringBuilder(root).toString(), null==o?null:o.toString());
		}else{
			Map<String, Method> getters = ReflectUtil.getAllUserGetters(o.getClass());
			for(String fieldName:getters.keySet()){
				Method getter = getters.get(fieldName);
				Object value = ReflectUtil.invokeMethod(getter , o);
				if(null==value)continue;
				_toFlatData(value, new StringBuilder(root).append(Judgment.nullOrBlank(root)?"":".").append(fieldName).toString(), map);
			}
			
		}
		
		return map;
	}
	
	private static Object _toMapTree(Object o){
 
		Assert.notNull(o, ServiceCode.FAIL, "需要MapTree化的对象不能为空！");
		Class<?> clazz = o.getClass();
		if(ReflectUtil.isSimpleType(clazz) || Enum.class.isAssignableFrom(clazz)){
			return o.toString();
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(Collection.class.isAssignableFrom(clazz)){
			Collection<?> _c = (Collection<?>)o;
			int i=0;
			for(Object value:_c){
				map.put(String.valueOf(i++), _toMapTree(value));
			}
		}else if(Map.class.isAssignableFrom(clazz)){
			Map<?,?> _m = ((Map<?,?>)o);
			for(Object key:_m.keySet()){
				map.put(key==null?null:key.toString(), _m.get(key));
			}
		}else{
			Map<String, Method> getters = ReflectUtil.getAllUserGetters(o.getClass());
			for(String fieldName:getters.keySet()){
				Method getter = getters.get(fieldName);
				Object value = ReflectUtil.invokeMethod(getter , o);
				if(null==value)continue;
				map.put(fieldName, _toMapTree(value));
			}
		}
		
		return map;
	}
}
