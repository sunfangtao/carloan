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
import com.aioute.carloan.fragment.PhotographProcessedFragment;
import com.aioute.carloan.fragment.PhotographProcessedFragment_;
import com.aioute.carloan.fragment.PhotographUntreatedFragment;
import com.aioute.carloan.fragment.PhotographUntreatedFragment_;
import com.aioute.carloan.util.GlideImageLoader;
import com.flysand.ninegrid.NineGridView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 拍照处理页面
 */
@EActivity(R.layout.activity_photograph)
public class PhotographActivity extends CustomBaseActivity implements RadioGroup.OnCheckedChangeListener {

    // 切换
    @ViewById(R.id.photograph_rg)
    RadioGroup radioGroup;
    // 未完成
    @ViewById(R.id.photograph_untreated_rb)
    RadioButton inProgressRB;
    // 已完成
    @ViewById(R.id.photograph_processed_rb)
    RadioButton finishRB;

    // 未处理Fragment
    PhotographUntreatedFragment untreatedFragment;
    // 已处理Fragment
    PhotographProcessedFragment processedFragment;
    //------------------------------------------------------------
    int selectId = 0;

    @Override
    protected void noSaveInstanceStateForCreate() {
        if (untreatedFragment == null) {
            untreatedFragment = new PhotographUntreatedFragment_();
        }
        if (processedFragment == null) {
            processedFragment = new PhotographProcessedFragment_();
        }

        FragmentManager fm = getFragmentManager();

        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.photograph_contain_layout, untreatedFragment, "untreatedFragment");
        transaction.add(R.id.photograph_contain_layout, processedFragment, "processedFragment");

        transaction.hide(untreatedFragment);
        transaction.hide(processedFragment);
        transaction.commit();

        onCheckedChanged(null, selectId = R.id.photograph_untreated_rb);
    }

    @Override
    protected void afterViews() {

        updateInProgressCount(0);
        updateFinishCount(0);

        radioGroup.setOnCheckedChangeListener(this);
        //设置预览图片的加载器
        if (NineGridView.getImageLoader() == null) {
            NineGridView.setImageLoader(new GlideImageLoader());
        }
    }

    /**
     * 更新未完成任务数量
     *
     * @param count
     */
    public void updateInProgressCount(int count) {
        inProgressRB.setText(String.format(getString(R.string.untreated), count));
    }

    /**
     * 更新完成任务数量
     *
     * @param count
     */
    public void updateFinishCount(int count) {
        finishRB.setText(String.format(getString(R.string.processed), count));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(untreatedFragment);
        transaction.hide(processedFragment);
        switch (checkedId) {
            case R.id.photograph_untreated_rb:
                transaction.show(untreatedFragment);
                break;
            case R.id.photograph_processed_rb:
                transaction.show(processedFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.PHOTOGRAPH_ITEM_CONFIRM, false)) {
            // 确认
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.PHOTOGRAPH_ITEM_TAKEPHOTO, false)) {
            // 重拍照片
        }
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        onCheckedChanged(null, radioGroup.getCheckedRadioButtonId() == -1 ? selectId : radioGroup.getCheckedRadioButtonId());
    }

}
