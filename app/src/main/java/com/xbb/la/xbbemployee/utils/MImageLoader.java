package com.xbb.la.xbbemployee.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.MLog;

import java.io.File;

public class MImageLoader {
    private static MImageLoader mImageLoader;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    public static final String URL_IMGCACHE = Constant.Path.PATH_CACHE
            + File.separator + "ImgCache";

    private MImageLoader(Context context) {

        if (imageLoader == null) {

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    context)
                    .diskCache(new UnlimitedDiskCache(new File(URL_IMGCACHE)))
                    .defaultDisplayImageOptions(
                            DisplayImageOptions.createSimple()).build();

            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).cacheInMemory(true)
                            // .showImageOnFail(R.drawable.icon_test)
                            // .showImageForEmptyUri(R.drawable.icon_test)
//                    .showImageForEmptyUri(com.github.yanglw.selectimages.R.drawable.sample)
//                    .showImageOnFail(com.github.yanglw.selectimages.R.drawable.sample)
//                    .showImageOnLoading(com.github.yanglw.selectimages.R.drawable.sample)
                    .cacheOnDisk(true).cacheInMemory(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
        }
    }

    public static MImageLoader getInstance(Context context) {

        syncInit(context);

        return mImageLoader;
    }

    private static synchronized void syncInit(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new MImageLoader(context);
        }
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void displayImage(String uri, ImageView imageView) {
        imageLoader.displayImage(uri, imageView, displayImageOptions);
    }

    public void displayImageM(String uri, ImageView imageView) {
        displayImage(getAbsoluteURL(uri), imageView);
    }

    public void displayImage(String uri, ImageView imageView,
                             DisplayImageOptions displayImageOptions) {
        imageLoader.displayImage(uri, imageView);
    }

    public void displayImageByAbsoluteUrl(String uri, ImageView imageView, int defaultImgId, ImageLoadingListener listener) {
        DisplayImageOptions myOptions = new DisplayImageOptions.Builder()
                // .showImageOnFail(R.drawable.icon_test)
                // .showImageForEmptyUri(R.drawable.icon_test)
                .showImageForEmptyUri(defaultImgId)
                .showImageOnFail(defaultImgId)
                .showImageOnLoading(defaultImgId)
                .cacheOnDisk(true).cacheInMemory(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader.displayImage(uri, imageView, myOptions, listener);
    }

    public void displayImageByHalfUrl(String uri, ImageView imageView, int defaultImgId, ImageLoadingListener listener) {
        DisplayImageOptions myOptions = new DisplayImageOptions.Builder()
                // .showImageOnFail(R.drawable.icon_test)
                // .showImageForEmptyUri(R.drawable.icon_test)
                .showImageForEmptyUri(defaultImgId)
                .showImageOnFail(defaultImgId)
                .showImageOnLoading(defaultImgId)
                .cacheOnDisk(true).cacheInMemory(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader.displayImage(getAbsoluteURL(uri), imageView, myOptions, listener);
    }

    public void loadImage(String uri) {

        imageLoader.loadImage(uri, displayImageOptions, null);
    }

    public void loadImage(String uri, ImageLoadingListener listener) {

        imageLoader.loadImage(uri, displayImageOptions, listener);
    }

    public Bitmap loadImageSync(String uri) {
        return imageLoader.loadImageSync(uri);
    }

    public Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
        return imageLoader.loadImageSync(uri, targetImageSize);
    }

    public static String getAbsoluteURL(String url) {
        return Constant.Path.URL_PREFIX_FILE + url;
    }

}
