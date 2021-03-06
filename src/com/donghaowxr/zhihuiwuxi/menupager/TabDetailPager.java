package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.NewsDetailActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean.TabData.TabNews;
import com.donghaowxr.zhihuiwuxi.domain.NewsTabBean.TabData.TopNews;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.utils.CacheUtils;
import com.donghaowxr.zhihuiwuxi.utils.PerfUtils;
import com.donghaowxr.zhihuiwuxi.view.PullToRefreshListView;
import com.donghaowxr.zhihuiwuxi.view.PullToRefreshListView.OnRefreshListener;
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
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class TabDetailPager extends BaseMenuDetailPager {

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager vpTopNews;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView lvList;
	private ChildrenArray mTabData;
	private String topNewsUrl;
	private NewsTabBean dataBean;
	private ArrayList<TopNews> topNews;
	private ArrayList<TabNews> mNewList;
	private String more;
	private Handler mHandler;

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
		View mHeaderView=View.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeaderView);
		lvList.addHeaderView(mHeaderView);
		lvList.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (!TextUtils.isEmpty(more)) {
					getMoreDataFromServer();
				}else {
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
					lvList.setRefreshComplete(false);
				}
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemPosition=position-lvList.getHeaderViewsCount();//当前位置需要去掉头布局个数
				String ids=PerfUtils.getString(mActivity, "read_ids", "");
				int currentId=mNewList.get(itemPosition).id;
				if (!ids.contains(String.valueOf(currentId))) {
					ids=ids+currentId+",";
					PerfUtils.setString(mActivity, "read_ids", ids);
				}
				TextView tvTitle=(TextView) view.findViewById(R.id.tv_news_title);
				tvTitle.setTextColor(Color.GRAY);
				
				String newsUrl=mNewList.get(itemPosition).url;
				newsUrl=newsUrl.substring(25, newsUrl.length());
				newsUrl=GlobalConfig.SERVER_URL+newsUrl;
				Intent intent=new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("newsUrl", newsUrl);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}
	
	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		String cacheJson=CacheUtils.getCache(topNewsUrl, mActivity,"");
		if (!TextUtils.isEmpty(cacheJson)) {
			processData(cacheJson,false);
		}
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
				CacheUtils.setCache(topNewsUrl, result, mActivity);
				lvList.setRefreshComplete(true);
				processData(result,false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				System.out.println(msg);
				Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
				lvList.setRefreshComplete(false);
			}
		});
	}
	
	/**
	 * 请求网络加载更多
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConfig.SERVER_URL+more, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				processData(result,true);
				lvList.setRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				System.out.println(msg);
				Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
				lvList.setRefreshComplete(false);
			}
		});
	}

	/**
	 * 解析服务器返回json数据
	 * @param result json数据
	 */
	protected void processData(String result,boolean isMore) {
		Gson gson=new Gson();
		dataBean = gson.fromJson(result, NewsTabBean.class);
		more = dataBean.data.more;
		if (!isMore) {
			topNews = dataBean.data.topnews;
			if (topNews!=null) {
				vpTopNews.setAdapter(new TopNewAdapter());
				mIndicator.setViewPager(vpTopNews);
				mIndicator.setSnap(true);
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						tvTitle.setText(topNews.get(position).title);
					}
					@Override
					public void onPageScrolled(int position, float positionOffset,
							int positionOffsetPixels) {
					}
					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});
				tvTitle.setText(topNews.get(0).title);
				mIndicator.onPageSelected(0);//默认页面初始化是第一个指示器被选中
			}
			mNewList = dataBean.data.news;
			if (mNewList!=null) {
				NewsAdapter mNewAdapter=new NewsAdapter();
				lvList.setAdapter(mNewAdapter);
			}
			if (mHandler==null) {
				mHandler=new Handler(){
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						int currentPosition=vpTopNews.getCurrentItem();
						currentPosition++;
						if (currentPosition>topNews.size()-1) {
							currentPosition=0;
						}
						vpTopNews.setCurrentItem(currentPosition);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);
				vpTopNews.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							//用户在viewPager上手指按下后清除handler所有消息
							mHandler.removeCallbacksAndMessages(null);
							break;
						case MotionEvent.ACTION_UP:
							//当手指离开viewpager时重新发送轮询消息
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;
						case MotionEvent.ACTION_CANCEL:
							//当事件被取消时，重新发送轮询消息
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;
						}
						return false;
					}
				});
			}
		}else {
			ArrayList<TabNews>moreNews=dataBean.data.news;
			mNewList.addAll(moreNews);
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
//			view.setBackgroundResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);
			String imageUrl=topNews.get(position).topimage;
			imageUrl=imageUrl.substring(25, imageUrl.length());
			imageUrl=GlobalConfig.SERVER_URL+imageUrl;
			//下载图片，设置给ImageView
			//使用BitmapUtils
			bitmapUtils.display(view, imageUrl);
			bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);//BitmapUtils设置默认图片
			
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	class NewsAdapter extends BaseAdapter{
		private BitmapUtils bitmapUtils;

		public NewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}
		@Override
		public int getCount() {
			return mNewList.size();
		}

		@Override
		public TabNews getItem(int position) {
			return mNewList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null) {
				convertView=View.inflate(mActivity, R.layout.list_item_news, null);
				holder = new ViewHolder();
				holder.ivIcon=(ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_news_title);
				holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}else {
				holder=(ViewHolder) convertView.getTag();
			}
			TabNews news=getItem(position);
			String ids=PerfUtils.getString(mActivity, "read_ids", "");
			if (ids.contains(String.valueOf(news.id))) {
				holder.tvTitle.setTextColor(Color.GRAY);
			}else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);
			
			String newsUrl=news.listimage;
			newsUrl=newsUrl.substring(25, newsUrl.length());
			newsUrl=GlobalConfig.SERVER_URL+newsUrl;
			bitmapUtils.display(holder.ivIcon, newsUrl);
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
