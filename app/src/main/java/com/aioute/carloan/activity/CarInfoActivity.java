package com.aioute.carloan.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.DeviceDetailDeviceAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 * 车辆信息界面
 */

@EActivity(R.layout.activity_carinfo)
public class CarInfoActivity extends CustomBaseActivity {

    //
    @ViewById(R.id.carinfo_rv)
    RecyclerView recyclerView;
    //----------------------------------------
    //车辆信息
    List<String> carInfoList;

    @Override
    protected void afterViews() {
        initRecyclerView();
    }

    void initRecyclerView() {
        DeviceDetailDeviceAdapter adapter = new DeviceDetailDeviceAdapter(this, carInfoList = new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(1));

        carInfoList.add("车辆状态：");
        carInfoList.add("车主姓名：");
        carInfoList.add("车型品牌：");

        adapter.notifyDataSetChanged();
    }
}
