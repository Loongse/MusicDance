package com.md.lib_audio.mediaplayer.event;

import com.md.lib_audio.mediaplayer.model.AudioBean;

public class AudioLoadEvent {
    public AudioBean audioBean;

    public AudioLoadEvent(AudioBean audioBean) {
        this.audioBean = audioBean;
    }
}
