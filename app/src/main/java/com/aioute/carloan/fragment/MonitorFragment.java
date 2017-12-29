package com.aioute.carloan.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseFragment;
import com.amap.api.maps.MapView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/25.
 * 监控
 */

@EFragment(R.layout.fragment_monitor)
public class MonitorFragment extends CustomBaseFragment {

    // 地图
    @ViewById(R.id.monitor_map)
    MapView mapView;
    // 搜索框
    @ViewById(R.id.monitor_searchView)
    SearchView searchView;

    @Override
    protected void afterViews() {
        initSearchView();
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
    protected void afterRestoreInstanceState(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
