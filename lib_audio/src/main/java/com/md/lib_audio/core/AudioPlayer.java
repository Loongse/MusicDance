package com.md.lib_audio.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.md.lib_audio.app.AudioHelper;
import com.md.lib_audio.model.AudioBean;

/**
 * 1.播放音频
 * 2.对外发送各种类型的事件
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener
        , MediaPlayer.OnErrorListener, AudioFocusManager.AudioFocusListener {
    private static final String TAG = "AudioPlayer";
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 100;
    //真正负责音频播放
    private CustomMediaPlayer mMediaPlayer;
    private WifiManager.WifiLock mWifiLock;
    //音频焦点监视器
    private AudioFocusManager mAudioFocusManager;
    private boolean isPauseByFocusLossTransient;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    break;
            }
        }
    };

    public AudioPlayer() {
        init();
    }

    //初始化
    private void init() {
        mMediaPlayer = new CustomMediaPlayer();
        mMediaPlayer.setWakeMode(null, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnErrorListener(this);
        //初始化wifilock
        mWifiLock = ((WifiManager) AudioHelper.getContext()
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE)).
                createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        mAudioFocusManager = new AudioFocusManager(AudioHelper.getContext(), this);
    }

    //获取播放器状态
    public CustomMediaPlayer.Status getStatus() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getStatus();
        } else {
            return CustomMediaPlayer.Status.STOPPTED;
        }
    }

    //设置音量
    private void setVolume(float leftVol, float rightVol) {
        if (mMediaPlayer != null) mMediaPlayer.setVolume(leftVol, rightVol);
    }

    /**
     * 对内提供加载方法
     *
     * @param audioBean
     */
    public void load(AudioBean audioBean) {
        try {
            //正常加载逻辑
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(audioBean.mUrl);
            mMediaPlayer.prepareAsync();
            //对外发送load事件
        } catch (Exception e) {
            //对外发送error事件
        }
    }

    /**
     * 内部开始播放
     */
    private void start() {
        if (!mAudioFocusManager.requestAudioFocus()) {
            Log.e(TAG, "获取音频焦点失败");
        }
        mMediaPlayer.start();
        mWifiLock.acquire();
        //对外开放start事件
    }

    /**
     * 对外提供暂停方法
     */
    public void pause() {
        if (getStatus() == CustomMediaPlayer.Status.STARTED) {
            mMediaPlayer.pause();
            //释放音频焦点WiFi lock
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
            //释放音频焦点
            if (mAudioFocusManager != null) {
                mAudioFocusManager.abandonAudioFocus();
            }
            //发送暂停事件
        }
    }

    /**
     * 对外提供恢复方法
     */
    public void resume() {
        if (getStatus() == CustomMediaPlayer.Status.PAUSED) {
            //直接复用start
            start();
        }
    }

    public void release() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (mAudioFocusManager != null) {
            mAudioFocusManager.abandonAudioFocus();
        }
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
        mWifiLock = null;
        mAudioFocusManager = null;
        //发送release销毁事件
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void audioFocusGrant() {
        //再次获得焦点
        setVolume(1.0f, 1.0f);
        if (isPauseByFocusLossTransient) {
            resume();
        }
        isPauseByFocusLossTransient = false;
    }

    @Override
    public void audioFocusLoss() {
        //永久失去焦点
        pause();
    }

    @Override
    public void audioFocusLossTransient() {
        //暂时失去焦点
        pause();
        isPauseByFocusLossTransient = true;
    }

    @Override
    public void audioFocusLossDuck() {
        //瞬间失去焦点
        setVolume(0.5f, 0.5f);
    }
}
