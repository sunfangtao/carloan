package com.aioute.carloan.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.MainActivity_;
import com.aioute.carloan.adapter.InfoWindowAdapter;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.RegionItemBean;
import com.aioute.carloan.bean.UserBean;
import com.aioute.carloan.cluster.ClusterClickListener;
import com.aioute.carloan.cluster.ClusterItem;
import com.aioute.carloan.cluster.ClusterOverlay;
import com.aioute.carloan.cluster.ClusterRender;
import com.aioute.carloan.cluster.MapChangedListener;
import com.aioute.carloan.common.Contant;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;

import static com.aioute.carloan.common.Contant.MAP_MAX_ZOOM_LEVEL;

/**
 * Created by Administrator on 2017/12/25.
 * 监控
 */

@EFragment(R.layout.fragment_monitor)
public class MonitorFragment extends CustomBaseFragment implements MapChangedListener, AMap.InfoWindowAdapter, ClusterClickListener, AMap.OnMapClickListener {

    // 地图
    @ViewById(R.id.monitor_map)
    MapView mapView;
    // 搜索框
    @ViewById(R.id.monitor_searchView)
    SearchView searchView;
    // 前一个Marker
    @ViewById(R.id.monitor_pre_btn)
    ImageButton preMarkerBtn;
    // 后一个Marker
    @ViewById(R.id.monitor_next_btn)
    ImageButton nextMarkerBtn;
    // 当前显示Marker
    Marker currentMarker;
    // 点聚合层
    ClusterOverlay mClusterOverlay;
    //---------------------------------------------------------------------

    @Override
    protected void afterViews() {
        initSearchView();
        initMapListener();

        List<ClusterItem> items = new ArrayList<ClusterItem>();
        //随机10000个点
        for (int i = 0; i < 100; i++) {
            double lat = Math.random() + 39.474923;
            double lon = Math.random() + 116.027116;
            LatLng latLng = new LatLng(lat, lon, false);
            RegionItemBean regionItem = new RegionItemBean(latLng, "test" + i, 0);
            items.add(regionItem);

        }
        ClusterRender render = new ClusterRender() {
            @Override
            public Drawable getDrawable(int clusterNum) {
                return null;
            }
        };
        mClusterOverlay = new ClusterOverlay(mapView.getMap(), Util.dp2px(getActivity(), 50), getActivity());
        mClusterOverlay.setOnClusterClickListener(this);
        mClusterOverlay.setOnMapChagnedListener(this);
//        mClusterOverlay.setClusterRenderer(render);
        mClusterOverlay.setClusterItems(items);

        Util.print("afterViews");
    }

    /**
     * SearchView属性修改
     */
    void initSearchView() {
        LinearLayout searchLayout = searchView.findViewById(getResources().getIdentifier("android:id/search_edit_frame", null, null));
        LinearLayout searchPlateLayout = searchView.findViewById(getResources().getIdentifier("android:id/search_plate", null, null));
        EditText searchEditText = searchView.findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));

        searchEditText.setTextColor(getResources().getColor(R.color.colorBlackText));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorGrayText));
        searchPlateLayout.setBackgroundDrawable(new GradientDrawable());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.colorTransGray));
        drawable.setStroke(Util.dp2px(getActivity(), 2), getResources().getColor(R.color.colorBlueText));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50f);
        searchLayout.setBackgroundDrawable(drawable);
    }

    void initMapListener() {
        mapView.getMap().setInfoWindowAdapter(this);
        mapView.getMap().setOnMapClickListener(this);
    }

    @Click(R.id.monitor_pre_btn)
    void preMarkerBtnClick() {

    }

    @Click(R.id.monitor_next_btn)
    void nextMarkerBtnClick() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mClusterOverlay.onDestroy();
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
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Util.print("query=" + query);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Util.print("newText=" + newText);
                return false;
            }
        });

    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        closeInfoWindow();
        currentMarker = marker;
        if (clusterItems.size() == 1) {
            // 只有一个点，点击显示详情的InfoWindow
            currentMarker.showInfoWindow();
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (cameraPosition.zoom == MAP_MAX_ZOOM_LEVEL) {
            preMarkerBtn.setVisibility(View.VISIBLE);
            nextMarkerBtn.setVisibility(View.VISIBLE);
        } else {
            preMarkerBtn.setVisibility(View.GONE);
            nextMarkerBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        closeInfoWindow();
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
        List deviceDataList = new ArrayList<>();
        deviceDataList.add("1111111111111111111111111111111111111111111111111111111");
        deviceDataList.add(marker.getTitle());
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");

        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(getActivity(), this, deviceDataList);
        RecyclerView recyclerView = view.findViewById(R.id.infowindow_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(infoWindowAdapter);

        view.findViewById(R.id.infowindow_detail_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 0)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_position_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 1)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_trace_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 2)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_setting_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 3)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

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
