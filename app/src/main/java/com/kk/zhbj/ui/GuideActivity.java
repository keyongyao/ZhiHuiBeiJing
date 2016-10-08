package com.kk.zhbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kk.zhbj.R;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private ArrayList<ImageView> mImageViews;
    private LinearLayout mPointContainer;
    private Button mBtnStart;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mActivity = this;
        initData();
        initUI();
        setLinstener();
    }

    private void setLinstener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 设置开始按钮 是否可见
                if (position == mImageViews.size() - 1) {
                    mBtnStart.setVisibility(View.VISIBLE);
                } else {
                    mBtnStart.setVisibility(View.GONE);
                }
                // 设置圆点的状态
                for (int i = 0; i < mPointContainer.getChildCount(); i++) {
                    if (i == position)
                        mPointContainer.getChildAt(i).setEnabled(true);
                    else
                        mPointContainer.getChildAt(i).setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, MainActivity.class));
                mActivity.finish();
            }
        });
    }

    private void initData() {
        // 引导页面的图片View
        mImageViews = new ArrayList<>();
        int[] ImgIds = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        for (int i = 0; i < ImgIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(ImgIds[i]);
            mImageViews.add(iv);
        }
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guideActivity_guide);
        mBtnStart = (Button) findViewById(R.id.btn_guideActivity_start);
        mBtnStart.setVisibility(View.GONE);
        // 添加状态圆点
        mPointContainer = (LinearLayout) findViewById(R.id.ll_guideActivity_pointContainer);
        for (int i = 0; i < mImageViews.size(); i++) {
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.guide_activity_point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            if (i != 0) {
                params.leftMargin = 20;
            }
            point.setLayoutParams(params);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }

            mPointContainer.addView(point);
        }
        mViewPager.setAdapter(new MyPagerAdapter());

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
