# Wellness and Fitness Trackers API
An unofficial library to communicate with health and fitness trackers. Currently, this library supports MI Band 2. For questions, contact [Herman Saksono](http://www.ccs.neu.edu/~hsaksono/).


## Acknowledgments
This library is an adaptation of the following projects:
* [Pangliang](https://github.com/pangliang/miband-sdk-android), a library for the first MI Band.
* [FreeYourGadget](https://github.com/Freeyourgadget/Gadgetbridge), an app to monitor your fitness data without using the app from the manufacturer.

This material is based upon work supported by the National Science Foundation under Grant Number #1618406. 

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
                // TODO Handle failure
            }
        });
    }
```
### Authenticating and pairing your Android device with the MI Band 2 device
Before you can communicate with the Mi Band 2, you must (1) authenticate your Android device, then (2) pair your Android device with the MI Band 2. Your app only need to do step 1 once, but your app need to do step 2 every time you connect to the MI Band 2.

You can call `doAuthAndPair` method after your app is connected to the MI Band 2 device. If the MI Band 2 device has not been bonded, this method will call `doAuth`. Otherwise this will proceed with pairing by calling `doPair`.
```
    private void doAuthAndPair() {
        boolean isPaired = miBand.getDevice().getBondState() != BluetoothDevice.BOND_NONE;
        if (isPaired == false) {
            this.doAuth();
        } else {
            this.doPair();
        }
    }
```
The `doAuth` method will perform the authentication process. If the authentication is successful, the `ActionCallback` will proceed with the pairing process by calling the `doPair` method. Otherwise, this method needs to handle this failure.
```
    private void doAuth() {
        this.miBand.auth(new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                doPair();
            }
            @Override
            public void onFail(int errorCode, String msg){
                // TODO Handle auth failure (e.g., log the msg)
            }
        });
    }
```
The `doPair` method will perform the pairing process. If the authentication is successful, the `ActionCallback` will call the `doPair` method. Otherwise, this method needs to handle this failure.

```
    private void doPair() {
        this.miBand.pair(new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                // TODO Do other action
            }
            @Override
            public void onFail(int errorCode, String msg){
                // TODO Handle pairing error (e.g., log the msg)
            }
        });
    }
```

### Getting device's battery information
```
    private void getBatteryInfo() {
        this.miBand.getBatteryInfo(new BatteryInfoCallback() {
            @Override
            public void onSuccess(BatteryInfo info){
                Log.d("mi-band-2", "Battery: " + info.toString());
            }
            @Override
            public void onFail(int errorCode, String msg){
                Log.d("mi-band-2" , "Battery info failed: " + msg);
            }
        });
    }
```

### Getting and setting device's time
```
    private void getDeviceTime() {
        this.miBand.getCurrentTime(new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                Date deviceTime = (Date) data;
                // TODO Do something with the deviceTime
            }
            @Override
            public void onFail(int errorCode, String msg){
                // TODO Handle failure when retrieving the device's time
            }
        });
    }
```
```
    private void setDeviceTime(GregorianCalendar newTime) {
        this.miBand.setTime(newTime, new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                // TODO Do something
            }
            @Override
            public void onFail(int errorCode, String msg){
                // TODO Do something upon failure
            }
        });
    }
```

### Getting and setting user's profile on the device
```
        UserInfo userInfo = new UserInfo(userIdInteger, UserInfo.BIOLOGICAL_SEX_MALE, 
                                         age, heightCm, weightKg, userAlias, 1);
```
```
private void setUserData(UserInfo userInfo) {
        this.miBand.setUserInfo(userInfo, new ActionCallback() {
            @Override
            public void onSuccess(Object data){
                // TODO Handle when successful
            }
            @Override
            public void onFail(int errorCode, String msg){
                // TODO Handle when failure
            }
        });
    }
```

### Download steps data from the device
To retrieve steps data from the device, your app must define a listener. Then pass that listener to the `fetchActivityData` method. This API will make a fitness data request to the MI Band 2 device. Once the request is completed, this API will pass the results to the listener.

The `downloadStepsData` method below shows how this can be done inside your app. First we define a `FetchActivityListener` that will take care of the data once this API has completed the request. The `OnFetchComplete` method will receive the starting date (`startDate`) and a list of steps count (`stepsByMinutes`). This list of steps contains minute-by-minute steps data starting from `startDate` to the last data available on the MI Band 2 device. After this listener is defined, the method pass the start date and the listener to the `fetchActivityData` method.
```
    private void downloadStepsData(GregorianCalendar fromStartDate) {
        FetchActivityListener fetchActivityListener = new FetchActivityListener() {
            @Override
            public void OnFetchComplete(Calendar startDate, List<Integer> stepsByMinutes) {
                // TODO Process the minute-by-minute steps data from startDate
            }
        };
        this.miBand.fetchActivityData(startDate, fetchActivityListener);
    }
```

### Getting real-time heartrate data from the device
TODO

### Getting real-time steps data from the device
TODO

### Sending vibration alert on the device
TODO
