package com.benshanyang.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.benshanyang.photo_gallery.PhotoGallery;
import com.benshanyang.photo_gallery.PhotoGalleryable;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhotoGallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = new PhotoGallery<>(new PhotoGalleryable<String>() {
            @Override
            public List<String> getUrls() {
                return Arrays.asList("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=518565351,741314501&fm=26&gp=0.jpg", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=820556762,652942924&fm=26&gp=0.jpg", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=512426052,3777200390&fm=26&gp=0.jpg");
            }

            @Override
            public void bindData(String data, ImageView imageView) {
                Glide.with(MainActivity.this).load(data).into(imageView);
            }
        });
    }

    public void showDialog(View view) {
        gallery.show(getSupportFragmentManager(), "gallery");
    }
}