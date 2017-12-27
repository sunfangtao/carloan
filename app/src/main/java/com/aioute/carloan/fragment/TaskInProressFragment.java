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
    protected List<TaskBean> taskList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        DefaultNullRecyclerView recyclerView = new DefaultNullRecyclerView(getActivity());
        return recyclerView;
    }

    @Override
    protected void afterViews() {
        TaskAdapter adapter = new TaskAdapter(getActivity(), this, taskList = new ArrayList<>());
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(Util.dp2px(getActivity(), 10));
        DefaultNullRecyclerView recyclerView = (DefaultNullRecyclerView) getView();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void noSaveInstanceStateForCreate() {
        getTask();
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置的item（用于完成拍照后从未拍照列表删除）
     *
     * @param position
     */
    public void removeItemForFinishPhoto(int position) {
        if (position < 0 || position > taskList.size() - 1) {
            throw new IllegalArgumentException("position 越界");
        }
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 刷新Adapter
     */
    void notifyDataSetChanged() {
        ((DefaultNullRecyclerView) getView()).getAdapter().notifyDataSetChanged();
    }

    /**
     * 删除Adapter指定item
     */
    void notifyItemRemoved(int position) {
        ((DefaultNullRecyclerView) getView()).getAdapter().notifyItemRemoved(position);
    }


    void getTask() {
        TaskBean taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
    }

}
