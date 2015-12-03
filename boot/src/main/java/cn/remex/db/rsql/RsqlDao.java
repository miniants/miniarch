/**
 * @author yangyang
 * 
 * 目前该版本的DAO仅支持MSSql
 * 
 * 
 */
package cn.remex.db.rsql;

import cn.remex.core.RemexApplication;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.RsqlDBExecuteException;
import cn.remex.db.rsql.connection.RDBConnection;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.SqlBean;
import cn.remex.db.sql.SqlBeanNamedParam;
import oracle.jdbc.internal.OraclePreparedStatement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.List;

public class RsqlDao {

	private static RsqlDao rsqlDao;

	/**
	 * 默认的公共构造器。
	 * @return RsqlDao
	 * @rmx.call {@link RsqlContainer#createCall(String)}
	 * @rmx.call {@link RsqlContainer#execute(String, java.util.HashMap)}
	 * @rmx.call {@link RsqlContainer#executeQuery(String, java.util.HashMap)}
	 * @rmx.call {@link RsqlContainer#query(Class)}
	 * @rmx.call {@link RsqlContainer#query(DbCvo)}
	 * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
	 */
	public static RsqlDao getDefaultRemexDao() {
		if (null == rsqlDao) {
			rsqlDao = (RsqlDao) RemexApplication.getBean(RsqlDao.class,"singleton");//此处由spring创建bean，如果开启了切面可以对他进行监控。
		}
		return rsqlDao;
	}

	/**
	 * 公共接口 此接口是为了执行DLL语句设计的。
	 * @rmx.summary 可能存在的问题：preparedstatement 的关闭以前如果报出异常，则无法处理preparedStatment没有关闭而引起的游标溢出问题
	 * @param dbCvo
	 * @return DbRvo
	 * @rmx.call {@link RsqlContainer#execute(String, java.util.HashMap)}
	 */
	public <T extends Modelable> DbRvo execute(DbCvo<T> dbCvo) {
		RsqlRvo rsqlRvo = new RsqlRvo(dbCvo);

		SqlBean<T> sqlBean = dbCvo.getSqlBean();
		// SqlOption sqlOption = sqlBean.getSqlOption(); DDL操作暂时无需选项

		// 数据库条件准备检查
		RsqlAssert.isOkRsqlCvo(dbCvo);

		/* 进入数据库操作 */
		RDBConnection con = null;
		PreparedStatement pstmt = null;
		// 获得数据库连接
		con = RDBManager.getLocalConnection(dbCvo.getSpaceName());
		RsqlAssert.isOkRDBConnection(dbCvo, con);

		List<SqlBeanNamedParam> sqlNamedParams = sqlBean.getNamedParams();
		String sqlString = sqlBean.getSqlString();
		rsqlRvo.appendMsg("In table [ ").append(sqlBean.getBeanName()).append(" ]");
		try {
			// 自动提交设置为False为回滚准备
			// con.setAutoCommit(false); 默认已是

			// 预储存语句
			pstmt = con.prepareStatement(sqlString);
			// 设置参数
			for (SqlBeanNamedParam sqlNamedParam : sqlNamedParams) {
				if (-1 != sqlNamedParam.getIndex())
					pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
			}

			// 执行数据库操作
			long time = System.currentTimeMillis();
			pstmt.execute();
			rsqlRvo.appendMsg("Execute took:[").append(System.currentTimeMillis() - time).append("ms]");

			// 清除参数关闭与储存
			pstmt.clearParameters();
			// 操作成功后，预储存过程必须关闭,如果关闭前出错，必须后处理关闭工作
			pstmt.close();
			// 如果一切正常则设置ＯＫ状态
			rsqlRvo.setMsg(true, "数据库操作[execute]数据库操作成功！");
			rsqlRvo.setStatus(true);
		} catch (SQLException e) {
			// sqlRvo.setMsg(false, "数据库操作[execute]出现异常:", e.toString(),
			// "。回滚数据！");
			// 无论有任何失败后，预储存也必须关闭
			StringBuilder msg = new StringBuilder("数据库执行Sql过程中发生异常：\n");
			try {
				pstmt.close();
				msg.append("异常后处理PreparedStatement.close()已完成。");
			} catch (Exception e1) {
				msg.append("异常后处理PreparedStatement.close()也发生异常。");
			}
			/**
			 * 重新抛出，spring或Aop配置的RsqlAspect配置事务将捕获此异常， 并调用@{link
			 * RsqlTransactionalManager#abortTransactional()};处理rollback等事务。
			 */
			throw new RsqlDBExecuteException(sqlString, sqlNamedParams.toString(), msg.toString(), e);
		} finally {
			// 必须清除参数
			dbCvo.clear();
			RDBManager.freeLocalConnection(con);
		}
		/* 数据库操作结束 */

		// 返回操作状态
		return rsqlRvo;
	}

