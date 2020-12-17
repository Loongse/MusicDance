package com.md.md_voice.view.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.md.md_voice.view.discory.DiscoryFragment;
import com.md.md_voice.view.friend.FriendFragment;
import com.md.md_voice.view.home.model.CHANNEL;
import com.md.md_voice.view.mine.MineFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private CHANNEL[] mList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }

    //初始化对应的fragment
    @Override
    public Fragment getItem(int position) {
        int type = mList[position].getValue();
        Log.d("TAG", ""+type);
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCORY_ID:
                return DiscoryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
            default:
                return MineFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}
