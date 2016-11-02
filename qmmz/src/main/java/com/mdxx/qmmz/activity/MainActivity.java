package com.mdxx.qmmz.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.BannerBean;
import com.mdxx.qmmz.Configure;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.adapter.PullAdapter;
import com.mdxx.qmmz.customview.AutoScrollTextView;
import com.mdxx.qmmz.utils.FileUtils;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.newqm.pointwall.QSdkManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.k.e;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.yow.YoManage;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseActivity implements OnClickListener {
	private long mExitTime;
	private Animation loadAnimation;
	private ImageView refresh_date;
	private PullToRefreshListView pullListView;
	private TextView textuser_id;
	private ImageView user_icon;
	private TextView textresidue;
	private TextView textjifen;
	private TextView textall_money;
	private TextView textinvite;
	private ListView mListView;
	private int[] mDrawables = new int[] { R.drawable.chip,
			R.drawable.icon_yaoqing, R.drawable.image_qiandao,
			R.drawable.exchange };
	private String[] mTitle = new String[] { "开始赚钱", "签到红包", "邀请红包", "兑换提现" };
	private String[] mMessage = new String[] { "做任务赚钱", "签到领红包", "五级好友奖励",
			"一元提现秒到" };
	private Dialog logindialog;
	private final String loginurl = InterfaceTool.ULR + "user/login";
	private final String yaoqingurl = InterfaceTool.ULR + "user/yaoqing";
	private final String sendQQurl = InterfaceTool.ULR + "user/bindqq";
	private final String Qdburl = InterfaceTool.ULR + "user/qiandao";
	private final String infourl = InterfaceTool.ULR + "user/account";
	private final String banner_url = InterfaceTool.ULR + "banner/index";
	private View layout_huodong;
	private PushAgent mPushAgent;
	private AlertDialog dialog_qq;
	private AlertDialog dialog_yaoqing;
	private AutoScrollTextView autoText_gonggao;
	private TextView text_neirong;
	private Dialog sharetoqd_dialog;
	private BannerBean bannerBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PgyCrashManager.register(this);// 蒲公英
		String channel1 = FileUtils.getChannel1(this);
		issim();
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		initguanggao();
		youmeng();
		initpull();
		getinfo();

	}

	private final String noticeurl = InterfaceTool.ULR + "gonggao/info";
	private View layoutStatus;
	private ImageView banner;

	private void getnotices() {
		layoutStatus.setVisibility(View.GONE);
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue, null, noticeurl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							Log.e("main", "" + arg0.toString());
							String code = arg0.getString("code");
							if (code.equals("1")) {
								JSONArray jsonArray = arg0.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = (JSONObject) jsonArray
											.get(i);

									String status = jsonObject
											.getString("status");
									if (status.equals("1")) {
										Log.e("layoutStatus", "VISIBLE");
										layoutStatus
												.setVisibility(View.VISIBLE);
									}

								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pullListView.onRefreshComplete();

					}
				}, params);
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
		AdManager.getInstance(MainActivity.this).init("80348d0604765fff",
				"56a9137c34d7d1d7", false, false);
		OffersManager.getInstance(MainActivity.this).onAppLaunch();
		// 趣米
		QSdkManager.init(this, "73f3521a4e9297d4", "2d0ba3847ae2cd2a", "",
				getuseid());
		QSdkManager.getsdkInstance(this).initOfferAd(this);
		// 聚优
		YoManage.getInstance(this, "cfb116dc2e79e022982faaf31977c141", "qq")
				.init();
	}

	private void initpull() {

		pullListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		mListView = pullListView.getRefreshableView();
		mListView.setDivider(null);
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (islogin()) {
					getinfo();
				} else {
					pullListView.onRefreshComplete();
				}
			}
		});
		mListView.setAdapter(new PullAdapter(MainActivity.this, mDrawables,
				mTitle, mMessage));
		initAddHeadView();
