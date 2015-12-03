package cn.remex.core.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性映射类
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2012-12-2
 */
public class FieldMapper {
	
	public FieldMapper(){
		
	}
	
	/**
	 * 该构造函数用于Spring属性注入<br>注入点ReflectConfigure.setFieldMapper
	 * @param leftClass
	 * @param rightClass
	 */
	public FieldMapper(Class<?> leftClass, Class<?>rightClass){
		this.leftClass = leftClass;
		this.rightClass = rightClass;
	}
	
	
	private Class<?> leftClass;
	private Class<?> rightClass;
//	private Map<String, String> sourceAndTargetMap = new HashMap<String, String>();
	/**
	 * key是leftClass 的field，value是rightClass的field
	 */
	private Map<String, String> fieldMap = new HashMap<String, String>();
	public Class<?> getLeftClass() {
		return this.leftClass;
	}

	public Class<?> getRightClass() {
		return this.rightClass;
	}

	public Map<String, String> obtainFieldMap(){
		return fieldMap;
//		if((this.leftClass.equals(targetClass)||this.leftClass.isAssignableFrom(targetClass)
//				)
//				&&(this.rightClass.equals(sourceClass)||this.rightClass.isAssignableFrom(sourceClass))) {
//			return this.targetAndSourceMap;
//		} else if((this.rightClass.equals(targetClass)||this.rightClass.isAssignableFrom(targetClass)
//				)
//				&&(this.leftClass.equals(sourceClass)||this.leftClass.isAssignableFrom(sourceClass))) {
//			return this.sourceAndTargetMap;
//		} else {
//			// 程序不会到这里，如果发生了说明ReflectUtil.hashCode方法设计有问题。
//			throw new RuntimeException("自动属性映射错误,可能是发生了hashCode错误，请联系系统设计人员！targetClass"+targetClass.getName()+"sourceClass"+sourceClass.getName());
//		}
	}
	public void setFieldMap(final Map<String, String> fieldMap) {
		this.fieldMap.clear();
		this.fieldMap.putAll(fieldMap);
//		for (String key : this.targetAndSourceMap.keySet()) {
//			this.sourceAndTargetMap.put(this.targetAndSourceMap.get(key), key);
//		}
	}
	public void setLeftClass(final Class<?> leftClass) {
		this.leftClass = leftClass;
	}
	public void setRightClass(final Class<?> rightClass) {
		this.rightClass = rightClass;
	}

}