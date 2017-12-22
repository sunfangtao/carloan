package com.aioute.carloan.activity;

import android.view.Menu;
import android.widget.ImageView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.taghandler.FontSizeTagHandler;
import cn.sft.util.MyHandler;
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

        String text = "<customfont size='13sp' color='#00a65a'>今天天气好吗？</customfont><br><customfont size='10sp' color='#ff005a'>挺好的</customfont>";

        textView.setHtmlText(text, new FontSizeTagHandler(this));

        new MyHandler(1000) {
            @Override
            public void run() {
                MainActivity_.intent(WelcomeActivity.this).start();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
