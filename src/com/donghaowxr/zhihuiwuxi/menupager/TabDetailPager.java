package com.donghaowxr.zhihuiwuxi.menupager;

import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TabDetailPager extends BaseMenuDetailPager {

	private TextView view;
	private ChildrenArray mTabData;

	public TabDetailPager(Activity activity, ChildrenArray childrenArray) {
		super(activity);
		mTabData = childrenArray;
	}

	@Override
	protected View initView() {
		view = new TextView(mActivity);
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}
	
	@Override
	public void initData() {
		view.setText(mTabData.title);
	}

}
