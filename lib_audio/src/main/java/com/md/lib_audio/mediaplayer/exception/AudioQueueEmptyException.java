package com.md.lib_audio.mediaplayer.exception;

/**
 * 播放队列为空异常
 */
public class AudioQueueEmptyException extends RuntimeException {
    public AudioQueueEmptyException(String err) {
        super(err);
    }
}
