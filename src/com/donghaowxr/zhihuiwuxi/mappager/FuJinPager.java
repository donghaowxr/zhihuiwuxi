package com.donghaowxr.zhihuiwuxi.mappager;

import java.util.ArrayList;

import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.donghaowxr.zhihuiwuxi.R;

import android.R.integer;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FuJinPager extends BaseMap implements OnGetPoiSearchResultListener {

	private PoiSearch mPoiSearch;
	private String city;
	private String local;
	private String busStart;
	private String busEnd;
	private EditText etCity;
	private EditText etLocal;
	private EditText etBusStart;
	private EditText etBusStop;
	private PoiOverlay poiOverlay;
	private View pop;
	private TextView tvTitle;
	private HideThread hideThread = null;
	private ArrayList<View>popList;
	private class HideThread extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message message = handler.obtainMessage();
			handler.sendMessage(message);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// mMapView.removeView(pop);
			for (int i = 0; i < popList.size(); i++) {
				mMapView.removeView(popList.get(i));
				popList.remove(i);
			}
			if (hideThread != null) {
				hideThread.interrupt();
				hideThread = null;
			}
		};
	};

	public FuJinPager(Activity activity) {
		super(activity);
	}

	@Override
	protected void initSetting() {
		llSearch.setVisibility(View.VISIBLE);
		etCity = (EditText) view.findViewById(R.id.et_city);
		etLocal = (EditText) view.findViewById(R.id.et_local);
		etBusStart = (EditText) view.findViewById(R.id.et_bus_start);
		etBusStop = (EditText) view.findViewById(R.id.et_bus_stop);
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		popList=new ArrayList<View>();
		poiOverlay = new PoiOverlay(mBaiduMap) {
			@Override
			public boolean onPoiClick(int i) {
				PoiInfo poiInfo = getPoiResult().getAllPoi().get(i);
				mPoiSearch.searchPoiDetail(getSearchDetailParams(poiInfo.uid));
				// if (pop == null) {
				pop = View.inflate(mActivity, R.layout.item_pop, null);
				tvTitle = (TextView) pop.findViewById(R.id.tv_pop_title);
				MapViewLayoutParams params = createLayoutParam(poiInfo.location);
				mMapView.addView(pop, params);
				popList.add(pop);
				// }else {
				// mMapView.updateViewLayout(pop,
				// createLayoutParam(poiInfo.location));
				// }
				tvTitle.setText(poiInfo.name);
				if (hideThread == null) {
					hideThread = new HideThread();
					hideThread.start();
				} else {
					hideThread.run();
				}
				return true;
			}
		};
		btnSearchLocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPoiSearch.searchInCity(getSearchParam(etCity.getText()
						.toString(), etLocal.getText().toString()));
			}
		});
	}

	protected PoiDetailSearchOption getSearchDetailParams(String poiUid) {
		PoiDetailSearchOption params = new PoiDetailSearchOption();
		params.poiUid(poiUid);
		return params;
	}

	private PoiCitySearchOption getSearchParam(String mCity, String mKeyword) {
		PoiCitySearchOption mPoiCitySearchOption = new PoiCitySearchOption();
		mPoiCitySearchOption.city(mCity);
		mPoiCitySearchOption.keyword(mKeyword);
		mPoiCitySearchOption.pageCapacity(10);
		mPoiCitySearchOption.pageNum(0);
		return mPoiCitySearchOption;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mActivity, "没有搜索到详情", Toast.LENGTH_SHORT).show();
			return;
		}
		// if (pop == null) {
		// pop = View.inflate(mActivity, R.layout.item_pop, null);
		// tvTitle = (TextView) pop.findViewById(R.id.tv_pop_title);
		// MapViewLayoutParams params = createLayoutParam(result.getLocation());
		// mMapView.addView(pop, params);
		// }else {
		// mMapView.updateViewLayout(pop,
		// createLayoutParam(result.getLocation()));
		// }
		// tvTitle.setText(result.getName());
	}

	private MapViewLayoutParams createLayoutParam(LatLng position) {
		MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
		builder.layoutMode(ELayoutMode.mapMode);
		builder.position(position);
		builder.yOffset(-25);
		MapViewLayoutParams params = builder.build();
		return params;
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult result) {
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mActivity, "检索失败", Toast.LENGTH_SHORT).show();
			return;
		}

		mBaiduMap.setOnMarkerClickListener(poiOverlay);
		poiOverlay.setData(result);
		poiOverlay.addToMap();
		poiOverlay.zoomToSpan();
	}
}
