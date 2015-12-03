package cn.remex.core.quartz;

import org.quartz.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.*;

/**
 * @author liuhengyang
 * @date 2014-10-14 上午11:58:38
 * @version 版本号码
 * @在原来车辆查询批量任务的基础上进行的修订
 */
/**
 * @author liuhengyang
 * @date 2014-10-14 下午3:54:32
 * @version 版本号码
 * @TODO 描述
 */
public class SchedulerTask {

	@SuppressWarnings("rawtypes")
	private Class className = null;
	private String cronExpression = null;
	private SchedulerJobDate endDateInfo = null;// 当设置了当前Job需要有结束时间时用
	private int intervalTime = 0;// min or sec <60
	/** 定义轮询类型 day month year week */
	private String intervalType = "day";
	private String jobName = "";
	// 扫描文件夹类型
//	private String scanDir = "";
	private Calendar startDate = Calendar.getInstance();
	private String triggerName = "";
	protected Map<String, Object> jobParams = new HashMap<String, Object>();

	/**
	 * 默认为每天0时启动一次。
	 * 
	 * @param className
	 */
	public SchedulerTask(Class<?> className) {
		this.className = className;
	}

	/**
	 * @param className
	 * @param cronExpression cron 表达式
	 * 
	 * 
	 * 根据指定的cronExpression生成CronTrigger 执行任务
	 * <pre>
	 * Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，Cron有如下两种语法格式：
	 * Seconds Minutes Hours DayofMonth Month DayofWeek Year或 
	 * Seconds Minutes Hours DayofMonth Month DayofWeek
	 * 每一个域可出现的字符如下： 
	 * Seconds:可出现", - * /"四个字符，有效范围为0-59的整数 
	 * Minutes:可出现", - * /"四个字符，有效范围为0-59的整数 
	 * Hours:可出现", - * /"四个字符，有效范围为0-23的整数 
	 * DayofMonth:可出现", - * / ? L W C"八个字符，有效范围为0-31的整数 
	 * Month:可出现", - * /"四个字符，有效范围为1-12的整数或JAN-DEc 
	 * DayofWeek:可出现", - * / ? L C #"四个字符，有效范围为1-7的整数或SUN-SAT两个范围。1表示星期天，2表示星期一， 依次类推 
	 * Year:可出现", - * /"四个字符，有效范围为1970-2099年
	 * 每一个域都使用数字，但还可以出现如下特殊字符，它们的含义是： 
	 * (1)*：表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。
	 * (2)?:只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。 
	 * (3)-:表示范围，例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次 
	 * (4)/：表示起始时间开始触发，然后每隔固定时间触发一次，例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次. 
	 * (5),:表示列出枚举值值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。 
	 * (6)L:表示最后，只能出现在DayofWeek和DayofMonth域，如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。 
	 * (7)W:表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。例如：在 DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。如果5日是星期天，则在6日(周一)触发；如果5日在星期一到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份 
	 * (8)LW:这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。 
	 * (9)#:用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。
	 * 举几个例子: 
	 * 0 0 2 1 * ? * 表示在每月的1日的凌晨2点调度任务 
	 * 0 15 10 ? * MON-FRI 表示周一到周五每天上午10：15执行作业 
	 * 0 15 10 ? 6L 2002-2006 表示2002-2006年的每个月的最后一个星期五上午10:15执行作
	 * 
	 * 一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。 
	 * 按顺序依次为 
	 * 秒（0~59） 
	 * 分钟（0~59） 
	 * 小时（0~23） 
	 * 天（月）（0~31，但是你需要考虑你月的天数） 
	 * 月（0~11） 
	 * 天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT） 
	 * 年份（1970－2099）
	 * 其中每个元素可以是一个值(如6),一个连续区间(9-12),一个间隔时间(8-18/4)(/表示每隔4小时),一个列表(1,3,5),通配符。由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置?
	 * 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点 
	 * 0 0/30 9-17 * * ? 朝九晚五工作时间内每半小时 
	 * 0 0 12 ? * WED 表示每个星期三中午12点 
	 * "0 0 12 * * ?" 每天中午12点触发 
	 * "0 15 10 ? * *" 每天上午10:15触发 
	 * "0 15 10 * * ?" 每天上午10:15触发 
	 * "0 15 10 * * ? *" 每天上午10:15触发 
	 * "0 15 10 * * ? 2005" 2005年的每天上午10:15触发 
	 * "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 
	 * "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发 
	 * "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 
	 * "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发 
	 * "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发 
	 * "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发 
	 * "0 15 10 15 * ?" 每月15日上午10:15触发 
	 * "0 15 10 L * ?" 每月最后一日的上午10:15触发 
	 * "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 
	 * "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发 
	 * "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
	 * 
	 * 有些子表达式能包含一些范围或列表
	 * 例如：子表达式（天（星期））可以为 “MON-FRI”，“MON，WED，FRI”，“MON-WED,SAT”
	 * “*”字符代表所有可能的值
	 * 因此，“*”在子表达式（月）里表示每个月的含义，“*”在子表达式（天（星期））表示星期的每一天
	 * 
	 * “/”字符用来指定数值的增量 
	 * 例如：在子表达式（分钟）里的“0/15”表示从第0分钟开始，每15分钟 
	 * 在子表达式（分钟）里的“3/20”表示从第3分钟开始，每20分钟（它和“3，23，43”）的含义一样
	 * 
	 * “？”字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值 
	 * 当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“？”
	 * “L” 字符仅被用于天（月）和天（星期）两个子表达式，它是单词“last”的缩写 
	 * 但是它在两个子表达式里的含义是不同的。 
	 * 在天（月）子表达式中，“L”表示一个月的最后一天 
	 * 在天（星期）自表达式中，“L”表示一个星期的最后一天，也就是SAT
	 * 如果在“L”前有具体的内容，它就具有其他的含义了
	 * 例如：“6L”表示这个月的倒数第６天，“FRIL”表示这个月的最一个星期五 
	 * 注意：在使用“L”参数时，不要指定列表或范围，因为这会导致问题
	 * 字段 允许值 允许的特殊字符 
	 * 秒 0-59 , - * / 
	 * 分 0-59 , - * / 
	 * 小时 0-23 , - * / 
	 * 日期 1-31 , - * ? / L W C 
	 * 月份 1-12 或者 JAN-DEC , - * / 
	 * 星期 1-7 或者 SUN-SAT , - * ? / L C # 
	 * 年（可选） 留空, 1970-2099 , - * /
	 * </pre>
	 * 

	 */
	public SchedulerTask(Class<?> className, String cronExpression) {
		this.className = className;
		this.cronExpression = cronExpression;
	}
	public SchedulerTask(Class<?> className, Date startDate) {
		this.className = className;
		this.startDate.setTime(startDate);
	}

