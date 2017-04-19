package com.yu.imgpicker.core;

import android.os.AsyncTask;
import android.view.View;

import com.yu.imgpicker.compress.CompressHelper;
import com.yu.imgpicker.entity.ImageItem;

import java.io.File;
import java.util.List;

/**
 * 图片压缩任务类
 * Created by lyu on 2017/4/19.
 */
public class CompressTask extends AsyncTask<List<ImageItem>, Void, List<ImageItem>> {

    private CompressHelper compressHelper;
    private View progressView;
    private CompressListener listener;

    public CompressTask(CompressHelper helper, View progressView, CompressListener listener) {
        this.compressHelper = helper;
        this.progressView = progressView;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ImageItem> doInBackground(List<ImageItem>... params) {
        List<ImageItem> datas = params[0];
        for (ImageItem item : datas) {
            item.compressPath = compressHelper.compressToFile(new File(item.path)).getAbsolutePath();
        }
        return datas;
    }

    @Override
    protected void onPostExecute(List<ImageItem> imageItems) {
        progressView.setVisibility(View.GONE);
        if (listener != null) {
            listener.onCompressComplete(imageItems);
        }
    }

    public interface CompressListener {
        void onCompressComplete(List<ImageItem> imageItems);
    }
}
