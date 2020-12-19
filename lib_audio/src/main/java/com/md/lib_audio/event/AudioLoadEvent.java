package com.md.lib_audio.event;

import com.md.lib_audio.model.AudioBean;

public class AudioLoadEvent {
    public AudioBean audioBean;

    public AudioLoadEvent(AudioBean audioBean) {
        this.audioBean = audioBean;
    }
}
