package com.md.lib_audio.event;

/**
 * 播放模式切换事件
 */
public class AudioPlayModeEvent {
    public AudioController.PlayMode mPlayMode;

    public AudioPlayModeEvent(AudioController.PlayMode PlayMode) {
        this.mPlayMode = PlayMode;
    }
}
