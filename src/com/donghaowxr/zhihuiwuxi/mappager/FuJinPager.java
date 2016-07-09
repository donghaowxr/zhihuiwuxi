package com.donghaowxr.zhihuiwuxi.mappager;

import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.donghaowxr.zhihuiwuxi.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
		btnSearchLocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPoiSearch.searchInCity(getSearchParam(etCity.getText().toString(),etLocal.getText().toString()));
			}
		});	
	}

	private PoiCitySearchOption getSearchParam(String mCity,String mKeyword) {
		PoiCitySearchOption mPoiCitySearchOption = new PoiCitySearchOption();
		mPoiCitySearchOption.city(mCity);
		mPoiCitySearchOption.keyword(mKeyword);
		mPoiCitySearchOption.pageCapacity(10);
		mPoiCitySearchOption.pageNum(0);
		return mPoiCitySearchOption;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult result) {
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result==null||result.error!=SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mActivity, "检索失败", Toast.LENGTH_SHORT).show();
			return;
		}
		PoiOverlay poiOverlay=new PoiOverlay(mBaiduMap);
		poiOverlay.setData(result);
		poiOverlay.addToMap();
		poiOverlay.zoomToSpan();
	}
}
