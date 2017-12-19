package com.aioute.carloan.base;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2017/12/19.
 */

@EActivity
public class CustomBaseActivity extends cn.sft.base.activity.DefaultResponseParseActivity {

    @Override
    protected boolean isTitleCenter() {
        return false;
    }

    public void setContentView(int var1) {
        super.setContentView(var1);
        setTitleColor(Color.parseColor("#FFFFFF"));
    }

    public void setContentView(View var1, ViewGroup.LayoutParams var2) {
        super.setContentView(var1, var2);
        setTitleColor(Color.parseColor("#FFFFFF"));
    }

    public void setContentView(View var1) {
        super.setContentView(var1);
        setTitleColor(Color.parseColor("#FFFFFF"));
    }

    @AfterViews
    protected void afterViews() {

    }
}
