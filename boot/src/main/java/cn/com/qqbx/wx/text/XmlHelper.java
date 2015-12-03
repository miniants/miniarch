package cn.com.qqbx.wx.text;

/* 
* �� �� �� : XmlHelper.java
* CopyRright (c) since 2013: 
* �ļ���ţ� 
* �� �� �ˣ�Liu Hengyang Email:yangyang8599@163.com QQ:119316891
* ��    �ڣ� 2013-6-17
* �� �� �ˣ� 
* ��   �ڣ� 
* ��   ���� 
* �� �� �ţ� 1.0
*/ 


import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.SqlType.FieldType;
import cn.remex.exception.JaxbException;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.Assert;
import cn.remex.util.StringHelper;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-17
 *
 */
public final class XmlHelper {
	private static HashMap<Class<?>, XStream> xStreamMap = new HashMap<Class<?>, XStream>();
	private static final String xmlPrex = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

	public static void registerAlias(Class<?>objectClass,HashMap<String, Class<?>> aliasMap){
		XStream xStream = xStreamMap.get(objectClass);
		for(String name:aliasMap.keySet()){
			xStream.alias(name, aliasMap.get(name));
		}
	}
	
	public static XStream getXStream(Class<?> c){
		XStream xStream = xStreamMap.get(c);
		if(null == xStream){
			xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_","_")));
			xStreamMap.put(c,xStream);
		}
		return xStream;
	}
	
	public static String objectToXml(Object object){
		XStream xStream = getXStream(object.getClass());
		return new StringBuffer().append(xmlPrex).append(xStream.toXML(object)).toString();
	}
	
	public static void processAnnotations(Class<?> c){
		XStream xStream = getXStream(c);
		xStream.processAnnotations(c);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T objectFromXml(Class<T>c,String xml){
		XStream xStream = getXStream(c);
		Assert.notNullAndEmpty(xml, "XStream���ܽ�null�ַ����empty�ַ���xml��׼ת��Ϊ����");
		return (T) xStream.fromXML(xml);
	}
	
	final static private Map<Class<?>, JAXBContext> JAXBCONTEXTS = new HashMap<Class<?>, JAXBContext>();
	static public JAXBContext getJAXBContext(Class<?> clazz){
		if(JAXBCONTEXTS.containsKey(clazz))
			return JAXBCONTEXTS.get(clazz);
		else{
			try {
				JAXBContext curJaxbContext = JAXBContext.newInstance(clazz);
				JAXBCONTEXTS.put(clazz, curJaxbContext);
				return curJaxbContext;
			} catch (JAXBException e) {
				throw new JaxbException("�޷����JaxbContextʵ��Ŀ����Ϊ��"+clazz.getName(), e);
			}
		}
	}
	@SuppressWarnings("unchecked")
	static public <T> T unmarshall(Class<T> clazz, String responseXml){  
		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
				responseXml.getBytes());
		JAXBContext curJaxbContext = getJAXBContext(clazz); 
		T ret = null;
		try {
			Unmarshaller curUnmarshaller = curJaxbContext.createUnmarshaller();
			ret = (T) curUnmarshaller.unmarshal(tInputStringStream);
			tInputStringStream.close();
			// ��ע�������ʱ��������쳣 ��Ϊ����ѯ��ʧ�� ϵͳ�ڲ�ԭ�����
		} catch (Exception e) {
			throw new JaxbException("���鱨��ʧ�ܣ����������ǣ�"+responseXml+",�쳣ԭ���ǣ�", e);
		}
		return ret;
	}
	
	
	/**
	 * ���л�����ΪMap
	 * ����ΪTBase
	 * @param o
	 * @return
	 * @throws Exception
	 * 
	 */
//	public static Map<String,Object> maplizeObject(final Object o) {
//		Map<String, Object> mapFromObject = new HashMap<String, Object>();
//
//
//		Class<?> clazz = o.getClass();
//
//		Map<String, Method> baseGetters = SqlType.getGetters(clazz,FieldType.TBase);
//		for(String fieldName:baseGetters.keySet()){
//			Method getter = baseGetters.get(fieldName);
//			Object value = ReflectUtil.invokeMethod(getter, o);
//			if(null!=value)
//				mapFromObject.put(fieldName,value);
//		}
//
//		return mapFromObject;
//	}
	public static Map<String,Object> maplizeObject(final Object o,String pre) {
		Map<String, Object> mapFromObject = new HashMap<String, Object>();


		Class<?> clazz = o.getClass();

		Map<String, Method> baseGetters = SqlType.getGetters(clazz,FieldType.TBase);
		boolean preFlag = pre==null?false:true;
		for(String fieldName:baseGetters.keySet()){
			Method getter = baseGetters.get(fieldName);
			Object value = ReflectUtil.invokeMethod(getter, o);
			if(null!=value)
				mapFromObject.put(preFlag?pre+StringHelper.upperFirstLetter(fieldName):fieldName,value);
		}

		return mapFromObject;
	}
	public static Map<String,Object> maplizeObject(final Object o) {
		return maplizeObject(o,null);
	}

}
