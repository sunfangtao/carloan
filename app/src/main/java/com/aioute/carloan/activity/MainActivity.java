package com.aioute.carloan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.fragment.MainFragment;
import com.aioute.carloan.fragment.MainFragment_;
import com.aioute.carloan.fragment.MonitorFragment;
import com.aioute.carloan.fragment.MonitorFragment_;
import com.aioute.carloan.fragment.MyFragment;
import com.aioute.carloan.fragment.MyFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 登录后的主界面
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends CustomBaseActivity implements RadioGroup.OnCheckedChangeListener {

    // 切换
    @ViewById(R.id.main_rg)
    RadioGroup radioGroup;
    // 主页面Fragment
    MainFragment mainFragment;
    // 监控Fragment
    MonitorFragment monitorFragment;
    // 我的Fragment
    MyFragment myFragment;
    //------------------------------------------------------------
    int selectId = 0;

    @Override
    protected void noSaveInstanceStateForCreate() {
        if (mainFragment == null) {
            mainFragment = new MainFragment_();
        }
        if (monitorFragment == null) {
            monitorFragment = new MonitorFragment_();
        }
        if (myFragment == null) {
            myFragment = new MyFragment_();
        }

        FragmentManager fm = getFragmentManager();

        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.main_contain_layout, mainFragment, "mainFragment");
        transaction.add(R.id.main_contain_layout, monitorFragment, "monitorFragment");
        transaction.add(R.id.main_contain_layout, myFragment, "myFragment");

        transaction.hide(mainFragment);
        transaction.hide(monitorFragment);
        transaction.hide(myFragment);

        transaction.commit();

        onCheckedChanged(null, selectId = R.id.main_main_rb);
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
        transaction.hide(mainFragment);
        transaction.hide(monitorFragment);
        transaction.hide(myFragment);
        switch (checkedId) {
            case R.id.main_main_rb:
                transaction.show(mainFragment);
                break;
            case R.id.main_monitor_rb:
                transaction.show(monitorFragment);
                break;
            case R.id.main_my_rb:
                transaction.show(myFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }
}
