package cn.remex.core.util;

import cn.remex.core.exception.DateException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 *	日期处理工具
 */
public class DateHelper {
	/**
	 * 根据日期获得年龄
	 * @param dts string格式的日期
	 * @return	int  年龄
	 */
	static public int getAge(final String dts) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date ld;int age =0;
		try {
			Assert.notNullAndEmpty(dts, "传参错误!不能为空或空值");
			ld = sdf.parse(dts);
			Calendar lc = Calendar.getInstance();
			lc.setTime(ld);
			int lyear = lc.get(Calendar.YEAR);
			int lmonth = lc.get(Calendar.MONTH)+1;
			int lday = lc.get(Calendar.DAY_OF_MONTH);
			Calendar nc = Calendar.getInstance();
			nc.setTime(new Date());
			int nyear = nc.get(Calendar.YEAR);
			int nmonth = nc.get(Calendar.MONTH)+1;
			int nday = nc.get(Calendar.DAY_OF_MONTH);

			age = nyear - lyear;
			if (nmonth < lmonth) {
				age--;
			} else if (nmonth == lmonth && nday < lday) {
				age--;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return age;
	}

	/**
	 * 将String转化为日期格式
	 * @param dateString  日期格式的string
	 * @param format 要转化的日期格式
	 * @return Date 日期
	 */
	 
	static public Date parseDate(String dateString, String... format){
		Assert.notNullAndEmpty(dateString, "日期字符串为空！");
		SimpleDateFormat sdf = new SimpleDateFormat(null == format || format.length<1?"yyyy-MM-dd":format[0]);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new DateException("格式化信息出错", e) ;
		}

	}
	/**
	 * @param dateString 要转换的时间字符
	 * @param format 转换格式
	 * @return String 返回指定格式的时间类型的字符串
	 */
	static public String parseDateString(String dateString, String... format){
		return	formatDate(parseDate(dateString, format), format);
	}
	/**
	 * 将日期格式转化为String
	 * @param date 日期
	 * @param format 日期格式
	 * @return String 
	 */
	static public String formatDate(Date date, String... format){
		Assert.notNullAndEmpty(date, "日期不能为空!");
		SimpleDateFormat sdf = new SimpleDateFormat(null == format|| format.length<1?"yyyy-MM-dd":format[0]);
			return sdf.format(date);

	}
	/**
	 * 获取日期参数个数
	 * @param dateString string型的日期
	 * @param key  key:y-年，M-月,d-天
	 * @param format 日期格式
	 * @return int 日期参数个数
	 */
	static public int getDateParam(String dateString, String key,String...format){
		return getDateParam(parseDate(dateString, format[0]),key);
	}
	/**
	 * 获取日期参数个数
	 * key:y-年，M-月,d-天
	 * @param date 日期
	 * @param key 
	 * @return int 
	 */
	static public int getDateParam(Date date, String key){
		Calendar nc = Calendar.getInstance();
		nc.setTime(date);
		if("y".equals(key)){
			return nc.get(Calendar.YEAR);
		}else if("M".equals(key)){
			return nc.get(Calendar.MONTH)+1;
		}else if("d".equals(key)){
			return nc.get(Calendar.DAY_OF_MONTH);
		}else {
			throw new DateException("参数错误，只能是y-年，M-月,d-天") ;
		}

	}
	/**
	 * 通过设置天数, 日期向前或向后跳转几天
	 * @param date 需要处理的时间
	 * @param diff 指定改变日期的天数 负数 日期提前 正数 日期往后
	 * @param format 可不输入默认为："yyyy-MM-dd"
	 * @return Date 日期
	 */
	static public Date getDate(Date date,int diff,String...format){
		Assert.notNullAndEmpty(date, "日期不能为空");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, diff);
		return calendar.getTime();

	}
	/**
	 * 通过设置年数, 日期向前或向后跳转几年
	 * @param date 需要处理的日期
	 * @param diff 指定改变日期的年数 负数 日期提前 正数 日期往后
	 * @param format 指定格式 可不输入默认为："yyyy-MM-dd"
	 * @return Date 日期
	 */
	static public Date getDateDiffYear(Date date,int diff,String...format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, diff);
		return calendar.getTime();
	}
	/**
	 * 参见DateHelper#getDate(Date, int, String...)
	 * @see DateHelper#getDate(Date, int, String...)
	 * @param date 日期
	 * @param diff 时间间隔
	 * @param format 日期格式
	 * @return
	 */
	static public String getDate(String date,int diff,String...format){
		SimpleDateFormat sdf = new SimpleDateFormat(null == format?"yyyy-MM-dd":format[0]);
		return sdf.format(getDate(parseDate(date,format), diff, format));

	}
	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"形式当前日期
	 * @return String string型的日期
	 */
	static public String getNow(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());

	}
	/**
	 * 返回format形式当前日期
	 * 如format = "yyyy-MM-dd HH:mm:ss";
	 * @param format 日期格式
	 * @return String string型的日期
	 */
	static public String getNow(final String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());

	}

	/**
	 * 根据reqCal获取上diffMon或下diffMon个月份的时间。proxyDay为指定月份的第几天
	 * 
	 * @param reqCal null时，则以当前时间为基准。
	 * @param diffMon 正数月份前移，负数月份后移
	 * @param proxyDay 指定月份的第几天
	 * @return
	 */
	public static Date getTargetDate(Calendar  reqCal,int diffMon,int proxyDay){
		Calendar  cal = null == reqCal ? Calendar.getInstance() : reqCal;
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+diffMon);
		cal.set(Calendar.DATE, proxyDay); 
		Date date = cal.getTime();
		return date;
	}
	
	public static Date getDiffTargetDate(Calendar cal,int diffDay){
		if(null == cal){
			cal = Calendar.getInstance();
		}
//		cal.set(cal.DAY_OF_YEAR, cal.get(cal.DAY_OF_YEAR)+diffDay);
		cal.add(Calendar.DATE, diffDay);
		return cal.getTime();
	}
	
	public static int getMonthDays(Object year,Object month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year.toString()));
		cal.set(Calendar.MONTH, Integer.parseInt((month.toString()))-1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 查询两个日期之间相差的月份
	 * @param startDateStr 起始日期
	 * @param endDateStr 截止日期
	 * @return 相差的月份
	 */
	public static int getDiffMonth(String startDateStr, String endDateStr){
		Date startDate = parseDate(startDateStr);
		Date endDate = parseDate(endDateStr);
		Assert.isTrue((endDate.getTime() - startDate.getTime())>0, "起始日期须小于截止日期！");
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		return (endCalendar.get(Calendar.YEAR)-startCalendar.get(Calendar.YEAR))*12 + 
				endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH);
	}


	/**
	 * @param dateStrOrDateOrCalendar 日期参数，支持Date/String/Calendar,如果是String 格式默认为为 yyyy-MM-dd
	 * @param field {@link Calendar},如：{@link Calendar#YEAR}{@link Calendar#MONTH}{@link Calendar#MONDAY}等。
	 * @param diffValue 正负整数，整数是往未来移动，负数是往过去移动
	 * @param format 可选参数
	 * @return
	 */
	public static Date getDate(Object dateStrOrDateOrCalendar, int field, int diffValue,String... format){
		Calendar cale;
		if(dateStrOrDateOrCalendar instanceof Date){
			cale =Calendar.getInstance();
			cale.setTime((Date)dateStrOrDateOrCalendar);
		}else if(dateStrOrDateOrCalendar instanceof String){
			cale =Calendar.getInstance();
			cale.setTime((Date)parseDate((String)dateStrOrDateOrCalendar,null == format?"yyyy-MM-dd":format[0]));
		}else if(dateStrOrDateOrCalendar instanceof Calendar)
			cale = (Calendar)dateStrOrDateOrCalendar;
		else
			throw new DateException("日期工具函数参数类型错误，此处仅支持Date/String/Calendar类型");
		
		cale.add(field, diffValue);
		
		return cale.getTime();
		
	}
}
