package com.md.lib_audio.mediaplayer.model;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 收藏表
 */
public class Favourite {
    @Id(autoincrement = true)
    Long favouriteId;
    @NotNull
    String audioId;
    //一条收藏记录唯一的一个实体
    AudioBean audioBean;

    public Favourite(Long favouriteId, @NotNull String audioId) {
        this.favouriteId = favouriteId;
        this.audioId = audioId;
    }

}
