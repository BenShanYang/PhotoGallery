package com.benshanyang.photo_gallery;

import android.view.View;

/**
* @ClassName: OnItemLongClickListener
* @Description: 图片的长点击事件
* @Author: YangKuan
* @Date: 2020/12/18 14:06
*/
public interface OnItemLongClickListener<T> {
    void onLongClick(View view, T bean, int position);
}
