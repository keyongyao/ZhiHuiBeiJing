package com.kk.zhbj.ui.pager;

import android.app.Activity;
import android.view.View;

/**
 * 注释：新闻详情页  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/9 - 8:41 <br>
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initDetailContentUI();  //先初始化UI
        //  initData();   //再填充数据
    }

    /**
     * @return 新闻详情的页面
     */
    protected abstract View initDetailContentUI();

    /**
     * 初始化数据
     */
    public void initData() {

    }


}
