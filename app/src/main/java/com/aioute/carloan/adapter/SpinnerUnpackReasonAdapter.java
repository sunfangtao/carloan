package com.aioute.carloan.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aioute.carloan.R;

import java.util.List;

import cn.sft.util.Util;
import cn.sft.view.AlwaysMarqueeTextView;

public class SpinnerUnpackReasonAdapter extends BaseAdapter {

    private Context context;
    private List<String> reasonList;

    public SpinnerUnpackReasonAdapter(Context context, List<String> reasonList) {
        this.context = context;
        this.reasonList = reasonList;
    }

    @Override
    public int getCount() {
        return reasonList.size();
    }

    @Override
    public String getItem(int position) {
        return reasonList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = new AlwaysMarqueeTextView(context);
            ((AlwaysMarqueeTextView) convertView).setTextColor(context.getResources().getColor(R.color.colorAccent));
            ((AlwaysMarqueeTextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
            ((AlwaysMarqueeTextView) convertView).setDrawableHeight(Util.dp2px(context, 20));
            ((AlwaysMarqueeTextView) convertView).setDrawableWidth(Util.dp2px(context, 20));
            ((AlwaysMarqueeTextView) convertView).setHeight(Util.dp2px(context, 45));
            ((AlwaysMarqueeTextView) convertView).setCompoundDrawables(null, null, context.getResources().getDrawable(R.mipmap.user_icon_right), null);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            convertView.setPadding(Util.dp2px(context, 16), 0, Util.dp2px(context, 16), 0);
            holder.nameTv = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String reason = getItem(position);

        holder.nameTv.setText(reason);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = new TextView(context);
            ((TextView) convertView).setTextColor(context.getResources().getColor(R.color.colorBlackText));
            ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Util.dp2px(context, 45)));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            convertView.setPadding(Util.dp2px(context, 16), 0, 0, 0);
            holder.nameTv = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String reason = getItem(position);

        holder.nameTv.setText(reason);

        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
    }

}

