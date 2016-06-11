package com.donghaowxr.zhihuiwuxi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private float startX;
	private float startY;
	private float moveX;
	private float moveY;
	private float dx;
	private float dy;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}
	
	/**
	 * 上下滑动时需要父控件拦截事件
	 * 向右滑动并且当前是第一个页面，需要拦截
	 * 向左滑动并且当前是最后一个页面，需要拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = ev.getX();
			moveY = ev.getY();
			dx = moveX-startX;
			dy = moveY-startY;
			if (Math.abs(dx)>Math.abs(dy)) {
				int currentPage=getCurrentItem();
				if (dx>0) {
					if (currentPage==0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}else {
					int pageSize=getAdapter().getCount();
					if (currentPage==pageSize-1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
