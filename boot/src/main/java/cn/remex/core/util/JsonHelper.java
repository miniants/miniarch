package cn.remex.core.util;

import cn.remex.core.CoreSvo;
import cn.remex.core.exception.JSONException;
import cn.remex.core.reflect.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

/**
 * json处理工具类
 */
public class JsonHelper {
//初步证实当写入对象为流是，此对象线程不安全 LHY 2014-11-2
//	private static ObjectMapper mapper = new ObjectMapper();
//	static {
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
//	}
	
	private final static String LocalObjectMapper = "JsonHelper";
	private static ObjectMapper obtainLocalObjectMapper(){
		ObjectMapper mapper = (ObjectMapper) CoreSvo.valLocal(LocalObjectMapper);
		if(null == mapper){
			mapper = new ObjectMapper();
			//允许不用双引号将fieldName括起来。
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
			mapper.configure(Feature.ALLOW_SINGLE_QUOTES,true);
			//不允许自动关闭输出流。
			mapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
			
			//序列化时：忽略nullbean导致的失败
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
			//序列化时：输入map中的null值
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			//序列化时：忽略空字段
			mapper.setSerializationInclusion(Include.NON_NULL);
			
			//反序列化时：忽略没有匹配上的属性
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		//反序列化时：将空字符串标示为空对象。
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			
			CoreSvo.putLocal(LocalObjectMapper, mapper);
		}
		return mapper;
	}
	
	/**
	 * 对象转化为Json字符串
	 * @param obj java对象
	 * @return String 字符串
	 */
	public static String toJsonString(Object obj){
		try {
			return obtainLocalObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new JSONException("无法将对象转化为Json字符串！", e);
		}
	}
	/**
	 * 向流中写入Json字符串
	 * @param writer 字符流
	 * @param value java对象
	 */
	public static void writJsonString(Writer writer, Object value){
		try {
			obtainLocalObjectMapper().writeValue(writer,value);
		} catch (Exception e) {
			throw new JSONException("无法向流中写入Json字符串！", e);
		}
	}
	/**
	 * 将Json字符串转化为Java对象
	 * @param jsonString json字符串
	 * @param clazz java类
	 * @return T 类
	 */
	public static  <T> T toJavaObject(String jsonString,Class<T> clazz){
		try {
			return Judgment.nullOrBlank(jsonString)?null:obtainLocalObjectMapper().readValue(jsonString, clazz);
		} catch (Exception e) {
			throw new JSONException("无法将Json字符串转化为Java对象！", e);
		}
	}
	public static  <T> T toJavaObject(String jsonString,Type clazz){
		try {
			ObjectMapper mapper = obtainLocalObjectMapper();
			JavaType javaType = mapper.getTypeFactory().constructParametricType((Class)clazz, ReflectUtil.obtainParameterClasses(clazz));
			return Judgment.nullOrBlank(jsonString)?null:obtainLocalObjectMapper().readValue(jsonString,javaType);
		} catch (Exception e) {
			throw new JSONException("无法将Json字符串转化为Java对象！", e);
		}
	}

	/**
	 * 将Json字符串转化为Java对象
	 * @param jsonString  json字符串
	 * @param clazz java类
	 * @param path 路径
	 * @return	T 类
	 *
	 */
	public static  <T> T toJavaObject(String jsonString,Class<T> clazz,String path){
		try {
			return obtainLocalObjectMapper().readValue(obtainLocalObjectMapper().readTree(jsonString).path(path).traverse(),clazz);
		} catch (Exception e) {
			throw new JSONException("无法将Json字符串转化为Java对象！", e);
		}
	}
	/**
	 * 将Json字符串转化为Java对象
	 * @param reader 字符流
	 * @param clazz java类
	 * @return T 类
	 */
	public static  <T> T toJavaObject(Reader reader,Class<T> clazz){
		try {
			return obtainLocalObjectMapper().readValue(reader, clazz);
		} catch (Exception e) {
			throw new JSONException("无法将Json字符串转化为Java对象！", e);
		}
	}
}
