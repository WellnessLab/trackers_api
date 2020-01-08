package edu.neu.ccs.wellness.trackers;

import java.util.GregorianCalendar;

import edu.neu.ccs.wellness.trackers.callback.FetchActivityListener;
import edu.neu.ccs.wellness.trackers.callback.RealtimeStepsNotifyListener;

/**
 * Created by hermansaksono on 7/25/18.
 */

public interface StepsTrackingDevice {
    /**
     * Fetch steps count data from the device.
     *
     * @param startTime Determines the start time of the activity data that will be fetched.
     * @param Listener This listener will take care of the data once the MI Band completed
     *                 the request.
     */
    void fetchActivityData(GregorianCalendar startTime, FetchActivityListener Listener);

    /**
     * Starts realtime steps notification.
     * @param listener The {@link RealtimeStepsNotifyListener} that will handle steps count updates.
     */
    void startRealtimeStepsNotification(final RealtimeStepsNotifyListener listener);

    /**
     * Stops realtime steps notification.
     */
    void stopRealtimeStepsNotification();
}
