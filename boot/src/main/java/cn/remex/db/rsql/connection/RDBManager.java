/**
 * 
 */
package cn.remex.db.rsql.connection;

import cn.remex.core.CoreSvo;
import cn.remex.core.exception.NestedException;
import cn.remex.core.util.Assert;
import cn.remex.core.util.PackageUtil;
import cn.remex.db.exception.RsqlDBExecuteException;
import cn.remex.db.exception.RsqlDBInitException;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.RsqlDao;
import cn.remex.db.rsql.model.Modelable;

import java.lang.ref.SoftReference;
import java.lang.reflect.Modifier;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * 
 * 
 * @author yangyang
 * @version v3.0
 * 
 */
public class RDBManager {
	public static String DEFAULT_SPACE =  "default";

	/**
	 * 数据库池管理实例，唯一静态实例
	 */
	static private RDBManager instance;
	private static final String isRsqlTransactionFlag = "cn.remex.db.rsql.transactional.RsqlTransactionalManager.isRsqlTransactionFlag";

	private static ThreadLocal<Integer> TransactionalLevel = new ThreadLocal<Integer>();

	static public void abortTransactional(final Throwable ex,final String poolName) {
		RDBConnectionPool pool = RDBManager.getPool(poolName);

		RDBConnection con = pool.getLocalConnection();
		try {
			if(con!=null){
				con.rollback();
				con.setAutoCommit(true);
			}
			
			if (RsqlConstants.isDebug)
				RsqlConstants.logger.info("数据库事务回滚（无论事务是否嵌套，整个事务完全回滚）！当前层级:" + TransactionalLevel.get() + ",数据库池：" + poolName);

			if(ex instanceof RsqlException) {
				throw (RsqlException)ex;
			} else if(ex instanceof NestedException){
				throw (NestedException)ex;
			}else if(ex instanceof RuntimeException){
				throw (RuntimeException)ex;
			}else{
				throw new RuntimeException(ex);
			}
		} catch (SQLException e) {
			throw new RsqlDBExecuteException("数据库操作失败，同时回滚操作也失败！"+e.toString(), ex);
		} finally {
			if(null!=con)
				con.free();
			pool.removeLocalConnection();
			CoreSvo.$SL(isRsqlTransactionFlag, false);
		}
	}
	/**
	 * 初始化本线程的数据库连接栈。<br>
	 * @rmx.summary Remex的数据库管理采用同一个线程共享一个真实数据库连接({@link Connection}封装于{@link RDBConnection}
	 * @rmx.call {@link RsqlTransactionalAspect}
	 * @rmx.call {@link RDBManager#commitTransactional(String)}
	 */
	static public void beginTransactional(final String spaceName) {
		RDBConnectionPool pool = RDBManager.getPool(spaceName);
		// 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
		RDBConnection localCon = pool.getLocalConnection();

		if (null == localCon) {
			TransactionalLevel.set(1);
			
			localCon = new RDBConnection(spaceName);
			pool.setLocalConnection(localCon);
		} else {
			TransactionalLevel.set(TransactionalLevel.get() + 1);
		}

		if (RsqlConstants.isDebug)
			RsqlConstants.logger.info("数据库事务开始，当前层级:" + TransactionalLevel.get() + ",数据库池：" + spaceName);

		//因为第一层事务已经处理，此两行可忽略
		localCon.setAutoCommit(false);
		CoreSvo.$SL(isRsqlTransactionFlag, true);
	}

	static public void commitTransactional(String spaceName){
		finishTransactional(spaceName);
		beginTransactional(spaceName);
	}
	/**
	 * 正常完成事务。<br>
	 * @rmx.summary 由于事务存在嵌套，但本模块在同一线程中式是同一个数据库连接，提交必须是在数据库连接栈中最后一个时操作。
	 * @rmx.call {@link RDBManager#commitTransactional(String)}
	 * @rmx.call {@link RsqlTransactionalAspect}
	 */
	static public void finishTransactional(final String spaceName) {
		if(RsqlConstants.isDebug)
			RsqlConstants.logger.info("正尝试提交事务(层级等于1才真正提交)，当前层级:"+TransactionalLevel.get()+",数据库池："+spaceName);

		RDBConnectionPool pool = RDBManager.getPool(spaceName);
		// 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
		RDBConnection localCon = pool.getLocalConnection();

		boolean hasLocalCon=null!=localCon;
		int level = TransactionalLevel.get();
		
		try{
			if (hasLocalCon && level==1) {
				localCon.commit();
				localCon.setAutoCommit(true);
				localCon.free();
				pool.removeLocalConnection();
			}
		}catch(Exception e){
			throw new RsqlDBExecuteException("提交事务失败",e) ;
		}finally{
			if(level==1){
				TransactionalLevel.set(1);
				CoreSvo.$SL(isRsqlTransactionFlag, false);
			}else{
				TransactionalLevel.set(level>1?--level:1);
			}
		}
	}
	
