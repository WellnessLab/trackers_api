package edu.neu.ccs.wellness.trackers.miband2.model;

import java.util.UUID;

public class Profile {
    public static final int BLUETOOTH_STATE_133 = 133;
    public static final int BLUETOOTH_STATE_129 = 129;
    // ========================== 服务部分 ============================
    /**
     * 主要的service
     */
    public static final UUID UUID_SERVICE_MIB1 = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_MIB2 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_MILI = UUID_SERVICE_MIB1;
    public static final UUID UUID_CHAR_CONTROL_POINT = UUID.fromString("0000ff05-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CURRENT_TIME = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_1_SENSOR = UUID.fromString("00000001-0000-3512-2118-0009AF100700");
    public static final UUID UUID_CHAR_3_CONFIGURATION = UUID.fromString("00000003-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_4_FETCH = UUID.fromString("00000004-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_5_ACTIVITY = UUID.fromString("00000005-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_6_BATTERY = UUID.fromString("00000006-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_7_REALTIME_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_8_USER_SETTING = UUID.fromString("00000008-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_9_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHAR_10_DEVICEEVENT = UUID.fromString("00000010-0000-3512-2118-0009af100700");


    /**
     * 震动
     */
    public static final UUID UUID_SERVICE_VIBRATION = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");


    /**
     * Heart Rate
     */
    public static final UUID UUID_SERVICE_HEARTRATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_NOTIFICATION_HEARTRATE = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHAR_HEARTRATE = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");


    /**
     * 未知作用
     */
    public static final UUID UUID_SERVICE_UNKNOWN1 = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_UNKNOWN2 = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_UNKNOWN4 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_UNKNOWN5 = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    // ========================== 服务部分 end ============================

    // ========================== 描述部分 ============================
    public static final UUID UUID_DESCRIPTOR_UPDATE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"); //!!

    // ========================== 描述部分 end ============================

    // ========================== 特性部分 ============================
    public static final UUID UUID_CHAR_DEVICE_INFO = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHAR_DEVICE_NAME = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    /**
     * 通用通知
     */
    public static final UUID UUID_CHAR_NOTIFICATION = UUID.fromString("0000ff03-0000-1000-8000-00805f9b34fb");

    /**
     * 用户信息，读写
     */
    public static final UUID UUID_CHAR_USER_INFO = UUID.fromString("0000ff04-0000-1000-8000-00805f9b34fb");

    /**
     * 控制,如震动等
     */
    /**
     * 电池,只读,通知
     */
    //public static final UUID UUID_CHAR_6_BATTERY = UUID.fromString("0000ff0c-0000-1000-8000-00805f9b34fb");

    /**
     * 实时步数通知 通知
     */




    public static final UUID UUID_CHAR_FIRMWARE_DATA = UUID.fromString("0000ff08-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHAR_LE_PARAMS = UUID.fromString("0000ff09-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHAR_DATA_TIME = UUID.fromString("0000ff0a-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHAR_STATISTICS = UUID.fromString("0000ff0b-0000-1000-8000-00805f9b34fb");

    /**
     * 自检,读写
     */
    public static final UUID UUID_CHAR_TEST = UUID.fromString("0000ff0d-0000-1000-8000-00805f9b34fb");

    /**
     * 配对,读写
     */
    public static final UUID UUID_CHAR_SENSOR_DATA = UUID.fromString("0000ff0e-0000-1000-8000-00805f9b34fb");

    /**
     * 配对,读写
     */
    public static final UUID UUID_CHAR_PAIR = UUID.fromString("0000ff0f-0000-1000-8000-00805f9b34fb");

    /**
     * 震动
     */
    public static final UUID UUID_CHAR_VIBRATION = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");
    // ========================== 特性部分 end ============================
}
