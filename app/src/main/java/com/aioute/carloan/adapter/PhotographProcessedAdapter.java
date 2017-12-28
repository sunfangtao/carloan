package com.aioute.carloan.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;

/**
 * Created by FlySand on 2017/12/27.
 * 拍照已读 适配器
 */

public class PhotographProcessedAdapter extends BaseAdapter {


    public PhotographProcessedAdapter(Context context, BaseFragment fragment, List<?> list) {
        super(context, fragment, list);
    }

    @Override
    public int onCreateViewLayoutID(int i) {
        return R.layout.item_photograph_processed;
    }

    @Override
    public void onBindViewHolder(int i, View view, int i1) {

        //头像
        ImageView headpicIv = view.findViewById(R.id.item_photograph_processed_headpic_im);
        //车牌
        TextView plateTv = view.findViewById(R.id.item_photograph_processed_plate_tv);
        //发送人及上传时间展示
        TextView senderTv = view.findViewById(R.id.item_photograph_processed_sender_tv);
        //拍照位置
        TextView locationTv = view.findViewById(R.id.item_photograph_processed_location_tv);
        //位置信息是否一致
        TextView addressCheckTv = view.findViewById(R.id.item_photograph_processed_address_check_tv);
        //3张照片
        ImageView[] photoViews = new ImageView[3];
        photoViews[0] = view.findViewById(R.id.item_photograph_processed_photo_0);
        photoViews[1] = view.findViewById(R.id.item_photograph_processed_photo_1);
        photoViews[2] = view.findViewById(R.id.item_photograph_processed_photo_2);

        Glide.with(context).load(R.mipmap.ic_launcher).into(headpicIv);
        for (ImageView iv : photoViews) {
            Glide.with(context).load(R.mipmap.ic_launcher).into(iv);
        }


    }
}
