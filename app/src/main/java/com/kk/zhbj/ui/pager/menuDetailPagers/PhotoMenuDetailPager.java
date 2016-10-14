package com.kk.zhbj.ui.pager.menuDetailPagers;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.PhotoNewsBean;
import com.kk.zhbj.constant.GlobalConstant;
import com.kk.zhbj.ui.pager.BaseMenuDetailPager;
import com.kk.zhbj.utils.MyBitmapUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 注释：菜单组图详情页  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/9 - 8:51 <br>
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    private static final String TAG = "main";
    @ViewInject(R.id.iv_photonews_listItem_photo)
    ImageView mIvPhoto;
    @ViewInject(R.id.tv_photonews_listItem_title)
    TextView mTvTitle;
    @ViewInject(R.id.lv_phototNews_list)
    ListView mLv;
    @ViewInject(R.id.gv_photoNews_gird)
    GridView mGd;
    private String mJsonUrl = GlobalConstant.SERVER + "/photos/photos_1.json";
    private PhotoNewsBean mPhotoNewsBean;
    private ImageButton mIbList, mIbGrid;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mLv.setAdapter(new MyAdapter());
            mGd.setAdapter(new MyAdapter());
        }
    };

    public PhotoMenuDetailPager(Activity mActivity, ImageButton listButton, ImageButton girdButton) {
        super(mActivity);
        mIbList = listButton;
        mIbGrid = girdButton;
        mIbGrid.setVisibility(View.VISIBLE); // 默认打开列表视图，所以这个样式按钮需要显示
        initPhotosUrl();
        setlistener();
    }

    // 按钮的显示与隐藏  List 和 gird 的显示和隐藏
    private void setlistener() {
        mIbGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIbList.setVisibility(View.VISIBLE);
                mLv.setVisibility(View.GONE);
                mIbGrid.setVisibility(View.GONE);
                mGd.setVisibility(View.VISIBLE);

            }
        });
        mIbList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIbList.setVisibility(View.GONE);
                mLv.setVisibility(View.VISIBLE);
                mIbGrid.setVisibility(View.VISIBLE);
                mGd.setVisibility(View.GONE);
            }
        });
    }


    // 载入图片Url
    private void initPhotosUrl() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mJsonUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processPhotosData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + mJsonUrl);
            }
        });
    }

    /**
     * 处理 json
     */
    private void processPhotosData(String jsonStr) {
        Gson gson = new Gson();
        mPhotoNewsBean = gson.fromJson(jsonStr, PhotoNewsBean.class);
        Log.i(TAG, "PhotoMenuDetailPager.processPhotosData: " + mPhotoNewsBean.toString());
        mhandler.sendEmptyMessage(0);
    }

    @Override
    protected View initDetailContentUI() {
        View inflate = View.inflate(mActivity, R.layout.pager_photonew, null);
        ViewUtils.inject(this, inflate);
        return inflate;
    }

    /**
     * 图片列表适配器
     */
    private class MyAdapter extends BaseAdapter {
        BitmapUtils utils;
        MyBitmapUtil myBitmapUtil;

        public MyAdapter() {
            utils = new BitmapUtils(mActivity);
            myBitmapUtil = new MyBitmapUtil(mActivity);
        }

        @Override
        public int getCount() {
            return mPhotoNewsBean.data.news.size();
        }

        @Override
        public PhotoNewsBean.NewsBean getItem(int position) {
            return mPhotoNewsBean.data.news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.pager_photonews_list_item, null);
                holder = new Holder();
                holder.img = (ImageView) convertView.findViewById(R.id.iv_photonews_listItem_photo);
                holder.title = (TextView) convertView.findViewById(R.id.tv_photonews_listItem_title);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.title.setText(getItem(position).title);
            //  utils.display(holder.img,getItem(position).listimage.replace("http://10.0.2.2:8080/zhbj",GlobalConstant.SERVER));
            myBitmapUtil.display(holder.img, getItem(position).listimage.replace("http://10.0.2.2:8080/zhbj", GlobalConstant.SERVER));
            return convertView;
        }

        private class Holder {
            ImageView img;
            TextView title;
        }
    }


}
