/**
 *
 */
package cn.remex.db.rsql.connection;

import cn.remex.core.CoreSvo;
import cn.remex.core.exception.NestedException;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.PackageUtil;
import cn.remex.db.exception.RsqlConnectionException;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.transactional.RsqlTransaction;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class RDBManager {
    // 数据库池管理实例，唯一静态实例
    static private RDBManager instance;

    public static String DEFAULT_SPACE = "default";
    private static HashMap<String, RDBSpaceConfig> spaceConfigs = new HashMap<>();
    private static final String RsqlTransactionFlag = "cn.remex.db.rsql.transactional.RsqlTransactionalManager.RsqlTransactionFlag";
    private static final String RsqlUseBatchUpdateFlag = "cn.remex.db.rsql.transactional.RsqlTransactionalManager.RsqlUseBatchUpdateFlag";
    private static final String RsqlTransactionLevelFlag = "cn.remex.db.rsql.transactional.RsqlTransactionalManager.RsqlTransactionLevelFlag";
    private static final String RsqlTransactionConnectionFlag = "cn.remex.db.rsql.transactional.RsqlTransactionalManager.RsqlTransactionConnectionFlag";

    //事务管理的功能函数
    public static boolean isTransaction(String spaceName) {
        return null == CoreSvo.valLocal(RsqlTransactionFlag + "#" + spaceName) ? false : ((Boolean) CoreSvo.valLocal(RsqlTransactionFlag + "#" + spaceName));
    }
    public static boolean isUseBatchUpdate(String spaceName) {
        Object usedBatchUpdateObj = CoreSvo.valLocal(RsqlUseBatchUpdateFlag + "#" + spaceName);
        return null != usedBatchUpdateObj && (boolean) usedBatchUpdateObj;
    }

    public static void beginTransactional(final String spaceName, RsqlTransaction rsqlTranAnno) {
        /**
         * 初始化本线程的数据库连接栈。<br>
         * @rmx.summary Remex的数据库管理采用同一个线程共享一个真实数据库连接({@link Connection}封装于{@link RDBConnection}
         * @rmx.call {@link RDBManager#commitTransactional(String)}
         */
        // 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
        RDBSpaceConfig spaceConfig = spaceConfigs.get(spaceName);
        Connection localCon = (Connection) CoreSvo.valLocal(RsqlTransactionConnectionFlag + "#" + spaceName);

        if (null == localCon) {
            CoreSvo.putLocal(RsqlTransactionLevelFlag + "#" + spaceName, 1);
            try {
                localCon = spaceConfig.getDataSource().getConnection();
            } catch (SQLException e) {
                throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库连接错误", e);
            }
            CoreSvo.putLocal(RsqlTransactionConnectionFlag + "#" + spaceName, localCon);
        } else {
            CoreSvo.putLocal(RsqlTransactionLevelFlag + "#" + spaceName, (int) CoreSvo.valLocal(RsqlTransactionLevelFlag + "#" + spaceName) + 1);
        }

        if (RsqlConstants.isDebug)
            RsqlConstants.logger.info("数据库事务开始，当前层级:" + CoreSvo.valLocal(RsqlTransactionLevelFlag + "#" + spaceName) + ",数据空间：" + spaceName);

        //因为第一层事务已经处理，此两行可忽略
        try {
            localCon.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库事务设置为非自动提交异常", e);
        }
        CoreSvo.putLocal(RsqlTransactionFlag + "#" + spaceName, true);
        if(null!=rsqlTranAnno && rsqlTranAnno.useBatchUpdate()) {
            CoreSvo.putLocal(RsqlUseBatchUpdateFlag + "#" + spaceName, rsqlTranAnno.useBatchUpdate());
        }

        //batch
        if(RDBManager.isUseBatchUpdate(spaceName)
                && null == CoreSvo.valLocal(spaceName + "#BacthPerpareStatement")){
            CoreSvo.putLocal(spaceName+"#BacthPerpareStatement",new HashMap<>());
        }
    }

    public static void finishTransactional(final String spaceName) {
        /**
         * 正常完成事务。<br>
         * @rmx.summary 由于事务存在嵌套，但本模块在同一线程中式是同一个数据库连接，提交必须是在数据库连接栈中最后一个时操作。
         * @rmx.call {@link RDBManager#commitTransactional(String)}
         */
        if (RsqlConstants.isDebug)
            RsqlConstants.logger.info("正尝试提交事务(层级等于1才真正提交)，当前层级:" + CoreSvo.valLocal(RsqlTransactionLevelFlag + "#" + spaceName) + ",数据空间：" + spaceName);

        // 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
        Connection localCon = (Connection) CoreSvo.valLocal(RsqlTransactionConnectionFlag + "#" + spaceName);

        boolean hasLocalCon = null != localCon;
        int level = (int) CoreSvo.valLocal(RsqlTransactionLevelFlag + "#" + spaceName);

        try {
            if (hasLocalCon && level == 1) {
                //批处理
                if(isUseBatchUpdate(spaceName)){
                    HashMap<Object, PreparedStatement> batchPstmtMap = (HashMap<Object, PreparedStatement>) CoreSvo.valLocal(spaceName + "#BacthPerpareStatement");
                    batchPstmtMap.forEach((sqlHashCode,preparedStatement)->{
                        try {
                            preparedStatement.executeBatch();
                        } catch (SQLException e) {
                            throw new RsqlExecuteException(ServiceCode.RSQL_UPDATE_ERROR, "批处理最后一批提交失败",e);
                        }
                    });
                    batchPstmtMap.clear();
                }

                localCon.commit();
                localCon.setAutoCommit(true);
                localCon.close();
                CoreSvo.putLocal(RsqlTransactionConnectionFlag + "#" + spaceName, null);
            }
        } catch (Exception e) {
            throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "提交事务失败", e);
        } finally {
            if (level == 1) {
                CoreSvo.putLocal(RsqlTransactionLevelFlag + "#" + spaceName, 1);
                CoreSvo.putLocal(RsqlTransactionFlag + "#" + spaceName, false);
                CoreSvo.putLocal(RsqlUseBatchUpdateFlag + "#" + spaceName, false);
            } else {
                CoreSvo.putLocal(RsqlTransactionLevelFlag + "#" + spaceName, level > 1 ? --level : 1);
            }
        }
    }

    public static void abortTransactional(final Throwable ex, final String spaceName) {
        Connection con = null;
        try {
            con = (Connection) CoreSvo.valLocal(RsqlTransactionConnectionFlag + "#" + spaceName);

            if (con != null) {
                con.rollback();
                con.setAutoCommit(true);
            }

            if (RsqlConstants.isDebug)
                RsqlConstants.logger.info("数据库事务回滚（无论事务是否嵌套，整个事务完全回滚）！当前层级:" + CoreSvo.valLocal(RsqlTransactionLevelFlag + "#" + spaceName) + ",数据库池：" + spaceName);

            if (ex instanceof RuntimeException) {
                throw (RuntimeException)ex;
            } else {
                throw new NestedException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库事务中出现非架构异常", ex);
            }
        } catch (SQLException e) {
            throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库操作失败，同时回滚操作也失败！" + e.toString(), ex);
        } finally {
            CoreSvo.putLocal(RsqlTransactionFlag + "#" + spaceName, false);
            if (null != con)
                try {
                    con.close();
                } catch (SQLException e) {
                    //throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库关闭错误", e);
                }
            CoreSvo.putLocal(RsqlTransactionConnectionFlag + "#" + spaceName, null);
        }
    }

    public static void commitTransactional(String spaceName) {
        finishTransactional(spaceName);
        beginTransactional(spaceName, null);
    }


    //数据库连接的功能函数
    public static void freeLocalConnection(String spaceName, Connection con) {
        if (!isTransaction(spaceName)) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "释放数据库连接错误", e);
            }
        }
    }

    public static Connection getLocalConnection(final String spaceName) {
        // 或的本线程的数据库链接栈。此栈是可以进行数据库事务套用管理的。本模块中一个线程只用一个数据库链接。栈的深度决定了套用的次数。
        RDBSpaceConfig pool = spaceConfigs.get(spaceName);
        if (isTransaction(spaceName)) {
            Connection localCon = (Connection) CoreSvo.valLocal(RsqlTransactionConnectionFlag + "#" + spaceName);
            Assert.notNull(localCon, ServiceCode.RSQL_CONNECTION_ERROR, "初始化事务时没有创建数据库连接，请联系系统管理员！", RsqlConnectionException.class);
            return localCon;
        } else {
            try {
                return pool.getDataSource().getConnection();
            } catch (SQLException e) {
                throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库连接错误", e);
            }
        }

    }

    //数据空间的功能函数
    static void createSpace(RDBSpaceConfig spaceConfig) {
        (null != instance ? instance : new RDBManager()).createSpace(spaceConfig, false);
    }

    public static RDBSpaceConfig getLocalSpaceConfig() {
        return spaceConfigs.get(DEFAULT_SPACE);
    }

    public static RDBSpaceConfig getLocalSpaceConfig(String spaceName) {
        return spaceConfigs.get(spaceName);
    }

    //重置和销毁
    public static synchronized void reset(boolean rebuildDB) {
        if (null != instance) {
            destroy();
        }
        instance = new RDBManager();
        for (RDBSpaceConfig spaceConfig : spaceConfigs.values()) {
            instance.createSpace(spaceConfig, rebuildDB && !spaceConfig.isCannotRebuild());
        }
    }

    public static synchronized void destroy() {
//		for (RDBConnectionPool pool : instance.spaceConfigs.values()) {
//			pool.release();
//		}
//		Enumeration<Driver> allDrivers = instance.drivers.elements();
//		while (allDrivers.hasMoreElements()) {
//			Driver driver = allDrivers.nextElement();
//			try {
//				DriverManager.deregisterDriver(driver);
//				RsqlConstants.logger.info("注销JDBC驱动：" + driver.getClass().getName());
//			} catch (SQLException e) {
//				RsqlConstants.logger.error("无法注销JDBC驱动: " + driver.getClass().getName(), e);
//			}
//		}
    }

    private RDBManager() {
    }

    private void createSpace(RDBSpaceConfig spaceConfig, boolean rebuildDB) {
        Map<String, Class<?>> ormBeans = new HashMap<>();
        RsqlConstants.logger.info("数据库空间【" + spaceConfig.getSpaceName() + "】初始化中,cn.remex下属所有的包都默认添加到ORMBeans中...");

        List<String> list = new ArrayList<>();
        list.addAll(spaceConfig.getOrmBeanPackages());
        list.add("cn.remex");
        for (String pkg : list) {
            Set<Class<?>> orbs = PackageUtil.getClasses(pkg);
            for (Class<?> c : orbs) {
                String cn = c.getSimpleName();
                String fn = c.getName();

                if (Modelable.class.isAssignableFrom(c))
                    if (ormBeans.get(cn) != null) {
                        RsqlConstants.logger.error("ORMBeans 的包中有重复类简名的类，因数据库建表表明无法重复，这种情况是不允许的！,此类将被忽略，其名为" + fn);
                    } else if (fn.indexOf('$') > 0) {
                        RsqlConstants.logger.warn("ORMBeans 的包中有inner类，不建议在数据模型类中使用这种类型并持久化！您依然可以使用该类编程，但数据库建模中将忽略此类，其名为" + fn);
                    } else if (Modifier.isAbstract(c.getModifiers())) {
                        RsqlConstants.logger.info("在数据库建模package中发现一个抽象类" + fn + "将被忽略!");
                    } else if (c.isInterface()) {
                        RsqlConstants.logger.info("在数据库建模package中发现一个接口" + fn + "将被忽略!");
                    } else {
                        ormBeans.put(cn, c);
                        //构建bean缓存
                        //spaceConfig.getDbBeanPool().put(c, new HashMap<>(15));
                    }
            }
        }
        spaceConfig.setOrmBeans(ormBeans);

        //建表或刷新表
        try {
            if (rebuildDB) {
                // RsqlCore.initDBSystemTable(spaceConfig);
                RsqlCore.refreshORMBaseTables(spaceConfig);
                RsqlCore.refreshORMCollectionTables(spaceConfig);
                // RsqlCore.refreshORMMapTables(ormBeans);
                // 建立约束或者刷新约束
                RsqlCore.refreshORMConstraints(spaceConfig);
            }

        } catch (NestedException e) {
            throw e;
        } catch (Exception e) {
            throw new RsqlException(ServiceCode.RSQL_INIT_ERROR, "初始化数据库错误！", e);
        }

        spaceConfigs.put(spaceConfig.getSpaceName(), spaceConfig);
    }
}
