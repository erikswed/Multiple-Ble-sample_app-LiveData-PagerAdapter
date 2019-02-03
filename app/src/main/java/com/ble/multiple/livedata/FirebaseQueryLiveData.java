package com.ble.multiple.livedata;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.ble.multiple.data.LogManager;
import com.ble.multiple.data.SmartPowerUtil;
import com.ble.multiple.entity.BatteryEntity;
import com.ble.multiple.entity.ByteData;
import com.google.firebase.firestore.*;
import org.greenrobot.eventbus.EventBus;

public class FirebaseQueryLiveData extends LiveData<BatteryEntity> {

    private boolean listenerRemovePending = false;
    private final Handler watchHandler = new Handler();
    private final MyValueEventListener listener = new MyValueEventListener();
    private SmartPowerUtil smartPowerUtil = new SmartPowerUtil();
    private final Application application;
    private BatteryEntity battery;
    private final DocumentReference documentReference;
    private ListenerRegistration listenerRegistration;

    public FirebaseQueryLiveData(Application application, DocumentReference documentReference, BatteryEntity battery) {
        this.application = application;
        this.battery = battery;
        this.documentReference = documentReference;
        this.battery = battery;
    }

    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            listenerRegistration.remove();
            listenerRemovePending = false;
            EventBus.getDefault().unregister(this);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        // Use old listener or create new, check boolean listenerRemovePending
        if (listenerRemovePending) {
            watchHandler.removeCallbacks(removeListener);
        } else {
            listenerRegistration = documentReference.addSnapshotListener(listener);
        }
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        // Listener will be removed when two second have passed or else continue as usual
        watchHandler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements EventListener<DocumentSnapshot> {
        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                LogManager.e(this, "Can't listen to DocumentSnapshot: " + documentSnapshot + ":::" + e.getMessage());
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                ByteData byteData = documentSnapshot.toObject(ByteData.class);
                if(byteData != null) {
                    setLiveData(byteData);
                }
            }
        }
    }

    private synchronized void setLiveData(ByteData byteData){
        if (smartPowerUtil.handleMessage(byteData.getData(), battery)) {
            setValue(battery);
        }
    }
}