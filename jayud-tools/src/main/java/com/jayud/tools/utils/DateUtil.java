package com.jayud.tools.utils;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getWeekDay(LocalDateTime dateTime){
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        Date date = Date.from(dateTime.atZone( ZoneId.systemDefault()).toInstant());

        return dateFm.format(date);
    }

    public static String getWeekDay2(LocalDateTime dateTime){
        Date date = Date.from(dateTime.atZone( ZoneId.systemDefault()).toInstant());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (i < 0){
            i = 0;
        }
        return weekDays[i];
    }


}
