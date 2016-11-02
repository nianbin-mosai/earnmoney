package com.mdxx.qmmz.newp;

import java.util.HashMap;
import java.util.Map;

import net.youmi.android.offers.OffersManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.datouniao.AdPublisher.AppConfig;
import com.datouniao.AdPublisher.AppConnect;
import com.fingermobi.vj.activity.QdiActivity;
import com.fingermobi.vj.listener.IVJAPI;
import com.fingermobi.vj.listener.IVJAppidStatus;
import com.mdxx.qmmz.EventMessage;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.WebActivity;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.newqm.pointwall.QSdkManager;
import com.qm.lo.inter.QEarnNotifier;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.yow.YoManage;

import de.greenrobot.event.EventBus;

public class HomeFragment extends Fragment implements OnClickListener,
		QEarnNotifier {

	private NMainActivity activity;
	private Dialog sharetoqd_dialog;
	private final String Qdburl = InterfaceTool.ULR + "user/qiandao";
	private TextView note;
	private final String infourl = InterfaceTool.ULR + "user/account";
	private AppConnect instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);// 注事件总线
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (NMainActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		view.findViewById(R.id.banner_layout).setOnClickListener(this);
		view.findViewById(R.id.renwu_one).setOnClickListener(this);
		view.findViewById(R.id.renwu_two).setOnClickListener(this);
		view.findViewById(R.id.renwu_three).setOnClickListener(this);
		view.findViewById(R.id.renwu_four).setOnClickListener(this);
		view.findViewById(R.id.weishare).setOnClickListener(this);
		view.findViewById(R.id.weiguanzhu).setOnClickListener(this);
		view.findViewById(R.id.g_image2).setOnClickListener(this);
		view.findViewById(R.id.g_image3).setOnClickListener(this);
		view.findViewById(R.id.g_image1).setOnClickListener(this);
		view.findViewById(R.id.g_image4).setOnClickListener(this);
		note = (TextView) view.findViewById(R.id.textView8);
		AppConfig config = new AppConfig();//大头鸟
		config.setCtx(getActivity());
		config.setClientUserID(activity.getuseid());
		instance = AppConnect.getInstance(config);
		
		if (activity.sp.getBoolean("islogin", false)){
			getinfo();
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.banner_layout:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				activity.getdbaUrl();
			}
			break;
		case R.id.renwu_one:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				OffersManager.getInstance(getActivity()).setCustomUserId(
						activity.getuseid());
				OffersManager.getInstance(getActivity())
						.setUsingServerCallBack(true);
				OffersManager.getInstance(getActivity()).showOffersWall();
			}
			break;
		case R.id.renwu_two:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				instance.ShowAdsOffers();
			}
			break;

		case R.id.renwu_three:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				QSdkManager.getsdkInstance().showOffers(this);
			}
			break;
		case R.id.renwu_four:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				YoManage.showOffer(activity, activity.getuseid());
			}
			break;
		case R.id.weishare:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				IVJAPI ivjapi = new IVJAPI();
				ivjapi.setAppid("ae7304f92345fc939e9edfb27c1d7305");
				ivjapi.setGameid(activity.getuseid());
				ivjapi.init(activity, new IVJAppidStatus() {

					@Override
					public void appidStatus(int status) {
						switch (status) {
						case IVJAPI.VJ_ERROR:
							// 失败
							break;
						case IVJAPI.VJ_APPID_INVALID:
							// 广告位关闭
							break;
						case IVJAPI.VJ_SUCCESS:
							// 广告位打开
							startActivity(new Intent(activity,
									QdiActivity.class));
							break;
						case IVJAPI.VJ_CLOSE:
							// 界面被关闭
							break;
						case IVJAPI.VJ_ON_PRESENT:
							// 界面被展示出来
							break;
						}
					}
				});
			}
			break;
		case R.id.weiguanzhu:
			Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.g_image2:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				getqdurl();
			}
			break;
		case R.id.g_image3:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.g_image1:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.g_image4:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.layout_qqkj:
			activity.mController.postShare(activity, SHARE_MEDIA.QZONE,
					new MysnspostListener());
			break;
		case R.id.layout_wb:
			activity.mController.postShare(activity, SHARE_MEDIA.QQ,
					new MysnspostListener());
			break;
		case R.id.layout_pyq:
			activity.mController.postShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE,
					new MysnspostListener());
			break;
		}
	}

	private void getqdurl() {
		Map params = new HashMap<String, String>();
		InterfaceTool.Networkrequest(activity, activity.queue,
				activity.m_pDialog, InterfaceTool.ULR + "user/qdhongdong",
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						activity.closewaite();
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String imageur = arg0.getString("imageurl");
								String url = arg0.getString("url");
								showsharetoqd(imageur, url);
							} else {
								activity.Toastshow("获取签到信息失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					};
				}, params);
	}

	private void showsharetoqd(String iamgeurl, final String url) {
		activity.setshare();
		sharetoqd_dialog = new Dialog(activity, R.style.dialog_login_style);
		View inflate = activity.getLayoutInflater().inflate(
				R.layout.dialog_qiandao_share, null);
		ImageView image_guanggao = (ImageView) inflate
				.findViewById(R.id.image_guanggao);
		activity.imageLoader.displayImage(iamgeurl, image_guanggao,
				activity.options, null);
		image_guanggao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity, WebActivity.class).putExtra(
						"url", url));
			}
		});
		inflate.findViewById(R.id.layout_qqkj).setOnClickListener(this);
		inflate.findViewById(R.id.layout_pyq).setOnClickListener(this);
		inflate.findViewById(R.id.layout_wb).setOnClickListener(this);
		sharetoqd_dialog.setContentView(inflate);
		if (sharetoqd_dialog != null && !sharetoqd_dialog.isShowing())
			sharetoqd_dialog.show();
	}

	class MysnspostListener implements SnsPostListener {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode,
				SocializeEntity entity) {
			if (eCode == 200) {
				Qiandao();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10104) {
			Qiandao();
		}
	}

	private void Qiandao() {
		Map params = new HashMap<String, String>();
		params.put("userid", activity.getuseid());
		InterfaceTool.Networkrequest(activity, activity.queue,
				activity.m_pDialog, Qdburl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						activity.closewaite();
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String money = arg0.getString("money");
								qd_dialog("恭喜您签到获得" + money + "元");
							} else if (code.equals("2")) {
								qd_dialog("今天已经签到过了，明日赶早");
							} else {
								activity.Toastshow("请求失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, params);
	}

	private void qd_dialog(String neirong) {
		final Dialog qddialog = new Dialog(activity, R.style.dialog_login_style);
		View inflate = activity.getLayoutInflater().inflate(
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

	private void getinfo() {
		Map map = new HashMap<String, String>();
		map.put("userid", activity.getuseid());
		InterfaceTool.Networkrequest(activity, activity.queue, activity.m_pDialog, infourl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						activity.closewaite();
						try {
							String code = response.getString("code");
							if (code.equals("1")) {
								JSONObject data = response
										.getJSONObject("data");
								
								String noticetext = data.getString("noticetext");
								note.setText(noticetext);
							} else if (code.equals("9")) {
								activity.sp.edit().putBoolean("islogin", false).commit();
								activity.islogin();
							} else {
								activity.Toastshow(response.getString("msg"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, map);
	}
	
	@Override
	public void earnedPoints(float arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPoints(float arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPointsFailed(String arg0) {
		// TODO Auto-generated method stub

	}
	
	public void onEventMainThread(EventMessage eventMessage) {
		if("getinfo".equals(eventMessage.getInfo())){
			getinfo();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (instance != null) {
			instance.close();
		}
		EventBus.getDefault().unregister(this);// 取消事件总线
	}
}
