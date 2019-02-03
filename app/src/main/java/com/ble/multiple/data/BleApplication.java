package com.ble.multiple.data;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;
import com.ble.multiple.entity.EventPlayToast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BleApplication extends MultiDexApplication {

    private static BleApplication instance;

    private SharedPreferences mDefaultSharedPreferences;

    private Toast toast;

    /**
     * Handler to execute runnable in UI thread.
     */
    private final Handler mHandler;

    public static BleApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public BleApplication() {
        instance = this;
        mHandler = new Handler();
        EventBus.getDefault().register(this);
    }

    /**
     * Submits request to be executed in UI thread.
     *
     * @param runnable the runnable
     */
    public void runOnUiThread(final Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Submits request to be executed in UI thread delayed.
     *
     * @param runnable    the runnable
     * @param delayMillis the delay millis
     */
    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlayToast(EventPlayToast event) {
        playToast(event.text, event.length);
    }

    private void playToast(String s, int duration) {
        if (this.toast != null) {
            this.toast.cancel();
            this.toast = null;
        }
        this.toast = Toast.makeText(this, s, duration);
        this.toast.setText(s);
        this.toast.show();
    }
}
