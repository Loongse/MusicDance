package com.md.lib_audio.mediaplayer.core;

import android.util.Log;

import com.md.lib_audio.mediaplayer.db.GreenDaoHelper;
import com.md.lib_audio.mediaplayer.event.AudioCompleteEvent;
import com.md.lib_audio.mediaplayer.event.AudioErrorEvent;
import com.md.lib_audio.mediaplayer.event.AudioFavouriteEvent;
import com.md.lib_audio.mediaplayer.event.AudioPlayModeEvent;
import com.md.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.md.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

/**
 * 控制播放逻辑：添加控制方法时需要考虑通过event来通知更新UI
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
    //播放队列不能为空
    private ArrayList<AudioBean> mQueue = new ArrayList<>();//歌曲队列
    private int mQueueIndex = 0;//当前播放歌曲索引
    private PlayMode mPlayMode = PlayMode.LOOP;//循环模式

    private AudioController() {
        EventBus.getDefault().register(this);
        mAudioPlayer = new AudioPlayer();
    }

    /**
     * 获取播放状态
     */
    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getStatus();
    }

    private AudioBean getPlaying(int index) {
        if (mQueue != null && !mQueue.isEmpty() && mQueueIndex >= 0 && mQueueIndex < mQueue.size()) {
            return mQueue.get(index);
        } else {
            throw new AudioQueueEmptyException("当前播放队列为空，请先设置播放队列");
        }
    }

    //获取下一首播放
    private AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
        }
        return null;
    }

    //获取上一首播放
    private AudioBean getPreviousPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex - 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
        }
        return null;
    }

    //加载
    private void load(AudioBean bean) {
        mAudioPlayer.load(bean);
    }

    public ArrayList<AudioBean> getQueue() {
        return mQueue == null ? new ArrayList<>() : mQueue;
    }

    /**
     * 设置播放队列
     *
     * @param queue
     */
    public void setQueue(ArrayList<AudioBean> queue) {
        setQueue(queue, 0);
    }

    public void setQueue(ArrayList<AudioBean> queue, int queueIndex) {
        mQueue.addAll(queue);
        mQueueIndex = queueIndex;
    }

    private void addCustomAudio(int index, AudioBean bean) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空，请先设置播放队列");
        }
        mQueue.add(index, bean);
    }

    private int queryAudio(AudioBean bean) {
        return mQueue.indexOf(bean);
    }

    /**
     * 队首添加歌曲
     */
    public void addAudio(AudioBean bean) {
        addAudio(0, bean);//默认添加在首部
    }

    public void addAudio(int index, AudioBean bean) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列");
        }
        int query = queryAudio(bean);
        if (query <= -1) {
            //没有添加过,添加且直接播放
            addCustomAudio(index, bean);
            setPlayIndex(index);
        } else {
            AudioBean currentBean = getNowPlaying();
            if (!currentBean.id.equals(bean.id)) {
                //已经添加过且不在播放
                setPlayIndex(query);
            }
        }
    }

    //获取播放模式
    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    public void setPlayMode(PlayMode playMode) {
        this.mPlayMode = playMode;
        //还需要对外发送切换事件，修改UI
        EventBus.getDefault().post(new AudioPlayModeEvent(mPlayMode));
    }

    public void setPlayIndex(int index) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空，请先设置播放队列");
        }
        mQueueIndex = index;
        play();
    }

    public int getPlayIndex() {
        return mQueueIndex;
    }

    /**
     * 对外提供当前获取的歌曲信息
     */
    public AudioBean getNowPlaying() {
        return getPlaying(mQueueIndex);
    }

    /**
     * 对外提供的play方法
     */
    public void play() {
        AudioBean bean = getPlaying(mQueueIndex);
        load(bean);
    }

    public void pause() {
        mAudioPlayer.pause();
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    public void release() {
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 播放下一首
     */
    public void next() {
        AudioBean bean = getNextPlaying();
        load(bean);
    }

    /**
     * 播放上一首
     */
    public void previous() {
        AudioBean bean = getPreviousPlaying();
        load(bean);
    }

    /**
     * 自动切换、暂停
     */
    public void playOrPause() {
        Log.e("longchao", "playOrPause");
        if (isStartStatus()) {
            pause();
        } else if (isPauseStatus()) {
            resume();
        }
    }

    public void changeFavouriteStatus() {
        if (null != GreenDaoHelper.selectFavourite(getNowPlaying())) {
            //取消收藏
            GreenDaoHelper.removeFavourite(getNowPlaying());
            EventBus.getDefault().post(new AudioFavouriteEvent(false));
        } else {
            GreenDaoHelper.addFavourite(getNowPlaying());
            EventBus.getDefault().post(new AudioFavouriteEvent(true));
        }
    }

    /**
     * 对外提供获取当前播放事件
     */
    public int getNowPlayTime() {
        return mAudioPlayer.getCurrentPosition();
    }

    /**
     * 对外提供歌曲总时长
     */
    public int getTotalPlayTime() {
        return mAudioPlayer.getDuration();
    }

    /**
     * 对外提供是否播放中状态
     */
    public boolean isStartStatus() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提供是否暂停状态
     */
    public boolean isPauseStatus() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }

    //播放完毕的事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnAudioCompleteEvent(AudioCompleteEvent event) {
        next();
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnAudioErrorEvent(AudioErrorEvent event) {
        next();
    }
}
