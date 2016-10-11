package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 注释： 让父控件 不拦截事件  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/10 - 20:09 <br>
 */

public class DisAllowIntercepteViewPager extends ViewPager {
    private int startX;
    private int startY;

    public DisAllowIntercepteViewPager(Context context) {
        super(context);
    }

    public DisAllowIntercepteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dy) < Math.abs(dx)) {
                    int currentItem = getCurrentItem();
                    // 左右滑动
                    if (dx > 0) {
                        // 向右划
                        if (currentItem == 0) {
                            // 第一个页面,需要拦截
                            requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        // 向左划
                        int count = getAdapter().getCount();// item总数
                        if (currentItem == count - 1) {
                            // 最后一个页面,需要拦截
                            requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    // 上下滑动,需要拦截
                    requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }
        return super.onInterceptHoverEvent(ev);
    }
}
