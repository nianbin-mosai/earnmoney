package com.mdxx.qmmz.newp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.Reward;
import com.mdxx.qmmz.utils.InterfaceTool;

public class DuiFragment extends Fragment implements OnClickListener {
	private PullToRefreshListView pull_reward;
	private ListView mListView;
	private RewardAdapter adapter;
	private NMainActivity activity;
	private TextView text_invitenum;
	private TextView text_friend7;
	private TextView text_hongbao;
	private int page = 1;
	private final String rewardurl = InterfaceTool.ULR + "user/friendreward";
	private ArrayList<Reward> rewardlist = new ArrayList<Reward>();
	private ImageView noDataImage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (NMainActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_dui, container, false);
		initview(view);
		getreward(true);
		return view;
	}

	private void initview(View v) {
		v.findViewById(R.id.duihuan_layout).setOnClickListener(this);
		noDataImage = (ImageView) v.findViewById(R.id.imageView2);
		pull_reward = (PullToRefreshListView) v.findViewById(R.id.pull_reward);
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
		pull_reward
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						page++;
						getreward(false);
					}
				});
		
		View view = activity.getLayoutInflater().inflate(R.layout.invitation_head, null);
		text_invitenum = (TextView) view.findViewById(R.id.text_invitenum);
		text_friend7 = (TextView) view.findViewById(R.id.text_friend7);
		text_hongbao = (TextView) view.findViewById(R.id.text_hongbao);
		mListView.addHeaderView(view);
		
		noDataImage.setVisibility(View.VISIBLE);
		pull_reward.setVisibility(View.GONE);
	}
	
	private void getreward(final boolean isclear) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", activity.getuseid());
		params.put("page", page + "");
		InterfaceTool.Networkrequest(activity, activity.queue, null, rewardurl,
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
								if(rewardlist.size() > 0){
									noDataImage.setVisibility(View.GONE);
									pull_reward.setVisibility(View.VISIBLE);
								}
							} else {
								activity.Toastshow("错误代码" + code);
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
				convertView = activity.getLayoutInflater().inflate(
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
			activity.imageLoader.displayImage(activity.getimageurl(), holder.image_head,
					activity.options, null);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.duihuan_layout:
			if (activity.islogin()) {
				if ("".equals(activity.sp.getString("token", ""))) {
					activity.sp.edit().putBoolean("islogin", false).commit();
					activity.islogin();
					return;
				}
				activity.getdbaUrl();
			}
			break;
		}
	}
}
