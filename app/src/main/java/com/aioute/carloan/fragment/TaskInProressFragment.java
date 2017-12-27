package com.aioute.carloan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aioute.carloan.adapter.TaskAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.TaskBean;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;
import cn.sft.view.DefaultNullRecyclerView;

/**
 * Created by Administrator on 2017/12/27.
 * 未完成的任务
 */

@EFragment
public class TaskInProressFragment extends CustomBaseFragment {

    //未完成的任务
    private List<TaskBean> taskList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        DefaultNullRecyclerView recyclerView = new DefaultNullRecyclerView(getActivity());
        return recyclerView;
    }

    @Override
    protected void afterViews() {
        TaskAdapter adapter = new TaskAdapter(getActivity(), this, taskList = new ArrayList<>());
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(Util.dp2px(getActivity(), 5));
        DefaultNullRecyclerView recyclerView = (DefaultNullRecyclerView) getView();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);

        getInProgressTask();
        adapter.notifyDataSetChanged();
    }

    void getInProgressTask() {
        TaskBean taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
    }
}
