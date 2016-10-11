package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.NewsMenuBean;
import com.kk.zhbj.ui.MainActivity;
import com.kk.zhbj.ui.pager.BaseMenuDetailPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 注释：菜单的新闻详情页  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/9 - 8:47 <br>
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ArrayList<NewsMenuBean.ChildContent> mChildren;  // 新闻的详细标签 数据
    @ViewInject(R.id.vp_newsMenuDetailPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator_newsMenuDetailPager)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.ib_newsMenuDetailPager)
    private ImageButton mIbcatArr;
    private ArrayList<NewsTabDetailPager> mNewsTabsPager;
    private SlidingMenu slidingMenu;

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsMenuBean.ChildContent> children) {
        super(mActivity);
        this.mChildren = children;
        slidingMenu = ((MainActivity) mActivity).getSlidingMenu();

        initData();
    }

    @Override
    protected View initDetailContentUI() {
        View inflate = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, inflate);
        return inflate;
    }

    @Override
    public void initData() {
        mViewPager.setAdapter(new NewsDetailViewPagerAdapter());
        mIndicator.setViewPager(mViewPager);
        // 只有选中第一个标签时 才打开菜单
        // TODO: 2016/10/11  再向左滑动 tab 新闻的 ViewPager 时 不能 返回图片 
       /* mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(mActivity, " 选中 "+position, Toast.LENGTH_SHORT).show();

               if (position==0){
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/
        // 点击右则按钮 进入下一个标签页
        mIbcatArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
        // 标签的具体页面内容
        mNewsTabsPager = new ArrayList<>();
        for (int i = 0; i < mChildren.size(); i++) {
            NewsTabDetailPager temp = new NewsTabDetailPager(mActivity, mChildren.get(i));
            temp.initData();
            mNewsTabsPager.add(temp);
        }

    }

    /**
     * ViewPager Adapter for member variable mViewPager
     */
    private class NewsDetailViewPagerAdapter extends PagerAdapter {
        private static final String TAG = "main";

        // 指示器调用此方法 设置 标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildren.get(position).title;
        }

        @Override
        public int getCount() {
            return mChildren.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsTabDetailPager newsTab = mNewsTabsPager.get(position);
            container.addView(newsTab.mRootView);
            Log.i(TAG, "NewsDetailViewPagerAdapter.instantiateItem: 增加 tab news " + position);
            return newsTab.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(TAG, "NewsDetailViewPagerAdapter.instantiateItem: 删除 tab news " + position);

            object = null;
            System.gc();
        }
    }
}


