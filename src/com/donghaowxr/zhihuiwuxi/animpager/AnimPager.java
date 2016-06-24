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

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class AnimPager extends BasePager {
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

	public AnimPager(Activity activity, AnimBean animBean) {
		super(activity);
		mAnimLists = animBean.data.animlist;
		mTopAnims = animBean.data.topanim;
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

			}
		});
		return view;
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
						processData(result);
						lvAnim.setRefreshComplete(true);
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
		mTopAnims=mAnimBean.data.topanim;
		mAnimLists=mAnimBean.data.animlist;
		mLvAnimAdapter.notifyDataSetChanged();
		mAnimAdapter.notifyDataSetChanged();
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
