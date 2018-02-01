/*
 *   Copyright (C)  2016 android@19code.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package alpha.cyber.intelmain.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT_SHORTDATE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 格式化日期时间
     *
     * @param date
     * @return
     */
    public static String formatDataTime(long date) {
        return DATE_FORMAT_DATETIME.format(new Date(date));
    }
   /**
     * 格式化日期时间
     *
     * @param date
     * @return
     */
    public static String formatShortDataTime(long date) {
        return DATE_FORMAT_SHORTDATE.format(new Date(date));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(long date) {
        return DATE_FORMAT_DATE.format(new Date(date));
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String formatTime(long date) {
        return DATE_FORMAT_TIME.format(new Date(date));
    }

    /**
     * 自定义格式的格式化日期时间
     *
     * @param beginDate
     * @param format
     * @return
     */
    public static String formatDateCustom(String beginDate, String format) {
        return new SimpleDateFormat(format).format(new Date(Long.parseLong(beginDate)));
    }

    /**
     * 将时间字符串转换成Date
     *
     * @param beginDate
     * @param format
     * @return
     */
    public static String formatDateCustom(Date beginDate, String format) {
        return new SimpleDateFormat(format).format(beginDate);
    }

    /**
     * 获取系统时间
     *
     * @param s
     * @param style
     * @return
     */
    public static Date string2Date(String s, String style) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(style);
        Date date = null;
        if (s == null || s.length() < 6) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    /**
     * 计算两个时间差
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static long subtractDate(Date dateStart, Date dateEnd) {
        return dateEnd.getTime() - dateStart.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获取当前时间为本月的第几周
     *
     * @return
     */
    public static int getWeekOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        return week - 1;
    }

    /**
     * 获取当前时间为本周的第几天
     *
     * @return
     */
    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day = day - 1;
        }
        return day;
    }

    /**
     * 根据strformat日期格式,转换成特定的日期格式
     *
     * @param date
     * @param strformat "yyyy-MM-dd hh:mm:ss"12小时格式 ,"yyyyMMddHHmmss","yyyy-MM-dd HH:mm","yyyy年MM月dd日   EEEE","MM-dd HH:mm"
     * @return "yyyy年MM月dd日","yyyy-MM-dd"  "yyyy-MM-dd HH:mm:ss"24小时格式
     */
    public static String getTime(Date date, String strformat) {
        SimpleDateFormat format = new SimpleDateFormat(strformat);
        return format.format(date);
    }

    public static String getTime(long dt, String strformat) {
        Date date = new Date(dt);
        SimpleDateFormat format = new SimpleDateFormat(strformat);
        return format.format(date);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public final static String getWeekOfDate1(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 动态获取时间格式
     *
     * @param milliseconds
     * @return
     */
    public static String getDynamicTime(long milliseconds) {
        SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fm2 = new SimpleDateFormat("MM-dd");
        SimpleDateFormat fm3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat fm4 = new SimpleDateFormat("MM-dd HH:mm");
        if (milliseconds < 0) return "时间异常";
        Date history = new Date(milliseconds);
        Date current = new Date();
        // 判断年
        if (current.getYear() != history.getYear()) {
            return fm1.format(history);
        }
        // 判断月
        if (current.getMonth() != history.getMonth()) {
            return fm2.format(history);
        }
        // 判断日
        int intervalDay = current.getDate() - history.getDate();
        // 超过2天
        if (intervalDay > 2) {
            return fm2.format(history);
        } else if (intervalDay > 1) {
            return "前天";
        } else if (intervalDay > 0) {
            return "昨天 " + fm3.format(history);
        } else if (intervalDay < 0) { //时间超前1天及以上，一个月以内用fm4
            return fm4.format(history);
        }
        //今天
        int intervalHour = current.getHours() - history.getHours(); // 获取间隔了多少小时
        int intervalMinute = (int) ((current.getTime() - history.getTime()) / (60 * 1000)); // 获取间隔了多少分钟
        // 时间超前
        if (intervalMinute < 0) {
            return "今天 " + fm3.format(history);
        }
        //刚刚
        if (intervalMinute < 3) {
            return "刚刚";
        }
        if (intervalMinute < 60) {
            return intervalMinute + "分钟前";
        } else {
            return intervalHour + "小时前";
        }
    }
    /**
     * 获取指定的时间的Date对象
     *
     * @param time
     * @return
     */
    public final static Date getDate(long time) {

        return new Date(time);
    }
    /**
     * Date 对象转换成对应格式的 字符串
     *
     * @param date 默认："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String dateToString(Date date) {
        if (date == null)
            return "";
        return dateToString(date, null);
    }

    /**
     * Date 对象转换成对应格式的 字符串
     *
     * @param date
     * @param format 如："yyyy-MM-dd HH:mm:ss"，默认："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat formater = new SimpleDateFormat(
                isNull(format) ? "yyyy-MM-dd HH:mm:ss" : format.trim());
        return formater.format(date);
    }
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        str = str != null ? str.trim() : str;
        return str == null || "".equals(str) ? true : false;
    }
    /**
     * 获取动态格式时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDynamicFormateDate(String date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date history;
        try {
            history = formater.parse(date);
        } catch (ParseException e) {
            return date; // 格式不对，直接返回原来数据
        }
        Date current = new Date();
        if(current.compareTo(history) <= 0){
            return date.substring(0, date.indexOf(" "));
        }
        // 判断年月
        if (current.getYear() > history.getYear()
                || current.getMonth() > history.getMonth()) {
            return date.substring(0, date.indexOf(" "));
        }

        int intervalDay = current.getDay() - history.getDay();

        int minutes = history.getMinutes();

        String StrMinutes = null;

        if (minutes < 10) {
            StrMinutes = "0" + minutes;
        } else {
            StrMinutes = "" + minutes;
        }

        // 超过2天，直接显示原来格式时间
        if (intervalDay > 2) {
            return date.substring(0, date.indexOf(" "));
        } else if (intervalDay > 1) {
            return "前天" + history.getHours() + ":" + StrMinutes;
        } else if (intervalDay > 0) {
            return "昨天" + history.getHours() + ":" + StrMinutes;
        }

        int intervalMinute = (int) ((current.getTime() - history.getTime()) / (60 * 1000)); // 获取间隔了多少分钟

        // 时间超前，显示原来时间
        if (intervalMinute < 0) {
            return date.substring(0, date.indexOf(" "));
        }

        // 今天
        if (intervalMinute < 3) {
            return "刚刚";
        }
        if (intervalMinute < 60) {
            return intervalMinute + "分钟前";
        } else if (intervalMinute < 24 * 60) {
            return "今天" + history.getHours() + ":" + StrMinutes;
        } else {
            return date.substring(0, date.indexOf(" "));
        }

    }

    public static boolean isIntervalTwoMinute(String oldDateTime,
                                              String newDateTime) {

        try {
            Date oldDate = toDate(oldDateTime);
            Date newDate = toDate(newDateTime);

            long oldDateLong = oldDate.getTime();
            long newDateLong = newDate.getTime();

            if (newDateLong - oldDateLong > 2 * 60 * 1000) {
                return false;
            }

        } catch (Exception e) {
            return true;
        }

        return true;
    }


    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取动态格式时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getModularizationDate(Date date) {
        SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat fm2 = new SimpleDateFormat("HH:mm");
        String beforeTime1 = fm1.format(date);
        String beforeTime2 = fm2.format(date);
        Date current = new Date();
        // 判断年月
        if (current.getYear() > date.getYear() || current.getMonth() > date.getMonth()) {
            return beforeTime1;
        }
        int intervalDay = current.getDate() - date.getDate();

        // 超过2天，直接显示原来格式时间
        if (intervalDay == 0) {
            return "今天  " + beforeTime2;
        } else if (intervalDay == 1) {
            return "昨天  " + beforeTime2;
        } else {
            return beforeTime1;
        }
    }

    public static boolean isBusyHour() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        return hour > 6 && hour < 22 ? true : false;
    }
}
