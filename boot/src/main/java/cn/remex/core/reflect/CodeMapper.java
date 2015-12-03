/*
 * 文 件 名 : CodeMapper.java
 * CopyRright (c) since 2012:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2012-11-12
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */

package cn.remex.core.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码映射
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2012-11-12
 */
public class CodeMapper {
	/**
	 * @author liuhengyang
	 *这是一组代码映射。
	 *默认的映射map是sourceAndTargetMap。其中的key是sourceField(getterKey)，此key必须是改组映射中的唯一值。
	 */
	public class CodeMapItem {
		/**
		 * 
		 */
		private Map<String, String> codeMap = new HashMap<String, String>();
//		private Map<String, String> targetAndSourceMap = new HashMap<String, String>();//暂时不用

		/**
		 * 根据本mapper的值进行映射。如果没有找到映射值，则将不映射原值返回。
		 * @param value
		 * @return
		 */
		public Object obtainCode(final Object value) {
			Object reflectValue =  codeMap.get(value);
			return null==reflectValue?value:reflectValue;
		}
		
//		public Map<String, String> obtainCodeItemMap(final Class<?> targetClass, final Class<?> sourceClass) {
//			if ((leftClass.equals(targetClass) || leftClass.isAssignableFrom(targetClass)) && (rightClass.equals(sourceClass) || rightClass.isAssignableFrom(sourceClass))) {
//				return this.targetAndSourceMap;
//			} else if ((rightClass.equals(targetClass) || rightClass.isAssignableFrom(targetClass)) && (leftClass.equals(sourceClass) || leftClass.isAssignableFrom(sourceClass))) {
//				return sourceAndTargetMap;
//			} else {
//				// 程序不会到这里，如果发生了说明ReflectUtil.hashCode方法设计有问题。
//				throw new RuntimeException("自动属性映射错误,可能是发生了hashCode错误，请联系系统设计人员！targetClass" + targetClass.getName() + "sourceClass" + sourceClass.getName());
//			}
//		}

		@Override
		public String toString() {
			return new StringBuilder("\r\n属性代码映射为：").append(codeMap.toString()).append("\r\n").toString();
		}

		public void setCodeMap(final Map<String, String> codeMap) {
			this.codeMap.clear();
			this.codeMap.putAll(codeMap);
//			for (String key : this.targetAndSourceMap.keySet()) {
//				this.sourceAndTargetMap.put(this.targetAndSourceMap.get(key), key);
//			}
		}
		public Map<String,String> getCodeMap(){
			return this.codeMap;
		}

	}
	private Class<?> leftClass;
	
	private Class<?> rightClass;

	private Map<String, CodeMapItem> codeMapItems = new HashMap<String, CodeMapper.CodeMapItem>();

	public CodeMapper(){
		
	}

	public CodeMapper(Class<?> leftClass, Class<?>rightClass){
		this.leftClass = leftClass;
		this.rightClass = rightClass;
	}
	public Map<String, CodeMapItem> obtainCodeMap() {
		return codeMapItems;
	}
	public void putCodeMapItem(String fieldName,CodeMapItem cmi){
		this.codeMapItems.put(fieldName, cmi);
	}

	public Class<?> getLeftClass() {
		return leftClass;
	}

	public Class<?> getRightClass() {
		return rightClass;
	}

	public void setLeftClass(Class<?> leftClass) {
		this.leftClass = leftClass;
	}

	public void setRightClass(Class<?> rightClass) {
		this.rightClass = rightClass;
	}

	@Override
	public String toString() {
		return new StringBuilder("代码映射如下：")
		.append("leftClass:").append(leftClass)
		.append(";rightClass").append(rightClass)
		.append(";映射为：").append(codeMapItems).toString();
	}
	

}
