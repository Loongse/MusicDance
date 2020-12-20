package com.md.lib_audio.mediaplayer.db;

import android.database.sqlite.SQLiteDatabase;

import com.md.lib_audio.app.AudioHelper;
import com.md.lib_audio.mediaplayer.model.AudioBean;
import com.md.lib_audio.mediaplayer.model.Favourite;

/**
 * 用于操作greenDao数据库
 */
public class GreenDaoHelper {
    private static final String DB_NAME = "music_db";
    //数据库辅助类：用于创建数据库以及升级数据库
    private static DaoMaster.DevOpenHelper mhelper;
    //最终创建的数据库
    private static SQLiteDatabase mDb;
    //管理数据库
    private static DaoMaster mDaoMaster;
    //管理实体dao:不对业务层直接暴露
    private static DaoSession mDaoSession;

    /**
     * 初始化greendao
     */
    public static void initDataBase() {
        mhelper = new DaoMaster.DevOpenHelper(AudioHelper.getContext(), DB_NAME, null);
        mDb = mhelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 添加收藏
     */
    public static void addFavourite(AudioBean audioBean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = new Favourite();
        favourite.setAudioId(audioBean.id);
        favourite.setAudioBean(audioBean);
        dao.insertOrReplace(favourite);
    }

    /**
     * 移除一个收藏
     */
    public static void removeFavourite(AudioBean audioBean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = dao.queryBuilder()
                .where(FavouriteDao.Properties.AudioId.eq(audioBean.id)).unique();
        dao.delete(favourite);
    }

    /**
     * 查询一个收藏
     */
    public static Favourite selectFavourite(AudioBean audioBean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = dao.queryBuilder().where(
                FavouriteDao.Properties.AudioId.eq(audioBean.id)
        ).unique();
        return favourite;
    }
}
