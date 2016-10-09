package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kk.zhbj.ui.pager.BaseMenuDetailPager;

/**
 * 注释：菜单组图详情页  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/9 - 8:51 <br>
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    public PhotoMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View initDetailContentUI() {
        TextView view = new TextView(mActivity);
        view.setText("菜单组图详情页 ");
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        return view;
    }
}
