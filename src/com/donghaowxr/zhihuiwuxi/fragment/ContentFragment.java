package com.donghaowxr.zhihuiwuxi.fragment;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.pager.BasePager;
import com.donghaowxr.zhihuiwuxi.pager.GovPager;
import com.donghaowxr.zhihuiwuxi.pager.HomePager;
import com.donghaowxr.zhihuiwuxi.pager.NewsPager;
import com.donghaowxr.zhihuiwuxi.pager.SettingPager;
import com.donghaowxr.zhihuiwuxi.pager.SmartPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ContentFragment extends BaseFragment {

	private ViewPager mViewPager;
	private ArrayList<BasePager>mPagers;

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
		return view;
	}

	@Override
	public void initData() {
		mPagers=new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsPager(mActivity));
		mPagers.add(new SmartPager(mActivity));
		mPagers.add(new GovPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
	}

	private class ContentAdapter extends PagerAdapter{

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
			View view=mPagers.get(position).mRootView;
			mPagers.get(position).initData();
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
}
