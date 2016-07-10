package com.donghaowxr.zhihuiwuxi.fragment;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.MapMenu.MapData;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray;
import com.donghaowxr.zhihuiwuxi.mappager.DingWeiPager;
import com.donghaowxr.zhihuiwuxi.mappager.FuJinPager;
import com.donghaowxr.zhihuiwuxi.pager.GovPager;
import com.donghaowxr.zhihuiwuxi.pager.NewsPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_menu)
	private ListView lvMenu;
	private ArrayList<DataArray> mMenuData;
	private LeftMenuAdapter leftMenuAdapter;
	private ArrayList<MapData> mMapDatas;
	private LeftMapMenuAdapter mLeftMapMenuAdapter;
	public int mCurentPos;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

	}

	/**
	 * 设置菜单栏数据
	 * 
	 * @param data
	 *            json对象
	 */
	public void setMenuData(ArrayList<DataArray> data) {
		mMenuData = data;
		leftMenuAdapter = new LeftMenuAdapter();
		lvMenu.setAdapter(leftMenuAdapter);
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurentPos = position;
				leftMenuAdapter.notifyDataSetChanged();
				Toggle();
				setCurrentDetailPager(position);
			}
		});
	}

	/**
	 * 设置当前菜单对应的页面
	 * 
	 * @param position
	 *            位置
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity activity = (MainActivity) mActivity;// 拿到activity对象
		ContentFragment fragment = activity.getContentFragment();// 拿到contentfragment对象
		NewsPager pager = fragment.getNewsPager();// 拿到新闻中心页对象
		pager.setCurrentDetailPager(position);// 设置新闻中心当前选中的菜单页
	}

	public void setMapMenu(ArrayList<MapData> data) {
		mMapDatas = data;
		mLeftMapMenuAdapter = new LeftMapMenuAdapter();
		lvMenu.setAdapter(mLeftMapMenuAdapter);
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurentPos = position;
				mLeftMapMenuAdapter.notifyDataSetChanged();
				Toggle();
				setCurrentMapDetailPager(position);
			}
		});
	}

	protected void setCurrentMapDetailPager(int position) {
		MainActivity activity = (MainActivity) mActivity;
		ContentFragment contentFragment = activity.getContentFragment();
		GovPager pager = contentFragment.getGovPager();
		DingWeiPager dingWeiPager = pager.getDingWeiPager();
		pager.setCurrentPager(position);
		if (position == 1) {
			dingWeiPager.stopLocation();
		}
	}

	/**
	 * 切换侧边栏状态
	 */
	protected void Toggle() {
		MainActivity activity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = activity.getSlidingMenu();
		slidingMenu.toggle();
	}

	private class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMenuData.size();
		}

		@Override
		public Object getItem(int position) {
			return mMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.item_leftmenu, null);
			TextView tvLeftMenuItem = (TextView) view
					.findViewById(R.id.tv_leftmenu_item);
			DataArray item = mMenuData.get(position);
			tvLeftMenuItem.setText(item.title);
			if (mCurentPos == position) {
				tvLeftMenuItem.setEnabled(true);
			} else {
				tvLeftMenuItem.setEnabled(false);
			}
			return view;
		}
	}

	public class LeftMapMenuAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mMapDatas.size();
		}

		@Override
		public MapData getItem(int position) {
			return mMapDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.item_leftmenu, null);
			TextView tvLeftMenuItem = (TextView) view
					.findViewById(R.id.tv_leftmenu_item);
			MapData item = mMapDatas.get(position);
			tvLeftMenuItem.setText(item.title);
			if (mCurentPos == position) {
				tvLeftMenuItem.setEnabled(true);
			} else {
				tvLeftMenuItem.setEnabled(false);
			}
			return view;
		}

	}
}
