package com.aioute.carloan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.AlarmInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21.
 */

public class AlarmInfoAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private List<AlarmInfoBean> beanList;

    public AlarmInfoAdapter(Context context, List<AlarmInfoBean> beanList) {
        this.mInflater = LayoutInflater.from(context);
        this.beanList = beanList;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_alarm_info_group_view, null);
            holder = new GroupViewHolder();
            holder.checkBox = convertView.findViewById(R.id.item_alarm_info_cb);
            holder.plateNumberTv = convertView.findViewById(R.id.item_alarm_info_plate_number_tv);
            holder.stateTv = convertView.findViewById(R.id.item_alarm_info_state_tv);
            holder.typeTv = convertView.findViewById(R.id.item_alarm_info_type_tv);
            holder.timeTv = convertView.findViewById(R.id.item_alarm_info_time_tv);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        try {

            AlarmInfoBean bean = beanList.get(groupPosition);
            holder.plateNumberTv.setText(bean.getPlateNumber());
            holder.stateTv.setText(bean.getState());
            holder.typeTv.setText(bean.getType());
            holder.timeTv.setText(bean.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_alarm_info_child_view, null);
            holder = new ChildViewHolder();
            holder.childTv = convertView.findViewById(R.id.item_alarm_info_child_tv);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        holder.childTv.setText(beanList.get(groupPosition).getDescribe());

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return beanList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        public CheckBox checkBox;
        public TextView plateNumberTv;
        public TextView typeTv;
        public TextView stateTv;
        public TextView timeTv;
    }

    private class ChildViewHolder {
        public TextView childTv;
    }
}
