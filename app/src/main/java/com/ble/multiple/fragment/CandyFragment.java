package com.ble.multiple.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ble.multiple.R;
import com.ble.multiple.data.SettingsManager;
import com.ble.multiple.entity.BatteryEntity;

import static com.ble.multiple.activity.MainActivity.BLUETOOTH_DEVICE_ID;
import static com.ble.multiple.activity.MainActivity.BLUETOOTH_DEVICE_TYPE;
import static com.ble.multiple.activity.MainActivity.BLUETOOTH_DEVICE_NAME;

public class CandyFragment extends EntityId {
    private TextView name;
    private TextView type;
    private TextView uuid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntityId(getArguments().getString(BLUETOOTH_DEVICE_ID));
        setDeviceType((SettingsManager.DeviceType) getArguments().getSerializable(BLUETOOTH_DEVICE_TYPE));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = view.findViewById(R.id.text_view_name);
        this.type = view.findViewById(R.id.text_view_type);
        this.uuid = view.findViewById(R.id.text_view_uuid);
        setName(getArguments().getString(BLUETOOTH_DEVICE_NAME));
        setType((SettingsManager.DeviceType) getArguments().getSerializable(BLUETOOTH_DEVICE_TYPE));
        setUuid(getArguments().getString(BLUETOOTH_DEVICE_ID));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candy, container, false);
    }

    public void activityNotifiDataChange(BatteryEntity batteryEntity) {
        setName(batteryEntity.getName());
        // Set your data
    }

    private void setName(String name){
        this.name.setText(getString(R.string.name, name));
    }

    private void setType(SettingsManager.DeviceType type){
        this.type.setText(getString(R.string.type, type.name()));
    }

    private void setUuid(String uuid){
        this.uuid.setText(getString(R.string.uuid, uuid));
    }
}