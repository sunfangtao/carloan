package com.aioute.carloan.bean;

import cn.sft.tree.Node;

/**
 * Created by Administrator on 2018/1/8.
 */

public class GroupBean extends Node<String> {

    private String groupId;
    private String groupParentId;

    @Override
    public String getId() {
        return groupId;
    }

    @Override
    public void setId(String s) {
        this.groupId = s;
    }

    @Override
    public String getpId() {
        return groupParentId;
    }

    @Override
    public void setpId(String s) {
        this.groupParentId = s;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupParentId() {
        return groupParentId;
    }

    public void setGroupParentId(String groupParentId) {
        this.groupParentId = groupParentId;
    }
}
