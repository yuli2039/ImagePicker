package com.yu.imgpicker.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.yu.imgpicker.ImgPicker;
import com.yu.imgpicker.core.OnSelectedListSizeChangeListener;
import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.base.RecyclerAdapter;
import com.yu.imgpicker.adapter.base.ViewHolder;
import com.yu.imgpicker.core.ImageLoader;
import com.yu.imgpicker.entity.ImageItem;

import java.util.List;
import java.util.Set;

/**
 *
 */
public class ImgGridAdapter extends RecyclerAdapter<ImageItem> {

    private static final int ITEM_TYPE_IMAGE = 0;
    private static final int ITEM_TYPE_CAMERA = 1;

    private boolean showCamera;
    private boolean multiSelect;
    private ImageLoader imageLoader;
    private Context context;
    private Set<ImageItem> selectedImages;
    private OnSelectedListSizeChangeListener listener;

    public ImgGridAdapter(Context context, List<ImageItem> data) {
        super(data, R.layout.adapter_image_list_item, R.layout.adapter_camera_item);
        this.context = context;
        ImgPicker imgPicker = ImgPicker.getInstance();

        this.selectedImages = imgPicker.getSelectedImages();
        this.showCamera = imgPicker.getConfig().showCamera;
        this.multiSelect = imgPicker.getConfig().multiSelect;
        this.imageLoader = imgPicker.getConfig().loader;
    }

    @Override
    public int getItemCount() {
        if (showCamera)
            return super.getItemCount() + 1;
        return super.getItemCount();
    }

    @Override
    public ImageItem getItem(int position) {
        if (showCamera)
            return position == 0 ? null : super.getItem(position - 1);
        return super.getItem(position);
    }

    /**
     * 返回的 ViewType用作构造方法中layoutid数组的角标`
     */
    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0)
            return ITEM_TYPE_CAMERA;
        return ITEM_TYPE_IMAGE;
    }

    @Override
    protected void onBindData(final ViewHolder holder, final int position, final ImageItem item) {
        if (getItemViewType(position) == ITEM_TYPE_IMAGE) {
            boolean selected = selectedImages.contains(item);
            holder.setChecked(R.id.cbCheck, selected)
                    .setVisibility(R.id.mask, selected ? View.VISIBLE : View.GONE)
                    .setVisibility(R.id.cbCheck, multiSelect ? View.VISIBLE : View.GONE);

            final CheckBox cbCheck = holder.findViewById(R.id.cbCheck);
            cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbCheck.isChecked()) {
                        holder.setVisibility(R.id.mask, View.VISIBLE);
                        selectedImages.add(item);
                    } else {
                        holder.setVisibility(R.id.mask, View.GONE);
                        selectedImages.remove(item);
                    }
                    // 回调选中的图片数量改变
                    if (multiSelect && listener != null) {
                        listener.onChange();
                    }
                }
            });

            imageLoader.displayImage(context, item.path, holder.findViewAsImageView(R.id.ivThumb));
        }
    }

    public void setOnSelectedListSizeChangeListener(OnSelectedListSizeChangeListener listener) {
        this.listener = listener;
    }

}