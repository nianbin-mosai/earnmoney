package com.mdxx.qmmz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.Invitation;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InvitationActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView pull_invitation;
	private ArrayList<Invitation> invitationlist = new ArrayList<Invitation>();
	private String Invitationurl = InterfaceTool.ULR + "user/invite";
	private int page = 1;
	private InvitationAdapter adapter;
	private ListView mListView;
	private TextView text_invitenum;
	private TextView text_friend7;
	private TextView text_hongbao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitation);
		initview();
		initAddHeadView();
		getInvitation(true);
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		pull_invitation = (PullToRefreshListView) findViewById(R.id.pull_invitation);
		mListView = pull_invitation.getRefreshableView();
		mListView.setDividerHeight(0);
		adapter = new InvitationAdapter();
		mListView.setAdapter(adapter);

		pull_invitation.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				getInvitation(true);
			}
		});
		pull_invitation
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						page++;
						getInvitation(false);
					}
				});
	}

	private void getInvitation(final boolean isclear) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("page", page + "");
		InterfaceTool.Networkrequest(this, queue, null, Invitationurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								String hongbao = arg0.getString("hongbao");
								String friend7 = arg0.getString("friend7");
								String invitenum = arg0.getString("invitenum");
								text_friend7.setText(friend7);
								text_hongbao.setText(hongbao);
								text_invitenum.setText(invitenum);
								JSONArray data = arg0.getJSONArray("data");
								if (isclear) {
									invitationlist.clear();
								}
								for (int i = 0; i < data.length(); i++) {
									JSONObject jsonObject = (JSONObject) data
											.get(i);
									String userid = jsonObject
											.getString("userid");
									String time = jsonObject.getString("time");
									String headimgurl = jsonObject
											.getString("headimgurl");
									String allmoney = jsonObject
											.getString("allmoney");
									String level = jsonObject
											.getString("level");
									String name = jsonObject
											.getString("name");
									String qq = jsonObject.getString("qq");
									Invitation invitation = new Invitation(
											userid, time, headimgurl, allmoney,
											level, qq,name);
									invitationlist.add(invitation);
								}
								adapter.notifyDataSetChanged();
							} else {
								Toastshow("错误代码" + code);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pull_invitation.onRefreshComplete();
					}
				}, params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	private void initAddHeadView() {
		View view = getLayoutInflater().inflate(R.layout.invitation_head, null);
		text_invitenum = (TextView) view.findViewById(R.id.text_invitenum);
		text_friend7 = (TextView) view.findViewById(R.id.text_friend7);
		text_hongbao = (TextView) view.findViewById(R.id.text_hongbao);
		mListView.addHeaderView(view);
	}

	class InvitationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return invitationlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder holder;
			if (convertView == null) {
				holder = new Viewholder();
				convertView = getLayoutInflater().inflate(
						R.layout.linnear_invitation, null);
				holder.image_head = (ImageView) convertView
						.findViewById(R.id.image_head);
				holder.text_time = (TextView) convertView
						.findViewById(R.id.text_time);
				holder.text_name = (TextView) convertView
						.findViewById(R.id.text_name);
				holder.text_level_allmoney = (TextView) convertView
						.findViewById(R.id.text_level_allmoney);
				holder.btn_addqq = (Button) convertView
						.findViewById(R.id.btn_addqq);
				convertView.setTag(holder);
			} else {
				holder = (Viewholder) convertView.getTag();
			}
			final Invitation invitation = invitationlist.get(position);
			imageLoader.displayImage(invitation.getHeadimgurl(),
					holder.image_head, options, null);
			holder.text_time.setText(invitation.getTime());
			holder.text_name.setText(invitation.getname());
			holder.text_level_allmoney.setText("（" + invitation.getLevel()
					+ "级好友）（总收入：" + invitation.getAllmoney() + "元）");
			;
			holder.btn_addqq
					.setVisibility(invitation.getQq().length() > 0 ? View.VISIBLE
							: View.GONE);
			holder.btn_addqq.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = "mqqwpa://im/chat?chat_type=wpa&uin="
							+ invitation.getQq() + "&version=1";
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}
			});
			return convertView;
		}

	}

	class Viewholder {
		ImageView image_head;
		TextView text_time;
		TextView text_name;
		TextView text_level_allmoney;
		Button btn_addqq;
	}
}
