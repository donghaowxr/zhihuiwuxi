package com.donghaowxr.zhihuiwuxi.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		TextView view = new TextView(mActivity);
		view.setText("聊天");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		tvTitle.setText("聊天");
		btnMenu.setVisibility(View.INVISIBLE);
	}
}
