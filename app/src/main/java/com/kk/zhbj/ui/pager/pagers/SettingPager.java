package com.kk.zhbj.ui.pager.pagers;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kk.zhbj.ui.pager.BasePager;

/**
 * 注释：SettingPager 页面的pager <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/8 - 17:37 <br>
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initContentData() {

    }

    @Override
    protected void initContentUI(FrameLayout mFlContent) {
        TextView textView = new TextView(mActivity);
        textView.setText("这里是设置");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        textView.setGravity(Gravity.CENTER);
        mFlContent.addView(textView);

        getmTvTitle().setText("设置");
        getmIbMenu().setVisibility(View.GONE);
    }


}
