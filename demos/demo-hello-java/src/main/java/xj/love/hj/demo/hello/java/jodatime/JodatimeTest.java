package xj.love.hj.demo.hello.java.jodatime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jodatime简单使用示例。
 *
 * @author xiaojia
 * @since 1.0
 */
public class JodatimeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JodatimeTest.class);

    public static void calNovemberFirstMondyAfterFirstTuesday() {
        LocalDate now = new LocalDate(new Date());
        LocalDate electionDate = now.monthOfYear()//
                .setCopy(11) // November
                .dayOfMonth() // Access Day Of Month Property
                .withMinimumValue() // Get its minimum value
                .plusDays(6) // Add 6 days
                .dayOfWeek() // Access Day Of Week Property
                .setCopy("Monday") // Set to Monday (it will round down)
                .plusDays(1); // Gives us Tuesday
        LOGGER.info("11月中第一个星期一之后的第一个星期二:" + electionDate.toString());
    }

    public static DateTime getDateTime() {
        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        LOGGER.info("getDateTime:" + dateTime);
        return dateTime;
    }

    public static Date convertByTimeZone(Date date, String timeZone) {
        if (null == timeZone) {
            timeZone = "GMT+00";
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sf.setTimeZone(TimeZone.getTimeZone(timeZone));
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = sf1.parse(sf.format(date));
            LOGGER.info("convertByTimeZone:" + newDate);
        } catch (ParseException e) {
            LOGGER.error("ParseException: {}", e);
        }
        return newDate;
    }

    public static void main(String[] args) {
        calNovemberFirstMondyAfterFirstTuesday();
        DateTime dt = getDateTime();
        convertByTimeZone(new Date(), "GMT+04");

        LOGGER.info("Tomorrow：" + dt.plusDays(1).toLocalDate());
        LOGGER.info("Yesterday：" + dt.minusDays(1).toLocalDate());
    }
}
