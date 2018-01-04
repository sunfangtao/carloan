package com.aioute.carloan.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.CarInfoActivity_;
import com.aioute.carloan.adapter.DeviceDetailDeviceAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2018/1/2.
 * 设备详情页
 */
@EFragment(R.layout.fragment_devicedetail)
public class DeviceDetailFragment extends CustomBaseFragment {

    // 设备简介
    @ViewById(R.id.devicedetail_tv)
    AlwaysMarqueeTextView deviceInfoTV;
    //
    @ViewById(R.id.devicedetail_rv)
    RecyclerView deviceRecyclerView;
    // 当前设备报警信息
    @ViewById(R.id.devicewarn_tv)
    TextView deviceWarnTV;
    // 关联车辆
    @ViewById(R.id.devicecar_tv)
    TextView carTV;
    //
    @ViewById(R.id.devicecar_rv)
    RecyclerView deviceCarRecycyclerView;

    //--------------------------------------
    // 设备信息
    List<String> deviceInfoList;
    // 车辆信息
    List<String> deviceCarInfoList;

    @Override
    protected void afterViews() {
        updateDeviceInfo("138138138001", "OBD", "离线3天", "自粗");

        initDeviceRecyclerView();
        updateCarPlate("鲁F123456");
    }

    /**
     * 初始化设备列表
     */
    void initDeviceRecyclerView() {
        DeviceDetailDeviceAdapter deviceAdapter = new DeviceDetailDeviceAdapter(getActivity(), this, deviceInfoList = new ArrayList<>());
        deviceRecyclerView.setAdapter(deviceAdapter);
        deviceRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(1));
        deviceRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        deviceInfoList.add("最后定位：");
        deviceInfoList.add("定位时间：");
        deviceInfoList.add("更新时间：");
        deviceInfoList.add("服务期起：");
        deviceInfoList.add("服务期止：");

        deviceAdapter.notifyDataSetChanged();


        DeviceDetailDeviceAdapter deviceCarAdapter = new DeviceDetailDeviceAdapter(getActivity(), this, deviceCarInfoList = new ArrayList<>());
        deviceCarRecycyclerView.setAdapter(deviceCarAdapter);
        deviceCarRecycyclerView.addItemDecoration(new RecyclerViewItemDecoration(1));
        deviceCarRecycyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        deviceCarInfoList.add("车辆状态：");
        deviceCarInfoList.add("车主姓名：");
        deviceCarInfoList.add("车型品牌：");

        deviceCarAdapter.notifyDataSetChanged();
    }

    @Click(R.id.devicewarn_tv)
    void deviceWarnTVClick() {

    }

    @Click(R.id.devicecar_tv)
    void carTVClick() {
        CarInfoActivity_.intent(this).start();
    }

    /**
     * 更新关联车辆的车牌号
     *
     * @param plate
     */
    void updateCarPlate(String plate) {
        carTV.setText(String.format(getString(R.string.relative_car), plate));
    }

    /**
     * 更新设备信息
     *
     * @param deviceNum
     * @param style
     * @param status
     * @param group
     */
    void updateDeviceInfo(String deviceNum, String style, String status, String group) {
        deviceInfoTV.setSingleLine(false);
        deviceInfoTV.setText(String.format(getString(R.string.device_intro_info), deviceNum, style, status, group));
    }
}
