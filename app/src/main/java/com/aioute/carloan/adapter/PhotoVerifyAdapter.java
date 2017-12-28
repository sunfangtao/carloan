package com.aioute.carloan.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.TaskBean;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/28.
 * 未完成任务
 */

public class PhotoVerifyAdapter extends BaseAdapter {

    public PhotoVerifyAdapter(Context context, List<TaskBean> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_photoverify;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        ImageView headPicIM = view.findViewById(R.id.photoverify_headpic_im);
        TextView senderTimeTV = view.findViewById(R.id.photoverify_time_tv);
        AlwaysMarqueeTextView plateTV = view.findViewById(R.id.photoverify_plate_tv);
        AlwaysMarqueeTextView positionTV = view.findViewById(R.id.photoverify_position_tv);

        final TaskBean taskBean = (TaskBean) getObjcet(position);

        senderTimeTV.setText(String.format(context.getString(R.string.sender), "18562172893"));
        plateTV.setText(String.format(context.getString(R.string.plate), "鲁F123456"));
        positionTV.setText("莱山区" + position);
        Glide.with(context).load(R.mipmap.ic_launcher_round).into(headPicIM);
    }
}
