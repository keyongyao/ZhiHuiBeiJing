package com.kk.zhbj.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.kk.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 注释：左侧的菜单的Fragment                                      <br>
 * 作者： kk                                   <br>
 * QQ  :                                       <br>
 * 创建时间：2016/10/8 - 15:07                  <br>
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_fragmentMenu_list)
    private ListView mListView;

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        View inflate = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
        ViewUtils.inject(this, inflate);  // 注入事件  java annotation
        return inflate;
    }
}
