package com.yu.imgpicker.entity;

import java.io.Serializable;

/**
 */
public class ImageItem implements Serializable {

    public String name = "";       //图片的名字
    public String path = "";       //图片的路径
    public long size;         //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType = "";   //图片的类型
    public long addTime;      //图片的创建时间

    public String compressPath = ""; // 压缩后的地址，可能没有

    @Override
    public int hashCode() {
        return path.hashCode() + (int) (addTime ^ (addTime >>> 32));
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime;
        } else {
            return super.equals(o);
        }
    }
}
