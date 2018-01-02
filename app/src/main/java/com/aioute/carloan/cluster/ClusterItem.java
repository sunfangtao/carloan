package com.aioute.carloan.cluster;

import com.amap.api.maps.model.LatLng;

public interface ClusterItem {

    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
    LatLng getPosition();

    /**
     * 获取当前状态
     *
     * @return
     */
    int getStatus();

    String getTitle();
}
