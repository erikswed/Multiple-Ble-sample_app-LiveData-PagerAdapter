Complete Android app showing how to stay connected to multiple ble devices or custom devices using Android Jetpack LiveData, ViewModel and mush more like nested Fragments with FragmentPagerAdapter

By scanning for and optaining your own  <a href="https://developer.android.com/reference/kotlin/android/bluetooth/BluetoothDevice">BluetoothDevice</a> 
you can attach it and get notifications. Check out the MainActivity()

```
private void initFragments() {
        // Since this is a mock example and there are no real Bluetooth devices so here I create
        // a local and remote device. You can also create two local devices or custom devices
        BatteryEntity batteryEntity1 = new BatteryEntity(
                "eewe.weew.wewe.wwew",
                "AAA",
                SettingsManager.DeviceType.LOCAL_DEVICE,
                "the real bluetoothDevice"); // this should be the actual bluetoothDevice
        BatteryEntity batteryEntity2 = new BatteryEntity(
                "ghgh.ghgh.ghgh.vyyy",
                "BBB",
                SettingsManager.DeviceType.REMOTE_DEVICE,
                "the real bluetoothDevice"); // this should be the actual bluetoothDevice
        ArrayList<BatteryEntity> batteryEntityList = new ArrayList<>();
        batteryEntityList.add(batteryEntity1);
        batteryEntityList.add(batteryEntity2);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BLUETOOTH_DEVICES, batteryEntityList);
        this.viewpage.setOffscreenPageLimit(3);
        setCurrentTab(0);
        adapter = new MainActivityPagerAdapter(getSupportFragmentManager(), bundle);
        this.viewpage.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager(), bundle));
    }
```


