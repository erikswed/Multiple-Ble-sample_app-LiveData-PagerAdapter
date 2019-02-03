package com.ble.multiple.viewmodel;


import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ble.multiple.data.LogManager;
import com.ble.multiple.entity.BatteryEntity;
import com.ble.multiple.livedata.LocalQueryLiveData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class BatteryLocalViewModel extends ViewModel {

    private final MediatorLiveData<BatteryEntity> liveDataMerger = new MediatorLiveData<>();
    private final ArrayList<BatteryEntity> batteryEntityList;
    private Application application;
    private LiveData<BatteryEntity> batteryData;

    /**
     * Local background Thread executor ..
     */
    private final ExecutorService mBackgroundExecutor = Executors
            .newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    Thread thread = new Thread(runnable,
                            "my thread");
                    thread.setPriority(Thread.NORM_PRIORITY);
                    thread.setDaemon(true);
                    return thread;
                }
            });

    public BatteryLocalViewModel(Application application, ArrayList<BatteryEntity> batteryEntityList) {
        this.application = application;
        this.batteryEntityList = batteryEntityList;
        initLiveData();
    }

    private void initLiveData() {
        // sett upp all batteries with their own LiveData
        for (BatteryEntity battery : batteryEntityList) {
            LocalQueryLiveData liveData = new LocalQueryLiveData(application, battery);
            liveDataMerger.addSource(liveData, new Observer<BatteryEntity>() {
                @Override
                public void onChanged(@Nullable final BatteryEntity batteryEntity) {
                    if (batteryEntity != null) {
                        runInBackground(new Runnable() {
                            @Override
                            public void run() {
                                liveDataMerger.postValue(batteryEntity);
                            }
                        });
                    } else {
                        LogManager.w(this,"batteryEntity was NULL");
                    }
                }
            });
        }
        batteryData = Transformations.map(liveDataMerger, new Deserializer());
    }

    private class Deserializer implements Function<BatteryEntity, BatteryEntity> {
        @Override
        public BatteryEntity apply(BatteryEntity batteryEntity) {
            return batteryEntity;
        }
    }

    @NonNull
    public LiveData<BatteryEntity> getDataSnapshotLiveData() {
        return batteryData;
    }

    /**
     * Submits request to be executed in background.
     * Threads submitted will be executed synchronously.
     *
     * @param runnable the runnable
     */
    private void runInBackground(final Runnable runnable) {
        mBackgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
