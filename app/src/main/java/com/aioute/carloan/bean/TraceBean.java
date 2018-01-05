package com.aioute.carloan.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2018/1/5.
 * 轨迹点
 */

public class TraceBean extends DBVO {

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
