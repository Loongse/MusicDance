package com.md.lib_share.share;

import cn.sharesdk.framework.Platform.ShareParams;

/**
 * 需要分享的数据实体
 */
public class ShareData {
    /**
     * 分享的平台
     */
    public ShareManager.PlatformType mPlatformType;
    /**
     * 分享的参数
     */
    public ShareParams mShareParams;
}
