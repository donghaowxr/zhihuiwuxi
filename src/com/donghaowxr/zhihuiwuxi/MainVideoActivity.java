package com.donghaowxr.zhihuiwuxi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.donghaowxr.zhihuiwuxi.fragment.ContentFragment;
import com.donghaowxr.zhihuiwuxi.fragment.LeftMenuFragment;
import com.donghaowxr.zhihuiwuxi.fragment.ScreenFragment;
import com.donghaowxr.zhihuiwuxi.fragment.VideoFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainVideoActivity extends SlidingFragmentActivity {
	private String TAG_ScreenMenu = "TAG_ScreenMenu";
	private String TAG_Video = "TAG_Video";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_video);
		setBehindContentView(R.layout.screen_menu);// 设置侧边栏布局
		SlidingMenu slidingMenu = getSlidingMenu();
		// slidingMenu.setSecondaryMenu(R.layout.left_menu);//设置第二个侧边栏
		slidingMenu.setMode(SlidingMenu.RIGHT);// 设置侧边栏的位置
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int width = screenWidth * 270 / 320;
		slidingMenu.setBehindOffset(width);// 设置屏幕预留的宽度
		initFragment();
	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();// 获取Fragment管理器
		FragmentTransaction transaction = fm.beginTransaction();// 开始事务
		transaction.replace(R.id.fl_screen_menu, new ScreenFragment(),
				TAG_ScreenMenu);// 用Fragment替换原有布局
		transaction.replace(R.id.fl_main_video, new VideoFragment(),
				TAG_Video);
		transaction.commit();// 提交事务
	}
	
	public String getVideoTitle(){
		return getIntent().getExtras().getString("title", "");
	}
	
	/**
	 * 获取主页面fragment对象
	 * 
	 * @return videofragment对象
	 */
	public VideoFragment getVideoFragment() {
		FragmentManager fm = getSupportFragmentManager();
		VideoFragment fragment=(VideoFragment) fm.findFragmentByTag(TAG_Video);
		return fragment;
	}
}
