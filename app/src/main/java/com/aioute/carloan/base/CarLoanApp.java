package com.aioute.carloan.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import cn.sft.base.application.BaseApplication;

/**
 * Created by Administrator on 2017/12/19.
 */

public class CarLoanApp extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
