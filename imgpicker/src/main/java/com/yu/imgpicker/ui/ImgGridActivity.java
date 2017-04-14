package com.yu.imgpicker.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yu.imgpicker.core.OnSelectedListSizeChangeListener;
import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.ImgFolderAdapter;
import com.yu.imgpicker.adapter.ImgGridAdapter;
import com.yu.imgpicker.adapter.base.RecyclerAdapter;
import com.yu.imgpicker.core.ImageDataSource;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.ui.widget.FolderPopUpWindow;

import java.util.List;

/**
 * Created by yu on 2017/4/13.
 */
public class ImgGridActivity extends ImageBaseActivity implements View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private RelativeLayout mFolderRoot;
    private Button mBtnOk;
    private Button mBtnDir;
    private Button mBtnPre;
    private ImgGridAdapter mGridAdapter;
    private ImgFolderAdapter mFolderAdapter;
    private FolderPopUpWindow mFolderPopUpWindow;

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
        mFolderRoot = (RelativeLayout) findViewById(R.id.rlFolderRoot);
        mBtnOk = (Button) findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(this);
        mBtnDir = (Button) findViewById(R.id.btnDir);
        mBtnDir.setOnClickListener(this);
        mBtnPre = (Button) findViewById(R.id.btnPreview);
        mBtnPre.setOnClickListener(this);
        setSelectNumber();

        if (mConfig.multiSelect) {
            mBtnOk.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        // 设置recyclerview
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mGridAdapter = new ImgGridAdapter(this, null);
        mGridAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                // TODO: 2017/4/14 跳转预览页面选择
            }
        });
        mGridAdapter.setOnSelectedListSizeChangeListener(new OnSelectedListSizeChangeListener() {
            @Override
            public void onChange() {
                setSelectNumber();
            }
        });
        mRecyclerView.setAdapter(mGridAdapter);

        // 设置文件夹选择弹窗
        mFolderAdapter = new ImgFolderAdapter(this, null);
        mFolderPopUpWindow = new FolderPopUpWindow(this, mFolderAdapter);
        mFolderPopUpWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFolder folder) {
                mGridAdapter.refreshWithNewData(folder.images);
                mBtnDir.setText(folder.name);
            }
        });
    }

    private void setSelectNumber() {
        if (mImgPicker.getSelectedImages().size() == 0) {
            mBtnOk.setText("完成");
            mBtnOk.setClickable(false);
            mBtnPre.setClickable(false);
        } else {
            mBtnOk.setText(String.format("完成(%d/%d)", mImgPicker.getSelectedImages().size(), mConfig.maxNum));
            mBtnOk.setClickable(true);
            mBtnPre.setClickable(true);
        }
        mBtnPre.setText(String.format("预览(%d)", mImgPicker.getSelectedImages().size()));
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
                mImgPicker.setImageFolders(imageFolders);
                mFolderAdapter.refreshWithNewData(imageFolders);
                mGridAdapter.refreshWithNewData(imageFolders.get(0).images);
            }
        }).loadImages();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBack) {
            finish();
        } else if (i == R.id.btnDir) {
            mFolderPopUpWindow.showAtLocation(mFolderRoot, Gravity.BOTTOM, 0, 0);
        } else if (i == R.id.btnPreview) {
            // TODO: 2017/4/14 预览
        }
    }
}
