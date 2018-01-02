package com.aioute.carloan.bean;

import com.aioute.carloan.cluster.ClusterItem;
import com.amap.api.maps.model.LatLng;

import cn.sft.sqlhelper.DBVO;

public class RegionItemBean extends DBVO implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private int status;

    public RegionItemBean(LatLng latLng, String title, int status) {
        this.mLatLng = latLng;
        this.mTitle = title;
        this.status = status;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
