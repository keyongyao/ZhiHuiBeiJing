package com.kk.zhbj.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基本的Fragment
 * Created by Administrator on 2016/10/8.
 */

public abstract class BaseFragment extends Fragment {
    Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setListener();
    }

    /**
     * 设置监视器
     */
    protected abstract void setListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * @return 初始化View
     */
    public abstract View initView();
}
