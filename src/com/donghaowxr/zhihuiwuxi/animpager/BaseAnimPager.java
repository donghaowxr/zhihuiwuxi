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

public class BaseAnimPager extends BasePager {

	public ViewPager vpAnimPager;
	public AnimBean animBean;
	private ArrayList<BasePager> mBasePagers;

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
		vpAnimPager = (ViewPager) view.findViewById(R.id.vp_anim_page);
		return view;
	}

	@Override
	public void initData() {
		mBasePagers = new ArrayList<BasePager>();
		mBasePagers.add(new AnimPager(mActivity, animBean));
		vpAnimPager.setAdapter(new BaseAnimAdapter());
//		vpAnimPager.setCurrentItem(0);
	}

	public class BaseAnimAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mBasePagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			AnimPager mAnimPager=(AnimPager) mBasePagers.get(position);
			View view=mAnimPager.mRootView;
			container.addView(view);
			mAnimPager.initData();
			return view;
		}
	}
}
