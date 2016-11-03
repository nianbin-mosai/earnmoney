package com.mdxx.qmmz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.mdxx.qmmz.MyApplication;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.newfeature.LoginActivity;
import com.mdxx.qmmz.utils.BitMapUtils;

public class HelloActivity extends BaseActivity {

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final long SPLASH_DELAY_MILLIS = 2500;

	/**
	 * Handler:
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goLogin();
				// goGuide();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};
	public MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello);
		myApp = (MyApplication) getApplication();
		 ImageView mguide_top = (ImageView) findViewById(R.id.guide_top);
		 mguide_top.setImageBitmap(BitMapUtils.readBitMap(
				 
		 getApplicationContext(), R.drawable.ic_hello_bg));



		if (UserPF.getInstance().isFirstRunning()) {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}

	}

	private void goLogin() {
		Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
		HelloActivity.this.startActivity(intent);
		HelloActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(HelloActivity.this, GuideActivity.class);
		HelloActivity.this.startActivity(intent);
		HelloActivity.this.finish();
	}

}
