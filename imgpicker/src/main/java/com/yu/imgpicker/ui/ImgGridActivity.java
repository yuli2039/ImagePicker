package com.yu.imgpicker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.ImgFolderAdapter;
import com.yu.imgpicker.adapter.ImgGridAdapter;
import com.yu.imgpicker.adapter.base.RecyclerAdapter;
import com.yu.imgpicker.compress.CompressHelper;
import com.yu.imgpicker.core.ImageDataSource;
import com.yu.imgpicker.core.OnSelectedListSizeChangeListener;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.ui.widget.FolderPopUpWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.yu.imgpicker.utils.Utils.crop;
import static com.yu.imgpicker.utils.Utils.takePhoto;


/**
 * Created by yu on 2017/4/13.
 */
public class ImgGridActivity extends ImageBaseActivity implements View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    public static final int REQUEST_PREVIEW_CODE = 0x03;
    public static final int REQUEST_CAPTURE_CODE = 0x04;
    public static final int REQUEST_CROP_CODE = 0x05;

    private RelativeLayout mFolderRoot;
    private Button mBtnOk;
    private Button mBtnDir;
    private Button mBtnPre;
    private ImgGridAdapter mGridAdapter;
    private ImgFolderAdapter mFolderAdapter;
    private FolderPopUpWindow mFolderPopUpWindow;
    private File mPhotoFile;
    private File mPhotoCropFile;

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            setSelectNumber();
        }
    };
    private CompressHelper mCompressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        initView();

        if (mImgPicker.getImageFolders() != null) {
            loadCompleted(mImgPicker.getImageFolders());
        } else {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                loadImages();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }

        if (mConfig.compress) {
            mCompressHelper = new CompressHelper.Builder(ImgGridActivity.this)
                    .setMaxWidth(mConfig.maxWidth)
                    .setMaxHeight(mConfig.maxHeight)
                    .setQuality(mConfig.quality)
                    .setDestinationDirectoryPath(Environment.getExternalStorageDirectory() + "/compress/")
                    .build();
        }

    }

    private void initView() {
        mFolderRoot = (RelativeLayout) findViewById(R.id.rlFolderRoot);

        View topBar = findViewById(R.id.topBar);
        if (mConfig.titleBarColor != -1)
            topBar.setBackgroundColor(mConfig.titleBarColor);

        ImageView btnBack = (ImageView) topBar.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        if (mConfig.backResId != -1)
            btnBack.setImageResource(mConfig.backResId);

        TextView tvTitle = (TextView) topBar.findViewById(R.id.tvTitle);
        if (mConfig.titleTextColor != -1)
            tvTitle.setTextColor(mConfig.titleTextColor);
        if (mConfig.titleText != null)
            tvTitle.setText(mConfig.titleText);

        mBtnOk = (Button) topBar.findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(this);
        if (mConfig.btnResId != -1)
            mBtnOk.setBackgroundResource(mConfig.btnResId);
        if (mConfig.btnTextColor != -1)
            mBtnOk.setTextColor(mConfig.btnTextColor);

        mBtnDir = (Button) findViewById(R.id.btnDir);
        mBtnDir.setOnClickListener(this);
        mBtnPre = (Button) findViewById(R.id.btnPreview);
        mBtnPre.setOnClickListener(this);

        setSelectNumber();

        if (mConfig.limited > 1) {
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
                if (mConfig.showCamera && position == 0) {
                    if (checkPermission(Manifest.permission.CAMERA)) {
                        mPhotoFile = takePhoto(ImgGridActivity.this, REQUEST_CAPTURE_CODE);
                    } else {
                        ActivityCompat.requestPermissions(ImgGridActivity.this,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                    }
                    return;
                }

                if (mConfig.limited > 1) {
                    ImagePreviewActivity.start(ImgGridActivity.this, mGridAdapter.getDataSet(),
                            mConfig.showCamera ? position - 1 : position);
                } else {
                    ImageItem imageItem = mGridAdapter.getItem(position);
                    if (mConfig.needCrop) {
                        mPhotoCropFile = crop(ImgGridActivity.this, REQUEST_CROP_CODE, new File(imageItem.path), mConfig);
                    } else {
                        // 单选压缩
                        if (mConfig.compress) {
                            imageItem.compressPath = mCompressHelper.compressToFile(new File(imageItem.path)).getAbsolutePath();
                        }
                        if (mConfig.listener != null) {
                            ArrayList<ImageItem> data = new ArrayList<>();
                            data.add(imageItem);
                            mConfig.listener.onSelect(data);
                            finish();
                        }
                    }
                }
            }
        });
        mGridAdapter.setOnSelectedListSizeChangeListener(new OnSelectedListSizeChangeListener() {
            @Override
            public void onChange() {
                setSelectNumber();
            }
        });
        mGridAdapter.registerAdapterDataObserver(mObserver);
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

    @Override
    protected void onDestroy() {
        mGridAdapter.unregisterAdapterDataObserver(mObserver);
        super.onDestroy();
    }

    /**
     * 刷新选中数量
     */
    private void setSelectNumber() {
        if (mImgPicker.getSelectedImages().size() == 0) {
            mBtnOk.setText(getString(R.string.top_bar_ok));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
        } else {
            mBtnOk.setText(String.format("完成(%d/%d)", mImgPicker.getSelectedImages().size(), mConfig.limited));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
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
                showToast(getString(R.string.read_permission_denied));
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPhotoFile = takePhoto(this, REQUEST_CAPTURE_CODE);
            } else {
                showToast(getString(R.string.camera_permission_denied));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PREVIEW_CODE:
                // 预览页面返回,肯定是多选
                if (resultCode == RESULT_CANCELED) {
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    // 预览页面点击完成按钮
                    callListenerMultiSelected();
                    finish();
                }
                break;
            case REQUEST_CAPTURE_CODE:
                if (resultCode == RESULT_OK) {
                    if (mConfig.limited == 1 && mConfig.needCrop) {// 只有单选裁剪才生效
                        mPhotoCropFile = crop(this, REQUEST_CROP_CODE, mPhotoFile, mConfig);
                    } else {
                        // 拍照之后压缩
                        String compressPath = "";
                        if (mConfig.compress) {
                            compressPath = mCompressHelper.compressToFile(mPhotoFile).getAbsolutePath();
                        }
                        callListenerWith(mPhotoFile, compressPath);
                        finish();
                    }
                } else {
                    callListenerWithFail(getString(R.string.camera_error));
                    finish();
                }
                break;
            case REQUEST_CROP_CODE:
                if (resultCode == RESULT_OK) {
                    callListenerWith(mPhotoCropFile, "");
                } else {
                    callListenerWithFail(getString(R.string.crop_error));
                }
                finish();
                break;
        }
    }

    /**
     * 多选回调listener
     */
    private void callListenerMultiSelected() {
        if (mConfig.listener != null) {
            if (mConfig.compress) {// 多选压缩
                for (ImageItem item : mImgPicker.getSelectedImages()) {
                    item.compressPath = mCompressHelper.compressToFile(new File(item.path)).getAbsolutePath();
                }
            }
            mConfig.listener.onSelect(new ArrayList<>(mImgPicker.getSelectedImages()));
        }
    }

    /**
     * 拍照或者裁剪错误回调
     */
    private void callListenerWithFail(String msg) {
        if (mConfig.listener != null) {
            mConfig.listener.onSelectFail(msg);
        }
    }

    /**
     * 裁剪或者单选的压缩后，返回结果，需要手动创建ImageItem
     */
    private void callListenerWith(File file, String compressPath) {
        if (mConfig.listener != null) {
            ImageItem imageItem = new ImageItem();
            imageItem.name = file.getName();
            imageItem.path = file.getAbsolutePath();
            imageItem.size = file.length();
            imageItem.compressPath = compressPath;
            imageItem.mimeType = "image/jpeg";
            imageItem.addTime = file.lastModified();

            ArrayList<ImageItem> sdata = new ArrayList<>();
            sdata.add(imageItem);
            mConfig.listener.onSelect(sdata);
        }
    }

    private void loadImages() {
        new ImageDataSource(this, null, new ImageDataSource.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageFolder> imageFolders) {
                loadCompleted(imageFolders);
            }
        }).loadImages();
    }

    private void loadCompleted(List<ImageFolder> imageFolders) {
        mImgPicker.setImageFolders(imageFolders);
        mFolderAdapter.refreshWithNewData(imageFolders);
        mGridAdapter.refreshWithNewData(imageFolders.get(0).images);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBack) {
            finish();
        } else if (i == R.id.btnDir) {
            mFolderPopUpWindow.showAtLocation(mFolderRoot, Gravity.BOTTOM, 0, 0);
        } else if (i == R.id.btnPreview) {
            ImagePreviewActivity.start(ImgGridActivity.this, new ArrayList<>(mImgPicker.getSelectedImages()), 0);
        } else if (i == R.id.btnOk) {
            callListenerMultiSelected();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mConfig.listener != null) {
            mConfig.listener.onSelectImageCancel();
        }
    }
}
