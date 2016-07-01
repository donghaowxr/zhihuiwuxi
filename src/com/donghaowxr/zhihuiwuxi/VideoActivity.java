package com.donghaowxr.zhihuiwuxi;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.widget.MediaController.OnDanmuAddListener;
import io.vov.vitamio.widget.MediaController.OnDanmuCloseListener;
import io.vov.vitamio.widget.MediaController.OnHiddenListener;
import io.vov.vitamio.widget.MediaController.OnShownListener;
import io.vov.vitamio.widget.VideoView.OnPlayStateListener;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class VideoActivity extends Activity {
	private VideoView vvTest;
	private RelativeLayout rlTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		initView();
		initData();
	}

	private void initView() {
		vvTest = (VideoView) findViewById(R.id.vv_test);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_video_title);
	}

	private void initData() {
		if (!LibsChecker.checkVitamioLibs(this)) {
			return;
		}
		Uri uri = Uri.parse("http://192.168.0.93:8080/1.flv");
		vvTest.setVideoURI(uri);
		MediaController mediaController = new MediaController(this);

		vvTest.setMediaController(mediaController);
		vvTest.requestFocus();
		vvTest.setOnPlayStateListener(new OnPlayStateListener() {
			@Override
			public void playStateListener(int state) {
				System.out.println(state);
			}
		});
		mediaController.setOnShownListener(new OnShownListener() {

			@Override
			public void onShown() {
				rlTitle.setVisibility(View.VISIBLE);
			}
		});
		mediaController.setOnHiddenListener(new OnHiddenListener() {

			@Override
			public void onHidden() {
				rlTitle.setVisibility(View.GONE);
			}
		});
		mediaController.setOnDanmuAddListener(new OnDanmuAddListener() {
			@Override
			public void DanmuAdd() {
				System.out.println("添加弹幕");
			}
		});
		mediaController.setOnDanmuCloseListener(new OnDanmuCloseListener() {
			@Override
			public void DanmuClose() {
				System.out.println("关闭弹幕");
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}
}
