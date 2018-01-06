package com.aioute.carloan.fragment;

import com.aioute.carloan.bean.UnpackOrderBean;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Administrator on 2017/12/27.
 * 已接订单
 */

@EFragment
public class ReceivedOrderFragment extends OpenOrderFragment {

    protected boolean isReceived() {
        return true;
    }

    void getTask() {
        UnpackOrderBean unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);

        unpackOrderBean = new UnpackOrderBean();
        orderList.add(unpackOrderBean);
    }

}
