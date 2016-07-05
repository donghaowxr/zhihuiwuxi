package com.donghaowxr.zhihuiwuxi.mappager;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.utils.LocationUtils;

import android.app.Activity;

public class DingWeiPager extends BaseMap {

	private BitmapDescriptor icon;
	private MarkerOptions options;

	public DingWeiPager(Activity activity) {
		super(activity);
	}

	@Override
	protected void initSetting() {
		LocationUtils.initLocation(mActivity);
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newLatLng(new LatLng(hmPos.latitude, hmPos.longituds));
		mBaiduMap.setMapStatus(mapStatusUpdate);
		MapStatusUpdate zoonMapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.setMapStatus(zoonMapStatusUpdate);
		initMarker();
	}

	private void initMarker() {
		icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_eat);
		options = new MarkerOptions().icon(icon).title("优迈科技园")
				.position(new LatLng(hmPos.latitude, hmPos.longituds))
				.draggable(true);
		mBaiduMap.addOverlay(options);

		icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_eat);
		options = new MarkerOptions().icon(icon).title("向北")
				.position(new LatLng(hmPos.latitude+0.001, hmPos.longituds))
				.draggable(true);
		mBaiduMap.addOverlay(options);
		
		icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_eat);
		options = new MarkerOptions().icon(icon).title("向东")
				.position(new LatLng(hmPos.latitude, hmPos.longituds+0.001))
				.draggable(true);
		mBaiduMap.addOverlay(options);
		
		icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_eat);
		options = new MarkerOptions().icon(icon).title("向西南")
				.position(new LatLng(hmPos.latitude-0.001, hmPos.longituds))
				.draggable(true);
		mBaiduMap.addOverlay(options);
	}
}
