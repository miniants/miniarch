package cn.remex.core.reflect;

import java.util.HashMap;
import java.util.Map;



/**
 * 映射配置类
 * @author HengYang Liu
 */
public class ReflectConfigure {
	
	private static boolean checkModelabeClass = false;
	private static Class<?> ModelabeClass = null;
	private static ReflectContextFactory reflectContextFactory = null;
	static{
		try {
			ModelabeClass = Class.forName("cn.remex.db.rsql.model.Modelable");
			checkModelabeClass = true;
		} catch (ClassNotFoundException e) {
			ModelabeClass = null;
			checkModelabeClass = false;
		}
	}

	/**
	 * 判断一个对象是否为一个数据库model
	 * @rmx.summary 
	 * @param obj
	 * @return
	 * TODO 暂未用
	 */
	static boolean isModel(Object obj){
		return checkModelabeClass && ModelabeClass.isAssignableFrom(obj.getClass());
	}

	
	

	public static Map<String, FieldMapper> FieldMapperMap = new HashMap<String, FieldMapper>();
	public static Map<String, CodeMapper> CodeMapperMap = new HashMap<String, CodeMapper>();
//	private static final FieldMapper NullFieldMapper = new FieldMapper();
//	private static final CodeMapper NullCodeMapper = new CodeMapper();
	public static Map<String, ReflectContext> ReflectContextMap = new HashMap<String, ReflectContext>();
	final public static ReflectContext NullReflectContext = new ReflectContext();