	/**
	 * 公共接口 此接口是为了执行Query语句设计的
	 * @param dbCvo
	 * @throws Exception
	 * @rmx.call {@link RsqlContainer#query(Class)}
	 * @rmx.call {@link RsqlContainer#executeQuery(String, java.util.HashMap)}
	 * @rmx.call {@link RsqlContainer#query(DbCvo)}
	 * @return RsqlRvo
	 */
	public <T extends Modelable> RsqlRvo executeQuery(final DbCvo<T> dbCvo) {
		RsqlRvo rsqlRvo = new RsqlRvo(dbCvo);

		SqlBean<T> sqlBean = dbCvo.getSqlBean();
		// SqlOption sqlOption = sqlBean.getSqlOption();

		// 数据库条件准备检查
		RsqlAssert.isOkRsqlCvo(dbCvo);

		/* 进入数据库操作 */
		RDBConnection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 获得数据库连接
		con = RDBManager.getLocalConnection(dbCvo.getSpaceName());
		RsqlAssert.isOkRDBConnection(dbCvo, con);
		String sqlString = sqlBean.getSqlString();
		List<SqlBeanNamedParam> sqlNamedParams = sqlBean.getNamedParams();
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
		long time;
		rsqlRvo.appendMsg("In table [ ").append(sqlBean.getBeanName()).append(" ]");
		try {
			// 自动提交设置为False为回滚准备
			// con.setAutoCommit(false); 默认已是
			sqlString = sqlString + " " + RsqlUtils.obtainSQLOrder(dbCvo);
			
			String orgSqlString = sqlString; //统计所有记录数时必须使用没有加入分页参数的sql
			int pagination = dbCvo.getPagination();
			int rowCount = dbCvo.getRowCount();
			
			/******************** 执行查询*******************************/
			time = System.currentTimeMillis();
			if(dbCvo.isDoPaging()){
				int startId = rowCount * (pagination - 1);
				int endId = rowCount + startId;
				sqlString = dialect.obtainPagingSQL(sqlString, startId, endId, rowCount);
			}
			
			pstmt = con.prepareStatement(dialect.prepareSqlForCount(sqlString));

			// 设置参数
			for (SqlBeanNamedParam sqlNamedParam : sqlNamedParams) {
				if (-1 != sqlNamedParam.getIndex())
					pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
			}

			// rs查询
			rs = pstmt.executeQuery();
			rsqlRvo.appendMsg("doQuery took:[").append(System.currentTimeMillis() - time).append("ms];");

			/******************** 保存数据*******************************/
			// 构造并保存结果集合
			time = System.currentTimeMillis();
			rsqlRvo.saveData(rs, dbCvo);
			// 关闭ResultSet
			rs.close();
			// 清除参数关闭与储存
			pstmt.clearParameters();
			pstmt.close();
			
			/******************** 第一次查询或者茶到最后一页时查询总条数*******************************/
			//此模块必须放在查询之后，以适应mysql中 SELECT SQL_CALC_FOUND_ROWS SELECT FOUND_ROWS() AS count;
			int recordCount = dbCvo.getRecordCount();
			if(dbCvo.isDoCount()){
				if(recordCount==0 || pagination==1 ||pagination*rowCount>recordCount){
					time = System.currentTimeMillis();
					pstmt = con.prepareStatement(dialect.obtainCountSql(orgSqlString));
					
					if(dialect.needSetParamForCount()){
						// 设置参数
						for (SqlBeanNamedParam sqlNamedParam : sqlNamedParams) {
							if (-1 != sqlNamedParam.getIndex())
								pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
						}
					}
					
					rs = pstmt.executeQuery();
					if(rs.next())
						rsqlRvo.setRecordCount(rs.getInt(1));
					else
						rsqlRvo.setRecordCount(0);
					rs.close();
					pstmt.clearParameters();
					pstmt.close();
					rsqlRvo.appendMsg("doCount took:[").append(System.currentTimeMillis() - time).append("ms];");
				}else{
					rsqlRvo.setRecordCount(recordCount);
					rsqlRvo.appendMsg("useCount ").append(recordCount).append(" ;");
				}
			}
			
			rsqlRvo.appendMsg("Read Data Records [").append(rsqlRvo.getRows()!=null?rsqlRvo.getRows().size():0).append("], took:[").append(System.currentTimeMillis() - time).append("ms];");
			rsqlRvo.setMsg(true, "数据库操作[executeQuery]数据库操作成功！");
			rsqlRvo.setStatus(true);
		} catch (SQLException e) {
			// 无论有任何失败后，ResultSet也必须关闭
			StringBuilder msg = new StringBuilder("数据库执行Sql过程中发生异常：");
			try {
				if (rs != null) {
					rs.close();
					msg.append("异常后处理ResultSet.close()已完成。");
				}
			} catch (Exception e1) {
				msg.append("异常后处理ResultSet.close()也发生异常。");
			}
			// 无论有任何失败后，预储存也必须关闭
			try {
				pstmt.close();
				msg.append("异常后处理PreparedStatement.close()已完成。");
			} catch (Exception e1) {
				msg.append("异常后处理PreparedStatement.close()也发生异常。");
			}
			/**
			 * 重新抛出，spring或Aop配置的RsqlAspect配置事务将捕获此异常， 并调用@{link
			 * RsqlTransactionalManager#abortTransactional()};处理rollback等事务。
			 */
			rsqlRvo.setMsg(false, "数据库操作[executeQuery]出现异常:"+ e.toString());
			throw new RsqlDBExecuteException(sqlString, sqlNamedParams.toString(), msg.toString(), e);
		} finally {
			// 必须清除参数
			dbCvo.clear();
			RDBManager.freeLocalConnection(con);
		}
		/* 数据库操作结束 */

		// 返回操作状态
		return rsqlRvo;
	}

