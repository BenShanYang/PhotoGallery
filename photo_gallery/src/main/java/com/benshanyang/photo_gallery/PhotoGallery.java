package com.benshanyang.photo_gallery;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.benshanyang.photo_gallery.photoview.PhotoView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PhotoGallery
 * @Description: 图片浏览器
 * @Author: YangKuan
 * @Date: 2020/12/16 15:24
 */
public class PhotoGallery<T> extends DialogFragment {

    public static final int NONE = 0;//不显示指示器
    public static final int TEXT = 1;//设置指示器是文字类型的
    public static final int CIRCLE = 2;//设置指示器是圆点类型的

    private ViewPager viewPager;//图片浏览控件
    private TextView tvNumberIndicator;//文字指示器
    private RelativeLayout indicatorLayout;//底部圆点指示器父布局
    private RadioGroup indicatorGroup;//底部圆点指示器

    private int count = 0;//图片的数量
    private int indicatorType = 1;//指示器默认文字类型的
    private int currentItemIndex = 0;//当前ViewPager初始显示的页的索引值
    private List<PhotoView> photoViewList = null;
    private PhotoGalleryable<T> photoGalleryAble;
    private OnItemClickListener<T> onItemClickListener;//图片的点击事件
    private OnItemLongClickListener<T> onItemLongClickListener;//图片的长点击事件

    public PhotoGallery(PhotoGalleryable<T> photoGalleryAble) {
        this.photoGalleryAble = photoGalleryAble;
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
        indicatorGroup = (RadioGroup) view.findViewById(R.id.indicator_group);
        indicatorLayout = (RelativeLayout) view.findViewById(R.id.indicator_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        resetIndicator();//设置指示器类型

        viewPager.setAdapter(new GalleryAdapter() {

            @Override
            public void init() {
                if (photoGalleryAble != null) {
                    photoViewList = new ArrayList<>();
                    final List<T> urls = photoGalleryAble.getUrls();
                    count = (urls != null ? urls.size() : 0);
                    tvNumberIndicator.setText(String.format("%s/%s", "1", count));//顶部文字指示器
                    for (int i = 0; i < count; i++) {
                        final int finalI = i;
                        final T bean = (urls != null) ? urls.get(i) : null;
                        PhotoView photoView = new PhotoView(getContext());
                        photoGalleryAble.bindData(bean, photoView);
                        photoViewList.add(photoView);
                        photoView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onItemClickListener != null) {
                                    onItemClickListener.onClick(v, bean, finalI);
                                }
                            }
                        });
                        photoView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if (onItemLongClickListener != null) {
                                    onItemLongClickListener.onLongClick(v, bean, finalI);
                                }
                                return false;
                            }
                        });

                        //底部圆点指示器
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(dp2px(8f), dp2px(8f));
                        params.leftMargin = dp2px(3f);
                        params.rightMargin = dp2px(3f);
                        RadioButton radioButton = new RadioButton(getContext());
                        radioButton.setButtonDrawable(new StateListDrawable());
                        radioButton.setBackgroundResource(R.drawable.indicator_drawable);
                        indicatorGroup.addView(radioButton, params);
                        radioButton.setChecked(i == 0);
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
                if (tvNumberIndicator != null && tvNumberIndicator.getVisibility() == View.VISIBLE) {
                    //顶部文字指示器
                    tvNumberIndicator.setText(String.format("%s/%s", position + 1, count));
                }
                if (indicatorLayout != null && indicatorGroup != null && indicatorLayout.getVisibility() == View.VISIBLE) {
                    //底部圆点指示器
                    try {
                        ((RadioButton) indicatorGroup.getChildAt(position)).setChecked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(currentItemIndex);
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

    /**
     * 设置初始显示第几张图片
     *
     * @param index 对应图片的索引值
     * @return 返回当前类的引用
     */
    public PhotoGallery<T> setCurrentItem(int index) {
        currentItemIndex = index;
        if (viewPager != null) {
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(currentItemIndex);
                }
            });
        }
        return PhotoGallery.this;
    }

    /**
     * 图片的点击事件
     *
     * @param onItemClickListener 点击事件的回调函数
     * @return 返回当前类的引用
     */
    public PhotoGallery<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return PhotoGallery.this;
    }

    /**
     * 图片的长点击事件
     *
     * @param onItemLongClickListener 长点击事件的回调函数
     * @return 返回当前类的引用
     */
    public PhotoGallery<T> setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        return PhotoGallery.this;
    }

    /**
     * 设置图片浏览器的指示器类型
     *
     * @param type 指示器类型 共有3种：PhotoGallery.NONE-不显示指示器、PhotoGallery.TEXT-指示器是文字类型的、PhotoGallery.CIRCLE-指示器是圆点类型的
     * @return 返回当前类的引用
     */
    public PhotoGallery<T> showIndicator(@Indicator int type) {
        indicatorType = type;
        resetIndicator();
        return PhotoGallery.this;
    }

    private void resetIndicator() {
        switch (indicatorType) {
            case NONE:
                //无指示器
                if (indicatorLayout != null && indicatorLayout.getVisibility() == View.VISIBLE) {
                    //圆点指示器
                    indicatorLayout.setVisibility(View.GONE);
                }
                if (tvNumberIndicator != null && tvNumberIndicator.getVisibility() == View.VISIBLE) {
                    //文字指示器
                    tvNumberIndicator.setVisibility(View.GONE);
                }
                break;
            case CIRCLE:
                //底部圆点类型的指示器
                if (indicatorLayout != null && indicatorLayout.getVisibility() == View.GONE) {
                    //圆点指示器
                    indicatorLayout.setVisibility(View.VISIBLE);
                }
                if (tvNumberIndicator != null && tvNumberIndicator.getVisibility() == View.VISIBLE) {
                    //文字指示器
                    tvNumberIndicator.setVisibility(View.GONE);
                }
                break;
            case TEXT:
            default:
                //文字类型的指示器
                if (indicatorLayout != null && indicatorLayout.getVisibility() == View.VISIBLE) {
                    //圆点指示器
                    indicatorLayout.setVisibility(View.GONE);
                }
                if (tvNumberIndicator != null && tvNumberIndicator.getVisibility() == View.GONE) {
                    //文字指示器
                    tvNumberIndicator.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue dp值
     * @return 返回px值
     */
    private int dp2px(float dpValue) {
        int pxValue = 0;
        Context context = getContext();
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            pxValue = (int) (dpValue * scale + 0.5f);
        }
        return pxValue;
    }

    // 注解仅存在于源码中，在class字节码文件中不包含
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, TEXT, CIRCLE})
    private @interface Indicator {
    }

}
