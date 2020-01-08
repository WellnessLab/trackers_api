package edu.neu.ccs.wellness.trackers.callback;

import edu.neu.ccs.wellness.trackers.BatteryInfo;

public interface BatteryInfoCallback {
    void onSuccess(BatteryInfo batteryInfo);

    void onFail(int errorCode, String msg);
}
