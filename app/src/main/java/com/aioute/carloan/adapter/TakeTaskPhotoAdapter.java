package com.aioute.carloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.TakeTaskPhotoActivity_;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.PicUtil;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;

/**
 * Created by Administrator on 2017/12/28.
 */

public class TakeTaskPhotoAdapter extends BaseAdapter {

    private int itemCount;

    public TakeTaskPhotoAdapter(Context context, List<String> list, int count) {
        super(context, list);
        this.itemCount = count;
    }

    @Override
    public int getItemCount() {
        return Math.min(super.getItemCount() + 1, itemCount);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_taketaskphoto;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        ImageView photoIm = view.findViewById(R.id.taketaskphoto_item_im);
        Button cancelBtn = view.findViewById(R.id.taketaskphoto_item_btn);

        final String url = (String) getObjcet(position);

        PicUtil.loadImg(context, url, photoIm, R.mipmap.ic_launcher_round);

        if (url == null) {
            cancelBtn.setVisibility(View.GONE);
        } else {
            cancelBtn.setVisibility(View.VISIBLE);
        }

        photoIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url == null) {
                    // 拍照
                    context.sendBroadcast(new Intent(TakeTaskPhotoActivity_.class.getName())
                            .putExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_PHOTO, true));
                } else {
                    // 查看大图
                    context.sendBroadcast(new Intent(TakeTaskPhotoActivity_.class.getName())
                            .putExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_SHOW, true)
                            .putExtra(Contant.BroadcastKey.BEAN, url));
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(TakeTaskPhotoActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_CANCEL, true)
                        .putExtra(Contant.BroadcastKey.POSITION, position)
                        .putExtra(Contant.BroadcastKey.BEAN, url));
            }
        });
    }
}
