package cn.remex.db.sql;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.ReadOnlyMap;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class SqlType {
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>> TAllFields = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>> TBaseFields = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TBaseGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TBaseSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>> TCollectionFields = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TCollectionGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TCollectionSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>> TMapFields = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TMapGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TMapSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>> TObjectFields = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Type>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TObjectGetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>> TObjectSetter = new ReadOnlyMap<Class<?>, ReadOnlyMap<String,Method>>();
	final static private ArrayList<Class<?>> TypeBase = new ArrayList<Class<?>>(0);


	final static private ArrayList<Class<?>> TypeNumeric = new ArrayList<Class<?>>(0);
	static{
		TypeNumeric.add(int.class);
		TypeNumeric.add(long.class);
		TypeNumeric.add(double.class);
		TypeNumeric.add(float.class);
		TypeNumeric.add(boolean.class);
		TypeNumeric.add(short.class);
		TypeNumeric.add(char.class);
		TypeNumeric.add(byte.class);

		TypeNumeric.add(Integer.class);
		TypeNumeric.add(Long.class);
		TypeNumeric.add(Double.class);
		TypeNumeric.add(Float.class);
		TypeNumeric.add(Boolean.class);
		TypeNumeric.add(Short.class);
		TypeNumeric.add(Character.class);
		TypeNumeric.add(Byte.class);

		TypeBase.addAll(TypeNumeric);
		TypeBase.add(String.class);
		TypeBase.add(Date.class);
	}

	public static void clearCache() {
		TAllFields.clear();
		//		AllGetter.clear();
		//		AllSetter.clear();

		TBaseFields.clear();
		TBaseGetter.clear();
		TBaseSetter.clear();

		TCollectionFields.clear();
		TCollectionGetter.clear();
		TCollectionSetter.clear();

		TMapFields.clear();
		TMapGetter.clear();
		TMapSetter.clear();

		TObjectFields.clear();
		TObjectGetter.clear();
		TObjectSetter.clear();

	}
	
	/**
	 * 
	 * 获取类可以get、set的属性，必须同时拥有get set方法
	 * 含父类的
	 * 
	 * key是属性的名称
	 * value是属性的Type，如果是TObject 则是OrmBeanClass
	 * @param clazz
	 * @param fieldType
	 * @return Map
	 * @throws Exception
	 */
	public static Map<String,Type> getFields(final Class<?> clazz, final FieldType fieldType){
		ReadOnlyMap<Class<?>, ReadOnlyMap<String, Type>> pool;
		ReadOnlyMap<String,Type> fields;

		//分类对待
		switch (fieldType) {
		case TBase:		pool=TBaseFields;break;
		case TObject:	pool=TObjectFields;break;
		case TCollection:pool=TCollectionFields;break;
		case TMap:		pool=TMapFields;break;
		default:		pool=TAllFields;break;
		}

		//如果缓冲池中有
		fields = pool.get(clazz);
		if(null!=fields) {
			return fields;
		}

		//没有则根据TKind类型生成
		fields = new ReadOnlyMap<String, Type>();
		Map<String, Method> setters = getSetters(clazz,fieldType);
		Map<String, Method> getters = getGetters(clazz,fieldType);
		for(String fieldName:setters.keySet()){
			if(getters.containsKey(fieldName)) {
				fields.put(fieldName,getters.get(fieldName).getGenericReturnType());
			}
		}
		//放入缓存
		pool.put(clazz, fields);

		return fields;
	}
	/**
	 * 获取类的getter方法，根据TKind的类别获取
	 * 
	 * @param clazz
	 * @param TKind
	 * @return Map 返回的方法一定有一个对应的setter
	 */
	public static Map<String, Method> getGetters(final Class<?> clazz, final FieldType fieldType) {
		return getMethods(clazz, MethodType.MTGet, fieldType);
	}
	/**
	 * 获取类的setter方法，根据TKind的类别获取
	 * @param clazz
	 * @param fieldType
	 * @return 返回的方法一定有一个对应的Getter
	 * @throws Exception
	 */

	public static Map<String, Method> getSetters(final Class<?> clazz, final FieldType fieldType){
		return getMethods(clazz, MethodType.MTSet, fieldType);
	}
	/**
	 * 测试是否是基本类型，基本类型包括八种基本数据+String+Enum
	 * 如果是基本Class<Object>又不在ORMBeans
	 * 如果是Collection Map却不包含ORMbeans的泛型
	 * @param type
	 * @return boolean
	 */
	public static boolean isTBase(final Type type){
		if(TypeBase.contains(type)
				|| Enum.class.isAssignableFrom(ReflectUtil.obtainClass(type))) {
			return true;
			/*暂不开放非ORMBeans对象保存
		else if(type instanceof Class<?> //不是ORMBeans的object
				&& !RsqlCore.hasOrmBeanClass(type))
			return true;
		else if(type instanceof ParameterizedTypeImpl//不含有Ormbeans的泛型
				&& !isTCollection(type) && !isTMap(type))
			return true;*/
		} else {
			return false;
		}
	}
	/**
	 * 测试是否为TCollection<br>
	 * 条件是Collection的子类或者实现,如List，Set，Vector等<br>
	 * 如果这些Collection接口的实现类型包含的泛型不是数据库ORMBeans，不能列入TCollection
	 * 
	 * @param type
	 * @return boolean
	 */
	public static boolean isTCollection(final Type type){
		Class<?> clazz2 = ReflectUtil.obtainClass(type);
		if(Collection.class.isAssignableFrom(clazz2)){
			//如果是Collection接口的实现，必须有泛型，不然也不能当作TCollection
			//因此Type也必须是ParameterizedTypeImpl类型，其
//			Type[] atas = ((ParameterizedTypeImpl)type).getActualTypeArguments();
			Type[] atas = ReflectUtil.getActualTypeArguments(type);
			if(atas.length==1) {
				return RDBManager.getLocalSpaceConfig().hasOrmBeanClass(atas[0]);
			} else {
				return false;
			}
		}
		return false;
	}
	/**
	 * 测试是否为TMap
	 * 条件是Map的子类或者实现,如HashMap、TreeMap、LinkedHashMap等
	 * 如果这些Map接口的实现类型包含的泛型都不是数据库ORMBeans，不能列入TMap
	 * @param type
	 * @return boolean
	 */
	public static boolean isTMap(final Type type){
		Class<?> clazz2 = ReflectUtil.obtainClass(type);
		if(Map.class.isAssignableFrom(clazz2)){
			//如果是Map接口的实现，必须有泛型，不然也不能当作TMap
			//因此Type也必须是ParameterizedTypeImpl类型，两个泛型这至少有一个是
//			Type[] atas = ((ParameterizedTypeImpl)type).getActualTypeArguments();
			Type[] atas = ReflectUtil.getActualTypeArguments(type);

			RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
			if(atas.length==2) {
				return spaceConfig.hasOrmBeanClass(atas[0])
						|| spaceConfig.hasOrmBeanClass(atas[1]);
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 测试是否为TObject
	 * 条件是type为Class<?>,并且在ORMBeans中等
	 * @param type
	 * @return boolean
	 */
	public static boolean isTObject(final Type type){
		if(type instanceof Class<?> && RDBManager.getLocalSpaceConfig().hasOrmBeanClass(type)) {
			return true;
		}
		return false;
	}
	/**
	 * 获取类的方法，根据MTKind(set/get)分类，TKind的类别获取
	 * @param clazz
	 * @param TKind
	 * @param methodType
	 * @return
	 */
	private static Map<String, Method> getMethods(final Class<?> clazz, final MethodType methodType, final FieldType fieldType) {
		ReadOnlyMap<Class<?>, ReadOnlyMap<String, Method>> pool;
		ReadOnlyMap<String, Method> methods;
		Map<String, Method> allNeedMethods,allSetMethods,allGetMethods;
		//		Method checkSetMethod,checkGetMethod;
		//先按照set/get分类，在按照TKind分类
		switch (methodType) {
		case MTSet:
			allNeedMethods = ReflectUtil.getAllSetters(clazz);
			switch (fieldType) {
			case TBase:		pool=TBaseSetter;break;
			case TObject:	pool=TObjectSetter;break;
			case TCollection:pool=TCollectionSetter;break;
			case TMap:		pool=TMapSetter;break;
			default:		return allNeedMethods;
			}
			break;
		case MTGet:
			allNeedMethods = ReflectUtil.getAllGetters(clazz);
			switch (fieldType) {
			case TBase:		pool=TBaseGetter;break;
			case TObject:	pool=TObjectGetter;break;
			case TCollection:pool=TCollectionGetter;break;
			case TMap:		pool=TMapGetter;break;
			default:		return allNeedMethods;
			}
			break;
		default:
			return null;
		}

		//如果缓存中有则直接返回
		methods = pool.get(clazz);
		if(null!=methods) {
			return methods;
		}

		//如果缓存中没有则生成
		allSetMethods = ReflectUtil.getAllSetters(clazz);
		allGetMethods = ReflectUtil.getAllGetters(clazz);
		methods = new ReadOnlyMap<String, Method>();
		Method setter,getter;
		Type sType[],gType;
		switch (fieldType) {
		case TBase:
			for(String fieldName:allNeedMethods.keySet()){
				setter = allSetMethods.get(fieldName);
				getter = allGetMethods.get(fieldName);
				if(null==setter || null==getter) {
					continue;
				}
				sType = setter.getGenericParameterTypes();
				gType = getter.getGenericReturnType();
				if(null!=gType && null!=sType //测试set和对应的get都有
						&& sType.length==1    	//测试个数符合要求
						&& ReflectUtil.evaluateEqual(gType, sType[0])		//测试类型符合要求
						&& isTBase(gType)) {
					methods.put(fieldName,allNeedMethods.get(fieldName));
				}
			}
			break;
		case TObject:
			for(String fieldName:allNeedMethods.keySet()){
				setter = allSetMethods.get(fieldName);
				getter = allGetMethods.get(fieldName);
				if(null==setter || null==getter) {
					continue;
				}
				sType = setter.getGenericParameterTypes();
				gType = getter.getGenericReturnType();
				if(null!=gType && null!=sType //测试set和对应的get都有
						&& sType.length==1    	//测试个数符合要求
						&& ReflectUtil.evaluateEqual(gType, sType[0])		//测试类型符合要求
						&& isTObject(gType)) {
					methods.put(fieldName,allNeedMethods.get(fieldName));
				}
			}
			break;
		case TCollection:
			for(String fieldName:allNeedMethods.keySet()){
				setter = allSetMethods.get(fieldName);
				getter = allGetMethods.get(fieldName);
				if(null==setter || null==getter) {
					continue;
				}
				sType = setter.getGenericParameterTypes();
				gType = getter.getGenericReturnType();
				if(null!=gType && null!=sType 	//测试set和对应的get都有
						&& sType.length==1    	//测试个数符合要求
						&& ReflectUtil.evaluateEqual(gType, sType[0])		//测试类型符合要求
						&& isTCollection(gType)) {
					methods.put(fieldName,allNeedMethods.get(fieldName));
				}
			}
			break;
		case TMap:
			for(String fieldName:allNeedMethods.keySet()){
				setter = allSetMethods.get(fieldName);
				getter = allGetMethods.get(fieldName);
				if(null==setter || null==getter) {
					continue;
				}
				sType = setter.getGenericParameterTypes();
				gType = getter.getGenericReturnType();
				if(null!=gType && null!=sType //测试set和对应的get都有
						&& sType.length==1    	//测试个数符合要求
						&& ReflectUtil.evaluateEqual(gType, sType[0])		//测试类型符合要求
						&& isTMap(gType)) {
					methods.put(fieldName,allNeedMethods.get(fieldName));
				}
			}
			break;

		default:
			return null;
		}

		pool.put(clazz, methods);
		return methods;
	}


}
