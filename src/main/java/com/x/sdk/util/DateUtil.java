package com.x.sdk.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.base.exception.SystemException;

public final class DateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYYYMM = "yyyyMM";

    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    
    private DateUtil() {
        // 禁止实例化
    }

    /**
     * 获取当前时间戳
     * 
     * @return
     */
    public static long getCurrentTimeMillis() {
        Timestamp time = getSysDate();
        return time.getTime();
    }

    public static String getCurrentTime() throws SystemException {
        return getDateString(DATETIME_FORMAT);
    }

    /**
     * 获取系统时间
     * 
     * @return
     */
    public static Timestamp getSysDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 根据指定的格式输入时间字符串
     * 
     * @param pattern
     * @return
     * @throws SystemException 
     */
    public static String getDateString(String pattern) throws SystemException {
        if (StringUtil.isBlank(pattern)) {
            throw new SystemException("请指定日期格式");
        }
        Timestamp time = getSysDate();
        DateFormat dfmt = new SimpleDateFormat(pattern);
        java.util.Date date = time;
        return dfmt.format(date);
    }

    /**
     * 获取时间字符串
     * 
     * @param time
     * @param pattern
     * @return
     * @throws SystemException 
     */
    public static String getDateString(Timestamp time, String pattern) throws SystemException {
        if (time == null) {
            throw new SystemException("请指定时间");
        }
        if (StringUtil.isBlank(pattern)) {
            throw new SystemException("请指定格式");
        }
        DateFormat dfmt = new SimpleDateFormat(pattern);
        java.util.Date date = time;
        return date != null ? dfmt.format(date) : "";
    }

    /**
     * 获取指定时间的格式化串
     * 
     * @param date
     * @param pattern
     * @return
     * @throws SystemException 
     */
    public static String getDateString(Date date, String pattern) throws SystemException {
        if (date == null) {
            throw new SystemException("请指定时间");
        }
        if (StringUtil.isBlank(pattern)) {
            throw new SystemException("请指定格式");
        }
        SimpleDateFormat sdfmt = new SimpleDateFormat(pattern);
        return date != null ? sdfmt.format(date) : "";
    }

    /**
     * 判断时间是否符合格式要求
     * 
     * @param str
     * @param fomat
     * @return
     * @throws SystemException 
     */
    public static boolean isValidDate(String str, String fomat) throws SystemException {
        if (StringUtil.isBlank(str)) {
            throw new SystemException("请指定时间字符");
        }
        if (StringUtil.isBlank(fomat)) {
            throw new SystemException("请指定格式");
        }
        boolean flag = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fomat);
            sdf.parse(str);
            flag = true;
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * 字符串转日期（精确到日）
     * 
     * @param str
     * @return
     * @throws SystemException 
     */
    public static Date str2Date(String str) throws SystemException {
        Date date = null;
        if (!StringUtil.isBlank(str)) {
            date = DateUtil.to_date(str, DATE_FORMAT);
        }
        return date;

    }

    public static Timestamp getFutureTime() throws SystemException {
        Date d = str2Timestamp("2100-01-01 00:00:00");
        return getBeforeSecond(new Timestamp(d.getTime()));
    }

    /**
     * 转换为时间
     * 
     * @param str
     * @return
     * @throws SystemException 
     */
    public static Date str2Timestamp(String str) throws SystemException {
        Date date = null;
        if (!StringUtil.isBlank(str)) {
            date = DateUtil.to_date(str, DATETIME_FORMAT);
        }
        return date;

    }

    /**
     * 按指定格式将字符串转换为日期对象
     * 
     * @param dateStr
     * @param format
     * @return
     * @throws SystemException 
     */
    public static Date to_date(String dateStr, String format) throws SystemException {
        if (StringUtil.isBlank(dateStr)) {
            throw new SystemException("请指定时间");
        }
        if (StringUtil.isBlank(format)) {
            throw new SystemException("请指定格式");
        }
        DateFormat df = new SimpleDateFormat(format);
        try {
            java.util.Date date = df.parse(dateStr);
            return new Date(date.getTime());
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
            throw new SystemException("系统转换日期字符串时出错！", e);
        }
    }

    /**
     * 获取日期
     * 
     * @return
     * @throws SystemException 
     */
    public static Date getDate() throws SystemException {
        String s = getDateString(DATETIME_FORMAT);
        return DateUtil.str2Date(s);
    }

    /**
     * 获取某个时间点所代表的日期
     * 
     * @param sysDate
     * @return
     * @throws SystemException 
     */
    public static Date getTheDayDate(Timestamp sysDate) throws SystemException {
        DateFormat dfmt = new SimpleDateFormat(DATETIME_FORMAT);
        java.util.Date date = sysDate;
        String dateString = dfmt.format(date);
        return DateUtil.str2Date(dateString);
    }

    /**
     * 获取指定时间点偏移天数后的日期
     * 
     * @param sysDate
     * @param offsetDays
     * @return
     * @throws SystemException 
     */
    public static Date getOffsetDaysDate(Timestamp sysDate, int offsetDays) throws SystemException {
        Timestamp t = getOffsetDaysTime(sysDate, offsetDays);
        return getTheDayDate(t);
    }

    public static Date getOffsetDaysDate(Date date, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offsetDays);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取一天的第一秒 如：2011-11-11 00:00:00
     * 
     * @param sysDate
     * @return
     */
    public static Timestamp getTheDayFirstSecond(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取一天的最后一秒 如：2011-11-11 23:59:59
     * 
     * @param sysDate
     * @return
     */
    public static Timestamp getTheDayLastSecond(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, -1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取指定时间的偏移天数后的时间
     * 
     * @param sysDate
     * @param offsetDays
     * @return
     */
    public static Timestamp getOffsetDaysTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.DAY_OF_MONTH, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取指定时间的偏移月份后的时间
     * 
     * @param sysDate
     * @param offsetDays
     * @return
     * @author yangpy
     */
    public static Timestamp getOffsetMonthsTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.MONTH, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取指定时间的偏移年份后的时间
     * 
     * @param sysDate
     * @param offsetDays
     * @return
     * @author zhangxianwei
     */
    public static Timestamp getOffsetYearsTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.YEAR, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取本月最后一秒
     * 
     * @param sysDate
     * @return
     */
    public static Timestamp getTimeThisMonthLastSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, -1);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取本月第一秒
     * 
     * @param sysDate
     * @return
     */
    public static Timestamp getTimeThisMonthFirstSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取下月第一秒
     * 
     * @param sysDate
     * @return
     */
    public static Timestamp getTimeNextMonthFirstSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取当月总的天数
     * 
     * @return
     */
    public static int getDaysOfThisMonth() {
        Timestamp currTimestamp = DateUtil.getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 从yyyyMM格式中获取月
     * 
     * @param yyyyMM
     * @return
     * @throws SystemException 
     */
    public static String getMonth(String yyyyMM) throws SystemException {
        if (StringUtil.isBlank(yyyyMM) || yyyyMM.length() != 6) {
            throw new SystemException("格式出错，无法获取月");
        }
        return yyyyMM.substring(4, 6);
    }

    /**
     * 验证日期格式 yyyyMM、yyyyMMdd、yyyyMMddHHmmss
     * 
     * @param str
     * @return
     */
    public static boolean isDateType(String str) {
        String withYYYYMMDDHHSSRegax = "^\\d{4}([1-9]|(1[0-2])|(0[1-9]))([1-9]|([12]\\d)|(3[01])|(0[1-9]))(([0-1][0-9])|([2][0-3]))([0-5][0-9])([0-5][0-9])$";
        String withYYYYMMDDRegax = "^\\d{4}([1-9]|(1[0-2])|(0[1-9]))([1-9]|([12]\\d)|(3[01])|(0[1-9]))$";
        String withYYYYMMRegax = "^\\d{4}((1[0-2])|(0[1-9]))$";
        if (StringUtil.isBlank(str)) {
            return false;
        }
        if (str.length() == 6) {
            return str.matches(withYYYYMMRegax);
        } else if (str.length() == 8) {
            return str.matches(withYYYYMMDDRegax);
        } else if (str.length() == 14) {
            return str.matches(withYYYYMMDDHHSSRegax);
        }
        return false;
    }

    /**
     * 计算两个日期的时间差(天)
     * 
     * @param formatTime1
     * @param formatTime2
     * @return
     */
    public static long getTimeDifference(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        // 毫秒ms
        long diff = t1 - t2;
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * 两个日期相差月份
     * 
     * @param beginDate
     *            yyyyMM
     * @param endDate
     *            yyyyMM
     * @return
     */
    public static int getTimeDifference(String beginDate, String endDate) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMM");

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        try {
            cal1.setTime(timeformat.parse(endDate));
            cal2.setTime(timeformat.parse(beginDate));
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)
                - cal2.get(Calendar.MONTH);
        return c;
    }

    /**
     * 获取今天是本月第几天
     * 
     * @return
     */
    public static int getDates() {
        Timestamp currTimestamp = DateUtil.getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 计算两个日期之间相差天数
     * 
     * @param beginDate
     *            yyyyMM
     * @param endDate
     *            yyyyMM
     * @return
     */
    public static int getDaysBetween(String beginDate, String endDate) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0;
        try {
            cal.setTime(timeformat.parse(beginDate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(timeformat.parse(endDate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取指定月份的偏移月数所在的月
     * 
     * @param date
     *            yyyyMM
     * @param offsetMonth
     *            （正数：以后，负数：以前）
     * @return
     */
    public static String getOffsetMonth(String date, int offsetMonth) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMM");

        Calendar cal = new GregorianCalendar();
        try {
            cal.setTime(timeformat.parse(date));
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        cal.add(Calendar.MONTH, offsetMonth);
        return timeformat.format(cal.getTime());
    }

    /**
     * 计算两个日期的时间差(分钟)
     * 
     * @param formatTime1
     * @param formatTime2
     * @return
     */
    public static long getMinuteDif(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        // 毫秒ms
        long diff = t1 - t2;
        return diff / (60 * 1000);
    }

    /**
     * 格式化时间 Locale是设置语言敏感操作
     * 
     * @param formatTime
     * @return
     */
    public static String getTimeStampNumberFormat(Timestamp formatTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss",
                new Locale("zh", "cn"));
        return format.format(formatTime);
    }

    public static int getMillis() {
        Timestamp currTimestamp = DateUtil.getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 当前时间前一秒
     * 
     * @param currentDate
     * @return
     */
    public static Timestamp getBeforeSecond(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(Calendar.SECOND, -1);
        return new Timestamp(calender.getTimeInMillis());
    }

    /**
     * 当前时间后一秒
     * 
     * @param currentDate
     * @return
     */
    public static Timestamp getAfterSecond(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(Calendar.SECOND, 1);
        return new Timestamp(calender.getTimeInMillis());
    }

    public static Timestamp getTimestamp(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Timestamp ts = null;
        try {
            ts = new Timestamp(format.parse(time).getTime());
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return ts;
    }

    /**
     * 获取系统年月
     */
    public static String getCurYM() {
        SimpleDateFormat df = new SimpleDateFormat(YYYYMM);// 设置日期格式
        Calendar calender = Calendar.getInstance();
        return df.format(calender.getTime());
    }

    /**
     * 将指定格式的日期字符串转成Timestamp
     * 
     * @param time
     * @param pattern
     * @return
     * @author mayt
     * @throws SystemException 
     */
    public static Timestamp getTimestamp(String time, String pattern) throws SystemException {
        if (StringUtil.isBlank(time)) {
            throw new SystemException("请指定字符串时间");
        }
        if (StringUtil.isBlank(pattern)) {
            throw new SystemException("请指定日期格式");
        }
        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        Timestamp ts = null;
        try {
            ts = new Timestamp(format.parse(time).getTime());
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return ts;
    }

    public static Timestamp getTimestamp(long time) {
        Timestamp ts = new Timestamp(time);
        return ts;
    }

    /**
     * 计算两个日期的时间差(月)
     * 
     * @param formatTime1
     * @param formatTime2
     * @return
     * @author zhangxd7
     */
    public static int getTimeDifferenceMonth(Timestamp formatTime1, Timestamp formatTime2) {

        Calendar calendarTime1 = Calendar.getInstance();
        calendarTime1.setTime(formatTime1);
        int yearTime1 = calendarTime1.get(Calendar.YEAR);
        int monthTime1 = calendarTime1.get(Calendar.MONTH);
        int dayTime1 = calendarTime1.get(Calendar.DAY_OF_MONTH);

        Calendar calendarTime2 = Calendar.getInstance();
        calendarTime2.setTime(formatTime2);
        int yearTime2 = calendarTime2.get(Calendar.YEAR);
        int monthTime2 = calendarTime2.get(Calendar.MONTH);
        int dayTime2 = calendarTime2.get(Calendar.DAY_OF_MONTH);

        int y = yearTime2 - yearTime1;// 年差
        int m = monthTime2 - monthTime1;// 月差
        int d = dayTime2 - dayTime1;// 天差

        if (d >= 0) {
            // 如果天数差大于零
            return (y * 12 + m + 1);
        } else {
            return (y * 12 + m);
        }
    }

    /**
     * 时间转换成中文
     * 
     * @param time
     * @return
     */
    public static String trans2CnTime(Timestamp time) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return df.format(time);
    }

    /**
     * 时间转换成中文
     * 
     * @param time
     * @return
     */
    public static String trans2CnDate(Timestamp time) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(time);
    }

    /**
     * Timestamp类型时间加一天
     * 
     * @param date
     * @return
     * @author fengbo
     */
    public static Timestamp getTimeNextDay(Timestamp date) {
        long time = date.getTime();
        time = time + 24 * 60 * 60 * 1000;
        return new Timestamp(time);
    }

    /**
     * Timestamp类型时间减一天
     * 
     * @param date
     * @return
     * @author fengbo
     */
    public static Timestamp getTimeBeforeDay(Timestamp date) {
        long time = date.getTime();
        time = time - 24 * 60 * 60 * 1000;
        return new Timestamp(time);
    }

    /**
     * 当前时间(currentDate)前一月
     * 
     * @param currentDate
     * @return
     * @author guorf
     */
    public static Timestamp getBeforeMonth(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(Calendar.MONTH, -1);
        return new Timestamp(calender.getTimeInMillis());
    }

    /**
     * 获取上月的最后一秒 如：2011-11-11 23:59:59
     * 
     * @param sysDate
     * @author zhangxd7
     */
    public static Timestamp getTimeLastMonthLastSec(Timestamp sysDate) {
        // 获取当前时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(sysDate);
        // 调到上个月
        cal.add(Calendar.MONTH, -1);
        // 得到一个月最最后一天日期(31/30/29/28)
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置时间
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), maxDay, 23, 59, 59);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    /**
     * 获取年星期 yyyy-ww
     * @return
     * @author mayt
     */
    public static String getYearWeek() {
        Date date = getDate();
        return getYearWeek(date);
    }
    /**
     * 获取年星期 yyyy-ww
     * @return
     * @author mayt
     */
    public static String getYearWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        String year =  (new SimpleDateFormat("yyyy")).format(calendar.getTime());
        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        if (week.length() < 2) {
            week = "0" + week;
        }
        return year + "-" + week;
    }
}
