package com.donghaowxr.zhihuiwuxi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();//获取fragment所在activity的对象
	}
	
	/**
	 * 初始化Fragment布局时调用
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return initView();
	}
	/**
	 * 初始化fragment
	 * @return View对象
	 */
	public abstract View initView();
	
	/**
	 * 当fragment所在activity当onCreate之行完成之后调用
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	
}
