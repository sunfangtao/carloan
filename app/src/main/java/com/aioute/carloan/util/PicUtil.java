package com.aioute.carloan.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

}
