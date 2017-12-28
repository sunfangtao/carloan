package com.aioute.carloan.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.TakeTaskPhotoAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.TakePhotoUtil;
import com.jph.takephoto.app.TakePhoto;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.FileUtils;
import cn.sft.util.PermissionRequestUtil;
import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/28.
 * 现场拍照
 */

@EActivity(R.layout.activity_taketaskphoto)
public class TakeTaskPhotoActivity extends CustomBaseActivity implements TakePhoto.TakeResultListener {

    // 现场无车
    @ViewById(R.id.taketaskphoto_ck)
    AppCompatCheckBox noCarCK;
    // 拍照图片列表
    @ViewById(R.id.taketaskphoto_rv)
    RecyclerView photoRV;

    // 拍照工具
    protected TakePhotoUtil takePhotoUtil;

    private final int MAX_COUNT = 3;
    //----------------------------------------------------------
    // 拍照列表
    private List<String> pohotList;
    // 拍照保存的路径
    private String photoFilePath;

    @Override
    protected void noSaveInstanceStateForCreate() {
        takePhotoUtil = new TakePhotoUtil(this, this, null);
        photoFilePath = Util.getPicPath(this, Contant.FilePath.PHOTO_PATH);
    }

    @Override
    protected void afterViews() {
        TakeTaskPhotoAdapter adapter = new TakeTaskPhotoAdapter(this, pohotList = new ArrayList<>(), MAX_COUNT);
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(5);
        photoRV.addItemDecoration(decoration);
        photoRV.setLayoutManager(new GridLayoutManager(this, MAX_COUNT));
        photoRV.setAdapter(adapter);
    }

    @Click(R.id.taketaskphoto_btn)
    void upload() {
        RequestParams params = new RequestParams();
        params.put("", noCarCK.isChecked() ? "true" : "false");


    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_PHOTO, false)) {
            // 拍照
            if (PermissionRequestUtil.getInstance().requestPermission(this, PermissionRequestUtil.CAMERA)) {
                byCamera();
            }
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_SHOW, false)) {
            // 大图显示
        } else if (intent.getBooleanExtra(Contant.BroadcastKey.TAKETASKPHOTO_ITEM_CANCEL, false)) {
            // 删除拍照的图片
            pohotList.remove(intent.getIntExtra(Contant.BroadcastKey.POSITION, 0));
            notifyDataSetChanged();

            FileUtils.deleteFile(intent.getStringExtra(Contant.BroadcastKey.BEAN));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionRequestUtil.CAMERA) {
            // 获取摄像头权限结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                byCamera();
            } else {
                // 拒绝权限
                toast.setText("没有获取到相机权限");
            }
        }
    }

    private void byCamera() {
        takePhotoUtil.setFilePath(photoFilePath, System.currentTimeMillis() + ".png");
        takePhotoUtil.startTakePhoto(1, 1);
    }

    void notifyDataSetChanged() {
        photoRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void takeSuccess(String imagePath) {
        Util.print("imagePath=" + imagePath);
        pohotList.add(imagePath);
        notifyDataSetChanged();
    }

    @Override
    public void takeFail(String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhotoUtil.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
