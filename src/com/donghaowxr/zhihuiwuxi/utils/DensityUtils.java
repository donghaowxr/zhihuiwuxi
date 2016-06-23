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
		float density = ctx.getResources().getDisplayMetrics().density;// 屏幕密度
		float dp = px / density;
		return dp;
	}

	/**
	 * px转sp
	 * 
	 * @param px
	 * @param ctx
	 * @return
	 */
	public static float px2sp(int px, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().scaledDensity;// 文字缩放系数
		float sp = px / density;
		return sp;
	}

	/**
	 * sp转px
	 * 
	 * @param sp
	 * @param ctx
	 * @return
	 */
	public static float sp2px(float sp, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().scaledDensity;// 文字缩放系数
		return sp * density;
	}
}
