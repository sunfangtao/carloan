package com.aioute.carloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.TaskActivity_;
import com.aioute.carloan.bean.TaskBean;
import com.aioute.carloan.common.Contant;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/27.
 * 我的任务(未完成和已完成)
 */

public class TaskAdapter extends BaseAdapter {

    public TaskAdapter(Context context, BaseFragment fragment, List<TaskBean> list) {
        super(context, fragment, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_task;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        ImageView headPicIM = view.findViewById(R.id.task_headpic_im);
        TextView senderTV = view.findViewById(R.id.task_sender_tv);
        TextView sendTimeTV = view.findViewById(R.id.task_sendtime_tv);
        AlwaysMarqueeTextView plateTV = view.findViewById(R.id.task_plate_tv);
        AlwaysMarqueeTextView positionTV = view.findViewById(R.id.task_position_tv);
        RelativeLayout operateLayout = view.findViewById(R.id.task_operate_layout);
        Button photoBtn = view.findViewById(R.id.task_photo_btn);
        Button navBtn = view.findViewById(R.id.task_nav_btn);

        final TaskBean taskBean = (TaskBean) getObjcet(position);

        senderTV.setText(String.format(context.getString(R.string.sender), "18562172893"));
        plateTV.setText(String.format(context.getString(R.string.plate), "鲁F123456"));
        positionTV.setHtmlText("莱山区" + taskBean.getNumber());
//        Glide.with(context).load(R.mipmap.ic_launcher_round).into(headPicIM);
        sendTimeTV.setText("2017-12-14 10:22:22");

        operateLayout.setVisibility(View.VISIBLE);

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(TaskActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.TASK_ITEM_PHOTO, true)
                        .putExtra(Contant.BroadcastKey.POSITION, position)
                        .putExtra(Contant.BroadcastKey.BEAN, taskBean));
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(TaskActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.TASK_ITEM_NAV, true)
                        .putExtra(Contant.BroadcastKey.POSITION, position)
                        .putExtra(Contant.BroadcastKey.BEAN, taskBean));
            }
        });
    }
}
