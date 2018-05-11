package com.digitalscale.model;

/**
 * Created by Vishal Gadhiya on 5/30/2017.
 */

public class BTDeviceItem {

    private String deviceName;
    private boolean isPaired;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean getIsPaired() {
        return isPaired;
    }

    public void setPaired(boolean paired) {
        isPaired = paired;
    }
}
