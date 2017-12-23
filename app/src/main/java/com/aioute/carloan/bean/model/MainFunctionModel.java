package com.aioute.carloan.bean.model;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2017/12/23.
 * 主页面功能块
 */

public class MainFunctionModel extends DBVO {

    private int drawableId;
    private String title;
    private String badge;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
