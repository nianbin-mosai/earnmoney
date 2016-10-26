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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.Detail;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;

public class DetailActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView pullListView; 
	private ListView mListView;
	private TextView all;
	private TextView ren;
	private TextView qian;
	private TextView ti;
	private int post = 1;
	private int page = 1;
	private String Recordurl = InterfaceTool.ULR + "user/accountlist";
	private ArrayList<Detail> detailist = new ArrayList<Detail>();
	private DetailAdapter detailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		findViewById(R.id.back).setOnClickListener(this);
		pullListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		mListView = pullListView.getRefreshableView(); 
		mListView.setDivider(null);
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				page=1;
				initdate(post);
			}
		});
		pullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				initdate(post);
			}
		});
		
		all = (TextView) findViewById(R.id.all);
		ren = (TextView) findViewById(R.id.ren);
		qian = (TextView) findViewById(R.id.qian);
		ti = (TextView) findViewById(R.id.ti);
		all.setOnClickListener(this);
		ren.setOnClickListener(this);
		qian.setOnClickListener(this);
		ti.setOnClickListener(this);
		detailAdapter=new DetailAdapter();
		mListView.setAdapter(detailAdapter);
		setChooseSelector(1);
	}

	private void initdate(int num) {
		Map params = new HashMap<String, String>();
		params.put("userid", getuseid());
		params.put("type", num+"");
		params.put("page", page + "");

		InterfaceTool.Networkrequest(DetailActivity.this, queue, null,
				Recordurl, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject resut) {
						closewaite();
						try {
							String code = resut.getString("code");
							if (code.equals("1")) {
								if (page == 1) {
									detailist.clear();
								}
								JSONArray data = resut.getJSONArray("data");

								for (int i = 0;i<data.length(); i++) {
									JSONObject jsonObject =(JSONObject) data.get(i);
									String remark = jsonObject.getString("remark");
									String time = jsonObject.getString("time");
									String money = jsonObject.getString("money");
									String headimgurl = jsonObject.getString("headimgurl");
									Detail detail=new Detail(remark, time, money,headimgurl);
									detailist.add(detail);
								}
								detailAdapter.notifyDataSetChanged();
								page++;
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pullListView.onRefreshComplete();
					}
				}, params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.all:
			setChooseSelector(1);
			break;
		case R.id.ren:
			setChooseSelector(2);
			break;
		case R.id.qian:
			setChooseSelector(3);
			break;
		case R.id.ti:
			setChooseSelector(4);
			break;
		}
	}

	private void setChooseSelector(int which) {
		post = which;
		page = 1;
		initdate(post);
		switch (which) {
		case 1:
			all.setBackgroundResource(R.drawable.detail_left_bg);
			all.setTextColor(this.getResources().getColor(R.color.white));
			ren.setBackgroundDrawable(null);
			ren.setTextColor(this.getResources().getColor(R.color.black));
			qian.setBackgroundDrawable(null);
			qian.setTextColor(this.getResources().getColor(R.color.black));
			ti.setBackgroundDrawable(null);
			ti.setTextColor(this.getResources().getColor(R.color.black));
			break;
		case 2:
			ren.setBackgroundResource(R.drawable.detail_ceter_bg);
			ren.setTextColor(this.getResources().getColor(R.color.white));
			all.setBackgroundDrawable(null);
			all.setTextColor(this.getResources().getColor(R.color.black));
			qian.setBackgroundDrawable(null);
			qian.setTextColor(this.getResources().getColor(R.color.black));
			ti.setBackgroundDrawable(null);
			ti.setTextColor(this.getResources().getColor(R.color.black));
			break;
		case 3:
			qian.setBackgroundResource(R.drawable.detail_ceter_bg);
			qian.setTextColor(this.getResources().getColor(R.color.white));
			ren.setBackgroundDrawable(null);
			ren.setTextColor(this.getResources().getColor(R.color.black));
			all.setBackgroundDrawable(null);
			all.setTextColor(this.getResources().getColor(R.color.black));
			ti.setBackgroundDrawable(null);
			ti.setTextColor(this.getResources().getColor(R.color.black));
			break;
		case 4:
			ti.setBackgroundResource(R.drawable.detail_right_bg);
			ti.setTextColor(this.getResources().getColor(R.color.white));
			ren.setBackgroundDrawable(null);
			ren.setTextColor(this.getResources().getColor(R.color.black));
			qian.setBackgroundDrawable(null);
			qian.setTextColor(this.getResources().getColor(R.color.black));
			all.setBackgroundDrawable(null);
			all.setTextColor(this.getResources().getColor(R.color.black));
			break;
		}
	}
	class DetailAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return detailist.size();
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
			if(convertView==null){
				holder=new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.linear_detail, null);
				holder.image_head=(ImageView) convertView.findViewById(R.id.image_head);
				holder.text_time=(TextView) convertView.findViewById(R.id.text_time);
				holder.text_shuoming=(TextView) convertView.findViewById(R.id.text_shuoming);
				holder.text_addmoney=(TextView) convertView.findViewById(R.id.text_addmoney);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Detail detail = detailist.get(position);
			holder.text_time.setText(detail.getTime());
			holder.text_shuoming.setText(detail.getRemark());
			imageLoader.displayImage(detail.getheadimgurl(),
					holder.image_head, options, null);
			holder.text_addmoney.setText(detail.getMoney()+"元");
			return convertView;
		}
		
	}
class ViewHolder{
	ImageView image_head;
	TextView text_time;
	TextView text_shuoming;
	TextView text_addmoney;
}
}
