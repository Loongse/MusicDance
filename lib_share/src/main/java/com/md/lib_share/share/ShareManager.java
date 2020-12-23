package com.md.lib_share.share;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享功能统一入口，负责发送数据到指定平台
 */
public class ShareManager {
    private static ShareManager mShareManager = new ShareManager();
    /**
     * 分享到的平台
     */
    private Platform mCurrentPlatform;

    public static ShareManager getInstance() {
        return mShareManager;
    }

    /**
     * 第一个执行的方法，程序入口执行
     */
    public static void init(Context context) {
        ShareSDK.initSDK(context);
    }

    /**
     * 分享数据到不同平台
     */
    public void shareData(ShareData shareData, PlatformActionListener listener) {
        switch (shareData.mPlatformType) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            case WeChat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WechatMoments:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
            default:
                break;
        }
        mCurrentPlatform.setPlatformActionListener(listener);//应用层回调处理
        mCurrentPlatform.share(shareData.mShareParams);
    }

    /**
     * 需要分享的平台
     */
    public enum PlatformType {
        QQ, QZone, WeChat, WechatMoments
    }
}
