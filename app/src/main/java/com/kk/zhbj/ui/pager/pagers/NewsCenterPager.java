package com.kk.zhbj.ui.pager.pagers;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.NewsMenuBean;
import com.kk.zhbj.constant.GlobalConstant;
import com.kk.zhbj.ui.MainActivity;
import com.kk.zhbj.ui.fragment.LeftMenuFragment;
import com.kk.zhbj.ui.pager.BaseMenuDetailPager;
import com.kk.zhbj.ui.pager.BasePager;
import com.kk.zhbj.ui.pager.menuDetailPagers.InterActiveMenuDetailPager;
import com.kk.zhbj.ui.pager.menuDetailPagers.NewsMenuDetailPager;
import com.kk.zhbj.ui.pager.menuDetailPagers.PhotoMenuDetailPager;
import com.kk.zhbj.ui.pager.menuDetailPagers.TopicMenuDetailPager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 注释：NewsCenterPager 页面的pager <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/8 - 17:37 <br>
 */

public class NewsCenterPager extends BasePager {
    private static final String TAG = "main";
    private final static int LoadingOK = 2;
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagerList;
    private NewsMenuBean mNewsMenuBean;
    private Handler mHandler;

    public NewsCenterPager(Activity mActivity) {
        super(mActivity);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LoadingOK) {
                    setCurrentMenuDetailPager(0);
                    getmTvTitle().setText(mNewsMenuBean.data.get(0).title);
                }
            }
        };
    }

    @Override
    protected void initContentData() {
        getNewsCategoriesFromServer();

    }

    /**
     * 从服务器下载数据
     */
    private void getNewsCategoriesFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, GlobalConstant.NEWCATEGROREIES, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e(TAG, "onFailure: " + msg);
            }
        });
    }

    // 处理从服务器获取的新闻分类json
    private void processData(String jsonStr) {
        Gson gson = new Gson();
        mNewsMenuBean = gson.fromJson(jsonStr, NewsMenuBean.class);
        Log.i(TAG, "NewsCenterPager.processData: " + mNewsMenuBean.toString());
        MainActivity ma = (MainActivity) this.mActivity;
        LeftMenuFragment leftMenuFragment = ma.getmLeftMenuFragment();
        // 把菜单的数据传给 leftMenuFragment
        leftMenuFragment.setmMenudata(mNewsMenuBean.data);

        // 初始化 4个菜单详情页面
        mMenuDetailPagerList = new ArrayList<>();
        mMenuDetailPagerList.add(new NewsMenuDetailPager(mActivity, mNewsMenuBean.data.get(0).children));
        mMenuDetailPagerList.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagerList.add(new PhotoMenuDetailPager(mActivity));
        mMenuDetailPagerList.add(new InterActiveMenuDetailPager(mActivity));
        Message msg = Message.obtain();
        msg.what = LoadingOK;
        mHandler.sendMessage(msg);
    }

    // 初始化界面
    @Override
    protected void initContentUI(FrameLayout mFlContent) {
        // 加载数据的展示页面
        View inflate = View.inflate(mActivity, R.layout.newspager_loding, null);
        mFlContent.addView(inflate);
    }

    /**
     * 设置菜单的详情页面<br>
     *
     * @param position 菜单位置
     */
    public void setCurrentMenuDetailPager(int position) {
        FrameLayout contentFrameLayout = getmFlContent();
        contentFrameLayout.removeAllViews();
        contentFrameLayout.addView(mMenuDetailPagerList.get(position).mRootView);
        getmTvTitle().setText(mNewsMenuBean.data.get(position).title);
    }

}
