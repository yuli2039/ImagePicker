package com.yu.imgpicker.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.yu.imgpicker.R;
import com.yu.imgpicker.core.ImageDataSource;
import com.yu.imgpicker.entity.ImageFolder;

import java.util.List;

/**
 * Created by yu on 2017/4/13.
 */

public class ImgGridActivity extends ImageBaseActivity implements View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private Button mBtnOk;
    private Button mBtnDir;
    private Button mBtnPre;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        initView();

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadImages();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }

    }

    private void initView() {
        findViewById(R.id.btnBack).setOnClickListener(this);
        mBtnOk = (Button) findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(this);
        mBtnDir = (Button) findViewById(R.id.btnDir);
        mBtnDir.setOnClickListener(this);
        mBtnPre = (Button) findViewById(R.id.btnPreview);
        mBtnPre.setOnClickListener(this);

        if (mConfig.multiSelect) {
            mBtnOk.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.setAdapter();// TODO: 2017/4/13  

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            // 点击相机item，申请权限，完成后回调这里
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // TODO: 2017/4/13  调用相机

            } else {
                showToast("权限被禁止，无法打开相机");
            }
        }
    }

    private void loadImages() {
        new ImageDataSource(this, null, new ImageDataSource.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageFolder> imageFolders) {
                // TODO: 2017/4/13
                mImgPicker.setImageFolders(imageFolders);

            }
        }).loadImages();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBack) {
            finish();
        } else if (i == R.id.btnDir) {

        } else if (i == R.id.btnPreview) {

        }
    }
}
