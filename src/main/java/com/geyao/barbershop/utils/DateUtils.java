package com.geyao.barbershop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {


    public static String getNow(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
    public static String getNowDate(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

        /*
           str to date(format:yyyy-MM-dd HH:mm:ss)
         */
        public static Date strToYMDHSMDate(String strDate) {
            return strToDate(strDate, "yyyy-MM-dd HH:mm:ss");
        }

        public static Date strToYMDDate(String strDate) {
            return strToDate(strDate, "yyyyMMdd");
        }

        public static Date strToyMDDate(String strDate) {
            return strToDate(strDate, "yyMMdd");
        }

        public static Date strToDate(String strDate, String formatStr) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(formatStr);
                if (strDate == null || "".equals(strDate)) {
                    return new Date();
                }
                Date strToDate = format.parse(strDate);
                return strToDate;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String transferDate(String strDay, String sourceFormat, String targetFormat) {
            Date startDate = strToDate(strDay, sourceFormat);
            return dateToStr(startDate, targetFormat);
        }

        /**
         * 将字符串日期时间转换成java.util.Date类型
         * <p>
         * 日期时间格式yyyy-MM-dd HH:mm:ss
         *
         * @param datetime
         * @return
         */
        public static Date parseDatetime(String datetime) throws ParseException {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return datetimeFormat.parse(datetime);
        }

        /**
         * 将字符串日期转换成java.util.Date类型
         * <p>
         * 日期时间格式yyyy-MM-dd
         *
         * @param date
         * @return
         * @throws ParseException
         */
        public static Date parseDate(String date, String pattern) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 判断原日期是否在目标日期之前
         *
         * @param src
         * @param dst
         * @return
         */
        public static boolean isBefore(Date src, Date dst) {
            return src.before(dst);
        }

        public static String dateToStr(Date strDate, String formatStr) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(formatStr);
                if (strDate == null || "".equals(strDate)) {
                    return null;
                }
                String dateToStr = format.format(strDate);
                return dateToStr;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static int getCurrentMonth() {
            return Calendar.getInstance().get(Calendar.MONTH) + 1;
        }

        public static int getCurrentYear() {
            return Calendar.getInstance().get(Calendar.YEAR);
        }

        public static int getMonth(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH) + 1;
        }

        public static int getPreviousOrNextMonth(String dateString, int n) {
            Date date = strToDate(dateString, "yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, n);
            return cal.get(Calendar.MONTH) + 1;
        }

        public static int getPreviousYear(String year, int n) {
            Date date = strToDate(year, "yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, n);
            return cal.get(Calendar.YEAR);
        }


        public static String getPreviousOrNextDay(String date, String format, int n) {
            Date _date = strToDate(date, format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.DATE, n);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }

        public static String getCheckPreviousOrNextDay(String date, String format, int n) {
            Date _date = strToYMDDate(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.DATE, n);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }

        public static String getNextDay(String date, String format, int n) {
            Date _date = strToDate(date, "yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.DATE, n);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }


        public static String getNextDay(String date) {
            String formatTmp = "yyyyMMdd";
            Date _date = strToDate(date, formatTmp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.DATE, 1);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(formatTmp);
            return sdf.format(time);
        }

        public static String getPreviousOrNextYear(String date, String format, int n) {
            Date _date = strToDate(date, format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.YEAR, n);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }

        public static int getMonth(String datestr, String format) {
            Date date = strToDate(datestr, format);
            return getMonth(date);
        }

        public static String getYesterday(String format) {
            return getPreviousOrNextDay(null, format, -1);
        }

        public static int getLastYear() {
            Date _date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            cal.add(Calendar.YEAR, -1);
            Date time = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            return Integer.parseInt(sdf.format(time));
        }



        /**
         * 将字符串日期转换成java.util.Date类型
         * <p>
         * 日期时间格式yyyy-MM-dd
         *
         * @param date
         * @return
         * @throws ParseException
         */
        public static Date parseDate(String date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 判断某个日期是否在某个日期范围
         *
         * @param beginDate 日期范围开始
         * @param endDate   日期范围结束
         * @param src       需要判断的日期
         * @return
         */
        public static boolean between(Date beginDate, Date endDate, Date src) {
            return beginDate.before(src) && endDate.after(src);
        }

        /**
         * 时间计算 格式化日期时间
         *
         * @param date
         * @param pattern 格式化模式，详见{@link SimpleDateFormat}构造器
         *                <code>SimpleDateFormat(String pattern)</code>
         * @return
         */
        public static String calcDate(Date date, String pattern, int days) {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
            customFormat.applyPattern(pattern);
            Calendar cal = calendar();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, days);
            return customFormat.format(cal.getTime());
        }

        public static String calcDate(String dateStr, String pattern, int days) {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat1.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
            customFormat.applyPattern(pattern);
            Calendar cal = calendar();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, days);
            return customFormat.format(cal.getTime());
        }

        public static Calendar calendar() {
            Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            return cal;
        }

        /**
         * 判断原日期是否在目标日期之后
         *
         * @param src
         * @param dst
         * @return
         */
        public static boolean isAfter(Date src, Date dst) {
            return src.after(dst);
        }

        /**
         * 验证字符串是否为日期
         *
         * @param key
         * @return
         */
        public static boolean verifyStrToDate(String key) {
            String regex = "^[1-9]\\d{3}\\-(0?[1-9]|1[0-2])\\-(0?[1-9]|[12]\\d|3[01])\\s*(0?[1-9]|1\\d|2[0-3])(\\:(0?[1-9]|[1-5]\\d)){2}$";
            return (key.matches(regex));
        }

        /**
         * @param
         * @return
         * @Description 获取n天前的时间
         * @Date 2019/8/30
         * @Author LiLiang
         **/
        public static Date getDateBefore(Date d, int day) {
            Calendar now = Calendar.getInstance();
            now.setTime(d);
            now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
            return now.getTime();
        }

        /**
         * @param
         * @return
         * @Description 获取前n个月的时间
         * @Date 2019/12/23
         * @Author LiLiang
         **/
        public static Date getDateBeforeMonth(int n) {
            Calendar c = Calendar.getInstance();
            //过去一月
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, n);
            return c.getTime();
        }

        public static String dateToStr(Date date) {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return datetimeFormat.format(date);
        }

        /**
         * 获取某天的开始日期(时间清0)
         *
         * @param offset
         * @return java.util.Date
         */
        public static Date getDateYMD(int offset) {
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = dayStart(offset).atZone(zone).toInstant();
            return Date.from(instant);
        }


        /**
         * 获取某天的开始日期
         *
         * @param offset 0今天，1明天，-1昨天，依次类推
         * @return
         */
        public static LocalDateTime dayStart(int offset) {
            return LocalDate.now().plusDays(offset).atStartOfDay();
        }

        /**
         * 获取此刻与相对当天第day天的起始时间相隔的秒数。day为0表示今天的起始时间；1明天，2后天，-1昨天，-2前天等，依次例推。
         *
         * @param day
         * @return
         */
        public static int ttl(int day) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime time = LocalDate.now().plusDays(day).atStartOfDay();
            int ttl = (int) Duration.between(now, time).toMillis() / 1000;
            return ttl;
        }

        /**
         * 获取某周的开始日期
         *
         * @param offset 0本周，1下周，-1上周，依次类推
         * @return
         */
        public static LocalDate weekStart(int offset) {
            LocalDate localDate = LocalDate.now().plusWeeks(offset);
            return localDate.with(DayOfWeek.MONDAY);
        }

        /**
         * 获取某月的开始日期
         *
         * @param offset 0本月，1下个月，-1上个月，依次类推
         * @return
         */
        public static LocalDate monthStart(int offset) {
            return LocalDate.now().plusMonths(offset).with(TemporalAdjusters.firstDayOfMonth());
        }

        /**
         * @Author: umizhang
         * @Title: getStartOrEndDayOfQuarter
         * @Description TODO 获取本季度的第一天或最后一天
         * @Date: 2019/7/23 13:46
         * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
         * @return: java.lang.String
         * @Exception:
         */
        public static String getStartOrEndDayOfQuarter(LocalDate today, Boolean isFirst) {
            LocalDate resDate = LocalDate.now();
            if (today == null) {
                today = resDate;
            }
            Month month = today.getMonth();
            Month firstMonthOfQuarter = month.firstMonthOfQuarter();
            Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
            if (isFirst) {
                resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
            } else {
                resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
            }
            return resDate.toString();
        }


        /**
         * @return java.time.LocalDate
         * @Description //TODO 上一季度起始时间
         * @Date 10:07 2020/3/16
         * @Author zouxinru
         * @Param []
         **/
        public static LocalDate lastQuarterStartTime(LocalDate nowDate) {
            LocalDate localDate = nowDate.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
            return localDate;
        }

        /**
         * @return java.time.LocalDate
         * @Description //TODO 上一季度结束时间
         * @Date 10:08 2020/3/16
         * @Author zouxinru
         * @Param [nowDate]
         **/
        public static LocalDate lastQuarterEndTime(LocalDate nowDate) {
            LocalDate localDate = nowDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            return localDate;
        }


        /**
         * 获取某年的开始日期
         *
         * @param offset 0今年，1明年，-1去年，依次类推
         * @return
         */
        public static LocalDate yearStart(int offset) {
            return LocalDate.now().plusYears(offset).with(TemporalAdjusters.firstDayOfYear());
        }

        /**
         * 获取某天 第 past 天的日期
         *
         * @param past
         * @return
         */
        public static String getFutureDate(int past, Date date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
            Date today = calendar.getTime();
            String result = format.format(today);
            return result;
        }

        /**
         * String 转localDate
         *
         * @return
         */
        public static LocalDate getStringToLocalDate(String date, String pattern) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(date, fmt);
        }

        /**
         * @return java.util.Date
         * @Description String转Date
         * @Date 15:17 2019/5/14
         * @Author zouxinru
         * @Param [str]
         **/
        public static Date getStringToDate(String str) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(str);
        }

        public static Date getStrToDate(String str,String format) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(str);
        }

        /**
         * @param date
         * @param type 转换格式
         * @return String
         * @Description date转String
         * @Date 2019年7月31日
         * @Author LiLiang
         **/
        public static String getDateToString(Date date, String type) {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.format(date);
        }

        /**
         * @Description //TODO yyyyMMdd to yyyy-MM-dd HH:mm:ss
         * @Date 16:03 2020/3/31
         * @Author zouxinru
         * @Param [maxOrMix LocalTime.MIN/LocalTime.MAX]
         * @return java.lang.String
         **/
        public static String tradeQueryPatternChange(String date,LocalTime maxOrMix){
            DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter ymdhms = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDate.parse(date, ymd).atTime(maxOrMix).format(ymdhms);
        }

        /**
         * 时间字符串格式转换
         * 目前支持：yyyy-MM-dd HH:mm:ss；yyyy-MM-dd；yyyyMMddHHmmss；yyyyMMdd；四种入参格式
         * @param dateStr
         * @param formatType 你想得到的格式
         * @return
         */
        public static String dateStrFormat(String dateStr,String formatType){
            SimpleDateFormat sdf = null;
            if(dateStr.contains("-")){
                if(dateStr.contains(":")){
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }else{
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }
            }else if(dateStr.length() == 14){
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            }else if(dateStr.length() == 8){
                sdf = new SimpleDateFormat("yyyyMMdd");
            }else{
                return dateStr;
            }
            try{
                Date date  = sdf.parse(dateStr);
                return dateFormatByStr(date,formatType);
            }catch (ParseException e){
                e.printStackTrace();
                return dateStr;
            }
        }

        public static String dateFormatByStr(Date date,String formatStr){
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }


        /**
         * 计算日期{@code startDate}与{@code endDate}的间隔天数
         *
         * @param startDate
         * @param endDate
         * @return 间隔天数
         */
        public static long until(Date startDate, Date endDate){
            return UDateToLocalDate(startDate).until(UDateToLocalDate(endDate), ChronoUnit.DAYS);
        }

        /**
         * Date 转 LocalDate
         * @param date
         * @return
         */
        public static LocalDate UDateToLocalDate(Date date) {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            return localDateTime.toLocalDate();
        }

}
