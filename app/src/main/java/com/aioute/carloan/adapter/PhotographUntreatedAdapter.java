package com.aioute.carloan.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.PhotographActivity_;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.bean.PhotographBean;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.PicUtil;
import com.flysand.ninegrid.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;
import cn.sft.listener.RecyclerViewItemClickListener;
import cn.sft.util.Util;

/**
 * Created by FlySand on 2017/12/27.
 */

public class PhotographUntreatedAdapter extends BaseAdapter {

    int screenWidth;

    public PhotographUntreatedAdapter(Context context, BaseFragment fragment, List<?> list) {
        super(context, fragment, list);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int onCreateViewLayoutID(int i) {
        return R.layout.item_photograph_untreated;
    }

    @Override
    public void onBindViewHolder(int i, View view, final int position) {

        final PhotographBean bean = (PhotographBean) getObjcet(position);

        //头像
        ImageView headpicIv = view.findViewById(R.id.item_photograph_untreated_headpic_im);
        //车牌
        TextView plateTv = view.findViewById(R.id.item_photograph_untreated_plate_tv);
        //发送人及上传时间展示
        TextView senderTv = view.findViewById(R.id.item_photograph_untreated_sender_tv);
        //拍照位置
        TextView locationTv = view.findViewById(R.id.item_photograph_untreated_location_tv);
        //位置信息是否一致
        TextView addressCheckTv = view.findViewById(R.id.item_photograph_untreated_address_check_tv);
        //确认按钮
        Button confirmBtn = view.findViewById(R.id.item_photograph_untreated_confirm_btn);
        Button takePhotoBtn = view.findViewById(R.id.item_photograph_procressed_btn);

        RecyclerView recyclerView = view.findViewById(R.id.item_photograph_untreated_rv);

        PhotoUntreatedItemAdapter adapter = null;
        if (recyclerView.getAdapter() != null) {
            adapter = (PhotoUntreatedItemAdapter) recyclerView.getAdapter();
            adapter.updateData(bean.getPhotos());
        } else {
            int padding = Util.dp2px(context, 5);
            // +1 表示两边也有间距 3列有4个间距
            int width = screenWidth - view.getPaddingLeft() - view.getPaddingRight() - (Contant.TAKE_TASK_PHOTO_COLUMN + 1) * padding;

            recyclerView.setLayoutManager(new GridLayoutManager(context, Contant.TAKE_TASK_PHOTO_COLUMN));
            adapter = new PhotoUntreatedItemAdapter(context, bean.getPhotos(), width / Contant.TAKE_TASK_PHOTO_COLUMN);
            recyclerView.setAdapter(adapter);
            RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(padding);
            recyclerView.addItemDecoration(decoration);
        }

        //头像
//        Glide.with(context).load(R.mipmap.ic_launcher).into(headpicIv);
        senderTv.setText(String.format(context.getString(R.string.sender_time), "哈哈", "2017-12-01 12:12:14"));
        plateTv.setText("鲁F123456");
        locationTv.setText(String.format(context.getString(R.string.takephoto_position), "asdfaf"));
        addressCheckTv.setEnabled(false);
//        addressCheckTv.setText(context.getString(R.string.takephoto_position_ok));
        addressCheckTv.setText(context.getString(R.string.takephoto_position_notice));

        adapter.setItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(BaseAdapter baseAdapter, View view, int position) {
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < bean.getPhotos().size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(bean.getPhotos().get(i));
                    info.setBigImageUrl(bean.getPhotos().get(i));
                    imageInfo.add(info);
                }
                new PicUtil().showBigImages(context, imageInfo, position);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(PhotographActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.PHOTOGRAPH_ITEM_CONFIRM, true)
                        .putExtra(Contant.BroadcastKey.BEAN, bean)
                        .putExtra(Contant.BroadcastKey.POSITION, position));
            }
        });

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(PhotographActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.PHOTOGRAPH_ITEM_TAKEPHOTO, true)
                        .putExtra(Contant.BroadcastKey.BEAN, bean)
                        .putExtra(Contant.BroadcastKey.POSITION, position));
            }
        });

    }

}
