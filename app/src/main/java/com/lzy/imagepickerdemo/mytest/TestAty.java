package com.lzy.imagepickerdemo.mytest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.imagepickerdemo.R;
import com.lzy.imagepickerdemo.imageloader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu
 *         Create on 2016/11/6.
 */

public class TestAty extends AppCompatActivity {

    private GridView gv;
    private ImageItem imgAdd;
    private List<ImageItem> sdata;
    private GvAdapter adapter;
    private ImagePicker imagePicker;
    private PhotoPickerWindow pw;
    private View root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initImagePicker();

        root = findViewById(R.id.root);
        gv = (GridView) findViewById(R.id.gv);

        imgAdd = new ImageItem();
        imgAdd.isAdd = true;

        sdata = new ArrayList<>();
        sdata.add(imgAdd);

        adapter = new GvAdapter(this);
        gv.setAdapter(adapter);

    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
//        imagePicker.setMultiMode(false);
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(6);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(300);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(300);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void showDia() {
        pw = new PhotoPickerWindow(this);
        pw.setPickCallback(new PhotoPickerWindow.PickCallback() {
            @Override
            public void onCameraClick() {
                imagePicker.fromCamera(TestAty.this, 100);
            }

            @Override
            public void onGalleryClick() {
                imagePicker.setSelectLimit(6 - sdata.size());
                imagePicker.fromGallery(TestAty.this, 100);
            }
        });
        pw.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                sdata.addAll(images);
                if (sdata.contains(imgAdd)) {
                    sdata.remove(imgAdd);
                }
                if (sdata.size() < 6) {
                    sdata.add(imgAdd);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GvAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public GvAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return sdata.size();
        }

        @Override
        public Object getItem(int position) {
            return sdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_test, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ImageItem item = sdata.get(position);
            holder.ivDelete.setVisibility(item.isAdd ? View.GONE : View.VISIBLE);
            if (item.isAdd)
                Glide.with(TestAty.this).load(R.mipmap.image_add_nor).centerCrop().into(holder.iv);
            else
                Glide.with(TestAty.this).load(item.path).centerCrop().into(holder.iv);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sdata.remove(position);
                    if (sdata.size() < 6 && !sdata.contains(imgAdd))
                        sdata.add(imgAdd);
                    notifyDataSetChanged();
                }
            });
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isAdd) {
                        sdata.remove(imgAdd);
                        showDia();
                    } else {
                        // TODO: 2016/11/6 看大图
                    }
                }
            });

            return convertView;
        }
    }

    private static class ViewHolder {
        public ImageView ivDelete;
        public ImageView iv;
    }
}
