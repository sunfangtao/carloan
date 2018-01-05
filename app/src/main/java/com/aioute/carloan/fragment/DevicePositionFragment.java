package com.aioute.carloan.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.SingleDeviceOperActivity_;
import com.aioute.carloan.adapter.InfoWindowAdapter;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.TraceBean;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.MapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
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
    LinearLayout traceLayout;
    // 追踪控制布局
    @ViewById(R.id.position_position_controll_layout)
    LinearLayout positionLayout;
    // SeekBar
    @ViewById(R.id.position_seekbar)
    SeekBar seekBar;
    // 轨迹开始暂停按钮
    @ViewById(R.id.position_trace_ck)
    CheckBox traceCK;

    //
    AMap aMap;
    //
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // -----------------------------追踪模式页面参数--------------------------
    // 刷新定位位置定时器
    MyHandler positionHandler;
    // 定位行驶路线
    Polyline positionPolyline;
    // 定位Marker
    Marker curPositionMarker;
    // -----------------------------轨迹模式页面参数---------------------------------
    // 轨迹Marker
    Marker curTraceMarker;
    // 轨迹行驶路线
    Polyline tracePolyline;
    // 轨迹Handler
    MyHandler traceHandler;
    //------------------------------需要保存的数据----------------------------
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
    // 选择日期的轨迹点列表
    List<TraceBean> traceBeanList = new ArrayList<>();
    // 当前绘制的下标，从0开始
    int tracePointIndex = 0;
    // 轨迹播放速度
    int traceSpeed = 1;
    // 是否正在播放
    boolean isShowTrace = false;

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

            boolean isShow = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // TODO 用户手动拖动
                    tracePointIndex = progress;
                    showTrackCurMarker();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                this.isShow = new Boolean(isShowTrace);
                traceCK.setChecked(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (this.isShow) {
                    traceCK.setChecked(true);
                }
            }
        });

    }

    void getCurPosition() {
        time++;
        LatLng latLng = new LatLng(36.2 + time * 0.01, 121.2 + time * 0.01);
        latLngList.add(latLng);
        addPositionRoute();
        addMarker(R.mipmap.car_gray, "138138138012", latLng);

        if (isShowInfoWindow && curPositionMarker != null) {
            curPositionMarker.showInfoWindow();
        }

        aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
     * 停止追踪
     */
    void exitPosition() {
        if (positionHandler != null) {
            positionHandler.cancle();
            positionHandler = null;
        }
        latLngList.clear();
        if (positionPolyline != null) {
            positionPolyline.remove();
        }
        if (curPositionMarker != null) {
            curPositionMarker.remove();
            curPositionMarker = null;
        }
    }

    /**
     * 添加定位路线,
     */
    // TODO 页面关闭需要处理
    Polyline addPositionRoute() {
        if (latLngList.size() > 1) {
            if (positionPolyline != null) {
                positionPolyline.remove();
            }
            return positionPolyline = aMap.addPolyline(new PolylineOptions().
                    addAll(latLngList).width(Util.dp2px(getActivity(), 5)).color(Color.argb(255, 1, 1, 1)));
        }
        return null;
    }

    /**
     * 添加车辆 Marker,添加后划运行线
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
        if (curPositionMarker != null) {
            MarkerOptions options = curPositionMarker.getOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            curPositionMarker.setMarkerOptions(options);
            return curPositionMarker;
        } else {
            return curPositionMarker = aMap.addMarker(new MarkerOptions().anchor(25f / bitmap.getWidth(), 1)
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        }
    }

    public void setShowPosition(boolean isShowPosition) {
        this.isShowPosition = isShowPosition;
    }


    @CheckedChange(R.id.position_trace_ck)
    void traceStartEndChanged(boolean isStart) {
        if (isStart) {
            startTrace();
        } else {
            pauseTrace();
        }
    }

    /**
     * 初始化页面
     */
    void initPositionTraceUI(boolean isShowPosition) {
        traceLayout.setVisibility(isShowPosition ? View.GONE : View.VISIBLE);
        positionLayout.setVisibility(!isShowPosition ? View.GONE : View.VISIBLE);
    }

    /**
     * 更新Toolbar菜单(一般情况下，更新后就要重新获取轨迹)
     */
    void updateToolbarMenu(int index) {
        if (dateList.size() > index) {
            getActivity().sendBroadcast(new Intent(SingleDeviceOperActivity_.class.getName())
                    .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_REFRESH, true)
                    .putExtra(Contant.BroadcastKey.SINGLEDEVICE_MENU_TIME, dateList.get(index)));

            initTraceData(dateList.get(index), dateList.get(index));
        }

    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
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
                if (which == array.length - 1) {
                    // 自定义时间
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            if (datePicker.isShown()) {
                                selectYear = year;
                                selectMonth = month;
                                selectDay = day;

                                Calendar cal = Calendar.getInstance();

                                String curDate = simpleDateFormat.format(cal.getTime());

                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, month);
                                cal.set(Calendar.DAY_OF_MONTH, day);
                                cal.add(Calendar.DATE, -1);
                                dateList.clear();
                                for (int i = 0; i < 7; i++) {
                                    cal.add(Calendar.DATE, 1);
                                    dateList.add(simpleDateFormat.format(cal.getTime()));
                                }

                                int index = dateList.indexOf(curDate);
                                Util.print("index=" + index + " size=" + dateList.size());
                                if (index >= 0) {
                                    // 去掉将来的日期
                                    dateList = dateList.subList(0, index + 1);
                                }
                                updateToolbarMenu(0);
                            }
                        }
                    }, selectYear, selectMonth, selectDay);
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                    datePickerDialog.show();
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
     * 初始化轨迹数据
     */
    public void initTraceData(String startTime, String endTime) {
        Util.print("startTime=" + startTime + " endTime=" + endTime);
        for (int i = 0; i < 200; i++) {
            double lat = 36.7 + 0.01 * i;
            double lng = 121.3 + 0.01 * i;
            TraceBean traceBean = new TraceBean();
            traceBean.setLat(lat);
            traceBean.setLng(lng);

            traceBeanList.add(traceBean);
        }
        seekBar.setMax(traceBeanList.size());

        addTraceRoute();
    }

    /**
     * 退出轨迹播放
     */
    void exitTrace() {
        if (traceCK != null) {
            traceCK.setChecked(false);
        }
        if (seekBar != null) {
            seekBar.setProgress(0);
        }
        if (seekBar != null) {
            mapView.getMap().clear();
        }
        traceBeanList.clear();
        isShowTrace = false;
    }

    /**
     * 暂停轨迹
     */
    void pauseTrace() {
        isShowTrace = false;
        if (traceHandler != null) {
            traceHandler.cancle();
            traceHandler = null;
        }
    }

    /**
     * 开始播放轨迹
     */
    void startTrace() {
        final int length = traceBeanList.size();
        if (tracePointIndex >= length - 1) {
            // 已经播放完成，从头播放
            tracePointIndex = 0;
        }

        if (traceHandler != null) {
            traceHandler.cancle();
            traceHandler = null;
        }

        isShowTrace = true;
        traceHandler = new MyHandler(10, true, 200) {
            @Override
            public void run() {
                if (isShowTrace && length > 0 && tracePointIndex < length) {
                    // 添加 Marker或者修改 Marker 位置
                    showTrackCurMarker();

                    seekBar.setProgress(tracePointIndex);
                    if (++tracePointIndex == length) {
                        // TODO 播放完成
                        toast.setText(String.format(getString(R.string.trace_end_tip), length));
                        tracePointIndex--;
                        traceCK.setChecked(false);
                    }
                } else {
                    this.cancle();
                }
            }
        };
    }

    /**
     * 添加轨迹路线
     */
    Polyline addTraceRoute() {
        if (traceBeanList.size() > 1) {
            mapView.getMap().clear();
            LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
            ArrayList<LatLng> list = new ArrayList<>();
            for (int i = 0; i < traceBeanList.size(); i++) {
                LatLng latLng = new LatLng(traceBeanList.get(i).getLat(), traceBeanList.get(i).getLng());
                list.add(latLng);
                newbounds.include(latLng);
            }
            tracePolyline = aMap.addPolyline(new PolylineOptions().
                    addAll(list).width(Util.dp2px(getActivity(), 5)).color(Color.argb(255, 1, 1, 1)));
            aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_blue))
                    .position(list.get(0)));
            aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_blue))
                    .position(list.get(list.size() - 1)));
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                    Util.dp2px(getActivity(), 50)));
            return tracePolyline;
        }
        return null;
    }

    /**
     * 显示轨迹当前点 Marker
     */
    void showTrackCurMarker() {
        TraceBean traceBean = traceBeanList.get(tracePointIndex);
        LatLng latLng = new LatLng(traceBean.getLat(), traceBean.getLng());
        if (curTraceMarker != null) {
            curTraceMarker.setMarkerOptions(curTraceMarker.getOptions().position(latLng));
        } else {
            curTraceMarker = mapView.getMap().addMarker(new MarkerOptions()
                    .anchor(0.5f, 1f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_blue))
                    .position(latLng));
        }
    }

    @Override
    public void onDestroyView() {
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
        Calendar calendar = Calendar.getInstance();
        selectYear = calendar.get(Calendar.YEAR);
        selectMonth = calendar.get(Calendar.MONTH);
        selectDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        curPositionMarker = marker;
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

        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(getActivity(), deviceDataList);
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
