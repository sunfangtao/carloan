package com.aioute.carloan.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2017/12/29.
 */

public class MapUtil {

    private static final String GaodePackageName = "com.autonavi.minimap";

    public static boolean isInstall(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void openGaoDeMap(Context context, double lat, double lng, int style) {
        try {
            String uri = "androidamap://route?sourceApplication=%1$s&dlat=%2$f&dlon=%3$f&dev=0&t=%4$d";
            if (isInstall(GaodePackageName)) {
                Intent intent = new Intent("android.intent.action.VIEW"
                        , android.net.Uri.parse(String.format(uri, GaodePackageName, lat, lng, 0)));
                intent.setPackage(GaodePackageName);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "请下载安装高德地图", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

}
