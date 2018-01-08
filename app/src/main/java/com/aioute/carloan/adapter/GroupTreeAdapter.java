package com.aioute.carloan.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.GroupBean;

import java.util.List;

import cn.sft.tree.Node;
import cn.sft.tree.TreeListViewAdapter;
import cn.sft.util.Util;
import cn.sft.view.AlwaysMarqueeTextView;

public class GroupTreeAdapter extends TreeListViewAdapter {

    private final int LEAF_ITEM = 1;
    private final int GROUP_ITEM = 0;

    public GroupTreeAdapter(Context context, List<GroupBean> datas, int defaultExpandLevel) {
        super(context, datas, defaultExpandLevel);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return ((Node) getItem(position)).isLeaf() ? LEAF_ITEM : GROUP_ITEM;
    }

    @Override
    public View getConvertView(final Node node, int position, View convertView, ViewGroup parent) {
        Util.print("getConvertView position=" + position);
        ViewHolder holder = null;
        switch (getItemViewType(position)) {
            case GROUP_ITEM:
                if (convertView == null) {
                    LinearLayout layout = new LinearLayout(context);
                    layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
                    CheckBox ck = new CheckBox(context);
                    ck.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    layout.addView(ck);
                    holder = new ViewHolder();
                    holder.ck = ck;
                    convertView = layout;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
            case LEAF_ITEM:
                if (convertView == null) {
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
                    CheckBox ck = new CheckBox(context);
                    ck.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    layout.addView(ck);
                    AlwaysMarqueeTextView textView = new AlwaysMarqueeTextView(context);
                    textView.setDrawableWidth(Util.dp2px(context, 30));
                    textView.setDrawableHeight(Util.dp2px(context, 30));
                    textView.setCompoundDrawablePadding(Util.dp2px(context, 10));
                    textView.setTextColor(context.getResources().getColor(R.color.colorBlackText));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                    holder = new ViewHolder();
                    holder.ck = ck;
                    holder.textView = textView;
                    convertView = layout;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
        }

        // 设置内边距
        convertView.setPadding(node.getLevel() * Util.dp2px(context, 20), 3, 3, 3);

        if (holder.textView != null) {
            updateLeafValue(holder.textView, "", "", "", "");
            holder.textView.setCompoundDrawables(context.getResources().getDrawable(R.mipmap.ic_launcher_round), null, null, null);
        }

        holder.ck.setOnCheckedChangeListener(null);

        if (node.isChecked()) {
            holder.ck.setChecked(true);
        } else {
            holder.ck.setChecked(false);
        }

        holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(node, isChecked);
            }
        });

        return convertView;
    }

    class ViewHolder {
        CheckBox ck;
        AlwaysMarqueeTextView textView;
    }

    private void updateLeafValue(TextView textView, String deviceNum, String deviceType, String plate, String name) {
        textView.setSingleLine(false);
        textView.setText(String.format(context.getString(R.string.group_leaf_detail), deviceNum, deviceType, plate, name));
    }
}