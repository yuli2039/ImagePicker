package com.yu.imgpicker.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.yu.imgpicker.core.ImgSelConfig;

import java.io.File;
import java.util.List;

/**
 * Created by yu on 2017/4/17.
 */
public class Utils {

    private static final String FILE_PROVIDER_AUTHORITIES = "com.yu.imgpicker.fileprovider";// 用于适配7.0的文件共享

    /**
     * 调用相机拍摄一张图片
     *
     * @param activity    接收的activity
     * @param requestCode 请求code
     * @return 返回原图存储的文件
     */
    public static File takePhoto(Activity activity, int requestCode) {
        File file = createFile(String.valueOf(System.currentTimeMillis()));
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITIES, file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        activity.startActivityForResult(intent, requestCode);

        return file;
    }

    /**
     * 裁剪一张图片
     *
     * @param activity    接收的activity
     * @param requestCode 请求code
     * @param sourceImage 原始图片
     * @param config      用于获取裁剪输出参数
     * @return 返回裁剪之后存储的图片文件
     */
    public static File crop(Activity activity, int requestCode, File sourceImage, ImgSelConfig config) {
        Uri imageUri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITIES, sourceImage);

        File outputFile = createFile("crop_" + System.currentTimeMillis());
        Uri outputUri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITIES, outputFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        //将存储图片的uri读写权限授权给剪裁工具应用，否则会出现无法存储裁剪图片的情况
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        activity.startActivityForResult(intent, requestCode);

        return outputFile;
    }

    private static File createFile(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + fileName + ".jpeg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return file;
    }
}
