package com.aioute.carloan.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flysand.ninegrid.ImageInfo;
import com.flysand.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/28.
 */

public class PicUtil {

    public static void loadImg(Context context, String url, ImageView imageView, int resourceId) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(resourceId).into(imageView);
        } else {
            Glide.with(context).load(url).into(imageView);
        }
    }

    public static void loadImage(Context context, @DrawableRes int resId, final ImageView view) {
        try {
            Glide.with(context).load(resId)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
//            view.setBackgroundResource(R.drawable.default_seat_h);
        }
    }

    public static void loadImage(Context context, String url, final ImageView view) {

        try {
            Glide.with(context).load(url).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showBigImages(Context context, ArrayList<ImageInfo> imageInfo, int currentItem) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, currentItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
