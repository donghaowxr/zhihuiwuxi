package com.donghaowxr.zhihuiwuxi;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity {
	@ViewInject(R.id.btn_Menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;
	@ViewInject(R.id.wv_news)
	private WebView wvNews;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		ViewUtils.inject(NewsDetailActivity.this);
		btnMenu.setVisibility(View.GONE);
		btnBack.setVisibility(View.VISIBLE);
		llControl.setVisibility(View.VISIBLE);
		
		wvNews.loadUrl("http://www.baidu.com");
		WebSettings settings=wvNews.getSettings();
		settings.setJavaScriptEnabled(true);
		
		wvNews.setWebViewClient(new WebViewClient(){
			/**
			 * 网页加载开始
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pbLoading.setVisibility(View.VISIBLE);
			}
			
			/**
			 * 网页加载结束
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pbLoading.setVisibility(View.INVISIBLE);
			}
			
			/**
			 * 链接跳转时会进入此方法
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);//强制网页在webview中跳转
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
	}
}
