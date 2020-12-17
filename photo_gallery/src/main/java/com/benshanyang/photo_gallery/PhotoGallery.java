package com.benshanyang.photo_gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.benshanyang.photo_gallery.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PhotoGallery
 * @Description: 图片浏览器
 * @Author: YangKuan
 * @Date: 2020/12/16 15:24
 */
public class PhotoGallery<T> extends DialogFragment implements View.OnClickListener {

    private TextView tvNumberIndicator;

    private int count = 0;
    private List<PhotoView> photoViewList = null;
    private PhotoGalleryable<T> photoGalleryable;

    public PhotoGallery(PhotoGalleryable<T> photoGalleryable) {
        this.photoGalleryable = photoGalleryable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GalleryDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
            dialog.setCanceledOnTouchOutside(false); //点击边际可消失
        }
        return inflater.inflate(R.layout.layout_photo_gallery, container);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNumberIndicator = (TextView) view.findViewById(R.id.tv_number_indicator);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new GalleryAdapter() {

            @Override
            public void init() {
                if (photoGalleryable != null) {
                    photoViewList = new ArrayList<>();
                    List<T> urls = photoGalleryable.getUrls();
                    count = (urls != null ? urls.size() : 0);
                    tvNumberIndicator.setText(String.format("%s/%s", "1", count));//顶部文字指示器
                    for (int i = 0; i < count; i++) {
                        PhotoView photoView = new PhotoView(getContext());
                        photoGalleryable.bindData(urls.get(i), photoView);
                        photoViewList.add(photoView);
                    }
                }
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = null;
                try {
                    view = photoViewList.get(position);
                    ViewGroup.LayoutParams params;
                    if (view.getLayoutParams() == null) {
                        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        view.setLayoutParams(params);
                    }
                    container.addView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                try {
                    View view = photoViewList.get(position);
                    container.removeView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvNumberIndicator.setText(String.format("%s/%s", position + 1, count));//顶部文字指示器
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = (dialog != null) ? dialog.getWindow() : null;
        if (window != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setLayout(width, height);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            dismiss();
        }
    }

}
