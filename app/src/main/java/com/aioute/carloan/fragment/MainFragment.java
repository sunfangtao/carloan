package com.aioute.carloan.fragment;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.PhotoVerifyActivity_;
import com.aioute.carloan.activity.TaskActivity_;
import com.aioute.carloan.adapter.MainFunctionAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseFragment;
import com.aioute.carloan.bean.model.MainFunctionModel;
import com.aioute.carloan.view.WarnCarCountTextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.listener.RecyclerViewItemClickListener;
import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/25.
 * 首页
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends CustomBaseFragment implements RecyclerViewItemClickListener {

    // 所在部门
    @ViewById(R.id.main_department_tv)
    TextView departmentTV;
    // 不同状态下的设备数量
    @ViewById(R.id.main_devicetypecount_tv)
    TextView deviceTypeCountTV;
    // 功能块
    @ViewById(R.id.main_function_rv)
    RecyclerView mainFunctionRV;
    // 报警车辆数量
    @ViewById(R.id.main_warn_carcount_tv)
    WarnCarCountTextView carCountTV;
    // 离线三天以上（有线）
    @ViewById(R.id.main_offline_pb)
    ProgressBar offlinePB;
    @ViewById(R.id.main_offline_tv)
    TextView offlineTV;
    // 两天以上未行驶（有线）
    @ViewById(R.id.main_notravel_pb)
    ProgressBar noTravelPB;
    @ViewById(R.id.main_notravel_tv)
    TextView noTravelTV;
    // 断电报警
    @ViewById(R.id.main_electric_pb)
    ProgressBar electricPB;
    @ViewById(R.id.main_electric_tv)
    TextView electricTV;
    // 电子围栏报警
    @ViewById(R.id.main_rail_pb)
    ProgressBar railPB;
    @ViewById(R.id.main_rail_tv)
    TextView railTV;
    //------------------------------------------------------------
    ArrayList<MainFunctionModel> mainFunctionModelList;

    @Override
    protected void afterViews() {
        departmentTV.setText("测试");
        updateDeviceTypeCount(0, 0, 0, 0);

        initUI();
    }

    void initUI() {
        // departmentTV 添加背景
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#FF0000"));
        drawable.setCornerRadius(50);
        drawable.setShape(GradientDrawable.RECTANGLE);
        departmentTV.setBackgroundDrawable(drawable);

        initMainFunctionUI();
        initMainProgressUI();
        initMainCarCountUI();
    }

    /**
     * 初始化功能块数据，不在此更新角标
     */
    void initMainFunctionUI() {

        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        mainFunctionRV.setLayoutManager(layoutManager);

        int deviderPx = Util.dp2px(getActivity(), 2);
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(deviderPx);
        mainFunctionRV.addItemDecoration(decoration);

        int viewHeight = (getResources().getDisplayMetrics().widthPixels - spanCount * deviderPx) / spanCount;
        MainFunctionAdapter mAdapter = new MainFunctionAdapter(getActivity(), this, mainFunctionModelList = new ArrayList<>(), viewHeight);
        mainFunctionRV.setAdapter(mAdapter);

        // 设备列表
        MainFunctionModel mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.device_list));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModelList.add(mainFunctionModel);

        // 预约拆机
        mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.appointment_unpack));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModelList.add(mainFunctionModel);

        // 报警信息
        mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.warn_info));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModel.setBadge("0");
        mainFunctionModelList.add(mainFunctionModel);

        // 现场拍照
        mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.onsite_photo));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModelList.add(mainFunctionModel);

        // 我的任务
        mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.my_tasks));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModelList.add(mainFunctionModel);

        // 保留功能
        mainFunctionModel = new MainFunctionModel();
        mainFunctionModel.setTitle(getString(R.string.coming_soon));
        mainFunctionModel.setDrawableId(R.mipmap.ic_launcher_round);
        mainFunctionModelList.add(mainFunctionModel);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化进度条数据，不在此更新进度
     */
    void initMainProgressUI() {

        int cornerRadius = Util.dp2px(getActivity(), 5);
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#DDDDDD"));
        background.setCornerRadius(cornerRadius);
        GradientDrawable p = new GradientDrawable();
        p.setCornerRadius(cornerRadius);

        p.setColor(Color.parseColor("#FF0000"));
        ClipDrawable offlineCD = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable offlineLD = new LayerDrawable(new Drawable[]{background, offlineCD});
        offlinePB.setProgressDrawable(offlineLD);
        updateOfflinePBAndValue(0, 0);

        p.setColor(Color.parseColor("#FFFF00"));
        ClipDrawable noTravelCD = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable noTravelLD = new LayerDrawable(new Drawable[]{background, noTravelCD});
        noTravelPB.setProgressDrawable(noTravelLD);
        updateNoTravelPBAndValue(0, 0);

        p.setColor(Color.parseColor("#00FF00"));
        ClipDrawable electricCD = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable electricLD = new LayerDrawable(new Drawable[]{background, electricCD});
        electricPB.setProgressDrawable(electricLD);
        updateBrokeElectricPBAndValue(0, 0);

        p.setColor(Color.parseColor("#0000FF"));
        ClipDrawable railCD = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable railLD = new LayerDrawable(new Drawable[]{background, railCD});
        railPB.setProgressDrawable(railLD);
        updateRailWarnPBAndValue(0, 0);

    }

    /**
     * 初始化报警车辆数据，不在此更新数量
     */
    void initMainCarCountUI() {
        updateWarnCarCount(0, 0);
    }

    /**
     * 更新报警的车的数量
     *
     * @param count 报警数量
     * @param total 车总数量
     */
    void updateWarnCarCount(int count, int total) {
        carCountTV.setCount(count, total);
    }

    /**
     * 更新不同状态下的设备数量
     *
     * @param total   全部设备
     * @param online  在线设备
     * @param offline 离线设备
     * @param alarm   报警设备
     */
    void updateDeviceTypeCount(int total, int online, int offline, int alarm) {

        if (online + offline > total || alarm > total) {
            throw new IllegalArgumentException("设备数量参数错误！");
        }
        deviceTypeCountTV.setText(String.format(getString(R.string.device_type_count), total, online, offline, alarm));
    }

    /**
     * 更新报警角标
     *
     * @param badge 角标值
     */
    void updateWarnBadge(String badge) {
        if (TextUtils.isEmpty(badge))
            badge = "0";
        updateBadge(badge, 2);
    }

    /**
     * 更新角标
     *
     * @param badge 角标值
     * @param index 下标值（0开始）
     */
    void updateBadge(String badge, int index) {
        if (index < 0 || index >= mainFunctionModelList.size()) {
            throw new IllegalArgumentException("index范围异常！");
        }

        mainFunctionModelList.get(index).setBadge(badge);
        mainFunctionRV.getAdapter().notifyItemChanged(index);
    }

    /**
     * 更新离线车数量（进度条）
     *
     * @param offlineCount 离线数量
     * @param total        车总数量
     */
    void updateOfflinePBAndValue(int offlineCount, int total) {
        if (offlineCount > total) {
            throw new IllegalArgumentException("车数量参数错误！");
        }
        offlinePB.setProgress(total == 0 ? 0 : (int) (offlineCount * 100f / total));
        offlineTV.setText(String.format(getString(R.string.device_type_percent_count), offlinePB.getProgress(), offlineCount));
    }

    /**
     * 更新无轨迹车数量（进度条）
     *
     * @param noTravelCount
     * @param total
     */
    void updateNoTravelPBAndValue(int noTravelCount, int total) {
        if (noTravelCount > total) {
            throw new IllegalArgumentException("车数量参数错误！");
        }
        noTravelPB.setProgress(total == 0 ? 0 : (int) (noTravelCount * 100f / total));
        noTravelTV.setText(String.format(getString(R.string.device_type_percent_count), noTravelPB.getProgress(), noTravelCount));
    }

    /**
     * 更新断电车数量（进度条）
     *
     * @param electricCount
     * @param total
     */
    void updateBrokeElectricPBAndValue(int electricCount, int total) {
        if (electricCount > total) {
            throw new IllegalArgumentException("车数量参数错误！");
        }
        electricPB.setProgress(total == 0 ? 0 : (int) (electricCount * 100f / total));
        electricTV.setText(String.format(getString(R.string.device_type_percent_count), electricPB.getProgress(), electricCount));
    }

    /**
     * 更新围栏报警车数量（进度条）
     *
     * @param railWarnCount
     * @param total
     */
    void updateRailWarnPBAndValue(int railWarnCount, int total) {
        if (railWarnCount > total) {
            throw new IllegalArgumentException("车数量参数错误！");
        }
        railPB.setProgress(total == 0 ? 0 : (int) (railWarnCount * 100f / total));
        railTV.setText(String.format(getString(R.string.device_type_percent_count), railPB.getProgress(), railWarnCount));
    }

    @Override
    public void onRecyclerViewItemClick(BaseAdapter baseAdapter, View view, int index) {
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                // 拍照检查
                PhotoVerifyActivity_.intent(getActivity()).start();
                break;
            case 4:
                // 我的任务
                TaskActivity_.intent(getActivity()).start();
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        if (!isHidden) {
            // 刷新数据
            refreshUIData();
        }
    }

    /**
     * 刷新界面数据
     */
    void refreshUIData() {
        // TODO 快速切换是否控制，防止重复操作
    }

}
