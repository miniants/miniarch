package cn.remex.db.bs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import cn.remex.web.service.BsRvo;
@Aspect
public class DataBsAspect {

	/**
	 * @summary 数据库对外提供服务的切面，
	 * 主要拦截异常、入参相关的异常断言，将异常信息以与请求方式（xml，http，json，ajax等）
	 * 一样的方式返回异常原因给调用者以方便查找原因等
	 * Spring配置中必须在RsqlTransactionalAspectzz切面之上
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(public * cn.remex.db.bs.DataBs.*(..))")
	public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		try{
			Object retVal = pjp.proceed();
			return retVal;
		}catch (Exception e) {
			DataBs.logger.info(e.getMessage());
			BsRvo bsRvo = (BsRvo) args[1];
			bsRvo.setExtend(doException(e));
			return bsRvo;
		}
	}
	public DataResult doException(Exception e){
		DataResult dataResult = new DataResult();
		dataResult.setErrorMsg(e.getMessage());
		return dataResult;
		
	}
}