	/**
	 * 公共接口
	 * 可能存在的问题：preparedstatement 的关闭以前如果报出异常，则无法处理preparedStatment没有关闭而引起的游标溢出问题
	 * @param dbCvo
	 * @throws Exception
	 * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
	 * @rmx.call {@link RsqlContainer#executeUpdate(String, java.util.HashMap)}
	 * @return RsqlRvo
	 */
	public <T extends Modelable> RsqlRvo executeUpdate(DbCvo<T> dbCvo) {
		RsqlRvo rsqlRvo = new RsqlRvo(dbCvo);

		SqlBean<T> sqlBean = dbCvo.getSqlBean();
		// SqlOption sqlOption = sqlBean.getSqlOption();

		// 数据库条件准备检查
		RsqlAssert.isOkRsqlCvo(dbCvo);

		/* 进入数据库操作 */
		RDBConnection con = null;
		PreparedStatement pstmt = null;// , pstmt2 = null, pstmt3 = null;
		int ri = 0;
		String id = dbCvo.getId();
		ResultSet rs = null;
		// 获得数据库连接
		con = RDBManager.getLocalConnection(dbCvo.getSpaceName());
		RsqlAssert.isOkRDBConnection(dbCvo, con);
		String sqlString = sqlBean.getSqlString();
		List<SqlBeanNamedParam> sqlNamedParams = sqlBean.getNamedParams();
		rsqlRvo.appendMsg("In table [ ").append(sqlBean.getBeanName()).append(" ]");

		// 所有处理全部转化成复杂处理
		try {
			// 自动提交设置为False为回滚准备
			// con.setAutoCommit(false); 默认已是

			// RDBSpaceConfig sc = con.getSpaceConfig();
			pstmt = con.prepareStatement(sqlString);
			// , Statement.RETURN_GENERATED_KEYS
			// 设置参数

			for (SqlBeanNamedParam sqlNamedParam : sqlNamedParams) {
				int t = sqlNamedParam.getType();
				if (-1 != sqlNamedParam.getIndex())
					if (t == Types.CLOB && pstmt instanceof OraclePreparedStatement) {
						((OraclePreparedStatement) pstmt).setStringForClob(sqlNamedParam.getIndex(), String.valueOf(sqlNamedParam.getValue()));
						// pstmt.setNull(sqlNamedParam.getIndex(),Types.CLOB);
					} else if(t == Types.JAVA_OBJECT){
						if(null == sqlNamedParam.getValue()){
							pstmt.setBytes(sqlNamedParam.getIndex(),null);
						}else{
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(bos);
							oos.writeObject(sqlNamedParam.getValue());
							bos.toByteArray();
							
							pstmt.setBytes(sqlNamedParam.getIndex(), bos.toByteArray());
							oos.close();
							bos.close();
							
						}
					} else {
						pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType() == Types.BOOLEAN ? Types.VARCHAR : sqlNamedParam.getType());
					}
			}
			// String ids = null;
			long time = System.currentTimeMillis();

			// 执行
			ri = pstmt.executeUpdate();
			// // 获取id
			// if (sqlString.substring(0, 6).toUpperCase().startsWith("INSERT"))
			// {
			// rs = pstmt.getGeneratedKeys();
			// rs.next();
			// id = rs.getLong(PN_id);
			// }

			rsqlRvo.appendMsg("Execute took:[").append(System.currentTimeMillis() - time).append("ms];");

			pstmt.clearParameters();
			pstmt.close();
			rsqlRvo.setEffectRowCount(ri);
			rsqlRvo.setId(id);
			rsqlRvo.setMsg(true, "数据库操作[executeUpdate "+sqlString.substring(0, 5)+"]成功! EffectRowCount is " + ri);
			rsqlRvo.setStatus(true);

			// 更新操作應該清楚緩存數據
			// RDBManager.getLocalSpaceConfig().cacheIt(sqlBean.getClass(),rsqlCvo.getBean());
		} catch (SQLException e) {
			// 无论有任何失败后，ResultSet也必须关闭
			StringBuilder msg = new StringBuilder("数据库执行Sql过程中发生异常：\n");
			try {
				if (rs != null) {
					rs.close();
					msg.append("异常后处理ResultSet.close()已完成。");
				}
			} catch (Exception e1) {
				msg.append("异常后处理ResultSet.close()也发生异常。");
			}
			// 无论有任何失败后，预储存也必须关闭
			try {
				if (null != pstmt) {
					pstmt.close();
					msg.append("异常后处理PreparedStatement.close()已完成。");
				}
			} catch (Exception e1) {
				msg.append("异常后处理PreparedStatement.close()也发生异常。");
			}
			/**
			 * 重新抛出，spring或Aop配置的RsqlAspect配置事务将捕获此异常， 并调用@{link
			 * RsqlTransactionalManager#abortTransactional()};处理rollback等事务。
			 */
			rsqlRvo.setMsg(false, "数据库操作[executeQuery]出现异常:"+ e.toString());
			throw new RsqlDBExecuteException(sqlString, sqlNamedParams.toString(), msg.toString(), e);
		} catch (IOException e) {
			StringBuilder msg = new StringBuilder("数据库数据流化异常：\n");
			rsqlRvo.setMsg(false, "数据库操作[executeUpdate]出现异常:"+ e.toString());
			throw new RsqlDBExecuteException(sqlString, sqlNamedParams.toString(), msg.toString(), e);
		} finally {
			// 必须清除参数
			dbCvo.clear();
			RDBManager.freeLocalConnection(con);
		}
		/* 数据库操作结束 */

