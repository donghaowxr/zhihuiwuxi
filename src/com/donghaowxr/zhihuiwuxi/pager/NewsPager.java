package com.donghaowxr.zhihuiwuxi.pager;

import com.donghaowxr.zhihuiwuxi.domain.NewsMenu;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.graphics.Color;
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
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConfig.CATEGORY_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				processData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				System.out.println(msg);
			}
		});
	}

	protected void processData(String json) {
		Gson gson=new Gson();
		NewsMenu data=gson.fromJson(json, NewsMenu.class);
		System.out.println("解析结果:"+data.retcode);
	}
}
