package com.ldm.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ldm.test.utils.WifiUtils;

import java.util.List;

/**
 * Search WIFI and show in ListView
 * 
 */
public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private Button search_btn;
	private ListView wifi_lv;
	private WifiUtils mUtils;
	private List<String> result;
	private ProgressDialog progressdlg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUtils = new WifiUtils(this);
		findViews();
		setLiteners();
	}

	private void findViews() {
		this.search_btn = (Button) findViewById(R.id.search_btn);
		this.wifi_lv = (ListView) findViewById(R.id.wifi_lv);
	}

	private void setLiteners() {
		search_btn.setOnClickListener(this);
		wifi_lv.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.search_btn) {
			showDialog();
			new MyAsyncTask().execute();
		}
	}

	/**
	 * init dialog and show
	 */
	private void showDialog() {
		progressdlg = new ProgressDialog(this);
		progressdlg.setCanceledOnTouchOutside(false);
		progressdlg.setCancelable(false);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(getString(R.string.wait_moment));
		progressdlg.show();
	}

	/**
	 * dismiss dialog
	 */
	private void progressDismiss() {
		if (progressdlg != null) {
			progressdlg.dismiss();
		}
	}

	class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			//扫描附近WIFI信息
			result = mUtils.getScanWifiResult();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDismiss();
			initListViewData();
		}
	}

	private void initListViewData() {
		if (null != result && result.size() > 0) {
			wifi_lv.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(), R.layout.wifi_list_item,
					R.id.ssid, result));
		} else {
			View view  = View.inflate(this,R.layout.list_empty,null);
			wifi_lv.setEmptyView(view.findViewById(R.id.ssid));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView tv = (TextView) arg1.findViewById(R.id.ssid);
		if (!TextUtils.isEmpty(tv.getText().toString())) {
			Intent in = new Intent(MainActivity.this, WifiConnectActivity.class);
			in.putExtra("ssid", tv.getText().toString());
			startActivity(in);
		}
	}
}
