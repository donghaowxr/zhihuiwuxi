package com.donghaowxr.zhihuiwuxi.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.donghaowxr.zhihuiwuxi.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint({ "ClickableViewAccessibility", "SimpleDateFormat" }) public class PullToRefreshListView extends ListView implements OnScrollListener {

	private View headerView;
	private int measureHeight;
	private float startY;
	private float endY;
	private float dY;
	private final int STATE_PULL_TO_REFRESH=0;
	private final int STATE_RELEASE_TO_REFRESH=1;
	private final int STATE_REFRESHING=2;
	private int mCurrentState=STATE_PULL_TO_REFRESH;
	private ImageView ivArrow;
	private ProgressBar pbLoading;
	private TextView tvTitle;
	private TextView tvTime;
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;
	private OnRefreshListener onRefreshListener;
	private View footView;
	private int footMeasureHeight;
	private boolean isScroll=true;

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFootView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFootView();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFootView();
	}
	
	/**
	 * 初始化头部布局
	 */
	private void initHeaderView() {
		headerView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) headerView.findViewById(R.id.pb_loading);
		tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
		tvTime = (TextView) headerView.findViewById(R.id.tv_time);
		addHeaderView(headerView);
		headerView.measure(0, 0);
		measureHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -measureHeight, 0, 0);
		refreshTime();
		initAmin();
	}
	
	/**
	 * 初始化脚布局
	 */
	private void initFootView() {
		footView = View.inflate(getContext(), R.layout.foot_to_more, null);
		addFooterView(footView);
		footView.measure(0, 0);
		footMeasureHeight = footView.getMeasuredHeight();
		footView.setPadding(0, -footMeasureHeight, 0, 0);
		this.setOnScrollListener(this);
	}
	
	/**
	 * 初始化旋转动画
	 */
	private void initAmin() {
		upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(100);
		upAnimation.setFillAfter(true);
		
		downAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(100);
		downAnimation.setFillAfter(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY==-1) {
				startY=ev.getY();//防止用户按住viewpager上下滑动的时候事件被viewpager消费掉
			}
			endY = ev.getY();
			dY = endY-startY;
			if (mCurrentState==STATE_REFRESHING) {
				break;
			}
			if (getFirstVisiblePosition()==0&&dY>0) {
				int padding=(int) (dY-measureHeight);
				headerView.setPadding(0, padding, 0, 0);
				if (padding>0&&mCurrentState!=STATE_RELEASE_TO_REFRESH) {
					mCurrentState=STATE_RELEASE_TO_REFRESH;
					refreshState();
				}else if (padding<0&&mCurrentState!=STATE_PULL_TO_REFRESH) {
					mCurrentState=STATE_PULL_TO_REFRESH;
					refreshState();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mCurrentState==STATE_RELEASE_TO_REFRESH) {
				mCurrentState=STATE_REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
			}else if (mCurrentState==STATE_PULL_TO_REFRESH) {
				headerView.setPadding(0, -measureHeight, 0, 0);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉控件状态
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(downAnimation);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(upAnimation);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.INVISIBLE);
			pbLoading.setVisibility(View.VISIBLE);
			if (onRefreshListener!=null) {
				onRefreshListener.onRefresh();
			}
			break;
		}
	}
	
	/**
	 * 恢复初始状态
	 */
	public void setRefreshComplete(boolean success){
		headerView.setPadding(0, -measureHeight, 0, 0);
		mCurrentState=STATE_PULL_TO_REFRESH;
		tvTitle.setText("下拉刷新");
		ivArrow.setVisibility(View.VISIBLE);
		pbLoading.setVisibility(View.INVISIBLE);
		if (success) {
			refreshTime();
		}
	}
	
	private void refreshTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time=format.format(new Date());
		tvTime.setText(time);
	}
	
	/**
	 * 设置下拉刷新监听回调函数
	 * @param listener
	 */
	public void setOnRefreshListener(OnRefreshListener listener){
		onRefreshListener=listener;
	}
	
	public interface OnRefreshListener{
		void onRefresh();
		void onLoadMore();
	}

	/**
	 * 当滑动状态变化时
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState==SCROLL_STATE_IDLE) {//当滑动状态为空闲时
			int currentPosition=getLastVisiblePosition();
			if (currentPosition==getCount()-1) {
				if (isScroll) {
					isScroll=false;
					footView.setPadding(0, 0, 0, 0);
					setSelection(getCount()-1);
					if (onRefreshListener!=null) {
						onRefreshListener.onLoadMore();
					}
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

}
