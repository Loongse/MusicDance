package com.md.lib_audio.mediaplayer.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.md.lib_audio.R;
import com.md.lib_audio.app.AudioHelper;
import com.md.lib_audio.mediaplayer.core.AudioController;
import com.md.lib_audio.mediaplayer.core.MusicService;
import com.md.lib_audio.mediaplayer.db.GreenDaoHelper;
import com.md.lib_audio.mediaplayer.model.AudioBean;
import com.md.lib_image_loader.app.ImageLoaderManager;

/**
 * 音乐Notification帮助类
 * 1.完成notification的创建和初始化
 * 2.对外提供表更新notification的方法
 */
public class NotificationHelper {
    public static final String CHANNEL_ID = "channel_id_audio";
    public static final String CHANNEL_NAME = "channel_name_audio";
    public static final int NOTIFICATION_ID = 0x111;
    /**
     * UI相关
     */
    //最终的Notification显示类
    private Notification mNotification;
    private RemoteViews mRemoteViews;//大布局
    private RemoteViews mSmallRemoteViews;//小布局
    private NotificationManager mNotificationManager;
    /**
     * data
     */
    private NotificationHelperListener mListener;
    private String packageName;
    //当前需要播放的歌曲bean
    private AudioBean mAudioBean;

    public static NotificationHelper getInstance() {
        return SingletonHolder.instance;
    }

    public void changeFavouriteStatus(boolean isFavourite) {
        if (mRemoteViews != null) {
            mRemoteViews.setImageViewResource(R.id.favourite_view, isFavourite ?
                    R.mipmap.note_btn_loved : R.mipmap.note_btn_love_white);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    private static class SingletonHolder {
        private static NotificationHelper instance = new NotificationHelper();
    }

    public void init(NotificationHelperListener listener) {
        mNotificationManager = (NotificationManager) AudioHelper.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        packageName = AudioHelper.getContext().getPackageName();
        mAudioBean = AudioController.getInstance().getNowPlaying();
        initNotification();
        mListener = listener;
        if (mListener != null) mListener.onNotificationInit();
    }

    /**
     * 创建notification
     */
    private void initNotification() {
        if (mNotification == null) {
            //首先创建notification
            initRemoteViews();
            //2构建notification
            //适配8.0的信息渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel =
                        new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(false);
                channel.enableVibration(false);
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(AudioHelper.getContext(), CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setCustomBigContentView(mRemoteViews)//大布局
                            .setContent(mSmallRemoteViews);//正常布局
            mNotification = builder.build();
            showLoadStatus(mAudioBean);
        }
    }

    /**
     * 创建notification的布局，默认的布局是loading状态
     */
    private void initRemoteViews() {
        int layoutId = R.layout.notification_big_layout;
        mRemoteViews = new RemoteViews(packageName, layoutId);
        mRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
        mRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);

        if (GreenDaoHelper.selectFavourite(mAudioBean) != null) {
            //被收藏过
            mRemoteViews.setImageViewResource(R.id.favourite_view, R.mipmap.note_btn_loved);
        } else {
            //没收藏过
            mRemoteViews.setImageViewResource(R.id.favourite_view, R.mipmap.note_btn_love_white);
        }

        int smallLayoutId = R.layout.notification_small_layout;
        mSmallRemoteViews = new RemoteViews(packageName, smallLayoutId);
        mSmallRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
        mSmallRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);

        //点击播放按钮要发送的广播
        Intent playIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_PLAY);
        PendingIntent playPendingIntent =
                PendingIntent.getBroadcast(AudioHelper.getContext(),
                        1, playIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.play_view, playPendingIntent);
        mSmallRemoteViews.setOnClickPendingIntent(R.id.play_view, playPendingIntent);

        //点击上一首需要发送的广播
        Intent previousIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_PRE);
        PendingIntent previousPendingIntent =
                PendingIntent.getBroadcast(AudioHelper.getContext(),
                        2, previousIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.previous_view, previousPendingIntent);

        //点击下一首需要发送的广播
        Intent nextIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_NEXT);
        PendingIntent nextPendingIntent =
                PendingIntent.getBroadcast(AudioHelper.getContext(),
                        3, nextIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.next_view, nextPendingIntent);
        mSmallRemoteViews.setOnClickPendingIntent(R.id.next_view, nextPendingIntent);

        //点击喜欢/收藏需要发送的广播
        Intent favouriteIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_FAV);
        PendingIntent favouritePendingIntent =
                PendingIntent.getBroadcast(AudioHelper.getContext(),
                        4, favouriteIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.favourite_view, favouritePendingIntent);
    }

    /**
     * 更新为加载状态
     */
    public void showLoadStatus(AudioBean bean) {
        mAudioBean = bean;
        if (mRemoteViews != null) {
            mRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white);
            mRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
            mRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);
            //为notification中的image view加载图片
            ImageLoaderManager.getInstance()
                    .displayForNotification(
                            AudioHelper.getContext(),
                            mRemoteViews,
                            R.id.image_view,
                            mNotification,
                            NOTIFICATION_ID,
                            mAudioBean.albumPic
                    );
            //更新收藏状态
            if (GreenDaoHelper.selectFavourite(mAudioBean) != null) {
                //被收藏过
                mRemoteViews.setImageViewResource(R.id.favourite_view, R.mipmap.note_btn_loved);
            } else {
                //没收藏过
                mRemoteViews.setImageViewResource(R.id.favourite_view, R.mipmap.note_btn_love_white);
            }
            //更新小图片
            mSmallRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white);
            mSmallRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
            mSmallRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);
            ImageLoaderManager.getInstance()
                    .displayForNotification(
                            AudioHelper.getContext(),
                            mSmallRemoteViews,
                            R.id.image_view,
                            mNotification,
                            NOTIFICATION_ID,
                            mAudioBean.albumPic
                    );
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    /**
     * 更新为播放状态
     */
    public void showPlayStatus() {
        if (mRemoteViews != null) {
            mRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white);
            mSmallRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    /**
     * 更新为暂停状态
     */
    public void showPauseStatus() {
        if (mRemoteViews != null) {
            mRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white);
            mSmallRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    public Notification getNotification() {
        return mNotification;
    }

    /**
     * 与音乐service的回调通信
     */
    public interface NotificationHelperListener {
        void onNotificationInit();
    }
}
