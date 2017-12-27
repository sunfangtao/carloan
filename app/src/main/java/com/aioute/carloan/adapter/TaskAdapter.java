package com.aioute.carloan.adapter;

import android.content.Context;
import android.view.View;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.TaskBean;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/12/27.
 * 我的任务
 */

public class TaskAdapter extends BaseAdapter {

    public TaskAdapter(Context context, BaseFragment fragment, List<TaskBean> list) {
        super(context, fragment, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.task_item;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {


    }
}
