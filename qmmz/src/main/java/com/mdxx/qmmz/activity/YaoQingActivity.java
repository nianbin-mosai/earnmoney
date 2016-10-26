package com.mdxx.qmmz.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.os.Bundle;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class YaoQingActivity extends BaseActivity implements OnClickListener {

	private String yaoqingimage = InterfaceTool.ULR + "user/qr?uid=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yao_qing);
		setshare();
		initview();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.yaoqing_layout).setOnClickListener(this);
		findViewById(R.id.erweima_layout).setOnClickListener(this);
		findViewById(R.id.btn_jiaqun).setOnClickListener(this);
		findViewById(R.id.fuzhi_layout).setOnClickListener(this);
		TextView text_Qqun = (TextView) findViewById(R.id.text_Qqun);
		text_Qqun.setText("QQ群：" + getString(R.string.Qqun));
		findViewById(R.id.wode_layout).setOnClickListener(this);
		findViewById(R.id.jiangli_layout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_jiaqun:
			turnQqun();
			break;
		case R.id.yaoqing_layout:
			mController.openShare(YaoQingActivity.this, false);
			break;
		case R.id.erweima_layout:
			showerweima();
			break;
		case R.id.fuzhi_layout:
			String shareUrl = sp.getString("shareUrl", "");
			copyurl(shareUrl);
			break;
		case R.id.wode_layout:
			startActivity(new Intent(YaoQingActivity.this,
					InvitationActivity.class));
			break;
		case R.id.jiangli_layout:
			startActivity(new Intent(YaoQingActivity.this, RewardActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void showerweima() {
		Dialog dialog = new Dialog(this, R.style.dialog_login_style);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		imageLoader.displayImage(yaoqingimage + getuseid(), imageView, options,
				null);
		LayoutParams params = new LayoutParams(500, 500);
		dialog.setContentView(imageView, params);
		dialog.show();
	}

	@Override
	public void copyurl(String url) {
		ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		cmb.setText("我给你发了一个随机红包哟！最高2元！最少0.3元！每天签到捡钱、每天下载广告APP捡钱、邀请好友还可以对半拆红包捡钱！一天捡几十元简直小意思啦！赶紧来吧："+ url);
		Toastshow("已复制到黏贴板");
	}
	
}
