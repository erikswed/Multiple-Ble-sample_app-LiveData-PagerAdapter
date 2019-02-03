package com.ble.multiple.livedata;

import android.annotation.TargetApi;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.bluetooth.*;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.InputDeviceCompat;
import android.widget.Toast;
import com.ble.multiple.R;
import com.ble.multiple.data.*;
import com.ble.multiple.entity.BLECallback;
import com.ble.multiple.entity.BatteryEntity;
import com.ble.multiple.entity.EventPlayToast;
import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@TargetApi(18)
public class LocalQueryLiveData extends LiveData<BatteryEntity> implements BLECallback {
    public static final String TAG = "localasQueryLiveData";
    final Application application;
    BatteryEntity battery;

    private int connectStatus = 2;
    private BluetoothGatt gatt;
    private BluetoothDevice bluetoothDevice;
    private final android.bluetooth.BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback(this);
    private Toast toast;
    private boolean isExit = false;
    private ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
    private int resendCount = 3;

    private final Handler connectStatusCallbackHandler = new ConnectStatusCallbackHandler(this);
    private BLECallback bleConnectStatuesCallback;
    private boolean listenerRemovePending = false;
    private final Handler watchHandler = new Handler();

    private SmartPowerUtil smartPowerUtil = new SmartPowerUtil();

    static class ConnectStatusCallbackHandler extends Handler {
        private final WeakReference<LocalQueryLiveData> localQueryLiveData;

        ConnectStatusCallbackHandler(LocalQueryLiveData target) {
            localQueryLiveData = new WeakReference<>(target);
        }

