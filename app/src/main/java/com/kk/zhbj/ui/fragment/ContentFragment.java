package com.kk.zhbj.ui.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kk.zhbj.R;
import com.kk.zhbj.ui.MainActivity;
import com.kk.zhbj.ui.pager.BasePager;
import com.kk.zhbj.ui.pager.GovAffairsPager;
import com.kk.zhbj.ui.pager.HomePager;
import com.kk.zhbj.ui.pager.NewsCenterPager;
import com.kk.zhbj.ui.pager.SettingPager;
import com.kk.zhbj.ui.pager.SmartServicePager;

import java.util.ArrayList;

/**
 * 注释：正文的Fragment                                      <br>
 * 作者： kk                                   <br>
 * QQ  :                                       <br>
 * 创建时间：2016/10/8 - 15:03                  <br>
 */

public class ContentFragment extends BaseFragment {
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private ArrayList<BasePager> mBasePagersList;

    @Override
    protected void setListener() {
        // 底部的 tab标签按钮
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        Toast.makeText(mActivity, "点击首页", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_govaffairs:
                        Toast.makeText(mActivity, "点击政务", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_newscenter:
                        Toast.makeText(mActivity, "点击新闻中心", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smartservice:
                        Toast.makeText(mActivity, "点击智慧服务", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_setting:
                        Toast.makeText(mActivity, "点击设置", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });
        // 根据用户打开的页面 动态 禁用或启用 右侧菜单
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 4) {
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {
        // 填充 5 个 pagers
        mBasePagersList = new ArrayList<>();
        mBasePagersList.add(new HomePager(mActivity));
        mBasePagersList.add(new NewsCenterPager(mActivity));
        mBasePagersList.add(new SmartServicePager(mActivity));
        mBasePagersList.add(new GovAffairsPager(mActivity));
        mBasePagersList.add(new SettingPager(mActivity));
        // viewpager 监视器
        mViewPager.setAdapter(new MyViewPager());
        // 设置偏移的pager个数
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public View initView() {
        View inflate = View.inflate(mActivity, R.layout.fragment_main_content, null);
        mRadioGroup = (RadioGroup) inflate.findViewById(R.id.rg_mainContent);
        mViewPager = (ViewPager) inflate.findViewById(R.id.vp_mainContent);
        // 禁用右侧菜单
        setSlidingMenuEnable(false);
        return inflate;
    }

    /**
     * @param isEnable 是否启用左侧边菜单
     */
    private void setSlidingMenuEnable(boolean isEnable) {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (isEnable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    class MyViewPager extends PagerAdapter {

        @Override
        public int getCount() {
            return mBasePagersList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View mRootView = mBasePagersList.get(position).mRootView;
            container.addView(mRootView);
            return mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) (object));
        }
    }

}
