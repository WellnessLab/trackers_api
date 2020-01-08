package edu.neu.ccs.wellness.trackers.miband2.operations;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import edu.neu.ccs.wellness.trackers.miband2.MiBand2Profile;
import edu.neu.ccs.wellness.trackers.callback.ActionCallback;
import edu.neu.ccs.wellness.trackers.miband2.MiBand;
import edu.neu.ccs.wellness.trackers.callback.NotifyListener;

/**
 * Created by hermansaksono on 6/22/18.
 */

public class MonitorSensorData {

    private static final int BTLE_DELAY_MODERATE = 1000;

    private BluetoothDevice device;
    private MiBand miBand;
    private MiBand2Profile profile;
    private NotifyListener listener;

    private Handler handler = new android.os.Handler();

    public void connect(Context context, MiBand2Profile profile, NotifyListener listener) {
        this.miBand = new MiBand();
        this.profile = profile;
        this.listener = listener;
        this.startScanAndMonitorSteps();
    }

    public void disconnect() {
        if (this.miBand != null) {
            this.miBand.disableSensorDataNotify();
            this.miBand.disconnect();
            this.miBand = null;
        }
    }

    final ScanCallback scanCallback = new ScanCallback(){
        @Override
        public void onScanResult(int callbackType, ScanResult result){
            device = result.getDevice();
            if (MiBand.isThisTheDevice(device, profile)) {
                connectToMiBand(device);
                MiBand.publishDeviceFound(device, result);
            }
        }
    };

    private void startScanAndMonitorSteps() {
        //MiBand.startScan(scanCallback);
    }

    private void connectToMiBand(BluetoothDevice device) {
        this.miBand.connect(device, null, new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                Log.d("SWELL","connect success");
                //MiBand.stopScan(scanCallback);
                startMonitorSensorData();
            }
            @Override
            public void onFail(int errorCode, String msg){
                Log.d("SWELL","connect fail, code:"+errorCode+",mgs:"+msg);
            }
        });
    }

    private void startMonitorSensorData() {
        disableOneTimeHeartRateSensor();
    }

    private void disableOneTimeHeartRateSensor() {
        this.miBand.disableOneTimeHeartRateSensor();
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                disableContinuousHeartRateSensor();
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void disableContinuousHeartRateSensor() {
        this.miBand.disableContinuousHeartRateSensor();
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableAccelerometerSensor();
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void enableAccelerometerSensor() {
        this.miBand.enableAccelerometerSensor();
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableAccelerometerNotifications();
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void enableAccelerometerNotifications() {
        this.miBand.enableAccelerometerNotifications(new NotifyListener() {
            @Override
            public void onNotify(byte[] data) { listener.onNotify(data);
            }
        });
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableContinuousHeartRateSensor();
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void enableContinuousHeartRateSensor() {
        this.miBand.startHeartRateNotifications();
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startFetching();
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void startFetching(){
        this.miBand.startSensingNow();
    }
}
