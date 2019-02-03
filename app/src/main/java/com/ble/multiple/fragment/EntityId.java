package com.ble.multiple.fragment;


import com.ble.multiple.data.SettingsManager;

/**
 * Id of one Bluetooth connected battery
 */
public abstract class EntityId extends BaseFragment {

    /**
     * Uuid of one Bluetooth connected battery
     */
    private String entityId;

    /**
     * The type of device, remote or local
     */
    private SettingsManager.DeviceType deviceType;


    SettingsManager.DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(SettingsManager.DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

}

