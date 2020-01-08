package edu.neu.ccs.wellness.trackers.miband2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.neu.ccs.wellness.trackers.DeviceProfile;
import edu.neu.ccs.wellness.trackers.GenericScanner;

/**
 * Created by hermansaksono on 7/29/18.
 */

public class MiBandScanner implements GenericScanner {

    private static final String TAG = "mi-band-scanner";

    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner scanner;
    private List<ScanFilter> scanFilterList;
    private ScanSettings scanSettings;

    /* CONSTRUCTOR */

    /**
     * Initializes the MiBand 2's BLE scanner. Only looks for devices with name = "MI Band 2".
     */
    public MiBandScanner(Context context) {
        this.scanFilterList = getScanFilterList();
        this.scanSettings = getScanSetting(ScanSettings.SCAN_MODE_LOW_POWER);
        this.bluetoothManager = (BluetoothManager)
                context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    /**
     * Initializes the MiBand 2's BLE scanner. Only looks for devices with name = "MI Band 2" and
     * address that has a match in the address list. The scanner will run in low-power scan mode
     * (i.e., {@link ScanSettings}.SCAN_MODE_LOW_POWER.
     * @param profileList List of {@link DeviceProfile} that the scanner should look for.
     */
    public MiBandScanner(List<DeviceProfile> profileList) {
        this.scanFilterList = getScanFilterList(profileList);
        this.scanSettings = getScanSetting(ScanSettings.SCAN_MODE_LOW_POWER);
    }

    /**
     * Initializes the MiBand 2's BLE scanner. Only looks for devices with name = "MI Band 2" and
     * address that has a match in the address list. The scanner will run in user defined scan mode.
     * @param profileList List of {@link DeviceProfile} that the scanner should look for.
     * @param scanMode Scan mode as defined in {@link ScanSettings}.
     */
    public MiBandScanner(List<DeviceProfile> profileList, int scanMode) {
        this.scanFilterList = getScanFilterList(profileList);
        this.scanSettings = getScanSetting(scanMode);
    }

    /**
     * Start MiBand 2 BLE devices scan, then perform the callback on each discovered devices.
     *
     * @param callback
     */
    @Override
    public void startScan(ScanCallback callback) {
        // BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter adapter = bluetoothManager.getAdapter();

        if (null == adapter) {
            Log.e(TAG, "BluetoothAdapter is null");
            return;
        }
        this.scanner = adapter.getBluetoothLeScanner();
        if (null == this.scanner) {
            Log.e(TAG, "BluetoothLeScanner is null");
            return;
        }
        Log.v(TAG, "Starting MiBand 2 tracker search.");
        Log.v(TAG, "Scan filters: " + getBluetoothAddresses(this.scanFilterList).toString());
        this.scanner.startScan(this.scanFilterList, this.scanSettings, callback);
    }

    /**
     * Stop MiBand 2 BLE devices scan.
     * @param callback
     */
    @Override
    public void stopScan(ScanCallback callback) {
        if (null == bluetoothManager.getAdapter()) {
            Log.e(TAG, "Stopping MiBand 2 tracker search failed. BluetoothAdapter is null");
            return;
        }
        if (null != this.scanner) {
            Log.d(TAG, "Stopping MiBand 2 tracker search.");
            this.scanner.stopScan(callback);
            this.scanner = null;
        }
    }

    /* STATIC METHODS */
    /**
     * Determines whether Bluetooth is available.
     * @return
     */
    public static boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return (adapter != null && adapter.isEnabled());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MiBandScanner");
        sb.append("\n\tFilters: ");
        sb.append(this.scanFilterList.toString());
        sb.append("\n\tSetting: ");
        sb.append(this.scanSettings.toString());
        return sb.toString();
    }

    /* HELPER METHODS */
    private ScanSettings getScanSetting(int scanMode) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        //builder.setNumOfMatches(numDevices);
        //builder.setScanMode(scanMode);
        return builder.build();
    }

    private List<ScanFilter> getScanFilterList() {
        List<ScanFilter> scanFilterList = new ArrayList<>();
        scanFilterList.add(getScanFilter());
        return scanFilterList;
    }

    private static List<ScanFilter> getScanFilterList(List<DeviceProfile> profileList) {
        List<ScanFilter> scanFilterList = new ArrayList<>();
        for (DeviceProfile profile : profileList) {
            scanFilterList.add(getScanFilterWithAddress(profile.getAddress()));
        }
        return scanFilterList;
    }

    private static ScanFilter getScanFilter() {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceName(MiBand.DEVICE_NAME);
        return builder.build();
    }

    private static ScanFilter getScanFilterWithAddress(String address) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceName(MiBand.DEVICE_NAME);
        builder.setDeviceAddress(address);
        return builder.build();
    }

    private static List<String> getBluetoothAddresses(List<ScanFilter> scanFilters) {
        List<String> addresses = new ArrayList<>();
        for (ScanFilter scanFilter : scanFilters) {
            addresses.add(scanFilter.getDeviceAddress());
        }
        return addresses;
    }
}
