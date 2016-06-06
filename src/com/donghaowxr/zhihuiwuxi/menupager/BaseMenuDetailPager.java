package com.donghaowxr.zhihuiwuxi.menupager;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	protected Activity mActivity;
	public View mRootView;

	public BaseMenuDetailPager(Activity activity){
		mActivity = activity;
		mRootView = initView();
	}
	
	/**
	 * 初始化布局
	 * @return 布局View对象
	 */
	protected abstract View initView();
	public void initData(){
		
	}
}
