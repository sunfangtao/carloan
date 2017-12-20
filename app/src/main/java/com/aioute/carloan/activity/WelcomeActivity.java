package com.aioute.carloan.activity;

import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2017/12/19.
 */

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends CustomBaseActivity {

    @ViewById(R.id.image)
    ImageView imageView;

    @Override
    protected void afterViews() {
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
