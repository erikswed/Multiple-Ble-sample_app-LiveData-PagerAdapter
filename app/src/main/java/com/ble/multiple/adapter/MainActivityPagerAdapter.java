package com.ble.multiple.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.ble.multiple.fragment.BaseFragment;
import com.ble.multiple.fragment.ParentBasicInfoFragment;
import com.ble.multiple.fragment.ParentCandyFragment;
import com.ble.multiple.fragment.ParentToyFragment;

public class MainActivityPagerAdapter extends PersistentPagerAdapter<BaseFragment> {
    private static int NUM_ITEMS = 3;
    private final Bundle bundle;

    public MainActivityPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    public int getCount() {
        return NUM_ITEMS;
    }
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                return ParentBasicInfoFragment.newInstance(bundle);
            case 1:
                return ParentToyFragment.newInstance(bundle);
            case 2:
                return ParentCandyFragment.newInstance(bundle);
            default:
                return null;
        }
    }
}
