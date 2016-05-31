package cn.remex.core;

import cn.remex.RemexConstants;
import cn.remex.core.cache.DataCachePool;
import cn.remex.core.reflect.ReflectConfigure;
import cn.remex.core.util.Judgment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import cn.remex.cert.CertConfiguration;
//import cn.remex.plugin.JqgUtility;

/**
 * 系统初始化、应用程序上下文数据处理 从spring配置文件中获取bean<br>
 * applicationContext.xml文件中配置
 */
public class RemexApplication implements ApplicationContextAware, ApplicationEventPublisherAware, DisposableBean, RemexConstants {
	/**
	 *用三种可能的启动方式：1.RemexFilter通过web容器加载xml配置文件调用refresh;2本地模式加载xml配置文件；<br>
	 *分别对应到本类的三个方法中：refresh();getContext();
	 */
	private static boolean isStarted = false;// 初次启动变量
	private static ApplicationContext context;// 声明一个静态变量保存

	/**
	 * 从spring配置文件中获取bean<br>
	 * applicationContext.xml文件中配置
	 * <code>&lt;bean class="cn.remex.aop.RemexDBAspect"/&gt;</code>
	 * 调用RsqlDao的数据连接事务管理。{@link }
	 * 
	 * TODO 后续补充相关内容。
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(final String name) {
		try{
			return (T) getContext().getBean(name);
		}catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}

	public static String getMapValue(final String name, final String key) {
		Map<String, String> map = getBean(name);
		return map == null ? null : map.get(key);
	}
	
	/**
	 * 从Spring的配置文件中，name="CONFIG"的map中获取配置。
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getConfig(final String name,final String defaultValue) {
			return null==getBean("CONFIG")?defaultValue:((Map<String, String>)getBean("CONFIG")).get(name);
	}
	/**
	 * 用于在调试或本地模式下初始化Remex框架。
	 */
	public static void init() {
		getContext();
		refresh();
	}
	public static ApplicationContext getContext() {
		if (null == context) {
			String defaultConfig = "applicationContext*.xml";
			context = new ClassPathXmlApplicationContext(defaultConfig);
			publishRemexStartupEvent();//相当于初次启动。所以也要发布事件
			RemexConstants.logger.info("Remex Spring模块工作在[ 本地 ]模式：" + context.getDisplayName());
		} else {
		}
		return context;
	}

	private static ApplicationEventPublisher EventPublisher;

	@Override
	public void setApplicationContext(final ApplicationContext contex) throws BeansException {
		context = contex;
	}

	/**
	 * 并不知道实例化类的具体类型，还要植入切面（创建实例）用该方法 第二个参数是bean的生命周期 默认为prototype，
	 */
	public static <T> T getBean(Class<T> clazz, String... scope) {
		return getBean(clazz, null == scope || scope.length == 0 ? "prototype" : scope[0], new Object[] {}, new String[] {});
	}

	/**
	 * 根据指定参数获取一个spring代理的bean。如果该bean没有定义，则以className为其beanName在spring上下文中注册。
	 * 
	 * @param clazz
	 * @param scope
	 * @param PropertyValues
	 * @param PropertyReferences
	 * @return
	 * 
	 * @see RemexApplication#getBean(String, Class, String, Object[], String[])
	 */
	public static <T> T getBean(Class<T> clazz, String scope, Object[] PropertyValues, String[] PropertyReferences) {
		return getBean(clazz.getName(), clazz, scope, PropertyValues, PropertyReferences);
	}

