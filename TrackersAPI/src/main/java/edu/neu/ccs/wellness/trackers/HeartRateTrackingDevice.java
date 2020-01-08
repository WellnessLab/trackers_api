package edu.neu.ccs.wellness.trackers;

import edu.neu.ccs.wellness.trackers.callback.HeartRateNotifyListener;

/**
 * Created by hermansaksono on 7/29/18.
 */

public interface HeartRateTrackingDevice {

    /**
     * Starts heart rate tracking.
     * @param listener The {@link HeartRateNotifyListener} that will handle heart rate updates.
     * @return True if successful (i.e., the device is connected). Otherwise return false;
     */
    boolean startHeartRateNotification(final HeartRateNotifyListener listener);


    /**
     * Stops heart rate tracking.
     * @return True if successful (i.e., the device is connected). Otherwise return false;
     */
    boolean stopHeartRateNotification();
}
