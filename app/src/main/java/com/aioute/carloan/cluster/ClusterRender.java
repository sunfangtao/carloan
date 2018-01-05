package com.aioute.carloan.cluster;

import android.graphics.drawable.Drawable;

public interface ClusterRender {
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     *
     * @param cluster
     * @return
     */
    Drawable getDrawable(Cluster cluster);
}
