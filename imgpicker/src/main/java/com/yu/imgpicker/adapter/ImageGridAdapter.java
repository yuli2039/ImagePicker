package com.yu.imgpicker.adapter;

import android.view.View;

import com.yu.imgpicker.ImgPicker;
import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.base.RecyclerAdapter;
import com.yu.imgpicker.adapter.base.ViewHolder;
import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.entity.ImageItem;

import java.util.List;

/**
 *
 */
public class ImageGridAdapter extends RecyclerAdapter<ImageItem> {

    private static final int ITEM_TYPE_IMAGE = 0;
    private static final int ITEM_TYPE_CAMERA = 1;

    private boolean showCamera;// 第一个位置是否需要显示相机
    private boolean multiSelect;

    public ImageGridAdapter(List<ImageItem> data) {
        super(data, R.layout.adapter_image_list_item, R.layout.adapter_camera_item);
        ImgSelConfig config = ImgPicker.getInstance().getConfig();
        showCamera = config.showCamera;
        multiSelect = config.multiSelect;
    }

    @Override
    public int getItemCount() {
        if (showCamera)
            return super.getItemCount() + 1;
        return super.getItemCount();
    }

    @Override
    public int getLayoutIndex(int position, ImageItem item) {
        return showCamera && position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_IMAGE;
    }

    @Override
    protected void onBindData(ViewHolder holder, int position, ImageItem item) {
        if (getItemViewType(position) == ITEM_TYPE_IMAGE) {
            holder.setVisibility(R.id.cb_check, multiSelect ? View.VISIBLE : View.GONE);


        }
    }
}