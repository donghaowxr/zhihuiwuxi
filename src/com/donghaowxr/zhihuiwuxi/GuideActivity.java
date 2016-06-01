package com.donghaowxr.zhihuiwuxi;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity {
	private Button btn_start;
	private ViewPager vp_guide;
	private int[] mImageIds=new int[]{
		R.drawable.guide_1,
		R.drawable.guide_2,
		R.drawable.guide_3
	};
	private ArrayList<ImageView>mImageViewlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
		setContentView(R.layout.activity_guide);
		initView();
		initData();
		initAdapter();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		btn_start = (Button) findViewById(R.id.btn_start);
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mImageViewlist=new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView=new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			mImageViewlist.add(imageView);
		}
	}
	
	/**
	 * 初始化控制器
	 */
	private void initAdapter() {
		vp_guide.setAdapter(new GuideAdapter());
	}
	
	private class GuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mImageViewlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view=mImageViewlist.get(position);
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
}
