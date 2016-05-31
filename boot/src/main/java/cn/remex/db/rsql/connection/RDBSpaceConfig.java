/*
 * 文 件 名 : RDBSpaceConfig.java
 * CopyRright (c) since 2013:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-2-24
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */

package cn.remex.db.rsql.connection;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.remex.db.Container;
import cn.remex.db.rsql.RsqlContainer;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.connection.dialect.Dialect;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-2-24
 *
 */
public class RDBSpaceConfig implements InitializingBean {
	/**
	 * 数据库缓存
	 */
	private HashMap<Class<?>, HashMap<Object, SoftReference<Object>>> dbBeanPool = new HashMap<Class<?>, HashMap<Object, SoftReference<Object>>>(128);
	private Dialect dialect;
	private Class<? extends Container> containerClass = RsqlContainer.class;
	private String spaceName;
	private String type;
	private Map<String, Class<?>> ormBeans = new HashMap<>();
	private List<String> ormBeanPackages;
	private DataSource dataSource;
	private boolean cannotRebuild;

	private RDBSpaceConfig(){
	}
	/**
	 * @param spaceName
	 * @param type： Mysql Oracle
	 */
	public RDBSpaceConfig(final String spaceName, final String type) {
		super();
		this.spaceName = spaceName;
		this.type = type;

		RDBManager.createSpace(this);
	}

	/**
	 * 清理制定ormBean名为beanName的数据库告诉缓存。
	 * @param beanName
	 */
	public void clearCache(final String beanName,final String id){
		if(beanName==null) {
			return;
		}
		Class<?> clazz = getOrmBeanClass(beanName);
		if(clazz==null) {
			return;
		}
		this.dbBeanPool.get(clazz).remove(id);
	}

	public Class<? extends Container> getContainerClass() {
		return containerClass;
	}

	public void setContainerClass(Class<Container> containerClass) {
		this.containerClass = containerClass;
	}

	//	/**
//	 * @param beanName
//	 * @param id
//	 * @return Object
//	 */
//	public Modelable getDBBean(String beanName, Object id) {
//		return getDBBean(RDBManager.getLocalSpaceConfig().getOrmBeanClass(beanName));
//	}
	public Dialect getDialect() {
		return this.dialect;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 根据传入的beanName（代理类）获取真正的beanName
	 * @return Class
	 */
	public  Class<?> getOrmBeanClass(final String beanName) {
		return null != beanName ? this.ormBeans.get(beanName
				.split("\\$\\$EnhancerByCGLIB\\$\\$")[0]) : null;
	}

	public List<String> getOrmBeanPackages() {
		return this.ormBeanPackages;
	}
	/**
	 * ormBeans参数是重要参数，是本方法返回的是其副本。
	 * @return the ormBeans
	 * @rmx.call {@link RsqlCore#refreshORMBaseTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#refreshORMCollectionTables(RDBSpaceConfig)}
	 * @rmx.call {@link RsqlCore#refreshORMConstraints(RDBSpaceConfig)}
	 */
	public Map<String, Class<?>> getOrmBeans() {
		Map<String,Class<?>> ret = new HashMap<>();
		ret.putAll(this.ormBeans);
		return ret;
	}
	public String getSpaceName() {
		return this.spaceName;
	}

	/**
	 * mssql,mysql,oracle
	 */
	public String getType() {
		return this.type;
	}
	public  boolean hasOrmBeanClass(final Type type){
		if(type instanceof Class<?>) {
			return null!=getOrmBeanClass(((Class<?>) type).getSimpleName());
		}
		return false;
	}
	public void setDialectClass(final String dialect) throws Exception {
		this.dialect = (Dialect) Class.forName(dialect).newInstance();
	}
	public void setOrmBeanPackages(final List<String> ormBeanPackages) {
		this.ormBeanPackages = ormBeanPackages;
	}
	public void setOrmBeans(final Map<String, Class<?>> ormBeans) {
		this.ormBeans = ormBeans;
	}
	public void setSpaceName(final String spaceName) {
		this.spaceName = spaceName;
	}
	/**
	 * mssql,mysql,oracle
	 */
	public void setType(final String type) {
		this.type = type;
	}
	public HashMap<Class<?>, HashMap<Object, SoftReference<Object>>> getDbBeanPool() {
		return this.dbBeanPool;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RDBManager.createSpace(this);
	}

	public boolean isCannotRebuild() {
		return cannotRebuild;
	}

	public void setCannotRebuild(boolean cannotRebuild) {
		this.cannotRebuild = cannotRebuild;
	}
}
