package com.donghaowxr.zhihuiwuxi.animpager;

import java.util.ArrayList;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.AnimBean;
import com.donghaowxr.zhihuiwuxi.pager.BasePager;
import com.donghaowxr.zhihuiwuxi.view.NoScrollViewPager;

public class BaseAnimPager extends BasePager {

	public NoScrollViewPager vpAnimPager;
	public AnimBean animBean;
	public ArrayList<BaseAnimPager> mBasePagers;
	private BaseAnimAdapter mBaseAnimAdapter;
	private AnimSelectPager selectPager;

	public BaseAnimPager(Activity activity) {
		super(activity);
	}

	public BaseAnimPager(Activity activity, AnimBean mAnimBean) {
		super(activity);
		animBean = mAnimBean;
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_base_anim, null);
		vpAnimPager = (NoScrollViewPager) view.findViewById(R.id.vp_anim_page);
		return view;
	}

	@Override
	public void initData() {
		mBasePagers = new ArrayList<BaseAnimPager>();
		mBasePagers.add(new AnimPager(mActivity, animBean));
		selectPager = new AnimSelectPager(mActivity);
		mBasePagers.add(selectPager);
		vpAnimPager.setOffscreenPageLimit(0);
		mBaseAnimAdapter = new BaseAnimAdapter();
		vpAnimPager.setAdapter(mBaseAnimAdapter);
		vpAnimPager.setCurrentItem(0);
	}

	public NoScrollViewPager getViewPager() {
		if (vpAnimPager != null) {
			return vpAnimPager;
		} else {
			return null;
		}
	}
	
	public void setTitle(String title){
		selectPager.setTitle(title);
	}

	public class BaseAnimAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mBasePagers.size();
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
			BaseAnimPager mAnimPager = mBasePagers.get(position);
			View view = mAnimPager.mRootView;
			container.addView(view);
			mAnimPager.initData();
			return view;
		}
	}
}
