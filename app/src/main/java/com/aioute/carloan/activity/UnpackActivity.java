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
import com.aioute.carloan.fragment.OpenOrderFragment;
import com.aioute.carloan.fragment.OpenOrderFragment_;
import com.aioute.carloan.fragment.ReceivedOrderFragment;
import com.aioute.carloan.fragment.ReceivedOrderFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/27.
 * 预约拆机
 */

@EActivity(R.layout.activity_unpack)
public class UnpackActivity extends CustomBaseActivity implements RadioGroup.OnCheckedChangeListener {

    // 切换
    @ViewById(R.id.unpack_rg)
    RadioGroup radioGroup;
    // 待接订单
    @ViewById(R.id.unpack_open_rb)
    RadioButton openOrderRB;
    // 已接订单
    @ViewById(R.id.unpack_receiver_rb)
    RadioButton receivedOrderRB;
    // 代接订单Fragment
    OpenOrderFragment openOrderFragment;
    // 已完成Fragment
    ReceivedOrderFragment receivedOrderFragment;
    //------------------------------------------------------------
    int selectId = 0;

    @Override
    protected void noSaveInstanceStateForCreate() {

        if (openOrderFragment == null) {
            openOrderFragment = new OpenOrderFragment_();
        }
        if (receivedOrderFragment == null) {
            receivedOrderFragment = new ReceivedOrderFragment_();
        }

        FragmentManager fm = getFragmentManager();

        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.unpack_contain_layout, openOrderFragment, "openOrderFragment");
        transaction.add(R.id.unpack_contain_layout, receivedOrderFragment, "receivedOrderFragment");

        transaction.hide(openOrderFragment);
        transaction.hide(receivedOrderFragment);

        transaction.commit();

        openOrderRB.setChecked(true);
    }

    @Override
    protected void afterViews() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.appointment);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 预约
        AppointmentActivity_.intent(this).start();
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Util.print("onCheckedChanged=" + group);
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(openOrderFragment);
        transaction.hide(receivedOrderFragment);
        switch (checkedId) {
            case R.id.unpack_open_rb:
                transaction.show(openOrderFragment);
                break;
            case R.id.unpack_receiver_rb:
                transaction.show(receivedOrderFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.UNPACK_ITEM_OPEN_CANCEL, false)) {
            //未接订单，取消订单
            Util.print("未接订单，取消订单");
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.UNPACK_ITEM_TIP, false)) {
            // 未接订单，提醒订单
            Util.print("未接订单，提醒订单");
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.UNPACK_ITEM_RECEIVED_CANCEL, false)) {
            // 已接订单，取消订单
            Util.print("已接订单，取消订单");
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.UNPACK_ITEM_PHONE, false)) {
            // 已接订单，电话
            Util.print("已接订单，电话");
        }
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }
}
