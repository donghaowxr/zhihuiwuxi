package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean.TabData.TopNews;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class TabDetailPager extends BaseMenuDetailPager {

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager vpTopNews;
	private ChildrenArray mTabData;
	private String topNewsUrl;
	private NewsTabBean dataBean;
	private ArrayList<TopNews> topNews;

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
		topNews = dataBean.data.topnews;
		if (topNews!=null) {
			vpTopNews.setAdapter(new TopNewAdapter());
		}
	}

	class TopNewAdapter extends PagerAdapter{
		private BitmapUtils bitmapUtils;
		public TopNewAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
		}
		@Override
		public int getCount() {
			return topNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view=new ImageView(mActivity);
			view.setBackgroundResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);
			String imageUrl=topNews.get(position).topimage;
			imageUrl=imageUrl.substring(25, imageUrl.length());
			imageUrl=GlobalConfig.SERVER_URL+imageUrl;
			//下载图片，设置给ImageView
			//使用BitmapUtils
			bitmapUtils.display(view, imageUrl);
			
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

}
