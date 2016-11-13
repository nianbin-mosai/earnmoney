package com.mdxx.qmmz.newfeature;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.activity.BaseActivity;
import com.mdxx.qmmz.common.Configs;

import java.net.URISyntaxException;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月07日 19:40
 * 邮箱：nianbin@mosainet.com
 */
public abstract class BaseWebViewActivity extends BaseActivity {
    private ProgressBar progressBar;
    protected WebView webView;
    private TextView textView;
    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_base_webview);
        initView();
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.tv_title);
        textView.setText(setMtitle());
        progressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        webView = (WebView) findViewById(R.id.myWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
        });
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        findViewById(R.id.rl_header).setVisibility(showHeader() ? View.VISIBLE : View.GONE);
        handleWebViewUrl();

    }

    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void clickBack() {
        if (findViewById(R.id.rl_back) != null) {
            findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        clickBack();
    }

    protected String setMtitle() {
        return getString(R.string.title);
    }

    protected void handleWebViewUrl() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getScheme().equals(setScheme())) {
                    try {
                        Intent intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        ResolveInfo ri = getPackageManager().resolveActivity(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        if (ri != null) {// 如果安装了支付宝
                            startActivity(intent);
                            finish();
                        } else {// 否则，没有安装支付宝
                            showToast(getString(R.string.tip_alipy_uninstall));
                            finish();
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl(Configs.alipayUrl);
    }

    protected boolean showHeader() {
        return false;
    }

    protected String setScheme() {
        return null;
    }
}
