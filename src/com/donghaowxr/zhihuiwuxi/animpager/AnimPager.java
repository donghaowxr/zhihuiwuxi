package com.donghaowxr.zhihuiwuxi.animpager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.AnimList;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.DesBean;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean.AnimData.TopAnim;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.utils.DensityUtils;
import com.donghaowxr.zhihuiwuxi.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.commons.logcollector.c;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class AnimPager extends BaseAnimPager {
	@ViewInject(R.id.vp_top_anim)
	private TopNewsViewPager vpTopAnim;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_anim)
	private ListView lvAnim;
	private ArrayList<AnimList> mAnimLists;
	private ArrayList<TopAnim> mTopAnims;

	public AnimPager(Activity activity, AnimBean animBean) {
		super(activity);
		mAnimLists = animBean.data.animlist;
		mTopAnims = animBean.data.topanim;
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_anim, null);
		ViewUtils.inject(this, view);
		View mHeadView=View.inflate(mActivity, R.layout.list_anim_header, null);
		ViewUtils.inject(this, mHeadView);
		lvAnim.addHeaderView(mHeadView);
		return view;
	}

	@Override
	public void initData() {
		lvAnim.setAdapter(new LvAnimAdapter());
		vpTopAnim.setAdapter(new AnimAdapter());
		mIndicator.setViewPager(vpTopAnim);
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
