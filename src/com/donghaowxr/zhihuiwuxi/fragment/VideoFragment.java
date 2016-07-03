package com.donghaowxr.zhihuiwuxi.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.widget.MediaController.OnDanmuAddListener;
import io.vov.vitamio.widget.MediaController.OnDanmuCloseListener;
import io.vov.vitamio.widget.MediaController.OnHiddenListener;
import io.vov.vitamio.widget.MediaController.OnScreenChangeListener;
import io.vov.vitamio.widget.MediaController.OnShownListener;
import io.vov.vitamio.widget.VideoView.OnPlayStateListener;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.ui.widget.DanmakuView;
import com.donghaowxr.zhihuiwuxi.MainVideoActivity;
import com.donghaowxr.zhihuiwuxi.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VideoFragment extends BaseFragment implements OnClickListener {

	private VideoView videoView;
	private RelativeLayout rlTitle;
	private TextView tvTitle;
	private ImageButton btnBack;
	private LinearLayout llDanmuAdd;
	private Button btnSendDanmu;
	private DanmakuView mDanmakuView;
	private EditText etSendDanmu;
	private DanmakuContext mContext;
	private BaseDanmakuParser mParser;
	private boolean state = false;

	private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
		private Drawable mDrawable;

		@Override
		public void releaseResource(BaseDanmaku danmaku) {
		}

		@Override
		public void prepareDrawing(final BaseDanmaku danmaku,
				boolean fromWorkerThread) {
			if (danmaku.text instanceof Spanned) {
				ExecutorService cachedThreadPool = Executors
						.newCachedThreadPool();
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						String url = "http://192.168.0.93:8080/tomcat.png";
						InputStream inputStream = null;
						Drawable drawable = mDrawable;
						if (drawable == null) {
							try {
								URLConnection urlConnection = new URL(url)
										.openConnection();
								inputStream = urlConnection.getInputStream();
								drawable = BitmapDrawable.createFromStream(
										inputStream, "bitmap");
								mDrawable = drawable;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								IOUtils.closeQuietly(inputStream);
							}
						}
						if (drawable != null) {
							drawable.setBounds(0, 0, 100, 100);
							SpannableStringBuilder spannable = createSpannable(
									drawable, etSendDanmu.getText().toString());
							danmaku.text = spannable;
							if (mDanmakuView != null) {
								mDanmakuView.invalidateDanmaku(danmaku, false);
							}
							return;
						}
					}
				});
			}
		}
	};
	private MediaController mediaController;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_video, null);
		videoView = (VideoView) view.findViewById(R.id.vv_test);
		rlTitle = (RelativeLayout) view.findViewById(R.id.rl_video_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_video_title);
		btnBack = (ImageButton) view.findViewById(R.id.btn_video_back);
		llDanmuAdd = (LinearLayout) view.findViewById(R.id.ll_danmu_add);
		btnSendDanmu = (Button) view.findViewById(R.id.btn_send_danmu);
		mDanmakuView = (DanmakuView) view.findViewById(R.id.sv_danmaku);
		etSendDanmu = (EditText) view.findViewById(R.id.et_send_danmu);
		return view;
	}

	@Override
	public void initData() {
		setTitle();
		setScreenState();
		btnBack.setOnClickListener(this);
		llDanmuAdd.setOnClickListener(this);
		btnSendDanmu.setOnClickListener(this);
		initDanmu();
		initVideo();
	}

	public void setTitle() {
		MainVideoActivity activity = (MainVideoActivity) mActivity;
		String title = activity.getVideoTitle();
		tvTitle.setText(title);
	}

	private void initVideo() {
		if (!LibsChecker.checkVitamioLibs(mActivity)) {
			return;
		}
		Uri uri = Uri.parse("http://10.0.0.11:8080/1.flv");
		videoView.setVideoURI(uri);
		mediaController = new MediaController(mActivity);

		videoView.setMediaController(mediaController);
		videoView.requestFocus();
		videoView.setOnPlayStateListener(new OnPlayStateListener() {
			@Override
			public void playStateListener(int state) {
				if (state == videoView.STATE_PLAYING) {
					mDanmakuView.resume();
				}
				if (state == videoView.STATE_PAUSED) {
					mDanmakuView.pause();
				}
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
				llDanmuAdd.setVisibility(View.VISIBLE);
				videoView.pause();
			}
		});
		mediaController.setOnDanmuCloseListener(new OnDanmuCloseListener() {
			@Override
			public void DanmuClose() {
				if (mDanmakuView.isShown()) {
					mDanmakuView.hide();
				} else {
					mDanmakuView.show();
				}
			}
		});
		mediaController.setOnScreenChangeListener(new OnScreenChangeListener() {
			@Override
			public void ScreenChange() {
				toggle();
			}
		});
	}
	
	public void changeScreenName(String name){
		mediaController.changeScreenName(name);
	}

	private void initDanmu() {
		// 设置滚动弹幕最大显示5行
		HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
		maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5);

		HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
		overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
		overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

		mContext = DanmakuContext.create();

		// 设置参数
		mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_SHADOW, 0);// 设置描边样式
		mContext.setDuplicateMergingEnabled(true);// 是否启用合并重复弹幕
		mContext.setScrollSpeedFactor(1.2f);// 弹幕滚动速度
		mContext.setScaleTextSize(1.2f);
		// mContext.setCacheStuffer(new SpannedCacheStuffer(),
		// mCacheStufferAdapter); //设置图文混排模式
		mContext.setCacheStuffer(new SpannedCacheStuffer(),
				mCacheStufferAdapter);
		mContext.setMaximumLines(maxLinesPair);
		mContext.preventOverlapping(overlappingEnablePair);// 设置防止弹幕重叠，null为允许重叠
		// mParser = createParser(this.getResources().openRawResource(
		// R.raw.comments));
		mParser = createParser(null);
		mDanmakuView
				.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
					@Override
					public void prepared() {
						mDanmakuView.start();
					}

					@Override
					public void updateTimer(DanmakuTimer timer) {
					}

					@Override
					public void danmakuShown(BaseDanmaku danmaku) {
					}

					@Override
					public void drawingFinished() {
					}
				});

		mDanmakuView.prepare(mParser, mContext);
		// mDanmakuView.showFPS(true);
		mDanmakuView.enableDanmakuDrawingCache(true);
		mDanmakuView.show();
	}

	/**
	 * 创建解析器对象，解析输入流
	 * 
	 * @param stream
	 * @return
	 */
	private BaseDanmakuParser createParser(InputStream stream) {

		if (stream == null) {
			return new BaseDanmakuParser() {

				@Override
				protected Danmakus parse() {
					return new Danmakus();
				}
			};
		}
		ILoader loader = DanmakuLoaderFactory
				.create(DanmakuLoaderFactory.TAG_BILI);// xml解析
		// ILoader
		// loader=DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN)//json格式解析

		try {
			loader.load(stream);
		} catch (IllegalDataException e) {
			e.printStackTrace();
		}
		BaseDanmakuParser parser = new BiliDanmukuParser();
		IDataSource<?> dataSource = loader.getDataSource();
		parser.load(dataSource);
		return parser;
	}

	/**
	 * 创建图文混排模式
	 */
	private SpannableStringBuilder createSpannable(Drawable drawable,
			String text) {
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
				text);
		ImageSpan span = new ImageSpan(drawable);// ImageSpan.ALIGN_BOTTOM);
		spannableStringBuilder.setSpan(span, 0, text.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spannableStringBuilder.append(text);
		spannableStringBuilder.setSpan(0, 0, spannableStringBuilder.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spannableStringBuilder;
	}

	/**
	 * 添加弹幕文本
	 * 
	 * @param islive
	 * @param text
	 */
	private void addDanmaku(boolean islive, String text) {
		BaseDanmaku danmaku = mContext.mDanmakuFactory
				.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
		if (danmaku == null || mDanmakuView == null) {
			return;
		}
		// for(int i=0;i<100;i++){
		// }
		// danmaku.text = text + System.nanoTime();
		danmaku.text = text;
		danmaku.padding = 5;
		danmaku.priority = 0; // 可能会被各种过滤器过滤并隐藏显示
		danmaku.isLive = islive;
		danmaku.time = mDanmakuView.getCurrentTime() + 1200;
		danmaku.textSize = 20f * (mParser.getDisplayer().getDensity() - 0.6f);
		// danmaku.textSize=25f;
		danmaku.textColor = Color.YELLOW;
		danmaku.textShadowColor = Color.WHITE;
		// danmaku.underlineColor = Color.GREEN;
		// danmaku.borderColor = Color.GREEN;//边框颜色，0表示无边框
		danmaku.borderColor = 0;
		mDanmakuView.addDanmaku(danmaku);
	}

	/**
	 * 添加图文混排弹幕
	 * 
	 * @param islive
	 * @param text
	 */
	private void addDanmakuShowTextAndImage(boolean islive, String text) {
		BaseDanmaku danmaku = mContext.mDanmakuFactory
				.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		drawable.setBounds(0, 0, 100, 100);
		SpannableStringBuilder spannable = createSpannable(drawable, text);
		danmaku.text = spannable;
		danmaku.padding = 5;
		danmaku.priority = 1; // 一定会显示, 一般用于本机发送的弹幕
		danmaku.isLive = islive;
		danmaku.time = mDanmakuView.getCurrentTime() + 1200;
		danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
		danmaku.textColor = Color.YELLOW;
		danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
		danmaku.underlineColor = 0;
		mDanmakuView.addDanmaku(danmaku);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_video_back:
			mActivity.finish();
			break;
		case R.id.ll_danmu_add:
			llDanmuAdd.setVisibility(View.GONE);
			videoView.start();
			break;
		case R.id.btn_send_danmu:
			addDanmaku(true, etSendDanmu.getText().toString());
			llDanmuAdd.setVisibility(View.GONE);
			videoView.start();
			break;
		}
	}

	private void setScreenState() {
		MainVideoActivity activity = (MainVideoActivity) mActivity;
		SlidingMenu slidingMenu = activity.getSlidingMenu();
		if (state) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	public void toggle() {
		MainVideoActivity activity = (MainVideoActivity) mActivity;
		SlidingMenu slidingMenu = activity.getSlidingMenu();
		slidingMenu.toggle();
	}

}
