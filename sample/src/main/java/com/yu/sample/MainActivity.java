package com.yu.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yu.imgpicker.ImagePicker;
import com.yu.imgpicker.PickerConfig;
import com.yu.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.yu.imgpicker.adapter.baseadapter.ViewHolder;
import com.yu.imgpicker.core.ImageLoader;
import com.yu.imgpicker.core.SimpleSelectListener;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.ui.PreviewDelActivity;
import com.yu.imgpicker.utils.LogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();

        PickerConfig config = new PickerConfig.Builder()
                .imageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context).load(path).centerCrop().into(imageView);
                    }
                })
                .showCamera(true)// 第一个item是否显示相机,默认true
                .limited(6)// 最多能选的张数（单选填1）
//                .titleBarColor(Color.BLACK) //titlebar的颜色和文字等自定义选项
//                .titleText("haha")
//                .titleTextColor(Color.parseColor("#ff0000"))
//                .btnResId(R.drawable.selector_back_press)
//                .btnTextColor(Color.parseColor("#00ff00"))
//                .backResId(R.mipmap.ic_launcher)

//                .needCrop(true)// 是否裁剪（只有单选时才有效）,如果裁剪就不会执行压缩
//                .compress(true)// 是否压缩
//                .maxHeight(960)// 压缩最大高度，默认960
//                .maxWidth(720)// 压缩最大宽度，默认720
//                .quality(80)// 压缩质量，默认80
                .callback(new SimpleSelectListener() {
                    @Override
                    public void onSelect(List<ImageItem> data) {
                        handleImages(data);
                    }
                })
                .build();

        ImagePicker.getInstance().setConfig(config);
    }

    public void btnClick(View v) {
        ImagePicker.getInstance().open(this);
    }

    private void initRecycler() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RecyclerAdapter<ImageItem>(null, R.layout.item) {
            @Override
            protected void onBindData(ViewHolder holder, int position, ImageItem item) {
                Glide.with(MainActivity.this)
                        .load(item.path)
                        .centerCrop()
                        .into(holder.findViewAsImageView(R.id.iv));
            }
        };
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                PreviewDelActivity.start(MainActivity.this, adapter.getDataSet(), position);
            }
        });
    }

    private void handleImages(List<ImageItem> data) {
        LogUtils.e("data.size = " + data.size());
        adapter.refreshWithNewData(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PreviewDelActivity.REQUEST_PREVIEW_CODE) {
            if (resultCode == RESULT_OK) {
                List<ImageItem> imageItems = (List<ImageItem>) data.getSerializableExtra(PreviewDelActivity.KEY_PREVIEW_DEL_DATA);
                adapter.refreshWithNewData(imageItems);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePicker.getInstance().clear();
    }
}