	private static boolean autoFetchFromPlugin = false;


//	public static Map<String, String> getFieldMap(final Class<?> leftClass, final Class<?> rightClass, final String mapType) {
//		String hashcode = ReflectUtil.hashCodeWithOrder(leftClass, rightClass, mapType);
//		// String hashcode = ReflectUtil.hashCode(leftClass, rightClass, mapType);
//		FieldMapper fieldMapper = FieldMapperMap.get(hashcode);
//		if (autoFetchFromPlugin && fieldMapper == null) { // 缓存为空，查询数据库
//			// 当前正向映射
//			fieldMapper = obtainFieldMapper(leftClass, rightClass, mapType,MapDirection_Forward);
//			FieldMapper revertFieldMapper = obtainFieldMapper(rightClass, leftClass, mapType,MapDirection_Forward);// 注意方向
//			
//			//正向映射：不为空则使用，否则使用反向映射的反转
//			FieldMapperMap.put(hashcode, null != fieldMapper ? fieldMapper : ( null != revertFieldMapper ?obtainFieldMapper(rightClass, leftClass, mapType,MapDirection_Reverse):NullFieldMapper));
//			
//			//反向映射：不为空则使用，否则使用正向映射的反转
//			String revertHashcode = ReflectUtil.hashCodeWithOrder(rightClass, leftClass, mapType);
//			FieldMapperMap.put(revertHashcode, null != revertFieldMapper ? revertFieldMapper : ( null != fieldMapper ?obtainFieldMapper(leftClass, rightClass, mapType,MapDirection_Reverse):NullFieldMapper));
//
//			fieldMapper = FieldMapperMap.get(hashcode);//此步很关键，否则第一次是取不到值得
//		}
//		return null != fieldMapper ? fieldMapper.obtainFieldMap() : null;
//	}

//	/**
//	 * @param leftClass
//	 * @param rightClass
//	 * @param mapType
//	 * @param direction  属性映射读取配置的方向，1-正向，即left->right； 不为1 - 反向，即right->left<br>
//	 * 这的里正向方向，和映射的正向反向不同。此处是值从数据库中根据leftClas和rightClass的已定属性参数的取值方向。<br>
//	 * 比如：数据中配置了A类到B类的属性映射{A:B;C:D}，但没有配置B类到A类的配置，此时，只需要将该参数改为：<br>
//	 * {@link ReflectContextDefaultFactory#MapDirection_Reverse}则也能获得B到A的属性映射{B:A;D:C}<br>
//	 * @return
//	 */
//	private static FieldMapper obtainFieldMapper(final Class<?> leftClass, final Class<?> rightClass, final String mapType,final int direction) {
//		// 查询DataMapping数据库表，返回表字段映射map
//		Map<String, String> map = ContainerFactory.getSession().query(DataMapping.class, new SqlPredicate<DataMapping>() {
//			@Override
//			public Class<?> initRules(DataMapping t) {
//				if (null != mapType)
//					addRule(t.getMapType(), WhereRuleOper.eq, mapType);
//				addRule(t.getTargetClass(), WhereRuleOper.eq, StringHelper.getClassName(leftClass));
//				addRule(t.getSourceClass(), WhereRuleOper.eq, StringHelper.getClassName(rightClass));
////				setRowCount(10000);
//				return null;
//			}
//		}).obtainMap(direction==MapDirection_Forward?"targetKey":"sourceKey",direction==MapDirection_Forward?"sourceKey":"targetKey");//进行属性映射的时候，因为是根据目标对象的set方法进行配置值，所以targetKey是key
//
//		if (map.size() > 0) {
//			FieldMapper fieldMapper = direction==MapDirection_Forward?new FieldMapper(leftClass, rightClass):new FieldMapper(rightClass, leftClass);
//			fieldMapper.setFieldMap(map);
//			return fieldMapper;
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * @param leftClass
//	 * @param rightClass
//	 * @param mapType
//	 * @return
//	 */
//	public static Map<String, CodeMapItem> getCodeMap(final Class<?> leftClass, final Class<?> rightClass) {
//		return getCodeMap(leftClass, rightClass, null);
//	}
//
//	/**
//	 * @param leftClass
//	 * @param rightClass
//	 * @param mapType
//	 * @return
//	 */
//	public static Map<String, CodeMapItem> getCodeMap(final Class<?> leftClass, final Class<?> rightClass, final String mapType) {
//		String hashcode = ReflectUtil.hashCodeWithOrder(leftClass, rightClass, mapType);
//		CodeMapper codeMapper = CodeMapperMap.get(hashcode);
//		if (autoFetchFromPlugin && codeMapper == null){ // 缓存为空，查询数据库
//			// 当前正向映射
//			codeMapper = obtainCodeMapper(leftClass, rightClass, mapType,MapDirection_Forward);
//			// 反向映射
//			CodeMapper revertCodeMapper = obtainCodeMapper(rightClass, leftClass, mapType,MapDirection_Forward);
//			
//			//正向映射：不为空则使用，否则使用反向映射的反转
//			CodeMapperMap.put(hashcode, null != codeMapper ? codeMapper : ( null != revertCodeMapper ?obtainCodeMapper(rightClass, leftClass, mapType,MapDirection_Reverse):NullCodeMapper));
//			
//			//反向映射：不为空则使用，否则使用正向映射的反转
//			String revertHashcode = ReflectUtil.hashCodeWithOrder(rightClass, leftClass, mapType);
//			CodeMapperMap.put(revertHashcode , null != revertCodeMapper ? revertCodeMapper : ( null != codeMapper ?obtainCodeMapper(leftClass, rightClass, mapType,MapDirection_Reverse):NullCodeMapper));
//			
//			codeMapper = CodeMapperMap.get(hashcode);//此步很关键，否则第一次是取不到值得
//		}
//		
//		if(RemexConstants.loggerDebug)
//			RemexConstants.logger.debug("代码映射,leftClass："+leftClass+"；rightClass："+rightClass+"；mapType："+mapType+"\r\n映射代码"+codeMapper);
//		
//		return null != codeMapper ? codeMapper.obtainCodeMap() : null;
//	}

//	private static CodeMapper revertCodeMapper(CodeMapper codeMapper) {
//		Map<String, CodeMapItem> codemap = codeMapper.obtainCodeMap();
//		Map<String, String> fieldMap = ReflectContextDefaultFactory.getFieldMap(codeMapper.getLeftClass(), codeMapper.getRightClass());
//
//		CodeMapper revertCodeMapper = new CodeMapper(codeMapper.getRightClass(), codeMapper.getLeftClass());
//		for (String field : codemap.keySet()) {
//			CodeMapItem curCmi = codemap.get(field);
//			Map<String, String> curCodes = curCmi.getCodeMap();
//			CodeMapItem cmi = revertCodeMapper.new CodeMapItem();
//			Map<String, String> revertCodes = new HashMap<String, String>();
//
//			for (String key : curCodes.keySet()) {
//				// 因为反转，所以此行的参数right、left要注意方向
//				revertCodes.put(curCodes.get(key), key);
//			}
//			cmi.setCodeMap(revertCodes);
//
//			revertCodeMapper.putCodeMapItem(fieldMap.get(field), cmi);
//		}
//		return revertCodeMapper;
//	}
//
//	/**
//	 * @param leftClass
//	 * @param rightClass
//	 * @param mapType
//	 * @param direction  代码映射读取配置的方向，1-正向，即left->right； 不为1 - 反向，即right->left<br>
//	 * 这的里正向方向，和映射的正向反向不同。此处是值从数据库中根据leftClas和rightClass的已定参数的取值方向。<br>
//	 * 比如：数据中配置了A类到B类的代码映射{A:B;C:D}，但没有配置B类到A类的配置，此时，只需要将该参数改为：<br>
//	 * {@link ReflectContextDefaultFactory#MapDirection_Reverse}则也能获得B到A的代码映射{B:A;D:C}
//	 * @return
//	 */
//	private static CodeMapper obtainCodeMapper(final Class<?> leftClass, final Class<?> rightClass, final String mapType,
//			final int direction
//			) {
//		// 查询DataMapping数据库表，返回表字段映射map
//		DbRvo rvo = ContainerFactory.getSession().query(CodeMapping.class, new SqlPredicate<CodeMapping>() {
//			@Override
//			public Class<?> initRules(CodeMapping t) {
//				if (null != mapType)
//					addRule(t.getMapType(), WhereRuleOper.eq, mapType);
//				addRule(t.getTargetClass(), WhereRuleOper.eq, StringHelper.getClassName(leftClass));
//				addRule(t.getSourceClass(), WhereRuleOper.eq, StringHelper.getClassName(rightClass));
////				setRowCount(10000);
//				return null;
//			}
//		});
//
//		if (rvo.getRecords() > 0) {
//			Map<String, Method> leftSetters = ReflectUtil.getAllSetters(leftClass);
//			CodeMapper fieldMapper = direction==MapDirection_Forward?new CodeMapper(leftClass, rightClass):new CodeMapper(rightClass,leftClass);
//			for (String leftField : leftSetters.keySet()) {
//				//进行值映射的时候，因为只能拿到来源的code，所以sourceCode是key且其必须唯一
//				Map<String, String> codeMap = (direction==MapDirection_Forward?
//						 rvo.obtainMap("sourceCode", "targetCode", "targetField", leftField)
//						:rvo.obtainMap("targetCode", "sourceCode", "sourceField", leftField));
//				if (codeMap != null && codeMap.size() > 0) {
//					CodeMapItem cmi = fieldMapper.new CodeMapItem();
//					cmi.setCodeMap(codeMap);
//					fieldMapper.putCodeMapItem(leftField, cmi);
//				}
//			}
//			return fieldMapper;
//		} else {
//			return null;
//		}
//
//	}

//	public void setCodeMapper(final CodeMapper codeMapper) {
//		CodeMapperMap.put(ReflectUtil.hashCodeWithOrder(codeMapper.getLeftClass(), codeMapper.getRightClass(), null), codeMapper);
//		CodeMapperMap.put(ReflectUtil.hashCodeWithOrder(codeMapper.getRightClass(), codeMapper.getLeftClass(), null), revertCodeMapper(codeMapper));
//	}
	
