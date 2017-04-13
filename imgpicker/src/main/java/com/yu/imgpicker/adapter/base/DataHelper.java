package com.yu.imgpicker.adapter.base;

import java.util.List;

/**
 * @author yu.
 */
public interface DataHelper<D> {

    D getItem(int position);
    boolean contains(D d);

    void addItem(D item);
    void addItems(List<D> items);

    void addItemToHead(D item);
    void addItemsToHead(List<D> items);

    void remove(int position);
    void remove(D item);

    void modify(D oldData, D newData);
    void modify(int index, D newData);

    void clear();
    void refreshWithNewData(List<D> items);

}
