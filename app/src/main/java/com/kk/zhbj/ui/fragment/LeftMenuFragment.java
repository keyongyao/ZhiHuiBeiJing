package com.kk.zhbj.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kk.zhbj.R;
import com.kk.zhbj.bean.NewsMenuBean;
import com.kk.zhbj.ui.MainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 注释：左侧的菜单的Fragment                                      <br>
 * 作者： kk                                   <br>
 * QQ  :                                       <br>
 * 创建时间：2016/10/8 - 15:07                  <br>
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_fragmentMenu_list)
    private ListView mListView;
    private ArrayList<NewsMenuBean.NewsData> mMenudata;
    private int mLastestClickitem;
    private MenuListAdapter mMenuListAdapter;
    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        View inflate = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
        // mListView= (ListView) inflate.findViewById(R.id.lv_fragmentMenu_list);
        ViewUtils.inject(this, inflate);  // 注入事件  java annotation
        return inflate;
    }

    // 设置菜单数据和适配器
    public void setmMenudata(ArrayList<NewsMenuBean.NewsData> menudata) {
        this.mMenudata = menudata;
        mMenuListAdapter = new MenuListAdapter();
        mListView.setAdapter(mMenuListAdapter);
        // listItem 条目 点击事件  监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLastestClickitem = position;
                Toast.makeText(mActivity, "" + mMenudata.get(position).title + " 点击了", Toast.LENGTH_SHORT).show();
                mMenuListAdapter.notifyDataSetChanged();
                menuToggle();
                setCurrentMenuDetailPager(position);
            }
        });
    }
    // 设置 contentFragment 的 framelayout的内容

    /**
     * @param position 告诉NewsCenterPager要更新详细页面
     */
    private void setCurrentMenuDetailPager(int position) {

        MainActivity ma = (MainActivity) this.mActivity;
        ContentFragment contentFragment = ma.getmContentFragment();
        contentFragment.getNewsCenterPager().setCurrentMenuDetailPager(position);
    }

    // 左侧菜单 开关
    private void menuToggle() {
        MainActivity ma = (MainActivity) mActivity;
        SlidingMenu slidingMenu = ma.getSlidingMenu();
        slidingMenu.toggle();
    }

    class MenuListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenudata.size();
        }

        @Override
        public NewsMenuBean.NewsData getItem(int position) {
            return mMenudata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = View.inflate(mActivity, R.layout.leftmenu_list_item, null);
            TextView textView = (TextView) itemView.findViewById(R.id.tv_leftmenu_listItem_title);
            textView.setText(getItem(position).title);
            if (position == mLastestClickitem) {
                textView.setEnabled(true);
            } else {
                textView.setEnabled(false);
            }
            return itemView;
        }
    }
}
