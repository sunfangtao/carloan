package com.aioute.carloan.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aioute.carloan.R;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;
import cn.sft.util.Util;

public class DeviceDetailDeviceAdapter extends BaseAdapter {

    public DeviceDetailDeviceAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public DeviceDetailDeviceAdapter(Context context, BaseFragment fragment, List<String> list) {
        super(context, fragment, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }

    @Override
    public View onCreateViewNoLayoutID() {
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.colorBlackText));
        textView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setHeight(Util.dp2px(context, 38));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setPadding((int) context.getResources().getDimension(R.dimen.activity_padding_leftright), 0,
                (int) context.getResources().getDimension(R.dimen.activity_padding_leftright), 0);
        return textView;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {
        TextView textView = (TextView) view;
        textView.setText((String) getObjcet(position));
    }
}