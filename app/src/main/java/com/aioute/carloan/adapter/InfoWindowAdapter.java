package com.aioute.carloan.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.aioute.carloan.R;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;

public class InfoWindowAdapter extends BaseAdapter {

    public InfoWindowAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }

    @Override
    public View onCreateViewNoLayoutID() {
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        return textView;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {
        TextView textView = (TextView) view;
        textView.setText((String) getObjcet(position));
    }
}