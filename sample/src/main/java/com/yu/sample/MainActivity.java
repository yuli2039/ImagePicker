package com.yu.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yu.imgpicker.ImgPicker;
import com.yu.imgpicker.core.ImageLoader;
import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.core.OnSelectListener;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.utils.LogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        ImgSelConfig config = new ImgSelConfig.Builder(this)
                .imageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context).load(path).centerCrop().into(imageView);
                    }
                })
                .limited(3)
                .needCrop(true)
                .callback(new OnSelectListener() {
                    @Override
                    public void onSelect(List<ImageItem> data) {
                        handleImages(data);
                    }

                    @Override
                    public void onSelectFail(String msg) {

                    }

                    @Override
                    public void onSelectImageCancel() {

                    }
                })
                .build();

        ImgPicker.getInstance().setConfig(config);
    }

    private void handleImages(List<ImageItem> data) {

        LogUtils.e("data.size = " + data.size());
        LogUtils.e("first image path = " + data.get(0).path);

        Glide.with(this).load(data.get(0).path).centerCrop().into(iv);
    }

    public void btnClick(View v) {
        ImgPicker.getInstance().launch(this);
    }
}
