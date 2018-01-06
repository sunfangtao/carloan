package com.aioute.carloan.activity;

import android.content.Intent;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.AppointmentAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sft.util.Util;
import cn.sft.view.DefaultNullRecyclerView;

/**
 * Created by Administrator on 2018/1/6.
 */
@EActivity(R.layout.activity_appointment)
public class AppointmentActivity extends CustomBaseActivity {

    //
    @ViewById(R.id.appointment_rv)
    DefaultNullRecyclerView recyclerView;
    //-----------------------------------------------------------------------
    //
    List<String> labelList;

    @Override
    protected void afterViews() {
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(Util.dp2px(this, 1)));
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this, labelList = new ArrayList<>());
        recyclerView.setAdapter(appointmentAdapter);

        labelList.add("车牌号");
        labelList.add("车主姓名");
        labelList.add("钥匙保管人");
        labelList.add("保管电话");
        labelList.add("车停靠位置");

        appointmentAdapter.notifyDataSetChanged();
    }

    void updateUserName(String userName) {
        ((AppointmentAdapter) recyclerView.getAdapter()).updateUserName(userName);
    }

    @Click(R.id.appointment_btn)
    void nextBtnClick() {
        Map<Integer, String> map = ((AppointmentAdapter) recyclerView.getAdapter()).getValueMap();
        if (map.size() != labelList.size()) {
            Util.print("信息填写不完整");
        }
        SelectUnpackDeviceActivity_.intent(this).start();
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.OBTAIN_NAME_BY_PLATE, false)) {
            String plate = intent.getStringExtra(Contant.BroadcastKey.BEAN);
            Util.print("plate=" + plate);
            updateUserName(plate);
        }
    }
}
