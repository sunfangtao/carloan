package com.aioute.carloan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aioute.carloan.R;
import com.aioute.carloan.util.PicUtil;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;

/**
 * Created by Administrator on 2017/12/28.
 */

public class PhotoUntreatedItemAdapter extends BaseAdapter {

    private int height;

    public PhotoUntreatedItemAdapter(Context context, List<String> list, int width) {
        super(context, list);
        this.height = width;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }

    @Override
    public View onCreateViewNoLayoutID() {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        linearLayout.setLayoutParams(params);
        linearLayout.addView(imageView);

        return linearLayout;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        LinearLayout linearLayout = (LinearLayout) view;
        final String url = (String) getObjcet(position);
        PicUtil.loadImg(context, url, (ImageView) linearLayout.getChildAt(0), R.mipmap.ic_launcher_round);
    }
}
