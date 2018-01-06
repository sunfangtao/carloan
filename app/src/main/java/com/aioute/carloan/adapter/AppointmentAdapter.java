package com.aioute.carloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.AppointmentActivity_;
import com.aioute.carloan.common.Contant;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/27.
 * 我的任务(未完成和已完成)
 */

public class AppointmentAdapter extends BaseAdapter {

    private Map<Integer, String> valueMap;

    public AppointmentAdapter(Context context, List<String> list) {
        super(context, list);
        valueMap = new HashMap<>();
    }

    public Map<Integer, String> getValueMap() {
        return valueMap;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_appointment;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        Util.print("onBindViewHolder");

        TextView labelTV = view.findViewById(R.id.item_appointment_label_tv);
        final EditText contentET = view.findViewById(R.id.item_appointment_et);

        final String label = (String) getObjcet(position);

        if (position == 1) {
            contentET.setEnabled(false);
        } else {
            contentET.setEnabled(true);
        }
        labelTV.setText(label);

        try {
            Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            Object object = field.get(contentET);
            ((List) object).clear();
            field.setAccessible(false);
        } catch (Exception e) {

        }

        contentET.setText(valueMap.get(position));

        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueMap.put(position, s.toString());
                if (position == 0) {
                    // 车牌号查询车主姓名
                    context.sendBroadcast(new Intent(AppointmentActivity_.class.getName())
                            .putExtra(Contant.BroadcastKey.OBTAIN_NAME_BY_PLATE, true)
                            .putExtra(Contant.BroadcastKey.BEAN, s.toString()));
                }
            }
        });
    }

    public void updateUserName(String userName) {
        valueMap.put(1, userName);
        notifyItemChanged(1);
    }
}
