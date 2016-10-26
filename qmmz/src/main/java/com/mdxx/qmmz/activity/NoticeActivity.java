package com.mdxx.qmmz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mdxx.qmmz.Notice;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeActivity extends BaseActivity implements OnClickListener{
	private PullToRefreshListView pullListView;
	private ListView mListView;
	private ArrayList<Notice> noticelist = new ArrayList<Notice>();
	private final String noticeurl = InterfaceTool.ULR + "gonggao/list";
	private NoticeAdapter noticeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
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
				startActivity(new Intent(NoticeActivity.this, WebActivity.class)
						.putExtra("url", noticelist.get(position - 1)
								.getTourl()));
			}
		});
	}

	private void getnotices() {
		Map params = new HashMap<String, String>();
		InterfaceTool.Networkrequest(this, queue, null, noticeurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								noticelist.clear();
								JSONArray jsonArray = arg0.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = (JSONObject) jsonArray
											.get(i);
									String title = jsonObject
											.getString("title");
									String tourl = jsonObject.getString("url");
									String time = jsonObject.getString("time");
									String imagepath = jsonObject
											.getString("imageurl");
									Notice notice = new Notice(imagepath,
											title, time, tourl);
									noticelist.add(notice);
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
			return noticelist.size();
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
						R.layout.linnear_notice, null);
				holder.image_head = (ImageView) convertView
						.findViewById(R.id.image_head);
				holder.text_title = (TextView) convertView
						.findViewById(R.id.text_title);
				holder.text_time = (TextView) convertView
						.findViewById(R.id.text_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Notice notice = noticelist.get(position);
			imageLoader.displayImage(notice.getImagepath(), holder.image_head,
					options, null);
			holder.text_title.setText(notice.getTitle());
			holder.text_time.setText(notice.getTime());
			return convertView;
		}

	}

	class ViewHolder {
		TextView text_title;
		ImageView image_head;
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
