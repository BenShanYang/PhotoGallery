package com.benshanyang.photogallery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.benshanyang.photo_gallery.OnItemClickListener;
import com.benshanyang.photo_gallery.OnItemLongClickListener;
import com.benshanyang.photo_gallery.PhotoGallery;
import com.benshanyang.photo_gallery.PhotoGalleryable;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhotoGallery<String> gallery;
    private List<String> list = Arrays.asList("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=518565351,741314501&fm=26&gp=0.jpg", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=820556762,652942924&fm=26&gp=0.jpg", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=512426052,3777200390&fm=26&gp=0.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = new PhotoGallery<>(new PhotoGalleryable<String>() {
            @Override
            public List<String> getUrls() {
                return list;
            }

            @Override
            public void bindData(String data, ImageView imageView) {
                Glide.with(MainActivity.this).load(data).into(imageView);
            }
        });
        //设置指示器的类型
        // 共有3种：PhotoGallery.NONE-不显示指示器、PhotoGallery.TEXT-指示器是文字类型的、PhotoGallery.CIRCLE-指示器是圆点类型的
        gallery.showIndicator(list.size() == 1 ? PhotoGallery.NONE : PhotoGallery.TEXT);
        gallery.setCurrentItem(1);//设置初始显示第几张图片
        gallery.setOnItemLongClickListener(new OnItemLongClickListener<String>() {
            @Override
            public void onLongClick(View view, String bean, int position) {
                //图片的长点击事件
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("您确认要保存该图片?")
                        .setPositiveButton("确定", null)
                        .setNeutralButton("取消", null)
                        .create()
                        .show();
            }
        });
        gallery.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View view, String bean, int position) {
                //图片的点击事件
                Toast.makeText(MainActivity.this, String.format("您点击了第%s张图片", position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog(View view) {
        //显示图片浏览器
        gallery.show(getSupportFragmentManager(), "gallery");
    }
}