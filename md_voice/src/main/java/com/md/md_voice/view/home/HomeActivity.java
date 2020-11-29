package com.md.md_voice.view.home;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.md.md_voice.R;
import com.md.md_voice.view.home.model.CHANNEL;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private static final CHANNEL[] CHANNELS =
            new CHANNEL[]{
                    CHANNEL.MY,
                    CHANNEL.DISCORY,
                    CHANNEL.FRIEND,
                    CHANNEL.VIDEO
            };

    /*
     * View
     */
    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mToggleView.setOnClickListener(this);
        mSearchView = findViewById(R.id.search_view);
    }

    @Override
    public void onClick(View view) {

    }
}