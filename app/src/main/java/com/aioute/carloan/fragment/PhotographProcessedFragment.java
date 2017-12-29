package com.aioute.carloan.fragment;

import com.aioute.carloan.bean.PhotographBean;

import org.androidannotations.annotations.EFragment;

import java.util.Arrays;

/**
 * Created by FlySand on 2017/12/25.
 * 拍照已处理
 */
@EFragment
public class PhotographProcessedFragment extends PhotographUntreatedFragment {

    void getTask() {
        String[] photos = new String[]{"http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png",
                "http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png",
                "http://mapopen-website-wiki.cdn.bcebos.com/homePage/images/hp-use2.png"};

        for (int i = 0; i < 10; i++) {
            PhotographBean bean = new PhotographBean();
            bean.setPhotos(Arrays.asList(photos));
            photographBeanList.add(bean);
        }
    }

}
