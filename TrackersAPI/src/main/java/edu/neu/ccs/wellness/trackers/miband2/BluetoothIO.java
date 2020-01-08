package edu.neu.ccs.wellness.trackers.miband2;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import edu.neu.ccs.wellness.trackers.callback.ActionCallback;
import edu.neu.ccs.wellness.trackers.callback.NotifyListener;
import edu.neu.ccs.wellness.trackers.miband2.model.Profile;

import java.util.HashMap;
import java.util.UUID;

class BluetoothIO extends BluetoothGattCallback {
    private static final int CONNECTION_DELAY = 600;
    private static final int DISCOVER_DELAY = 1000;
    private static final String TAG = "BluetoothIO";
    protected BluetoothGatt gatt;
    private ActionCallback currentCallback;

    private HashMap<UUID, NotifyListener> notifyListeners = new HashMap<UUID, NotifyListener>();
    private NotifyListener disconnectedListener = null;

    // Special variables for reconnecting only.
    private Handler connectHandler = new Handler();
    private Handler discoverHandler = new Handler();
    private Context contextForReconnecting;
    private BluetoothDevice bleDeviceForReconnecting;
    private BluetoothGattCallback bluetoothGattCallback = this;

    public void connect(
            final Context context, final BluetoothDevice device, final ActionCallback callback) {
        this.currentCallback = callback;
        this.contextForReconnecting = context;
        this.bleDeviceForReconnecting = device;
        startDelayedConnection();
    }

    public void disconnect() {
        if (this.gatt == null) {
            return;
        }
        this.gatt.disconnect();
        this.gatt.close();
        this.gatt = null;
        this.currentCallback = null;
        this.notifyListeners = null;
        this.disconnectedListener = null;
        this.contextForReconnecting = null;
        this.bleDeviceForReconnecting = null;
    }

    public boolean isConnected() {
        return this.gatt != null;
    }

    public void setDisconnectedListener(NotifyListener disconnectedListener) {
        this.disconnectedListener = disconnectedListener;
    }

    /* Handlers */
    private Runnable delayedConnectRunnable = new Runnable() {
        @Override
        public void run() {
            bleDeviceForReconnecting.connectGatt(contextForReconnecting, false,
                    bluetoothGattCallback);
        }
    };

    private void startDelayedConnection() {
        this.connectHandler.removeCallbacks(delayedConnectRunnable);
        this.connectHandler.postDelayed(delayedConnectRunnable, CONNECTION_DELAY);
    }

    private Runnable delayedDiscoverRunnable = new Runnable() {
        @Override
        public void run() {
            if (gatt != null) {
                gatt.discoverServices();
            }
        }
    };

    private void startDelayedDiscovery() {
        this.discoverHandler.removeCallbacks(delayedDiscoverRunnable);
        this.discoverHandler.postDelayed(delayedDiscoverRunnable, DISCOVER_DELAY);
    }

    /* BLE Connection Methods */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        // Handle the weird status #133 and #126
        switch (status) {
            case Profile.BLUETOOTH_STATE_133:
                gatt.close();
                startDelayedConnection();
                return;
            case Profile.BLUETOOTH_STATE_129:
                startDelayedDiscovery();
                return;
        }

