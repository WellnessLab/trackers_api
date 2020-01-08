package edu.neu.ccs.wellness.trackers.miband2.model;

import java.util.UUID;

/**
 * Created by hermansaksono on 7/9/18.
 */

public class GattCharacteristics {

    public static final String BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb";

    public static final UUID UUID_SERVICE_DEVICE_INFORMATION = UUID.fromString((String.format(BASE_UUID, "180A")));
    public static final UUID UUID_CHARACTERISTIC_MANUFACTURER_NAME_STRING = UUID.fromString((String.format(BASE_UUID, "2A29")));
    public static final UUID UUID_CHARACTERISTIC_MODEL_NUMBER_STRING = UUID.fromString((String.format(BASE_UUID, "2A24")));
    public static final UUID UUID_CHARACTERISTIC_SERIAL_NUMBER_STRING = UUID.fromString((String.format(BASE_UUID, "2A25")));
    public static final UUID UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING = UUID.fromString((String.format(BASE_UUID, "2A27")));
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING = UUID.fromString((String.format(BASE_UUID, "2A26")));
    public static final UUID UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING = UUID.fromString((String.format(BASE_UUID, "2A28")));
    public static final UUID UUID_CHARACTERISTIC_SYSTEM_ID = UUID.fromString((String.format(BASE_UUID, "2A23")));
    public static final UUID UUID_CHARACTERISTIC_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = UUID.fromString((String.format(BASE_UUID, "2A2A")));
    public static final UUID UUID_CHARACTERISTIC_PNP_ID = UUID.fromString((String.format(BASE_UUID, "2A50")));

}
