package com.donghaowxr.zhihuiwuxi.mappager;

import com.donghaowxr.zhihuiwuxi.R;

import android.app.Activity;
import android.view.View;

public class FuJinPager extends BaseMapPager {

	public FuJinPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_fujin, null);
		return view;
	}

}
