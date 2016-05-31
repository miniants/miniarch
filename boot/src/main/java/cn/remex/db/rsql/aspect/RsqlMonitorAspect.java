package cn.remex.db.rsql.aspect;

import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.db.rsql.model.Modelable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RsqlMonitorAspect {

	private static boolean isDebug = RsqlConstants.logger.isDebugEnabled();
	/**
	 * 暂时没用
	 * @param pjp
	 * @return Object
	 * @throws Throwable
	 */
	@Around("execution(java.util.*List cn.remex.db.model.*.get*())"
			+ "|| execution(java.util.*Map cn.remex.db.model.*.get*())"
			+ "|| execution(java.util.*Set cn.remex.db.model.*.get*())"
			+ "|| execution(java.util.*Vector cn.remex.db.model.*.get*())")
	public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = pjp.proceed();
		return retVal;
	}

	/**
	 * 扑捉cn.remex.db.sql.RemexDao. execute(RsqlCvo) executeQuery(RsqlCvo)
	 * executeUpdate(RsqlCvo)
	 * 
	 * @param pjp
	 * @return Object
	 * @throws Throwable
	 */
	@Around("execution(public * cn.remex.db.rsql.RsqlDao.execute*(..))")
	public Object doAroundRemexSqlExecute(final ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		StringBuilder msg = new StringBuilder("RsqlDao.").append(pjp.getSignature().getName()).append("() executed [ ms ], Message:");
		DbRvo retVal = null;
		StringBuilder debugMsg = null;
		if (isDebug) {
			// Object o = pjp.getTarget();
			@SuppressWarnings("unchecked")
			DbCvo<Modelable> dbCvo = (DbCvo<Modelable>) pjp.getArgs()[0];
			String sqlString = dbCvo._getSqlString();

			StringBuilder debugMsg1 = new StringBuilder().append("\r\n——SQL语句：\r\n").append(sqlString)
					.append(RsqlUtils.obtainSQLOrder(dbCvo))// 生产是最好关掉
					.append("\r\n——Parameters：\r\n").append(dbCvo._getNamedParams().toString());
			//dbCvo._getNamedParams().forEach(sbnq->debugMsg1.append(sbnq.toString()).append("\r\n"));
			debugMsg = debugMsg1;
		}
		
		try {
			retVal = (DbRvo) pjp.proceed();
			if(isDebug) msg.append(debugMsg);
		} catch (Exception e) {
			RsqlConstants.logger.warn(msg.append("：[异常] ").append(e.toString()).insert(34, System.currentTimeMillis() - time), e);
			throw e;
		}
		
		if(isDebug){
			msg.append("\r\n——DataBase Msg：").append(retVal.getMsg())
			.insert(34,System.currentTimeMillis() - time);
			
			RsqlConstants.logger.debug(msg);
		} else {
			if (!retVal.getStatus()) {
				RsqlConstants.logger.warn(msg.append("：[异常] ").append(retVal.getMsg())
						.insert(34,System.currentTimeMillis() - time));
			}else{
				RsqlConstants.logger.info(msg.append("：").append(retVal.getMsg())	
						.insert(34,System.currentTimeMillis() - time));
			}
		}

		return retVal;
	}

}
