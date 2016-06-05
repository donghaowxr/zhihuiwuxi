package com.donghaowxr.zhihuiwuxi.pager;

import com.donghaowxr.zhihuiwuxi.R;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
	protected Activity mActivity;
	protected TextView tvTitle;
	protected ImageButton btnMenu;
	protected FrameLayout flContent;
	public View mRootView;

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
		return view;
	}
	
	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
