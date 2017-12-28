package com.aioute.carloan.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.PhotoVerifyAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.bean.TaskBean;
import com.aioute.carloan.common.Contant;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/28.
 * 查看拍照任务完成情况
 */

@EActivity(R.layout.activity_photoverify)
public class PhotoVerifyActivity extends CustomBaseActivity {

    // 未完成
    @ViewById(R.id.photoverify_inprogress_tv)
    TextView inProgressTV;
    // 已完成
    @ViewById(R.id.photoverify_finish_tv)
    TextView finishTV;
    // 未拍照任务列表
    @ViewById(R.id.photoverify_rv)
    RecyclerView inProressRV;
    //------------------------------------------------------------
    // 拍照任务列表
    private List<TaskBean> taskList = new ArrayList<>();

    @Override
    protected void noSaveInstanceStateForCreate() {

    }

    @Override
    protected void afterViews() {
        updateInProgressCount(0);
        updateFinishCount(0);
        inProressRV.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(Util.dp2px(this, 5));
        inProressRV.addItemDecoration(decoration);
        PhotoVerifyAdapter adapter = new PhotoVerifyAdapter(this, taskList);
        inProressRV.setAdapter(adapter);

        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        taskList.add(new TaskBean());
        adapter.notifyDataSetChanged();
    }

    @Click(R.id.photoverify_finish_tv)
    void photoFinishClick() {
        PhotographActivity_.intent(this).start();
    }

    /**
     * 更新未完成任务数量
     *
     * @param count
     */
    public void updateInProgressCount(int count) {
        inProgressTV.setText(String.format(getString(R.string.no_photo), count));
    }

    /**
     * 更新完成任务数量
     *
     * @param count
     */
    public void updateFinishCount(int count) {
        finishTV.setText(String.format(getString(R.string.has_photo), count));
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.TASK_ITEM_PHOTO, false)) {
            Util.print("photo:" + intent.getIntExtra(Contant.BroadcastKey.POSITION, -1));
//            inProressFragment.removeItemForFinishPhoto(intent.getIntExtra(Contant.BroadcastKey.POSITION, -1));
            TakeTaskPhotoActivity_.intent(this).start();
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.TASK_ITEM_NAV, false)) {
            Util.print("nav:" + intent.getIntExtra(Contant.BroadcastKey.POSITION, -1));
        }
    }

}
