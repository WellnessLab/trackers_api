package edu.neu.ccs.wellness.trackers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.Calendar;

import edu.neu.ccs.wellness.trackers.callback.ActionCallback;
import edu.neu.ccs.wellness.trackers.callback.BatteryInfoCallback;
import edu.neu.ccs.wellness.trackers.callback.NotifyListener;

/**
 * Created by hermansaksono on 7/25/18.
 */

public interface GenericTrackingDevice {

    /* CONNECTING AND DISCONNECTING */
    /** Connect to a specific device.
     * @param device The {@link BluetoothDevice} to be connected.
     * @param context The {@link Context} that will assist the connection.
     * @param callback An {@link ActionCallback} that is executed after the device is connected.
     */
    void connect(BluetoothDevice device, Context context, final ActionCallback callback);

    /**
     * Disconnect the currently connected device.
     */
    void disconnect();

    /**
     * Set the disconnected listener..
     */
    void setDisconnectedListener(NotifyListener disconnectedListener);

    /* AUTHENTICATION AND PAIRING */
    /**
     * Perform Auth initialization on the currently connected device.
     * @param callback An {@link ActionCallback} that is executed after the device has been paired.
     */
    void auth(final ActionCallback callback);

    /**
     * Perform Bluetooth pairing on the currently connected device.
     * @param callback An {@link ActionCallback} that is executed after the device has been paired.
     */
    void pair(final ActionCallback callback);

    /* BASIC OPERATIONS */
    /**
     * Reading the device's signal strength RSSI value
     * @param callback An {@link ActionCallback} that handles the returned RSSI value.
     */
    void readRssi(ActionCallback callback);

    /**
     * Prints Services and Characteristics available on the connected device.
     */
    void showServicesAndCharacteristics();

    /**
     * Read device's Battery information
     */
    void getBatteryInfo(final BatteryInfoCallback batteryInfoCallback);

    /**
     * Set the device's current date and time.
     * @param calendar A {@link Calendar} object that indicates the date and time for the device.
     * @param callback An {@link ActionCallback} listener that handles notification on date change.
     */
    void setTime(Calendar calendar, final ActionCallback callback);

    /**
     * Get the device's current time.
     * @param callback An {@link ActionCallback} listener that handles notification on the date.
     */
    void getCurrentTime(final ActionCallback callback);

    /**
     * Sets user information
     * @param userInfo A {@link UserInfo} object that describes the user.
     */
    void setUserInfo(UserInfo userInfo, final ActionCallback callback);

    /**
     * Do one vibration on the device.
     */
    void doOneVibration();


}
