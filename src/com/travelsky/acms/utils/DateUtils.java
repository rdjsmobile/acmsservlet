/*
 * Copyright (c) 2011-2036 International Air Transport Association corp.
 * All Rights Reserved.
 */
package com.travelsky.acms.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date tools.
 * 
 * @version Seurat v1.0
 * @author Li Jielin, 2011-12-9
 */
public class DateUtils {

    /** default time zone set to GMT+0. **/
    @SuppressWarnings("unused")
    private static final String DEFAULT_TIMEZONE = "GMT+0";
    /** this date format for internal use. **/
    private static final String DATEFORMAT_YYYYMMDD = "yyyyMMdd";
    /** this date format for internal use. **/
    private static final String DATEFORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "EPT", "OCT",
        "NOV", "DEC" };

    /** default constructor. **/
    private DateUtils() {
    }

    /**
     * get date.
     * 
     * @return Date.
     */
    public static Date getDate() {
        return new Date();
    }

//    /**
//     * create date.
//     * @param year int.
//     * @param month int.
//     * @param date int.
//     * @return Date.
//     */
//    public static Date createDate(int year, int month, int date) {
//        Calendar calendar = DateUtils.getCalendar();
//        calendar.set(year, month, date);
//        return calendar.getTime();
//    }

    /**
     * get GregorianCalendar.
     * 
     * @return Calendar,GregorianCalendar.
     */
    public static Calendar getCalendar() {
        return new GregorianCalendar();
    }

    /**
     * get GregorianCalendar with given date.
     * 
     * @param date Date.
     * @return Calendar, GregorianCalendar.
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * return date format with Locale US.
     * 
     * @param pattern date format pattern.
     * @return DateFormat
     */
    public static DateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.US);
    }

    /**
     * clear the time of date.
     * 
     * <pre>
     * sample:
     * 2012-11-11 12:00:00  ->  2012-11-11
     * </pre>
     * 
     * @param date
     * @return date
     */
    public static Date clearTime(Date date) {
        return parseDate(dateToString(date, DATEFORMAT_YYYYMMDD), DATEFORMAT_YYYYMMDD);
    }

    /**
     * Compare the time specified in the start and end period of time.
     * 
     * <pre>
     * start1 = new Date(50),end1 = new Date(200),start2 = new Date(1),end2 = new Date(100) --> true
     * start1 = new Date(50),end1 = new Date(200),start2 = new Date(100),end2 = new Date(1000) --> true
     * start1 = new Date(50),end1 = new Date(200),start2 = new Date(100),end2 = new Date(150) --> true
     * start1 = new Date(100),end1 = new Date(200),start2 = new Date(50),end2 = new Date(1000) --> true
     * 
     * If end1 or end2 is null by default to use a specific value(future:2222-02-02).
     * 
     * </pre>
     * 
     * @param start1 start time of the first group
     * @param end1 Date end time of the first group
     * @param start2 Date start time of the second group
     * @param end2 Date end time of the second group
     * @return boolean
     */
    public static boolean between(Date start1, Date end1, Date start2, Date end2) {
        Date f = future();
        if (end1 == null) {
            end1 = f;
        }
        if (end2 == null) {
            end2 = f;
        }

        if (start1.compareTo(start2) <= 0 && start2.compareTo(end1) <= 0) {
            return true;
        }
        if (start1.compareTo(end2) <= 0 && end2.compareTo(end1) <= 0) {
            return true;
        }

        if (start2.compareTo(start1) <= 0 && end2.compareTo(end1) >= 0) {
            return true;
        }

        if (f.equals(end1)) {
            end1 = null;
        }
        if (f.equals(end2)) {
            end2 = null;
        }
        return false;
    }

    /**
     * validate whether [start1,end1] contains in [start2,end2].
     * 
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return if [start1,end1] = [2012-09-01 12:00,2012-09-02 12:00],[start2,end2] = [2012-08-01
     *         12:00,2012-09-03 12:00],return true; if [start1,end1] = [2012-08-01 12:00,2012-09-03
     *         12:00],[start2,end2] = [2012-09-01 12:00,2012-09-02 12:00],return false; if
     *         [start1,end1] = [2012-08-01 12:00,null],[start2,end2] = [2012-09-01 12:00,2012-09-02
     *         12:00],return false; if [start1,end1] = [2012-08-01 12:00,null],[start2,end2] =
     *         [2012-09-01 12:00,null],return true;
     */
    public static boolean contain(Date start1, Date end1, Date start2, Date end2) {

        if (start2 == null) {
            if (end2 == null) {
                return true;
            } else {
                return false;
            }
        }

        if (start1 == null) {
            return false;
        }

        if (end1 == null) {
            return end2==null?start1.compareTo(start2) >= 0
                    :(start1.compareTo(start2) >= 0 && start1.compareTo(end2)<=0);
        }

        if (end1.compareTo(start2) == 0) {
            return start1.compareTo(start2) == 0;
        }

        if (end1.compareTo(start2) < 0) {
            return false;
        }

        if (end2 == null) {
            return start1.compareTo(start2) >= 0;
        }

        boolean flag1 = start1.compareTo(start2) >= 0;
        boolean flag2 = end1.compareTo(end2) <= 0;
        return (flag1 && flag2);
    }

    /**
     * get Native SQL Date.
     * 
     * @param date date
     * @return toDate string.
     */
    public static String getNativeSQLDate(Date date) {
        String dbString = "to_date('%s','yyyy-mm-dd hh24:mi:ss')";
        String d = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        return String.format(dbString, d);
    }

    /**
     * contain.
     * 
     * @param compareDate Date
     * @param fromDate Date
     * @param toDate Date
     * @return boolean
     */
    public static boolean contain(Date compareDate, Date fromDate, Date toDate) {
        if (compareDate == null) {
            return false;
        }
        if (fromDate == null) {
            return false;
        }
        long date = compareDate.getTime();
        long from = fromDate.getTime();
        if (from <= date) {
            if (toDate != null) {
                long to = toDate.getTime();
                if (date <= to) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Bsp level of time can't exceed Global level time range.
     * 
     * @param start1 Global level of start time
     * @param end1 Global level of end time
     * @param start2 Bsp level of start time
     * @param end2 Bsp level of end time
     * @return boolean
     */
    public static boolean timeCompare(Date start1, Date end1, Date start2, Date end2) {
        if (end1 == null && end2 == null) {
            return true;
        }

        int formAndTo = compareDateByDay(start2, end2);
        if (formAndTo > 0) {
            return false;
        }

        int from = compareDateByDay(start1, start2);

        if (from > 0) {
            return false;
        }

        int to = compareDateByDay(end1, end2);

        if (to < 1) {
            return false;
        }

        return false;
    }

    /**
     * Days, compare the size of the two dates.
     * 
     * <pre>
     *  (20120105,20120109) --> -1
     *  (20120109,20120105) --> 1
     *  (20120109,20120109) --> 0
     *  
     * If any date is null by default to use a specific value(future:2222-02-02).
     * 
     * </pre>
     * 
     * 
     * @param date1 Date
     * @param date2 Date
     * @return int
     */
    public static int compareDateByDay(Date date1, Date date2) {
        if (date1 == null) {
            date1 = future();
        }
        if (date2 == null) {
            date2 = future();
        }
        String d1 = dateToString(date1, DATEFORMAT_YYYYMMDD);
        String d2 = dateToString(date2, DATEFORMAT_YYYYMMDD);
        return d1.compareTo(d2);

    }

    /**
     * Timestamp, compare the size of the two dates.
     * 
     * <pre>
     *  (20120109121201,20120109121212) --> -1
     *  (20120109121212,20120109121201) --> 1
     *  (20120109121212,20120109121212) --> 0
     *  
     * If any date is null by default to use a specific value(future:2222-02-02).
     * 
     * </pre>
     * 
     * 
     * @param date1 Date
     * @param date2 Date
     * @return int
     */
    public static int compareDateByTimestamp(Date date1, Date date2) {
        if (date1 == null) {
            date1 = future();
        }
        if (date2 == null) {
            date2 = future();
        }
        String d1 = dateToString(date1, DATEFORMAT_YYYYMMDDHHMMSS);
        String d2 = dateToString(date2, DATEFORMAT_YYYYMMDDHHMMSS);
        return d1.compareTo(d2);

    }

    /**
     * A monthly basis, the size of the two dates.
     * 
     * <pre>
     *  (201201,201205) --> -1
     *  (201205,201201) --> 1
     *  (201205,201205) --> 0
     *  
     * If any date is null by default to use a specific value(future:2222-02-02).
     * 
     * </pre>
     * 
     * @param date1 Date
     * @param date2 Date
     * @return int
     */
    public static int compareDateByMonth(Date date1, Date date2) {
        if (date1 == null) {
            date1 = future();
        }
        if (date2 == null) {
            date2 = future();
        }
        String d1 = dateToString(date1, "yyyyMM");
        String d2 = dateToString(date2, "yyyyMM");
        return d1.compareTo(d2);

    }

    /**
     * Annualized to compare two dates size.
     * 
     * <pre>
     *  (2011,2012) --> -1
     *  (2012,2011) --> 1
     *  (2012,2012) --> 0
     *  
     * If any date is null by default to use a specific value(future:2222-02-02).
     * 
     * </pre>
     * 
     * @param date1 Date
     * @param date2 Date
     * @return int
     */
    public static int compareDateByYear(Date date1, Date date2) {
        if (date1 == null) {
            date1 = future();
        }
        if (date2 == null) {
            date2 = future();
        }
        String d1 = dateToString(date1, "yyyy");
        String d2 = dateToString(date2, "yyyy");
        return d1.compareTo(d2);
    }

    /**
     * Date converted to a string.
     * 
     * <pre>
     *  DateUtils.dateToString(new Date(0), "yyyyMMdd") --> 19700101
     * </pre>
     * 
     * @param date target date
     * @param pattern fomate pattern
     * @return String
     */
    public static String dateToString(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    /**
     * The date of Date type conversion, in accordance with the specified format to a String.
     * 
     * <pre>
     * Locale = Locale.US.
     * examples:
     * <code>
     * DateUtils.formatDateWithPattern(new Date(0), "yyyyMMdd") = "19700101"
     * </code>
     * </pre>
     * 
     * @param date target date
     * @param pattern formate pattern
     * @return String, return value will be <code>null</code> if has exceptions.
     */
    public static String formatDate(Date date, String pattern) {
        String result = null;
        if (date == null || pattern == null) {
            return result;
        }
        try {
            result = getDateFormat(pattern).format(date);
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    /**
     * Given a future date, default date format yyyy-MM-dd.
     * 
     * @return Date, will return 2222-02-02.
     */
    public static Date future() {
        Date date = null;
        try {
            DateFormat df = getDateFormat(DATEFORMAT_YYYYMMDD);
            date = df.parse(df.format(df.parse("22220202")));
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * Get the current date in which quarterly. The first quarter: January 1-March 31. The second
     * quarter: April 1 - June 30. The third quarter: July 1 - September 30. The fourth quarter:
     * October 1 - December 31.
     * 
     * <pre>
     * examples:
     *  01-01~04-01 --> 01
     *  04-01~07-01 --> 02
     *  07-01~10-01 --> 03
     *  10-01~01-01 --> 04
     * </pre>
     * 
     * @return String
     */
    public static String getCurrQuarter() {
        try {
            Calendar calendar = getCalendar();
            DateFormat df = getDateFormat("yyyy-MM-dd hh:mm");
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            Date dt = getDate();
            Date dt1;
            dt1 = df.parse(year + "-01-01 00:00");
            Date dt2 = df.parse(year + "-04-01 00:00");
            if (dt.getTime() >= dt1.getTime() && dt.getTime() < dt2.getTime()) {
                return "01";
            }
            dt1 = df.parse(year + "-04-01 00:00");
            dt2 = df.parse(year + "-07-01 00:00");
            if (dt.getTime() >= dt1.getTime() && dt.getTime() < dt2.getTime()) {
                return "02";
            }
            dt1 = df.parse(year + "-07-01 00:00");
            dt2 = df.parse(year + "-10-01 00:00");
            if (dt.getTime() >= dt1.getTime() && dt.getTime() < dt2.getTime()) {
                return "03";
            }
            dt1 = df.parse(year + "-10-01 00:00");
            year = String.valueOf(calendar.get(Calendar.YEAR) + 1);
            dt2 = df.parse(year + "-01-01 00:00");
            if (dt.getTime() >= dt1.getTime() && dt.getTime() < dt2.getTime()) {
                return "04";
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Specified date to the last day of the month.
     * 
     * <pre>
     * examples:
     *  DateUtils.getLastDay(new Date(0)) = 31
     *  DateUtils.getLastDay(null)        = 1-
     * </pre>
     * 
     * @param date Date
     * @return int, if date is null will return -1, else return the date.
     */
    public static int getLastDay(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    /**
     * According to the month to determine the last day of each month.
     * 
     * <pre>
     * examples:
     *  DateUtils.getLastDayByMonth("201201") = "20120131"
     *  DateUtils.getLastDayByMonth("201202") = "20120229"
     *  other conidtions will return null.
     * </pre>
     * 
     * @param month month number
     * @return String, return yyyyMMdd.
     */
    public static String getLastDayByMonth(String month) {
        if (null == month) {
            return null;
        }
        String year = month.substring(0, 4);
        String mon = month.substring(4, 6);
        String day = "30";
        List<String> month30 = Arrays.asList("04", "06", "09", "11");
        List<String> month31 = Arrays.asList("01", "03", "05", "07", "08", "10", "12");

        if (month30.contains(mon)) {
            day = "30";
        } else if (month31.contains(mon)) {
            day = "31";
        }
        if (mon.equals("02")) {
            if (isLeapYear(Integer.valueOf(year))) {
                day = "29";
            } else {
                day = "28";
            }
        }
        return month + day;
    }

    /**
     * judge the year whether is leap year.
     * 
     * @param year year value.
     * @return true: is leap year, false: other.
     */
    public static boolean isLeapYear(int year) {
        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            return true;
        }
        return false;
    }

    /**
     * Determine the last day of each quarter based on quarter.
     * 
     * <pre>
     * examples:
     *  DateUtils.getLastDayByQuarter("201201") = "20120331"
     *  DateUtils.getLastDayByQuarter("201202") = "20120630"
     *  DateUtils.getLastDayByQuarter("201203") = "20120930"
     *  DateUtils.getLastDayByQuarter("201204") = "20121231"
     *  other conditions will return null.
     * </pre>
     * 
     * @param quarter the input quarter
     * @return String, the return value will be: yyyy0331, yyyy0630, yyyy0931, yyyy1231.
     */
    public static String getLastDayByQuarter(String quarter) {
        if (null == quarter) {
            return null;
        }
        String year = quarter.substring(0, 4);
        String quart = quarter.substring(4, 6);
        String daily = "";
        if (quart.equals("01")) {
            daily = "0331";
        } else if (quart.equals("02")) {
            daily = "0630";
        } else if (quart.equals("03")) {
            daily = "0930";
        } else if (quart.equals("04")) {
            daily = "1231";
        } else {
            return null;
        }
        return year + daily;
    }

    /**
     * Determine the last month of each quarter based on quarter.
     * 
     * <pre>
     * examples:
     * DateUtils.getLastMonth("201201") = "201203"
     * DateUtils.getLastMonth("201202") = "201206"
     * DateUtils.getLastMonth("201203") = "201209"
     * DateUtils.getLastMonth("201204") = "201212"
     * other conditions will return null.
     * </pre>
     * 
     * @param quarter quarter number
     * @return String, the return value will be: yyyy03, yyyy06, yyyy09, yyyy12.
     */
    public static String getQuarterLastMonth(String quarter) {
        if (null == quarter) {
            return null;
        }
        String year = quarter.substring(0, 4);
        String quart = quarter.substring(4, 6);
        String month = "";
        if (quart.equals("01")) {
            month = "03";
        } else if (quart.equals("02")) {
            month = "06";
        } else if (quart.equals("03")) {
            month = "09";
        } else if (quart.equals("04")) {
            month = "12";
        } else {
            return null;
        }
        return year + month;
    }

    /**
     * Given quarter, next quarter.
     * 
     * <pre>
     * examples:
     *  DateUtils.getNextQuarter("20120303") = ""
     *  DateUtils.getNextQuarter("201201")   = "201202"
     *  DateUtils.getNextQuarter("201204")   = "201301"
     * </pre>
     * 
     * @param curQuarter given quarter
     * @return String, the return value will be: yyyy01, yyyy02, yyyy03, yyyy04.
     */
    public static String getNextQuarter(String curQuarter) {
        if (curQuarter.length() != 6) {
            return "";
        }
        int year = Integer.parseInt(curQuarter.substring(0, 4));
        int quart = Integer.parseInt(curQuarter.substring(4, 6));
        if (quart == 4) {
            year++;
            quart = 1;
        } else {
            quart++;
        }
        return year + "0" + quart;
    }

    /**
     * <pre>
     * The calculation of the date of increase, reducing the number of days of the date. 
     * days is a positive number that when added, is negative for the reduction
     * 
     *  DateUtils.addDay(1970-01-01, 1) = 1970-01-02
     * </pre>
     * 
     * @param date target date
     * @param days variable number
     * @return Date
     */
    public static Date addDay(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * <pre>
     * Set day of date object.
     * 
     *  DateUtils.setDay(1970-01-01, 17) = 1970-01-17
     * </pre>
     * 
     * @param date target date
     * @param days variable number
     * @return Date
     */
    public static Date setDay(Date date, int day) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.set(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * <pre>
     * Set hour of date object.
     * 
     *  DateUtils.setHour(1970-01-01, 17) = 1970-01-01 17:00:00
     * </pre>
     * 
     * @param date target date
     * @param hour variable number
     * @return Date
     */
    public static Date setHour(Date date, int hour) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    /**
     * <pre>
     * Set minute of date object.
     * 
     *  DateUtils.setMinute(1970-01-01 17:00:00, 17) = 1970-01-01 17:17:00
     * </pre>
     * 
     * @param date target date
     * @param minute variable number
     * @return Date
     */
    public static Date setMinute(Date date, int minute) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.set(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * <pre>
     * The calculation of the date of increase or decrease the number of hours after the date. 
     * hours is positive for Canada, is negative for Less
     * 
     *  DateUtils.addHour(1970-01-01 10:00, 1) = 1970-01-01 11:00
     * </pre>
     * 
     * @param date target date
     * @param hours variable number
     * @return Date
     */
    public static Date addHour(Date date, int hours) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.HOUR, hours);
        return c.getTime();
    }

    /**
     * <pre>
     * The calculation of the date of increase or decrease the number of minutes later date. 
     * minutes is positive for Canada, is negative for Less
     * 
     *  DateUtils.addMinute(1970-01-01 10:00, 1) = 1970-01-01 10:01
     * </pre>
     * 
     * @param date target date
     * @param minutes variable number
     * @return Date
     */
    public static Date addMinute(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    /**
     * <pre>
     * The calculation of the date of the increase or decrease the number of months after the date.
     * months is positive for Canada, is negative for Less
     * 
     * <code>
     * DateUtils.addMonth(1970-01-01, 1) = 1970-02-01
     * </code>
     * </pre>
     * 
     * @param date target date
     * @param months variable number
     * @return Date
     */
    public static Date addMonth(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * <pre>
     * Increase or reduce the number of seconds after the date of calculation date 
     * when seconds is a positive number is added as a negative number is less.
     * 
     *  DateUtils.addSecond(1970-01-01 10:00:00, 1) = 1970-01-01 10:00:01
     * </pre>
     * 
     * @param date target date
     * @param seconds variable number
     * @return Date
     */
    public static Date addSecond(Date date, int seconds) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * <pre>
     * The calculation of the date of the increase or decrease the number of weeks after the date. 
     * weeks is positive for Canada, is negative for Less
     * 
     *  DateUtils.addWeek(1970-01-01, 1) = 1970-01-08
     * </pre>
     * 
     * @param date target date
     * @param weeks variable number
     * @return Date
     */
    public static Date addWeek(Date date, int weeks) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.WEEK_OF_MONTH, weeks);
        return c.getTime();
    }

    /**
     * <pre>
     * The calculation of the date of the increase or decrease the number of years after the date.
     * years is positive for Canada, is negative for Less
     * 
     * DateUtils.addYear(20120101,2) = 20140101
     * DateUtils.addYear(20120101,-2) = 20100101
     * </pre>
     * 
     * @param date target date
     * @param years variable number
     * @return Date
     */
    public static Date addYear(Date date, int years) {
        if (date == null) {
            return null;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * Transferred in accordance with the specified format string date into a Date type.
     * 
     * <pre>
     * <code>
     * DateUtils.parseDate(20120202, "yyyyMMdd", TimeZone.getDefault()) = 20120202;
     * </code>
     * </pre>
     * 
     * @param dateString The date of the string types
     * @param format format pattern
     * @param zone timeZone
     * @return Date
     */
    public static Date parseDate(String dateString, String format, TimeZone zone) {
        Date date = null;
        try {
            DateFormat df = getDateFormat(format);
            df.setTimeZone(zone);
            date = df.parse(dateString);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * According to the specified format, String type Date object to parse the return value is a
     * Date object.
     * 
     * <pre>
     * <code>
     * DataUtils.parseDate("2012-12-01", "yyyy-MM-dd") = 2012-12-01
     * </code>
     * </pre>
     * 
     * @param dateString dateValue of string type
     * @param pattern formate pattern
     * @return Date
     */
    public static Date parseDate(String dateString, String pattern) {
        Date result = null;
        DateFormat sdf = getDateFormat(pattern);
        sdf.setLenient(false);
        try {
            result = sdf.parse(dateString);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * get the end time of the date.
     * 
     * <pre>
     * Example: 
     * the last time the given date, specific to the second date is 2011-12-09 The last time 2011-12-09 23:59:59.999
     * </pre>
     * 
     * <pre>
     * DateUtils.getEndTimeOfDay(2011-12-09) = 2011-12-09 23:59:59.999
     * </pre>
     * 
     * @param date Date
     * @return Date
     */
    public static Date getEndTimeOfDay(Date date) {
        Date result = null;

        if (date != null) {
            Calendar calendar = getCalendar(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 23, 59, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            result = calendar.getTime();
        }
        return result;
    }

    /**
     * <pre>
     * Start time of the given date, specific to the second. 
     * Example: The date is 2011-12-09 The last time 2011-12-09 00:00:00.000
     * </pre>
     * 
     * <pre>
     * DateUtils.getStartTimeOfDay(2011-12-09) = 2011-12-09 00:00:00.000
     * </pre>
     * 
     * @param date target date
     * @return Date
     */
    public static Date getStartTimeOfDay(Date date) {
        Date result = null;
        if (date != null) {
            Calendar calendar = getCalendar(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            result = calendar.getTime();
        }
        return result;
    }

    /**
     * Get the current date value.
     * 
     * <pre>
     * DateUtils.today() = 20120629
     * </pre>
     * 
     * @return Date, default pattern yyyyMMdd.
     */
    public static Date today() {
        return today(DATEFORMAT_YYYYMMDD);
    }

    /**
     * To get the current time value in accordance with the format specified.
     * 
     * <pre>
     * <code>
     * DateUtils.today("yyyyMMdd") = 20120629
     * </code>
     * </pre>
     * 
     * @param pattern date pattern to truncate the time.
     * @return Date
     */
    public static Date today(String pattern) {
        Date date = getDate();
        if (pattern == null) {
            return date;
        }
        try {
            DateFormat df = getDateFormat(pattern);
            date = df.parse(df.format(getDate()));
        } catch (Exception e) {
            return date;
        }
        return date;
    }

    /**
     * add date
     * 
     * @param date start date
     * @param unit the unit for add
     * @param quantity the quantity for add
     * @return date
     */
    public static Date addDate(Date date, String unit, int quantity) {
        if (unit.equalsIgnoreCase("Year")) {
            return addYear(date, quantity);
        }
        if (unit.equalsIgnoreCase("Month")) {
            return addMonth(date, quantity);
        }
        if (unit.equalsIgnoreCase("Week")) {
            return addWeek(date, quantity);
        }
        if (unit.equalsIgnoreCase("Day")) {
            return addDay(date, quantity);
        }
        if (unit.equalsIgnoreCase("Hour")) {
            return addHour(date, quantity);
        }
        if (unit.equalsIgnoreCase("Minute")) {
            return addMinute(date, quantity);
        }
        if (unit.equalsIgnoreCase("Second")) {
            return addSecond(date, quantity);
        }
        return null;
    }

    /**
     * Returns a number representing the month that represented by this Date object. The value
     * returned is between 1 and 12, with the value 1 representing January.
     * 
     * <pre>
     * <code>
     * DateUtils.getMonth(1970-01-01 10:00) = 1
     * </code>
     * </pre>
     * 
     * @param date - Date Object
     * @return the month represented by this date.
     */
    public static int getMonth(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * getYear.
     * 
     * @param date Date
     * @return int
     */
    public static int getYear(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * Returns the day of the month represented by this Date object. The value returned is between 1
     * and 31 representing the day of the month.
     * 
     * <pre>
     * <code>
     * DateUtils.getMonth(1970-01-05 10:00) = 5
     * </code>
     * </pre>
     * 
     * @param date - Date Object
     * @return the day of the month represented by this date.
     */
    public static int getDayOfMonth(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the day of the year represented by this Date object.
     * 
     * 
     * @param date - Date Object
     * @return the day of the year represented by this date.
     */
    public static int getDayOfYear(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Returns the day of the week represented by this date. The returned value (1 = Monday, 2 =
     * Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday, 7 = Sunday) represents the
     * day of the week.
     * 
     * <pre>
     * <code>
     * DateUtils.getMonth(1970-01-05 10:00) = 1
     * </code>
     * </pre>
     * 
     * @param date - Date Object
     * @return the day of the week represented by this date.
     */
    public static int getDayOfWeek(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * Return the week no. of the month represented by this date.
     * 
     * @param date - Date Object
     * @return the week no. of the month
     */
    public static int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_MONTH);
    }
}
