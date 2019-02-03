package com.ble.multiple.entity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

@SuppressLint({"InlinedApi"})
@TargetApi(18)
public interface BLECallback {
    int GATT_FAILURE = 4;
    int GATT_SUCCESS = 3;
    int READ_FAILURE = 6;
    int READ_SUCCESS = 5;
    int STATE_CONNECTED = 1;
    int STATE_CONNECTEING = 0;
    int STATE_DISCONNECTED = 2;
    int WRITE_FAILURE = 8;
    int WRITE_SUCCESS = 7;
    int WriteCompleted_SUCCESS = 9;
    int SetNewNameComplete = 10;

    void onConnectedChanged(int i);

    void onNotification(byte[] bArr);

    void onRead(byte[] bArr);

    void onServicesDiscovered(int i);

    void onWrite(String str, int i);
}
