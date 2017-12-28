package com.aioute.carloan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aioute.carloan.adapter.PhotographUntreatedAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.PhotographBean;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;
import cn.sft.view.DefaultNullRecyclerView;

/**
 * Created by FlySand on 2017/12/25.
 * 拍照未处理
 */
@EFragment
public class PhotographUntreatedFragment extends CustomBaseFragment {

    //拍照列表
    protected List<PhotographBean> photographBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        DefaultNullRecyclerView recyclerView = new DefaultNullRecyclerView(getActivity());
        return recyclerView;
    }

    @Override
    protected void afterViews() {
        PhotographUntreatedAdapter adapter = new PhotographUntreatedAdapter(getActivity(), this, photographBeanList = new ArrayList<>());
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(Util.dp2px(getActivity(), 5));
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
     * 删除指定位置的item
     *
     * @param position
     */
    public void removeItemForFinishPhoto(int position) {
        if (position < 0 || position > photographBeanList.size() - 1) {
            throw new IllegalArgumentException("position 越界");
        }
        photographBeanList.remove(position);
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
        ((DefaultNullRecyclerView) getView()).getAdapter().notifyItemRangeChanged(position, photographBeanList.size());
    }

    void getTask() {
        String[] photos = new String[]{"http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png", "http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png", "http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png"};

        for (int i = 0; i < 10; i++) {
            PhotographBean bean = new PhotographBean();
            bean.setPhotos(photos);
            photographBeanList.add(bean);
        }
    }

}
