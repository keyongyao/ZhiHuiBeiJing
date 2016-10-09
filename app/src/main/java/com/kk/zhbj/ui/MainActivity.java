package com.kk.zhbj.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.kk.zhbj.R;
import com.kk.zhbj.ui.fragment.ContentFragment;
import com.kk.zhbj.ui.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    public static final String MAINFRAGMENT = "mainFragment";
    public static final String LEFTMENUFRAGMENT = "leftMenuFragment";
    private ContentFragment mContentFragment;
    private LeftMenuFragment mLeftMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置侧边菜单
        setBehindContentView(R.layout.layout_leftmenu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(400);

        initFragments();
    }

    // 用 fragment替换 上面的布局
    private void initFragments() {
        mContentFragment = new ContentFragment();
        mLeftMenuFragment = new LeftMenuFragment();
        FragmentManager fg = getSupportFragmentManager();
        FragmentTransaction transaction = fg.beginTransaction();
        transaction.replace(R.id.fl_main, mContentFragment, MAINFRAGMENT);
        transaction.replace(R.id.fl_leftmenu, mLeftMenuFragment, LEFTMENUFRAGMENT);
        transaction.commit();
    }

    public LeftMenuFragment getmLeftMenuFragment() {
        return mLeftMenuFragment;
    }

    public ContentFragment getmContentFragment() {
        return mContentFragment;
    }
}
