/* 
* 文 件 名 : XmlHelper.java
* CopyRright (c) since 2013: 
* 文件编号： 
* 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
* 日    期： 2013-6-17
* 修 改 人： 
* 日   期： 
* 描   述： 
* 版 本 号： 1.0
*/ 

package cn.remex.core.util;

import cn.remex.core.exception.JaxbException;
import cn.remex.core.reflect.ReflectUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-17
 *
 */
public final class XmlHelper {
	final static private Map<Class<?>, JAXBContext> JAXBCONTEXTS = new HashMap<Class<?>, JAXBContext>();
//	private static final String xmlPrex = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
	private static String obtainXmlPrex(String sourceCharset){
		String charsetStr = null == sourceCharset ? "UTF-8" : sourceCharset;
		return "<?xml version=\"1.0\" encoding=\""+charsetStr+"\" ?>\n";
	}
	private static HashMap<Class<?>, XStream> xStreamMap = new HashMap<Class<?>, XStream>();
	
	static private JAXBContext getJAXBContext(Class<?> clazz){
		if(JAXBCONTEXTS.containsKey(clazz))
			return JAXBCONTEXTS.get(clazz);
		else{
			try {
				JAXBContext curJaxbContext = JAXBContext.newInstance(clazz);
				JAXBCONTEXTS.put(clazz, curJaxbContext);
				return curJaxbContext;
			} catch (JAXBException e) {
				throw new JaxbException("无法生成JaxbContext实例，目标类为："+clazz.getName(), e);
			}
		}
	}
	
	private static XStream getXStream(Class<?> c){
		XStream xStream = xStreamMap.get(c);
		if(null == xStream){
			xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_","_")));
			xStream.processAnnotations(c);
			xStreamMap.put(c,xStream);
		}
		return xStream;
	}
	static private <T> String useJaxbMarshall(Class<T> clazz, Object XMLObject,String orgnCharset){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBContext curJaxbContext = getJAXBContext(clazz);
		try {
			Marshaller marshaller = curJaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);// 省略头信息
			marshaller.setProperty(Marshaller.JAXB_ENCODING, orgnCharset);// 编码方式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,	Boolean.TRUE);// 是否格式化生成的xml串 生成的XML是否具有模式结构
			// 如果要在控制台打印则将参数2 换为System.out
			marshaller.marshal(XMLObject, baos);
			return baos.toString(orgnCharset);
		} catch (Exception e) {
			throw new JaxbException("解组报文失败,异常原因是：", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	static private <T> T useJaxbUnmarshall(Class<T> clazz, String responseXml, String orgnCharset){  
		JAXBContext curJaxbContext = getJAXBContext(clazz); 
		T ret = null;
		try {
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
					responseXml.getBytes(orgnCharset));
			Unmarshaller curUnmarshaller = curJaxbContext.createUnmarshaller();
			Marshaller marshaller = curJaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);// 省略头信息
			marshaller.setProperty(Marshaller.JAXB_ENCODING, orgnCharset);// 编码方式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,	Boolean.FALSE);// 是否格式化生成的xml串 生成的XML是否具有模式结构

			ret = (T) curUnmarshaller.unmarshal(tInputStringStream);
			tInputStringStream.close();
			// 在注册解组器时候出现了异常 视为本次询价失败 系统内部原因造成
		} catch (Exception e) {
			throw new JaxbException("解组报文失败，报文内容是："+responseXml+",异常原因是：", e);
		}
		return ret;
	}

	public static Map<String,Object> maplizeObject(final Object o) {
		return maplizeObject(o,null);
	}
	
	/**
	 * 序列化对象为Map
	 * 类型为TBase
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

		Map<String, Method> baseGetters = ReflectUtil.getAllGetters(clazz);
		boolean preFlag = pre==null?false:true;
		for(String fieldName:baseGetters.keySet()){
			Method getter = baseGetters.get(fieldName);
			Object value = ReflectUtil.invokeMethod(getter, o);
			if(null!=value){
				ReflectUtil.isSimpleType(value.getClass());
				mapFromObject.put(preFlag?pre+StringHelper.upperFirstLetter(fieldName):fieldName,value);
			}
		}

		return mapFromObject;
	}
	public static <T> T unmarshall(Class<T>c,String xml){
		return unmarshall(c, xml,"UTF-8");
	}
	@SuppressWarnings("unchecked")
	public static <T> T unmarshall(Class<T>c,String xml, String sourceCharset){
		if(c.isAnnotationPresent(XmlRootElement.class)){
			return useJaxbUnmarshall(c, xml,sourceCharset);
		}else{
			XStream xStream = getXStream(c);
			Assert.notNullAndEmpty(xml, "XStream不能将null字符串或者empty字符串根据xml标准转化为对象。");
			return (T) xStream.fromXML(xml);
		}
		
	}
	public static String marshall(Object object){
		return marshall(object,"UTF-8");
	}
	
	public static String marshall(Object object,String sourceCharset){
		if(object.getClass().isAnnotationPresent(XmlRootElement.class)){
			return useJaxbMarshall(object.getClass(), object, sourceCharset);
		}else{
			XStream xStream = getXStream(object.getClass());
			return new StringBuffer().append(obtainXmlPrex(sourceCharset)).append(xStream.toXML(object)).toString();
		}
		
	}

}
