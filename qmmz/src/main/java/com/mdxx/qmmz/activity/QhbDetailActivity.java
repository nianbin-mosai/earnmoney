package com.mdxx.qmmz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.QHBdetailBean;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;

public class QhbDetailActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView pullListView;
	private ListView mListView;
	private String qhbdetail_url = InterfaceTool.ULR + "qhongbao/list";
	private ArrayList<QHBdetailBean> arr = new ArrayList<QHBdetailBean>();
	private MyAda myAda;
	private TextView headtxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qhb_detail);
		initpull();
		getDetailMes();
	}

	private void initpull() {
		findViewById(R.id.back).setOnClickListener(this);
		headtxt = (TextView) findViewById(R.id.textView2);
		pullListView = (PullToRefreshListView) findViewById(R.id.pull_qhb);
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getDetailMes();
			}
		});
		mListView = pullListView.getRefreshableView();
		mListView.setDivider(null);
		myAda = new MyAda();
		mListView.setAdapter(myAda);
	}

	private void getDetailMes() {
		Map params = new HashMap<String, String>();
		InterfaceTool.Networkrequest(this, queue, null, qhbdetail_url,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						try {
							int code = arg0.getInt("code");
							if (code == 1) {
								headtxt.setText(arg0.getString("num") + "个红包,合计" + arg0.getString("all") + "元");
								arr.clear();
								JSONArray jsonArray = arg0.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = jsonArray.getJSONObject(i);
									QHBdetailBean qhBdetailBean = new QHBdetailBean();
									qhBdetailBean.setHeadimage(jsonObject.getString("headimgurl"));
									qhBdetailBean.setName(jsonObject.getString("name"));
									qhBdetailBean.setMoney(jsonObject.getString("money"));
									qhBdetailBean.setTime(jsonObject.getString("time"));
									arr.add(qhBdetailBean);
								}
								myAda.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						pullListView.onRefreshComplete();
					}
				}, params);
	}

	class MyAda extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.qhbdetail_item, null);
				viewHolder = new ViewHolder();
				viewHolder.head = (ImageView) convertView.findViewById(R.id.imageView1);
				viewHolder.name = (TextView) convertView.findViewById(R.id.textView1);
				viewHolder.time = (TextView) convertView.findViewById(R.id.textView2);
				viewHolder.money = (TextView) convertView.findViewById(R.id.textView3);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(arr.get(position).getName());
			viewHolder.money.setText(arr.get(position).getMoney());
			viewHolder.time.setText(arr.get(position).getTime());
			imageLoader.displayImage(arr.get(position).getHeadimage(),
					viewHolder.head, options, null);
			return convertView;
		}
		
	}
	
	class ViewHolder{
		ImageView head;
		TextView name;
		TextView time;
		TextView money;
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
}
