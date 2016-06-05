package com.donghaowxr.zhihuiwuxi.fragment;

import java.util.ArrayList;
import com.donghaowxr.zhihuiwuxi.MainActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.NewsMenu.DataArray;
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

@SuppressLint("ViewHolder") public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_menu)
	private ListView lvMenu;
	private ArrayList<DataArray>mMenuData;
	private LeftMenuAdapter leftMenuAdapter;
	private int mCurentPos;
	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

	}

	public void setMenuData(ArrayList<DataArray> data) {
		mMenuData=data;
		leftMenuAdapter = new LeftMenuAdapter();
		lvMenu.setAdapter(leftMenuAdapter);
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mCurentPos=position;
				leftMenuAdapter.notifyDataSetChanged();
				Toggle();
			}
		});
	}
	
	/**
	 * 切换侧边栏状态
	 */
	protected void Toggle() {
		MainActivity activity=(MainActivity) mActivity;
		SlidingMenu slidingMenu=activity.getSlidingMenu();
		slidingMenu.toggle();
	}

	private class LeftMenuAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mMenuData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mMenuData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View view=View.inflate(mActivity, R.layout.item_leftmenu, null);
			TextView tvLeftMenuItem=(TextView) view.findViewById(R.id.tv_leftmenu_item);
			DataArray item=mMenuData.get(position);
			tvLeftMenuItem.setText(item.title);
			if (mCurentPos==position) {
				tvLeftMenuItem.setEnabled(true);
			}else {
				tvLeftMenuItem.setEnabled(false);
			}
			return view;
		}
		
	}

}
