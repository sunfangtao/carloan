package com.aioute.carloan.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.RegionItemBean;
import com.aioute.carloan.cluster.Cluster;
import com.aioute.carloan.cluster.ClusterItem;
import com.aioute.carloan.cluster.ClusterOverlay;
import com.aioute.carloan.cluster.ClusterRender;
import com.aioute.carloan.cluster.MapChangedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.MyHandler;
import cn.sft.util.Util;

import static com.aioute.carloan.common.Contant.MAP_MAX_ZOOM_LEVEL;

/**
 * Created by Administrator on 2017/12/25.
 * 监控
 */

@EFragment(R.layout.fragment_monitor)
public class MonitorFragment extends CustomBaseFragment implements MapChangedListener {

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
    // 点聚合层
    ClusterOverlay mClusterOverlay;
    //---------------------------------------------------------------------

    @Override
    protected void afterViews() {

        Util.print("afterViews");
        initSearchView();

        List<ClusterItem> items = new ArrayList<ClusterItem>();
        //随机10000个点
        for (int i = 0; i < 500; i++) {
            double lat = Math.random() + 39.474923;
            double lon = Math.random() + 116.027116;
            LatLng latLng = new LatLng(lat, lon, false);
            RegionItemBean regionItem = new RegionItemBean(latLng, "test" + i, 0);
            items.add(regionItem);

        }
        ClusterRender render = new ClusterRender() {
            @Override
            public Drawable getDrawable(Cluster cluster) {
                // TODO 根据cluster中点状态设置不同颜色
                int strokeColor = Color.parseColor("#03A964");
                int fillColor = Color.parseColor("#45BE37");
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(fillColor);
                drawable.setStroke(Util.dp2px(getActivity(), 1), strokeColor);
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setUseLevel(true);
                drawable.setCornerRadius(20);

                return drawable;
            }
        };
        mClusterOverlay = new ClusterOverlay(mapView.getMap(), Util.dp2px(getActivity(), 50), getActivity());
        mClusterOverlay.setOnMapChagnedListener(this);
        mClusterOverlay.setClusterRenderer(render);
        mClusterOverlay.setClusterItems(items);
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
        new MyHandler(5000){
            @Override
            public void run() {
                mapView.onResume();
            }
        };
        Util.print("onResume");
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
    protected void afterRestoreInstanceState(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
