package edu.neu.ccs.wellness.trackers.miband2;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.neu.ccs.wellness.trackers.callback.FetchActivityListener;
import edu.neu.ccs.wellness.trackers.callback.NotifyListener;
import edu.neu.ccs.wellness.trackers.miband2.model.Profile;
import edu.neu.ccs.wellness.trackers.miband2.model.Protocol;
import edu.neu.ccs.wellness.trackers.miband2.utils.CalendarUtils;
import edu.neu.ccs.wellness.trackers.miband2.utils.TypeConversionUtils;

/**
 * Created by hermansaksono on 6/22/18.
 */

public class OperationFetchActivities {

    private static final int BTLE_DELAY_MODERATE = 1000;
    private static final int BTLE_WAIT_FOR_ALL_SAMPLES = 2000;
    private static final int BTLE_DELAY_LONG = 3000;
    private static final int BTLE_DELAY_SHORT = 500;
    private static final int ONE_MIN_ARRAY_SUBSET_LENGTH = 4;
    private static final int STEPS_DATA_INDEX = 3;
    private static final String TAG = "mi-band-activities";

    private BluetoothIO io;
    private GregorianCalendar startDateFromDevice;
    private int numberOfSamplesFromDevice;
    private int numberOfPacketsFromDevice;
    private List<List<Integer>> rawPackets;

    private Handler handler;
    private FetchActivityListener fetchActivityListener;
    private NotifyListener notifyListener = new NotifyListener() {
        @Override
        public void onNotify(byte[] data) {
            processRawActivityData(data);
        }
    };
    private Runnable packetsWaitingRunnable;

    private Runnable dataFetchRunnable = new Runnable() {
        @Override
        public void run() {
            startFetchingData();
        }
    };

    public OperationFetchActivities(FetchActivityListener notifyListener, Handler handler) {
        this.fetchActivityListener = notifyListener;
        this.handler = handler;
        this.rawPackets = new ArrayList<>();
    }

    public void perform(BluetoothIO io, GregorianCalendar date) {
        Calendar expectedEndDate = CalendarUtils.getRoundedMinutes(GregorianCalendar.getInstance());
        expectedEndDate.add(Calendar.MINUTE, -1);
        this.io = io;

        int expectedNumberOfSamples = (int) CalendarUtils.getDurationInMinutes(date, expectedEndDate);
        int expectedNumberOfPackets = (int) Math.ceil(expectedNumberOfSamples / 4f);

        Log.v(TAG, String.format("Expecting to stop after %d samples, %d packets",
                expectedNumberOfSamples, expectedNumberOfPackets));
        startFetchingFitnessData(date);
    }

    private void startFetchingFitnessData(GregorianCalendar startDate) {
        this.io.stopNotifyListener(Profile.UUID_SERVICE_MILI, Profile.UUID_CHAR_5_ACTIVITY);
        this.enableFetchUpdatesNotify(startDate);
    }

