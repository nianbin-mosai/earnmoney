package com.mdxx.qmmz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.Gonggao;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.umeng.socialize.utils.Log;

public class GonggaoActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView pullListView;
	private ListView mListView;
	private ArrayList<Gonggao> gonggaolist = new ArrayList<Gonggao>();
	private final String noticeurl = InterfaceTool.ULR + "gonggao/info";
	private NoticeAdapter noticeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gonggao);
		findViewById(R.id.back).setOnClickListener(this);
		initpull();
		getnotices();
	}

	private void initpull() {
		pullListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		mListView = pullListView.getRefreshableView();
		mListView.setDivider(null);
		noticeAdapter = new NoticeAdapter();
		mListView.setAdapter(noticeAdapter);
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getnotices();
			}

		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				startActivity(new Intent(GonggaoActivity.this,
						WebActivity.class).putExtra("url",
						gonggaolist.get(position - 1).getUrl()));
			}
		});
	}

	private void getnotices() {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		InterfaceTool.Networkrequest(this, queue, null, noticeurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						try {
							Log.e("gonggao", "" + arg0.toString());
							String code = arg0.getString("code");
							if (code.equals("1")) {
								gonggaolist.clear();
								JSONArray jsonArray = arg0.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = (JSONObject) jsonArray
											.get(i);
									String title = jsonObject
											.getString("title");
									String url = jsonObject.getString("url");
									String time = jsonObject.getString("time");
									String status = jsonObject
											.getString("status");
									Gonggao gonggao = new Gonggao(status, time,
											url, title);
									gonggaolist.add(gonggao);
								}
								noticeAdapter.notifyDataSetChanged();
							} else {
								Toastshow("获取失败，失败代码" + code);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pullListView.onRefreshComplete();

					}
				}, params);
	}

	class NoticeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gonggaolist.size();
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
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.linnear_gonggao, null);
				holder.text_state = (TextView) convertView
						.findViewById(R.id.text_state);
				holder.text_title = (TextView) convertView
						.findViewById(R.id.text_title);
				holder.text_time = (TextView) convertView
						.findViewById(R.id.text_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Gonggao gonggao = gonggaolist.get(position);
			holder.text_title.setText(gonggao.getTitle());
			if (gonggao.getStatus().equals("0")) {
				holder.text_state.setVisibility(View.GONE);
			} else {
				Log.e("text_state","VISIBLE");
				holder.text_state.setVisibility(View.VISIBLE);
			}
			holder.text_time.setText(gonggao.getTime());
			return convertView;
		}

	}

	class ViewHolder {
		TextView text_title;
		TextView text_state;
		TextView text_time;
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
}
