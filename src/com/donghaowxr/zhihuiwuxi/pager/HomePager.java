package com.donghaowxr.zhihuiwuxi.pager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.menupager.BaseMenuDetailPager;
import com.donghaowxr.zhihuiwuxi.menupager.TalkDetailPager;

import android.app.Activity;
import android.view.View;

public class HomePager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mBaseMenuDetailPagers;

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText("聊天");
		btnMenu.setVisibility(View.INVISIBLE);
		mBaseMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mBaseMenuDetailPagers.add(new TalkDetailPager(mActivity));
		setCurrentPage(0);
	}

	private void setCurrentPage(int position) {
		BaseMenuDetailPager pager = mBaseMenuDetailPagers.get(position);
		View view = pager.mRootView;
		flContent.removeAllViews();
		flContent.addView(view);
		pager.initData();
	}
}
