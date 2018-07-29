# Wellness and Fitness Trackers API
An unofficial library to communicate with health and fitness trackers. Currently, this library supports MI Band 2. For questions, contact [Herman Saksono](http://www.ccs.neu.edu/~hsaksono/).


## Acknowledgments
This library is an adaptation of the following projects:
* [Pangliang](https://github.com/pangliang/miband-sdk-android), a library for the first MI Band.
* [FreeYourGadget](https://github.com/Freeyourgadget/Gadgetbridge), an app to monitor your fitness data without using the app from the manufacturer.

This material is based upon work supported by the National Science Foundation under Grant Number #1618406. 

## Examples
### Connecting, Authenticating, and Pairing
#### Connecting to a MI Band 2 device
First, define `MiBand2Scanner` object and a `MiBand` variable to help the Bluetooth scanner find the MI Band 2 device.
```
MiBandScanner miBandScanner = new MiBandScanner();
MiBand miBand;
```
Then, define a `ScanCallback` that will take care the device when the Bluetooth scanner found it.
```
final ScanCallback scanCallback = new ScanCallback() { 
	@Override 
	public void onScanResult(int callbackType, ScanResult result) {
		        miBandScanner.stopScan(scanCallback); 
		        BluetoothDevice device = result.getDevice();
		        miBand = getConnectionToDevice(device);
		}); 
	} 
};

```
Finally, define the method to connect to the device that was found by the `ScanCallback`.
```
private void getConnectionToDevice(BluetoothDevice device) {
        return MiBand.newConnectionInstance(device, context, new ActionCallback() {
			@Override 
			public void onSuccess(Object data) { 
				// Connected! TODO Do auth (if necessary) and pairing
			}
			
			@Override 
			public void onFail(int errorCode, String msg) { 
				// TODO Handle failure 
			} 
		}
    }
```
#### Authenticating and pairing your Android device with the MI Band 2 device
Before your app can communicate with the Mi Band 2, your app must (1) authenticate, then (2) pair your Android device with the MI Band 2. Your app only need to do step 1 once, but your app need to do step 2 every time you connect to the MI Band 2.

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
The `auth` method will perform the authentication process. If the authentication is successful, the `ActionCallback` will proceed with the pairing process by calling the `pair` method. Otherwise, this method needs to handle this failure.
```
this.miBand.auth(new ActionCallback() {
    @Override
    public void onSuccess(Object data){
        // TODO call the pair() method
    }
    @Override
    public void onFail(int errorCode, String msg){
        // TODO Handle auth failure (e.g., log the msg)
    }
});
```
The `pair` method will perform the pairing process. If the authentication is successful, the `ActionCallback` will do other actions. Otherwise, this method needs to handle this failure.

```
this.miBand.pair(new ActionCallback() {
    @Override
    public void onSuccess(Object data){
        // TODO Do other actions
    }
    @Override
    public void onFail(int errorCode, String msg){
        // TODO Handle pairing error (e.g., log the msg)
    }
});
```
### Basic Operations
#### Getting the device's battery information
```
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
```

#### Getting and setting the device's time
```
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
```
```
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
```

#### Getting and setting user's profile on the device
```
UserInfo userInfo = new UserInfo(userIdInteger, UserInfo.BIOLOGICAL_SEX_MALE, 
                                 age, heightCm, weightKg, userAlias, 1);
```
```
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
```
#### Sending vibration alert on the device
```
this.miBand.doOneVibration();
```
### Wellness and Fitness Operations
#### Download steps data from the device
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

#### Getting real-time heartrate data from the device
```
this.miBand.startRealtimeStepsNotification(new HeartRateNotifyListener() {
	@Override 
	public void onNotify(int heartRate) {
		Log.d("mi-band", "Heart rate: "+ heartRate); 
	} 
});
```

#### Getting real-time steps data from the device
```
this.miBand.startRealtimeStepsNotification(new RealtimeStepsNotifyListener() {
    @Override
    public void onNotify(int steps){
        Log.d("mi-band", String.format("Steps: %d", steps));
    }
});
```

