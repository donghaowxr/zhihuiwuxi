package com.donghaowxr.zhihuiwuxi.mappager;

import com.donghaowxr.zhihuiwuxi.R;

import android.app.Activity;
import android.view.View;

public class DaoHangPager extends BaseMapPager {

	public DaoHangPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_daohang, null);
		return view;
	}

}