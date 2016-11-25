package com.mdxx.qmmz.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.mdxx.qmmz.MyApplication;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.common.FastJsonUtils;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.network.progress.DefaultProgressIndicator;
import com.mdxx.qmmz.network.progress.IProgressIndicator;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月21日 15:21
 * 邮箱：nianbin@mosainet.com
 */
public abstract class OkhttpResponseHandler extends Callback<Response> {
    private Class<? extends HttpResponse> mClass;
    private IProgressIndicator progressIndicator;
    private Context context;
    private Toast toast;
    protected boolean isShowToast=true;
    private OkhttpResponseHandler() {
        super();
    }
    public OkhttpResponseHandler(Context context) {
        this(context, HttpResponse.class);
    }

    public OkhttpResponseHandler(Context context, Class<? extends HttpResponse> mClass) {
        this(context, mClass, DefaultProgressIndicator.newInstance(context));
    }

    public OkhttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, IProgressIndicator progressIndicator) {
        this.context = context;
        this.mClass = mClass;
        this.progressIndicator = progressIndicator;
    }
    public OkhttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, IProgressIndicator progressIndicator, boolean isShowToast) {
        this();
        this.context = context;
        this.mClass = mClass;
        this.progressIndicator = progressIndicator;
        this.isShowToast = isShowToast;
    }
    @Override
    public Response parseNetworkResponse(Response response, int id) throws Exception {
        return response;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
    }

    @Override
    public void onResponse(Response response, int id) {
        try {
            parseDatas(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        onResponeseStart();
        LogUtils.i(request.toString());
    }

    @Override
    public void onAfter(int id) {
        onResponesefinish();
    }

    @Override
    public void inProgress(float progress, long total, int id) {
        super.inProgress(progress, total, id);
    }
    public void onResponeseStart() {
        showProgress();
    }

    /**
     * 请求完成时候调用 无论失败成功都会调用
     */
    public void onResponesefinish() {
        dismissProgress();
    }
    public void showProgress() {
        if (progressIndicator != null) {
            progressIndicator.showProgress();
        }
    }
    public void dismissProgress() {
        if (progressIndicator != null) {
            progressIndicator.dismissProgress();
        }
    }
    private void handleResponseSuccess(int statusCode, HttpResponse response, String responseString){
        onResponeseSucess(statusCode, response, responseString);
    }
    public abstract void onResponeseSucess(int statusCode, HttpResponse response, String responseString);
    public void showToast(int resid) {
        showToast(getString(resid));
    }

    /**
     * 显示Toast
     */
    public void showToast(CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    protected final String getString(int resId, Object... formatArgs) {
        return MyApplication.INSTANCE.getString(resId, formatArgs);
    }
    private void parseDatas(Response response) throws IOException {
        if (response!=null) {
            Response result =  response;
            if(result!=null){
                final int statusCode = result.code();
                String responseString = result.body().string();
                if(responseString.startsWith("\n")){
                    responseString = responseString.replaceFirst("\n","");
                }
                if ((response).code() == HttpURLConnection.HTTP_OK){
                    if (null == mClass) {
                        handleResponseSuccess(statusCode, new HttpResponse(), responseString);
                    } else {
                        try {
                            if (FastJsonUtils.isJson(responseString)) {
                                HttpResponse mResponse = FastJsonUtils.parseObject(responseString, mClass);
                                if (mResponse != null && mResponse.isSuccess()) {
                                    handleResponseSuccess(statusCode, mResponse, responseString);
                                } else {
                                    handleResponseFail(statusCode, mResponse == null ? new HttpResponse(getString(R.string.parse_data_error)) : mResponse, responseString,true);
                                }
                            } else {
                                handleResponseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)), responseString,false);
                            }
                        } catch (Exception e) {
                            LogUtils.e("解析json数据异常", e);
                            final String finalResponseString = responseString;
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    handleResponseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)), finalResponseString,false);
                                }
                            });
                        }
                    }
                }else{
                    handleResponseFail(statusCode, new HttpResponse(getString(R.string.server_response_value_error, statusCode)), responseString,false);
                }
            }
        }
    }
    public void handleResponseFail(int statusCode, HttpResponse response, String responseString,boolean showMessage) {
        if (showMessage) {
            onResponeseFail(statusCode, response, responseString);
        }else{
            if (isShowToast) {
                showToast(response.message);
            }
        }

    }
    public void onResponeseFail(int statusCode, HttpResponse response, String responseString){
        try {
            if (response.isError()) {
                JSONObject result = new JSONObject(responseString);
                String errorMessage = result.optString("message");
                showToast(errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void postRunnable(Runnable runnable){
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
