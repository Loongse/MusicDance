package com.md.lib_audio.mediaplayer.utils;

/**
 * 工具类
 */
public class Utils {
    /**
     * 毫秒->分秒
     */
    public static String formatTime(long time) {
        String min = (time / (1000 * 60)) + "";
        String second = (time % (1000 * 60) / 1000) + "";
        if (min.length() < 2) {
            min = 0 + min;
        }
        if (second.length() < 2) {
            second = 0 + second;
        }
        return min + second;
    }
}
