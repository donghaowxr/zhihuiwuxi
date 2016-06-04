package com.donghaowxr.zhihuiwuxi.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class SmartPager extends BasePager {

	public SmartPager(Activity activity) {
		super(activity);
	}

	@Override
	protected void initData() {
		TextView view=new TextView(mActivity);
		view.setText("智慧服务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
	}
}
