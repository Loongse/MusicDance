package com.md.lib_audio.app;

import android.content.Context;

import com.md.lib_audio.mediaplayer.core.AudioController;
import com.md.lib_audio.mediaplayer.core.MusicService;
import com.md.lib_audio.mediaplayer.db.GreenDaoHelper;
import com.md.lib_audio.mediaplayer.model.AudioBean;

import java.util.ArrayList;

/**
 * lib_audio与外部通信类
 */
public final class AudioHelper {
    //SDK全局Context 提供给子类使用
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        GreenDaoHelper.initDataBase();
    }

    public static Context getContext() {
        return mContext;
    }

    //外部启动MusicService
    public static void startMusicService(ArrayList<AudioBean> audios) {
        MusicService.startMusicService(audios);
    }

    public static void pauseAudio() {
        AudioController.getInstance().pause();
    }

    public static void resumeAudio() {
        AudioController.getInstance().resume();
    }
}
