package com.aioute.carloan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.fragment.DeviceDetailFragment;
import com.aioute.carloan.fragment.DeviceDetailFragment_;
import com.aioute.carloan.fragment.DevicePositionFragment;
import com.aioute.carloan.fragment.DevicePositionFragment_;
import com.aioute.carloan.fragment.DeviceSettingFragment;
import com.aioute.carloan.fragment.DeviceSettingFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2018/1/2.
 * 单个设备操作界面（设备信息，定位，轨迹，设置）
 */

@EActivity(R.layout.activity_single_deviceoper)
public class SingleDeviceOperActivity extends CustomBaseActivity implements RadioGroup.OnCheckedChangeListener {

    // 切换
    @ViewById(R.id.single_deviceoper_rg)
    RadioGroup radioGroup;
    // 设备信息Fragment
    DeviceDetailFragment deviceDetailFragment;
    // 定位轨迹Fragment
    DevicePositionFragment devicePositionFragment;
    // 设备设置Fragment
    DeviceSettingFragment deviceSettingFragment;
    //------------------------------------------------------------
    int selectId = 0;
    // toolbar menu文字
    String menuTime;

    @Override
    protected void noSaveInstanceStateForCreate() {
        if (deviceDetailFragment == null) {
            deviceDetailFragment = new DeviceDetailFragment_();
        }
        if (devicePositionFragment == null) {
            devicePositionFragment = new DevicePositionFragment_();
        }
        if (deviceSettingFragment == null) {
            deviceSettingFragment = new DeviceSettingFragment_();
        }

        FragmentManager fm = getFragmentManager();

        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.single_deviceoper_contain_layout, deviceDetailFragment, "deviceDetailFragment");
        transaction.add(R.id.single_deviceoper_contain_layout, devicePositionFragment, "devicePositionFragment");
        transaction.add(R.id.single_deviceoper_contain_layout, deviceSettingFragment, "deviceSettingFragment");

        transaction.hide(deviceDetailFragment);
        transaction.hide(devicePositionFragment);
        transaction.hide(deviceSettingFragment);

        transaction.commit();

        int index = getIntent().getIntExtra(Contant.BroadcastKey.POSITION, 0);
        switch (index) {
            case 0:
                selectId = R.id.single_deviceoper_detail_rb;
                break;
            case 1:
                selectId = R.id.single_deviceoper_position_rb;
                break;
            case 2:
                selectId = R.id.single_deviceoper_trace_rb;
                break;
            default:
                selectId = R.id.single_deviceoper_setting_rb;
        }
        ((RadioButton) findViewById(selectId)).setChecked(true);
    }

    @Override
    protected void afterViews() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(deviceDetailFragment);
        transaction.hide(devicePositionFragment);
        transaction.hide(deviceSettingFragment);
        switch (checkedId) {
            case R.id.single_deviceoper_detail_rb:
                transaction.show(deviceDetailFragment);
                break;
            case R.id.single_deviceoper_position_rb:
                devicePositionFragment.setShowPosition(true);
                transaction.show(devicePositionFragment);
                break;
            case R.id.single_deviceoper_trace_rb:
                devicePositionFragment.setShowPosition(false);
                transaction.show(devicePositionFragment);
                break;
            case R.id.single_deviceoper_setting_rb:
                transaction.show(deviceSettingFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (radioGroup.getCheckedRadioButtonId() == R.id.single_deviceoper_trace_rb) {
            menu.add(0, 0, 0, menuTime == null ? "" : menuTime);
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.add(0, 1, 0, "");
            menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.getItem(1).setIcon(R.mipmap.icon_time);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (radioGroup.getCheckedRadioButtonId() == R.id.single_deviceoper_trace_rb) {
            switch (item.getItemId()) {
                case 0:
                    devicePositionFragment.showTraceDay();
                    break;
                case 1:
                    devicePositionFragment.showTraceTime();
                    break;
            }
        }
        return true;
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_REFRESH, false)) {
            menuTime = intent.getStringExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_TIME);
            invalidateOptionsMenu();
        }
    }
}
