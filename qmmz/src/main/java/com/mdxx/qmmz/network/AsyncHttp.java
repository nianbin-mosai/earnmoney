package com.mdxx.qmmz.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.common.UserPF;

import java.security.KeyStore;
import java.util.Map;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;

/**
 * HTTP请求
 *
 * @author Rays 2016年4月13日
 *
 */
public class AsyncHttp {
    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;
    public static final int METHOD_PUT = 2;
    public static final int METHOD_DELETE = 3;
    private static final AsyncHttp INSTANCE = new AsyncHttp();
    private AsyncHttpClient client = new AsyncHttpClient();

    private AsyncHttp() {

    }

    public static AsyncHttp getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        client.setTimeout(15000); // 设置链接超时，如果不设置，默认为30s
        client.setCookieStore(new PersistentCookieStore(context.getApplicationContext()));
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(socketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    protected void execute(Context context, String url, int method,
                           AsyncHttpResponseHandler responseHandler,boolean withToken){
        execute(context, url, null, null, method, responseHandler,withToken);
    }

    protected void execute(Context context, String url, Map<String, String> headerMap, int method,
                           AsyncHttpResponseHandler responseHandler,boolean withToken){
        execute(context, url, null, headerMap, method, responseHandler,withToken);
    }

    protected void execute(Context context, String url, RequestParams params, int method,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, params, null, method, responseHandler,false);
    }

    protected void execute(Context context, String url, RequestParams params, Map<String, String> headerMap, int method,
                           AsyncHttpResponseHandler responseHandler,boolean withToken){
        if(params!=null && withToken){
            params.put("token", UserPF.getInstance().getToken());
        }
        LogUtils.i(url + (params == null ? "" : ("?" + params.toString())));
        client.removeAllHeaders();
        if (headerMap != null) {
            for (String header : headerMap.keySet()) {
                client.addHeader(header, headerMap.get(header));
            }
        }
        switch (method) {
            case METHOD_POST:
                client.post(context, url, params, responseHandler);
                break;
            case METHOD_GET:
                client.get(context, url, params, responseHandler);
                break;
            case METHOD_DELETE:
                client.delete(url, params, responseHandler);
                break;
            default:
                client.put(context, url, params, responseHandler);
                break;
        }
    }

    public void clearCookieStore() {
        CookieStore cookieStore = (CookieStore) client.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }

}
