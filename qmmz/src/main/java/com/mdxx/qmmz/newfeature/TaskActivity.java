package com.mdxx.qmmz.newfeature;

import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.common.LogUtils;

public class TaskActivity extends BaseWebViewActivity {
    private static final String indexUrl = "http://cs.yunxinwifi.cc/aspx/mobile/index.aspx";
    private boolean showIndex;
    @Override
    protected String setMtitle() {
        return getString(R.string.task);
    }

    @Override
    protected String setScheme() {
        return "alipays";
    }


    protected void handleWebViewUrl() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.i("shouldOverrideUrlLoading:"+url);
                if(TextUtils.equals(url,indexUrl)){
                    if (!showIndex) {
                        view.loadUrl(url);
                        showIndex=true;
                    }else{
                        finish();
                    }
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });
        LogUtils.i(url);
        webView.loadUrl(url);
    }
}
