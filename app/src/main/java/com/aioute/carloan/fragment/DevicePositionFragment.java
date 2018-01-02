package com.aioute.carloan.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.InfoWindowAdapter;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.util.MapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.MyHandler;
import cn.sft.util.Util;

/**
 * Created by Administrator on 2018/1/2.
 */

@EFragment(R.layout.fragment_position)
public class DevicePositionFragment extends CustomBaseFragment implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {

    // 地图
    @ViewById(R.id.position_map)
    MapView mapView;
    // 定位Marker
    Marker currentMarker;
    // SeekBar
    @ViewById(R.id.position_seekbar)
    SeekBar seekBar;
    // 轨迹开始暂停按钮
    @ViewById(R.id.position_trace_ck)
    CheckBox traceCK;

    // 刷新定位位置定时器
    MyHandler positionHandler;
    //---------------------------------------------------------------------
    int time = 0;
    // 是否显示定位
    boolean isShowPosition = false;

    @Override
    protected void afterViews() {
        initMapListener();

        final View markerView = View.inflate(getActivity(), R.layout.marker_layout, null);
        ImageView statusImg = markerView.findViewById(R.id.marker_img);
        TextView deviceNumTV = markerView.findViewById(R.id.marker_title_tv);

        deviceNumTV.setText("asdfasdfasdf");
        Bitmap bitmap = MapUtil.convertViewToBitmap(markerView);
        mapView.getMap().addMarker(new MarkerOptions().anchor(25f / bitmap.getWidth(), 1)
                .position(new LatLng(36.2d, 121.3d))
                .title("asdfasdfasdf")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Util.print("fromUser=" + fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Util.print("onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Util.print("onStopTrackingTouch");
            }
        });
    }

    void initMapListener() {
        mapView.getMap().setOnMarkerClickListener(this);
        mapView.getMap().setInfoWindowAdapter(this);
    }

    public void setShowPosition(boolean isShowPosition) {
        this.isShowPosition = isShowPosition;
    }

    @CheckedChange(R.id.position_trace_ck)
    void traceStartEndChanged(boolean isChanged) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void noSaveInstanceStateForCreate() {
        mapView.onCreate(null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        currentMarker = marker;
        return true;
    }

    /**
     * 关闭InfoWindow
     */
    void closeInfoWindow() {
        if (currentMarker != null && currentMarker.isInfoWindowShown()) {
            currentMarker.hideInfoWindow();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.marker_infowindow, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.marker_infowindow, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    void render(Marker marker, View view) {
        Util.print("render=");
        List deviceDataList = new ArrayList<>();
        deviceDataList.add("1111111111111111111111111111111111111111111111111111111");
        deviceDataList.add(marker.getTitle());
        deviceDataList.add("time=" + time);
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");

        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(getActivity(), this, deviceDataList);
        RecyclerView recyclerView = view.findViewById(R.id.infowindow_rv);
        view.findViewById(R.id.infowindow_operate_layout).setVisibility(View.GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(infoWindowAdapter);
    }

    @Override
    protected void afterRestoreInstanceState(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
