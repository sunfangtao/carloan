package com.aioute.carloan.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aioute.carloan.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.util.Util;

public class WarnSettingAdapter extends BaseAdapter {

    Map<Integer, Boolean> selectMap;

    public WarnSettingAdapter(Context context, List<String> list) {
        super(context, list);
        selectMap = new HashMap<>();
    }

    public void setAllSelect(boolean allSelect) {
        if (allSelect) {
            for (int i = 0; i < getItemCount(); i++) {
                selectMap.put(i, true);
            }
        } else {
            selectMap.clear();
        }
        notifyDataSetChanged();
    }

    public Map<Integer, Boolean> getSelectMap(){
        return selectMap;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }

    @Override
    public View onCreateViewNoLayoutID() {
        AppCompatCheckBox checkBox = new AppCompatCheckBox(context, null, android.R.attr.checkboxStyle);
        checkBox.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dp2px(context, 45)));
        return checkBox;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        AppCompatCheckBox checkBox = (AppCompatCheckBox) view;

        checkBox.setOnCheckedChangeListener(null);

        checkBox.setText((String) getObjcet(position));
        checkBox.setChecked(selectMap.get(position) != null ? selectMap.get(position) : false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectMap.put(position, isChecked);
                notifyDataSetChanged();
            }
        });
    }
}