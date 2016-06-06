package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsMenuDetailPager extends BaseMenuDetailPager {

	private ViewPager mViewPager;
	private ArrayList<ChildrenArray> mTabData;
	private ArrayList<TabDetailPager>mPagers;

	public NewsMenuDetailPager(Activity activity, ArrayList<ChildrenArray> children) {
		super(activity);
		mTabData = children;
	}

	@Override
	protected View initView() {
		View view=View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
		return view;
	}
	
	@Override
	public void initData() {
		mPagers=new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager tabDetailPager=new TabDetailPager(mActivity,mTabData.get(i));
			mPagers.add(tabDetailPager);
		}
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
	}
	
	private class NewsMenuDetailAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager=mPagers.get(position);
			View view=pager.mRootView;
			container.addView(view);
			pager.initData();
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

}