        // Handle regular status
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                gatt.discoverServices();
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                gatt.close();
                if (this.disconnectedListener != null)
                    this.disconnectedListener.onNotify(null);
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            this.gatt = gatt;
            this.onSuccess(null);
        } else {
            this.onFail(status, "onServicesDiscovered fail");
        }
    }

    public BluetoothDevice getDevice() {
        if (null == gatt) {
            Log.e(TAG, "connect to miband first");
            return null;
        }
        return gatt.getDevice();
    }

    public void writeAndRead(final UUID uuid, byte[] valueToWrite, final ActionCallback callback) {
        ActionCallback readCallback = new ActionCallback() {

            @Override
            public void onSuccess(Object characteristic) {
                BluetoothIO.this.readCharacteristic(uuid, callback);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                callback.onFail(errorCode, msg);
            }
        };
        this.writeCharacteristic(uuid, valueToWrite, readCallback);
    }

    public void writeCharacteristic(UUID characteristicUUID, byte[] value, ActionCallback callback) {
        writeCharacteristic(Profile.UUID_SERVICE_MILI, characteristicUUID, value, callback);
    }

    public void writeCharacteristic(UUID serviceUUID, UUID characteristicUUID, byte[] value, ActionCallback callback) {
        try {
            if (null == gatt) {
                Log.e(TAG, "connect to miband first");
                throw new Exception("connect to miband first");
            }
            this.currentCallback = callback;
            BluetoothGattCharacteristic chara = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID);
            if (null == chara) {
                this.onFail(-1, "BluetoothGattCharacteristic " + characteristicUUID + " is not exsit");
                return;
            }
            chara.setValue(value);
            if (false == this.gatt.writeCharacteristic(chara)) {
                this.onFail(-1, "gatt.writeCharacteristic() return false");
            }
        } catch (Throwable tr) {
            Log.e(TAG, "writeCharacteristic", tr);
            this.onFail(-1, tr.getMessage());
        }
    }

    public void readCharacteristic(UUID serviceUUID, UUID uuid, ActionCallback callback) {
        Log.i(TAG, "Read characteristic from service: " + serviceUUID + ", uuid: " + uuid);
        try {
            if (null == gatt) {
                Log.e(TAG, "connect to miband first");
                throw new Exception("connect to miband first");
            }
            this.currentCallback = callback;
            BluetoothGattCharacteristic chara = gatt.getService(serviceUUID).getCharacteristic(uuid);
            if (null == chara) {
                this.onFail(-1, "BluetoothGattCharacteristic " + uuid + " does not exist");
                return;
            }
            if (false == this.gatt.readCharacteristic(chara)) {
                this.onFail(-1, "gatt.readCharacteristic() return false");
            }
        } catch (Throwable tr) {
            Log.e(TAG, "readCharacteristic", tr);
            this.onFail(-1, tr.getMessage());
        }
    }

    public void readCharacteristic(UUID uuid, ActionCallback callback) {
        this.readCharacteristic(Profile.UUID_SERVICE_MILI, uuid, callback);
    }

    public void readRssi(ActionCallback callback) {
        try {
            if (null == gatt) {
                Log.e(TAG, "connect to miband first");
                throw new Exception("connect to miband first");
            }
            this.currentCallback = callback;
            this.gatt.readRemoteRssi();
        } catch (Throwable tr) {
            Log.e(TAG, "readRssi", tr);
            this.onFail(-1, tr.getMessage());
        }

    }

    public void setNotifyListener(UUID serviceUUID, UUID characteristicId, NotifyListener listener) {
        if (null == gatt) {
            Log.e(TAG, "connect to miband first");
            return;
        }

        BluetoothGattCharacteristic chara = gatt.getService(serviceUUID).getCharacteristic(characteristicId);
        if (chara == null) {
            Log.e(TAG, "characteristicId " + characteristicId.toString() + " not found in service " + serviceUUID.toString());
            return;
        }

        this.gatt.setCharacteristicNotification(chara, true);
        BluetoothGattDescriptor descriptor = chara.getDescriptor(Profile.UUID_DESCRIPTOR_UPDATE_NOTIFICATION);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        this.gatt.writeDescriptor(descriptor);
        this.notifyListeners.put(characteristicId, listener);
    }

    public void stopNotifyListener(UUID serviceUUID, UUID characteristicId) {
        if (this.notifyListeners.containsKey(characteristicId)) {
            Log.d(TAG, String.format("Stopping listener on %s.", characteristicId.toString()));
            this.notifyListeners.remove(characteristicId);
        } else {
            Log.d(TAG, String.format("Listener on %s doesn't exist.", characteristicId.toString()));
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            this.onSuccess(characteristic);
        } else {
            this.onFail(status, "onCharacteristicRead fail");
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            this.onSuccess(characteristic);
        } else {
            this.onFail(status, "onCharacteristicWrite fail");
        }
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            this.onSuccess(rssi);
        } else {
            this.onFail(status, "onCharacteristicRead fail");
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        if (this.notifyListeners.containsKey(characteristic.getUuid())) {
            this.notifyListeners.get(characteristic.getUuid()).onNotify(characteristic.getValue());
        }
    }

    private void onSuccess(Object data) {
        if (this.currentCallback != null) {
            ActionCallback callback = this.currentCallback;
            this.currentCallback = null;
            callback.onSuccess(data);
        }
    }

    private void onFail(int errorCode, String msg) {
        if (this.currentCallback != null) {
            ActionCallback callback = this.currentCallback;
            this.currentCallback = null;
            callback.onFail(errorCode, msg);
        }
    }

}
