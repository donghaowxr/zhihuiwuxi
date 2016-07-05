package com.donghaowxr.zhihuiwuxi.mappager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.MainActivity.OnMapState;
import com.donghaowxr.zhihuiwuxi.R;

import android.app.Activity;
import android.view.View;

public class DingWeiPager extends BaseMapPager {

	private MapView mMapView;
	private BaiduMap mBaiduMap;

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
		MainActivity activity = (MainActivity) mActivity;
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
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newLatLng(new LatLng(30.190879,120.186773));
		mBaiduMap.setMapStatus(mapStatusUpdate);
		MapStatusUpdate zoonMapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.setMapStatus(zoonMapStatusUpdate);
	}
}
