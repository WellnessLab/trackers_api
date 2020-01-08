package edu.neu.ccs.wellness.trackers.miband2;

import edu.neu.ccs.wellness.trackers.DeviceProfile;

/**
 * Created by hermansaksono on 6/25/18.
 */

public class MiBand2Profile implements DeviceProfile {

    private String name = "MI Band 2";
    private String address = null;

    public MiBand2Profile(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }
}
