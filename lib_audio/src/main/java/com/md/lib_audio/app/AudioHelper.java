package com.md.lib_audio.app;

import android.content.Context;

/**
 * lib_audio与外部通信类
 */
public final class AudioHelper {
    //SDK全局Context 提供给子类使用
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
