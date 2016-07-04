package com.donghaowxr.zhihuiwuxi.mappager;


import com.baidu.mapapi.map.MapView;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.MainActivity.OnMapState;
import com.donghaowxr.zhihuiwuxi.R;

import android.app.Activity;
import android.view.View;

public class DingWeiPager extends BaseMapPager {

	private MapView mMapView;

	public DingWeiPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_dingwei, null);
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		return view;
	}
	
	@Override
	public void initData() {
		MainActivity activity=(MainActivity) mActivity;
		activity.setOnMapStateListener(new OnMapState() {
			@Override
			public void MapState(String state) {
				if (state.equals("destory")) {
					mMapView.onDestroy();
				}
				if (state.equals("resume")) {
					mMapView.onResume();
				}
				if (state.equals("pause")) {
					mMapView.onPause();
				}
			}
		});
	}
	
}
