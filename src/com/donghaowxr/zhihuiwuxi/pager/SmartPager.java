package com.donghaowxr.zhihuiwuxi.pager;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.animpager.AnimPager;
import com.donghaowxr.zhihuiwuxi.animpager.BaseAnimPager;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SmartPager extends BasePager {
	private ArrayList<BasePager> mBaseAnimPagers;

	public SmartPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		TextView view = new TextView(mActivity);
		view.setText("弹幕视频");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		flContent.addView(view);
		btnMenu.setVisibility(View.INVISIBLE);

		String cacheData = CacheUtils.getCache(GlobalConfig.ANIM_URL,
				mActivity, "");
		if (!TextUtils.isEmpty(cacheData)) {
			processData(cacheData);
		}
		getDataFromServer();
	}

	/**
	 * 从服务端获取json数据
	 */
	private void getDataFromServer() {
		HttpUtils mHttpUtils = new HttpUtils();
		mHttpUtils.send(HttpMethod.GET, GlobalConfig.ANIM_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						CacheUtils.setCache(GlobalConfig.ANIM_URL, result,
								mActivity);
						processData(result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						System.out.println(msg);
						Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	/**
	 * 解析json数据
	 * 
	 * @param result
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		AnimBean mAnimBean = gson.fromJson(result, AnimBean.class);
		tvTitle.setText(mAnimBean.data.title);
		mBaseAnimPagers = new ArrayList<BasePager>();
		mBaseAnimPagers.add(new BaseAnimPager(mActivity, mAnimBean));
		// mBaseAnimPagers.add(new AnimPager(mActivity,mAnimBean));
		flContent.removeAllViews();
		setCurrentPage(0);
	}

	public BaseAnimPager getBaseAnimPager() {
		if (mBaseAnimPagers == null) {
			return null;
		} else {
			BaseAnimPager mBaseAnimPager = (BaseAnimPager) mBaseAnimPagers
					.get(0);
			return mBaseAnimPager;
		}
	}

	private void setCurrentPage(int position) {
		BasePager mBaseAnimPager = mBaseAnimPagers.get(position);
		View view = mBaseAnimPager.mRootView;
		flContent.removeAllViews();
		flContent.addView(view);
		mBaseAnimPager.initData();
	}
}
