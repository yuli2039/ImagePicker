package com.yu.imgpicker.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.ImgFolderAdapter;
import com.yu.imgpicker.adapter.base.RecyclerAdapter;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.utils.ScreenUtils;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：16/8/1
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FolderPopUpWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;
    private final View marginView;

    public FolderPopUpWindow(final Context context, final ImgFolderAdapter adapter) {
        super(context);

        final View view = View.inflate(context, R.layout.pop_folder, null);
        marginView = view.findViewById(R.id.margin);
        marginView.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int maxHeight = ScreenUtils.height((Activity) view.getContext()) * 5 / 8;
                int realHeight = recyclerView.getHeight();
                ViewGroup.LayoutParams listParams = recyclerView.getLayoutParams();
                listParams.height = realHeight > maxHeight ? maxHeight : realHeight;
                recyclerView.setLayoutParams(listParams);
            }
        });
//        setAnimationStyle(R.style.popwin_anim_style);// TODO: 2017/4/14  动画

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                adapter.setSelected(position);
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(adapter.getItem(position));
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ImageFolder folder);
    }
}
