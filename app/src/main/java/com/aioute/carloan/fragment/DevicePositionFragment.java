package com.aioute.carloan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.SingleDeviceOperActivity_;
import com.aioute.carloan.adapter.InfoWindowAdapter;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.MapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    // 轨迹控制布局
    @ViewById(R.id.position_trace_controll_layout)
    RelativeLayout traceLayout;
    // 追踪控制布局
    @ViewById(R.id.position_position_controll_layout)
    LinearLayout positionLayout;
    // SeekBar
    @ViewById(R.id.position_seekbar)
    SeekBar seekBar;
    // 轨迹开始暂停按钮
    @ViewById(R.id.position_trace_ck)
    CheckBox traceCK;
    // 日期选择
    @ViewById(R.id.postion_dp)
    DatePicker datePicker;

    // 定位Marker
    Marker currentMarker;
    //
    AMap aMap;
    // 刷新定位位置定时器
    MyHandler positionHandler;
    // 行驶路线
    Polyline polyline;
    // Marker
    Marker curMarker;
    //
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //---------------------------------------------------------------------
    // 是否显示InfoWindow
    boolean isShowInfoWindow;
    int time = 0;
    // 是否显示定位
    boolean isShowPosition = false;
    // 车辆位置
    ArrayList<LatLng> latLngList = new ArrayList<>();
    // 轨迹日期列表
    List<String> dateList = new ArrayList<>();
    // 年月日
    int selectYear, selectMonth, selectDay;

    @Override
    protected void afterViews() {
        initMap();
        initMapListener();
        initListener();
    }

    void initMap() {
        aMap = mapView.getMap();
    }

    void initMapListener() {
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    void initListener() {
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

        datePicker.init(selectYear, selectMonth, selectDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectYear = year;
                selectMonth = monthOfYear;
                selectDay = dayOfMonth;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DATE, dayOfMonth);

                calendar.add(Calendar.DATE, -7);
                for (int i = 0; i < 7; i++) {
                    calendar.add(Calendar.DATE, 1);
                    dateList.add(simpleDateFormat.format(calendar.getTime()));
                }
                updateToolbarMenu(0);
            }
        });
    }

    /**
     * 添加轨迹路线,
     */
    // TODO 页面关闭需要处理
    Polyline addRoute() {
        if (latLngList.size() > 1) {
            if (polyline != null) {
                polyline.remove();
            }
            return polyline = aMap.addPolyline(new PolylineOptions().
                    addAll(latLngList).width(Util.dp2px(getActivity(), 5)).color(Color.argb(255, 1, 1, 1)));
        }
        return null;
    }

    /**
     * 添加车辆Marker,添加后划线
     *
     * @param resourceId 车辆图片
     * @param title      显示文字
     * @param latLng     经纬度
     * @return
     */
    Marker addMarker(@DrawableRes int resourceId, String title, LatLng latLng) {
        final View markerView = View.inflate(getActivity(), R.layout.marker_layout, null);
        ImageView statusImg = markerView.findViewById(R.id.marker_img);
        TextView deviceNumTV = markerView.findViewById(R.id.marker_title_tv);

        statusImg.setBackgroundResource(resourceId);
        deviceNumTV.setText(title);

        Bitmap bitmap = MapUtil.convertViewToBitmap(markerView);
        if (currentMarker != null) {
            MarkerOptions options = currentMarker.getOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            currentMarker.setMarkerOptions(options);
            return currentMarker;
        } else {
            return currentMarker = aMap.addMarker(new MarkerOptions().anchor(25f / bitmap.getWidth(), 1)
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        }
    }

    public void setShowPosition(boolean isShowPosition) {
        this.isShowPosition = isShowPosition;
    }

    @CheckedChange(R.id.position_trace_ck)
    void traceStartEndChanged(boolean isChanged) {

    }

    /**
     * 开启追踪
     */
    void startPosition() {
        exitPosition();
        positionHandler = new MyHandler(2000, true) {
            @Override
            public void run() {
                getCurPosition();
            }
        };
    }

    /**
     * 初始化页面
     */
    void initPositionTraceUI(boolean isShowPosition) {
        traceLayout.setVisibility(isShowPosition ? View.GONE : View.VISIBLE);
        positionLayout.setVisibility(!isShowPosition ? View.GONE : View.VISIBLE);
        // toolbar 修改
        getActivity().sendBroadcast(new Intent(SingleDeviceOperActivity_.class.getName())
                .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_REFRESH, true)
                .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_TIME, ""));
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        Util.print("onHiddenChanged isHidden=" + isHidden);
        if (isHidden) {
            // 停止轨迹和跟踪
            exitPosition();
            exitTrace();
        } else {
            // 开始追踪或者轨迹
            initPositionTraceUI(isShowPosition);
            if (isShowPosition) {
                startPosition();
            } else {
                showTraceTime();
            }
        }
    }

    /**
     * 停止追踪
     */
    void exitPosition() {
        if (positionHandler != null) {
            positionHandler.cancle();
            positionHandler = null;
        }
        latLngList.clear();
        if (polyline != null) {
            polyline.remove();
        }
        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
    }

    /**
     * 获取那一天的轨迹日期选择
     */
    public void showTraceDay() {
        if (dateList.size() > 0) {
            new AlertDialog.Builder(getActivity()).setItems(dateList.toArray(new String[]{}), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateToolbarMenu(which);
                }
            }).show();
        }
    }

    /**
     * 获取轨迹查询日期
     */
    public void showTraceTime() {

        final String[] array = getResources().getStringArray(R.array.trace_time_array);
        new AlertDialog.Builder(getActivity()).setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == array.length) {
                    // 自定义时间
                } else {
                    Calendar cal = Calendar.getInstance();
                    dateList.clear();
                    switch (which) {
                        case 0:
                            // 今天
                            dateList.add(simpleDateFormat.format(cal.getTime()));
                            break;
                        case 1:
                            // 昨天
                            cal.add(Calendar.DATE, -1);
                            dateList.add(simpleDateFormat.format(cal.getTime()));
                            break;
                        case 2:
                            // 近7天
                            cal.add(Calendar.DATE, -7);
                            for (int i = 0; i < 7; i++) {
                                cal.add(Calendar.DATE, 1);
                                dateList.add(simpleDateFormat.format(cal.getTime()));
                            }
                            break;
                    }
                    updateToolbarMenu(0);
                }
            }
        }).show();
    }

    /**
     * 更新Toolbar菜单
     */
    void updateToolbarMenu(int index) {
        if (dateList.size() > index) {
            getActivity().sendBroadcast(new Intent(SingleDeviceOperActivity_.class.getName())
                    .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_REFRESH, true)
                    .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_TIME, dateList.get(index)));
        }

    }

    /**
     * 初始化轨迹数据
     */
    public void initTraceData(String startTime, String endTime) {

    }

    /**
     * 退出轨迹播放
     */
    void exitTrace() {

    }

    /**
     * 暂停轨迹
     */
    void pauseTrace() {

    }

    /**
     * 开始播放轨迹
     */
    void startTrace() {

    }

    void getCurPosition() {
        time++;
        LatLng latLng = new LatLng(36.2 + time * 0.01, 121.2 + time * 0.01);
        latLngList.add(latLng);
        addRoute();
        addMarker(R.mipmap.car_gray, "138138138012", latLng);

        if (isShowInfoWindow && currentMarker != null) {
            curMarker.showInfoWindow();
        }

        aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onDestroyView() {
        Util.print("onDestroyView");
        super.onDestroyView();
        mapView.onDestroy();
        exitPosition();
        exitTrace();
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
