package com.aioute.carloan.activity;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.taghandler.FontSizeTagHandler;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/23.
 */

@EActivity(R.layout.activity_setting)
public class SettingActivity extends CustomBaseActivity {

    // 监控刷新时间
    @ViewById(R.id.text)
    AlwaysMarqueeTextView MonitorRefreshTimeTV;

    @Override
    protected void afterViews() {
        String text = "<customfont size='13sp' color='#00a65a'>监控刷新时间</customfont><br><customfont size='10sp' color='#666666'>10s</customfont>";
        MonitorRefreshTimeTV.setHtmlText(text, new FontSizeTagHandler(this));
    }
}
