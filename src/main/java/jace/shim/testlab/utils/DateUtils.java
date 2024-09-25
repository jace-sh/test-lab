package jace.shim.testlab.utils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

public final class DateUtils {

    // RFC 3339
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String DEFAULT_DAY_FORMAT = "yyyy-MM-dd";
    public static final String KST_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String KST_DATE_FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_YEAR_MONTH_FORMAT = "yyyy-MM";

    private DateUtils() {
        throw new UnsupportedOperationException("Unable to create instance");
    }

    public static String parseDefaultDateFormat(ZonedDateTime dateTime) {
        if (dateTime == null) {return null;}
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    public static String parseKstDateFormat(ZonedDateTime dateTime) {
        if (dateTime == null) {return null;}
        return dateTime.format(DateTimeFormatter.ofPattern(KST_DATE_FORMAT));
    }

    public static String parseDefaultDayFormat(ZonedDateTime dateTime) {
        if (dateTime == null) {return null;}
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DAY_FORMAT));
    }

    public static String parseDateFormat(ZonedDateTime dateTime, String format) {
        if (dateTime == null) {return null;}
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static ZonedDateTime of(String datetime) {
        if (StringUtils.isEmpty(datetime)) {return null;}
        return ZonedDateTime.parse(datetime, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    public static ZonedDateTime of(String datetime, String pattern) {
        if (StringUtils.isEmpty(datetime)) {return null;}
        LocalDateTime localdateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(pattern));
        return ZonedDateTime.of(localdateTime, ZoneId.systemDefault());
    }

    public static Timestamp convertToTimestamp(ZonedDateTime dateTime) {
        if (dateTime == null) {return null;}
        return Timestamp.valueOf(dateTime.toLocalDateTime());
    }

    public static ZonedDateTime parseDateAtStartOfDay(String date) {
        if (StringUtils.isEmpty(date)) {return null;}
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DEFAULT_DAY_FORMAT)).atStartOfDay(ZoneOffset.systemDefault());
    }

    public static ZonedDateTime parseDateAtEndOfDay(String date) {
        if (StringUtils.isEmpty(date)) {return null;}
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DEFAULT_DAY_FORMAT))
                        .atStartOfDay(ZoneOffset.systemDefault())
                        .with(LocalTime.MAX);
    }

    public static String parseDefaultYearMonthFormat(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_MONTH_FORMAT));
    }

    public static String parseDefaultDateFormat(LocalDate localDate) {
        if (localDate == null) {return null;}
        return localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DAY_FORMAT));
    }
}