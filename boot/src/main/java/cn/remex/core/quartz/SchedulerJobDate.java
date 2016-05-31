package cn.remex.core.quartz;

import java.util.List;
import java.util.Vector;


/**
 * 
 * *****************************************************************
 * <p>Sunyard System Engineering Co.,Ltd</p>
 * <p>Sunyard Science & Technology Building NO.3888</p>
 * <p>Jiangnan Main(BinJiang District)Hangzhou,China</p>
 * <p>Copyright (c) 1996－2009 Sunyard System Engineering Co.,Ltd</p>
 * <p>Object:</p>
 * <p>Description:日期对象处理信息</p>
 * <p>Title:</p>
 * @author Joe_zhang</p>
 * @version 1.0</p>
 * @date 2009-12-23
 * <p>modify： </p>
 ******************************************************************
 */
public class SchedulerJobDate {

	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int sec = 0;
	
	Object[] obj = new Object[]{"year","month","day","hour","minute","sec"};
	
	public SchedulerJobDate(String dateString) {
		setParamsValue(dateString);
	}
	
	/**
	 * 将timestamp类型的时间日期转换为年/月/日/时/分/秒列表
	 * @param dateString
	 */
	private static List<String> DateSplit(String dateString) {
		// TODO Auto-generated method stub
		List<String> lSplitDate = new Vector<String>();
		String[] sDate = dateString.split(" ");
		String[] YMD = sDate[0].split("-");
		String[] HMS = null;
		if(sDate[1]!=null)
			HMS = sDate[1].split(":");
		for (int i = 0; i < YMD.length; i++) {
			lSplitDate.add(YMD[i]);
		}
		for (int i = 0; i < HMS.length; i++) {
			lSplitDate.add(HMS[i]);
		}
		
		return lSplitDate;
	}	
	/**
	 * 设置参数值
	 *
	 */
	private void setParamsValue(String dateString) {
		List<String> lDate = DateSplit(dateString);
		for (int i = 0; i < lDate.size(); i++) {
			String iValue = lDate.get(i);
			int values = Integer.parseInt(iValue);
			String paramsName = (String)obj[i];
			if("year".equalsIgnoreCase(paramsName)){
				year = values;
			}else if("month".equalsIgnoreCase(paramsName)){
				month = values;
			}else if("day".equalsIgnoreCase(paramsName)){
				day = values;
			}else if("hour".equalsIgnoreCase(paramsName)){
				hour = values;
			}else if("minute".equalsIgnoreCase(paramsName)){
				minute = values;
			}else if("sec".equalsIgnoreCase(paramsName)){
				sec = values;
			}
		}
		
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}


	public int getSec() {
		return sec;
	}


	public void setSec(int sec) {
		this.sec = sec;
	}
}