		// 返回操作状态
		return rsqlRvo;
	}

	/**
	 * @param sqlString
	 * @param poolName
	 * @return RsqlRvo
	 * @rmx.call {@link RsqlContainer#createCall(String)}
	 */
	public RsqlRvo createCall(final String sqlString, final String poolName) {
		// RsqlRvo rsqlRvo = new RsqlRvo(rsqlCvo);
		// // rsqlRvo.initUpdateResult();
		//
		// SqlBean sqlBean = rsqlCvo.getSqlBean();
		//
		// // 数据库条件准备检查
		// RsqlAssert.isOkRsqlCvo(rsqlCvo, sqlBean);
		//
		// /* 进入数据库操作 */
		RDBConnection con = null;
		// PreparedStatement pstmt = null;// , pstmt2 = null, pstmt3 = null;
		// // CallableStatement cstmt= null;
		// // 获得数据库连接
		con = RDBManager.getLocalConnection(poolName);
		// RsqlAssert.isOkRDBConnection(rsqlCvo, con);
		// String sqlString = sqlBean.getSqlString();
		// //创建存储过程
		// try {
		// pstmt = con.prepareStatement(sqlString);
		// pstmt.executeUpdate();
		// pstmt.clearParameters();
		// pstmt.close();
		// rsqlRvo.setMsg(true, "数据库操作[executeUpdate]成功! ");
		// rsqlRvo.setOK(true);
		//
		// } catch (SQLException e2) {
		// throw new RsqlDBExecuteException(""+e2);
		// }

		RsqlRvo rsqlRvo = new RsqlRvo();
		// 调用存储过程
		try {

			Statement cstmt = con.createStatement();
			long time = System.currentTimeMillis();
			cstmt.executeUpdate(sqlString);
			// cstmt.clearParameters();
			cstmt.close();
			rsqlRvo.appendMsg("Execute took:[").append(System.currentTimeMillis() - time).append("ms]");
		} catch (SQLException e2) {
			throw new RsqlDBExecuteException("" + e2);
		}finally {
			RDBManager.freeLocalConnection(con);
		}
		// 返回操作状态
		return rsqlRvo;
	}

}
