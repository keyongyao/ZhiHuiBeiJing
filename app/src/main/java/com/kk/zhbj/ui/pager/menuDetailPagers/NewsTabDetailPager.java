package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.NewsMenuBean;
import com.kk.zhbj.bean.NewsTabBean;
import com.kk.zhbj.constant.GlobalConstant;
import com.kk.zhbj.ui.ReadNewsActivity;
import com.kk.zhbj.ui.pager.BaseMenuDetailPager;
import com.kk.zhbj.utils.SPutil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 标签页的详细页面内容 再左右翻得时候  实时 保存 3 个viewPager 页面
 */

public class NewsTabDetailPager extends BaseMenuDetailPager {
    private static final String TAG = "main";
    private NewsTabBean mNewsTabBean;
    @ViewInject(R.id.vp_NewsTab)
    private DisAllowIntercepteViewPager mViewPager;
    @ViewInject(R.id.lv_NewsTab)
    private ListView mListView;
    private NewsMenuBean.ChildContent mChildTab;
    private String mChildUrl;
    private View mListHeader;
    private int mLoadMoreHeadHeight;
    private boolean mIsLoadingLMore;
    private TopicNewAdapter mTopicNewAdapter;
    private NewsAdapter mNewsAdapter;
    /**
     * // 处理下载下来的json 数据
     *
     * @param strJson json 的字符串文件
     */
    private float startY;

    public NewsTabDetailPager(Activity mActivity, NewsMenuBean.ChildContent child) {
        super(mActivity);
        mChildTab = child;
        mChildUrl = GlobalConstant.SERVER + mChildTab.url;
    }

    @Override
    protected View initDetailContentUI() {
        View inflate = View.inflate(mActivity, R.layout.news_list, null);
        ViewUtils.inject(this, inflate);
        mListHeader = View.inflate(mActivity, R.layout.news_list_item_header, null);
        ViewUtils.inject(this, mListHeader);

        View loadMoreHeader = View.inflate(mActivity, R.layout.news_list_loadmore_header, null);
        ViewUtils.inject(this, loadMoreHeader);
        // 添加 刷新 数据头部
        mListView.addHeaderView(loadMoreHeader);
        loadMoreHeader.measure(0, 0);
        mLoadMoreHeadHeight = loadMoreHeader.getMeasuredHeight();
        // 添加头部ViewPager视图
        mListView.addHeaderView(mListHeader);
        // 隐藏 加载更多的ListView 头部
        mListView.setPadding(0, -mLoadMoreHeadHeight,0,0);
        return inflate;
    }

