package com.aioute.carloan.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.MainFunctionAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.bean.model.MainFunctionModel;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.listener.RecyclerViewItemClickListener;
import cn.sft.taghandler.FontSizeTagHandler;
import cn.sft.util.MyHandler;
import cn.sft.util.Util;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * 登录后的主界面
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends CustomBaseActivity implements RecyclerViewItemClickListener {

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
    AlwaysMarqueeTextView carCountTV;
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
        updateDeviceTypeCount(8, 4, 3, 8);

        initUI();

        new MyHandler(2000){
            @Override
            public void run() {
                updateWarnBadge("20");
            }
        };
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
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mainFunctionRV.setLayoutManager(layoutManager);

        int deviderPx = Util.dp2px(this, 2);
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(deviderPx);
        mainFunctionRV.addItemDecoration(decoration);

        int viewHeight = (getResources().getDisplayMetrics().widthPixels - spanCount * deviderPx) / spanCount;
        MainFunctionAdapter mAdapter = new MainFunctionAdapter(this, mainFunctionModelList = new ArrayList<>(), viewHeight);
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

        int cornerRadius = Util.dp2px(this, 5);
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

        // 宽高变为1:1
        carCountTV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                carCountTV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                carCountTV.setHeight(carCountTV.getWidth());
            }
        });

        updateWarnCarCount(1, 3);
    }

    /**
     * 更新报警的车的数量
     *
     * @param count 报警数量
     * @param total 车总数量
     */
    void updateWarnCarCount(int count, int total) {
        if (count < 0 || total <= 0) {
            throw new IllegalArgumentException("数量错误！");
        }

        int outerWidth = Util.dp2px(MainActivity.this, 2);
        int innerWidth = Util.dp2px(MainActivity.this, 15) + outerWidth;

        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setColor(getResources().getColor(R.color.colorAccent));
        drawable1.setStroke(outerWidth, getResources().getColor(R.color.colorGray));
        drawable1.setShape(GradientDrawable.OVAL);

        ArcShape arcShape = new ArcShape(-90, 360f * count / total);
        ShapeDrawable drawable2 = new ShapeDrawable(arcShape);
        drawable2.getPaint().setColor(getResources().getColor(R.color.colorPrimary));
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.setPadding(outerWidth, outerWidth, outerWidth, outerWidth);

        GradientDrawable drawable3 = new GradientDrawable();
        drawable3.setColor(getResources().getColor(R.color.colorWhite));
        drawable3.setStroke(outerWidth, getResources().getColor(R.color.colorGray));
        drawable3.setShape(GradientDrawable.OVAL);

        LayerDrawable ld = new LayerDrawable(new Drawable[]{drawable1, drawable2, drawable3});
        ld.setLayerInset(1, outerWidth, outerWidth, outerWidth, outerWidth);
        ld.setLayerInset(2, innerWidth, innerWidth, innerWidth, innerWidth);
        carCountTV.setBackgroundDrawable(ld);

        String text = "<customfont size='20sp' color='#00a65a'>" + count
                + "</customfont><br><customfont size='10sp' color='#666666'>报警车辆</customfont>";
        carCountTV.setHtmlText(text, new FontSizeTagHandler(this));
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
        SettingActivity_.intent(MainActivity.this).start();
    }

    @Override
    protected void afterRestoreInstanceState(Bundle bundle) {
        mainFunctionRV.getAdapter().notifyDataSetChanged();
    }
}