	/**
	 * @param className
	 * @param jobName
	 * @param jobID
	 * @param triggerName
	 * @param intervalType 支持day/week/month/once/cron。sec/min尚不稳定。
	 * @param startDate  
	 */
	public SchedulerTask(Class<?> className, String jobName, String triggerName, String intervalType, Date startDate) {
		this.className = className;
		this.jobName = jobName;
		this.triggerName = triggerName;
		this.intervalType = intervalType;
		this.startDate.setTime(startDate);
	}
	/**
	 * @param className
	 * @param jobName
	 * @param jobID
	 * @param triggerName
	 * @param cronExpression
	 * @see SchedulerTask#SchedulerTask(Class, String)
	 */
	public SchedulerTask(Class<?> className, String jobName, String triggerName, String cronExpression) {
		this.className = className;
		this.jobName = jobName;
		this.triggerName = triggerName;
		this.intervalType = "cron";
		this.cronExpression = cronExpression;
	}

	@SuppressWarnings("rawtypes")
	public Class getClassName() {
		return className;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public SchedulerJobDate getEndDateInfo() {
		return endDateInfo;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public String getIntervalType() {
		return intervalType;
	}

	public String getJobName() {
		return jobName;
	}

	public Map<?, ?> getJobParams(JobExecutionContext cxt) {
		return (Map<?, ?>) cxt.getJobDetail().getJobDataMap();
	}

	public String getTriggerName() {
		return triggerName;
	}

	public JobDetail obtainJobDetail(String group) {

		JobDetail detail = new JobDetail(jobName, group, className);
		JobDataMap dataMap = detail.getJobDataMap();
//		dataMap.put("scanDir", scanDir);
//		dataMap.put("JOBID", jobID);
		dataMap.put("JOBNAME", jobName);
		dataMap.putAll(jobParams);
		return detail;
	}
	public void putJobParams(String key,Object param){
		jobParams.put(key, param);
	}

	public Trigger obtainTrigger(String group) {
		Trigger trigger = null;
		int year = startDate.get(Calendar.YEAR);
		int month = startDate.get(Calendar.MONTH);
		int day = startDate.get(Calendar.DAY_OF_MONTH);
		int weekDay = startDate.get(Calendar.DAY_OF_WEEK);
		int hour = startDate.get(Calendar.HOUR_OF_DAY);
		int minute = startDate.get(Calendar.MINUTE);

		if ("day".equalsIgnoreCase(intervalType)) {
			trigger = TriggerUtils.makeDailyTrigger(triggerName, hour, minute);
		} else if ("week".equalsIgnoreCase(intervalType)) {
			trigger = TriggerUtils.makeWeeklyTrigger(triggerName, weekDay, hour, minute);
		} else if ("month".equalsIgnoreCase(intervalType)) {
			trigger = TriggerUtils.makeMonthlyTrigger(triggerName, day, hour, minute);
		} else if ("once".equalsIgnoreCase(intervalType)) { // 下一秒立即执行一次。
			startDate.set(Calendar.SECOND, +1);
			trigger = new SimpleTrigger(triggerName, group, startDate.getTime(),null,0,0L);
		} else if ("sec".equalsIgnoreCase(intervalType) || "min".equalsIgnoreCase(intervalType)) {
			month = month - 1;
			Calendar cal = new GregorianCalendar(year, month, day, hour, minute);
			if (cal.getTimeInMillis() < System.currentTimeMillis()) {
				return null;
			}
			if ("sec".equalsIgnoreCase(intervalType)) {
				trigger = TriggerUtils.makeSecondlyTrigger(intervalTime);
			} else {
				trigger = TriggerUtils.makeMinutelyTrigger(intervalTime);
			}
			trigger.setName(triggerName);
			trigger.setStartTime(cal.getTime());
			// set the job end time

			if (endDateInfo != null) {
				year = endDateInfo.getYear();
				month = endDateInfo.getMonth() - 1;
				day = endDateInfo.getDay();
				hour = endDateInfo.getHour();
				minute = endDateInfo.getMinute();
				Calendar cal2 = new GregorianCalendar(year, month, day, hour, minute);
				trigger.setEndTime(cal2.getTime());
				// System.out.println(trigger.getEndTime());
			}

		} else if ("cron".equals(intervalType)) {
			trigger = new CronTrigger();
			trigger.setName(triggerName);
			try {
				((CronTrigger) trigger).setCronExpression(this.cronExpression); // 每天指定小时和分钟整点启动服务
			} catch (ParseException e) {
				throw new QuartzException("创建day类型，出发小时内，每30秒启动一次", e);
			}
		} else {
			throw new QuartzException("必须指定任务的类型，支持day/week/month/once/sec/min");
		}
		return trigger;
	}

	public void setClassName(@SuppressWarnings("rawtypes") Class className) {
		this.className = className;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void setEndDateInfo(SchedulerJobDate endDateInfo) {
		this.endDateInfo = endDateInfo;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

}
