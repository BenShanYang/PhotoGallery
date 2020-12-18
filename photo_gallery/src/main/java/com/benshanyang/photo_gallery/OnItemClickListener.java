package com.benshanyang.photo_gallery;

import android.view.View;

/**
 * @ClassName: OnItemClickListener
 * @Description: 图片的点击事件
 * @Author: YangKuan
 * @Date: 2020/12/18 14:07
 */
public interface OnItemClickListener<T> {
    void onClick(View view, T bean, int position);
}
