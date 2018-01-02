package com.aioute.carloan.cluster;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Cluster {

    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;
    private int status = 0;

    Cluster(LatLng latLng) {
        mLatLng = latLng;
        mClusterItems = new ArrayList<ClusterItem>();
    }

    void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
        status |= clusterItem.getStatus();
    }

    int getClusterCount() {
        return mClusterItems.size();
    }


    LatLng getCenterLatLng() {
        return mLatLng;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }

    int getStatus() {
        return status;
    }

}
