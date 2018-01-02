package com.aioute.carloan.cluster;

import com.amap.api.maps.model.CameraPosition;

public interface MapChangedListener {
    /**
     * 点击聚合点的回调处理函数
     */
    public void onCameraChangeFinish(CameraPosition cameraPosition);
}
