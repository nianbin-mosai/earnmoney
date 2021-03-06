package com.mdxx.qmmz.newp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.activity.GonggaoActivity;
import com.mdxx.qmmz.activity.InvitationActivity;
import com.mdxx.qmmz.activity.KeFuActivity;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.UserPF;
import com.mdxx.qmmz.network.AppAction;
import com.mdxx.qmmz.network.HttpResponse;
import com.mdxx.qmmz.network.HttpResponseHandler;
import com.mdxx.qmmz.newfeature.LoginActivity;
import com.mdxx.qmmz.newfeature.MemberActivity;
import com.mdxx.qmmz.newfeature.PayActivity;
import com.mdxx.qmmz.newfeature.bean.WebViewConfigs;
import com.mdxx.qmmz.utils.InterfaceTool;

import org.json.JSONException;
import org.json.JSONObject;

public class MeFragment extends Fragment implements OnClickListener {
	private WebViewConfigs webViewConfigs = new WebViewConfigs();
	private NMainActivity activity;
	private ImageView touxiang;
	private TextView name;
	private TextView userid;
	private TextView residue;
	private TextView jifen;
	private final String infourl = InterfaceTool.ULR + "user/account";
	private Animation loadAnimation;
	private ImageView refresh_date;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (NMainActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		touxiang = (ImageView) view.findViewById(R.id.touxiang);
		name = (TextView) view.findViewById(R.id.name);
		userid = (TextView) view.findViewById(R.id.userid);
		residue = (TextView) view.findViewById(R.id.residue);
		jifen = (TextView) view.findViewById(R.id.jifen);
		view.findViewById(R.id.mymes_layout).setOnClickListener(this);
		view.findViewById(R.id.xiaxian).setOnClickListener(this);
		view.findViewById(R.id.paimingsai).setOnClickListener(this);
		view.findViewById(R.id.yaoqingmx).setOnClickListener(this);
		view.findViewById(R.id.rl_pay).setOnClickListener(this);
		view.findViewById(R.id.rl_exchange_point).setOnClickListener(this);
		view.findViewById(R.id.rl_game_center).setOnClickListener(this);
		view.findViewById(R.id.rl_vip).setOnClickListener(this);
		view.findViewById(R.id.btn_logout).setOnClickListener(this);
		view.findViewById(R.id.btn_member).setOnClickListener(this);
		refresh_date = (ImageView) view.findViewById(R.id.refresh_date);
		refresh_date.setOnClickListener(this);
		loadAnimation = AnimationUtils.loadAnimation(activity,R.anim.refresh_date_anim);
//		getinfo();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mymes_layout://客服
//			if (activity.islogin()) {
//				if ("".equals(activity.sp.getString("token", ""))) {
//					activity.sp.edit().putBoolean("islogin", false).commit();
//					activity.islogin();
//					return;
//				}
//			}
			startActivity(new Intent(activity, KeFuActivity.class));
			break;
		case R.id.xiaxian://公告
//			if (activity.islogin()) {
//				if ("".equals(activity.sp.getString("token", ""))) {
//					activity.sp.edit().putBoolean("islogin", false).commit();
//					activity.islogin();
//					return;
//				}
//			}
			startActivity(new Intent(activity,
					GonggaoActivity.class));
			break;
		case R.id.paimingsai://排名赛
//			if (activity.islogin()) {
//				if ("".equals(activity.sp.getString("token", ""))) {
//					activity.sp.edit().putBoolean("islogin", false).commit();
//					activity.islogin();
//					return;
//				}
//			}
			break;
		case R.id.yaoqingmx://邀请明细
//			if (activity.islogin()) {
//				if ("".equals(activity.sp.getString("token", ""))) {
//					activity.sp.edit().putBoolean("islogin", false).commit();
//					activity.islogin();
//					return;
//				}
				startActivity(new Intent(activity,
						InvitationActivity.class));
//			}
			break;
		case R.id.refresh_date:
			refresh_date.startAnimation(loadAnimation);
			getWebViewConfigs();
//			getinfo();
			break;

			case R.id.rl_vip:
				if(!TextUtils.isEmpty(webViewConfigs.member)){

				}
				break;
			case R.id.rl_exchange_point:
				if(!TextUtils.isEmpty(webViewConfigs.exchange)){

				}
				break;
			case R.id.rl_game_center:
				if(!TextUtils.isEmpty(webViewConfigs.game)){

				}
				break;
			case R.id.rl_pay:
				if(!TextUtils.isEmpty(webViewConfigs.pay)){
					Intent intent = new Intent(getActivity(), PayActivity.class);
					intent.putExtra("url",getFormatUrl(webViewConfigs.pay));
					startActivityForResult(intent,0);
				}
				break;
			case  R.id.btn_logout:
				logout();
				break;
			case R.id.btn_member:
//				share();
				if(!TextUtils.isEmpty(webViewConfigs.member)){
					Intent intent = new Intent(getActivity(), MemberActivity.class);
					intent.putExtra("url",getFormatUrl(webViewConfigs.member));
					LogUtils.i(getFormatUrl(webViewConfigs.member));
					startActivityForResult(intent,0);
				}
				break;
		}
	}

	private void share() {
	}

	private void logout(){
		new MaterialDialog.Builder(getActivity()).title(getString(R.string.tip_logout))
		.negativeText(getString(R.string.cancel))
		.positiveText(getString(R.string.ok))
		.onNegative(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				dialog.dismiss();
			}
		})
		.onPositive(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				dialog.dismiss();
				UserPF.getInstance().logout();
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		})
		.show();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getWebViewConfigs();
	}
	private void getWebViewConfigs(){
		AppAction.getWebViewConfigs(getActivity(), new HttpResponseHandler(getActivity(),HttpResponse.class,(BaseActivity)getActivity()) {
			@Override
			public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
				try {
					JSONObject result = new JSONObject(responseString);
					JSONObject data = result.optJSONObject("data");
					webViewConfigs.exchange = data.optString("exchange");
					webViewConfigs.game = data.optString("game");
					webViewConfigs.member = data.optString("member");
					webViewConfigs.pay = data.optString("pay");
					webViewConfigs.task = data.optString("task");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


		});
	}

	private String getFormatUrl(String url){
		return String.format("%s?userid=%s&token=%s",url, UserPF.getInstance().getUserid(),UserPF.getInstance().getToken());
	}
}
