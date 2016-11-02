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
import com.mdxx.qmmz.Record;
import com.mdxx.qmmz.utils.InterfaceTool;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecordActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView pullListrecord;
	private ListView mListView;
	private ArrayList<Record> recordlist = new ArrayList<Record>();
	private RecordAdapter adapter;
	private int page = 1;
	private String recordurl = InterfaceTool.ULR + "exchange/record";
	private TextView text_wu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		initview();
		getrecord(true);
	}

	private void initview() {
		text_wu = (TextView) findViewById(R.id.text_wu);
		findViewById(R.id.back).setOnClickListener(this);
		pullListrecord = (PullToRefreshListView) findViewById(R.id.pullListrecord);
		mListView = pullListrecord.getRefreshableView();
		mListView.setDividerHeight(0);
		adapter = new RecordAdapter();
		mListView.setAdapter(adapter);
		pullListrecord.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				getrecord(true);
			}
			
		});
		pullListrecord.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				page++;
				getrecord(false);
			}
		});
	}

	private void getrecord(boolean b) {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("page", page + "");
		InterfaceTool.Networkrequest(this, queue, null, recordurl,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						try {
							String code = arg0.getString("code");
							if (code.equals("1")) {
								if (page == 1) {
									recordlist.clear();
								}
								JSONArray data = arg0.getJSONArray("data");
								for (int i = 0; i < data.length(); i++) {
									JSONObject jsonbject = (JSONObject) data
											.get(i);
									String haoma = jsonbject.getString("haoma");
									String time = jsonbject
											.getString("createdate");
									String type = jsonbject.getString("type");
									String money = jsonbject.getString("money");
									Record record = new Record(type, time,
											money, haoma);
									recordlist.add(record);
								}
								text_wu.setVisibility(data.length() == 0 ? View.VISIBLE
										: View.GONE);
								adapter.notifyDataSetChanged();
							} else {
								Toastshow("获取数据失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pullListrecord.onRefreshComplete();
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

	class RecordAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordlist.size();
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
						R.layout.linnear_record, null);
				holder.text_haoma = (TextView) convertView
						.findViewById(R.id.text_haoma);
				holder.text_money = (TextView) convertView
						.findViewById(R.id.text_money);
				holder.text_time = (TextView) convertView
						.findViewById(R.id.text_time);
				holder.text_type = (TextView) convertView
						.findViewById(R.id.texttype);
				convertView.setTag(holder);
			} else {
				holder = (Viewholder) convertView.getTag();
			}
			Record record = recordlist.get(position);
			holder.text_haoma.setText(record.getHaoma());
			holder.text_money.setText(record.getMoney());
			holder.text_time.setText(record.getTime());
			holder.text_type.setText(record.getTpye());
			return convertView;
		}

	}

	class Viewholder {
		TextView text_time;
		TextView text_money;
		TextView text_type;
		TextView text_haoma;
	}

}
