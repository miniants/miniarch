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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.db.rsql.RsqlAssert;
import cn.remex.db.rsql.RsqlContainer;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-2-24
 *
 */
public class RDBSpaceConfig {
	static private ReferenceQueue<SoftReference<Object>> queue = new ReferenceQueue<SoftReference<Object>>();
	/**
	 * 数据库缓存
	 */
	private HashMap<Class<?>, HashMap<Object, SoftReference<Object>>> dbBeanPool = new HashMap<Class<?>, HashMap<Object, SoftReference<Object>>>(128);
	private Dialect dialect;
	private int maxconn;
	private List<String> ormBeanPackages;

	private Map<String, Class<?>> ormBeans = new HashMap<String, Class<?>>();
	private String password;
	private String space;

	private String type;
	private String url;
	private String username;

	public RDBSpaceConfig(){

	}
	/**
	 * @param space
	 * @param type
	 * @param url
	 * @param username
	 * @param password
	 * @param maxconn
	 */
	public RDBSpaceConfig(final String space, final String type, final String url, final String username, final String password, final int maxconn) {
		super();
		this.space = space;
		this.type = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.maxconn = maxconn;
	}
	
	/**
	 * 将类型为clazz的对象o放入数据缓存池中
	 * @param clazz
	 * @param bean
	 */
	public void cacheIt(final Class<?> clazz ,final Object bean){
		if(null == bean) {
			return;
		}
		HashMap<Object, SoftReference<Object>> pool = this.dbBeanPool.get(clazz);
		if(null == pool){
			pool = new HashMap<Object, SoftReference<Object>>();
			this.dbBeanPool.put(clazz, pool);
		}
		Object id= ReflectUtil.invokeGetter(RsqlConstants.SYS_id, bean);
		if(null!=id && !"-1".equals(id)){
			@SuppressWarnings({ "unchecked", "rawtypes" })
			SoftReference<Object> ref = new SoftReference(bean, queue);
			this.dbBeanPool.get(clazz).put(id, ref);
		}
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
	
	/**
	 * 尝试从缓存中根据id来获取bean，如果缓存没有，则
	 * 调用{@link RsqlCore#createDBBean(Class)}获取新的bean并存入缓存中。<br>
	 * 如果id 为空则返回一个新的bean。
	 * @param <T>
	 * @param clazz
	 * @return T
	 */
	@SuppressWarnings({ "unchecked"})
	public <T>T getDBBean(final Class<?> clazz){
//		Object bean = RsqlCore.createDBBean(clazz);
		return (T) RsqlCore.createDBBean(clazz);
//		
//		HashMap<Object, SoftReference<Object>> pool = this.dbBeanPool.get(clazz);
//		if(null == pool){
//			pool = new HashMap<Object, SoftReference<Object>>();
//			this.dbBeanPool.put(clazz, pool);
//		}
//
//		SoftReference<Object> ref = pool.get(id);
//		if(ref==null ){
//			Object bean = RsqlCore.createDBBean(clazz);
//			// 2013-2-24 LHY 修订 1
//			ReflectUtil.invokeSetter(SYS_id, bean, ReflectUtil.caseObject(String.class, id));
//			// 2013-2-24 LHY 修订 1 END
//
//			cacheIt(clazz, bean);
//			return (T) bean;
//		}else{
//			Object ret = ref.get();
//			if(ret==null){
//				pool.remove(id);
//				//删除id后，回调函数进入ref=null
//				return getDBBean(clazz, id);
//			}else{
//				return (T) ret;
//			}
//		}
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
	public int getMaxconn() {
		return this.maxconn;
	}
	
	/**
	 * 根据传入的beanName（代理类）获取真正的beanName
	 * @param beanName
	 * @return Class
	 * @rmx.call {@link RDBSpaceConfig#clearCache(String, String)}
	 * @rmx.call {@link RsqlContainer#copy(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlUtils#createSelectSqlBean(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlContainer#existsModel(String)}
	 * @rmx.call {@link RDBSpaceConfig#hasOrmBeanClass(Type)}
	 * @rmx.call {@link RsqlAssert#isOrmBeanName(String)}
	 * @rmx.call {@link RsqlContainer#store(Modelable, cn.remex.db.DbCvo)}
	 */
	public  Class<?> getOrmBeanClass(final String beanName) {
		return null != beanName ? this.ormBeans.get(beanName
				.split("\\$\\$EnhancerByCGLIB\\$\\$")[0]) : null;
	}

	/**
	 * @return List
	 * @rmx.call {@link RDBManager#createSpace}
	 */
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
		Map<String,Class<?>> ret = new HashMap<String, Class<?>>();
		ret.putAll(this.ormBeans);
		return ret;
	}

	public String getPassword() {
		return this.password;
	}

	public String getSpace() {
		return this.space;
	}


	/**
	 * mssql,mysql,oracle
	 */
	public String getType() {
		return this.type;
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
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

	public void setMaxconn(final int maxconn) {
		this.maxconn = maxconn;
	}

	public void setOrmBeanPackages(final List<String> ormBeanPackages) {
		this.ormBeanPackages = ormBeanPackages;
	}

	public void setOrmBeans(final Map<String, Class<?>> ormBeans) {
		this.ormBeans = ormBeans;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setSpace(final String space) {
		this.space = space;
	}

	/**
	 * mssql,mysql,oracle
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public void setUrl(final String url) {
		this.url = url;
	}
	public void setUsername(final String username) {
		this.username = username;
	}
	public HashMap<Class<?>, HashMap<Object, SoftReference<Object>>> getDbBeanPool() {
		return this.dbBeanPool;
	}
}
