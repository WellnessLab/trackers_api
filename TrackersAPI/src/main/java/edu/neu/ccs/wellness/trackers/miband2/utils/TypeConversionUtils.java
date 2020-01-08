package edu.neu.ccs.wellness.trackers.miband2.utils;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * FromCopyright (C) 2016-2018 Andreas Shimokawa, Carsten Pfeiffer
   Portions of this code is part of Gadgetbridge.
 */

public class TypeConversionUtils {
    public static final int TZ_FLAG_INCLUDE_DST_IN_TZ = 1;
    private static final int TIME_OFFSET = -5;

    public static int byteToInt(int unsignedShort) {
        if (unsignedShort < 0 ) {
            return 256 + unsignedShort;
        } else {
            return unsignedShort;
        }
    }

    public static byte intToByte(int integer) {
        if (integer > 127) {
            return (byte) (integer - 256);
        } else {
            return (byte) integer;
        }
    }

    public static int[] byteArrayToIntArray(byte[] bytes) {
        int[] intArray = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            intArray[i] = byteToInt(bytes[i]);
        }
        return intArray;
    }



    public static Integer[] byteArrayToIntegerArray(byte[] bytes) {
        Integer[] intArray = new Integer[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            intArray[i] = byteToInt(bytes[i]);
        }
        return intArray;
    }

    public static byte[] join(byte[] start, byte[] end) {
        if (start == null || start.length == 0) {
            return end;
        }
        if (end == null || end.length == 0) {
            return start;
        }

        byte[] result = new byte[start.length + end.length];
        System.arraycopy(start, 0, result, 0, start.length);
        System.arraycopy(end, 0, result, start.length, end.length);
        return result;
    }

    /**
     * Returns the given date/time (calendar) as a byte sequence, suitable for sending to the
     * Mi Band 2 (or derivative). The band appears to not handle DST offsets, so we simply add this
     * to the timezone.
     * @param calendar
     * @param precision
     * @return
     */
    public static byte[] getTimeBytes(Calendar calendar, TimeUnit precision) {
        byte[] bytes;
        if (precision == TimeUnit.MINUTES) {
            //bytes = shortCalendarToRawBytes(calendar, true);
            bytes = shortCalendarToRawBytes(calendar, false);
        } else if (precision == TimeUnit.SECONDS) {
            bytes = calendarToRawBytes(calendar, false);
        } else {
            throw new IllegalArgumentException("Unsupported precision, only MINUTES and SECONDS are supported till now");
        }
        byte[] tail = new byte[] { 0, mapTimeZone(calendar.getTimeZone(), TZ_FLAG_INCLUDE_DST_IN_TZ) };
        // 0 = adjust reason bitflags? or DST offset?? , timezone
//        byte[] tail = new byte[] { 0x2 }; // reason
        byte[] all = join(bytes, tail);
        return all;
    }
    /**
     * Converts a timestamp to the byte sequence to be sent to the current time characteristic
     *
     * @param timestamp
     * @return
     */
    public static byte[] calendarToRawBytes(Calendar timestamp, boolean honorDeviceTimeOffset) {

        // The mi-band device currently records sleep
        // only if it happens after 10pm and before 7am.
        // The offset is used to trick the device to record sleep
        // in non-standard hours.
        // If you usually sleep, say, from 6am to 2pm, set the
        // shift to -8, so at 6am the device thinks it's still 10pm
        // of the day before.
        if (honorDeviceTimeOffset) {
            int offsetInHours = getDeviceTimeOffsetHours();
            if (offsetInHours != 0) {
                timestamp.add(Calendar.HOUR_OF_DAY, offsetInHours);
            }
        }

        // MiBand2:
        // year,year,month,dayofmonth,hour,minute,second,dayofweek,0,0,tz

        byte[] year = fromUint16(timestamp.get(Calendar.YEAR));
        return new byte[] {
                year[0],
                year[1],
                fromUint8(timestamp.get(Calendar.MONTH) + 1),
                fromUint8(timestamp.get(Calendar.DATE)),
                fromUint8(timestamp.get(Calendar.HOUR_OF_DAY)),
                fromUint8(timestamp.get(Calendar.MINUTE)),
                fromUint8(timestamp.get(Calendar.SECOND)),
                dayOfWeekToRawBytes(timestamp),
                0, // fractions256 (not set)
                // 0 (DST offset?) Mi2
                // k (tz) Mi2
        };
    }

    /**
     * Similar to calendarToRawBytes, but only up to (and including) the MINUTES.
     * @param timestamp
     * @param honorDeviceTimeOffset
     * @return
     */
    public static byte[] shortCalendarToRawBytes(Calendar timestamp, boolean honorDeviceTimeOffset) {

        // The mi-band device currently records sleep
        // only if it happens after 10pm and before 7am.
        // The offset is used to trick the device to record sleep
        // in non-standard hours.
        // If you usually sleep, say, from 6am to 2pm, set the
        // shift to -8, so at 6am the device thinks it's still 10pm
        // of the day before.
        if (honorDeviceTimeOffset) {
            int offsetInHours = getDeviceTimeOffsetHours();
            if (offsetInHours != 0) {
                timestamp.add(Calendar.HOUR_OF_DAY, offsetInHours);
            }
        }

        // MiBand2:
        // year,year,month,dayofmonth,hour,minute

        byte[] year = fromUint16(timestamp.get(Calendar.YEAR));
        return new byte[] {
                year[0],
                year[1],
                fromUint8(timestamp.get(Calendar.MONTH) + 1),
                fromUint8(timestamp.get(Calendar.DATE)),
                fromUint8(timestamp.get(Calendar.HOUR_OF_DAY)),
                fromUint8(timestamp.get(Calendar.MINUTE))
        };
    }



    /**
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.time_zone.xml
     * @param timeZone
     * @return sint8 value from -48..+56
     */
    public static byte mapTimeZone(TimeZone timeZone, int timezoneFlags) {
        int offsetMillis = timeZone.getRawOffset();
        if (true && timezoneFlags == TZ_FLAG_INCLUDE_DST_IN_TZ) {
            offsetMillis += timeZone.getDSTSavings();
        }
        int utcOffsetInHours =  (offsetMillis / (1000 * 60 * 60));

        return (byte) (utcOffsetInHours * 4);
    }

    private static byte dayOfWeekToRawBytes(Calendar cal) {
        int calValue = cal.get(Calendar.DAY_OF_WEEK);
        switch (calValue) {
            case Calendar.SUNDAY:
                return 7;
            default:
                return (byte) (calValue - 1);
        }
    }

    private static int getDeviceTimeOffsetHours() {
        return TIME_OFFSET;
    }

    public static byte[] fromUint16(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
        };
    }

    public static byte[] fromUint24(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
        };
    }

    public static byte[] fromUint32(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff),
        };
    }

    public static byte fromUint8(int value) {
        return (byte) (value & 0xff);
    }
}
