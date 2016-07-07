package com.donghaowxr.zhihuiwuxi.pager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.domain.MapMenu;
import com.donghaowxr.zhihuiwuxi.fragment.LeftMenuFragment;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.mappager.BaseMap;
import com.donghaowxr.zhihuiwuxi.mappager.BaseMapPager;
import com.donghaowxr.zhihuiwuxi.mappager.DingWeiPager;
import com.donghaowxr.zhihuiwuxi.mappager.FuJinPager;
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

public class GovPager extends BasePager {

	private ArrayList<BaseMapPager> mBaseMapPagers;

	public GovPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText("地图");

		String cache = CacheUtils.getCache(GlobalConfig.MAP_URL, mActivity, "");
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}else {
			getDataFromServer();
		}
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils mHttpUtils = new HttpUtils();
		mHttpUtils.send(HttpMethod.GET, GlobalConfig.MAP_URL,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						processData(result);
						CacheUtils.setCache(GlobalConfig.MAP_URL, result,
								mActivity);
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
		MapMenu mMapMenu = gson.fromJson(result, MapMenu.class);
		MainActivity activity = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = activity.getLeftMenuFragment();
		leftMenuFragment.setMapMenu(mMapMenu.data);
		leftMenuFragment.mCurentPos = 0;

		mBaseMapPagers = new ArrayList<BaseMapPager>();
		mBaseMapPagers.add(new DingWeiPager(mActivity));
		mBaseMapPagers.add(new FuJinPager(mActivity));
		setCurrentPager(0);
	}

	public void setCurrentPager(int position) {
		BaseMapPager mBaseMapPager = mBaseMapPagers.get(position);
		View view = mBaseMapPager.mRootView;
		flContent.removeAllViews();
		flContent.addView(view);
		mBaseMapPager.initData();
	}
	
	public DingWeiPager getDingWeiPager(){
		if (mBaseMapPagers!=null) {
			return (DingWeiPager) mBaseMapPagers.get(0);
		}else {
			return null;
		}
	}
	
	public FuJinPager getFuJinPager(){
		if (mBaseMapPagers!=null) {
			return (FuJinPager) mBaseMapPagers.get(1);
		}else {
			return null;
		}
	}
}
