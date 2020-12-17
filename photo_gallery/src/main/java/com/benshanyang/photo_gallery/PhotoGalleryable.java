package com.benshanyang.photo_gallery;

import android.widget.ImageView;

import java.util.List;

/**
 * @ClassName: PhotoGalleryable
 * @Description: 设置图片浏览器的数据监听回调函数
 * @Author: YangKuan
 * @Date: 2020/12/17 9:14
 */
public interface PhotoGalleryable<T> {
    public abstract List<T> getUrls();

    public abstract void bindData(T data, ImageView imageView);
}
