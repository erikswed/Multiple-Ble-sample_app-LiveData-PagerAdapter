package com.ble.multiple.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ble.multiple.R;
import com.ble.multiple.adapter.BasicInfoPagerAdapter;
import com.ble.multiple.entity.BatteryEntity;

public class ParentBasicInfoFragment extends BaseFragment {

    private BasicInfoPagerAdapter adapter;
    private ViewPager viewpage;

    public static ParentBasicInfoFragment newInstance(Bundle args) {
        ParentBasicInfoFragment frag = new ParentBasicInfoFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ViewPager viewpage = view.findViewById(R.id.viewpager);
        Bundle bundled = getArguments();
        viewpage.setOffscreenPageLimit(2);
        BasicInfoPagerAdapter adapter = new BasicInfoPagerAdapter(getChildFragmentManager(), bundled);
        viewpage.setAdapter(adapter);
        this.adapter = adapter;
        this.viewpage = viewpage;
        viewpage.setOffscreenPageLimit(2);
        viewpage.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void activityNotifiDataChange(BatteryEntity batteryEntity) {
        for (int i = 0; i < adapter.getCount(); i++) {
            BaseFragment frag = adapter.getRegisteredFragment(viewpage, i);
            if (((EntityId) frag).getEntityId() == null) {
                return;
            }
            EntityId entity = ((EntityId) frag);
            // Notify the correct frag since this fragment PagerAdapter can have multiple child frags
            if (entity.getEntityId().equals(batteryEntity.getEntityId())
                    && entity.getDeviceType() == batteryEntity.getDeviceType()) {
                frag.activityNotifiDataChange(batteryEntity);
            }
        }
    }
}