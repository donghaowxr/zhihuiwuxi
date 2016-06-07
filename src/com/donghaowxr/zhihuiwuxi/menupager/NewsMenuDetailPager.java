package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray.ChildrenArray;
import com.donghaowxr.zhihuiwuxi.fragment.ContentFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class NewsMenuDetailPager extends BaseMenuDetailPager {

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	private ArrayList<ChildrenArray> mTabData;
	private ArrayList<TabDetailPager>mPagers;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;

	public NewsMenuDetailPager(Activity activity, ArrayList<ChildrenArray> children) {
		super(activity);
		mTabData = children;
	}

	/**
	 * 初始化新闻菜单页布局
	 */
	@Override
	protected View initView() {
		View view=View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	/**
	 * 将新闻菜单页加入到viewpager中
	 */
	@Override
	public void initData() {
		mPagers=new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager tabDetailPager=new TabDetailPager(mActivity,mTabData.get(i));
			mPagers.add(tabDetailPager);
		}
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		
		mIndicator.setViewPager(mViewPager);
	}
	
	private class NewsMenuDetailAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabData.get(position).title;
		}
		
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
