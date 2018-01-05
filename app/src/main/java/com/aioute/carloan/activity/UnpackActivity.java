package com.aioute.carloan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.fragment.TaskFinishedFragment;
import com.aioute.carloan.fragment.TaskFinishedFragment_;
import com.aioute.carloan.fragment.TaskInProressFragment;
import com.aioute.carloan.fragment.TaskInProressFragment_;

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
    @ViewById(R.id.unpage_rg)
    RadioGroup radioGroup;
    // 未完成
    @ViewById(R.id.unpage_inprogress_rb)
    RadioButton inProgressRB;
    // 已完成
    @ViewById(R.id.unpage_finish_rb)
    RadioButton finishRB;
    // 未完成Fragment
    TaskInProressFragment inProressFragment;
    // 已完成Fragment
    TaskFinishedFragment finishedFragment;
    //------------------------------------------------------------
    int selectId = 0;

    @Override
    protected void noSaveInstanceStateForCreate() {

        if (inProressFragment == null) {
            inProressFragment = new TaskInProressFragment_();
        }
        if (finishedFragment == null) {
            finishedFragment = new TaskFinishedFragment_();
        }

        FragmentManager fm = getFragmentManager();

        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.task_contain_layout, inProressFragment, "inProressFragment");
        transaction.add(R.id.task_contain_layout, finishedFragment, "finishedFragment");

        transaction.hide(inProressFragment);
        transaction.hide(finishedFragment);

        transaction.commit();

        inProgressRB.setChecked(true);
    }

    @Override
    protected void afterViews() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Util.print("onCheckedChanged=" + group);
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(inProressFragment);
        transaction.hide(finishedFragment);
        switch (checkedId) {
            case R.id.task_inprogress_rb:
                transaction.show(inProressFragment);
                break;
            case R.id.task_finish_rb:
                transaction.show(finishedFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void forReceiverResult(Intent intent) {

    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }
}