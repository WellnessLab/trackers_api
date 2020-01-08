package edu.neu.ccs.wellness.trackers.miband2.model;

public class Protocol {
    public static final byte[] PAIR = {2};
    public static final byte[] VIBRATION_MESSAGE = {1};
    public static final byte[] VIBRATION_PHONE = {2};
    public static final byte[] VIBRATION_ONLY = {3};
    public static final byte[] VIBRATION_WITHOUT_LED = {4};
    public static final byte[] STOP_VIBRATION = {0};
    public static final byte[] ENABLE_REALTIME_STEPS_NOTIFY = {3, 1};
    public static final byte[] DISABLE_REALTIME_STEPS_NOTIFY = {3, 0};
    public static final byte[] SET_COLOR_RED = {14, 6, 1, 2, 1};
    public static final byte[] SET_COLOR_BLUE = {14, 0, 6, 6, 1};
    public static final byte[] SET_COLOR_ORANGE = {14, 6, 2, 0, 1};
    public static final byte[] SET_COLOR_GREEN = {14, 4, 5, 0, 1};
    public static final byte[] START_HEART_RATE_SCAN = {21, 2, 1};
    public static final byte[] STOP_HEART_RATE_SCAN = {21, 2, 0};

    public static final byte[] STOP_ONE_TIME_HEART_RATE = {0x15, 0x02, 0x00};
    public static final byte[] START_REALTIME_HEART_RATE = {0x15, 0x01, 0x01};
    public static final byte[] STOP_REALTIME_HEART_RATE = {0x15, 0x01, 0x00};
    public static final byte[] ENABLE_SENSOR_DATA_NOTIFY = {0x01, 0x03, 0x19};//{18, 1};
    public static final byte[] DISABLE_SENSOR_DATA_NOTIFY = {0x00, 0x03, 0x19};//{18, 0};
    public static final byte[] ENABLE_HEART_RATE_NOTIFY = {0x01, 0x00};
    public static final byte[] START_SENSOR_FETCH = {0x02};

    public static final byte[] COMMAND_ACTIVITY_PARAMS = {0x01, 0x01};
    public static final byte[] COMMAND_ACTIVITY_FETCH = {0x02}; // previously 0x06?

    public static final byte COMMAND_SET_USERINFO = 0x4f;

    public static final byte[] REBOOT = {12};
    public static final byte[] REMOTE_DISCONNECT = {1};
    public static final byte[] FACTORY_RESET = {9};
    public static final byte[] SELF_TEST = {2};

    public static final byte AUTH_SEND_KEY = 0x01;
    public static final byte AUTH_REQUEST_RANDOM_AUTH_NUMBER = 0x02;
    public static final byte AUTH_SEND_ENCRYPTED_AUTH_NUMBER = 0x03;
    public static final byte AUTH_RESPONSE = 0x10;
    public static final byte AUTH_SUCCESS = 0x01;
    public static final byte AUTH_FAIL = 0x04;
    public static final byte AUTH_BYTE = 0x08;

    public static final byte[] COMMAND_AUTH_SEND_KEY = {AUTH_SEND_KEY, AUTH_BYTE};
    public static final byte[] COMMAND_REQUEST_RANDOM_AUTH_NUMBER = {AUTH_REQUEST_RANDOM_AUTH_NUMBER, AUTH_BYTE};
    public static final byte[] COMMAND_SEND_ENCRYPTED_AUTH_NUMBER = {AUTH_SEND_ENCRYPTED_AUTH_NUMBER, AUTH_BYTE};
    /* DEVICE SETTINGS */
    public static byte ENDPOINT_DISPLAY = 0x06;
    public static byte ENDPOINT_DISPLAY_ITEMS = 0x0a;
    public static byte DISPLAY_ITEM_BIT_CLOCK = 0x01;
    public static byte DISPLAY_ITEM_BIT_STEPS = 0x02;
    public static byte DISPLAY_ITEM_BIT_DISTANCE = 0x04;
    public static byte DISPLAY_ITEM_BIT_CALORIES= 0x08;
    public static byte DISPLAY_ITEM_BIT_HEART_RATE = 0x10;
    public static byte DISPLAY_ITEM_BIT_BATTERY = 0x20;
    public static int SCREEN_CHANGE_BYTE = 1;
    public static byte ENDPOINT_DND = 0x09;

    public static final byte[] DATEFORMAT_DATE_TIME = new byte[] {ENDPOINT_DISPLAY, 0x0a, 0x0, 0x03 };
    public static final byte[] DATEFORMAT_TIME = new byte[] {ENDPOINT_DISPLAY, 0x0a, 0x0, 0x0 };
    public static final byte[] DATEFORMAT_TIME_12_HOURS = new byte[] {ENDPOINT_DISPLAY, 0x02, 0x0, 0x0 };
    public static final byte[] DATEFORMAT_TIME_24_HOURS = new byte[] {ENDPOINT_DISPLAY, 0x02, 0x0, 0x1 };

