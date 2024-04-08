package com.ms.demo.system;

import java.time.*;

public class TimeTest {
    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(now.getSecond());
//        System.out.println(now.getHour());

//        LocalDate date1 = LocalDate.of(2001, 10, 2);
//        LocalDate date2 = LocalDate.of(2001, 12, 2);
//
//        Period between = Period.between(date1, date2);
//        System.out.println(between);

        LocalTime time1 = LocalTime.of(22, 21, 11);
        LocalTime time2 = LocalTime.of(23, 11, 10);

        System.out.println(Duration.between(time1, time2));  // PT49M59S  T 表示小时  M表示分钟 S表示秒
    }
}
