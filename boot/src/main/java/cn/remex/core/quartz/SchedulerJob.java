package cn.remex.core.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;


/** 
 * @author liuhengyang 
 * @date 2014-10-14 上午11:58:38
 * @version 版本号码
 * @在原来车辆查询批量任务的基础上进行的修订
 */
public abstract class SchedulerJob implements Job {


	
	protected Map<?, ?> mJobParams = null;
	
	@SuppressWarnings("unused")
	public void execute(JobExecutionContext cxt) throws JobExecutionException {
		mJobParams = getJobParams(cxt);
		long start = System.currentTimeMillis();
		jobExecute(cxt);
		long end = System.currentTimeMillis();
	}
	
	public Map<?, ?> getJobParams(JobExecutionContext cxt) {
		return (Map<?, ?>)cxt.getJobDetail().getJobDataMap();
	}
	
	public abstract void jobExecute(JobExecutionContext cxt) throws JobExecutionException;

}
