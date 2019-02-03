package com.ble.multiple.data;

import com.ble.multiple.entity.BatteryEntity;
import com.ble.multiple.entity.BatteryInfoEntity;


import java.util.List;

public class SmartPowerUtil {

    public String broadcastUpdate(byte[] data) {
        // convert your Bluetooth byte data to string
        return "";
    }

    public boolean handleMessage(String stringData, BatteryEntity batteryEntity) {
        // handle data set all batteryEntity fields
        return true;
    }

    public List<BatteryInfoEntity> getBatteryInfo(String str) {
        // handle data
        return null;
    }
}
