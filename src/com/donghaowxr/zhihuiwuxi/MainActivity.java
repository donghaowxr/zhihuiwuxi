package com.donghaowxr.zhihuiwuxi;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.os.Bundle;

public class MainActivity extends SlidingFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);//设置侧边栏布局
		SlidingMenu slidingMenu=getSlidingMenu();
//		slidingMenu.setSecondaryMenu(R.layout.left_menu);//设置第二个侧边栏
		slidingMenu.setMode(SlidingMenu.LEFT);//设置侧边栏的位置
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
		slidingMenu.setBehindOffset(200);//设置屏幕预留的宽度
	}
}
