package com.aioute.carloan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
                selectId = R.id.single_deviceoper_position_rb;
                break;
            default:
                selectId = R.id.single_deviceoper_setting_rb;
        }
        onCheckedChanged(null, selectId);
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
                transaction.show(devicePositionFragment);
                break;
            case R.id.single_deviceoper_trace_rb:
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
}
