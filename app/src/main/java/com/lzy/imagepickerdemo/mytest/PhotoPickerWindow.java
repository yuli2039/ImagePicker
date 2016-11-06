package com.lzy.imagepickerdemo.mytest;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lzy.imagepickerdemo.R;


/**
 * 选择头像弹窗
 *
 * @author yu
 *         Create on 16/8/19.
 */
public class PhotoPickerWindow extends PopupWindow implements View.OnClickListener {

    private PickCallback listener;

    public PhotoPickerWindow(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_photo_picker, null);

        view.findViewById(R.id.btnCamera).setOnClickListener(this);
        view.findViewById(R.id.btnGallery).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(400);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x000000));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                listener.onCameraClick();
                break;
            case R.id.btnGallery:
                listener.onGalleryClick();
                break;
        }
        this.dismiss();
    }

    public interface PickCallback {
        void onCameraClick();

        void onGalleryClick();
    }

    public void setPickCallback(PickCallback listener) {
        this.listener = listener;
    }
}
