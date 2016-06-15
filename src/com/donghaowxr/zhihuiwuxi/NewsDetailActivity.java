package com.donghaowxr.zhihuiwuxi;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled") public class NewsDetailActivity extends Activity implements OnClickListener {
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
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		ViewUtils.inject(NewsDetailActivity.this);
		btnMenu.setVisibility(View.GONE);
		btnBack.setVisibility(View.VISIBLE);
		llControl.setVisibility(View.VISIBLE);
		String newUrl=getIntent().getExtras().getString("newsUrl", "");
		
		wvNews.loadUrl(newUrl);
		
		wvNews.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}
		});
		
		
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
				pbLoading.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				
				pbLoading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
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
		WebSettings settings=wvNews.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAppCacheEnabled(false);
	}
	
	/**
	 * 初始化按钮监听事件
	 */
	private void initListener() {
		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击返回新闻列表页
		 */
		case R.id.btn_back:
			finish();
			break;
			/**
			 * 修改页面字体
			 */
		case R.id.btn_textsize:
			showTextSizeDialog();
			break;
			/**
			 * 分享
			 */
		case R.id.btn_share:
			showShare();
			break;
		}
	}
	private int chooseItem;
	private int currentItem=2;
	/**
	 * 显示字体修改对话框
	 */
	private void showTextSizeDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		String[]textSizeArray=new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		builder.setSingleChoiceItems(textSizeArray, currentItem, new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				chooseItem = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings=wvNews.getSettings();
				switch (chooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;
				}
				currentItem=chooseItem;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("微信朋友圈分享sdk测试");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://www.qq.com");
		 // text是分享文本，所有平台都需要这个字段
//		 oks.setText("这是分享的文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/Android/data/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://www.qq.com");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http/://www.qq.com");

		// 启动分享GUI
		 oks.show(this);
		 }
}
