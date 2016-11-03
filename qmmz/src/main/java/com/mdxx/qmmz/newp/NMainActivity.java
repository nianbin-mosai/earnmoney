package com.mdxx.qmmz.newp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.mdxx.qmmz.Configure;
import com.mdxx.qmmz.EventMessage;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.Configs;
import com.mdxx.qmmz.utils.FileUtils;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.k.e;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import net.youmi.android.AdManager;
import net.youmi.android.offers.EarnPointsOrderInfo;
import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.OffersBrowserConfig;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsEarnNotify;
import net.youmi.android.offers.PointsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class NMainActivity extends BaseActivity implements OnClickListener ,PointsChangeNotify, PointsEarnNotify {
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public List<TextView> textList = new ArrayList<TextView>();
	private RelativeLayout home_layout;
	private RelativeLayout rank_layout;
//	private RelativeLayout zhuan_layout;
	private RelativeLayout dui_layout;
	private RelativeLayout me_layout;
	public int position;
	private int currentTab;
	private ImageView sy_image;
	private ImageView ph_image;
//	private ImageView z_image;
	private ImageView dh_image;
	private ImageView wo_image;
	private long mExitTime;
	private PushAgent mPushAgent;
	private Dialog logindialog;
	private AlertDialog dialog_yaoqing;
	private final String loginurl = InterfaceTool.ULR + "user/login";
	private final String yaoqingurl = InterfaceTool.ULR + "user/yaoqing";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nmain);
//		PgyCrashManager.register(this);// 蒲公英
		String channel1 = FileUtils.getChannel1(this);
		issim();
//		mPushAgent = PushAgent.getInstance(this);
//		mPushAgent.enable();
//		initguanggao();
//		youmeng();
		initUI();
		initYoumi();
		initTabData();
//		islogin();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		PgyFeedbackShakeManager.register(NMainActivity.this, false);
//		MobclickAgent.onResume(this);
//
//		// 版本检测方式2：带更新回调监听
//		PgyUpdateManager.register(NMainActivity.this,
//				new UpdateManagerListener() {
//					@Override
//					public void onUpdateAvailable(final String result) {
//						final AppBean appBean = getAppBeanFromString(result);
//
//						new AlertDialog.Builder(NMainActivity.this)
//								.setTitle("更新")
//								.setMessage("")
//								.setNegativeButton("确定",
//										new DialogInterface.OnClickListener() {
//
//											@Override
//											public void onClick(
//													DialogInterface dialog,
//													int which) {
//												startDownloadTask(
//														NMainActivity.this,
//														appBean.getDownloadURL());
//											}
//										}).show();
//
//					}
//
//					@Override
//					public void onNoUpdateAvailable() {
////						Toast.makeText(MainActivity.this, "已经是最新版本",
////								Toast.LENGTH_SHORT).show();
//					}
//				});
	}
	
	public void onPause() {
		super.onPause();
//		PgyFeedbackShakeManager.unregister();
//		MobclickAgent.onPause(this);
	}
	
	private void initUI() {
		sy_image = (ImageView) findViewById(R.id.sy_image);
		ph_image = (ImageView) findViewById(R.id.ph_image);
//		z_image = (ImageView) findViewById(R.id.z_image);
		dh_image = (ImageView) findViewById(R.id.dh_image);
		wo_image = (ImageView) findViewById(R.id.wo_image);
		TextView sy_txt = (TextView) findViewById(R.id.sy);
		TextView ph_txt = (TextView) findViewById(R.id.ph);
		TextView dh_txt = (TextView) findViewById(R.id.dh);
		TextView wo_txt = (TextView) findViewById(R.id.wo);
		textList.add(sy_txt);
		textList.add(ph_txt);
//		textList.add(null);
		textList.add(dh_txt);
		textList.add(wo_txt);
		home_layout = (RelativeLayout) findViewById(R.id.home_layout);
		rank_layout = (RelativeLayout) findViewById(R.id.rank_layout);
//		zhuan_layout = (RelativeLayout) findViewById(R.id.zhuan_layout);
		dui_layout = (RelativeLayout) findViewById(R.id.dui_layout);
		me_layout = (RelativeLayout) findViewById(R.id.me_layout);
		home_layout.setOnClickListener(this);
		rank_layout.setOnClickListener(this);
//		zhuan_layout.setOnClickListener(this);
		dui_layout.setOnClickListener(this);
		me_layout.setOnClickListener(this);
	}

	private void initTabData() {
		if (fragments.size() > 0) {
			fragments.clear();
		}
		HomeFragment homeFragment = new HomeFragment();
		RankFragment rankFragment = new RankFragment();
//		ZhuanFragment zhuanFragment = new ZhuanFragment();
		DuiFragment duiFragment = new DuiFragment();
		MeFragment meFragment = new MeFragment();
		fragments.add(homeFragment);
		fragments.add(rankFragment);
//		fragments.add(zhuanFragment);
		fragments.add(duiFragment);
		fragments.add(meFragment);

		String p = getIntent().getStringExtra("position");
		if (p == null) {
			p = "0";
		}
		position = Integer.valueOf(p);
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.tab_content, fragments.get(position));
		ft.commit();
		updataTabBg(position);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_layout:
			position = 0;
			break;
		case R.id.rank_layout:
			position = 1;
			break;
