package com.kk.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.kk.zhbj.anim.LauncherAnim;
import com.kk.zhbj.constant.GlobalConstant;
import com.kk.zhbj.ui.GuideActivity;
import com.kk.zhbj.ui.MainActivity;
import com.kk.zhbj.utils.SPutil;

public class LauncherActivity extends Activity {
    ImageView ivLauncher;
    AnimationSet mAnimationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initUI();
        setAnimation();

    }

    private void goTo() {
        boolean isFirstRun = SPutil.getBoolean(this, GlobalConstant.ISFIRSTRUN, true);
        if (isFirstRun) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }

    private void setAnimation() {
        mAnimationSet = LauncherAnim.makeAnimation(ivLauncher);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    private void initUI() {
        ivLauncher = (ImageView) findViewById(R.id.iv_launcher);
    }
}
