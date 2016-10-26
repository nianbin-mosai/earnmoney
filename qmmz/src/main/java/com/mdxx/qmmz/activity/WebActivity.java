package com.mdxx.qmmz.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mdxx.qmmz.R;

public class WebActivity extends BaseActivity implements OnClickListener {

	private TextView text_title;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		initview();
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		webView.loadUrl(url);
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		text_title = (TextView) findViewById(R.id.text_title);
		webView = (WebView) findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();
		webView.setDrawingCacheEnabled(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setDownloadListener(new MyWebViewDownLoadListener()); 
		webView.setWebViewClient(new WebViewClient() {
			// 当点击链接时,希望覆盖而不是打开新窗口
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.startsWith("http") || url.startsWith("https")) {
					view.loadUrl(url);
					return true;
				} else {
					return false; // 返回true,代表事件已处理,事件流到此终止
				}
			}

			

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				text_title.setText(view.getTitle());
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				if (url.startsWith("http") || url.startsWith("https")) {
					return super.shouldInterceptRequest(view, url);
				} else {
					Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(in);
					finish();
					return null;
				}
			}
		});
	}
	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack(); // 后退
		} else {
			super.onBackPressed();
		}
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

	@Override
	protected void onDestroy() {
		webView.clearCache(true);
		webView.destroy();
		super.onDestroy();
	}

	
	class MyWebViewDownLoadListener implements DownloadListener{  
		  
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,  
                                    long contentLength) {             
//            Log.i("tag", "url="+url);             
//            Log.i("tag", "userAgent="+userAgent);  
//            Log.i("tag", "contentDisposition="+contentDisposition);           
//            Log.i("tag", "mimetype="+mimetype);  
//            Log.i("tag", "contentLength="+contentLength);  
            Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);             
        }  
    }  
}
