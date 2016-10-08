package com.kk.zhbj.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * 闪屏动画
 * Created by Administrator on 2016/10/8.
 */

public class LauncherAnim {
    public static AnimationSet makeAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet set = new AnimationSet(true);
        set.setDuration(2000);
        set.setFillAfter(true);
        set.addAnimation(scaleAnimation);
        set.addAnimation(rotateAnimation);
        view.startAnimation(set);
        return set;
    }
}
