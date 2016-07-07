package com.donghaowxr.zhihuiwuxi.fragment;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.mappager.DingWeiPager;
import com.donghaowxr.zhihuiwuxi.mappager.FuJinPager;
import com.donghaowxr.zhihuiwuxi.pager.BasePager;
import com.donghaowxr.zhihuiwuxi.pager.GovPager;
import com.donghaowxr.zhihuiwuxi.pager.HomePager;
import com.donghaowxr.zhihuiwuxi.pager.NewsPager;
import com.donghaowxr.zhihuiwuxi.pager.SettingPager;
import com.donghaowxr.zhihuiwuxi.pager.SmartPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private ViewPager mViewPager;
	private ArrayList<BasePager>mPagers;
	private RadioGroup rgContent;

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
		rgContent = (RadioGroup) view.findViewById(R.id.rg_content);
		return view;
	}

	@Override
	public void initData() {
		//将page页添加到集合中
		mPagers=new ArrayList<BasePager>();
		mPagers.add(new NewsPager(mActivity));
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new SmartPager(mActivity));
		mPagers.add(new GovPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		
		rgContent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_Home:
					pauseMap();
					mViewPager.setCurrentItem(0, false);//参数2:不加入滑动效果动画
					break;
				case R.id.rb_News:
					pauseMap();
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_Smart:
					pauseMap();
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_Gov:
					resumeMap();
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_Setting:
					pauseMap();
					mViewPager.setCurrentItem(4, false);
					break;
				}
			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mPagers.get(position).initData();
				if (position==1||position==mPagers.size()-1||position==2||position==3) {
					setLeftMenuState(false);
				}else {
					setLeftMenuState(true);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		mPagers.get(0).initData();//默认加载第一页
		setLeftMenuState(true);
	}

	public void setLeftMenuState(boolean b) {
		MainActivity activity=(MainActivity)mActivity;
		SlidingMenu slidingMenu=activity.getSlidingMenu();
		if (b) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//开启侧边栏可以滑动
		}else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//关闭侧边栏不可以滑动
		}
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
		
		/**
		 * 将pager页添加到ContentFragment的ViewPager中
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view=mPagers.get(position).mRootView;
//			mPagers.get(position).initData();//在此处viewpager会初始化两个页面，影响性能
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	
	/**
	 * 获取新闻中心页对象
	 * @return 新闻中心页对象
	 */
	public NewsPager getNewsPager(){
		NewsPager pager=(NewsPager) mPagers.get(0);
		return pager;
	}
	
	public SmartPager getSmartPager(){
		SmartPager pager = (SmartPager) mPagers.get(2);
		return pager;
	}
	
	public GovPager getGovPager(){
		GovPager pager=(GovPager) mPagers.get(3);
		return pager;
	}
	
	public void destoryMap(){
		GovPager govPager=getGovPager();
		DingWeiPager dingWeiPager=govPager.getDingWeiPager();
		if (dingWeiPager!=null) {
			dingWeiPager.mapDestory();
		}
		FuJinPager fuJinPager=govPager.getFuJinPager();
		if (fuJinPager!=null) {
			fuJinPager.mapDestory();
		}
	}
	public void resumeMap(){
		GovPager govPager=getGovPager();
		DingWeiPager dingWeiPager=govPager.getDingWeiPager();
		if (dingWeiPager!=null) {
			dingWeiPager.mapResume();
		}
	}
	public void pauseMap(){
		GovPager govPager=getGovPager();
		DingWeiPager dingWeiPager=govPager.getDingWeiPager();
		if (dingWeiPager!=null) {
			dingWeiPager.stopLocation();
			dingWeiPager.mapPause();
		}
		FuJinPager fuJinPager=govPager.getFuJinPager();
		if (fuJinPager!=null) {
			fuJinPager.mapPause();
		}
	}
}
