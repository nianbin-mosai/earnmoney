package com.zhy.http.okhttp.intercoptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 16/3/1.
 */
public class CacheInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder().removeHeader("Pragma").header("Cache-Control",response.request().header("Cache-Control")).build();
    }
}
