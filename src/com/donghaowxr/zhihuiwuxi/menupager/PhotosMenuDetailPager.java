package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;

import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.PhotoBean;
import com.donghaowxr.zhihuiwuxi.domain.PhotoBean.PhotoData.PhotoNews;
import com.donghaowxr.zhihuiwuxi.global.GlobalConfig;
import com.donghaowxr.zhihuiwuxi.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements OnClickListener {
	@ViewInject(R.id.lv_photo)
	private ListView lvPhoto;
	@ViewInject(R.id.gv_photo)
	private GridView gvPhoto;
	private ArrayList<PhotoNews> photoNews;
	private boolean isListView=true;
	private ImageButton mBtnPhoto;

	public PhotosMenuDetailPager(Activity activity,ImageButton btnPhoto) {
		super(activity);
		btnPhoto.setOnClickListener(this);
		mBtnPhoto=btnPhoto;
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_photos_details, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		String cacheData = CacheUtils.getCache(GlobalConfig.PHOTOS_URL,
				mActivity, "");
		if (!TextUtils.isEmpty(cacheData)) {
			processData(cacheData);
		}
		getDataFromServer();
	}

	/**
	 * 从api获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConfig.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						processData(result);
						CacheUtils.setCache(GlobalConfig.PHOTOS_URL, result,
								mActivity);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						System.out.println(msg);
						Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	/**
	 * 解析json数据
	 * 
	 * @param result
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		PhotoBean dataBean = gson.fromJson(result, PhotoBean.class);
		photoNews = dataBean.data.news;

		lvPhoto.setAdapter(new PhotoAdapter());
		gvPhoto.setAdapter(new PhotoAdapter());
	}

	public class PhotoAdapter extends BaseAdapter {
		private BitmapUtils bitmapUtils;

		public PhotoAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return photoNews.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			return photoNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.list_item_photos, null);
				holder = new ViewHolder();
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(photoNews.get(position).title);
			String listimage = photoNews.get(position).listimage;
			listimage = listimage.substring(25, listimage.length());
			listimage = GlobalConfig.SERVER_URL + listimage;
			bitmapUtils.display(holder.ivPhoto, listimage);
			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivPhoto;
		public TextView tvTitle;
	}

	@Override
	public void onClick(View v) {
		if (isListView) {
			isListView=false;
			mBtnPhoto.setImageResource(R.drawable.icon_pic_list_type);
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
		}else {
			isListView=true;
			mBtnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
		}
	}

}