    public static final byte[] COMMAND_WEAR_LOCATION_LEFT_WRIST = new byte[] { 0x20, 0x00, 0x00, 0x02 };
    public static final byte[] COMMAND_WEAR_LOCATION_RIGHT_WRIST = new byte[] { 0x20, 0x00, 0x00, (byte) 0x82};
    public static final byte[] COMMAND_ENABLE_GOAL_NOTIFICATION = new byte[]{ENDPOINT_DISPLAY, 0x06, 0x00, 0x01};
    public static final byte[] COMMAND_DISABLE_GOAL_NOTIFICATION = new byte[]{ENDPOINT_DISPLAY, 0x06, 0x00, 0x00};
    public static final byte[] COMMAND_ENABLE_DISPLAY_ON_LIFT_WRIST = new byte[]{ENDPOINT_DISPLAY, 0x05, 0x00, 0x01};
    public static final byte[] COMMAND_DISABLE_DISPLAY_ON_LIFT_WRIST = new byte[]{ENDPOINT_DISPLAY, 0x05, 0x00, 0x00};
    public static final byte[] COMMAND_ENABLE_ROTATE_WRIST_TO_SWITCH_INFO = new byte[]{ENDPOINT_DISPLAY, 0x0d, 0x00, 0x01};
    public static final byte[] COMMAND_DISABLE_ROTATE_WRIST_TO_SWITCH_INFO = new byte[]{ENDPOINT_DISPLAY, 0x0d, 0x00, 0x00};
    public static final byte[] COMMAND_ENABLE_DISPLAY_CALLER = new byte[]{ENDPOINT_DISPLAY, 0x10, 0x00, 0x00, 0x01};
    public static final byte[] COMMAND_DISABLE_DISPLAY_CALLER = new byte[]{ENDPOINT_DISPLAY, 0x10, 0x00, 0x00, 0x00};

    public static final byte[] COMMAND_DO_NOT_DISTURB_AUTOMATIC = new byte[] { ENDPOINT_DND, (byte) 0x83 };
    public static final byte[] COMMAND_DO_NOT_DISTURB_OFF = new byte[] { ENDPOINT_DND, (byte) 0x82 };
    public static final byte[] COMMAND_DO_NOT_DISTURB_SCHEDULED = new byte[] { ENDPOINT_DND, (byte) 0x81, 0x01, 0x00, 0x06, 0x00 };
    public static final byte[] COMMAND_CHANGE_SCREENS = new byte[]{ENDPOINT_DISPLAY_ITEMS, DISPLAY_ITEM_BIT_CLOCK, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05};

    public static int INACTIVITY_WARNINGS_THRESHOLD = 2;
    public static int INACTIVITY_WARNINGS_INTERVAL_1_START_HOURS = 4;
    public static int INACTIVITY_WARNINGS_INTERVAL_1_START_MINUTES = 5;
    public static int INACTIVITY_WARNINGS_INTERVAL_1_END_HOURS = 6;
    public static int INACTIVITY_WARNINGS_INTERVAL_1_END_MINUTES = 7;
    public static int INACTIVITY_WARNINGS_INTERVAL_2_START_HOURS = 8;
    public static int INACTIVITY_WARNINGS_INTERVAL_2_START_MINUTES = 9;
    public static int INACTIVITY_WARNINGS_INTERVAL_2_END_HOURS = 10;
    public static int INACTIVITY_WARNINGS_INTERVAL_2_END_MINUTES = 11;
    public static final byte[] COMMAND_ENABLE_INACTIVITY_WARNINGS = new byte[] { 0x08, 0x01, 0x3c, 0x00, 0x04, 0x00, 0x15, 0x00, 0x00, 0x00, 0x00, 0x00 };
    public static final byte[] COMMAND_DISABLE_INACTIVITY_WARNINGS = new byte[] { 0x08, 0x00, 0x3c, 0x00, 0x04, 0x00, 0x15, 0x00, 0x00, 0x00, 0x00, 0x00 };
    
    public static final byte[] COMMAND_DISTANCE_UNIT_METRIC = new byte[] { ENDPOINT_DISPLAY, 0x03, 0x00, 0x00 };
    public static final byte[] COMMAND_DISTANCE_UNIT_IMPERIAL = new byte[] { ENDPOINT_DISPLAY, 0x03, 0x00, 0x01 };

}
