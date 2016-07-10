package com.donghaowxr.zhihuiwuxi.mappager;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
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

public class FuJinPager extends BaseMap implements
		OnGetPoiSearchResultListener, OnGetRoutePlanResultListener {

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
	private ArrayList<View> popList;

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
				pop = null;
			}
			if (hideThread != null) {
				hideThread.interrupt();
				hideThread = null;
			}
		};
	};
	private List<TransitRouteLine> routeLines;
	private TransitRouteOverlay transitRouteOverlay;

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
		searchInCity();
		searchByBus();
	}

	/**
	 * 初始化城市内搜索
	 */
	private void searchInCity() {
		mPoiSearch = PoiSearch.newInstance();
		// 设置城市内搜索监听
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		popList = new ArrayList<View>();

		// 城市搜索结果结果显示
		poiOverlay = new PoiOverlay(mBaiduMap) {
			@Override
			public boolean onPoiClick(int i) {
				PoiInfo poiInfo = getPoiResult().getAllPoi().get(i);
				mPoiSearch.searchPoiDetail(getSearchDetailParams(poiInfo.uid));
				if (pop == null) {
					pop = View.inflate(mActivity, R.layout.item_pop, null);
					tvTitle = (TextView) pop.findViewById(R.id.tv_pop_title);
					MapViewLayoutParams params = createLayoutParam(poiInfo.location);
					mMapView.addView(pop, params);
					popList.add(pop);
				} else {
					mMapView.updateViewLayout(pop,
							createLayoutParam(poiInfo.location));
				}
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
		/**
		 * 开始城市内搜索
		 */
		btnSearchLocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBaiduMap.clear();
				mPoiSearch.searchInCity(getSearchParam(etCity.getText()
						.toString(), etLocal.getText().toString()));
			}
		});
	}

	private void searchByBus() {
		final RoutePlanSearch mRoutePlanSearch = RoutePlanSearch.newInstance();
		mRoutePlanSearch.setOnGetRoutePlanResultListener(this);
		btnSearchBus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBaiduMap.clear();
				mRoutePlanSearch.transitSearch(getBusSearchParam());
			}
		});
		transitRouteOverlay = new TransitRouteOverlay(mBaiduMap) {
			@Override
			public boolean onRouteNodeClick(int i) {
				String desc = routeLines.get(0).getAllStep().get(i)
						.getInstructions();
				if (pop == null) {
					pop = View.inflate(mActivity, R.layout.item_pop, null);
					tvTitle = (TextView) pop.findViewById(R.id.tv_pop_title);
					MapViewLayoutParams params = createLayoutParam(routeLines
							.get(0).getAllStep().get(i).getEntrance()
							.getLocation());
					mMapView.addView(pop, params);
					popList.add(pop);
				} else {
					mMapView.updateViewLayout(pop, createLayoutParam(routeLines
							.get(0).getAllStep().get(i).getEntrance()
							.getLocation()));
				}
				tvTitle.setText(desc);
				if (hideThread == null) {
					hideThread = new HideThread();
					hideThread.start();
				} else {
					hideThread.run();
				}
				return true;
			}
		};
	}

	private TransitRoutePlanOption getBusSearchParam() {
		TransitRoutePlanOption params = new TransitRoutePlanOption();
		params.from(PlanNode.withCityNameAndPlaceName(etCity.getText()
				.toString(), etBusStart.getText().toString()));
		params.to(PlanNode.withCityNameAndPlaceName(
				etCity.getText().toString(), etBusStop.getText().toString()));
		params.city(etCity.getText().toString());// 设置在那个城市内搜索
		params.policy(TransitPolicy.EBUS_TIME_FIRST);// 设置策略为时间优先
		return params;
	}

	/**
	 * 获取搜索详细信息参数
	 * 
	 * @param poiUid
	 * @return
	 */
	protected PoiDetailSearchOption getSearchDetailParams(String poiUid) {
		PoiDetailSearchOption params = new PoiDetailSearchOption();
		params.poiUid(poiUid);
		return params;
	}

	/**
	 * 获取城市内搜索参数
	 * 
	 * @param mCity
	 * @param mKeyword
	 * @return
	 */
	private PoiCitySearchOption getSearchParam(String mCity, String mKeyword) {
		PoiCitySearchOption mPoiCitySearchOption = new PoiCitySearchOption();
		mPoiCitySearchOption.city(mCity);
		mPoiCitySearchOption.keyword(mKeyword);
		mPoiCitySearchOption.pageCapacity(10);
		mPoiCitySearchOption.pageNum(0);
		return mPoiCitySearchOption;
	}

	/**
	 * 详情页结果返回
	 */
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

	/**
	 * 标志物详情布局参数
	 * 
	 * @param position
	 * @return
	 */
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

	/**
	 * 获取城市内搜索返回
	 */
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

	/**
	 * 获取骑车搜索结果回调方法
	 */
	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {

	}

	/**
	 * 获取驾车搜索结果回调方法
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {

	}

	/**
	 * 获取换乘搜索结果回调方法
	 */
	@Override
	public void onGetTransitRouteResult(final TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mActivity, "检索失败", Toast.LENGTH_SHORT).show();
			return;
		}
		mBaiduMap.setOnMarkerClickListener(transitRouteOverlay);
		routeLines = result.getRouteLines();
		transitRouteOverlay.setData(routeLines.get(0));
		transitRouteOverlay.addToMap();
		transitRouteOverlay.zoomToSpan();
	}

	/**
	 * 获取步行搜索结果回调方法
	 */
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

	}
}
