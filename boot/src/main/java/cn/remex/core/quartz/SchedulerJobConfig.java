package cn.remex.core.quartz;


/** 
 * @author liuhengyang 
 * @date 2014-10-14 上午11:58:38
 * @version 版本号码
 * @在原来车辆查询批量任务的基础上进行的修订
 */
public class SchedulerJobConfig {
	public void setTask(SchedulerTask task){
		SchedulerHandler.getDefaultSchedulerHandler().addJob(task);
	}
	
}
