/**
 * 
 */
package cn.remex.db.rsql.connection;

import cn.remex.db.exception.RsqlDBInitException;
import cn.remex.db.rsql.RsqlConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 每个实例代表了一个数据库连接池
 */
public	class RDBConnectionPool {

	/**
	 * 当前本数据库连接池中的数据库连接数，包括池中的和正被程序使用的所有数据库连接对象
	 */
	private int curConnCount = 0;
	private int curConnCountInPool = 0;
	private Vector<Connection> freeConnections = new Vector<Connection>();
	private int maxConn;
	private String name;

	private String password;

	private RDBSpaceConfig spaceConfig;

	private String URL;

	private String user;

	ThreadLocal<RDBConnection> localConnection = new ThreadLocal<RDBConnection>();

	/**
	 * @param spaceConfig
	 */
	public RDBConnectionPool(final RDBSpaceConfig spaceConfig) {
		this.spaceConfig = spaceConfig;
		this.name = spaceConfig.getSpace();
		this.URL = spaceConfig.getUrl();
		this.user = spaceConfig.getUsername();
		this.password = spaceConfig.getPassword();
		this.maxConn = spaceConfig.getMaxconn();
	}

	/**
	 * 创建一个新的连接池
	 * @param name
	 *            连接池的名字
	 * @param type
	 *           mssql,mysql,oracle
	 * @param URL
	 *            数据库的JDBC URL
	 * @param user
	 *            数据库的用户名，或者为null
	 * @param password
	 *            数据库的用户口令，或者为null
	 * @param maxConn
	 *            最大的连接数，如果为0则没有限制
	 */
	RDBConnectionPool(final String name, final String type, final String URL, final String user,
			final String password, final int maxConn) {
		this.spaceConfig = new RDBSpaceConfig(name, type, URL, user, password, maxConn);
		this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
	}

	/**
	 * 从连接池中得到一个连接，如果当前没有空闲的连接，且没有达到最大， 则创建一个新的。
	 * @rmx.call {@link RDBConnectionPool#getConnection()}
	 */
	public synchronized Connection getConnection() {
		Connection con = null;

		if (this.freeConnections.size() > 0) {
			//池中有链接
			con = this.freeConnections.firstElement();
			this.freeConnections.removeElementAt(0);
			this.curConnCountInPool--;
			try {
				if (con.isClosed()) {
					RsqlConstants.logger.warn("在调用连接时发现数据库连接已关闭，从 〔"+this.name+"〕 中删除掉一个已关闭的数据库连接：");
					// 继续递归调用
					con = getConnection();
				}
			} catch (SQLException e) {
				RsqlConstants.logger.warn("在调用连接时发现数据库连接错误，从 〔"+this.name+"〕中删除掉一个错误的数据库连接：");
				this.curConnCount--;
				// 继续递归调用
				con = getConnection();
			} catch (NullPointerException e) {
				RsqlConstants.logger.warn("在调用连接时发现数据库连接null错误，从 〔"+this.name+"〕中删除掉一个已错误的数据库连接：");
				this.curConnCount--;
				// 继续递归调用
				con = getConnection();
			}
		} else if (this.curConnCount < this.maxConn || this.maxConn == 0) {
			// 池中没有链接，但是未达到数据库连接总数限制，可以创建
			con = newConnection();
		} else {
			// 如果数据库连接已达到配置的最大数，则通过延时获取
			try {
				synchronized (this) {
					RsqlConstants.logger.warn(this.name+"已达到最大连接数，当前进入等待。");
					wait();
					con = getConnection();
				}
			} catch (InterruptedException e) {
				RsqlConstants.logger.error(this.name+"等待数据库连接出错",e);
			}
		}

		//日志
		if(con != null) {
			if(RsqlConstants.logger.isDebugEnabled())RsqlConstants.logger.debug(this.name+"为程序提供一个数据库连接。当前Con总数和池存Con数分别为："+this.curConnCount+", "+this.curConnCountInPool);
		} else {
			RsqlConstants.logger.warn(this.name+"为程序提供一个数据库连接null错误。当前Con总数和池存Con数分别为："+this.curConnCount+", "+this.curConnCountInPool);
		}

		//返回
		return con;
	}

