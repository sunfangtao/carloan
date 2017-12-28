package com.aioute.carloan.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.PhotographBean;
import com.aioute.carloan.util.PicUtil;
import com.bumptech.glide.Glide;
import com.flysand.ninegrid.ImageInfo;
import com.flysand.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;

/**
 * Created by FlySand on 2017/12/27.
 */

public class PhotographUntreatedAdapter extends BaseAdapter {


    public PhotographUntreatedAdapter(Context context, BaseFragment fragment, List<?> list) {
        super(context, fragment, list);
    }

    @Override
    public int onCreateViewLayoutID(int i) {
        return R.layout.item_photograph_untreated;
    }

    @Override
    public void onBindViewHolder(int i, View view, int i1) {

        PhotographBean bean = (PhotographBean) getObjcet(i1);

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

        //3张照片
        ImageView[] photoViews = new ImageView[3];
        photoViews[0] = view.findViewById(R.id.item_photograph_untreated_photo_0);
        photoViews[1] = view.findViewById(R.id.item_photograph_untreated_photo_1);
        photoViews[2] = view.findViewById(R.id.item_photograph_untreated_photo_2);

        //头像
        Glide.with(context).load(R.mipmap.ic_launcher).into(headpicIv);
        for (int j = 0; j < photoViews.length; j++) {
            PicUtil.loadImage(context, bean.getPhotos()[j], photoViews[j]);
            Glide.with(context).load(bean.getPhotos()[j]).into(photoViews[j]);
            photoViews[j].setOnClickListener(new ImagePreviewListener(bean.getPhotos()));
        }
    }

    private class ImagePreviewListener implements View.OnClickListener {

        private String[] pohotos;

        public ImagePreviewListener(String[] pohotos) {
            this.pohotos = pohotos;
        }

        @Override
        public void onClick(View v) {

            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < pohotos.length; i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(pohotos[i]);
                info.setBigImageUrl(pohotos[i]);
                imageInfo.add(info);
            }
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

}
