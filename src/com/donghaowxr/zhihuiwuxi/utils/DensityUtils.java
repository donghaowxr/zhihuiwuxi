package com.donghaowxr.zhihuiwuxi.utils;

import android.content.Context;

public class DensityUtils {
	/**
	 * dp转px
	 * 
	 * @param dpi
	 * @param ctx
	 * @return
	 */
	public static int dp2px(float dpi, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;// 设备密度
		int px = (int) (dpi * density + 0.5f);// 加0.5实现四舍五入
		return px;
	}

	/**
	 * px转dp
	 * 
	 * @param px
	 * @param ctx
	 * @return
	 */
	public static float px2dp(int px, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
