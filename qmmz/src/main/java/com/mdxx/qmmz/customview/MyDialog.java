package com.mdxx.qmmz.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.AdvertisementActivity;
import com.mdxx.qmmz.activity.QhbDetailActivity;
import com.mdxx.qmmz.activity.YaoQingActivity;

public class MyDialog extends Dialog implements View.OnClickListener{

	String mcontent;
	int mtype;
	Context mcontext;
	public MyDialog(Context context,String con, int type) {
		super(context,R.style.dialog_login_style);
		mcontent = con;
		mtype = type;
		mcontext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		TextView content = (TextView) findViewById(R.id.textView2);
		RelativeLayout go_layout = (RelativeLayout) findViewById(R.id.go_layout);
		go_layout.setOnClickListener(this);
		TextView btntext = (TextView) findViewById(R.id.textView11);
		content.setText(mcontent);
		if(mtype == 1 || mtype == 3){
			btntext.setText("去做任务赚钱");
		}else{
			btntext.setText("发红包给好友");
		}
		
		TextView shouqi = (TextView) findViewById(R.id.textView3);
		shouqi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		shouqi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_layout:
			if(mtype == 1 || mtype == 3){
				mcontext.startActivity(new Intent(mcontext,
						AdvertisementActivity.class));
			}else{
				mcontext.startActivity(new Intent(mcontext,
						YaoQingActivity.class));
			}
			break;
		case R.id.textView3:
			mcontext.startActivity(new Intent(mcontext,
					QhbDetailActivity.class));
			break;
		}
		dismiss();
	}
}