	/**
	 * 用于映射插件注入 Remex2-ext-rsqlReflect
	 * @param reflectContextFactory 映射上下文工厂
	 * TODO 怎么使用
	 */
	public void setReflectContextFactory(
			ReflectContextFactory reflectContextFactory) {
		ReflectConfigure.reflectContextFactory = reflectContextFactory;
		ReflectConfigure.autoFetchFromPlugin = null!=ReflectConfigure.reflectContextFactory;
	}

	/**
	 * 用于spring 中配置属性映射
	 * @param fieldMapper 
	 * TODO 怎么配置
	 */
	@Deprecated
	public static void setFieldMapper(final FieldMapper fieldMapper) {
		FieldMapperMap.put(ReflectUtil.hashCodeWithOrder(fieldMapper.getLeftClass(), fieldMapper.getRightClass()), fieldMapper);
		Map<String, String> fieldMap = fieldMapper.obtainFieldMap();

		FieldMapper revertFieldMapper = new FieldMapper(fieldMapper.getRightClass(), fieldMapper.getLeftClass()); // 因为反转，所以此行的参数right、left要注意方向
		Map<String, String> revertfieldMap = new HashMap<String, String>();
		for (String field : fieldMap.keySet()) {
			revertfieldMap.put(fieldMap.get(field), field);
		}

		revertFieldMapper.setFieldMap(revertfieldMap);

		FieldMapperMap.put(ReflectUtil.hashCodeWithOrder(fieldMapper.getRightClass(), fieldMapper.getLeftClass()), revertFieldMapper);
	}

//	/** 初始化数据库字段映射，暂时不使用 */
//	public static void init() {
//		// fieldMapper =
//		// FieldMapperMap.put(ReflectUtil.hashCode(fieldMapper.getLeftClass(),
//		// fieldMapper.getRightClass()), fieldMapper);
//	}

	/**
	 * 清除映射缓存<br>
	 * 1.清除属性映射Map<br>2.清除代码映射Map<br>3.清除反射上下文Map
	 * @call RemexApplication.init
	 */
	public static void clearCache() {
		FieldMapperMap = new HashMap<String, FieldMapper>();
		CodeMapperMap = new HashMap<String, CodeMapper>();
		ReflectContextMap = new HashMap<String, ReflectContext>();
	}
	
	/**
	 * 获取 映射上下文
	 * @param target
	 * @param source
	 * @param fieldMapType
	 * @param valueMapType
	 * @return ReflectContext 映射上下文
	 * TODO
	 */
	static ReflectContext obtainReflectContext(Class<?> target,Class<?> source,String fieldMapType, String valueMapType){
		String hashcode = ReflectUtil.hashCodeWithOrder(target, source, fieldMapType,valueMapType);
		ReflectContext  reflectContext = ReflectContextMap.get(hashcode);
		if (reflectContext == null) { // 缓存为空，查询数据库
			if(autoFetchFromPlugin){
				reflectContext = reflectContextFactory.obtainReflectContext(target, source, fieldMapType, valueMapType);
				ReflectContextMap.put(hashcode, reflectContext);
			}else{
				reflectContext = NullReflectContext;
			}
		}
		return reflectContext;
	}
}
