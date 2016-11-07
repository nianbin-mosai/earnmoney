package com.mdxx.qmmz.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.mdxx.qmmz.R;

public class KeFuActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ke_fu);
		initview();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_kefu).setOnClickListener(this);
		findViewById(R.id.btn_Qqun).setOnClickListener(this);
		findViewById(R.id.text_Qkefu).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				copyurl(getString(R.string.Qkefu));
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_Qqun:
			turnQqun();
			break;
		case R.id.btn_kefu:
			String url = "mqqwpa://im/chat?chat_type=wpa&uin="+getString(R.string.Qkefu)+"&version=1";
			startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse(url)));
			break;
		default:
			break;
		}
	}


}
