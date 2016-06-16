package com.donghaowxr.zhihuiwuxi.pager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu;
import com.donghaowxr.zhihuiwuxi.fragment.LeftMenuFragment;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.menupager.BaseMenuDetailPager;
import com.donghaowxr.zhihuiwuxi.menupager.InteractMenuDetailPager;
import com.donghaowxr.zhihuiwuxi.menupager.NewsMenuDetailPager;
import com.donghaowxr.zhihuiwuxi.menupager.PhotosMenuDetailPager;
import com.donghaowxr.zhihuiwuxi.menupager.TopicMenuDetailPager;
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

public class NewsPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
	private NewsMenu data;

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
		
		String cacheJson=CacheUtils.getCache(GlobalConfig.CATEGORY_URL, mActivity,"");
		if (!TextUtils.isEmpty(cacheJson)) {
			System.out.println("从缓存中读取数据");
			processData(cacheJson);
		}
		//请求服务器
		getDataFromServer();
	}

	/**
	 * 从服务器获取数据
	 */
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
				Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 解析json数据
	 * @param json json字符串
	 */
	protected void processData(String json) {
		Gson gson=new Gson();
		data = gson.fromJson(json, NewsMenu.class);
		MainActivity activity=(MainActivity) mActivity;//获取activity对象
		LeftMenuFragment leftMenuFragment=activity.getLeftMenuFragment();//获取leftmenufragment对象
		leftMenuFragment.setMenuData(data.data);//设置侧边栏数据
		leftMenuFragment.mCurentPos=0;//将当权菜单选中的位置置为0
		
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,data.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
		setCurrentDetailPager(0);
	}
	
	/**
	 * 设置新闻中心当前菜单页
	 * @param position 当前选中菜单的位置
	 */
	public void setCurrentDetailPager(int position){
		BaseMenuDetailPager pager=(BaseMenuDetailPager) mMenuDetailPagers.get(position);
		View view=pager.mRootView;
		flContent.removeAllViews();
		flContent.addView(view);
		pager.initData();
		tvTitle.setText(data.data.get(position).title);
		
	}
}
