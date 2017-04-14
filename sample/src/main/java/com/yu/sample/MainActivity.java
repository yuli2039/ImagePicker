package com.yu.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yu.imgpicker.ImgPicker;
import com.yu.imgpicker.core.ImageLoader;
import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.ui.ImgGridActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImgSelConfig config = new ImgSelConfig.Builder(this, new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().into(imageView);
            }
        }).build();
        ImgPicker.getInstance().setConfig(config);
    }

    public void btnClick(View v) {
        startActivity(new Intent(this, ImgGridActivity.class));
    }
}
