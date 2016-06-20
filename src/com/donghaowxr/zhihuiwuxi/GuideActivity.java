package com.donghaowxr.zhihuiwuxi;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.utils.DensityUtils;
import com.donghaowxr.zhihuiwuxi.utils.PerfUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends Activity {
	private Button btn_start;
	private ViewPager vp_guide;
	private int[] mImageIds = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private ArrayList<ImageView> mImageViewlist;
	private LinearLayout llParent;
	private ImageView iv_point_red;
	private int mPointDis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题栏
		setContentView(R.layout.activity_guide);
		initView();
		initData();
		initAdapter();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		btn_start = (Button) findViewById(R.id.btn_start);
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		llParent = (LinearLayout) findViewById(R.id.llParent);
		iv_point_red = (ImageView) findViewById(R.id.iv_point_red);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mImageViewlist = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			mImageViewlist.add(imageView);
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.shap_point_grey);
			if (i > 0) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = DensityUtils.dp2px(10, this);
				point.setLayoutParams(params);// 设置小圆点左边距
			}
			llParent.addView(point);
		}
	}

	/**
	 * 初始化控制器
	 */
	private void initAdapter() {
		vp_guide.setAdapter(new GuideAdapter());
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position == 2) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int leftMargin = (int) (positionOffset * mPointDis + position
						* mPointDis);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_point_red
						.getLayoutParams();
				params.leftMargin = leftMargin;
				iv_point_red.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		// 计算两个圆点之间的距离，第二个小圆点left值－第一个小圆点left值
		// 获取视图树，当控件位置确定后获取两个圆点之间的距离，监听onLayout方式什么时候执行完
		iv_point_red.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						iv_point_red.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);// 避免重复回调
						mPointDis = llParent.getChildAt(1).getLeft()
								- llParent.getChildAt(0).getLeft();
					}
				});
		/**
		 * 点击跳转到主页面
		 */
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PerfUtils.setBoolean(getApplicationContext(), "loginState",
						false);
				Intent intent = new Intent(GuideActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViewlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewlist.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
