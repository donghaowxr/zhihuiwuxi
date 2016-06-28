package com.donghaowxr.zhihuiwuxi.animpager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.AnimList;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.DesBean;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.TopAnim;
import com.donghaowxr.zhihuiwuxi.fragment.ContentFragment;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.pager.BasePager;
import com.donghaowxr.zhihuiwuxi.utils.CacheUtils;
import com.donghaowxr.zhihuiwuxi.utils.DensityUtils;
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
import com.mob.commons.logcollector.c;
import com.viewpagerindicator.CirclePageIndicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class AnimPager extends BaseAnimPager {
	@ViewInject(R.id.vp_top_anim)
	private TopNewsViewPager vpTopAnim;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_anim)
	private PullToRefreshListView lvAnim;
	private ArrayList<AnimList> mAnimLists;
	private ArrayList<TopAnim> mTopAnims;
	private Handler mHandler;
	private LvAnimAdapter mLvAnimAdapter;
	private AnimAdapter mAnimAdapter;
	private String more;

	public AnimPager(Activity activity, AnimBean animBean) {
		super(activity);
		mAnimLists = animBean.data.animlist;
		mTopAnims = animBean.data.topanim;
		more = animBean.data.more;
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_anim, null);
		ViewUtils.inject(this, view);
		View mHeadView = View.inflate(mActivity, R.layout.list_anim_header,
				null);
		ViewUtils.inject(this, mHeadView);
		lvAnim.addHeaderView(mHeadView);
		lvAnim.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (!TextUtils.isEmpty(more)) {
					getMoreFromServer();
				} else {
					Toast.makeText(mActivity, "没有更多数据了...", Toast.LENGTH_SHORT)
							.show();
					lvAnim.setRefreshComplete(false);
				}
			}
		});
		return view;
	}

	/**
	 * 从服务端获取更多数据
	 */
	protected void getMoreFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConfig.SERVER_URL + more,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						processData(result, true);
						lvAnim.setRefreshComplete(true);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(msg);
						Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT)
								.show();
						lvAnim.setRefreshComplete(false);
					}
				});
	}

	/**
	 * 从服务端获取数据
	 */
	protected void getDataFromServer() {
		HttpUtils mHttpUtils = new HttpUtils();
		mHttpUtils.send(HttpMethod.GET, GlobalConfig.ANIM_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						CacheUtils.setCache(GlobalConfig.ANIM_URL, result,
								mActivity);
						processData(result, false);
						lvAnim.setRefreshComplete(true);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						System.out.println(msg);
						Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT)
								.show();
						lvAnim.setRefreshComplete(false);
					}
				});
	}

	/**
	 * 解析json数据
	 * 
	 * @param result
	 */
	protected void processData(String result, boolean isMore) {
		Gson gson = new Gson();
		AnimBean mAnimBean = gson.fromJson(result, AnimBean.class);
		more = mAnimBean.data.more;
		if (!isMore) {
			mTopAnims = mAnimBean.data.topanim;
			mAnimLists = mAnimBean.data.animlist;
		} else {
			ArrayList<AnimList> moreAnimLists = mAnimBean.data.animlist;
			mAnimLists.addAll(moreAnimLists);
		}
	}

	@Override
	public void initData() {
		mLvAnimAdapter = new LvAnimAdapter();
		lvAnim.setAdapter(mLvAnimAdapter);
		mAnimAdapter = new AnimAdapter();
		vpTopAnim.setAdapter(mAnimAdapter);
		mIndicator.setViewPager(vpTopAnim);
		if (mHandler == null) {
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					int currentPosition = vpTopAnim.getCurrentItem();
					currentPosition++;
					if (currentPosition > mTopAnims.size() - 1) {
						currentPosition = 0;
					}
					vpTopAnim.setCurrentItem(currentPosition);
					mHandler.sendEmptyMessageDelayed(0, 3000);
				}
			};
			mHandler.sendEmptyMessageDelayed(0, 3000);
			vpTopAnim.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mHandler.removeCallbacksAndMessages(null);
						break;
					case MotionEvent.ACTION_UP:
						mHandler.sendEmptyMessageDelayed(0, 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						mHandler.sendEmptyMessageDelayed(0, 3000);
						break;
					}
					return false;
				}
			});
		}
	}

	public class AnimAdapter extends PagerAdapter {

		private BitmapUtils bitmapUtils;

		public AnimAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopAnims.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			String animUrl = GlobalConfig.SERVER_URL
					+ mTopAnims.get(position).topimage;
			bitmapUtils.display(view, animUrl);
			container.addView(view);
			return view;
		}
	}

	public class LvAnimAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public LvAnimAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mAnimLists.size();
		}

		@Override
		public AnimList getItem(int position) {
			return mAnimLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_anim_item,
						null);
				holder.ivAnimPic = (ImageView) convertView
						.findViewById(R.id.iv_anim_pic);
				holder.tvAnimTitle = (TextView) convertView
						.findViewById(R.id.tv_anim_title);
				holder.llAnimDes = (LinearLayout) convertView
						.findViewById(R.id.ll_anim_des);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvAnimTitle.setText(mAnimLists.get(position).title);
			String url = GlobalConfig.SERVER_URL
					+ mAnimLists.get(position).listimage;
			mBitmapUtils.display(holder.ivAnimPic, url);
			ArrayList<DesBean> mDesBeans = mAnimLists.get(position).description;
			holder.llAnimDes.removeAllViews();
			for (DesBean desBean : mDesBeans) {
				LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				TextView textView = new TextView(mActivity);
				textView.setTextSize(DensityUtils.sp2px(6, mActivity));
				textView.setTextColor(mActivity.getResources().getColor(
						R.color.list_anim_text));
				textView.setText(desBean.item);
				textView.setLayoutParams(mLayoutParams);
				holder.llAnimDes.addView(textView);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivAnimPic;
		public TextView tvAnimTitle;
		public LinearLayout llAnimDes;
	}
}
