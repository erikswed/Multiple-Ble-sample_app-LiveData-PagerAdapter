package com.ble.multiple.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ParentFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public ParentFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Fragment getItem(int arg0) {
        return this.list.get(arg0);
    }

    public void setData(List<Fragment> fragmentList) {
        this.list = fragmentList;
    }

}


