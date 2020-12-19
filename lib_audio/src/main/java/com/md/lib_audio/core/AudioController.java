package com.md.lib_audio.core;

import android.media.AudioManager;

import com.md.lib_audio.model.AudioBean;

import java.util.ArrayList;

/**
 * 控制播放逻辑
 */
public class AudioController {
    /**
     * 播放方式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT
    }

    /**
     * 单例方法
     */
    public static AudioController getInstance() {
        return AudioController.SingletonHolder.instance;
    }

    public static class SingletonHolder {
        private static AudioController instance = new AudioController();
    }

    private AudioPlayer mAudioPlayer;//核心播放器
    private ArrayList<AudioBean> mQueue;//歌曲队列
    private int mQueueIndex;//当前播放歌曲索引
    private PlayMode mPlayMode;//循环模式

    private AudioController() {
        mAudioPlayer = new AudioPlayer();
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        mPlayMode = PlayMode.LOOP;
    }
}
