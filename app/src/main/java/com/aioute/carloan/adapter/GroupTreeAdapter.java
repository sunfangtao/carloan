package com.aioute.carloan.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

    public GroupTreeAdapter(Context context, List<GroupBean> datas, int defaultExpandLevel) {
        super(context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(final Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
            CheckBox ck = new CheckBox(context);
            ck.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            layout.addView(ck);
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(Util.dp2px(context, 20), Util.dp2px(context, 20));
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0,0,Util.dp2px(context,10),0);
            imageView.setLayoutParams(params);
            layout.addView(imageView);
            AlwaysMarqueeTextView textView = new AlwaysMarqueeTextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setDrawableWidth(Util.dp2px(context, 30));
            textView.setDrawableHeight(Util.dp2px(context, 30));
            textView.setCompoundDrawablePadding(Util.dp2px(context, 20));
            textView.setTextColor(context.getResources().getColor(R.color.colorBlackText));
            layout.addView(textView);
            holder = new ViewHolder();
            holder.ck = ck;
            holder.textView = textView;
            holder.imageView = imageView;
            convertView = layout;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (node.isLeaf()) {
            updateLeafValue(holder.textView, "111", "222", "333", "444");
            holder.imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            updateGroupValue(holder.textView, "id=" + node.getId());
            holder.imageView.setVisibility(View.GONE);
        }

        // 设置内边距
        convertView.setPadding(node.getLevel() * Util.dp2px(context, 20), 3, 3, 3);

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
        ImageView imageView;
    }

    private void updateLeafValue(TextView textView, String deviceNum, String deviceType, String plate, String name) {
        textView.setSingleLine(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        textView.setText(String.format(context.getString(R.string.group_leaf_detail), deviceNum, deviceType, plate, name));
    }

    private void updateGroupValue(TextView textView, String name) {
        textView.setSingleLine(true);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setText(name);
    }

}