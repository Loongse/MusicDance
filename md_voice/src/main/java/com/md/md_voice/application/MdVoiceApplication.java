package com.md.md_voice.application;

import android.app.Application;

import com.md.lib_audio.app.AudioHelper;
import com.md.lib_share.share.ShareManager;

public class MdVoiceApplication extends Application {
    private static MdVoiceApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //音频SDK初始化
        AudioHelper.init(this);
        //分享SDK初始化
        ShareManager.init(this);
    }

    public static MdVoiceApplication getInstance() {
        return mApplication;
    }
}
