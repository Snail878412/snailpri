package com.snail.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.snail.common.Constants;

/**
 * 
 * <p>
 * Title: com.snail.util.DateUtil
 * </p>
 * 
 * <p>
 * Description: 日期处理工具类.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月12日
 * 
 * @version 1.0
 * 
 */
public class DateUtil extends DateUtils {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");
	public static final SimpleDateFormat DATE_DF = new SimpleDateFormat(
			Constants.DATE_FORMAT);
	public static final SimpleDateFormat DATE_TIME_DF = new SimpleDateFormat(
			Constants.DATE_TIME_FORMAT);

	/**
	 * 格式化日期.
	 * 
	 * @param date
	 * @return 年-月-日
	 */
	public static String getFormatDate(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_DF.format(date);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 * @return 年-月-日 时:分:秒
	 */
	public static String getFormatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_TIME_DF.format(date);
	}

	/**
	 * 返回次月月初日期
	 * 
	 * @param dateStr
	 * @param parsePattern
	 * @return 年-月-日
	 * @throws Exception
	 */
	public static String getNextMonthBegin(String dateStr, String parsePattern)
			throws Exception {
		if (StringUtils.isBlank(parsePattern)) {
			parsePattern = Constants.DATE_FORMAT;
		}
		Date date = parseDate(dateStr, parsePattern);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		return DATE_DF.format(ca.getTime());
	}

	/**
	 * 返回指定日期月初的日期
	 * 
	 * @param date
	 * @return 年-月-日
	 */
	public static String getMonthBegin(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		return DATE_DF.format(ca.getTime());
	}

	/**
	 * 返回指定日期月末的日期
	 * 
	 * @param date
	 * @return 年-月-日
	 */
	public static String getMonthEnd(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		return DATE_DF.format(ca.getTime());
	}

	/**
	 * 返回指定month月之后的月末
	 * 
	 * @param dateStr
	 * @param parsePattern
	 * @param monthNum
	 * @return 年-月-日
	 * @throws ParseException
	 */
	public static String getAfterMonthEndDate(String dateStr,
			String parsePattern, int monthNum) throws ParseException {
		Date date = parseDate(dateStr, parsePattern);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, monthNum + 1);
		ca.set(Calendar.DAY_OF_MONTH, 0);
		return DATE_DF.format(ca.getTime());
	}

	/**
	 * 返回日期的毫秒数
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeInMillis(Date date) {
		if (date == null) {
			date = new Date();
		}
		return String.valueOf(toCalendar(date).getTimeInMillis());
	}

	/**
	 * 返回当前日期的毫秒数.
	 * 
	 * @return
	 */
	public static String getNowTimeInMillis() {
		return getTimeInMillis(null);
	}

	/**
	 * 计算指定的日期当月有多少天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		if (date != null) {
			ca.setTime(date);
		}
		return getDaysOfMonth(ca);
	}

	/**
	 * 计算指定的日期当月有多少天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Calendar ca) {
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		return ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算指定日期 当月剩余天数的比值 比较 当月共30天 当天27号 则返回 0.1
	 * 
	 * @param ca
	 * @return
	 */
	public static double getRisdualRatioOfDay(Calendar ca) {
		if (ca == null) {
			ca = Calendar.getInstance();
		}
		double ratio = 0.0;
		int thisDay = ca.get(Calendar.DAY_OF_MONTH);
		int monthDays = getDaysOfMonth(ca);
		ratio = (monthDays - thisDay) / (double) monthDays;
		BigDecimal bg = new BigDecimal(ratio);
		return bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 产生当前时间的序列号
	 * 
	 * 序列号 = 年(1) + 月(1) + 日(2) + 时(1) + 分(2) + 秒(2)
	 * 
	 * 年对26取余 再加上65 最后将得到的数字转换成char 这样得到的就是26个字母中的一个了
	 * 
	 * 月/时也是类似的处理 将月+65后转成char
	 * 
	 * 日/分/秒 则是直接显示两位数字
	 * 
	 * @return
	 */
	public static String getCurrentDateSerialNumber() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH);
		int day = ca.get(Calendar.DAY_OF_MONTH);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int minute = ca.get(Calendar.MINUTE);
		int second = ca.get(Calendar.SECOND);
		StringBuffer serialNum = new StringBuffer();
		serialNum.append((char) ((year % 26) + 65));
		serialNum.append((char) (month + 65));
		serialNum.append(DECIMAL_FORMAT.format(day));
		serialNum.append((char) (hour + 65));
		serialNum.append(DECIMAL_FORMAT.format(minute));
		serialNum.append(DECIMAL_FORMAT.format(second));
		return serialNum.toString();
	}
}
