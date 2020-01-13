package com.flower.sea.commonservice.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * LocalDateTime 工具类
 *
 * @author zhangLei
 * @serial 2020-01-13
 */
public class LocalDateTimeUtils {

    private LocalDateTimeUtils() {

    }

    /**
     * 指定时间跟当前时间进行对比
     * 判断指定时间是否已经过去
     * 参数的时间格式必须是:yy-mm-dd,否则会报错
     *
     * @param time 指定的时间(yy-mm-dd)
     * @return 大于0: 指定时间已经过去 小于0:指定时间尚未过去 等于0:恰好今天
     */
    public static Integer determineWhetherTheCurrentTimeHasPassed(String time) {
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = LocalDate.parse(time);
        return localDate.compareTo(localDate1);
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date Date时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time LocalDateTime时间格式
     * @return Date
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param time LocalDateTime时间格式
     * @return Long
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time LocalDateTime时间格式
     * @return Long
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time    LocalDateTime时间格式
     * @param pattern 对应的时间格式
     * @return String
     */
    private static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern 对应的时间格式
     * @return String
     */
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time   LocalDateTime时间格式的日期
     * @param number 加上的数值
     * @param field  ChronoUnit.* 单位(年月日时分秒)
     * @return LocalDateTime
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     *
     * @param time   LocalDateTime时间格式的日期
     * @param number 减去的数值
     * @param field  ChronoUnit.* 单位(年月日时分秒)
     * @return LocalDateTime
     */
    public static LocalDateTime reduce(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     ChronoUnit.* 单位(年月日时分秒)
     * @return long
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time 指定的时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     *
     * @param time 指定的时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }
}
