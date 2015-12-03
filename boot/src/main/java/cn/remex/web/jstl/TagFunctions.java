package cn.remex.web.jstl;

import cn.remex.admin.RemexAdminUtil;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.JsonHelper;

public class TagFunctions {
	public static String toJson(Object obj) {
		return JsonHelper.toJsonString(obj);
	}

	public static String invokeMethod(Object obj, String methodName, String key) {
		return String.valueOf(ReflectUtil.invokeMethod(methodName, obj, key));
	}

	public static String get(Object obj, String key) {
		return String.valueOf(ReflectUtil.invokeMethod("get", obj, key));
	}

	public static String obtainConf(String key) {
		try {
			return RemexAdminUtil.obtainConfig(key);
		} catch (Exception e) {
			return null;
		}
	}

}
