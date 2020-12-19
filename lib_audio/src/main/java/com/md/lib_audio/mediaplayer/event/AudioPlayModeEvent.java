package com.md.lib_audio.mediaplayer.event;

import com.md.lib_audio.mediaplayer.core.AudioController;

/**
 * 播放模式切换事件
 */
public class AudioPlayModeEvent {
    public AudioController.PlayMode mPlayMode;

    public AudioPlayModeEvent(AudioController.PlayMode PlayMode) {
        this.mPlayMode = PlayMode;
    }
}
