package cn.remex.core.quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.*;



/** 
 * @author liuhengyang 
 * @date 2014-10-14 上午11:58:38
 * @version 版本号码
 * @在原来车辆查询批量任务的基础上进行的修订
 */
public class SchedulerHandler {


	private static Map<String, SchedulerHandler> SchedulerHandlers = new HashMap<String, SchedulerHandler>() ;
	private static StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	public static SchedulerHandler getDefaultSchedulerHandler(){
		return getSchedulerHandler("default");
	}
	public static SchedulerHandler getSchedulerHandler(String SchedulerHandlerName){
		SchedulerHandler schedulerHandler = SchedulerHandlers.get(SchedulerHandlerName);
		if(null == schedulerHandler){
			SchedulerHandlers.put(SchedulerHandlerName, (schedulerHandler = new SchedulerHandler(SchedulerHandlerName)));
			schedulerHandler.group = SchedulerHandlerName;
		}
		return schedulerHandler;
	}

	private List<SchedulerTask> schedulerTasks;   //定时任务job队列
	private Scheduler scheduler;
	private String group = "DefaultGroup";

	private SchedulerHandler(String group) {
		super();
		try {
			this.scheduler = schedulerFactory.getScheduler();
			this.schedulerTasks = new Vector<SchedulerTask>();
			this.group = group;
		} catch (SchedulerException e) {
			throw new QuartzException("创建SchedulerHandler失败！", e);
		}
	}

	/**
	 * 新增一个任务，并按预定时间启动。
	 * 
	 * @param job
	 * @return
	 */
	public boolean addJob(SchedulerTask task) {
		if(schedulerTasks.contains(task))
			throw new QuartzException("任务已经存在，不允许重复添加！");
		
		JobDetail jobdtl = null;
		try {
			jobdtl = task.obtainJobDetail(group);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (jobdtl == null) {
			return false;
		}
		Trigger trigger = null;
		try {
			trigger = task.obtainTrigger(group);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (trigger == null) {
			return false;
		}
		try {
			scheduler.scheduleJob(jobdtl, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		
		schedulerTasks.add(task);
		return true;
	}

	/**
	 * 重新启动当前日程
	 * 
	 */
	public void againStart() {
		try {
			for (SchedulerTask temp:schedulerTasks) {
				if (!temp.getJobName().equalsIgnoreCase("RestartMinTrigger")) {
					addJob(temp);
				}
			}
			scheduler.start();
		} catch (SchedulerException e) {
			throw new QuartzException("重新启动日程失败！", e);
		}
	}

	/**
	 * 从当前计划中按指定要求停止并删除一个任务
	 * 
	 * @param task
	 * @return
	 */
	public boolean deleteJob(SchedulerTask task) {
		boolean b =false;
		try {
			// scheduler.pauseTrigger(job.getJobName(), Scheduler.DEFAULT_GROUP); //
			scheduler.pauseTrigger(task.getTriggerName(), group);
			b = scheduler.deleteJob(task.getJobName(), group);
			// System.out.println("triggername= "+job.getTriggerName());
		} catch (SchedulerException e) {
			return b;
		}finally{
			schedulerTasks.remove(task);
		}
		return b;
	}

	/**
	 * 启动按预定时间启动一次该任务
	 * @param jobClass 
	 * @param jobName 
	 * @param jobId 
	 * @param triggerName 
	 * @param date 
	 * @param task
	 * @param jobParams 必须是偶数,奇数为key String,偶数时为参数对象
	 * @return
	 */
	public boolean onceJob(Class<?> jobClass, String jobName, String triggerName, Date date,Object... jobParams){
		SchedulerTask task = new SchedulerTask(jobClass,jobName,triggerName,"once",date);
		if(null!=jobParams && 0<jobParams.length && jobParams.length%2==0)
			for(int i=0,s=jobParams.length;i<s;i=i+2){
				task.putJobParams(jobParams[i].toString(), jobParams[i+1]);
			}
		return addJob(task) && schedulerTasks.remove(task);
	}
	public void restart() {
		stop();
		againStart();
	}

	/**
	 * 启动当前日程
	 * 
	 */
	public void start() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new QuartzException("启动日程失败！",e);
		}
	}

	public void start(Collection<SchedulerTask> tasks) {
			if (tasks != null) {
				for (Iterator<SchedulerTask> iterator = tasks.iterator(); iterator.hasNext();) {
					SchedulerTask temp = iterator.next();
					addJob(temp);
				}
			}
			start();
	}
	
	
	/**
	 * 停止当前任务
	 * 
	 */
	public void stop() {
		if (scheduler != null) {
			SchedulerHandlers.remove(group);
			try {
				scheduler.shutdown(true);
			} catch (SchedulerException e) {
				throw new QuartzException("停止日程失败！",e);
			} finally {
				scheduler = null;
			}
		}
	}
}
