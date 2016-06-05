package com.donghaowxr.zhihuiwuxi.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class GovPager extends BasePager {

	public GovPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		TextView view=new TextView(mActivity);
		view.setText("政务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		tvTitle.setText("政务");
	}
}
