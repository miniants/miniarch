package cn.remex.db;

import cn.remex.core.RemexApplication;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;


/**
 * 2016-05-01 LHY 重构
 *
 * 本方法为数据操作的入口工厂方法<br>
 *
 * Remex2 框架中数据由两部分构成<br>
 *     1.管理连接和事务的{@link RDBManager}<br>
 *     2.管理SQL和ORM的{@link Container}<br>
 *
 *
 */
public class ContainerFactory {

	public static Container getSession() {
		return getSession(RDBManager.DEFAULT_SPACE);
	}

	public static Container getSession(String spaceName) {
		Class<? extends Container> containerClass = RDBManager.getLocalSpaceConfig(spaceName).getContainerClass();
		Container container = RemexApplication.getBean(containerClass);
		container.setSpaceName(spaceName);
		return container;
	}

	public static  <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass){
		return createDbCvo(beanClass, RDBManager.DEFAULT_SPACE);
	}
	public static  <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass, String spaceName){
		DbCvo<T> dbCvo = getSession(spaceName).createDbCvo(beanClass);
		return dbCvo;
	}

}