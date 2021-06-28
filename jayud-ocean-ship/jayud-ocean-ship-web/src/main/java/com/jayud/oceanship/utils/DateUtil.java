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
                str.append(dayOfMonth).append(" ").append("January").append(" ").append(year);
                break;
            case 2:
                str.append(dayOfMonth).append(" ").append("February").append(" ").append(year);
                break;
            case 3:
                str.append(dayOfMonth).append(" ").append("March").append(" ").append(year);
                break;
            case 4:
                str.append(dayOfMonth).append(" ").append("April").append(" ").append(year);
                break;
            case 5:
                str.append(dayOfMonth).append(" ").append("May").append(" ").append(year);
                break;
            case 6:
                str.append(dayOfMonth).append(" ").append("June").append(" ").append(year);
                break;
            case 7:
                str.append(dayOfMonth).append(" ").append("July").append(" ").append(year);
                break;
            case 8:
                str.append(dayOfMonth).append(" ").append("August").append(" ").append(year);
                break;
            case 9:
                str.append(dayOfMonth).append(" ").append("September").append(" ").append(year);
                break;
            case 10:
                str.append(dayOfMonth).append(" ").append("October").append(" ").append(year);
                break;
            case 11:
                str.append(dayOfMonth).append(" ").append("November").append(" ").append(year);
                break;
            case 12:
                str.append(dayOfMonth).append(" ").append("December").append(" ").append(year);
                break;

        }
        return str.toString();
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
