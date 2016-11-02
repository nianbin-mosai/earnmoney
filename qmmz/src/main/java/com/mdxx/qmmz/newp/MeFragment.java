package com.mdxx.qmmz.newp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.GonggaoActivity;
import com.mdxx.qmmz.activity.InvitationActivity;
import com.mdxx.qmmz.activity.KeFuActivity;
import com.mdxx.qmmz.activity.YaoQingActivity;
import com.mdxx.qmmz.utils.InterfaceTool;

public class MeFragment extends Fragment implements OnClickListener {

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
		refresh_date = (ImageView) view.findViewById(R.id.refresh_date);
		refresh_date.setOnClickListener(this);
		loadAnimation = AnimationUtils.loadAnimation(activity,R.anim.refresh_date_anim);
		getinfo();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mymes_layout://客服
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				startActivity(new Intent(activity, KeFuActivity.class));
			}
			break;
		case R.id.xiaxian://公告
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				startActivity(new Intent(activity,
						GonggaoActivity.class));
			}
			break;
		case R.id.paimingsai://排名赛
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				startActivity(new Intent(activity,
						YaoQingActivity.class));
			}
			break;
		case R.id.yaoqingmx://邀请明细
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				startActivity(new Intent(activity,
						InvitationActivity.class));
			}
			break;
		case R.id.refresh_date:
			refresh_date.startAnimation(loadAnimation);
			getinfo();
			break;
		}
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
								String usemoney = data.getString("usemoney");
								String jfusemoney = data
										.getString("jfusemoney");
								String headimgurl = data
										.getString("headimgurl");
								
								name.setText(activity.getName());
								userid.setText("ID:" + activity.getuseid());
								residue.setText("余额(元):"+usemoney);
								jifen.setText("积分:"+jfusemoney);
								activity.sp.edit().putString("headimgurl", headimgurl)
										.commit();
								activity.imageLoader.displayImage(headimgurl, touxiang,
										activity.options, null);
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
}
