package edu.neu.ccs.wellness.trackers.miband2.model;

import android.bluetooth.BluetoothGattCharacteristic;

import edu.neu.ccs.wellness.trackers.BatteryInfo;
import edu.neu.ccs.wellness.trackers.miband2.utils.CalendarUtils;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 手环电池相关信息类
 */
public class MiBand2BatteryInfo implements BatteryInfo {
    /**
     * 电池当前所在的状态
     */
    static enum Status {
        UNKNOWN, LOW, FULL, CHARGING, NOT_CHARGING, NORMAL;

        public static Status fromByte(byte b) {
            switch (b) {
                case 0:
                    return NORMAL;
                default:
                    return UNKNOWN;
            }
        }
    }

    private int level;
    private Status status;
    private Calendar lastChargedDate;
    private Calendar lastOffDate;

    private MiBand2BatteryInfo() {

    }

    public static MiBand2BatteryInfo fromByteData(byte[] data) {
        if (data.length < 2) {
            return null;
        }
        MiBand2BatteryInfo info = new MiBand2BatteryInfo();

        info.level = data[1];
        info.status = Status.fromByte(data[2]);
        info.lastChargedDate = CalendarUtils.bytesToCalendar(Arrays.copyOfRange(data, 11, data.length));
        info.lastOffDate = CalendarUtils.bytesToCalendar(Arrays.copyOfRange(data, 3, data.length));

        return info;
    }

    public String toString() {
        return "level: " + this.getLevel() + "%"
                + ", status: " + this.getStatus()
                + ", last charged: " + this.getLastChargedDate().getTime().toString();
    }

    /**
     * 电池电量百分比, level=40 表示有40%的电量
     */
    public int getLevel() {
        return level;
    }

    /**
     * 当前状态
     *
     * @see Status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 最后充电时间
     */
    public Calendar getLastChargedDate() {
        return lastChargedDate;
    }

    public Calendar getLastOffDate() {
        return lastOffDate;
    }

    public static boolean isBatteryInfo(BluetoothGattCharacteristic characteristics) {
        return characteristics.getValue().length >= 2;
    }
}
