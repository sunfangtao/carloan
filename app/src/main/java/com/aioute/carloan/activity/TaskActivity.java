package com.aioute.carloan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.fragment.TaskFinishedFragment;
import com.aioute.carloan.fragment.TaskFinishedFragment_;
import com.aioute.carloan.fragment.TaskInProressFragment;
import com.aioute.carloan.fragment.TaskInProressFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/27.
 * 我的任务
 */

@EActivity(R.layout.activity_task)
public class TaskActivity extends CustomBaseActivity implements RadioGroup.OnCheckedChangeListener {

    // 切换
    @ViewById(R.id.task_rg)
    RadioGroup radioGroup;
    // 未完成
    @ViewById(R.id.task_inprogress_rb)
    RadioButton inProgressRB;
    // 已完成
    @ViewById(R.id.task_finish_rb)
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
        updateInProgressCount(0);
        updateFinishCount(0);

        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 更新未完成任务数量
     *
     * @param count
     */
    public void updateInProgressCount(int count) {
        inProgressRB.setText(String.format(getString(R.string.in_progress_task), count));
    }

    /**
     * 更新完成任务数量
     *
     * @param count
     */
    public void updateFinishCount(int count) {
        finishRB.setText(String.format(getString(R.string.finish_task), count));
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
        if (intent.getBooleanExtra(Contant.BroadcastKey.TASK_ITEM_PHOTO, false)) {
            // 拍照
            TakeTaskPhotoActivity_.intent(this)
                    .extra(Contant.BroadcastKey.POSITION, intent.getIntExtra(Contant.BroadcastKey.POSITION, -1))
                    .extra(Contant.BroadcastKey.BEAN, intent.getSerializableExtra(Contant.BroadcastKey.BEAN))
                    .start();
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.TASK_ITEM_NAV, false)) {
            // 导航
            Util.print("nav:" + intent.getIntExtra(Contant.BroadcastKey.POSITION, -1));
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.TASK_ITEM_REMOVE, false)) {
            // 拍照完成，从列表中移除;对于已完成任务没有此回调；否则需要分别处理，不能直接使用inProressFragment
            inProressFragment.removeItemForFinishPhoto(intent.getIntExtra(Contant.BroadcastKey.POSITION, -1));
        }
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }
}
