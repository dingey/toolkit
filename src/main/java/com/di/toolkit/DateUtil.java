package com.di.toolkit;

import java.util.Calendar;
import java.util.Date;

/**
 * @author di
 */
public class DateUtil {
	/**
	 * 获取现在时间
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * 获取今天零点时间
	 * 
	 * @return
	 */
	public static Date getCurrentZeroDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取距当前时间几天后的时间
	 * 
	 * @param days
	 * @return
	 */
	public static Date getDaysLater(int days) {
		return getDaysLater(getCurrentDate(), days);
	}

	/**
	 * 获取指定时间date几天后的时间
	 * 
	 * @param date指定时间
	 * @param days几天后,如果要几天前，请输入负数
	 * @return
	 */
	public static Date getDaysLater(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间几周后的时间
	 * 
	 * @param weeks几周，负数表示前几周
	 * @return
	 */
	public static Date getWeeksLater(int weeks) {
		return getWeeksLater(getCurrentDate(), weeks);
	}

	/**
	 * 获取指定时间date几周后的时间
	 * 
	 * @param date指定时间
	 * @param weeks几周
	 * @return
	 */
	public static Date getWeeksLater(Date date, int weeks) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, weeks);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间几月后的时间
	 * 
	 * @param months几月
	 * @return
	 */
	public static Date getMonthsLater(int months) {
		return getMonthsLater(getCurrentDate(), months);
	}
	/**
	 * 获取指定时间date几月
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date getMonthsLater(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	public static Date getYearsLater(int years) {
		return getYearsLater(getCurrentDate(), years);
	}

	public static Date getYearsLater(Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	public static Date getPreviousYear() {
		return getYearsLater(-1);
	}

	public static Date getNextYear() {
		return getYearsLater(1);
	}

	public static Date getPreviousMonth() {
		return getMonthsLater(-1);
	}

	public static Date getNextMonth() {
		return getMonthsLater(1);
	}

	public static Date getPreviousDay() {
		return getDaysLater(-1);
	}

	public static Date getNextDay() {
		return getDaysLater(1);
	}

	public static int daysBetween1(Date date1, Date date2) {
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween2(Date date1, Date date2) {
		Calendar can1 = Calendar.getInstance();
		can1.setTime(date1);
		Calendar can2 = Calendar.getInstance();
		can2.setTime(date2);
		// 拿出两个年份
		int year1 = can1.get(Calendar.YEAR);
		int year2 = can2.get(Calendar.YEAR);
		// 天数
		int days = 0;
		Calendar can = null;
		// 如果can1 < can2
		// 减去小的时间在这一年已经过了的天数
		// 加上大的时间已过的天数
		if (can1.before(can2)) {
			days -= can1.get(Calendar.DAY_OF_YEAR);
			days += can2.get(Calendar.DAY_OF_YEAR);
			can = can1;
		} else {
			days -= can2.get(Calendar.DAY_OF_YEAR);
			days += can1.get(Calendar.DAY_OF_YEAR);
			can = can2;
		}
		for (int i = 0; i < Math.abs(year2 - year1); i++) {
			// 获取小的时间当前年的总天数
			days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
			// 再计算下一年。
			can.add(Calendar.YEAR, 1);
		}
		return days;
	}

}
