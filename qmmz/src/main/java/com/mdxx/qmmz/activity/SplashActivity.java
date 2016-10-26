package com.mdxx.qmmz.activity;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.R.layout;
import com.mdxx.qmmz.newp.NMainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_splash);
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this,NMainActivity.class));
				finish();
			}
		}, 1500);
	}

	

}
