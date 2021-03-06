package com.donghaowxr.zhihuiwuxi.mappager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.MainActivity.OnMapState;
import com.donghaowxr.zhihuiwuxi.domain.HmPos;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class BaseMap extends BaseMapPager {

	protected MapView mMapView;
	protected BaiduMap mBaiduMap;
	protected HmPos hmPos;
	protected LinearLayout llSearch;
	protected Button btnSearchLocal;
	protected Button btnSearchBus;
	protected View view;

	public BaseMap(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.page_map, null);
		if (mMapView == null) {
			mMapView = (MapView) view.findViewById(R.id.bmapView);
		}
		llSearch = (LinearLayout) view.findViewById(R.id.ll_search);
		btnSearchLocal = (Button) view.findViewById(R.id.btn_search_local);
		btnSearchBus = (Button) view.findViewById(R.id.btn_search_bus);
		return view;
	}

	@Override
	public final void initData() {
		mMapView.onResume();
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
		if (mBaiduMap == null) {
			mBaiduMap = mMapView.getMap();
		}
		hmPos = new HmPos(30.190879, 120.186773);
		initSetting();
	}

	protected abstract void initSetting();

	public void mapDestory() {
		if (mMapView != null) {
			mMapView.onDestroy();
		}
	}

	public void mapResume() {
		if (mMapView != null) {
			mMapView.onResume();
		}
	}

	public void mapPause() {
		if (mMapView != null) {
			mMapView.onPause();
		}
	}
}
