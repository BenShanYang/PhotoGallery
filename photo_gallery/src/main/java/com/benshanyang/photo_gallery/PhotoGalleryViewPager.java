package com.benshanyang.photo_gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @ClassName: PhotoGalleryViewPager
 * @Description: 自定义ViewPager捕捉多点触控导致的崩溃
 * @Author: YangKuan
 * @Date: 2020/12/17 11:48
 */
public class PhotoGalleryViewPager extends ViewPager {
    public PhotoGalleryViewPager(@NonNull Context context) {
        super(context);
    }

    public PhotoGalleryViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean b = false;
        try {
            b = super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

}