        public void handleMessage(Message msg) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            switch (msg.what) {
                case BLECallback.STATE_CONNECTED: // 1
                    if (target.bleConnectStatuesCallback != null) {
                        target.bleConnectStatuesCallback.onServicesDiscovered(msg.arg1);
                        return;
                    }
                    return;
                case BLECallback.STATE_DISCONNECTED: // 2
                    if (target.bleConnectStatuesCallback != null) {
                        target.bleConnectStatuesCallback.onConnectedChanged(msg.arg1);
                        return;
                    }
                    return;
                case BLECallback.GATT_SUCCESS: // 3 NOt in use
                    if (target.bleConnectStatuesCallback != null) {
                        target.bleConnectStatuesCallback.onRead((byte[]) msg.obj);
                        return;
                    }
                    return;
                case BLECallback.GATT_FAILURE: // 4
                    if (target.bleConnectStatuesCallback != null) {
                        target.bleConnectStatuesCallback.onWrite(BaseUtil.bytes2HexString((byte[]) msg.obj), msg.arg1);
                        return;
                    }
                    return;
                case BLECallback.READ_SUCCESS: // 5
                    if (target.bleConnectStatuesCallback != null) {
                        target.bleConnectStatuesCallback.onNotification((byte[]) msg.obj);
                        return;
                    }
                default:
            }
        }
    }

    public LocalQueryLiveData(Application application, BatteryEntity battery) {
        this.application = application;
        this.battery = battery;
    }

    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            bleConnectStatuesCallback = null;
            if (gatt != null) {
                gatt.disconnect();
            }
            if (threadExecutor != null) {
                threadExecutor.shutdownNow();
            }
            EventBus.getDefault().unregister(this);
            listenerRemovePending = false;
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.connection_closed, battery.getName()), 1, application));
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        if (listenerRemovePending) {
            watchHandler.removeCallbacks(removeListener);
        } else {
            setBleConnectStatuesCallback(this);
        }
        EventBus.getDefault().register(this);
        listenerRemovePending = false;
        connect(true, battery.getBluetoothDevice());
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        // Listener will be removed when two second have passed or else continue as usual
        watchHandler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private boolean connect(boolean autoConnect, BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        this.connectStatus = 0;
        if (this.gatt != null) {
            this.gatt.close();
        }
        this.bluetoothDevice = device;
        this.gatt = this.bluetoothDevice.connectGatt(BleApplication.getInstance(), autoConnect, this.bluetoothGattCallback);
        if (this.gatt != null) {
            return true;
        }
        return false;
    }

    static class BluetoothGattCallback extends android.bluetooth.BluetoothGattCallback {

        private final WeakReference<LocalQueryLiveData> localQueryLiveData;

        BluetoothGattCallback(LocalQueryLiveData target) {
            localQueryLiveData = new WeakReference<>(target);
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            LogManager.i("BLE-1", target.bluetoothDevice.getName() + "==BLE onConnectionStateChange==" + newState);
            target.gatt = gatt;
            Message msg;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (!target.gatt.discoverServices()) {
                    target.disConnect();
                    msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.STATE_CONNECTED);
                    msg.arg1 = 4;
                    target.connectStatusCallbackHandler.sendMessage(msg);
                }
                target.connectStatus = 1;
                msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.STATE_DISCONNECTED);
                msg.arg1 = 1;
                target.connectStatusCallbackHandler.sendMessage(msg);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                target.connectStatus = 2;
                msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.STATE_DISCONNECTED);
                msg.arg1 = 2;
                target.connectStatusCallbackHandler.sendMessage(msg);
                if (target.gatt != null && target.isExit) {
                    target.gatt.close();
                    target.gatt = null;
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            LogManager.i("BLE-2", target.bluetoothDevice.getName() + "==BLE onServicesDiscovered onConnectionStateChange==" + status);
            Message msg;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                target.connectStatus = 1;
                msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.STATE_CONNECTED);
                msg.arg1 = 3;
                target.connectStatusCallbackHandler.sendMessage(msg);
                return;
            }
            target.connectStatus = 2;
            if (status == InputDeviceCompat.SOURCE_KEYBOARD) {
                target.disConnect();
            }
            msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.STATE_CONNECTED);
            msg.arg1 = 4;
            target.connectStatusCallbackHandler.sendMessage(msg);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            LogManager.i("BLE-3", "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic.getValue().length > 0) {
                Message msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.GATT_SUCCESS);
                msg.obj = characteristic.getValue();
                target.connectStatusCallbackHandler.sendMessage(msg);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            LogManager.i("BLE-4", "onCharacteristicWrite== " + status);
            Message msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.GATT_FAILURE);
            msg.obj = characteristic.getValue();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                msg.arg1 = 7;
            } else {
                msg.arg1 = 8;
            }
            target.connectStatusCallbackHandler.sendMessage(msg);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            LocalQueryLiveData target = localQueryLiveData.get();
            if(target == null) {
                return;
            }
            LogManager.i("BLE-5", "onCharacteristicChanged");
            if (characteristic.getValue().length > 0) {
                Message msg = target.connectStatusCallbackHandler.obtainMessage(BLECallback.READ_SUCCESS);
                msg.obj = characteristic.getValue();
                target.connectStatusCallbackHandler.sendMessage(msg);
            }
        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        }
    }

    public void disConnect() {
        LogManager.i("BLE-disConnect", "conn disConnect");
        if (this.gatt != null) {
            LocalQueryLiveData.this.gatt.disconnect();
            this.connectStatus = 2;
        }
    }

    private void setBleConnectStatuesCallback(BLECallback bleConnectStatuesCallback) {
        this.bleConnectStatuesCallback = bleConnectStatuesCallback;
    }

    @Override
    public void onWrite(String data, int status) {
    }

    @Override
    public void onServicesDiscovered(int discovered) {
        if (discovered == 3) {
            setCharacteristicNotification(getGattCharacteristic(SettingsManager.UUID_SERVICE,
                    SettingsManager.UUID_NOTIFY), true);
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.services_discovered), 0, application));
        } else {
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.services_undiscovered), 0, application));
        }

        if (setCharacteristicNotification(getGattCharacteristic(SettingsManager.UUID_SERVICE, SettingsManager.UUID_NOTIFY), true)) {
            //start2HeaterAcitvity();
        } else {
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.open_notification_failure), 1, application));
        }
    }

    @Override
    public void onRead(byte[] data) {
    }

    @Override
    public void onNotification(byte[] data) {
        // Get the Bluetooth byte data as String
        String stringData = smartPowerUtil.broadcastUpdate(data);
        if (stringData.equals("")) {
            return;
        }
        // Convert it to a BatteryEntity and set the value for the LiveData
        if (smartPowerUtil.handleMessage(stringData, this.battery)) {
            // Update local LiveData listener
            setValue(battery);
        }
    }

    public void onConnectedChanged(int status) {
        if (status == 1) {
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.Ble_connected), 0, application));
        } else {
            EventBus.getDefault().post(new EventPlayToast(application.getString(R.string.Ble_disconnected), 0, application));
        }
    }

    private BluetoothGattCharacteristic getGattCharacteristic(UUID serviceUuids, UUID characteristicUuid) {
        if (this.gatt == null || this.gatt.getService(serviceUuids) == null) {
            return null;
        }
        return this.gatt.getService(serviceUuids).getCharacteristic(characteristicUuid);
    }

    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        if (this.gatt == null || characteristic == null) {
            return false;
        }
        for (BluetoothGattDescriptor dp : characteristic.getDescriptors()) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            this.gatt.writeDescriptor(dp);
        }
        return this.gatt.setCharacteristicNotification(characteristic, enable);
    }
}