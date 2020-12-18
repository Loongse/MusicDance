package com.md.lib_image_loader.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.Target;
import com.md.lib_image_loader.R;

/**
 * 图片加载类：本模块唯一对外暴露的接口，支持为view，notification，app widget加载
 */
public class ImageLoaderManager {
    private ImageLoaderManager() {
    }

    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 为ImageView加载图片
     */
    public void displayImageForView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 为notification加载图片
     */
    public void displayForNotification(Context context, RemoteViews rv,
                                       int id, Notification notification,
                                       int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(context, initNotificationTarget(context, rv, id, notification, NOTIFICATION_ID), url);
    }

    private NotificationTarget initNotificationTarget(Context context, RemoteViews rv,
                                                      int id, Notification notification,
                                                      int notification_id) {
        NotificationTarget target =
                new NotificationTarget(context, id,
                        rv, notification, notification_id);
        return target;
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .into(target);
    }

    @SuppressLint("CheckResult")
    private BaseRequestOptions<?> initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }
}
