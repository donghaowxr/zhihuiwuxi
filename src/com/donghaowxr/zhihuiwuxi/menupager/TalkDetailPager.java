package com.donghaowxr.zhihuiwuxi.menupager;

import java.util.ArrayList;
import java.util.Random;
import com.donghaowxr.zhihuiwuxi.R;
import com.donghaowxr.zhihuiwuxi.domain.Talk;
import com.donghaowxr.zhihuiwuxi.domain.TalkBean;
import com.donghaowxr.zhihuiwuxi.domain.TalkBean.WsBean;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TalkDetailPager extends BaseMenuDetailPager {

	private Button btnTalk;
	private ListView lvTalk;
	private StringBuilder sb;
	private ArrayList<Talk> mTalks;
	private TalkAdapter mTalkAdapter;
	private String[] mAnswer = new String[] { "约吗?", "等你哦!!!", "没有更多美女了!",
			"这是最后一张了!", "您老悠着点!", "人家害羞嘛" };
	private int[] mPic = new int[] { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4 };

	public TalkDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.page_talk, null);
		btnTalk = (Button) view.findViewById(R.id.btn_talk);
		lvTalk = (ListView) view.findViewById(R.id.lv_talk);
		// 将“12345678”替换成您申请的 APPID,申请地址:http://www.xfyun.cn
		// 请勿在“=”与 appid之间添加任务空字符或者转义符
		SpeechUtility.createUtility(mActivity, SpeechConstant.APPID + "=576951fb");
		return view;
	}
	
	@Override
	public void initData() {
		mTalks = new ArrayList<Talk>();
		sb = new StringBuilder();
		mTalkAdapter = new TalkAdapter();
		lvTalk.setAdapter(mTalkAdapter);
		btnTalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sb.delete(0, sb.length());
				// 1.创建RecognizerDialog对象
				RecognizerDialog mDialog = new RecognizerDialog(
						mActivity, null);
				// 2.设置accent、language等参数
				mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
				mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
				// 若要将UI控件用于语义理解,必须添加以下参数设置,设置之后onResult回调返回将是语义理解 //结果
				mDialog.setParameter("asr_sch", "1");
				mDialog.setParameter("nlp_version", "2.0");

				// 3.设置回调接口
				mDialog.setListener(new RecognizerDialogListener() {

					@Override
					public void onResult(RecognizerResult result, boolean isLast) {
						processTalk(result.getResultString(), isLast);
					}

					@Override
					public void onError(SpeechError arg0) {
						System.out.println("你没有说话!");
					}
				});
				// 4.显示dialog,接收语音输入
				mDialog.show();
			}
		});
	}
	
	/**
	 * 解析识别的语音
	 * 
	 * @param talk
	 * @param isLast
	 */
	private void processTalk(String talk, boolean isLast) {
		Gson gson = new Gson();
		com.donghaowxr.zhihuiwuxi.domain.TalkBean mTalkBean = gson.fromJson(talk, TalkBean.class);
		ArrayList<WsBean> mWsBeans = mTalkBean.ws;
		for (WsBean wsBean : mWsBeans) {
			sb.append(wsBean.cw.get(0).w);
		}
		if (isLast) {
			Talk mTalk = new Talk();
			mTalk.content = sb.toString();
			mTalk.isAsk = true;
			mTalk.imageId = -1;
			mTalks.add(mTalk);
			mTalkAdapter.notifyDataSetChanged();
			if (sb.toString().contains("你好")) {
				talkAnswer("你好啊！", -1);
			} else if (sb.toString().contains("美女")) {
				Random random = new Random();
				int currentAnswer = random.nextInt(mAnswer.length);
				int currentPic = random.nextInt(mPic.length);
				talkAnswer(mAnswer[currentAnswer], mPic[currentPic]);
			}else if (sb.toString().contains("天王盖地虎")) {
				talkAnswer("小鸡炖蘑菇", R.drawable.m);
			}else {
				talkAnswer("您在说什么?", -1);
			}
		}
	}
	
	/**
	 * 回答
	 * 
	 * @param answer
	 * @param imageId
	 */
	private void talkAnswer(String answer, int imageId) {
		Talk talk = new Talk();
		talk.content = answer;
		talk.imageId = imageId;
		talk.isAsk = false;
		mTalks.add(talk);
		mTalkAdapter.notifyDataSetChanged();
		lvTalk.setSelection(mTalks.size()-1);
		showAnswer(answer);
	}
	
	/**
	 * 合成语音
	 * 
	 * @param answer
	 */
	private void showAnswer(String answer) {
		// 1.创建 SpeechSynthesizer 对象, 第二个参数:本地合成时传 InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(
				mActivity, null);
		// 2.合成参数设置,详见《MSC Reference Manual》SpeechSynthesizer 类
		// 设置发音人(更多在线发音人,用户可参见 附录13.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); // 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量,范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		// 设置合成音频保存位置(可自定义保存位置),保存在“./sdcard/iflytek.pcm”
		// 保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
		// 仅支持保存为 pcm 和 wav 格式,如果不需要保存合成音频,注释该行代码
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
		// "./sdcard/iflytek.pcm");
		mTts.startSpeaking(answer, null);
	}

	public class TalkAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTalks.size();
		}

		@Override
		public Talk getItem(int position) {
			return mTalks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.list_talk_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.tvAsk = (TextView) convertView
						.findViewById(R.id.tv_ask);
				mViewHolder.llAnswer = (LinearLayout) convertView
						.findViewById(R.id.ll_answer);
				mViewHolder.tvAnswer = (TextView) convertView
						.findViewById(R.id.tv_answer);
				mViewHolder.ivAnswer = (ImageView) convertView
						.findViewById(R.id.iv_answer);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			if (mTalks.get(position).isAsk == true) {
				mViewHolder.llAnswer.setVisibility(View.GONE);
				mViewHolder.tvAsk.setVisibility(View.VISIBLE);
				mViewHolder.tvAsk.setText(mTalks.get(position).content);
			} else {
				if (mTalks.get(position).imageId == -1) {
					mViewHolder.tvAsk.setVisibility(View.GONE);
					mViewHolder.llAnswer.setVisibility(View.VISIBLE);
					mViewHolder.tvAnswer.setVisibility(View.VISIBLE);
					mViewHolder.ivAnswer.setVisibility(View.GONE);
					mViewHolder.tvAnswer.setText(mTalks.get(position).content);
				} else {
					mViewHolder.tvAsk.setVisibility(View.GONE);
					mViewHolder.llAnswer.setVisibility(View.VISIBLE);
					mViewHolder.tvAnswer.setVisibility(View.VISIBLE);
					mViewHolder.ivAnswer.setVisibility(View.VISIBLE);
					mViewHolder.tvAnswer.setText(mTalks.get(position).content);
					mViewHolder.ivAnswer
							.setImageResource(mTalks.get(position).imageId);
				}
			}
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvAsk;
		public LinearLayout llAnswer;
		public TextView tvAnswer;
		public ImageView ivAnswer;
	}
}
