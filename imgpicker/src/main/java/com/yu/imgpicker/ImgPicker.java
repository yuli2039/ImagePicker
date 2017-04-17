package com.yu.imgpicker;

import android.app.Activity;
import android.content.Intent;

import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.ui.ImgGridActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yu on 2017/4/13.
 */

public class ImgPicker {

    private ImgSelConfig mConfig;

    private List<ImageFolder> mImageFolders;      //所有的图片文件夹
    private Set<ImageItem> mSelectedImages = new HashSet<>();   //选中的图片集合

    public static ImgPicker getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static ImgPicker INSTANCE = new ImgPicker();
    }

    public ImgSelConfig getConfig() {
        return mConfig;
    }

    public void setConfig(ImgSelConfig mConfig) {
        this.mConfig = mConfig;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolder> mImageFolders) {
        this.mImageFolders = mImageFolders;
    }

    public Set<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    // ***********************************************************
    public void launch(Activity context) {
        context.startActivity(new Intent(context, ImgGridActivity.class));
    }
}
