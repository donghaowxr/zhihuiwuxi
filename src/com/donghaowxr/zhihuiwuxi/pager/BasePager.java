package com.donghaowxr.zhihuiwuxi.pager;

import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
	protected Activity mActivity;
	protected TextView tvTitle;
	protected ImageButton btnMenu;
	protected FrameLayout flContent;
	public View mRootView;
	protected ImageButton btnPhoto;

	public BasePager(Activity activity){
		mActivity = activity;
		mRootView = initView();
	}
	
	/**
	 * 初始化布局
	 * @return View对象
	 */
	protected View initView() {
		View view=View.inflate(mActivity, R.layout.page_base, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_Title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_Menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		
		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toggle();
			}
		});
		return view;
	}
	
	/**
	 * 切换侧边栏状态
	 */
	protected void toggle() {
		MainActivity activity=(MainActivity) mActivity;
		SlidingMenu slidingMenu=activity.getSlidingMenu();
		slidingMenu.toggle();
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
