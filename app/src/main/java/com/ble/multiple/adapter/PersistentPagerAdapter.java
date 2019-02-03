package com.ble.multiple.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.ble.multiple.fragment.BaseFragment;


public abstract class PersistentPagerAdapter<T extends BaseFragment> extends FragmentPagerAdapter {
    private SparseArray<T> registeredFragments = new SparseArray<>();

    public PersistentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public T instantiateItem(@NonNull ViewGroup container, int position) {
        T fragment = (T) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public T getRegisteredFragment(ViewGroup container, int position) {
        T existingInstance = registeredFragments.get(position);
        if (existingInstance != null) {
            return existingInstance;
        } else {
            return instantiateItem(container, position);
        }
    }
}
