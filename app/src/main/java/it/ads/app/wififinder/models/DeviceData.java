package it.ads.app.wififinder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceData {
    @SerializedName("deviceName")
    @Expose
    String deviceName;
    @SerializedName("deviceID")
    @Expose
    String deviceID;
    @SerializedName("deviceStrength")
    @Expose
    String deviceStrength;

    public DeviceData(String deviceName, String deviceID, String deviceStrength){
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.deviceStrength = deviceStrength;

    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceStrength() {
        return deviceStrength;
    }
}
