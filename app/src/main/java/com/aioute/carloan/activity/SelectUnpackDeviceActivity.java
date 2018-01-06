package com.aioute.carloan.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.SpinnerUnpackReasonAdapter;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.util.Util;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2018/1/6.
 * 选择拆除设备
 */
@EActivity(R.layout.activity_selectunpackdevice)
public class SelectUnpackDeviceActivity extends CustomBaseActivity {

    // 设备号，姓名
    @ViewById(R.id.selectunpackdevice_tv)
    AlwaysMarqueeTextView deviceNameTV;
    // 设备列表
    @ViewById(R.id.selectunpackdevice_rv)
    RecyclerView recyclerView;
    // 拆除原因
    @ViewById(R.id.selectunpackdevice_spinner)
    Spinner spinner;
    //--------------------------------------------
    // 设备列表
    List<String> deviceNumList;

    @Override
    protected void afterViews() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        DeviceListAdapter adapter = new DeviceListAdapter(this, deviceNumList = new ArrayList<>());
        recyclerView.setAdapter(adapter);

        updateDeviceName("asdfa", "dddd");

        deviceNumList.add("1111");
        deviceNumList.add("2222");
        deviceNumList.add("3333");
        deviceNumList.add("4444");

        List<String> reasonList = new ArrayList<>();
        reasonList.add("清贷拆机");
        reasonList.add("关联错误");
        reasonList.add("悔贷错误");
        reasonList.add("售后更换设备");
        SpinnerUnpackReasonAdapter spinnerUnpackReasonAdapter = new SpinnerUnpackReasonAdapter(this, reasonList);
        spinner.setAdapter(spinnerUnpackReasonAdapter);
    }

    /**
     * 更新拆机车辆和车主姓名
     */
    void updateDeviceName(String plate, String userName) {
        deviceNameTV.setSingleLine(false);
        deviceNameTV.setText(String.format(getString(R.string.device_name), plate, userName));
    }

    @Click(R.id.selectunpackdevice_btn)
    void commitBtnClick() {

    }

    class DeviceListAdapter extends BaseAdapter {

        Map<Integer, Boolean> selectMap;

        public DeviceListAdapter(Context context, List<String> list) {
            super(context, list);
            selectMap = new HashMap<>();
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return 0;
        }

        @Override
        public View onCreateViewNoLayoutID() {
            AppCompatCheckBox checkBox = new AppCompatCheckBox(context, null, android.R.attr.checkboxStyle);
            checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dp2px(context, 40)));
            return checkBox;
        }

        @Override
        public void onBindViewHolder(int viewType, View view, final int position) {

            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;

            checkBox.setOnCheckedChangeListener(null);

            checkBox.setText((String) getObjcet(position));
            checkBox.setChecked(selectMap.get(position) != null ? selectMap.get(position) : false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectMap.put(position, isChecked);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
