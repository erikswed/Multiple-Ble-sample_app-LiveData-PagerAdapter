package com.ble.multiple.entity;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import com.ble.multiple.data.SettingsManager;


public class BatteryEntity extends BatteryEntityId {
    public static final Parcelable.Creator<BatteryEntity> CREATOR = new CREATOR();
    private BluetoothDevice bluetoothDevice;

    public BatteryEntity() {
    }

    static class CREATOR implements Parcelable.Creator<BatteryEntity> {
        CREATOR() {
        }

        public BatteryEntity createFromParcel(Parcel source) {
            return new BatteryEntity(source);
        }

        public BatteryEntity[] newArray(int size) {
            return new BatteryEntity[size];
        }
    }

    public BatteryEntity(String uuid, String name, SettingsManager.DeviceType deviceType,
                         String TheBluetoothDevice) {
        super(uuid, deviceType, name);
        // assing the real TheBluetoothDevice here
        this.bluetoothDevice = null;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.bluetoothDevice, flags);
    }

    private BatteryEntity(Parcel in) {
        super(in);
        this.bluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

}