//		case R.id.zhuan_layout:
//			position = 2;
//			break;
		case R.id.dui_layout:
			position = 2;
			break;
		case R.id.me_layout:
			position = 3;
			break;
		}
		updataTabBg(position);
		setCheckedChange(position);
	}

	private void setCheckedChange(int pos) {
		Fragment fragment = fragments.get(pos);
		FragmentTransaction ft = obtainFragmentTransaction(pos);
		getCurrentFragment().onPause();
		if (fragment.isAdded()) {
			fragment.onResume();
		} else {
			ft.add(R.id.tab_content, fragment);
		}
		showTab(pos);
		ft.commit();
	}

	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
		}
		return ft;
	}

	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);
			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	private void updataTabBg(int pos) {
		switch (pos) {
		case 0:
			sy_image.setBackgroundResource(R.drawable.home_pre);
			ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
			dh_image.setBackgroundResource(R.drawable.money_normal);
			wo_image.setBackgroundResource(R.drawable.my_normal);
			break;
		case 1:
			sy_image.setBackgroundResource(R.drawable.home_normal);
			ph_image.setBackgroundResource(R.drawable.rank_press);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
			dh_image.setBackgroundResource(R.drawable.money_normal);
			wo_image.setBackgroundResource(R.drawable.my_normal);
			break;
//		case 2:
//			sy_image.setBackgroundResource(R.drawable.home_normal);
//			ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzz);
//			dh_image.setBackgroundResource(R.drawable.money_normal);
//			wo_image.setBackgroundResource(R.drawable.my_normal);
//			break;
		case 2:
			sy_image.setBackgroundResource(R.drawable.home_normal);
			ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
			dh_image.setBackgroundResource(R.drawable.money_press);
			wo_image.setBackgroundResource(R.drawable.my_normal);
			break;
		case 3:
			sy_image.setBackgroundResource(R.drawable.home_normal);
			ph_image.setBackgroundResource(R.drawable.rank_normal);
//			z_image.setBackgroundResource(R.drawable.zzzzzzzz);
			dh_image.setBackgroundResource(R.drawable.money_normal);
			wo_image.setBackgroundResource(R.drawable.my_pre);
			break;

		}
		for (int i = 0; i < fragments.size(); i++) {
//			if (i != 2) {
				if (i == pos) {
					textList.get(i).setTextColor(
							this.getResources().getColor(R.color.blue));
				} else {
					textList.get(i).setTextColor(
							this.getResources().getColor(R.color.black));
				}
//			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 1500) {
			Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fragments.clear();
		// （可选）注销积分监听
		// 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
		PointsManager.getInstance(this).unRegisterNotify(this);

		// （可选）注销积分订单赚取监听
		// 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)进行积分订单赚取监听器注册，那这里必须得注销
		PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);

		// 回收积分广告占用的资源
		OffersManager.getInstance(this).onAppExit();
	}

	private void issim() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (manager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
			Toastshow("请先插入SIM卡");
			finish();
		}
	}
	
	@SuppressWarnings("static-access")
	private void initguanggao() {
		// 有米
		AdManager.getInstance(NMainActivity.this).init(Configs.YMAppId,
				Configs.YMAppSecret, false, false);
		OffersManager.getInstance(NMainActivity.this).onAppLaunch();
//		// 趣米
//		QSdkManager.init(this, "73f3521a4e9297d4", "2d0ba3847ae2cd2a", "",
//				getuseid());
//		QSdkManager.getsdkInstance(this).initOfferAd(this);
		// 聚优
//		YoManage.getInstance(this, "cfb116dc2e79e022982faaf31977c141", "qq")
//				.init();
	}
	
	private void youmeng() {
		UMWXHandler wxHandler = new UMWXHandler(NMainActivity.this, weixinappId,
				weixinappSecret);
		wxHandler.addToSocialSDK();
	}
	
	public boolean islogin() {
		if (!sp.getBoolean("islogin", false)) {
			logindialog = new Dialog(this, R.style.dialog_login_style);
			View inflate = getLayoutInflater().inflate(R.layout.dialog_login,
					null);
			logindialog.setContentView(inflate);
			inflate.findViewById(R.id.btn_login).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							toweixin();
						}

					});
			inflate.findViewById(R.id.btn_canle).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							logindialog.dismiss();
							finish();
						}
					});
			inflate.findViewById(R.id.kefuqq).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							String url = "mqqwpa://im/chat?chat_type=wpa&uin="
									+ getString(R.string.Qkefu) + "&version=1";
							startActivity(new Intent(Intent.ACTION_VIEW, Uri
									.parse(url)));
						}
					});
			logindialog.setCanceledOnTouchOutside(false);
			logindialog.show();
			return false;
		} else {
			return true;
		}
	}
	
	public void toweixin() {
		mController.doOauthVerify(NMainActivity.this, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(NMainActivity.this, "开始授权",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(NMainActivity.this, "授权失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(final Bundle value,
							SHARE_MEDIA platform) {
						mController.getPlatformInfo(NMainActivity.this,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(NMainActivity.this,
												"开始获取资料...", Toast.LENGTH_SHORT)
												.show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											Set<String> keys = info.keySet();
											String headimgurl = "";
											String openid = "";
											String nickname = "";
											for (String key : keys) {
												if (key.equals("headimgurl")) {
													headimgurl = info.get(key)
															.toString();
												} else if (key.equals("openid")) {
													openid = info.get(key)
															.toString();
												} else if (key
														.equals("nickname")) {
													nickname = info.get(key)
															.toString();
												}
											}
											tologin(nickname,
													openid,
													headimgurl,
													String.valueOf(value
															.getCharSequence("access_token")));
										} else {
											Toast.makeText(NMainActivity.this,
													"获取资料失败" + status,
													Toast.LENGTH_SHORT).show();
										}
									}

								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(NMainActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	public void tologin(final String name, final String openid,
			final String headimgurl, final String access_tekon) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("openid", openid);
		params.put("headimgurl", headimgurl);
		params.put("access_token", access_tekon);
		params.put("imei", getIMEI());
		InterfaceTool.Networkrequest(this, queue, m_pDialog, loginurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						closewaite();
						try {
							int code = response.getInt("code");
							if (code == 2) {
								logindialog.dismiss();
								String userid = response.getString("userid");
								String token = response.getString("token");
								sp.edit().putBoolean("islogin", true)
										.putString("userid", userid)
										.putString("token", token).putString("name", name).commit();
								Toastshow("登录成功");
								EventBus.getDefault().post(new EventMessage("getinfo", 2, ""));
								mPushAgent.addAlias(userid, Configure.Project);
							} else if (code == 4) {
								Toastshow("手机已绑定其他微信号");
							} else if (code == 5) {
								Toastshow("不支持模拟器等设备");
							} else if (code == 1) {
								String token = response.getString("token");
								sp.edit().putString("token", token).putString("name", name).commit();
								yaoqing_dilog(response.getString("userid"));
								logindialog.dismiss();
								mPushAgent.addAlias(response.getString("userid"), Configure.Project);
								EventBus.getDefault().post(new EventMessage("getinfo", 1, ""));
							} else {
								Toastshow(response.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (e e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, params);
	}
	
	public void yaoqing_dilog(final String userid) {
		String invitationnum = FileUtils.getChannel1(this);
		if (invitationnum != null && !"".equals(invitationnum)) {
			sendyaoqing(userid, invitationnum);
		} else {
			View inflate = getLayoutInflater().inflate(R.layout.dialog_numview,
					null);
			final EditText edit_num = (EditText) inflate
					.findViewById(R.id.edit_num);
			dialog_yaoqing = new AlertDialog.Builder(this)
					.setTitle("请输入邀请ID")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(inflate)
					.setPositiveButton("确定", new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String yaoqingid = edit_num.getText().toString();
							sendyaoqing(userid, yaoqingid);
						}
					})
					.setNegativeButton("不输入",
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									sendyaoqing(userid, "10000");

								}
							}).create();
			if (!dialog_yaoqing.isShowing()) {
				dialog_yaoqing.show();
			}
		}
	}
	
	public void sendyaoqing(String userid, String yaoqingid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("invitecode", yaoqingid);
		InterfaceTool.Networkrequest(NMainActivity.this, queue, m_pDialog,
				yaoqingurl, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						closewaite();
						try {
							String code = response.getString("code");
							if (code.equals("1")) {
								String userid = response.getString("userid");
								sp.edit().putBoolean("islogin", true)
										.putString("userid", userid).commit();
								Toastshow("注册成功");
							} else {
								Toastshow("注册失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, map);
	}



	private void initYoumi() {
		// 自v6.3.0起，所有其他代码必须在初始化接口调用之后才能生效
		// 初始化接口，应用启动的时候调用(appId, appSecret, isTestModel, isEnableYoumiLog)
		AdManager.getInstance(this).init(Configs.YMAppId, Configs.YMAppSecret, false, true);

		// 有米android 积分墙sdk 5.0.0之后支持定制浏览器顶部标题栏的部分UI
		setOfferBrowserConfig();

		// 如果开发者使用积分墙的服务器回调,
		// 1.需要告诉sdk，现在采用服务器回调
		// 2.建议开发者传入自己系统中用户id（如：邮箱账号之类的）（请限制在50个字符串以内）
		// 3.务必在下面的OffersManager.getInstance(this).onAppLaunch();代码之前声明使用服务器回调

		// OffersManager.getInstance(this).setUsingServerCallBack(true);
		// OffersManager.getInstance(this).setCustomUserId("user_id");

		// 如果使用积分广告，请务必调用积分广告的初始化接口:
		OffersManager.getInstance(this).onAppLaunch();

		// (可选)注册积分监听-随时随地获得积分的变动情况
		PointsManager.getInstance(this).registerNotify(this);

		// (可选)注册积分订单赚取监听（sdk v4.10版本新增功能）
		PointsManager.getInstance(this).registerPointsEarnNotify(this);

		// (可选)设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
		// AdManager.getInstance(this).setIsDownloadTipsDisplayOnNotification(false);

		// (可选)设置安装完成后是否在通知栏显示已安装成功的通知。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
		// AdManager.getInstance(this).setIsInstallationSuccessTipsDisplayOnNotification(false);

		// (可选)设置是否在通知栏显示积分赚取提示。默认为true，标识开启；设置为false则关闭。
		// 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
		// PointsManager.getInstance(this).setEnableEarnPointsNotification(false);

		// (可选)设置是否开启积分赚取的Toast提示。默认为true，标识开启；设置为false这关闭。
		// 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
		// PointsManager.getInstance(this).setEnableEarnPointsToastTips(false);

		// -------------------------------------------------------------------------------------------
		// 积分墙SDK 5.3.0 新增分享任务，下面为新增接口

		// (可选) 获取当前应用的签名md5字符串，可用于申请微信appid时使用
		// 注意：获取是确保应用采用的是发布版本的签名而不是debug签名

		// Log.d("youmi", String.format("包名：%s\n签名md5值：%s", this.getPackageName(),
		//		OffersManager.getInstance(this).getSignatureMd5String()));

		// (重要) 如果开发者需要开启分享任务墙，需要调用下面代码以注册微信appid（这里的appid为贵应用在微信平台上注册获取得到的appid）
		// 1. 微信的appid，请开发者在微信官网上自行注册
		// 2. 如果注册失败(返回false)，请参考/doc/有米AndroidSDK常见问题.html
		boolean isRegisterSuccess = OffersManager.getInstance(this).registerToWx("wxbe86d519b643cf08");
		Toast.makeText(this, String.format("注册微信appid %s", isRegisterSuccess ? "成功" : "失败"), Toast.LENGTH_SHORT).show();

		// 查询积分余额
		// 从5.3.0版本起，客户端积分托管将由 int 转换为 float
		float pointsBalance = PointsManager.getInstance(this).queryPoints();
//		mTextViewPoints.setText("积分余额：" + pointsBalance);
	}
	/**
	 * 设置积分墙浏览器标题栏样式
	 */
	private void setOfferBrowserConfig() {

		// 设置标题栏——标题
		OffersBrowserConfig.getInstance(this).setBrowserTitleText("秒取积分");

		// 设置标题栏——背景颜色（ps：积分墙标题栏默认背景颜色为#FFBB34）
//		OffersBrowserConfig.getInstance(this).setBrowserTitleBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

		// 设置标题栏——是否显示积分墙右上角积分余额区域 true：是 false：否
		OffersBrowserConfig.getInstance(this).setPointsLayoutVisibility(true);

	}

	/**
	 * 积分余额发生变动时，就会回调本方法（本回调方法执行在UI线程中）
	 * <p/>
	 * 从5.3.0版本起，客户端积分托管将由 int 转换为 float
	 */
	@Override
	public void onPointBalanceChange(float pointsBalance) {
//		mTextViewPoints.setText("积分余额：" + pointsBalance);
	}

	/**
	 * 积分订单赚取时会回调本方法（本回调方法执行在UI线程中）
	 */
	@Override
	public void onPointEarn(Context context, EarnPointsOrderList list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		// 遍历订单并且toast提示
		for (int i = 0; i < list.size(); ++i) {
			EarnPointsOrderInfo info = list.get(i);
			Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
