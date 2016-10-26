package com.mdxx.qmmz.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.mdxx.qmmz.Configure;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.mdxx.qmmz.utils.MyVolley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	public ProgressDialog m_pDialog;
	public SharedPreferences sp;
	public RequestQueue queue;
	public DisplayImageOptions options;
	public ImageLoader imageLoader;
	protected TextView text_jifen;
	protected double currmoney;
	protected Tencent mTencent;
	public UMSocialService mController;
	private String moneyurl = InterfaceTool.ULR + "user/getmoney";
	public String weixinappId = "wx2a704314ec7093bd";
	public String weixinappSecret = "426825c299a4ce492314d0d5407da995";
	private final String duibaUrl = InterfaceTool.ULR + "duiba/autologin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = MyVolley.getRequestQueue();
		sp = getSharedPreferences(Configure.Project, 0);
		mTencent = Tencent.createInstance("1105646565", this.getApplication());
		mController = UMServiceFactory.getUMSocialService(Configure.UMSocialService);
		initprogress();
		initUIL();
	}

	private void initUIL() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.follow)
				.showImageOnFail(R.drawable.follow).cacheInMemory(true)
				.cacheOnDisk(true).build();
	}

	private void initprogress() {
		m_pDialog = new ProgressDialog(this);
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 提示信息
		m_pDialog.setMessage("正在努力加载");

		// 设置ProgressDialog 的进度条是否不明确
		m_pDialog.setIndeterminate(false);
	}

	public void Toastshow(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

	public void closewaite() {
		m_pDialog.dismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	protected void getmoney() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue, m_pDialog, moneyurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						closewaite();
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								if (text_jifen != null) {
									currmoney = Double.valueOf(arg0
											.getString("money"));
									text_jifen.setText(currmoney + "");
								}
							} else {
								Toastshow("获取账户积分失败");
								if (text_jifen != null)
									text_jifen.setText("");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, params);
	}

	public String getuseid() {
		return sp.getString("userid", "");
	}

	public String getName() {
		return sp.getString("name", "");
	}
	
	public String getimageurl() {
		return sp.getString("headimgurl", "");
	}

	public String shareTittle ="全民秒赚";
	public String shareContent ="加入全民秒赚，免费红包领不停！";
