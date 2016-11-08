package com.mdxx.qmmz.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.HbListBean;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.customview.HbDialog;
import com.mdxx.qmmz.customview.HbDialog.OnClickOk;
import com.mdxx.qmmz.customview.MyDialog;
import com.mdxx.qmmz.utils.InterfaceTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QhbActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView pullListView;
	private ListView mListView;
	private ImageView image_head;
	private TextView text_hb;
	private TextView text_fenmoney;
	private TextView text_allmoney;
	private TextView text_friend;
	private String Qhburl = InterfaceTool.ULR + "user/hongbao";
	private String hblistburl = InterfaceTool.ULR + "qhongbao/index";
	private String linghbburl = InterfaceTool.ULR + "qhongbao/finish";
	private TextView xsyq1;
	private TextView xsyq2;
	private TextView mryq1;
	private TextView mryq2;
	private TextView xscontent1;
	private TextView xscontent2;
	private TextView mrcontent1;
	private TextView mrcontent2;
	private ArrayList<HbListBean> hbarr = new ArrayList<HbListBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qhb);
		initpull();
		getQhb();
		getHbList();
		setshare();
	}

	private void initpull() {
		findViewById(R.id.back).setOnClickListener(this);
		pullListView = (PullToRefreshListView) findViewById(R.id.pull_qhb);
		
		pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getQhb();
			}

		});
		mListView = pullListView.getRefreshableView();
		mListView.setDivider(null);
		initAddHeadView();
	}

	private void initAddHeadView() {
		View view = getLayoutInflater().inflate(
				R.layout.pullrefresh_headview_hb, null);
		image_head = (ImageView) view.findViewById(R.id.image_head);
		imageLoader.displayImage(getimageurl(), image_head,options,null);
		text_hb = (TextView) view.findViewById(R.id.text_hb);
		text_fenmoney = (TextView) view.findViewById(R.id.text_fenmoney);
		text_allmoney = (TextView) view.findViewById(R.id.text_allmoney);
		text_friend = (TextView) view.findViewById(R.id.text_friend);
		text_hb = (TextView) view.findViewById(R.id.text_hb);
		view.findViewById(R.id.layout_sendfriend).setOnClickListener(this);
		view.findViewById(R.id.xinshou_hb1).setOnClickListener(this);
		view.findViewById(R.id.xinshou_hb2).setOnClickListener(this);
		view.findViewById(R.id.meiri_hb1).setOnClickListener(this);
		view.findViewById(R.id.meiri_hb2).setOnClickListener(this);
		xsyq1 = (TextView) view.findViewById(R.id.textView8);
		xsyq2 = (TextView) view.findViewById(R.id.TextView02);
		mryq1 = (TextView) view.findViewById(R.id.TextView04);
		mryq2 = (TextView) view.findViewById(R.id.TextView07);
		
		xscontent1 = (TextView) view.findViewById(R.id.textView7);
		xscontent2 = (TextView) view.findViewById(R.id.TextView03);
		mrcontent1 = (TextView) view.findViewById(R.id.TextView06);
		mrcontent2 = (TextView) view.findViewById(R.id.TextView09);
		
		mListView.addHeaderView(view);
		mListView.setAdapter(null);
	}

	private void getQhb() {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue, null, Qhburl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String hongbao = arg0.getString("hongbao");
								String invitenum = arg0.getString("invitenum");
								Double friendhongbao = Double.valueOf(arg0.getString("friendhongbao"));
								text_hb.setText(hongbao + "元");
								text_friend.setText(invitenum);
								text_allmoney.setText(friendhongbao*2 + "元");
								text_fenmoney.setText(friendhongbao + "元");

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						pullListView.onRefreshComplete();
					}
				}, params);
	}

	private void getHbList(){
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("imei", getIMEI());
		InterfaceTool.Networkrequest(this, queue, null, hblistburl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							int code = arg0.getInt("code");
							if (code == 1) {
								JSONObject listobject = arg0.getJSONObject("list");
								JSONObject hongbao1 = listobject.getJSONObject("hongbao1");
								JSONObject hongbao2 = listobject.getJSONObject("hongbao2");
								JSONObject hongbao3 = listobject.getJSONObject("hongbao3");
								JSONObject hongbao4 = listobject.getJSONObject("hongbao4");
								HbListBean hbListBean1 = new HbListBean();
								HbListBean hbListBean2 = new HbListBean();
								HbListBean hbListBean3 = new HbListBean();
								HbListBean hbListBean4 = new HbListBean();
								hbListBean1.setContent(hongbao1.getString("content"));
								hbListBean1.setDesc(hongbao1.getString("desc"));
								hbListBean1.setRemark(hongbao1.getString("remark"));
								hbListBean2.setContent(hongbao2.getString("content"));
								hbListBean2.setDesc(hongbao2.getString("desc"));
								hbListBean2.setRemark(hongbao2.getString("remark"));
								hbListBean3.setContent(hongbao3.getString("content"));
								hbListBean3.setDesc(hongbao3.getString("desc"));
								hbListBean3.setRemark(hongbao3.getString("remark"));
								hbListBean4.setContent(hongbao4.getString("content"));
								hbListBean4.setDesc(hongbao4.getString("desc"));
								hbListBean4.setRemark(hongbao4.getString("remark"));
								
								xsyq1.setText(hongbao1.getString("desc"));
								xsyq2.setText(hongbao2.getString("desc"));
								mryq1.setText(hongbao3.getString("desc"));
								mryq2.setText(hongbao4.getString("desc"));
								
								xscontent1.setText(hongbao1.getString("content"));
								xscontent2.setText(hongbao2.getString("content"));
								mrcontent1.setText(hongbao3.getString("content"));
								mrcontent2.setText(hongbao4.getString("content"));
								
								hbarr.add(hbListBean1);
								hbarr.add(hbListBean2);
								hbarr.add(hbListBean3);
								hbarr.add(hbListBean4);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, params);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.layout_makemoney:
//			startActivity(new Intent(this, AdvertisementActivity.class));
//			break;
//		case R.id.layout_withdrawals:
//			getdbaUrl();
//			break;
		case R.id.layout_sendfriend:
			break;
		case R.id.back:
			finish();
			break;
		case R.id.xinshou_hb1:
			getHbMoney(1, hbarr.get(0).getRemark());
			break;
		case R.id.xinshou_hb2:
			getHbMoney(2, hbarr.get(1).getRemark());
			break;
		case R.id.meiri_hb1:
			getHbMoney(3, hbarr.get(2).getRemark());
			break;
		case R.id.meiri_hb2:
			getHbMoney(4, hbarr.get(3).getRemark());
			break;
		}
	}

	private void getHbMoney(final int type, final String con){
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("imei", getIMEI());
		params.put("type", ""+type);
		InterfaceTool.Networkrequest(this, queue, null, linghbburl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							int code = arg0.getInt("code");
							if (code != 1) {
								MyDialog myDialog = new MyDialog(QhbActivity.this, con, type);
								myDialog.show();
							}else{
								HbDialog hbDialog = new HbDialog(QhbActivity.this, arg0.getString("money"));
								hbDialog.setOnClickOK(new OnClickOk() {
									
									@Override
									public void onClick() {
										getQhb();
									}
								});
								hbDialog.show();
							}
							Toastshow(arg0.getString("msg"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, params);
	}
	
	private void getgongbao(String hongbao) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("恭喜您拆得"+hongbao+"元红包,一元就可提现哟！").setNegativeButton("确定", null).show();
	}
}
