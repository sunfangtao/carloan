package com.aioute.carloan.fragment;

import com.aioute.carloan.bean.PhotographBean;

import org.androidannotations.annotations.EFragment;

/**
 * Created by FlySand on 2017/12/25.
 * 拍照已处理
 */
@EFragment
public class PhotographProcessedFragment extends PhotographUntreatedFragment {

    @Override
    void getTask() {
        for (int i = 0; i < 10; i++) {
            PhotographBean bean = new PhotographBean();
            photographBeanList.add(bean);
        }
    }

}
