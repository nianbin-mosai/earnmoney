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
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.Reward;
import com.mdxx.qmmz.utils.InterfaceTool;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RewardActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView pull_reward;
	private TextView text_invitenum;
	private TextView text_friend7;
	private TextView text_hongbao;
	private ArrayList<Reward> rewardlist = new ArrayList<Reward>();
	private final String rewardurl = InterfaceTool.ULR + "user/friendreward";
	private int page = 1;
	private ListView mListView;
	private RewardAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reward);
		initview();
		initAddHeadView();
		getreward(true);
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		pull_reward = (PullToRefreshListView) findViewById(R.id.pull_reward);
		mListView = pull_reward.getRefreshableView();
		mListView.setDividerHeight(0);
		adapter = new RewardAdapter();
		mListView.setAdapter(adapter);
		pull_reward.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				getreward(true);
			}

		});
		pull_reward.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				page++;
				 getreward(false);
			}
		});
	}

	private void initAddHeadView() {
		View view = getLayoutInflater().inflate(R.layout.invitation_head, null);
		text_invitenum = (TextView) view.findViewById(R.id.text_invitenum);
		text_friend7 = (TextView) view.findViewById(R.id.text_friend7);
		text_hongbao = (TextView) view.findViewById(R.id.text_hongbao);
		mListView.addHeaderView(view);
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

	private void getreward(final boolean isclear) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("page", page + "");
		InterfaceTool.Networkrequest(this, queue, null, rewardurl,
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
									rewardlist.clear();
								}
								for (int i = 0; i < data.length(); i++) {
									JSONObject jsonObject = (JSONObject) data
											.get(i);
									String time = jsonObject.getString("time");
									String money = jsonObject
											.getString("money");
									String remark = jsonObject
											.getString("remark");
									Reward reward = new Reward(remark, time,
											money);
									rewardlist.add(reward);
								}
								adapter.notifyDataSetChanged();
							} else {
								Toastshow("错误代码" + code);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pull_reward.onRefreshComplete();
					}
				}, params);
	}

	class RewardAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rewardlist.size();
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
						R.layout.linnear_reward, null);
				holder.image_head = (ImageView) convertView
						.findViewById(R.id.image_head);
				holder.text_time = (TextView) convertView
						.findViewById(R.id.text_time);
				holder.text_remark = (TextView) convertView
						.findViewById(R.id.text_remark);
				holder.text_money = (TextView) convertView
						.findViewById(R.id.text_money);
				convertView.setTag(holder);
			} else {
				holder = (Viewholder) convertView.getTag();
			}
			Reward reward = rewardlist.get(position);
			imageLoader.displayImage(getimageurl(), holder.image_head,
					options, null);
			holder.text_time.setText(reward.getTime());
			holder.text_remark.setText(reward.getRemark());
			holder.text_money.setText("+" + reward.getMoney() + "元");
			return convertView;
		}

	}

	class Viewholder {
		ImageView image_head;
		TextView text_time;
		TextView text_remark;
		TextView text_money;
	}
}
