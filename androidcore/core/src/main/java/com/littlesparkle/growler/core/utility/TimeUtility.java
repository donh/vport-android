package com.littlesparkle.growler.core.utility;

import android.content.Context;

import com.littlesparkle.growler.core.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Copyright (C) 2016-2016, The Little-Sparkle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TimeUtility {
    /**
     * 秒转换为时分秒
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getTimeInMessage(int time, Context context) {
        long nowTime = System.currentTimeMillis();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(new Date(Long.valueOf(time + "000")));
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date(nowTime));
        if (dateCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR)
                && dateCalendar.get(Calendar.MONTH) == nowCalendar.get(Calendar.MONTH)
                && dateCalendar.get(Calendar.DAY_OF_MONTH) == nowCalendar.get(Calendar.DAY_OF_MONTH)) {
            return context.getString(R.string.today) + " " + new SimpleDateFormat("HH:mm").format(new Date(Long.valueOf(time + "000")));
        } else if ((dateCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR)
                && dateCalendar.get(Calendar.MONTH) == nowCalendar.get(Calendar.MONTH)
                && dateCalendar.get(Calendar.DAY_OF_MONTH) != nowCalendar.get(Calendar.DAY_OF_MONTH))
                && dateCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == nowCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
            switch (dateCalendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    return context.getString(R.string.sunday);
                case 2:
                    return context.getString(R.string.monday);
                case 3:
                    return context.getString(R.string.tuesday);
                case 4:
                    return context.getString(R.string.wednesday);
                case 5:
                    return context.getString(R.string.thursday);
                case 6:
                    return context.getString(R.string.friday);
                case 7:
                    return context.getString(R.string.saturday);
            }

        }
        return new SimpleDateFormat("MM" + context.getString(R.string.month) + " " + "dd" + context.getString(R.string.day)).format(new Date(Long.valueOf(time + "000")));
    }

    public static String formatDate(int time) {
        if (time < 0) {
            time = 0;
        }
        Date dateAndTime = new Date(time * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(dateAndTime);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * date2 是否在 date1 的 几天、几小时、几分钟 或 几年之前
     *
     * @param date1
     * @param date2
     * @param day          数值
     * @param calendarType 单位
     * @return
     */
    public static boolean isTimeAfterTime(Date date1, Date date2, int day, int calendarType) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(calendarType, calendar1.get(calendarType) - day);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar1.after(calendar2);
    }
}
