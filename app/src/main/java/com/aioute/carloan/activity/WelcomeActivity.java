package com.aioute.carloan.activity;

import android.text.Html;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.util.Util;
import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/19.
 */

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends CustomBaseActivity {

    @ViewById(R.id.image)
    ImageView imageView;

    @ViewById(R.id.checkBox)
    AlwaysMarqueeTextView textView;

    @Override
    protected void afterViews() {
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round));

        textView.setSingleLine(false);
        textView.setLineSpacing(Util.dp2px(this, 2), 1.2f);
        textView.setText(Html.fromHtml("<font size=\"6\" color=\"red\">今天天气好吗今天天气好吗？</font><br><font size=\"3\" color=\"green\">挺好的</font>"));

//    public void setText(CharSequence text, TextView.BufferType type) {
//        setText(text, type, true, 0);
//
//        if (mCharWrapper != null) {
//            mCharWrapper.mChars = null;
//        }
//    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
