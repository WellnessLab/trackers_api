package edu.neu.ccs.wellness.trackers.miband2;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.util.Log;

import java.util.Arrays;

import edu.neu.ccs.wellness.trackers.callback.ActionCallback;
import edu.neu.ccs.wellness.trackers.callback.NotifyListener;
import edu.neu.ccs.wellness.trackers.miband2.model.MiBand2BatteryInfo;
import edu.neu.ccs.wellness.trackers.miband2.model.GattCharacteristics;
import edu.neu.ccs.wellness.trackers.miband2.model.Profile;

/**
 * Created by hermansaksono on 7/9/18.
 */

public class OperationPostPairing {
    private String TAG = "mi-band-post-pair";
    private BluetoothIO io;
    private Handler handler;
    private boolean isCompleted;

    public OperationPostPairing(Handler handler) {
        this.handler = handler;
    }

    public OperationPostPairing() {
        this.handler = new Handler();
    }

    public void perform(BluetoothIO io) {
        this.io = io;
        this.isCompleted = false;
        this.getSerialNumber();
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    private void getManufacturer() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_MANUFACTURER_NAME_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.e(TAG, "getManufacturer Success " + data.toString());
                        //getModelNumber();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getModelNumber() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_MODEL_NUMBER_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.e(TAG, "getModelNumber Success " + data.toString());
                        //getSerialNumber();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getSerialNumber() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_SERIAL_NUMBER_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                        Log.i(TAG, "getSerialNumber Success " + Arrays.toString(characteristic.getValue()));
                        getHardwareRevisionString();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getHardwareRevisionString() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                        Log.i(TAG, "getHardwareRevisionString Success " + Arrays.toString(characteristic.getValue()));
                        getSoftwareRevisionString();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getFirmwareRevisionString() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.i(TAG, "getFirmwareRevisionString Success " + data.toString());
                        getSoftwareRevisionString();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getSoftwareRevisionString() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                        Log.i(TAG, "getSoftwareRevisionString Success " + Arrays.toString(characteristic.getValue()));
                        getSystemId();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getSystemId() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_SYSTEM_ID,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                        Log.i(TAG, "getSystemId Success " + Arrays.toString(characteristic.getValue()));
                        getPNP();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getRegulatoryData() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.i(TAG, "getRegulatoryData Success " + data.toString());
                        getPNP();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void getPNP() {
        this.io.readCharacteristic(GattCharacteristics.UUID_SERVICE_DEVICE_INFORMATION,
                GattCharacteristics.UUID_CHARACTERISTIC_PNP_ID,
                new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                        Log.i(TAG, "getPNP Success " + Arrays.toString(characteristic.getValue()));
                        startEnablingNotifications();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.e(TAG, msg);
                    }
                });
    }

    private void startEnablingNotifications () {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableConfigurationNotify();
            }
        }, MiBand.BTLE_DELAY_MODERATE);
    }

    private void enableConfigurationNotify() {
        this.io.setNotifyListener(Profile.UUID_SERVICE_MILI,
                Profile.UUID_CHAR_3_CONFIGURATION,
                new NotifyListener() {
                    @Override
                    public void onNotify(byte[] data) {
                        Log.i(TAG, "enableConfigurationNotify " + Arrays.toString(data));

                    }
                });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableBatteryInfoNotify();
            }
        }, MiBand.BTLE_DELAY_MODERATE);
    }

    private void enableBatteryInfoNotify() {
        this.io.setNotifyListener(Profile.UUID_SERVICE_MILI,
                Profile.UUID_CHAR_6_BATTERY,
                new NotifyListener() {
                    @Override
                    public void onNotify(byte[] data) {
                        Log.i(TAG, "enableBatteryInfoNotify  " + Arrays.toString(data));

                    }
                });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceEvent();
            }
        }, MiBand.BTLE_DELAY_MODERATE);
    }

    private void getDeviceEvent() {
        this.io.setNotifyListener(Profile.UUID_SERVICE_MILI,
                Profile.UUID_CHAR_10_DEVICEEVENT,
                new NotifyListener() {
                    @Override
                    public void onNotify(byte[] data) {
                        Log.i(TAG, "getDeviceEvent  " + Arrays.toString(data));
                    }
                });

        this.isCompleted = true;
        Log.i(TAG, "Post pairing operations completed.");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBatteryInfo();
            }
        }, MiBand.BTLE_DELAY_MODERATE);
    }

    private void getBatteryInfo() {
        ActionCallback callback = new ActionCallback() {

            @Override
            public void onSuccess(Object data) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                if (characteristic.getValue().length >= 2) {
                    MiBand2BatteryInfo info = MiBand2BatteryInfo.fromByteData(characteristic.getValue());
                    Log.i(TAG, "Battery info: " + info.toString());
                } else {
                    Log.e(TAG, "Battery info error: result format wrong");
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
                Log.e(TAG, "Battery info error: " + msg);
            }
        };
        this.io.readCharacteristic(Profile.UUID_CHAR_6_BATTERY, callback);
    }
}
