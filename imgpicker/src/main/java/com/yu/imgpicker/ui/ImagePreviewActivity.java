package com.yu.imgpicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;

import com.yu.imgpicker.R;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.entity.PreviewData;

import java.util.List;

/**
 * Created by yu on 2017/4/17.
 */
public class ImagePreviewActivity extends ImagePreviewBaseActivity {

    private CheckBox cbCheck;

    public static void start(Activity context, List<ImageItem> data, int currentPosition) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(KEY_PREVIEW_DATA, new PreviewData(PreviewData.TYPE_PREVIEW, currentPosition, data));
        context.startActivityForResult(intent, ImgGridActivity.REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnOk.setVisibility(View.VISIBLE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        if (mSelectedImages.size() > 0) {
            btnOk.setText("完成(" + mSelectedImages.size() + "/" + mConfig.limited + ")");
            btnOk.setEnabled(true);
        } else {
            btnOk.setText("完成");
            btnOk.setEnabled(false);
        }

        cbCheck = (CheckBox) findViewById(R.id.cb_check);
        cbCheck.setChecked(mSelectedImages.contains(mImageItems.get(mCurrentPosition)));
        cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImages.size() == mConfig.limited && cbCheck.isChecked()) {
                    showToast(String.format("最多能选%d张", mConfig.limited));
                    cbCheck.setChecked(false);
                    return;
                }

                cbCheck.setChecked(cbCheck.isChecked());
                ImageItem item = mImageItems.get(mCurrentPosition);
                if (cbCheck.isChecked()) {
                    mSelectedImages.add(item);
                } else {
                    mSelectedImages.remove(item);
                }

                if (mSelectedImages.size() > 0) {
                    btnOk.setText("完成(" + mSelectedImages.size() + "/" + mConfig.limited + ")");
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setText("完成");
                    btnOk.setEnabled(false);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTitleCount.setText(position + 1 + "/" + mImageItems.size());
                cbCheck.setChecked(mSelectedImages.contains(mImageItems.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onImageSingleTap() {

    }
}