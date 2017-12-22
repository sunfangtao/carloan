package com.aioute.carloan.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.view.BadgeTextView;

@EActivity(R.layout.activity_main)
public class MainActivity extends CustomBaseActivity {

    // 所在部门
    @ViewById(R.id.main_department_tv)
    TextView departmentTV;
    // 不同状态下的设备数量
    @ViewById(R.id.main_devicetypecount_tv)
    TextView deviceTypeCountTV;
    // 报警信息
    @ViewById(R.id.main_count_tv)
    BadgeTextView warnInfoTV;

    @Override
    protected void afterViews() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#FF0000"));
        drawable.setCornerRadius(50);
        drawable.setShape(GradientDrawable.RECTANGLE);
        departmentTV.setBackgroundDrawable(drawable);
        departmentTV.setText("测试");
        updateDeviceTypeCount(8, 4, 3, 8);

        warnInfoTV.setText("报警信息");
        warnInfoTV.setBadge("10");
        warnInfoTV.setDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round));
        warnInfoTV.setDrawableSize(400, 200);

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
        deviceTypeCountTV.setText(getString(R.string.device_type_count)
                .replace("total", total + "")
                .replace("online", online + "")
                .replace("offline", offline + "")
                .replace("alarm", alarm + ""));
    }
}
