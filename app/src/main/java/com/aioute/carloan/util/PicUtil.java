package com.aioute.carloan.util;

import android.content.Context;
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
}
