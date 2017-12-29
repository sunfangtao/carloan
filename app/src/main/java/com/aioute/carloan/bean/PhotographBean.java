package com.aioute.carloan.bean;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by FlySand on 2017/12/28.
 */

public class PhotographBean extends DBVO {

    private List<String> photos;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
