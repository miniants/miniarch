package cn.remex.db;

import cn.remex.db.exception.RsqlDBExecuteException;
import cn.remex.db.lambdaapi.SessionPredicate;
import cn.remex.db.rsql.RsqlContainer;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.core.util.Judgment;
import cn.remex.db.rsql.model.Modelable;

/**
 *  本工厂类通过{@link ContainerFactory#getSession()}操作数据库。</li><br>
 * @rmx.summary 为注解式事务式管理或手动事务管理。如果希望使用事务，则本类最好通过spring 容器管理， 注入配置如下(以容器类为
 * {@link RsqlContainer}为例)。
 * 
 * <pre>
 * &lt;aop:aspectj-autoproxy proxy-target-class="true" /&gt;
 * &lt;bean id="RsqlTransactionalAspect" class="cn.remex.db.rsql.transactional.RsqlTransactionalAspect"/&gt;
 * &lt;bean id="containerFactory" class="cn.remex.db.ContainerFactory"
 *     scope="singleton"&gt;
 *   &lt;property name="containerClass"&gt;
 *     &lt;bean class="cn.remex.db.rsql.RsqlContainer" scope="prototype"&gt;&lt;/bean&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author Hengyang Liu
 * @since 2012-4-2
 * 
 */
public class ContainerFactory {
	private static ContainerFactory factory;

	public ContainerFactory getFactory() {
		return factory;
	}

	public void setFactory(ContainerFactory factory) {
		ContainerFactory.factory = factory;
	}

	/**
	 * 在spring中正确配置后，此返回容器实例将是注解式事务操作。<b>
	 * @rmx.summary 本方法应该在被注解{@link RsqlTransaction}
	 * 的任何方法内使用。 <br><br>
	 * <b>否则调用本方法将会抛出{@link RsqlDBExecuteException}</b>
	 * 对数据库进行增删改查等相关操作时都需要通过{@link ContainerFactory#getSession()}获取事务操作是容器{@link Container}，
	 * 进而调用Cotainer实现类的方法去操作数据库
	 * @see ContainerFactory
	 * @return Container {@link Container}
	 */
	public static Container getSession() {
		return factory.getContainer(RDBManager.DEFAULT_SPACE);
	}

	/**
	 * 指定表空间查询数据，支持跨表空间的数据库操作，方法同{@link ContainerFactory#getSession()}
	 * @param spaceName 表空间
	 * @return Container {@link Container}
	 */
	public static Container getSession(String spaceName) {
		Container container;
		if(Judgment.nullOrBlank(spaceName)){
			container = factory.getContainer(RDBManager.DEFAULT_SPACE);
			container.setSpaceName(RDBManager.DEFAULT_SPACE);
		}else{
			container = factory.getContainer(spaceName);
			container.setSpaceName(spaceName);
		}
		return container;
	}

	/**
	 * @return Container
	 */
	public Container getContainer(String spaceName){
		throw new RuntimeException("没有定义数据库工厂！");
	};

	public static  <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass){
		return getSession().createDbCvo(beanClass);
	}
	public static  <T extends Modelable> Container createSession(Class<T> beanClass,SessionPredicate<T> sessionPredicate){
		Container session = getSession();
		DbCvo<T> dbCvo = session.createDbCvo(beanClass);
		sessionPredicate.initDbCvo(dbCvo,dbCvo.createAOPBean());
		return session;
	}

}