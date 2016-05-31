package com.donghaowxr.zhihuiwuxi;

import com.donghaowxr.zhihuiwuxi.utils.PerfUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


public class SplashActivity extends Activity {

    private RelativeLayout rlRoot;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlRoot = (RelativeLayout) findViewById(R.id.rlRoot);
        /**
         * 旋转动画
         */
        RotateAnimation rotateAnimation=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        /**
         * 缩放动画
         */
        ScaleAnimation scaleAnimation=new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        /**
         * 渐变动画
         */
        AlphaAnimation alphaAnimation=new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        
        AnimationSet set=new AnimationSet(true);//是否共享加速度
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        /**
         * 开始动画
         */
        rlRoot.startAnimation(set);
        
        set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			/**
			 * 动画结束时，首次跳转到新手引导页，否则跳转到主页
			 */
			@Override
			public void onAnimationEnd(Animation arg0) {
				boolean loginState=PerfUtils.getBoolean(SplashActivity.this, "loginState", true);
				Intent intent;
				if (loginState) {
					intent=new Intent(getApplicationContext(), GuideActivity.class);
					
				}else {
					intent=new Intent(getApplicationContext(), MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
    }
}
