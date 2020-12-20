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
    //管理实体dao
    private static DaoSession mDaoSession;

    public static void initDataBase() {
        mhelper = new DaoMaster.DevOpenHelper(AudioHelper.getContext(), DB_NAME);
        mDb = mhelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 添加收藏
     *
     * @param bean
     */
    public static void addFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = new Favourite();
        favourite.setAudioId(bean.id);
        favourite.setAudioBean(bean);
        dao.insertOrReplace(favourite);
    }

    /**
     * 移除一个收藏
     *
     * @param bean
     */
    public static void removeFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = selectFavourite(bean);
        dao.delete(favourite);
    }

    /**
     * 查询一个收藏
     *
     * @param bean
     * @return
     */
    public static Favourite selectFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = dao.queryBuilder().where(
                FavouriteDao.Properties.AudioId.eq(bean.id)
        ).unique();
        return favourite;
    }
}
