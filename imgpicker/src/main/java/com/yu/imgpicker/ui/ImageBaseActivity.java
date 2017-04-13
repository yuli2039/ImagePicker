package com.yu.imgpicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yu.imgpicker.ImgPicker;
import com.yu.imgpicker.core.ImgSelConfig;
import com.yu.imgpicker.utils.StatusBarCompat;

/**
 *
 */
public class ImageBaseActivity extends AppCompatActivity {

    protected ImgSelConfig mConfig;
    protected ImgPicker mImgPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImgPicker = ImgPicker.getInstance();
        mConfig = mImgPicker.getConfig();
        if (mConfig != null) {
            StatusBarCompat.compat(this, mConfig.statusBarColor);
        }

    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}