//		initAddFootView();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 2:
					if (islogin()) {
						if ("".equals(sp.getString("token", ""))) {
							sp.edit().putBoolean("islogin", false).commit();
							islogin();
							return;
						}
						startActivity(new Intent(MainActivity.this,
								AdvertisementActivity.class));
					}
					break;
				case 3:
					if (islogin()) {
						if ("".equals(sp.getString("token", ""))) {
							sp.edit().putBoolean("islogin", false).commit();
							islogin();
							return;
						}
						getqdurl();
					}
					break;
				case 4:
					if (islogin()) {
						if ("".equals(sp.getString("token", ""))) {
							sp.edit().putBoolean("islogin", false).commit();
							islogin();
							return;
						}
						startActivity(new Intent(MainActivity.this,
								YaoQingActivity.class));
					}
					break;

				case 5:
					if (islogin()) {
						if ("".equals(sp.getString("token", ""))) {
							sp.edit().putBoolean("islogin", false).commit();
							islogin();
							return;
						}
						getdbaUrl();
					}
					break;

				}
			}

		});
		refresh_date = (ImageView) findViewById(R.id.refresh_date);
		refresh_date.setOnClickListener(this);
		loadAnimation = AnimationUtils.loadAnimation(this,
				R.anim.refresh_date_anim);
	}

