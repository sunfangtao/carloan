package com.aioute.carloan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aioute.carloan.adapter.UnpackAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.UnpackOrderBean;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;
import cn.sft.view.DefaultNullRecyclerView;

/**
 * Created by Administrator on 2017/12/27.
 * 已接订单
 */

@EFragment
public class OpenOrderFragment extends CustomBaseFragment {

    //任务列表
    protected List<UnpackOrderBean> orderList;

    protected boolean isReceived() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        DefaultNullRecyclerView recyclerView = new DefaultNullRecyclerView(getActivity());
        return recyclerView;
    }

    @Override
    protected void afterViews() {
        UnpackAdapter adapter = new UnpackAdapter(getActivity(), this, orderList = new ArrayList<>());
        if (isReceived())
            adapter.isReceived();
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
     * 删除指定位置的item（用于取消订单）
     *
     * @param position
     */
    public void removeItemForCancel(int position) {
        if (position < 0 || position > orderList.size() - 1) {
            throw new IllegalArgumentException("position 越界");
        }
        orderList.remove(position);
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
        ((DefaultNullRecyclerView) getView()).getAdapter().notifyItemRangeChanged(position, orderList.size());
    }


    void getTask() {
        UnpackOrderBean unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);
    }

}
