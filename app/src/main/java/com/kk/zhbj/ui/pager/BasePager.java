package com.kk.zhbj.ui.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kk.zhbj.R;
import com.kk.zhbj.ui.MainActivity;

/**
 * 注释：正文的body fragment   <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/8 - 17:06 <br>
 */

public abstract class BasePager {
    public View mRootView;
    protected Activity mActivity;
    private TextView mTvTitle;
    private ImageButton mIbMenu;
    private FrameLayout mFlContent;

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initRootUI();
        // 菜单按钮点击监听
        mIbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenuToggle();
            }
        });
        initContentData();
        initContentUI(mFlContent);
    }

    /**
     * 初始化 UI 所需的一些数据
     */
    protected abstract void initContentData();


    /**
     * 子类各自实现 填充内容 FrameLayout<br>
     *
     * @param mFlContent 等待填充内容的 正文FrameLayout
     */
    protected abstract void initContentUI(FrameLayout mFlContent);

    /**
     * 弹出 或 收起  菜单
     */
    private void slidingMenuToggle() {
        MainActivity ma = (MainActivity) mActivity;
        SlidingMenu slidingMenu = ma.getSlidingMenu();
        slidingMenu.toggle();
    }

    /**
     * @return 初始化根布局
     */
    public View initRootUI() {
        // 吹涨 布局
        View inflate = View.inflate(mActivity, R.layout.contentpager_base, null);
        // 标题
        mTvTitle = (TextView) inflate.findViewById(R.id.tv_contentTitle_title);
        // 右侧 菜单按钮
        mIbMenu = (ImageButton) inflate.findViewById(R.id.ib_contentTitle_menu);
        // body frameLayout 布局
        mFlContent = (FrameLayout) inflate.findViewById(R.id.fl_contentPager_base);
        return inflate;
    }

    /**
     * @return 标题TextView
     */
    public TextView getmTvTitle() {
        return mTvTitle;
    }

    /**
     * @return 右侧菜单 ImageButton
     */
    public ImageButton getmIbMenu() {
        return mIbMenu;
    }

    /**
     *
     * @return body 正文 frameLayout
     */
    /*
    public FrameLayout getmFlContent() {
        return mFlContent;
    }*/

}
