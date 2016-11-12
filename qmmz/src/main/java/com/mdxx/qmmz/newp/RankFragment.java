package com.mdxx.qmmz.newp;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.Paihang;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RankFragment extends Fragment implements OnClickListener {
	private PullToRefreshListView pullListView;
	private ListView mListView;
	private PaihangAdapter shouruadapter;
	private PaihangAdapter yaoqingadapter;
	private PaihangAdapter yueyaoadapter;
	private PaihangAdapter zhourenwuadapter;
	private ArrayList<Paihang> shouyilist = new ArrayList<Paihang>();
	private ArrayList<Paihang> yaoqinglist = new ArrayList<Paihang>();
	private ArrayList<Paihang> yueyaolist = new ArrayList<Paihang>();
	private ArrayList<Paihang> zhourenwulist = new ArrayList<Paihang>();
	private NMainActivity activity;
	private TextView message;
	private TextView shou;
	private TextView yao;
	private TextView zhouyouxiao;
	private TextView zhourenwu;
	private String paihangurl = "http://wifiapp.angles1131.com/" + "chart/index";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		activity = (NMainActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_rank, container, false);
		pullListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
		mListView = pullListView.getRefreshableView();
		mListView.setDivider(null);
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getpaihang();
			}
		});
		shouruadapter = new PaihangAdapter(shouyilist);
		yaoqingadapter = new PaihangAdapter(yaoqinglist);
		yueyaoadapter = new PaihangAdapter(yueyaolist);
		zhourenwuadapter = new PaihangAdapter(zhourenwulist);
		mListView.setAdapter(shouruadapter);
		initHeadView();
		getpaihang();
		return view;
	}

	private void initHeadView() {
		View view = activity.getLayoutInflater().inflate(R.layout.paihang_headview, null);
		message = (TextView) view.findViewById(R.id.message);
		shou = (TextView) view.findViewById(R.id.shou);
		yao = (TextView) view.findViewById(R.id.yao);
		zhourenwu = (TextView) view.findViewById(R.id.zhourenwu);
		zhouyouxiao = (TextView) view.findViewById(R.id.zhouyouxiao);
		shou.setOnClickListener(this);
		yao.setOnClickListener(this);
		zhourenwu.setOnClickListener(this);
		zhouyouxiao.setOnClickListener(this);
		setChooseSelector(1);
		mListView.addHeaderView(view);
	}
	
	class PaihangAdapter extends BaseAdapter {

		private ArrayList<Paihang> list;

		public PaihangAdapter(ArrayList<Paihang> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
				convertView = activity.getLayoutInflater().inflate(R.layout.linnear_paihang, null);
				holder.image_head = (ImageView) convertView.findViewById(R.id.image_head);
				holder.text_num = (TextView) convertView.findViewById(R.id.text_num);
				holder.text_remark = (TextView) convertView.findViewById(R.id.text_remark);
				holder.text_name = (TextView) convertView.findViewById(R.id.text_name);
				holder.text_jiangli = (TextView) convertView.findViewById(R.id.text_jiangli);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Paihang paihang = list.get(position);
			activity.imageLoader.displayImage(paihang.getHeadimgurl(), holder.image_head, activity.options, null);
			holder.text_num.setText(position + 1 + "");
			holder.text_remark.setText(paihang.getRemark());
			holder.text_name.setText(paihang.getName());
			if (paihang.getJiangli().length() > 0) {
				holder.text_jiangli.setText("奖" + paihang.getJiangli());
			}else{
				holder.text_jiangli.setText("");
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView text_num;
		ImageView image_head;
		TextView text_name;
		TextView text_jiangli;
		TextView text_remark;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shou:
			setChooseSelector(1);
			break;
		case R.id.yao:
			setChooseSelector(2);
			break;
		case R.id.zhourenwu:
			setChooseSelector(3);
			break;
		case R.id.zhouyouxiao:
			setChooseSelector(4);
			break;
		}
	}
	
	private void setChooseSelector(int which) {
		switch (which) {
		case 1:
			shou.setBackgroundResource(R.drawable.detail_left_bg);
			shou.setTextColor(this.getResources().getColor(R.color.white));
			yao.setBackgroundDrawable(null);
			yao.setTextColor(this.getResources().getColor(R.color.black));
			zhourenwu.setBackgroundDrawable(null);
			zhourenwu.setTextColor(this.getResources().getColor(R.color.black));
			zhouyouxiao.setBackgroundDrawable(null);
			zhouyouxiao.setTextColor(this.getResources().getColor(R.color.black));
			mListView.setAdapter(shouruadapter);
			break;
		case 2:
			yao.setBackgroundResource(R.drawable.detail_ceter_bg);
			yao.setTextColor(this.getResources().getColor(R.color.white));
			shou.setBackgroundDrawable(null);
			shou.setTextColor(this.getResources().getColor(R.color.black));
			zhourenwu.setBackgroundDrawable(null);
			zhourenwu.setTextColor(this.getResources().getColor(R.color.black));
			zhouyouxiao.setBackgroundDrawable(null);
			zhouyouxiao.setTextColor(this.getResources().getColor(R.color.black));
			mListView.setAdapter(yaoqingadapter);
			break;
		case 3:
			zhourenwu.setBackgroundResource(R.drawable.detail_ceter_bg);
			zhourenwu.setTextColor(this.getResources().getColor(R.color.white));
			yao.setBackgroundDrawable(null);
			yao.setTextColor(this.getResources().getColor(R.color.black));
			shou.setBackgroundDrawable(null);
			shou.setTextColor(this.getResources().getColor(R.color.black));
			zhouyouxiao.setBackgroundDrawable(null);
			zhouyouxiao.setTextColor(this.getResources().getColor(R.color.black));
			mListView.setAdapter(zhourenwuadapter);
			break;
		case 4:
			zhouyouxiao.setBackgroundResource(R.drawable.detail_right_bg);
			zhouyouxiao.setTextColor(this.getResources().getColor(R.color.white));
			yao.setBackgroundDrawable(null);
			yao.setTextColor(this.getResources().getColor(R.color.black));
			shou.setBackgroundDrawable(null);
			shou.setTextColor(this.getResources().getColor(R.color.black));
			zhourenwu.setBackgroundDrawable(null);
			zhourenwu.setTextColor(this.getResources().getColor(R.color.black));
			mListView.setAdapter(yueyaoadapter);
			break;
		}
	}
	
	private void getpaihang() {
		Map params = new HashMap<String, String>();
		InterfaceTool.Networkrequest(activity, activity.queue, null, paihangurl, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				String code;
				try {
					code = arg0.getString("code");
					if (code.equals("1")) {
						JSONObject data = arg0.getJSONObject("data");
						String usernum = data.getString("usernum");
						String allmoney = data.getString("allmoney");
						message.setText("今天共有" + usernum + "位赚友，赚了" + allmoney + "元!");
						JSONArray shouyi = data.getJSONArray("shouyi");
						JSONArray yaoqing = data.getJSONArray("yaoqing");
						JSONArray monthshouyi = data.getJSONArray("weekshouyi");
						JSONArray monthyaoqing = data.getJSONArray("weekyaoqing");
						loaddata(shouyi, shouyilist);
						loaddata(yaoqing, yaoqinglist);
						loaddata(monthshouyi, zhourenwulist);
						loaddata(monthyaoqing, yueyaolist);
						setChooseSelector(1);
					} else {
						activity.Toastshow(arg0.getString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pullListView.onRefreshComplete();
			}
		}, params);
	}

	private void loaddata(JSONArray jsonArray, ArrayList<Paihang> list) {
		list.clear();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String name = jsonObject.getString("name");
				String headimgurl = jsonObject.getString("headimgurl");
				String jiangli = jsonObject.getString("jiangli");
				String money = jsonObject.getString("money");
				String remark = jsonObject.getString("remark");
				Paihang paihang = new Paihang(name, headimgurl, money, remark, jiangli);
				list.add(paihang);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
