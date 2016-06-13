package com.donghaowxr.zhihuiwuxi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PerfUtils {
	/**
	 * 获取boolean类型的sp文件中的值
	 * @param context 上下文
	 * @param key 需要的key
	 * @param defValue 默认值
	 * @return sp中boolean对应的值
	 */
	@SuppressWarnings("static-access")
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	/**
	 * 设置sp文件中boolean类型的值
	 * @param context 上下文
	 * @param key 对应的key
	 * @param value 对应的值
	 */
	@SuppressWarnings("static-access")
	public static void setBoolean(Context context,String key,boolean value){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 获取String类型的值
	 * @param context 上下文
	 * @param key 对应的key
	 * @return 对应的值
	 */
	@SuppressWarnings("static-access")
	public static String getString(Context context,String key,String defValue){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	/**
	 * 设置sp文件中String的值
	 * @param context 上下文
	 * @param key 对应的key
	 * @param value 对应的值
	 */
	@SuppressWarnings("static-access")
	public static void setString(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