	/**
	 * 根据指定参数获取一个spring代理的bean。如果该beanName没有定义，则以对应参数在spring上下文中注册。
	 * 
	 * @param clazz
	 * @param scope
	 * @param PropertyValues
	 * @param PropertyReferences
	 * @return
	 * 
	 * @see RemexApplication#getBean(String, Class, String, Object[], String[])
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName, Class<T> clazz, String scope, Object[] PropertyValues, String[] PropertyReferences) {
		String className = beanName;
		if (getContext().containsBean(beanName)) {
			return (T) getContext().getBean(beanName);
		} else {
			registerBeanDefinition(beanName, clazz, scope, PropertyValues, PropertyReferences);
			return (T) getContext().getBean(className);
		}
	}

	/**
	 * 以className为beanName动态在spring上下文中注册一个bean模板
	 * @param clazz
	 * @param scope
	 * @param PropertyValues
	 * @param PropertyReferences
	 */
	public static <T> void registerBeanDefinitionWithClassName(Class<T> clazz, String scope, Object[] PropertyValues, String[] PropertyReferences) {
		registerBeanDefinition(clazz.getName(), clazz, scope, PropertyValues, PropertyReferences);
	}

	/**
	 * 以beanName为名字，在spring上下文中注册一个bean模板
	 * @param beanName
	 * @param clazz
	 * @param scope
	 * @param PropertyValues
	 * @param PropertyReferences
	 */
	public static <T> void registerBeanDefinition(String beanName, Class<T> clazz, String scope, Object[] PropertyValues, String[] PropertyReferences) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(clazz.getName());
		bdb.setScope(Judgment.nullOrBlank(scope) ? "prototype" : scope);
		if (null != PropertyValues) {
			for (int i = 0, l = PropertyValues.length; i < l; i = i + 2) {
				bdb.addPropertyValue((String) PropertyValues[i], PropertyValues[i + 1]);
			}
		}
		if (null != PropertyReferences) {
			for (int i = 0, l = PropertyReferences.length; i < l; i = i + 2) {
				bdb.addPropertyReference(PropertyReferences[i], PropertyReferences[i + 1]);
			}
		}
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) getContext()).getBeanFactory();
		beanFactory.registerBeanDefinition(beanName, bdb.getBeanDefinition());
	}

	static public List<RemexRefreshable> refreshables = new ArrayList<RemexRefreshable>();

	/**
	 * 清除jqgColumn，FieldMapperMap缓存
	 *
	 */
	static public void refresh() {

		for (RemexRefreshable remexRefreshable : refreshables) {
			remexRefreshable.refresh();
			logger.info("RemexRefreshable executing:" + remexRefreshable.getClass());
		}
		logger.info("RemexRefreshables刷新配置完成!");
		
		if (isStarted) { // 已经启动才刷新，初次启动不用重复刷新，节省资源
			((AbstractRefreshableConfigApplicationContext) getContext()).stop();
			((AbstractRefreshableConfigApplicationContext) getContext()).refresh();
			logger.info("刷新Spring配置完成！");
			
			ReflectConfigure.clearCache();
			logger.info("清除FieldMapperMap缓存完成！");
			
			DataCachePool.reset();
			logger.info("清除DataCachePool缓存完成！");
		}
		
		publishRemexStartupEvent();//发布初次启动的事件。
		logger.info("Remex初次启动完成!");
	}

	public void setRemexRefreshTarget(RemexRefreshable remexRefreshable) {
		refreshables.add(remexRefreshable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationEventPublisherAware#
	 * setApplicationEventPublisher
	 * (org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher paramApplicationEventPublisher) {
		EventPublisher = paramApplicationEventPublisher;
	}
	
	private static void publishRemexStartupEvent(){
		//确保事件发布不为空
		if(!getContext().containsBean(RemexApplication.class.getName())){
			EventPublisher = context;
		}
		if(!isStarted){
			//没启动才需要发布事件
			EventPublisher.publishEvent(new RemexStartupEvent("RemexStartup"));
		}
		isStarted = true;
		logger.info("Spring RemexStartup事件首次通知完成!");
	}

	public static void publishEvent(RemexEvent remexEvent) {
		EventPublisher.publishEvent(remexEvent);
	}

	@Override
	public void destroy() throws Exception {
	}
}
