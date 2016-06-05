package com.donghaowxr.zhihuiwuxi.pager;

import com.donghaowxr.zhihuiwuxi.domain.NewsMenu;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

public class NewsPager extends BasePager {

	public NewsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		TextView view=new TextView(mActivity);
		view.setText("新闻中心");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		tvTitle.setText("新闻中心");
		
		String cacheJson=CacheUtils.getCache(GlobalConfig.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cacheJson)) {
			System.out.println("从缓存中读取数据");
			processData(cacheJson);
		}
		//请求服务器
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConfig.CATEGORY_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				processData(result);
				CacheUtils.setCache(GlobalConfig.CATEGORY_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				System.out.println(msg);
			}
		});
	}

	/**
	 * 解析json数据
	 * @param json json字符串
	 */
	protected void processData(String json) {
		Gson gson=new Gson();
		NewsMenu data=gson.fromJson(json, NewsMenu.class);
		System.out.println("解析结果:"+data.retcode);
	}
}