//	private void initAddFootView() {
//		View view = getLayoutInflater().inflate(R.layout.foot_item, null);
//		banner = (ImageView) view.findViewById(R.id.imageView1);
//		banner.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this, WebActivity.class);
//				intent.putExtra("url", bannerBean.getUrl());
//				startActivity(intent);
//			}
//		});
//		mListView.addFooterView(view);
//	}

	private void getqdurl() {
		Map params = new HashMap<String, String>();
		InterfaceTool.Networkrequest(this, queue, m_pDialog, InterfaceTool.ULR
				+ "user/qdhongdong", new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				closewaite();
				try {
					String code = arg0.getString("code");
					if (code.equals("1")) {
						String imageur = arg0.getString("imageurl");
						String url = arg0.getString("url");
						showsharetoqd(imageur, url);
					} else {
						Toastshow("获取签到信息失败");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}, params);
	}

	private void youmeng() {
		UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, weixinappId,
				weixinappSecret);
		wxHandler.addToSocialSDK();
	}

	private boolean islogin() {
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
							// tologin("", "", "", "");
						}

					});
			inflate.findViewById(R.id.btn_canle).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							logindialog.dismiss();
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
			logindialog.show();
			return false;
		} else {
			return true;
		}
	}

	private void showsharetoqd(String iamgeurl, final String url) {
		setshare();
		sharetoqd_dialog = new Dialog(this, R.style.dialog_login_style);
		View inflate = getLayoutInflater().inflate(
				R.layout.dialog_qiandao_share, null);
		ImageView image_guanggao = (ImageView) inflate
				.findViewById(R.id.image_guanggao);
		imageLoader.displayImage(iamgeurl, image_guanggao, options, null);
		image_guanggao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, WebActivity.class)
						.putExtra("url", url));
			}
		});
		inflate.findViewById(R.id.layout_qqkj).setOnClickListener(this);
		inflate.findViewById(R.id.layout_pyq).setOnClickListener(this);
		inflate.findViewById(R.id.layout_wb).setOnClickListener(this);
		sharetoqd_dialog.setContentView(inflate);
		if (sharetoqd_dialog != null && !sharetoqd_dialog.isShowing())
			sharetoqd_dialog.show();
	}

	private void tologin(final String name, final String openid,
			final String headimgurl, final String access_tekon) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("openid", openid);
		params.put("headimgurl", headimgurl);
		params.put("access_token", access_tekon);
		params.put("imei", getIMEI());
		// params.put("name", "测试");
		// params.put("openid", "okSLFw2250zKuxodCPcB-Pnr3-E8");
		// params.put("headimgurl", "http://wx.qlogo.cn/mmopen/");
		// params.put("access_token", "Gi4TxbC0C44FdnHqjTbgV");
		// params.put("imei","3546852365523");
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
								textuser_id.setText("ID:" + userid);
								sp.edit().putBoolean("islogin", true)
										.putString("userid", userid)
										.putString("token", token).putString("name", name).commit();
								Toastshow("登录成功");
								getinfo();
							} else if (code == 4) {
								Toastshow("手机已绑定其他微信号");
							} else if (code == 5) {
								Toastshow("不支持模拟器等设备");
							} else if (code == 1) {
								String token = response.getString("token");
								sp.edit().putString("token", token).putString("name", name).commit();
								yaoqing_dilog(response.getString("userid"));
								logindialog.dismiss();
							} else {
								Toastshow(response.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, params);
	}

	private void getinfo() {
		getnotices();
		if (islogin()) {
			try {
				mPushAgent.addAlias(getuseid(), Configure.Project);
			} catch (e e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map map = new HashMap<String, String>();
			textuser_id.setText("ID：" + getuseid());
			map.put("userid", getuseid());
			InterfaceTool.Networkrequest(this, queue, m_pDialog, infourl,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							closewaite();
							try {
								String code = response.getString("code");
								if (code.equals("1")) {
									JSONObject data = response
											.getJSONObject("data");
									String usemoney = data
											.getString("usemoney");
									String jfusemoney = data
											.getString("jfusemoney");
									String allmoney = data
											.getString("allmoney");
									String invitenum = data
											.getString("invitenum");
									String headimgurl = data
											.getString("headimgurl");
									String qq = data.getString("qq");
									String ishownotice = data
											.getString("ishownotice");
									String noticetext = data
											.getString("noticetext");
									setgonggao(ishownotice, noticetext);
									String hongdongset = data
											.getString("hongdongset");
									layout_huodong.setVisibility(hongdongset
											.equals("1") ? View.VISIBLE
											: View.GONE);
									textinvite.setText("共邀请" + invitenum + "人");
									textall_money.setText("共收入" + allmoney
											+ "元");
									textjifen.setText("=" + jfusemoney + "积分");
									textresidue.setText(usemoney);
									sp.edit()
											.putString("headimgurl", headimgurl)
											.commit();
									imageLoader.displayImage(headimgurl,
											user_icon, options, null);
									// if (qq.length() == 0) {
									// setQQ();
									// }
									getBannerImage();
								} else if (code.equals("9")) {
									sp.edit().putBoolean("islogin", false)
											.commit();
									islogin();
								} else {
									Toastshow(response.getString("msg"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							pullListView.onRefreshComplete();
						}
					}, map);
		}
	}

	private void getBannerImage() {
		Map map = new HashMap<String, String>();
		map.put("type", "1");
		InterfaceTool.Networkrequest(this, queue, m_pDialog, banner_url,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						closewaite();
						try {
							int code = response.getInt("code");
							if (code == 1) {
								JSONArray data = response.getJSONArray("data");
								if (data != null) {
									JSONObject jsonObject = data
											.getJSONObject(0);
									bannerBean = new BannerBean();
									bannerBean.setUrl(jsonObject
											.getString("url"));
									bannerBean.setImageurl(jsonObject
											.getString("imageurl"));
									imageLoader.displayImage(
											bannerBean.getImageurl(), banner,
											options, null);
								}
							} else {
								Toastshow(response.getString("msg"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, map);
	}

	private void initAddHeadView() {
		View view = getLayoutInflater().inflate(R.layout.pullrefresh_headview,
				null);

		layoutStatus = view.findViewById(R.id.layout_status);
		textuser_id = (TextView) view.findViewById(R.id.user_id);
		user_icon = (ImageView) view.findViewById(R.id.user_icon);
		textresidue = (TextView) view.findViewById(R.id.residue);
		autoText_gonggao = (AutoScrollTextView) view
				.findViewById(R.id.autoText_gonggao);
		textjifen = (TextView) view.findViewById(R.id.jifen);
		textall_money = (TextView) view.findViewById(R.id.all_money);
		textall_money.setOnClickListener(this);
		layout_huodong = view.findViewById(R.id.layout_huodong);
		layout_huodong.setOnClickListener(this);
		// view.findViewById(R.id.btn_fl).setOnClickListener(this);
		textinvite = (TextView) view.findViewById(R.id.invite);
		textinvite.setOnClickListener(this);
		TextView gonggao = (TextView) view.findViewById(R.id.gonggao);
		gonggao.setOnClickListener(this);
		TextView paihang = (TextView) view.findViewById(R.id.paihang);
		paihang.setOnClickListener(this);
		RelativeLayout kefu_layout = (RelativeLayout) view
				.findViewById(R.id.kefu_layout);
		kefu_layout.setOnClickListener(this);
//		RelativeLayout qhb_layout = (RelativeLayout) view
//				.findViewById(R.id.qhb_layout);
//		qhb_layout.setOnClickListener(this);
		mListView.addHeaderView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refresh_date:
			refresh_date.startAnimation(loadAnimation);
			getinfo();
			break;
		case R.id.all_money:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this,
						DetailActivity.class));
			}
			break;
		case R.id.invite:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this,
						InvitationActivity.class));
			}
			break;
		case R.id.gonggao:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this,
						GonggaoActivity.class));
			}
			break;
		case R.id.paihang:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this,
						PaihangActivity.class));
			}
			break;
		case R.id.kefu_layout:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this, KeFuActivity.class));
			}
			break;
