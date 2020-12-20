package com.md.lib_audio.mediaplayer.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.md.lib_audio.app.AudioHelper;
import com.md.lib_audio.mediaplayer.event.AudioFavouriteEvent;
import com.md.lib_audio.mediaplayer.event.AudioLoadEvent;
import com.md.lib_audio.mediaplayer.event.AudioPauseEvent;
import com.md.lib_audio.mediaplayer.event.AudioStartEvent;
import com.md.lib_audio.mediaplayer.model.AudioBean;
import com.md.lib_audio.mediaplayer.view.NotificationHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.md.lib_audio.mediaplayer.view.NotificationHelper.NOTIFICATION_ID;

/**
 * 音乐后台服务,并更新notification状态
 */
public class MusicService extends Service implements NotificationHelper.NotificationHelperListener {

    /**
     * 常量
     */
    private static String DATA_AUDIOS = "AUDIOS";
    private static String ACTION_START = "ACTION_START";

    /**
     * data
     */
    private ArrayList<AudioBean> mAudioBeans;
    private NotificationReceiver mReceiver;

    /**
     * 外部直接使用service方法
     */
    public static void startMusicService(ArrayList<AudioBean> audioBeans) {
        Intent intent = new Intent(AudioHelper.getContext(), MusicService.class);
        intent.setAction(ACTION_START);
        //还需要将list数据传进来
        intent.putExtra(DATA_AUDIOS, audioBeans);
        AudioHelper.getContext().startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        registerBroadcastReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAudioBeans = (ArrayList<AudioBean>) intent.getSerializableExtra(DATA_AUDIOS);
        if (intent.getAction().equals(ACTION_START)) {
            //播放音乐
            plaMusic();
            //初始化notification
            NotificationHelper.getInstance().init(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void plaMusic() {
        AudioController.getInstance().setQueue(mAudioBeans);
        AudioController.getInstance().play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unRegisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new NotificationReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(NotificationReceiver.ACTION_STATUS_BAR);
            registerReceiver(mReceiver, filter);
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onNotificationInit() {
        //绑定notification与service，并且使用服务成为前台服务
        startForeground(NOTIFICATION_ID, NotificationHelper.getInstance().getNotification());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        //更新notification状态为加载状态
        NotificationHelper.getInstance().showLoadStatus(event.audioBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPlayEvent(AudioStartEvent event) {
        //更新notification状态为play状态
        NotificationHelper.getInstance().showPlayStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        //更新notification状态为pause状态
        NotificationHelper.getInstance().showPauseStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioFavouriteEvent(AudioFavouriteEvent event) {
        //更新notification状态为pause
        NotificationHelper.getInstance().changeFavouriteStatus(event.isFavourite);
    }

    /**
     * 接受notification发送的广播
     */
    public static class NotificationReceiver extends BroadcastReceiver {
        public static final String ACTION_STATUS_BAR =
                AudioHelper.getContext().getPackageName() + ".NOTIFICATION_ACTIONS";
        public static final String EXTRA = "extra";
        public static final String EXTRA_PLAY = "play_pause";
        public static final String EXTRA_NEXT = "play_next";
        public static final String EXTRA_PRE = "play_previous";
        public static final String EXTRA_FAV = "play_favourite";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            String action = intent.getStringExtra(EXTRA);
            switch (action) {
                case EXTRA_PLAY:
                    AudioController.getInstance().playOrPause();
                    break;
                case EXTRA_PRE:
                    AudioController.getInstance().previous();
                    ;
                    break;
                case EXTRA_NEXT:
                    AudioController.getInstance().next();
                    break;
                case EXTRA_FAV:
                    //收藏广播处理
                    AudioController.getInstance().changeFavouriteStatus();
                    break;
            }
        }
    }
}
