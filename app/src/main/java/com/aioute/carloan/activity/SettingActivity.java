package com.aioute.carloan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.taghandler.FontSizeTagHandler;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/23.
 * 设置界面
 */

@EActivity(R.layout.activity_setting)
public class SettingActivity extends CustomBaseActivity {

    // 监控刷新时间
    @ViewById(R.id.settting_monitor_tv)
    AlwaysMarqueeTextView monitorRefreshTimeTV;
    // 追踪刷新时间
    @ViewById(R.id.settting_trace_tv)
    AlwaysMarqueeTextView traceRefreshTimeTV;
    // 报警信息设置
    @ViewById(R.id.settting_warn_tv)
    AlwaysMarqueeTextView warnSettingTV;
    // 修改密码
    @ViewById(R.id.settting_fixpass_tv)
    AlwaysMarqueeTextView fixPasswordTV;

    @Override
    protected void afterViews() {

    }

    @Override
    protected void noSaveInstanceStateForCreate() {
        // 设置默认刷新时间
        updateMonitorTime("30秒");
        updateTraceTime("30秒");
    }

    @Click({R.id.settting_monitor_tv, R.id.settting_trace_tv})
    void monitorRefreshTVClick(final AlwaysMarqueeTextView view) {
        final String[] array = getResources().getStringArray(R.array.setting_refresh_time);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(array, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (view.getId()) {
                    case R.id.settting_monitor_tv:
                        updateMonitorTime(array[which]);
                        break;
                    case R.id.settting_trace_tv:
                        updateTraceTime(array[which]);
                        break;
                }
            }
        }).show();
    }

    @Click(R.id.settting_warn_tv)
    void warnInfoSettingClick() {
        WarnSettingActivity_.intent(this).start();
    }

    @Click(R.id.settting_fixpass_tv)
    void fixPasswordClick() {
        FixPasswordActivity_.intent(this).start();
    }

    /**
     * 更新监控刷新时间
     *
     * @param monitorRefreshTime 监控刷新时间 带单位
     */
    void updateMonitorTime(String monitorRefreshTime) {
        monitorRefreshTimeTV.setHtmlText(String.format(getFormatRefreshTime(), getString(R.string.monitor_refresh_time), monitorRefreshTime),
                new FontSizeTagHandler(this));
    }

    /**
     * 更新追踪刷新时间
     *
     * @param traceRefreshTime 追踪刷新时间 带单位
     */
    void updateTraceTime(String traceRefreshTime) {
        traceRefreshTimeTV.setHtmlText(String.format(getFormatRefreshTime(), getString(R.string.trace_refresh_time), traceRefreshTime),
                new FontSizeTagHandler(this));
    }

    /**
     * 获取待格式化的字符串
     *
     * @return
     */
    String getFormatRefreshTime() {
        return "<customfont size='13sp' color='#00a65a'>%1$s</customfont><br><customfont size='10sp' color='#666666'>%2$s</customfont>";
    }

}
