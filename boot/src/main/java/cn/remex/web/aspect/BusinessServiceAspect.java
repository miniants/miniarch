package cn.remex.web.aspect;

import cn.remex.RemexConstants;
import cn.remex.contrib.auth.AuthenticateBtx;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.Param;
import cn.remex.core.util.ThreadHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.db.model.SysStatus;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.sql.Sort;
import cn.remex.web.service.BusinessService;
import cn.remex.web.service.appbeans.AsyncCvo;
import cn.remex.web.service.appbeans.AsyncRvo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

import static cn.remex.db.rsql.RsqlConstants.SysStatusEnum.*;
import static cn.remex.db.sql.WhereRuleOper.eq;
import static cn.remex.db.sql.WhereRuleOper.ne;
import static cn.remex.web.service.BusinessService.ServiceType.AsyncService;

@Aspect
public class BusinessServiceAspect {

	@Around(value = "@annotation(cn.remex.web.service.BusinessService) && @annotation(bsAnno)")
	public Object doAroundBusinessService(final ProceedingJoinPoint pjp, BusinessService bsAnno) throws Throwable {
		if(bsAnno.needAuth()){
			AuthUser curUser = AuthenticateBtx.obtainCurUser();
			Assert.isTrue(curUser!=null && !Judgment.nullOrBlank(curUser.getId()), ServiceCode.ACCOUNT_NOT_AUTH,"用户未登录");
		}

		if (AsyncService.equals(bsAnno.type())) {
			return handleService(pjp, bsAnno);
		}else {
			return pjp.proceed();
		}

	}


	private Object handleService(final ProceedingJoinPoint pjp, BusinessService bsAnno) throws Throwable {
		Param<AsyncCvo> cvoParam = new Param<>(null);
		Arrays.asList(pjp.getArgs()).stream().filter(
				arg->null!=arg && arg instanceof AsyncCvo
		).findFirst().ifPresent(cvo->cvoParam.param=(AsyncCvo) cvo);
		AsyncCvo cvo = cvoParam.param;
		Assert.notNull(cvo, ServiceCode.BS_ERROR,"AsyncService 必须使用AsyncCvo!");
		Assert.notNull(cvo.getAsyncKey(),ServiceCode.BS_ERROR,"异步服务必须传asyncKey");

		Object ret = pjp.proceed();
		Assert.isTrue(ret!=null && ret instanceof AsyncRvo,ServiceCode.BS_ERROR, "AsyncService 必须返回AsyncRvo");
		AsyncRvo rvo = (AsyncRvo) ret;

		SysStatus sysStatus = ContainerFactory.createDbCvo(SysStatus.class).filterBy(SysStatus::getKey, eq, cvo.getAsyncKey())
				.filterBy(SysStatus::getStatus, ne, Finish)
				.filterBy(SysStatus::getStatus, ne, Fail)
				.orderBy(SysStatus::getModifyTime, Sort.DESC)
				.ready().queryBean();
		sysStatus = null==sysStatus?new SysStatus():sysStatus;
		sysStatus.setKey(cvo.getAsyncKey());
		rvo.setAsyncStatus(sysStatus);

		AsyncRvo retRvo = rvo.getProcessFunction().apply(rvo);
		if (null== sysStatus.getStatus() || Init.equals(sysStatus.getStatus()) //首次执行
				||( (Finish.equals(sysStatus.getStatus()) || Fail.equals(sysStatus.getStatus())) && cvo.getAsyncRestart() )//重新执行
				) {
			Assert.notNull(rvo.getStartFunction(),ServiceCode.BS_ERROR, "异步服务必须 通过asyncRvo.process(asyncRvo -> {//do you work} 注入更新逻辑");

			//注入更新状态的逻辑，从AsyncRvo中分离此逻辑有利于后期的扩展
			rvo._update(c-> ThreadHelper.run(()-> ContainerFactory.getSession().store(c.getAsyncStatus())));
			//启动异步任务，如果需要JOB模式仅需要在次修改
			ThreadHelper.run(()->{
				try{
					rvo.getStartFunction().apply(rvo);
				}catch (Throwable t){
					RemexConstants.logger.error("异步服务出现错误：",t);
					rvo.update(Fail, rvo.getAsyncStatus().getProgressRate(), t.toString());
				}

			});
			sysStatus.setStatus(Doing);
			ContainerFactory.getSession().store(sysStatus);
		}else if(Finish.equals(sysStatus.getStatus()) && rvo.getSuccessFunction() !=null){
			retRvo = rvo.getSuccessFunction().apply(rvo);
			sysStatus.setStatus(Finish);
			ContainerFactory.getSession().store(sysStatus);
		}

		return retRvo;
	}

}
