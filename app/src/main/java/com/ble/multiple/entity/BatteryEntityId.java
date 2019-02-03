package com.ble.multiple.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.ble.multiple.data.SettingsManager;


/**
 * The ID for this BlueTooth device
 */
public abstract class BatteryEntityId implements Parcelable {

    /**
     * Unique id for this battery
     */
    private String entityId;

    /**
     * The type of device, remote or local
     */
    private SettingsManager.DeviceType deviceType;

    /**
     * The name of the device
     */
    private String name;

    BatteryEntityId() {
    }

    public BatteryEntityId(String entityId, SettingsManager.DeviceType deviceType, String name) {
        this.entityId = entityId;
        this.deviceType = deviceType;
        this.name = name;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public SettingsManager.DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(SettingsManager.DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected BatteryEntityId(Parcel in) {
        entityId = in.readString();
        name = in.readString();
        deviceType = (SettingsManager.DeviceType) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entityId);
        dest.writeString(name);
        dest.writeSerializable(deviceType);
    }
}

