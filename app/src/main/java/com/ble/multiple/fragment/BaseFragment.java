package com.ble.multiple.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import com.ble.multiple.entity.BatteryEntity;

public abstract class BaseFragment extends Fragment implements OnClickListener {

    public abstract void activityNotifiDataChange(BatteryEntity batteryEntity);

    public void onClick(View v) {
    }
}