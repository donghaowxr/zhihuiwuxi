package com.donghaowxr.zhihuiwuxi;

import com.donghaowxr.zhihuiwuxi.animpager.BaseAnimPager;
import com.donghaowxr.zhihuiwuxi.fragment.ContentFragment;
import com.donghaowxr.zhihuiwuxi.fragment.LeftMenuFragment;
import com.donghaowxr.zhihuiwuxi.pager.SmartPager;
import com.donghaowxr.zhihuiwuxi.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {
	private String TAG_LeftMenu = "TAG_LeftMenu";
	private String TAG_Content = "TAG_Content";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);// 设置侧边栏布局
		SlidingMenu slidingMenu = getSlidingMenu();
		// slidingMenu.setSecondaryMenu(R.layout.left_menu);//设置第二个侧边栏
		slidingMenu.setMode(SlidingMenu.LEFT);// 设置侧边栏的位置
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int width = screenWidth * 200 / 320;
		slidingMenu.setBehindOffset(width);// 设置屏幕预留的宽度
		initFragment();
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();// 获取Fragment管理器
		FragmentTransaction transaction = fm.beginTransaction();// 开始事务
		transaction.replace(R.id.left_menu, new LeftMenuFragment(),
				TAG_LeftMenu);// 用Fragment替换原有布局
		transaction.replace(R.id.main_content, new ContentFragment(),
				TAG_Content);
		transaction.commit();// 提交事务
	}

	/**
	 * 获取侧边栏fragment对象
	 * 
	 * @return leftmenufragment对象
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm
				.findFragmentByTag(TAG_LeftMenu);
		return fragment;
	}

	/**
	 * 获取主页面fragment对象
	 * 
	 * @return contentfragment对象
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(TAG_Content);
		return fragment;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ContentFragment contentFragment = getContentFragment();
			SmartPager smartPager = contentFragment.getSmartPager();
			BaseAnimPager baseAnimPager = smartPager.getBaseAnimPager();
			NoScrollViewPager vpPager = null;
			if (baseAnimPager != null) {
				vpPager = baseAnimPager.getCurrentPosition();
			}
			if (vpPager != null) {
				if (vpPager.getCurrentItem() != 0) {
					vpPager.setCurrentItem(vpPager.getCurrentItem() - 1);
					return true;
				} else {
					return super.onKeyDown(keyCode, event);
				}
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
