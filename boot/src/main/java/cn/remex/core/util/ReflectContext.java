package cn.remex.core.util;

import java.util.HashMap;
import java.util.Map;

public class ReflectContext {
	boolean enableFieldMap = false;
	boolean enableCodeMap = false;
	private Map<String,String> fieldMap = new HashMap<String,String>();
	private Map<String,Map<String,String>> codeMap = new HashMap<String,Map<String,String>>();
	public boolean isEnableFieldMap() {
		return enableFieldMap;
	}
	public void setEnableFieldMap(boolean enableFieldMap) {
		this.enableFieldMap = enableFieldMap;
	}
	public boolean isEnableCodeMap() {
		return enableCodeMap;
	}
	public void setEnableCodeMap(boolean enableCodeMap) {
		this.enableCodeMap = enableCodeMap;
	}
	public Map<String, String> getFieldMap() {
		return fieldMap;
	}
	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap.clear();
		this.fieldMap.putAll(fieldMap);
	}
	public Map<String, Map<String, String>> getCodeMap() {
		return codeMap;
	}
	public void setCodeMap(Map<String, Map<String, String>> codeMap) {
		this.codeMap = codeMap;
	}
	
}
