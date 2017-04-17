package com.yu.imgpicker.core;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;

import com.yu.imgpicker.R;
import com.yu.imgpicker.utils.FileUtils;

import java.io.Serializable;

/**
 *
 */
public class ImgSelConfig {

    public boolean needCrop;                    // 是否需要裁剪
    public boolean rememberSelected = true;     // 是否记住上次的选中记录(只对多选有效)
    public int limited = 9;                      // 最多选择图片数
    public boolean showCamera;                  // 第一个item是否显示相机
    public int statusBarColor = -1;             // 状态栏颜色

    public int backResId = -1;                  // 返回按钮图标

    public String title;                        // 标题
    public int titleColor;                      // 标题颜色
    public int titleBgColor;                    // titlebar背景色

    public String btnText;                      // 确定按钮文字
    public int btnTextColor;                    // 确定按钮文字颜色
    public int btnBgColor;                      // 确定按钮背景色

    public String allImagesText;                // 全部图片按钮文字

    public String filePath;                     // 拍照存储路径
    public ImageLoader loader;                  // 自定义图片加载器
    public OnSelectListener listener;           // 选择完成的回调;

    /*
     * 裁剪输出大小
     */
    public int aspectX = 1;
    public int aspectY = 1;
    public int outputX = 400;
    public int outputY = 400;

    public ImgSelConfig(Builder builder) {
        this.needCrop = builder.needCrop;
        this.rememberSelected = builder.rememberSelected;
        this.limited = builder.limited;
        this.showCamera = builder.showCamera;
        this.statusBarColor = builder.statusBarColor;
        this.backResId = builder.backResId;
        this.title = builder.title;
        this.titleBgColor = builder.titleBgColor;
        this.titleColor = builder.titleColor;
        this.btnText = builder.btnText;
        this.btnBgColor = builder.btnBgColor;
        this.btnTextColor = builder.btnTextColor;
        this.allImagesText = builder.allImagesText;
        this.filePath = builder.filePath;
        this.loader = builder.loader;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
        this.listener = builder.listener;
    }

    public static class Builder implements Serializable {

        private boolean needCrop = false;
        private boolean rememberSelected = true;
        private int limited = 9;
        private boolean showCamera = true;
        private int statusBarColor = -1;
        private int backResId = -1;
        private String title;
        private int titleColor;
        private int titleBgColor;
        private String btnText;
        private int btnTextColor;
        private int btnBgColor;
        private String allImagesText;
        private String filePath;
        private ImageLoader loader;
        private OnSelectListener listener;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder(Context context) {

            if (FileUtils.isSdCardAvailable())
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera";
            else
                filePath = Environment.getRootDirectory().getAbsolutePath() + "/Camera";

            title = context.getResources().getString(R.string.image);
            titleBgColor = context.getResources().getColor(R.color.colorPrimary);
            titleColor = Color.WHITE;

            btnText = context.getResources().getString(R.string.confirm);
            btnBgColor = Color.TRANSPARENT;
            btnTextColor = Color.WHITE;

            allImagesText = context.getResources().getString(R.string.all_images);

            FileUtils.createDir(filePath);
        }

        public Builder imageLoader(ImageLoader loader) {
            this.loader = loader;
            return this;
        }
        public Builder callback(OnSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder needCrop(boolean needCrop) {
            this.needCrop = needCrop;
            return this;
        }

        public Builder rememberSelected(boolean rememberSelected) {
            this.rememberSelected = rememberSelected;
            return this;
        }

        public Builder limited(@IntRange(from = 1, to = 9) int maxNum) {
            this.limited = maxNum;
            return this;
        }

        public Builder showCamera(boolean needCamera) {
            this.showCamera = needCamera;
            return this;
        }

        public Builder statusBarColor(@ColorInt int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder backResId(@IdRes int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder titleBgColor(@ColorInt int titleBgColor) {
            this.titleBgColor = titleBgColor;
            return this;
        }

        public Builder btnText(String btnText) {
            this.btnText = btnText;
            return this;
        }

        public Builder btnTextColor(@ColorInt int btnTextColor) {
            this.btnTextColor = btnTextColor;
            return this;
        }

        public Builder btnBgColor(@ColorInt int btnBgColor) {
            this.btnBgColor = btnBgColor;
            return this;
        }

        public Builder allImagesText(String allImagesText) {
            this.allImagesText = allImagesText;
            return this;
        }

        private Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public ImgSelConfig build() {
            return new ImgSelConfig(this);
        }
    }
}
