package com.aioute.carloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.UnpackActivity_;
import com.aioute.carloan.bean.UnpackOrderBean;
import com.aioute.carloan.common.Contant;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/12/27.
 * 我的订单(代接订单和已接订单)
 */

public class UnpackAdapter extends BaseAdapter {

    // false 未接订单；true 已接订单
    private boolean isReceived = false;

    public UnpackAdapter(Context context, BaseFragment fragment, List<UnpackOrderBean> list) {
        super(context, fragment, list);
    }

    public void isReceived() {
        this.isReceived = true;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_unpack;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        TextView plateTV = view.findViewById(R.id.unpack_plate_tv);
        TextView deviceNumTimeTV = view.findViewById(R.id.unpack_devicenum_time_tv);
        Button cancelBtn = view.findViewById(R.id.unpack_cancel_btn);
        Button tipBtn = view.findViewById(R.id.unpack_tip_btn);

        final UnpackOrderBean unpackOrderBean = (UnpackOrderBean) getObjcet(position);

        plateTV.setText(String.format(context.getString(R.string.plate), "鲁F123456"));
        deviceNumTimeTV.setText("设备号：123456_有线\n下单时间：2018-10-12 14:20:12");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(UnpackActivity_.class.getName())
                        .putExtra(isReceived ? Contant.BroadcastKey.UNPACK_ITEM_RECEIVED_CANCEL : Contant.BroadcastKey.UNPACK_ITEM_OPEN_CANCEL, true)
                        .putExtra(Contant.BroadcastKey.POSITION, position)
                        .putExtra(Contant.BroadcastKey.BEAN, unpackOrderBean));
            }
        });

        tipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(UnpackActivity_.class.getName())
                        .putExtra(isReceived ? Contant.BroadcastKey.UNPACK_ITEM_PHONE : Contant.BroadcastKey.UNPACK_ITEM_TIP, true)
                        .putExtra(Contant.BroadcastKey.POSITION, position)
                        .putExtra(Contant.BroadcastKey.BEAN, unpackOrderBean));
            }
        });
    }
}
