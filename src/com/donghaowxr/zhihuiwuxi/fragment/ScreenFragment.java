package com.donghaowxr.zhihuiwuxi.fragment;

import com.donghaowxr.zhihuiwuxi.MainVideoActivity;
import com.donghaowxr.zhihuiwuxi.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScreenFragment extends BaseFragment {

	private ListView lvScreen;
	private String[] mScreens = new String[] { "超清", "高清", "标清" };

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_screen, null);
		lvScreen = (ListView) view.findViewById(R.id.lv_screen);
		return view;
	}

	@Override
	public void initData() {
		lvScreen.setAdapter(new ScreenAdapter());
		lvScreen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainVideoActivity activity=(MainVideoActivity) mActivity;
				VideoFragment videoFragment=activity.getVideoFragment();
				videoFragment.changeScreenName(mScreens[position]);
				videoFragment.toggle();
			}
		});
	}
	
	private class ScreenAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mScreens.length;
		}

		@Override
		public String getItem(int position) {
			return mScreens[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView==null) {
				viewHolder=new ViewHolder();
				convertView=View.inflate(mActivity, R.layout.list_item_screen, null);
				viewHolder.tvScreen=(TextView) convertView.findViewById(R.id.tv_screen);
				convertView.setTag(viewHolder);
			}else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.tvScreen.setText(mScreens[position]);
			return convertView;
		}
	}
	
	static class ViewHolder{
		public TextView tvScreen;
	}

}
