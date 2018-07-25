# trackers_api
An unofficial library to communicate with health and fitness trackers. Currently this library supports MI Band 2.

## Acknowledgments
This library were adapted from the following projects:
* [Pangliang](https://github.com/pangliang/miband-sdk-android), a library for the first MI Band.
* [FreeYourGadget](https://github.com/Freeyourgadget/Gadgetbridge), an app to monitor your fitness data without using the app from the manufacturer.

## Examples
### Connecting to a MI Band 2 device
First, define a `DeviceProfile` object and a `MiBand` variable to help the Bluetooth scanner find the MI Band 2 device.
```
DeviceProfile profile = new DeviceProfile("B8:F4:EF:2B:31:FA");
MiBand miBand;
```
Then, define a `ScanCallback` that will take care the device when the Bluetooth scanner found it.
```
final ScanCallback scanCallback = new ScanCallback(){
        @Override
        public void onScanResult(int callbackType, ScanResult result){
            BluetoothDevice device = result.getDevice();
            if (MiBand.isThisTheDevice(device, profile)) {
                MiBand.stopScan(scanCallback);
                connectToMiBand(device);
            }
        }
    };
```
Finally, define the method to connect to the device that was found by the `ScanCallback`.
```
private void connectToMiBand(BluetoothDevice device) {
        this.miBand.connect(device, new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                // Connected
                doPostConnectOperations();
            }

            @Override
            public void onFail(int errorCode, String msg){
                // Failure to connect
                return;
            }
        });
    }
```
### Authenticating and pairing your Android device with the MI Band 2 device
TODO


### Getting device's battery information
TODO

### Getting and setting device's time
TODO

### Getting and setting user's profile on the device
TODO

### Download fitness data from the device
TODO

### Getting real-time heartrate data from the device
TODO

### Getting real-time steps data from the device
TODO

### Sending vibration alert on the device
TODO
