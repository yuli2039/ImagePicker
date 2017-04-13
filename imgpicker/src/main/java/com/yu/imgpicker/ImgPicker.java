package com.yu.imgpicker;

import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.entity.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/4/13.
 */

public class ImgPicker {

    private ImgSelConfig mConfig;

    private List<ImageFolder> mImageFolders;      //所有的图片文件夹
    private ArrayList<ImageItem> mSelectedImages = new ArrayList<>();   //选中的图片集合


    public static ImgPicker getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static ImgPicker INSTANCE = new ImgPicker();
    }

    public ImgSelConfig getConfig() {
        return mConfig;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolder> mImageFolders) {
        this.mImageFolders = mImageFolders;
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }
}
