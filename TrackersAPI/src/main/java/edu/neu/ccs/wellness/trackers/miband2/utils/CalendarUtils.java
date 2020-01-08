package edu.neu.ccs.wellness.trackers.miband2.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by hermansaksono on 6/21/18.
 */

public class CalendarUtils {

    public static Date bytesToDate(byte[] data) {
        return bytesToCalendar(data).getTime();
    }

    public static Calendar bytesToCalendar(byte[] data) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, TypeConversionUtils.byteToInt(data[0])
                + (TypeConversionUtils.byteToInt(data[1]) * 256));
        calendar.set(Calendar.MONTH, TypeConversionUtils.byteToInt(data[2]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, TypeConversionUtils.byteToInt(data[3]));
        calendar.set(Calendar.HOUR_OF_DAY, TypeConversionUtils.byteToInt(data[4]));
        calendar.set(Calendar.MINUTE, TypeConversionUtils.byteToInt(data[5]));
        calendar.set(Calendar.SECOND, TypeConversionUtils.byteToInt(data[6]));
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getDurationInMinutes(Calendar pre, Calendar post) {
        long diffInMillisec = Math.abs(post.getTime().getTime() - pre.getTime().getTime());
        return TimeUnit.MINUTES.convert(diffInMillisec, TimeUnit.MILLISECONDS);
    }

    public static Calendar getRoundedMinutes(Calendar cal) {
        Calendar rounded = Calendar.getInstance();
        rounded.setTime(cal.getTime());
        rounded.set(Calendar.SECOND, 0);
        rounded.set(Calendar.MILLISECOND, 0);
        return rounded;
    }
}
