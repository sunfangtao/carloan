package com.aioute.carloan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.aioute.carloan.R;
import com.aioute.carloan.bean.model.MainFunctionModel;

import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.base.fragment.BaseFragment;
import cn.sft.view.BadgeTextView;

public class MainFunctionAdapter extends BaseAdapter {

    int viewHeight;

    public MainFunctionAdapter(Context context, BaseFragment fragment, List<MainFunctionModel> list, int viewHeight) {
        super(context, fragment, list);
        this.viewHeight = viewHeight;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.main_function_item;
    }

    @Override
    public void onBindViewHolder(int viewType, View view, final int position) {

        if (view.getLayoutParams().height != viewHeight)
            view.getLayoutParams().height = viewHeight;

        BadgeTextView textView = view.findViewById(R.id.main_function_item_badgetv);

        final MainFunctionModel mainFunction = (MainFunctionModel) getObjcet(position);

        if (!TextUtils.isEmpty(mainFunction.getBadge())) {
            textView.showBadge(true);
            textView.setBadge(mainFunction.getBadge());
        } else {
            textView.showBadge(false);
        }

        textView.setDrawable(context.getResources().getDrawable(mainFunction.getDrawableId()));
        textView.setText(mainFunction.getTitle());

    }
}