package com.donghaowxr.zhihuiwuxi.animpager;

import com.donghaowxr.zhihuiwuxi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class AnimSelectPager extends BaseAnimPager {

	@ViewInject(R.id.gv_anim_sel)
	GridView gvAnimSel;
	private int[] animCount = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13 };

	public AnimSelectPager(Activity activity) {
		super(activity);
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_anim_select, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		gvAnimSel.setAdapter(new SelAdapter());
	}

	private class SelAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return animCount.length;
		}

		@Override
		public Object getItem(int position) {
			return animCount[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.item_anim_sel,
						null);
				viewHolder.btnSelAnim = (Button) convertView
						.findViewById(R.id.btn_sel_anim);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.btnSelAnim.setText(String.valueOf(animCount[position]));
			return convertView;
		}
	}

	static class ViewHolder {
		public Button btnSelAnim;
	}
}
