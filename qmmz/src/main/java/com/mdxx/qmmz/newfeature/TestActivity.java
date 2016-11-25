package com.mdxx.qmmz.newfeature;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdxx.qmmz.common.LogUtils;

public class TestActivity extends BaseWebViewActivity {
    private String result;
    @Override
    protected String setMtitle() {
        return "测试页面";
    }

    @Override
    protected String setScheme() {
        return "alipays";
    }

    private String parseDatas(String result){
        return null;
    }
    protected void handleWebViewUrl() {
        result = getIntent().getStringExtra("result");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtils.i("shouldOverrideUrlLoading:"+url);
                return true;
            }
        });
        LogUtils.i(result);
        webView.loadDataWithBaseURL(null, result, "text/html", "utf-8",
                null);
    }
}
