package com.yu.imgpicker.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import com.yu.imgpicker.R;
import com.yu.imgpicker.adapter.ImageFolderAdapter;
import com.yu.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.yu.imgpicker.entity.ImageFolder;
import com.yu.imgpicker.utils.ScreenUtils;


/**
 * 选择图片文件夹的弹层
 * Created by lyu on 2017/4/17.
 */
public class FolderPopUpWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    public FolderPopUpWindow(final Context context, final ImageFolderAdapter adapter) {
        super(context);

        final View view = View.inflate(context, R.layout.pop_folder, null);
        view.findViewById(R.id.margin).setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.folderRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
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
//         setAnimationStyle(R.style.popwin_anim_style);//  TODO: 2017/4/14  动画

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
