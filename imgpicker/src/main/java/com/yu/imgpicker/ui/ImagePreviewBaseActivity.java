package com.yu.imgpicker.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.ImagePageAdapter;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.entity.PreviewData;
import com.yu.imgpicker.ui.widget.ViewPagerFixed;

import java.util.List;
import java.util.Set;

/**
 */
public abstract class ImagePreviewBaseActivity extends ImageBaseActivity {

    public static final String KEY_PREVIEW_DATA = "KEY_PREVIEW_DATA";

    protected List<ImageItem> mImageItems;      //跳转进ImagePreviewFragment的图片文件夹
    protected int mCurrentPosition = 0;              //跳转进ImagePreviewFragment时的序号，第几个图片
    protected TextView mTitleCount;                  //显示当前图片的位置  例如  5/31
    protected Set<ImageItem> mSelectedImages;   //所有已经选中的图片

    protected View topBar;
    protected ViewPagerFixed mViewPager;
    protected ImagePageAdapter mAdapter;
    protected Button btnOk;

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        PreviewData previewData = getIntent().getParcelableExtra(KEY_PREVIEW_DATA);
        if (previewData == null) {
            throw new IllegalArgumentException("----- the PreviewData is null -----");
        }
        mCurrentPosition = previewData.currentPosition;
        mImageItems = previewData.data;
        mSelectedImages = mImgPicker.getSelectedImages();

        topBar = findViewById(R.id.top_bar);
        btnOk = (Button) topBar.findViewById(R.id.btnOk);
        btnOk.setEnabled(mSelectedImages.size() > 0);
        btnOk.setVisibility(View.GONE);

        topBar.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTitleCount = (TextView) findViewById(R.id.tvDes);

        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        mAdapter = new ImagePageAdapter(this, mImageItems);
        mAdapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);

        //初始化当前页面的状态
        mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * 单击时，隐藏头和尾
     */
    public abstract void onImageSingleTap();
}