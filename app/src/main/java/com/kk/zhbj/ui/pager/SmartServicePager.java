package com.kk.zhbj.ui.pager;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 注释：SmartServicePager 页面的pager <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/8 - 17:37 <br>
 */

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initContentData() {

    }

    @Override
    protected void initContentUI(FrameLayout mFlContent) {
        TextView textView = new TextView(mActivity);
        textView.setText("这里是SmartServicePager");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        textView.setGravity(Gravity.CENTER);
        mFlContent.addView(textView);

        getmTvTitle().setText("生活");
    }


}
