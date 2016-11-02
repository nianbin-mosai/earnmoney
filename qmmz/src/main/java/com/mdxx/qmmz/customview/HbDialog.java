package com.mdxx.qmmz.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.QhbDetailActivity;

public class HbDialog extends Dialog implements
		View.OnClickListener {
	
	String money;
	Context mContext;
	OnClickOk mOnc;
	
	public HbDialog(Context context, String m) {
		super(context, R.style.dialog_login_style);
		
		mContext = context;
		money = m;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hb_dialog_layout);
		TextView moneytxt = (TextView) findViewById(R.id.textView2);
		findViewById(R.id.relativeLayout1).setOnClickListener(this);
		TextView shouqi = (TextView) findViewById(R.id.textView4);
		shouqi.setOnClickListener(this);
		moneytxt.setText("您领取到该红包~,得"+money+"元");
		shouqi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
		findViewById(R.id.imageView1).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relativeLayout1:
			toClick();
			break;
		case R.id.textView4:
			mContext.startActivity(new Intent(mContext, QhbDetailActivity.class));
			break;
		case R.id.imageView1:
			break;
		}
		dismiss();
	}
	
	public interface OnClickOk{
		void onClick();
	};
	public void setOnClickOK(OnClickOk onc){
		mOnc = onc;
	}
	public void toClick(){
		if(mOnc != null){
			mOnc.onClick();
		}
	}
	
}
