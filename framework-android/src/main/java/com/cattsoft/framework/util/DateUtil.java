package com.cattsoft.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 处理时间公共类
 * @author xueweiwei
 *
 */
public class DateUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "hh:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
	/* 定义日历对象，初始化时，用来获取当前时间 */
	public static Calendar mCalendar = Calendar.getInstance(Locale.CHINA);//从Calendar抽象基类获得实例对象，并设置成中国时区
	
	/**
	 * 获取当前系统时间
	 * 
	 */
	public static String getCurrentDate() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		String currentDate = sdf.format(dt);

		return currentDate;
	}
	/**
	 * 获取当前系统时间字符串，去掉-、空格和：
	 * @return
	 */
	public static String getCurrentDateStr(){
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		String currentDate = sdf.format(dt);
		return currentDate.replaceAll("-| |:", "");
	}
	
	/**
	 * 当前年份
	 * @return
	 */
	public static int getCurrYear(){
		return mCalendar.get(Calendar.YEAR);
	}
	/**
	 * 当前月份
	 * @return
	 */
	public static int getCurrMonth(){
		return mCalendar.get(Calendar.MONTH);
	}
	/**
	 * 当前日
	 * @return
	 */
	public static int getCurrDay(){
		return mCalendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 当前周
	 * @return
	 */
	public static int getCurrWeek(){
		return mCalendar.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 比较当前系统时间和传入的时间，如果当前时间已经过了传入的时间，则返回负数，否则返回正数
	 * @param orderDate
	 * @return
	 * @throws ParseException
	 */
	public static int compareToCurrDate(String orderDate) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date d1 = sdf.parse(orderDate);
		Date d2 = sdf.parse(getCurrentDate());// 当前系统时间
		return (int) (d1.getTime() - d2.getTime());		
	}
	/**
	 * 格式为yyyy-MM-dd HH:mm:ss的两个时间比较，如果第一个时间大于第二时间则返回ture，否则返回false
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareTime(String time1,String time2) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date date1 = sdf.parse(time1);
		Date date2 = sdf.parse(time2);
		return date1.getTime() - date2.getTime() > 0 ? true : false;
	}
	/**
	 * 如果第一个时间大于第二个时间则返回ture，否则false
	 * @param time1
	 * @param time2  
	 * @param format 时间格式
	 * @return
	 * @throws ParseException 
	 */
	public static boolean compareTime(String time1, String time2,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date1 = sdf.parse(time1);
		Date date2 = sdf.parse(time2);
		return date1.getTime() - date2.getTime() >= 0 ? true : false;

	}

	/**
	 * 按指定格式将字符串转换为日期对象
	 *
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 *
	 */
	public static Date to_date(String dateStr, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 按指定的格式将日期对象转换为字符串
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String to_char(Date date, String format) {
		if (date == null)
			return null;
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 日期（精确到秒）转字符串
	 *
	 * @param date
	 * @return
	 */
	public static String dateTime2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, DATE_TIME_FORMAT);
		}
		return str;
	}

	/**
	 * 得到大于传入天数的日期
	 *
	 * @param date
	 * @param num
	 * @return java.util.Date
	 * @throws ParseException
	 */
	public static Date addDays(Date date, int num) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DAY_OF_MONTH, num);
		return cal.getTime();
	}

	/**
	 * 字符串转日期（精确到分）
	 *
	 * @param str
	 * @return
	 * @throws
	 * @throws ParseException
	 */
	public static Date str2DateTimeHM(String str) {
		Date date = null;
		if (!StringUtil.isBlank(str)) {
			date = DateUtil.to_date(str, YEAR_MONTH_DAY_HOUR_MINUTE);
		}
		return date;

	}

	/**
	 * 字符串转日期（精确到秒）
	 *
	 * @param str
	 * @return
	 * @throws
	 * @throws ParseException
	 */
	public static Date str2DateTimeHMS(String str) {
		Date date = null;
		if (!StringUtil.isBlank(str)) {
			date = DateUtil.to_date(str, DATE_TIME_FORMAT);
		}
		return date;

	}



	/**
	 * 取得当前日期是多少周
	 * @author LQY
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}


	/**
	 * 得到某一年周的总数
	 * @author LQY
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime());
	}

	/**
	 * 得到某年某周的第一天
	 * @author LQY
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * @author LQY
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 取得指定日期所在周的第一天
	 * @author LQY
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * @author LQY
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的第一天
	 * @author LQY
	 * @return
	 */
	public static Date getFirstDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime ();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * @author LQY
	 * @return
	 */
	public static Date getLastDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}



	/**
	 * 获得指定日期的前一天
	 * @author LQY
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(DATE_FORMAT).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		return c.getTime();
	}

	/**
	 * 获得指定日期的后一天
	 * @author LQY
	 * @param specifiedDay
	 * @return
	 */
	public static Date getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(DATE_FORMAT).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		return c.getTime();
	}



	/**
	 * 获得指定月的前一月
	 * @author LQY
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedMonthBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(MONTH_FORMAT).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}

	/**
	 * 获得指定月的后一月
	 * @author LQY
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedMonthAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(MONTH_FORMAT).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		c.add(Calendar.MONTH, 1);

		return c.getTime();
	}

}
