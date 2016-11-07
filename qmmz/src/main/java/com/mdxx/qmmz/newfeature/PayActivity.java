package com.mdxx.qmmz.newfeature;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdxx.qmmz.R;

import java.net.URISyntaxException;

public class PayActivity extends BaseWebViewActivity {
    protected void handleWebViewUrl(){
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getScheme().equals("alipays")) {
                    try {
                        Intent intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                            finish();
                        } else {
                            // 没有安装支付宝
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
        webView.loadUrl(url);
    }

    @Override
    protected String setMtitle() {
        return getString(R.string.pay);
    }
}
