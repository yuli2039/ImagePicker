package com.yu.imgpicker.core;

import com.yu.imgpicker.entity.ImageItem;

import java.util.List;

/**
 * Created by yu on 2017/4/17.
 */

public interface OnSelectListener {

    void onSelect(List<ImageItem> data);

    void onSelectFail(String msg);

    void onSelectImageCancel();
}
