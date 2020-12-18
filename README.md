# PhotoGallery 简易图片浏览器-查看大图
[![](https://jitpack.io/v/BenShanYang/PhotoGallery.svg)](https://jitpack.io/#BenShanYang/PhotoGallery)

## 使用步骤

#### Step 1.依赖PhotoGallery
Gradle
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
dependencies {
        implementation 'com.github.BenShanYang:PhotoGallery:1.0.0'
}
```
或者引用本地lib
```groovy
implementation project(':photo_gallery')
```

#### Step 2.添加权限到你的 AndroidManifest.xml
```xml
<!-- if you want to load images from the internet -->
<uses-permission android:name="android.permission.INTERNET" /> 
```

#### Step 3.在你的类中使用

- 注意！PhotoGallery<T>和PhotoGalleryable<T> 泛型T为图片数据集合中数据的类型

```java

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final List<String> list = Arrays.asList("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=518565351,741314501&fm=26&gp=0.jpg", 
    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=820556762,652942924&fm=26&gp=0.jpg", 
    "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=512426052,3777200390&fm=26&gp=0.jpg");
    
    PhotoGallery<String> gallery = new PhotoGallery<>(new PhotoGalleryable<String>() {
        @Override
        public List<String> getUrls() {
            return list;//设置图片数据集合
        }

        @Override
        public void bindData(String data, ImageView imageView) {
            //将图片数据加载到图片控件
            Glide.with(MainActivity.this).load(data).into(imageView);
        }
    }).showIndicator(list.size() == 1 ? PhotoGallery.NONE : PhotoGallery.CIRCLE).setCurrentItem(1);
    gallery.show(getSupportFragmentManager(), "gallery");//显示图片查看大图
}
    
```


## 效果图与示例 apk


![photo_gallery1](https://github.com/BenShanYang/PhotoGallery/blob/main/image_demo/1.jpg)
![photo_gallery2](https://github.com/BenShanYang/PhotoGallery/blob/main/image_demo/2.jpg)