	/**
	 * @param con
	 * @rmx.call {@link RsqlDao#createCall(String, String)}
	 * @rmx.call {@link RsqlDao#execute(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeUpdate(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeQuery(cn.remex.db.DbCvo)}
	 */
	public static void freeLocalConnection(RDBConnection con) {
		if(!isRsqlTransaction()){
			con.free();
		}
	}

	/**
	 * @param poolName
	 * @return RDBConnection
	 * @rmx.call {@link RsqlDao#createCall(String, String)}
	 * @rmx.call {@link RsqlDao#execute(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeUpdate(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeQuery(cn.remex.db.DbCvo)}
	 */
	public static RDBConnection getLocalConnection(final String poolName) {
		if(isRsqlTransaction()){
			RDBConnectionPool pool = RDBManager.getPool(poolName);
			// 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
			RDBConnection localCon = pool.getLocalConnection();

			Assert.notNull(localCon,"初始化事务时没有创建数据库连接，请联系系统管理员！");
			
			return localCon;
		}else{
			return new RDBConnection(poolName);
		}

	}
	public static RDBSpaceConfig getLocalSpaceConfig() {
		return instance.pools.get(DEFAULT_SPACE).getSpaceConfig();
	}
	public static RDBSpaceConfig getLocalSpaceConfig(String spaceName) {
		return instance.pools.get(spaceName).getSpaceConfig();
	}
	public static RDBSpaceConfig getSpaceConfig(final String poolName) {
		return instance.pools.get(poolName).getSpaceConfig();
	}

	/**
	 * @rmx.call {@link RsqlCore#reset(boolean)}
	 * @rmx.call {@link RsqlCore#RsqlCore(String, HashMap, boolean)}
	 */
	static public synchronized void reset(
			HashMap<String, RDBSpaceConfig> RDBSpaceMap, String drivers,
			boolean rebuildDB) {
		if(null!=instance){
			destroy();
		}
		instance = new RDBManager(RDBSpaceMap, drivers, rebuildDB);
	}
	
