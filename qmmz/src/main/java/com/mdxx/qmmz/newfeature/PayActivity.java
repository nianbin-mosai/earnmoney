package com.mdxx.qmmz.newfeature;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.common.LogUtils;

import java.net.URISyntaxException;

public class PayActivity extends BaseWebViewActivity {

    @Override
    protected String setMtitle() {
        return getString(R.string.pay);
    }

    @Override
    protected String setScheme() {
        return "alipays";
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
        LogUtils.i(url);
        webView.loadUrl(url);
    }
}
