package cn.remex.db.rsql.aspect;

import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.connection.RDBManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class RsqlTransactionalAspect {

	public RsqlTransactionalAspect() {
		super();
		RsqlConstants.logger.info("Remex2中的切面类" + RsqlTransactionalAspect.class + "初始化完成！");
	}

	@Pointcut("@annotation(cn.remex.db.rsql.transactional.RsqlTransaction)")
	public void allRemexSqlTransactional() {
	}

	/**
	 * 这是是spring中数据库事务管理的。
	 * 
	 * @param pjp
	 * @return Object
	 * @throws Throwable
	 */
	@Around("allRemexSqlTransactional()")
	public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
		try {
			RDBManager.beginTransactional(RDBManager.DEFAULT_SPACE);
			Object retVal = pjp.proceed();
			RDBManager.finishTransactional(RDBManager.DEFAULT_SPACE);
			return retVal;
		} catch (Throwable t) {
			RDBManager.abortTransactional(t, RDBManager.DEFAULT_SPACE);
			throw t;// abortTranscational里面一定会抛出异常，本行一定无法到达。
		}
	}

}
