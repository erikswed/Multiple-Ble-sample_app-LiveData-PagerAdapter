package com.ble.multiple.activity;

import android.app.Application;
import android.arch.lifecycle.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.ble.multiple.R;
import com.ble.multiple.adapter.MainActivityPagerAdapter;
import com.ble.multiple.data.BaseUtil;
import com.ble.multiple.data.SettingsManager;
import com.ble.multiple.entity.BatteryEntity;
import com.ble.multiple.fragment.BaseFragment;
import com.ble.multiple.viewmodel.BatteryLocalViewModel;
import com.ble.multiple.viewmodel.BatteryRemoteViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static final String BLUETOOTH_DEVICES = "bluetooth_devices";
    public static final String BLUETOOTH_DEVICE_ID = "bluetooth_devices_id";
    public static final String BLUETOOTH_DEVICE_TYPE = "bluetooth_device_type";
    public static final String BLUETOOTH_DEVICE_NAME = "bluetooth_device_name";

    private ImageView ivBattery;
    private ImageView ivCharge;
    private ImageView ivPara;
    private View iv_bottom;
    private final int[] location1 = new int[2];
    private final int[] location2 = new int[2];
    private final int[] location3 = new int[2];

    private int pading;
    private TextView tvBasic;
    private TextView tvCharge;
    private TextView tvParra;
    private ViewPager viewpage;
    private ArrayList<BatteryEntity> batteryEntityList = new ArrayList<>();
    private MainActivityPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        initLinstener();
        initFragments();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.tvBasic.getLocationOnScreen(MainActivity.this.location1);
                MainActivity.this.tvParra.getLocationOnScreen(MainActivity.this.location2);
                MainActivity.this.tvCharge.getLocationOnScreen(MainActivity.this.location3);
                MainActivity.this.iv_bottom.setX((float) MainActivity.this.location1[0]);
                MainActivity.this.iv_bottom.getLayoutParams().width = BaseUtil.getWindowMetrics(MainActivity.this).widthPixels / 3;
                MainActivity.this.pading = MainActivity.this.location1[0] - MainActivity.this.location2[0];
                MainActivity.this.iv_bottom.requestLayout();
            }
        }, 500);
        initLiveDataObservers();
    }

    private void initUi() {
        this.tvBasic = findViewById(R.id.tv_basic);
        this.tvParra = findViewById(R.id.tv_parra);
        this.tvCharge = findViewById(R.id.tv_charge);
        this.ivBattery = findViewById(R.id.iv_battery);
        this.ivPara = findViewById(R.id.iv_para);
        this.ivCharge = findViewById(R.id.iv_charge);
        this.iv_bottom = findViewById(R.id.iv_bottom);
        this.viewpage = findViewById(R.id.viewpage);
    }

    private void initLinstener() {
        this.ivBattery.setOnClickListener(this);
        this.ivCharge.setOnClickListener(this);
        this.ivPara.setOnClickListener(this);
        this.viewpage.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float x;
                switch (position) {
                    case 0:
                        x = ((float) MainActivity.this.location1[0]) - (((float) MainActivity.this.pading) * positionOffset);
                        break;
                    case 1:
                        x = ((float) MainActivity.this.location2[0]) - (((float) MainActivity.this.pading) * positionOffset);
                        break;
                    case 2:
                        x = ((float) MainActivity.this.location3[0]) - (((float) MainActivity.this.pading) * positionOffset);
                        break;
                    default:
                        x = (float) MainActivity.this.location1[0];
                        break;
                }
                MainActivity.this.iv_bottom.setX(x);
            }

            public void onPageSelected(int position) {
                float x;
                MainActivity.this.setCurrentTab(position);
                switch (position) {
                    case 0:
                        x = (float) MainActivity.this.location1[0];
                        break;
                    case 1:
                        x = (float) MainActivity.this.location2[0];
                        break;
                    case 2:
                        x = (float) MainActivity.this.location3[0];
                        break;
                    default:
                        x = (float) MainActivity.this.location1[0];
                        break;
                }
                MainActivity.this.iv_bottom.setX(x);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

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

    /**
     * Create your ViewModels Observers here. In this example there are two ViewModels<br>
     * - BatteryLocalViewModel<br>
     * - BatteryRemoteViewModel<br>
     */
    protected void initLiveDataObservers() {
        // Create the temp list to sort the batteries on local and remote
        ArrayList<BatteryEntity> BatteryEntitiesRemote = new ArrayList<>();
        ArrayList<BatteryEntity> BatteryEntitiesLocal = new ArrayList<>();

        // Sort the batteries according to if they are remote or local
        for (BatteryEntity battery : batteryEntityList) {
            if (battery.getDeviceType() == SettingsManager.DeviceType.LOCAL_DEVICE) {
                BatteryEntitiesLocal.add(battery);
            } else if (battery.getDeviceType() == SettingsManager.DeviceType.REMOTE_DEVICE) {
                BatteryEntitiesRemote.add(battery);
            }
        }
        // Intit
        initiateLocalViewModels(BatteryEntitiesLocal);
        initiateRemoteViewModels(BatteryEntitiesRemote);
    }

    private void initiateLocalViewModels(ArrayList<BatteryEntity> batteryEntitiesLocal) {
        // Not really need but can be used to mock som future tests
        FactoryLocalEntity factory = new FactoryLocalEntity(getApplication(), batteryEntitiesLocal);
        BatteryLocalViewModel batteryLocalViewModel = ViewModelProviders.of(this, factory).
                get(BatteryLocalViewModel.class);
        LiveData<BatteryEntity> liveDataLocal = batteryLocalViewModel.getDataSnapshotLiveData();
        liveDataLocal.observe(this, new Observer<BatteryEntity>() {
            @Override
            public void onChanged(@Nullable final BatteryEntity batteryEntity) {
                Fragment frag = (BaseFragment) adapter.getRegisteredFragment(viewpage, viewpage.getCurrentItem());
                ((BaseFragment)frag).activityNotifiDataChange(batteryEntity);
            }
        });
    }

    private void initiateRemoteViewModels(ArrayList<BatteryEntity> batteryEntitiesRemote) {
        // Not really need but can be used to mock som future tests
        FactoryRemoteEntity factory = new FactoryRemoteEntity(getApplication(), batteryEntitiesRemote);
        BatteryRemoteViewModel batteryRemoteViewModel = ViewModelProviders.of(this, factory).
                get(BatteryRemoteViewModel.class);
        LiveData<BatteryEntity> liveDataLocal = batteryRemoteViewModel.getDataSnapshotLiveData();
        liveDataLocal.observe(this, new Observer<BatteryEntity>() {
            @Override
            public void onChanged(@Nullable final BatteryEntity batteryEntity) {
                Fragment frag = (BaseFragment) adapter.getRegisteredFragment(viewpage, viewpage.getCurrentItem());
                ((BaseFragment)frag).activityNotifiDataChange(batteryEntity);
            }
        });
    }

    public static class FactoryLocalEntity extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        @NonNull
        private final ArrayList<BatteryEntity> batteryEntityList;

        FactoryLocalEntity(@NonNull Application application, @NonNull ArrayList<BatteryEntity> batteryEntityList) {
            this.application = application;
            this.batteryEntityList = batteryEntityList;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new BatteryLocalViewModel(application, batteryEntityList);
        }
    }

    public static class FactoryRemoteEntity extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        @NonNull
        private final ArrayList<BatteryEntity> batteryEntityList;

        FactoryRemoteEntity(@NonNull Application application, @NonNull ArrayList<BatteryEntity> batteryEntityList) {
            this.application = application;
            this.batteryEntityList = batteryEntityList;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new BatteryRemoteViewModel(application, batteryEntityList);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.ivBattery) {
            this.viewpage.setCurrentItem(0);
            setCurrentTab(0);
        } else if (v == this.ivCharge) {
            this.viewpage.setCurrentItem(2);
            setCurrentTab(2);
        } else if (v == this.ivPara) {
            this.viewpage.setCurrentItem(1);
            setCurrentTab(1);
        }
    }

    private void setCurrentTab(int posi) {
        switch (posi) {
            case 0:
                this.tvBasic.setVisibility(View.VISIBLE);
                this.tvParra.setVisibility(View.INVISIBLE);
                this.tvCharge.setVisibility(View.INVISIBLE);
                return;
            case 1:
                this.tvBasic.setVisibility(View.INVISIBLE);
                this.tvParra.setVisibility(View.VISIBLE);
                this.tvCharge.setVisibility(View.INVISIBLE);
                return;
            case 2:
                this.tvBasic.setVisibility(View.INVISIBLE);
                this.tvParra.setVisibility(View.INVISIBLE);
                this.tvCharge.setVisibility(View.VISIBLE);
                return;
            default:
        }
    }
}