//		case R.id.qhb_layout:
//			if (islogin()) {
//				if ("".equals(sp.getString("token", ""))) {
//					sp.edit().putBoolean("islogin", false).commit();
//					islogin();
//					return;
//				}
//				startActivity(new Intent(MainActivity.this, QhbActivity.class));
//			}
//			break;
		case R.id.layout_huodong:
			if (islogin()) {
				if ("".equals(sp.getString("token", ""))) {
					sp.edit().putBoolean("islogin", false).commit();
					islogin();
					return;
				}
				startActivity(new Intent(MainActivity.this,
						NoticeActivity.class));
			}
			break;
		case R.id.layout_qqkj:
			mController.postShare(this, SHARE_MEDIA.QZONE,
					new MysnspostListener());
			break;
		case R.id.layout_wb:
			mController
					.postShare(this, SHARE_MEDIA.QQ, new MysnspostListener());
			break;
		case R.id.layout_pyq:
			mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE,
					new MysnspostListener());
			break;
		// case R.id.btn_fl:
		// if (islogin()) {
		// startActivity(new Intent(MainActivity.this, WebActivity.class)
		// .putExtra("url", "http://www.2kxz.com/jiedaibao/"));
		// }
		// break;
		default:
			break;
		}

	}

	private void setgonggao(String ishow, String text) {
		if (ishow.equals("0")) {
			autoText_gonggao.setVisibility(View.GONE);
		} else if (ishow.equals("1")) {
			autoText_gonggao.setVisibility(View.VISIBLE);
			autoText_gonggao.setText(text);
			autoText_gonggao.init(getWindowManager());
			autoText_gonggao.startScroll();
		}
	}

	private void toweixin() {
		mController.doOauthVerify(MainActivity.this, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(MainActivity.this, "开始授权",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(MainActivity.this, "授权失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(final Bundle value,
							SHARE_MEDIA platform) {
						mController.getPlatformInfo(MainActivity.this,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(MainActivity.this,
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
											Toast.makeText(MainActivity.this,
													"获取资料失败" + status,
													Toast.LENGTH_SHORT).show();
										}
									}

								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(MainActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void yaoqing_dilog(final String userid) {
		String invitationnum = FileUtils.getChannel1(this);
		if (invitationnum != null && !"".equals(invitationnum)) {
			sendyaoqing(userid, invitationnum);
		} else {
			View inflate = getLayoutInflater().inflate(R.layout.dialog_numview,
					null);
			final EditText edit_num = (EditText) inflate
					.findViewById(R.id.edit_num);
			dialog_yaoqing = new Builder(this)
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

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 1500) {
			Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PgyCrashManager.unregister();
		OffersManager.getInstance(MainActivity.this).onAppExit();
	}

	private void sendyaoqing(String userid, String yaoqingid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("invitecode", yaoqingid);
		InterfaceTool.Networkrequest(MainActivity.this, queue, m_pDialog,
				yaoqingurl, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						closewaite();
						try {
							String code = response.getString("code");
							if (code.equals("1")) {
								String userid = response.getString("userid");
								textuser_id.setText("ID:" + userid);
								sp.edit().putBoolean("islogin", true)
										.putString("userid", userid).commit();
								Toastshow("注册成功");
								getinfo();
							} else {
								Toastshow("注册失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, map);
	}

	private void setQQ() {
		View inflate = getLayoutInflater().inflate(R.layout.dialog_numview,
				null);
		final EditText edit_QQ = (EditText) inflate.findViewById(R.id.edit_num);
		dialog_qq = new Builder(this).setTitle("请输入您的QQ号")
				.setIcon(android.R.drawable.ic_dialog_info).setView(inflate)
				.setPositiveButton("确定", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String qq = edit_QQ.getText().toString();
						if (qq.length() > 4) {
							sendQQ(qq);
						}
						dialog_qq.dismiss();
					}
				}).create();
		if (!dialog_qq.isShowing()) {
			dialog_qq.show();
		}
	}

	private void sendQQ(String QQ) {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("qq", QQ);
		InterfaceTool.Networkrequest(this, queue, m_pDialog, sendQQurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						closewaite();
						try {
							String code = arg0.getString("code");

							if (code.equals("1")) {
								Toastshow("绑定QQ成功");
								String hongbao = arg0.getString("hongbao");
								hbtishi_dialog(hongbao);
							} else if (code.equals("4")) {
								Toastshow("不允许输入的QQ为空");
							} else {
								Toastshow("提交失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, params);
	}

	private void hbtishi_dialog(String hongbao) {
		Builder builder = new Builder(MainActivity.this);
		builder.setMessage("恭喜您获得好友赠送的" + hongbao + "元红包！");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				getinfo();
			}
		}).show();
	}

	private void Qiandao() {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue, m_pDialog, Qdburl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						closewaite();
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String money = arg0.getString("money");
								qd_dialog("恭喜您签到获得" + money + "元");
							} else if (code.equals("2")) {
								qd_dialog("今天已经签到过了，明日赶早");
							} else {
								Toastshow("请求失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, params);
	}

	private void qd_dialog(String neirong) {
		final Dialog qddialog = new Dialog(this, R.style.dialog_login_style);
		View inflate = getLayoutInflater().inflate(
				R.layout.dialog_qiandao_show, null);
		qddialog.setContentView(inflate);
		TextView text_neirong = (TextView) inflate
				.findViewById(R.id.text_neirong);
		text_neirong.setText(neirong);
		inflate.findViewById(R.id.btn_canle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						qddialog.dismiss();
					}
				});
		qddialog.show();
	}

	// 签到过去方式查看广告图片进行签到
	// private void qd_dialog(String imageurl, final String url) {
	// final Dialog qddialog = new Dialog(this, R.style.dialog_login_style);
	// qddialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
	// View inflate = getLayoutInflater().inflate(R.layout.dialog_qiandao,
	// null);
	// qddialog.setContentView(inflate);
	// text_neirong = (TextView) inflate.findViewById(R.id.text_neirong);
	// ImageView image_guanggao = (ImageView) inflate
	// .findViewById(R.id.image_guanggao);
	// imageLoader.displayImage(imageurl, image_guanggao, options, null);
	// image_guanggao.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// startActivity(new Intent(MainActivity.this, WebActivity.class)
	// .putExtra("url", url));
	// }
	// });
	// TimeButton btn_qiandao = (TimeButton) inflate
	// .findViewById(R.id.btn_qiandao);
	// btn_qiandao.onCreate();
	// btn_qiandao.setTextAfter("秒后可以点击").setTextBefore("签到")
	// .setLenght(10 * 1000);
	// btn_qiandao.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Qiandao();
	// }
	// });
	// inflate.findViewById(R.id.btn_guanbi).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// qddialog.dismiss();
	// }
	// });
	// qddialog.show();
	// }

	public void onResume() {
		super.onResume();
		PgyFeedbackShakeManager.register(MainActivity.this, false);
		MobclickAgent.onResume(this);

		// 版本检测方式2：带更新回调监听
		PgyUpdateManager.register(MainActivity.this,
				new UpdateManagerListener() {
					@Override
					public void onUpdateAvailable(final String result) {
						final AppBean appBean = getAppBeanFromString(result);

						new Builder(MainActivity.this)
								.setTitle("更新")
								.setMessage("")
								.setNegativeButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												startDownloadTask(
														MainActivity.this,
														appBean.getDownloadURL());
											}
										}).show();

					}

					@Override
					public void onNoUpdateAvailable() {
//						Toast.makeText(MainActivity.this, "已经是最新版本",
//								Toast.LENGTH_SHORT).show();
					}
				});
	}

	public void onPause() {
		super.onPause();
		PgyFeedbackShakeManager.unregister();
		MobclickAgent.onPause(this);
	}

	class MysnspostListener implements SnsPostListener {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode,
				SocializeEntity entity) {
			if (eCode == 200) {
				// if (sharetoqd_dialog.isShowing())
				// sharetoqd_dialog.dismiss();
				Qiandao();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10104) {
			// if (sharetoqd_dialog.isShowing())
			// sharetoqd_dialog.dismiss();
			Qiandao();
		}
	}
}