	/**
	 * 注销jdbc驱动，
	 * 释放空间中的数据库连接
	 */
	static public synchronized void destroy(){
		for (RDBConnectionPool pool : instance.pools.values()) {
			pool.release();
		}
		Enumeration<Driver> allDrivers = instance.drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				RsqlConstants.logger.info("注销JDBC驱动：" + driver.getClass().getName());
			} catch (SQLException e) {
				RsqlConstants.logger.error("无法注销JDBC驱动: " + driver.getClass().getName(), e);
			}
		}
	}
	
	private static boolean isRsqlTransaction(){
		return null == CoreSvo.$VL(isRsqlTransactionFlag) ? false:((Boolean)CoreSvo.$VL(isRsqlTransactionFlag));
	}

	protected static RDBConnectionPool getPool(final String poolName) {
		return instance.pools.get(poolName);
	}

	/**
	 * 数据库连接驱动容器
	 */
	private Vector<Driver> drivers = new Vector<Driver>();

	/**
	 * 数据库池哈希表
	 */
	private HashMap<String, RDBConnectionPool> pools = new HashMap<String, RDBConnectionPool>();
	/**
	 * 构造器，初始化各 管理池.
	 * 只允许初始化一次。
	 * 
	 */

	private RDBManager(HashMap<String, RDBSpaceConfig> RDBSpaceMap,
			String drivers, boolean rebuildDB) {
		loadDrivers(drivers);
		createPools(RDBSpaceMap);
		createSpace(rebuildDB);
	}
	/**
	 * 基于属性创建DBConnectionPool的实例 
	 * @rmx.summary DBConnectionPool可以使用下面的属性：<br>
	 * poolname.url 数据库的JDBC URL poolname.user 数据库用户(可选) poolname.password<br>
	 * 数据库用户的口令(如果着顶用户) poolname.maxconn 连接的最大数目(可选)<br>
	 * @param RDBSpaceMap 
	 * 
	 * @param props
	 *            数据库连接池的属性
	 *  @rmx.call {@link RDBManager}
	 */
	private void createPools(HashMap<String, RDBSpaceConfig> RDBSpaceMap) {
		for(String poolName : RDBSpaceMap.keySet()) {
			RDBSpaceConfig spaceConfig = RDBSpaceMap.get(poolName);
			RDBConnectionPool pool = new RDBConnectionPool(spaceConfig);
			this.pools.put(poolName, pool);
		}
	}


	/**
	 * 
	 */
	private void createSpace(boolean refreshDB) {
		
		for (RDBConnectionPool pool : this.pools.values()) {
			RDBSpaceConfig spaceConfig = pool.getSpaceConfig();
			Map<String, Class<?>> ormBeans = new HashMap<String, Class<?>>();
			RsqlConstants.logger.info("数据库空间【"+spaceConfig.getSpace()+"】初始化中,cn.remex下属所有的包都默认添加到ORMBeans中...");
			
			List<String> list = new ArrayList<String>();
			list.addAll(spaceConfig.getOrmBeanPackages());
			list.add("cn.remex");
			for(String pkg:list){
				Set<Class<?>> orbs = PackageUtil.getClasses(pkg);
				for(Class<?> c:orbs){
					String cn = c.getSimpleName();
					String fn = c.getName();
					
					if(Modelable.class.isAssignableFrom(c))
						if(ormBeans.get(cn)!=null) {
							RsqlConstants.logger.error("ORMBeans 的包中有重复类简名的类，因数据库建表表明无法重复，这种情况是不允许的！,此类将被忽略，其名为"+fn);
						} else if(fn.indexOf('$')>0) {
							RsqlConstants.logger.warn("ORMBeans 的包中有inner类，不建议在数据模型类中使用这种类型并持久化！您依然可以使用该类编程，但数据库建模中将忽略此类，其名为"+fn);
						}else if(Modifier.isAbstract(c.getModifiers())) {
							RsqlConstants.logger.info("在数据库建模package中发现一个抽象类"+fn+"将被忽略!");
						} else if(c.isInterface()) {
							RsqlConstants.logger.info("在数据库建模package中发现一个接口"+fn+"将被忽略!");
						} else {
							ormBeans.put(cn, c);
							//构建bean缓存
							spaceConfig.getDbBeanPool().put(c, new HashMap<Object, SoftReference<Object>>(15));
						}
//							RsqlConstants.logger.warn("在数据库建模package中发现一个没有实现Modelable接口的模型类"+fn+",他将被忽略!");
				}
			}
			spaceConfig.setOrmBeans(ormBeans);
			
			//建表或刷新表
			try{
				if(refreshDB){
//					System.out.print("重构数据库工作无法回滚,请输入确认码 :");
//					byte[] b = new byte [10] ;
//					System.in.read(b );
//					if (new String(b).trim().equals("YES, I DO!")) {
						// RsqlCore.initDBSystemTable(spaceConfig);
						RsqlCore.refreshORMBaseTables(spaceConfig);
						RsqlCore.refreshORMCollectionTables(spaceConfig);
						// RsqlCore.refreshORMMapTables(ormBeans);
						// 建立约束或者刷新约束
						RsqlCore.refreshORMConstraints(spaceConfig);
//					}
				}

			}catch (NestedException e) {
				throw e;
			}catch (Exception e) {
				throw new RsqlDBInitException("初始化数据库错误！", e);
			}
		}
	}
	/**
	 * 装载并登记所有的JDBC驱动程序
	 * @param props
	 *            数据库连接池的属性
	 */
	private void loadDrivers(String driversStr) {
		StringTokenizer st = new StringTokenizer(driversStr);
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
			try {
				Class<?> driverClass = Class.forName(driverClassName);
				Driver driver = null;
				Enumeration<Driver> regDrivers = DriverManager.getDrivers();
				while(regDrivers.hasMoreElements()){
					Driver thisDriver = regDrivers.nextElement();
					if(thisDriver.getClass().equals(driverClass)){
						driver=thisDriver;
						break;
					}
				}
				if(null==driver){
					driver = (Driver) driverClass.newInstance();
					DriverManager.registerDriver(driver);
				}
				this.drivers.addElement(driver);
				RsqlConstants.logger.info("注册JDBC驱动程序：" + driverClassName);
			} catch (Exception e) {
				RsqlConstants.logger.error("无法注册JDBC驱动: " + driverClassName, e);
			}
		}
	}

}
