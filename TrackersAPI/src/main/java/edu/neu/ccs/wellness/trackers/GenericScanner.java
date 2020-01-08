package edu.neu.ccs.wellness.trackers;

import android.bluetooth.le.ScanCallback;

/**
 * Created by hermansaksono on 7/29/18.
 */

public interface GenericScanner {
    /**
     * Start Bluetooth LE devices scan, then perform the callback on each discovered devices.
     *
     * @param callback
     */
    void startScan(ScanCallback callback);

    /**
     * Stop Bluetooth LE devices scan.
     * @param callback
     */
    void stopScan(ScanCallback callback);
}
