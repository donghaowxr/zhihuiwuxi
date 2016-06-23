package com.donghaowxr.zhihuiwuxi.animpager;

import android.app.Activity;
import android.view.View;

public abstract class BaseAnimPager {
	protected Activity mActivity;
	public View mRootView;

	public BaseAnimPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	/**
	 * 初始化布局
	 * 
	 * @return 布局View对象
	 */
	protected abstract View initView();

	public void initData() {

	}
}
