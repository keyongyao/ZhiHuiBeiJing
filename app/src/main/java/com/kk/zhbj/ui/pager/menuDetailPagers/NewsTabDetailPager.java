package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.NewsMenuBean;
import com.kk.zhbj.bean.NewsTabBean;
import com.kk.zhbj.constant.GlobalConstant;
import com.kk.zhbj.ui.pager.BaseMenuDetailPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

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
        // 添加头部ViewPager视图
        mListView.addHeaderView(mListHeader);
        return inflate;
    }

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

    // 处理下载下来的json 数据
    private void processInitData(String strJson) {
        Gson gson = new Gson();

        mNewsTabBean = gson.fromJson(strJson, NewsTabBean.class);
        Log.i(TAG, "NewsTabDetailPager.processInitData: " + mNewsTabBean.toString());
        Log.e(TAG, "processInitData: =================================================");

        // 获取数据后 设置适配器
        mViewPager.setAdapter(new TopicNewAdapter());
        mListView.setAdapter(new NewsAdapter());

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