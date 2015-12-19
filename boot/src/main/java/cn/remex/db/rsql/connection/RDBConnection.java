/**
 * 
 */
package cn.remex.db.rsql.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import cn.remex.db.exception.RsqlDBExecuteException;
import cn.remex.db.rsql.RsqlAssert;
import cn.remex.db.rsql.RsqlDao;

/**
 * 改写了java.sql.Connection<br>
 * 此对象是为了数据库池的自动调用，回收，并且保证回收的正确性重写的。
 */
public class RDBConnection {
	/**
	 * java.sql.Connection数据库连接
	 */
	private Connection con = null;
	/**
	 * 储存本数据库连接的池名字
	 */
	private String poolName = null;

	public String getPoolName() {
		return poolName;
	}
	private RDBSpaceConfig spaceConfig;
	private RDBConnectionPool pool;

	public RDBConnection(final String poolName) {
		this.poolName = poolName;
		this.spaceConfig = RDBManager.getSpaceConfig(poolName);
		this.pool = RDBManager.getPool(poolName);
	}
	/**
	 * 改写的java.sql.Connection.commit()
	 * @throws SQLException
	 * @rmx.call {@link RDBManager#finishTransactional(String)}
	 */
	public void commit() throws SQLException {
		getConnection().commit();
	}
	
	/**
	 * 用于向数据库池返回已使用完的连接。 此对象中的commit(),rollback(),已包含此操作
	 * @rmx.call {@link RDBManager#abortTransactional(Exception)}
	 * @rmx.call {@link RDBManager#finishTransactional(String)}
	 * @rmx.call {@link RDBManager#freeLocalConnection(RDBConnection)}
	 */
	public void free() {
		if (null != this.con) {
			this.pool.freeConnection(this.con);
			this.con = null;
		}
	}

	public RDBSpaceConfig getSpaceConfig() {
		return this.spaceConfig;
	}

	/**
	 * 用于检查数据库连接是否正常的方法
	 * @return boolean
	 * @rmx.call {@link RsqlAssert#isOkRDBConnection(cn.remex.db.DbCvo, RDBConnection)}
	 */
	public boolean isOK() {
		if (getConnection() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 改写的java.sql.Connection.prepareStatement(String sql)
	 * @param sql
	 * @return PreparedStatement
	 * @throws SQLException
	 * @rmx.call {@link RsqlDao#execute(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeQuery(cn.remex.db.DbCvo)}
	 * @rmx.call {@link RsqlDao#executeUpdate(cn.remex.db.DbCvo)}
	 */
	public PreparedStatement prepareStatement(final String sql)
			throws SQLException {
		return getConnection().prepareStatement(sql);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys autoGeneratedKeys a flag indicating whether auto-generated keys should be returned; one of Statement.RETURN_GENERATED_KEYS or Statement.NO_GENERATED_KEYS
	 * @return a new PreparedStatement object, containing the pre-compiled SQL statement, that will have the capability of returning auto-generated keys
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(final String sql,
			final int autoGeneratedKeys)
					throws SQLException {
		return getConnection().prepareStatement(sql, autoGeneratedKeys);
	}

	/**
	 * 改写的java.sql.Connection.prepareStatement(sql, resultSetType, resultSetConcurrency)
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(final String sql,
			final int resultSetType, final int resultSetConcurrency)
					throws SQLException {
		return getConnection().prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	/**
	 * @param sqlString
	 * @param obtainPSKeys
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(final String sqlString, final String[] obtainPSKeys) throws SQLException {
		return getConnection().prepareStatement(sqlString, obtainPSKeys);
	}

	public CallableStatement callableStatement(final String sql)
			throws SQLException {
		return getConnection().prepareCall(sql);
	}
	
	/**
	 * @return Statement
	 * @throws SQLException
	 * @rmx.call {@link RsqlDao#createCall(String, String)}
	 */
	public Statement createStatement() throws SQLException{
		return getConnection().createStatement();
	}
	

	/**
	 * 改写的java.sql.Connection.rollback()
	 * @throws SQLException
	 * @rmx.call {@link RDBManager#abortTransactional(Exception)}
	 */
	public void rollback() throws SQLException {
		getConnection().rollback();
	}

	/**
	 * 改写的java.sql.Connection.setAutoCommit(boolean)
	 * @param b
	 * @throws SQLException
	 * @rmx.call {@link RDBManager#abortTransactional(Exception)}
	 * @rmx.call {@link RDBManager#beginTransactional(String)}
	 * @rmx.call {@link RDBManager#finishTransactional(String)}
	 */
	public void setAutoCommit(final boolean b) {
		try {
			getConnection().setAutoCommit(b);
		} catch (SQLException e) {
			throw new RsqlDBExecuteException("数据库设置自动提交异常！",e);
		}
	}
	private Connection getConnection(){
		if(null==this.con) {
			this.con = pool.getConnection();
		}
		try {
			//TODO 等待时间。
			while(this.con.isClosed()) {
				this.con = pool.getConnection();
			}
		} catch (SQLException e) {
			throw new RsqlDBExecuteException("判断数据库是否关闭时发生异常！",e);
		}
		
		return this.con;
	}
}
