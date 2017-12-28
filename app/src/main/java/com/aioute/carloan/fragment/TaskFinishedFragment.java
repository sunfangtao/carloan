package com.aioute.carloan.fragment;

import com.aioute.carloan.bean.TaskBean;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Administrator on 2017/12/27.
 * 完成任务
 */

@EFragment
public class TaskFinishedFragment extends TaskInProressFragment {

    void getTask() {
        TaskBean taskBean = new TaskBean();
        taskBean.setNumber(11);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(22);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(33);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(44);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(55);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(66);
        taskList.add(taskBean);

        taskBean = new TaskBean();
        taskBean.setNumber(77);
        taskList.add(taskBean);
    }
}
