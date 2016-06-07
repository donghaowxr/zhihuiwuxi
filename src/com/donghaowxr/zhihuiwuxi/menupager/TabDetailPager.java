package com.donghaowxr.zhihuiwuxi.menupager;

import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class TabDetailPager extends BaseMenuDetailPager {

	@ViewInject(R.id.vp_top_news)
	private ViewPager vpTopNews;
	private ChildrenArray mTabData;
	private String topNewsUrl;
	private NewsTabBean dataBean;

	public TabDetailPager(Activity activity, ChildrenArray childrenArray) {
		super(activity);
		mTabData = childrenArray;
		topNewsUrl = GlobalConfig.SERVER_URL+childrenArray.url;
	}

	/**
	 * 初始化布局
	 */
	@Override
	protected View initView() {
		View view=View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		getDataFromServer();
	}
	
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, topNewsUrl, new RequestCallBack<String>() {

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

	/**
	 * 解析服务器返回json数据
	 * @param result json数据
	 */
	protected void processData(String result) {
		Gson gson=new Gson();
		dataBean = gson.fromJson(result, NewsTabBean.class);
	}

	class TopNewAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

}