    /**
     *  下载Tab 新闻的 json 文件
     */
    @Override
    public void initData() {
        // 下载 json 文件
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mChildUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 转换 json  为 bean 对象
                processInitData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e(TAG, "onFailure: " + msg);
            }
        });


    }

    private void processInitData(String strJson) {
        Gson gson = new Gson();

        mNewsTabBean = gson.fromJson(strJson, NewsTabBean.class);
        Log.i(TAG, "NewsTabDetailPager.processInitData: " + mNewsTabBean.toString());
        Log.e(TAG, "processInitData: =================================================");

        // 获取数据后 设置适配器
        mViewPager.setAdapter((mTopicNewAdapter = new TopicNewAdapter()));
        mListView.setAdapter((mNewsAdapter = new NewsAdapter()));

        // 设置 加载更多头部 可以触摸下拉出来
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getRawY();
                        float disY = moveY - startY;
                        if (mListView.getFirstVisiblePosition() == 0 && !mIsLoadingLMore) {
                            Log.i(TAG, "NewsTabDetailPager.onTouch:disY  mLoadMoreHeadHeight " + disY + "  " + mLoadMoreHeadHeight);
                            if (mListView.getPaddingTop() + disY > mLoadMoreHeadHeight * 2) {
                                mListView.setPadding(0, 0, 0, 0);
                                Log.e(TAG, "onTouch: mLoadMoreHeadHeight 正在加载更多数据！");
                                mIsLoadingLMore = true;
                                loadMoreData();

                            } else {

                                break;
                            }

                            startY = moveY;

                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });
        // 点击新闻阅读
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mActivity, "List position " + (position - 2), Toast.LENGTH_SHORT).show();
                // 标记已经阅读的新闻
                String value = SPutil.getString(mActivity, GlobalConstant.HADREADIDS, "") + mNewsTabBean.data.news.get(position - 2).id + ",";
                SPutil.putString(mActivity, GlobalConstant.HADREADIDS, value);
                mNewsAdapter.notifyDataSetChanged();

                // 跳转到新闻页
                String url = mNewsTabBean.data.news.get(position - 2).url.replace("http://10.0.2.2:8080/zhbj", GlobalConstant.SERVER);
                Log.i(TAG, "NewsTabDetailPager.onItemClick: " + url);
                Intent intent = new Intent(mActivity, ReadNewsActivity.class);
                intent.putExtra("url", url);
                mActivity.startActivity(intent);
            }
        });

    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        HttpUtils httpUtils = new HttpUtils();
        if (TextUtils.isEmpty(mNewsTabBean.data.more)) {
            // 延时 2 秒 关闭 刷新 listView 头部
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListView.setPadding(0, -mLoadMoreHeadHeight, 0, 0);
                            mIsLoadingLMore = false;
                            // 没有更多的数据了
                            Toast.makeText(mActivity, "已经是最新数据了", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 2000);

            return;
        }
        String more = GlobalConstant.SERVER + mNewsTabBean.data.more;
        Log.i(TAG, "NewsTabDetailPager.loadMoreData: 加载更多的URl " + more);
        httpUtils.send(HttpRequest.HttpMethod.GET, more, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processMoredata(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "加载更多数据出错", Toast.LENGTH_SHORT).show();
                    }
                });
                mIsLoadingLMore = false;
            }

        });
    }

    /**
     * 处理更多新闻数据
     *
     * @param jsonStr json 的 文本
     */
    private void processMoredata(String jsonStr) {
        Gson gson = new Gson();
        NewsTabBean moreNewsTabBean = gson.fromJson(jsonStr, NewsTabBean.class);
        // 再旧的数据基础上添加新的数据
        mNewsTabBean.data.more = moreNewsTabBean.data.more;
        mNewsTabBean.data.news.addAll(0, moreNewsTabBean.data.news);
        mNewsTabBean.data.topnews.addAll(0, moreNewsTabBean.data.topnews);
        mTopicNewAdapter.notifyDataSetChanged();
        mNewsAdapter.notifyDataSetChanged();
        mIsLoadingLMore = false;
        mListView.setPadding(0, -mLoadMoreHeadHeight, 0, 0);
    }

    /**
     * 大图 ViewPager 适配器
     */
    private class TopicNewAdapter extends PagerAdapter {
        ImageView imageView;
        private BitmapUtils mBitmapUtils;

        public TopicNewAdapter() {
            this.mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsTabBean.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String imgURL = mNewsTabBean.data.topnews.get(position).topimage;
            imgURL = imgURL.replace("http://10.0.2.2:8080/zhbj", GlobalConstant.SERVER);
            Log.e(TAG, "instantiateItem: imgURL  " + imgURL);

            mBitmapUtils.display(imageView, imgURL);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
            System.gc();
        }
    }

    /**
     * 新闻 列表条目 适配器
     */
    private class NewsAdapter extends BaseAdapter {
        BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            this.mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsTabBean.data.news.size();
        }

        @Override
        public NewsTabBean.NewsTabnews getItem(int position) {
            return mNewsTabBean.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.news_list_item, null);
                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.tv_NewsTab_news_item_title);
                holder.pubDate = (TextView) convertView.findViewById(R.id.tv_NewsTab_news_item_pubDate);
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_NewsTab_news_item_icon);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.title.setText(getItem(position).title);
            holder.pubDate.setText(getItem(position).pubdate);
            //如果已经月的的 标记为灰色
            String ids = SPutil.getString(mActivity, GlobalConstant.HADREADIDS, "");
            if (ids.contains(getItem(position).id + "")) {
                holder.title.setTextColor(Color.GRAY);
                holder.pubDate.setTextColor(Color.GRAY);
            }
            String iconUrl = getItem(position).listimage.replace("http://10.0.2.2:8080/zhbj", GlobalConstant.SERVER);
            Log.e(TAG, "getView: iconUrl  " + iconUrl);
            mBitmapUtils.display(holder.icon, iconUrl);
            return convertView;
        }

        private class Holder {
            TextView title;
            TextView pubDate;
            ImageView icon;
        }
    }
}