package com.aioute.carloan.fragment;

import com.aioute.carloan.bean.TaskBean;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Administrator on 2017/12/27.
 * 完成任务
 */

@EFragment
public class TaskFinishedFragment extends TaskInProressFragment {

    @Override
    void getTask() {
        TaskBean taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
        taskBean = new TaskBean();
        taskList.add(taskBean);
    }
}
