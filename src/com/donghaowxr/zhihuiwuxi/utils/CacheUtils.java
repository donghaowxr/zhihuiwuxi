package com.donghaowxr.zhihuiwuxi.utils;

import android.content.Context;

public class CacheUtils {
	/**
	 * 设置缓存存入sp文件中
	 * @param url 链接地址
	 * @param json 链接返回的json数据
	 * @param context 上下文
	 */
	public static void setCache(String url,String json,Context context){
		PerfUtils.setString(context, url, json);
	}
	
	/**
	 * 获取缓存数据
	 * @param url 链接地址
	 * @param context 上下文
	 * @return 链接对应的json数据
	 */
	public static String getCache(String url,Context context){
		return PerfUtils.getString(context, url);
	}
}
