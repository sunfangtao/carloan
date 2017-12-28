package com.aioute.carloan.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by FlySand on 2017/12/28.
 */

public class PhotographBean extends DBVO {

    private String[] photos;

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }
}
