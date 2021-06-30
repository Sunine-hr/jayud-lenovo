package com.jayud.oceanship.utils;

import java.time.LocalDateTime;

public class DateUtil {

    //将数字日期转换成英文日期
    public static String dateToEnglish(LocalDateTime dateTime) {
        int monthValue = dateTime.getMonthValue();
        int year = dateTime.getYear();
        int dayOfMonth = dateTime.getDayOfMonth();
        StringBuffer str = new StringBuffer();
        switch (monthValue){
            case 1:
                str.append(dayOfMonth).append(" ").append("Jan").append(" ").append(year);
                break;
            case 2:
                str.append(dayOfMonth).append(" ").append("Feb").append(" ").append(year);
                break;
            case 3:
                str.append(dayOfMonth).append(" ").append("Mar").append(" ").append(year);
                break;
            case 4:
                str.append(dayOfMonth).append(" ").append("Apr").append(" ").append(year);
                break;
            case 5:
                str.append(dayOfMonth).append(" ").append("May").append(" ").append(year);
                break;
            case 6:
                str.append(dayOfMonth).append(" ").append("Jun").append(" ").append(year);
                break;
            case 7:
                str.append(dayOfMonth).append(" ").append("Jul").append(" ").append(year);
                break;
            case 8:
                str.append(dayOfMonth).append(" ").append("Aug").append(" ").append(year);
                break;
            case 9:
                str.append(dayOfMonth).append(" ").append("Sep").append(" ").append(year);
                break;
            case 10:
                str.append(dayOfMonth).append(" ").append("Oct").append(" ").append(year);
                break;
            case 11:
                str.append(dayOfMonth).append(" ").append("Nov").append(" ").append(year);
                break;
            case 12:
                str.append(dayOfMonth).append(" ").append("Dec").append(" ").append(year);
                break;

        }
        return str.toString().toUpperCase();
    }


//    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        int monthValue = now.getMonthValue();
//        int year = now.getYear();
//        int dayOfMonth = now.getDayOfMonth();
//
//        System.out.println(monthValue);
//        System.out.println(year);
//        System.out.println(dayOfMonth);
//    }
}
