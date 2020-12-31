import org.junit.Test;

import java.time.LocalDateTime;

public class LocalDateTimeTest {

    @Test
    public void test1(){

        LocalDateTime dt = LocalDateTime.parse("2018-11-03T12:45:30");
        System.out.println(dt);
        System.out.println(dt.toLocalDate());

        //java.time.format.DateTimeParseException: Text '2018-11-03 12:45:30' could not be parsed at index 10
        //LocalDateTime dt2 = LocalDateTime.parse("2018-11-03 12:45:30");
        //System.out.println(dt2.toLocalDate());

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        //处理时间区间
        String start = now.toLocalDate().toString() + " 00:00:00";
        String end = now.toLocalDate().toString() + " 23:23:59";
        System.out.println(start);
        System.out.println(end);


    }
}