    private void enableFetchUpdatesNotify(final GregorianCalendar startDate) { // This needs delay
        this.io.setNotifyListener(Profile.UUID_SERVICE_MILI, Profile.UUID_CHAR_4_FETCH, new NotifyListener() {
        @Override
        public void onNotify(byte[] data) {
            Log.d(TAG, Arrays.toString(data));
            processFetchingNotification(data);
        }
        });
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendCommandParams(startDate);
            }
        }, BTLE_DELAY_MODERATE);
    }

    private void sendCommandParams(GregorianCalendar startDate) { // This doesn't need delay
        byte[] params = getFetchingParams(startDate);
        Log.d(TAG, String.format(
                "Fetching fitness data from %s.", startDate.getTime().toString()));
        Log.v(TAG, String.format(
                "Fetching fitness params: %s", Arrays.toString(getFetchingParams(startDate))));
        this.io.writeCharacteristic(Profile.UUID_CHAR_4_FETCH, params, null);
        this.enableFitnessDataNotify();
    }

    private void enableFitnessDataNotify() { // This doesn't need delay
        this.io.setNotifyListener(
                Profile.UUID_SERVICE_MILI, Profile.UUID_CHAR_5_ACTIVITY, this.notifyListener);
    }

    private void startFetchingData() {
        Log.v(TAG, String.format(
                "Begin fitness data transfer from %s: %d samples, %d packets.",
                this.startDateFromDevice.getTime().toString(),
                this.numberOfSamplesFromDevice,
                this.numberOfPacketsFromDevice));
        this.io.writeCharacteristic(
                Profile.UUID_CHAR_4_FETCH, Protocol.COMMAND_ACTIVITY_FETCH, null);

        if (this.numberOfSamplesFromDevice > 0) {

        } else {
            this.completeFetchingProcess();
        }

    }

    /* PARAM METHODS */
    private static byte[] getFetchingParams(Calendar startDate) {
        byte[] paramStartTime = TypeConversionUtils.getTimeBytes(startDate, TimeUnit.MINUTES);
        return TypeConversionUtils.join(Protocol.COMMAND_ACTIVITY_PARAMS, paramStartTime);
    }

    /* ACTIVITY DATA NOTIFICATION METHODS */
    private void processFetchingNotification(byte[] data) {
        if (isDataTransferReady(data)) { // [16, 1, 1, 5, 0, 0, 0, -30, 7, 8, 3, 14, 31, 0, -16]
            this.startDateFromDevice = getDateFromDeviceByteArray(data);
            this.numberOfSamplesFromDevice = getNumPacketsFromByteArray(data);
            this.numberOfPacketsFromDevice = (int) Math.ceil(this.numberOfSamplesFromDevice / 4f);
            this.startDelayedFetch();
        } else if (isAllDataTransferred(data)) { // [16, 2, 1]
            this.completeFetchingProcess();
            this.handler.removeCallbacks(packetsWaitingRunnable);
        }
    }

    private void startDelayedFetch() {
        if (this.dataFetchRunnable == null) {
            this.handler.removeCallbacks(this.dataFetchRunnable);
        }

        this.handler.postDelayed(this.dataFetchRunnable, BTLE_DELAY_SHORT);
    }

    private static boolean isDataTransferReady(byte[] byteArrayFromDevice) {
        return byteArrayFromDevice[1] == 1;
    }

    private static boolean isAllDataTransferred(byte[] byteArrayFromDevice) {
        return byteArrayFromDevice[1] == 2;
    }

    private static GregorianCalendar getDateFromDeviceByteArray(byte[] byteArrayFromDevice) {
        return (GregorianCalendar) CalendarUtils.bytesToCalendar(
                Arrays.copyOfRange(byteArrayFromDevice, 7, byteArrayFromDevice.length));
    }

    private static int getNumPacketsFromByteArray(byte[] byteArrayFromDevice) {
        return TypeConversionUtils.byteToInt(byteArrayFromDevice[3]);
    }

    /* ACTIVITY DATA PROCESSING METHODS */
    private void processRawActivityData(byte[] data) {
        rawPackets.add(Arrays.asList(TypeConversionUtils.byteArrayToIntegerArray(data)));
        Log.v(TAG, String.format("Fitness packet %d: %s", rawPackets.size(), Arrays.toString(data)));

        if (this.packetsWaitingRunnable == null) {
            this.waitAndComputeSamples(rawPackets.size());
        }
    }

    private void waitAndComputeSamples(final int numSamplesPreviously) {
        this.packetsWaitingRunnable = new Runnable() {
            @Override
            public void run() {
                if (rawPackets.size() > numSamplesPreviously) {
                    Log.v(TAG, String.format("Continue fetching after %d/%d packets",
                            rawPackets.size(), numberOfPacketsFromDevice ));
                    waitAndComputeSamples(rawPackets.size());
                } else {
                    Log.d(TAG, String.format("Aborting fetch after %d/%d packets",
                            rawPackets.size(), numberOfPacketsFromDevice ));
                    completeFetchingProcess();
                }
            }
        };
        this.handler.postDelayed(this.packetsWaitingRunnable, BTLE_WAIT_FOR_ALL_SAMPLES);
    }

    private void completeFetchingProcess() {
        List<Integer> fitnessSamples = getFitnessSamplesFromRawPackets(rawPackets);
        this.fetchActivityListener.OnFetchComplete(this.startDateFromDevice, fitnessSamples);
        this.io.stopNotifyListener(Profile.UUID_SERVICE_MILI, Profile.UUID_CHAR_5_ACTIVITY);
        this.handler.removeCallbacks(this.packetsWaitingRunnable);
        this.packetsWaitingRunnable = null;
    }

    /* FITNESS SAMPLES METHODS */
    private static List<Integer> getFitnessSamplesFromRawPackets(List<List<Integer>> rawSamples) {
        List<Integer> fitnessSamples = new ArrayList<>();
        for (List<Integer> rawSample : rawSamples) {
            fitnessSamples.add(getSteps(rawSample, 0));
            fitnessSamples.add(getSteps(rawSample, 1));
            fitnessSamples.add(getSteps(rawSample, 2));
            fitnessSamples.add(getSteps(rawSample, 3));
        }
        return fitnessSamples;
    }

    private static int getSteps(List<Integer> rawSample, int subindex) {
        int rawSampleIndex = (subindex * ONE_MIN_ARRAY_SUBSET_LENGTH) + STEPS_DATA_INDEX;
        if (rawSampleIndex < rawSample.size()) {
            return rawSample.get(rawSampleIndex);
        } else {
            return 0;
        }
    }
}
