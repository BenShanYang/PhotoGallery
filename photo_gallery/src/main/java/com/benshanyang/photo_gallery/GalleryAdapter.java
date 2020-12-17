package com.benshanyang.photo_gallery;

import androidx.viewpager.widget.PagerAdapter;

/**
 * @ClassName: GalleryAdapter
 * @Description: 图片浏览器适配器
 * @Author: YangKuan
 * @Date: 2020/12/16 17:55
 */
public abstract class GalleryAdapter extends PagerAdapter {

    public GalleryAdapter() {
        init();
    }

    public abstract void init();

}
