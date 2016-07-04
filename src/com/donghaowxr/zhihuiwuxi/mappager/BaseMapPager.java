package com.donghaowxr.zhihuiwuxi.mappager;

import android.app.Activity;
import android.view.View;

public abstract class BaseMapPager {
	protected Activity mActivity;
	public View mRootView;

	public BaseMapPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	public abstract View initView();

	public void initData() {

	}
}