	public RDBConnection getLocalConnection() {
		return this.localConnection.get();
	}

	public RDBSpaceConfig getSpaceConfig() {
		return this.spaceConfig;
	}

	public void removeLocalConnection() {
		this.localConnection.remove();
	}


	public void setLocalConnection(final RDBConnection localConnectionStack) {
		this.localConnection.set(localConnectionStack);
	}

	/**
	 * 创建一个新的连接，如果可能，使用指定的userid和password
	 */
	private Connection newConnection() {
		Connection con = null;
		try {
			if (this.user == null) {
				con = DriverManager.getConnection(this.URL);
			} else {
				con = DriverManager.getConnection(this.URL, this.user, this.password);
			}

			// 如果数据库连接不为空，则表示已经成功生成一个数据库连接对象，总数+1
			this.curConnCount++;
			RsqlConstants.logger.info(this.name+"中成功地【创建】了一个新的数据库连接。当前Con总数为："+this.curConnCount);
			//con.setAutoCommit(false); 只有事务需要进行手动提交
		} catch (SQLException e) {
//			RsqlConstants.logger.error();
			throw new RsqlDBInitException(this.name+"中无法创建一个新的数据库连接。URL:" + this.URL+"，用户名表空间："+this.user,e);
		}
		return con;
	}

	/**
	 * 从连接池中延时得到一个连接，如果当前没有空闲的连接，<br>
	 * 设计为延时1s，循环10次，如果再得不到连接就返回null
	 * @param timeout
	 *            毫秒为单位的超时的值
	 */
	//		public synchronized Connection getConnection(long timeout) {
	//			long startTime = new Date().getTime();
	//			Connection con;
	//			while ((con = getConnection()) == null) {
	//				try {
	//					wait(timeout);
	//				} catch (InterruptedException e) {
	//				}
	//				if ((new Date().getTime() - startTime) >= timeout) { // 超时
	//					return null;
	//				}
	//			}
	//			return con;
	//		}

	/**
	 * 将连接连接插入进连接池，并通知所有等待连接的其他线程。
	 * @param con
	 *            插入连接
	 * @rmx.call {@link RDBManager#freeConnection(String, Connection)}
	 */
	synchronized void freeConnection(final Connection con) {
		try {
			if(con.isClosed()){
				//如果连接关闭则抛弃，并减少计数
				this.curConnCount--;
				RsqlConstants.logger.info(this.name+"回收数据库连接时，发现连接异常，已删除该连接。当前Con总数和池存Con数分别为："+this.curConnCount+", "+this.curConnCountInPool);
				return;
			}
		} catch (SQLException e) {
			this.curConnCount--;
			RsqlConstants.logger.warn(this.name+"回收数据库连接时，发现连接异常，已删除该连接。当前Con总数和池存Con数分别为："+this.curConnCount+", "+this.curConnCountInPool);
			return;
		}
		// 将连接放在Vector对象中其他连接之后
		this.freeConnections.addElement(con);
		this.curConnCountInPool++;
		//叫醒等待
		synchronized (this) {
			notify();
		}
		if(RsqlConstants.logger.isDebugEnabled())RsqlConstants.logger.debug(this.name+"中成功地回收了一个的数据库连接。当前Con总数和池存Con数分别为："+this.curConnCount+", "+this.curConnCountInPool);
		// notifyAll();
	}

	/**
	 * 关闭所有有效的连接
	 * @rmx.call {@link RDBManager#reset(java.util.HashMap, String, boolean)}
	 */
	synchronized void release() {
		Enumeration<Connection> allConnections = this.freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection con = allConnections.nextElement();
			try {
				con.close();
				this.curConnCount--;
				this.curConnCountInPool--;
				RsqlConstants.logger.info(this.name+"中成功地【关闭】一个数据库连接。当前Con总数"+this.curConnCount);
			} catch (SQLException e) {
				RsqlConstants.logger.error(this.name+"中有无法关闭的数据苦连接。",e);
			}
		}
		this.freeConnections.removeAllElements();
	}
}