//	public String shareTittleWX ="哈哈，话费不愁啦！";
//	public String shareContentWX ="太给力了，今天玩了半小时就赚到35元话费，可以充Q币，提现到支付宝，一元提现，一起来玩吧～";
	
	public void setshare() {
		
		getShareUrl();
		
//		mController.setShareContent("好友邀请：" + yaoqingUrl + getuseid()+InterfaceTool.getRandomString(8));
//		// 图片
//		mController.setShareMedia(new UMImage(this, R.drawable.ic_launcher));
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
//				"1105646565", "1qHI5CrwTonwakOh");
//		qZoneSsoHandler.addToSocialSDK();
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		UMWXHandler wxHandler = new UMWXHandler(this, weixinappId, weixinappSecret);
//		wxHandler.addToSocialSDK();
//		UMWXHandler wxCircleHandler = new UMWXHandler(this, weixinappId, weixinappSecret);
//		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.addToSocialSDK();
//		// 设置微信好友分享内容
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		// 设置分享文字
//		weixinContent.setShareContent(shareContent);
//		// 设置title
//		weixinContent.setTitle(shareTittle);
//		// 设置分享内容跳转URL
//		weixinContent.setTargetUrl(yaoqingUrl + getuseid()+InterfaceTool.getRandomString(8));
//		// 设置分享图片
//		weixinContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
//		mController.setShareMedia(weixinContent);
//		// 设置微信朋友圈分享内容
//		CircleShareContent circleMedia = new CircleShareContent();
//		circleMedia.setShareContent(shareContent);
//		// 设置朋友圈title
//		circleMedia.setTitle(shareTittle);
//		circleMedia.setShareImage(new UMImage(this, R.drawable.ic_launcher));
//		circleMedia.setTargetUrl(yaoqingUrl + getuseid()+InterfaceTool.getRandomString(8));
//		mController.setShareMedia(circleMedia);
//		// QQ
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1105646565",
//				"1qHI5CrwTonwakOh");
//		qqSsoHandler.addToSocialSDK();
//		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
//				SHARE_MEDIA.TENCENT);
//		QQShareContent qqShareContent = new QQShareContent();
//		// 设置分享文字
//		qqShareContent.setShareContent(shareContent);
//		// 设置分享title
//		qqShareContent.setTitle(shareTittle);
//		// 设置分享图片
//		qqShareContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
//		// 设置点击分享内容的跳转链接
//		qqShareContent.setTargetUrl(yaoqingUrl + getuseid()+InterfaceTool.getRandomString(8));
//		mController.setShareMedia(qqShareContent);
//		QZoneShareContent qzone = new QZoneShareContent();
//		// 设置分享文字
//		qzone.setShareContent(shareContent);
//		// 设置点击消息的跳转URL
//		qzone.setTargetUrl(yaoqingUrl + getuseid()+InterfaceTool.getRandomString(8));
//		// 设置分享内容的标题
//		qzone.setTitle(shareTittle);
//		// 设置分享图片
//		qzone.setShareImage(new UMImage(this, R.drawable.ic_launcher));
//		mController.setShareMedia(qzone);
//		SinaShareContent sinaContent = new SinaShareContent();
//		sinaContent.setShareContent(shareContent + yaoqingUrl + getuseid());
//		sinaContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
//		mController.setShareMedia(sinaContent);
	}

	private void getShareUrl(){
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue,
				m_pDialog, InterfaceTool.ULR + "user/shaerurl",
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						closewaite();
						try {
							String shareUrl = arg0.getString("url");
							sp.edit().putString("shareUrl", shareUrl).commit();
							mController.setShareContent("好友邀请：" + shareUrl);
							// 图片
							mController.setShareMedia(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(BaseActivity.this,
									"1105646565", "1qHI5CrwTonwakOh");
							qZoneSsoHandler.addToSocialSDK();
							mController.getConfig().setSsoHandler(new SinaSsoHandler());
							UMWXHandler wxHandler = new UMWXHandler(BaseActivity.this, weixinappId, weixinappSecret);
							wxHandler.addToSocialSDK();
							UMWXHandler wxCircleHandler = new UMWXHandler(BaseActivity.this, weixinappId, weixinappSecret);
							wxCircleHandler.setToCircle(true);
							wxCircleHandler.addToSocialSDK();
							// 设置微信好友分享内容
							WeiXinShareContent weixinContent = new WeiXinShareContent();
							// 设置分享文字
							weixinContent.setShareContent(shareContent);
							// 设置title
							weixinContent.setTitle(shareTittle);
							// 设置分享内容跳转URL
							weixinContent.setTargetUrl(shareUrl);
							// 设置分享图片
							weixinContent.setShareImage(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							mController.setShareMedia(weixinContent);
							// 设置微信朋友圈分享内容
							CircleShareContent circleMedia = new CircleShareContent();
							circleMedia.setShareContent(shareContent);
							// 设置朋友圈title
							circleMedia.setTitle(shareTittle);
							circleMedia.setShareImage(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							circleMedia.setTargetUrl(shareUrl);
							mController.setShareMedia(circleMedia);
							// QQ
							UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(BaseActivity.this, "1105646565",
									"1qHI5CrwTonwakOh");
							qqSsoHandler.addToSocialSDK();
							mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
									SHARE_MEDIA.TENCENT);
							QQShareContent qqShareContent = new QQShareContent();
							// 设置分享文字
							qqShareContent.setShareContent(shareContent);
							// 设置分享title
							qqShareContent.setTitle(shareTittle);
							// 设置分享图片
							qqShareContent.setShareImage(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							// 设置点击分享内容的跳转链接
							qqShareContent.setTargetUrl(shareUrl);
							mController.setShareMedia(qqShareContent);
							QZoneShareContent qzone = new QZoneShareContent();
							// 设置分享文字
							qzone.setShareContent(shareContent);
							// 设置点击消息的跳转URL
							qzone.setTargetUrl(shareUrl);
							// 设置分享内容的标题
							qzone.setTitle(shareTittle);
							// 设置分享图片
							qzone.setShareImage(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							mController.setShareMedia(qzone);
							SinaShareContent sinaContent = new SinaShareContent();
							sinaContent.setShareContent(shareContent + shareUrl);
							sinaContent.setShareImage(new UMImage(BaseActivity.this, R.drawable.ic_launcher));
							mController.setShareMedia(sinaContent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					};
				}, params);
	}
	
	
	public void getdbaUrl() {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("imei", getIMEI());
		InterfaceTool.Networkrequest(this, queue, m_pDialog, duibaUrl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						closewaite();
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String url = arg0.getString("url");
								Intent intent = new Intent(BaseActivity.this,
										CreditActivity.class);
								intent.putExtra("navColor", "#0acbc1"); // 配置导航条的背景颜色，请用#ffffff长格式。
								intent.putExtra("titleColor", "#ffffff"); // 配置导航条标题的颜色，请用#ffffff长格式。
								intent.putExtra("url", url);
								startActivity(intent);
							} else {
								Toastshow("获取失败：错误代码" + code);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, params);
	}

	// 获取IMET
	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}
	public void copyurl(String url) {
		ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		cmb.setText("我给你发了一个随机红包哟！最高2元！最少0.3元！每天签到捡钱、每天下载广告APP捡钱、邀请好友还可以对半拆红包捡钱！"+url);
		Toastshow("已复制到黏贴板");
	}
	public void turnQqun() {
		String qqurl = "http://jq.qq.com/?_wv=1027&k=40EDgxt";
		Intent intent = new Intent(this,WebActivity.class);
		intent.putExtra("url", qqurl);
		startActivity(intent);
	}
}
