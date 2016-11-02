package com.mdxx.qmmz.network;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.mdxx.qmmz.MyApplication;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.common.FastJsonUtils;
import com.mdxx.qmmz.common.LogUtils;
import com.mdxx.qmmz.network.cache.CacheEntity;
import com.mdxx.qmmz.network.cache.DataCache;
import com.mdxx.qmmz.network.cache.OnGetCacheListener;
import com.mdxx.qmmz.network.progress.DefaultProgressIndicator;
import com.mdxx.qmmz.network.progress.IProgressIndicator;

import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;

public abstract class HttpResponseHandler extends TextHttpResponseHandler implements OnGetCacheListener {
    private Class<? extends HttpResponse> mClass;
    private IProgressIndicator progressIndicator;
    private Context context;
    private Toast toast;
    private String cacheKey;
    private static final long DEFAULT_CACHE_TIME = 600000L;
    protected boolean isShowToast;

    private HttpResponseHandler() {
        super();
    }

    public HttpResponseHandler(Context context) {
        this(context, HttpResponse.class);
    }

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass) {
        this(context, mClass, DefaultProgressIndicator.newInstance(context));
    }

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, IProgressIndicator progressIndicator) {
        this();
        this.context = context;
        this.mClass = mClass;
        this.progressIndicator = progressIndicator;
    }

	@Override
	public void onSuccess(final int statusCode, Header[] headers, final String responseString) {
		LogUtils.i("状态码：" + statusCode + " 返回值：" + responseString);
        parseData(statusCode, responseString, false);
    }

    protected void parseData(final int statusCode, final String responseString, final boolean isCache) {
        isShowToast = false;
        if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
            if (null == mClass) {
                handleResponseSuccess(statusCode, new HttpResponse(), responseString);
                if (!isCache && isOpenCache() && cacheKey != null) {
                    DataCache.getInstance().save(cacheKey, statusCode, responseString, cacheTime());
                }
            } else {
                Runnable parser = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (FastJsonUtils.isJson(responseString)) {
                                final HttpResponse response = FastJsonUtils.parseObject(responseString, mClass);
                                postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response != null && response.isSuccess()) {
                                            handleResponseSuccess(statusCode, response, responseString);
                                            if (!isCache && isOpenCache() && cacheKey != null) {
                                                DataCache.getInstance().save(cacheKey, statusCode, responseString, cacheTime());
                                            }
                                        } else {
                                            handleResponseFail(statusCode, response == null ? new HttpResponse(getString(R.string.parse_data_error)) : response, responseString);
                                        }
                                    }
                                });
                            } else {
                                postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleResponseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)), responseString);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            LogUtils.e("解析json数据异常", e);
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    handleResponseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)), responseString);
                                }
                            });
                        }
                    }
                };
                if (!getUseSynchronousMode() && !getUsePoolThread()) {
                    new Thread(parser).start();
                } else {
                    parser.run();
                }
            }
        } else {
            handleResponseFail(statusCode, new HttpResponse(getString(R.string.server_response_value_error, statusCode)), responseString);
        }
    }

    @Override
	public void onFailure(int statusCode, Header[] headers,
                          String responseString, Throwable throwable) {
        LogUtils.e("状态码：" + statusCode + " 返回值：" + responseString);
        HttpResponse httpResponse = null;
        isShowToast = false;
        if (TextUtils.isEmpty(responseString)) {
            isShowToast = true;
            responseString = getString(R.string.network_request_timeout);
        } else if (FastJsonUtils.isJson(responseString)){
            httpResponse = FastJsonUtils.parseObject(responseString, HttpResponse.class);
        }
        if (httpResponse == null) {
            httpResponse = new HttpResponse(responseString);
        }
        handleResponseFail(statusCode, httpResponse, responseString);
	}

    @Override
    public void onCancel() {
        super.onCancel();
        dismissProgress();
    }

    @Override
	public void onFinish() {
		super.onFinish();
		onResponesefinish();
	}

	@Override
	public void onStart() {
		super.onStart();
		onResponeseStart();
	}

    @Override
    public void onGetCacheSuccess(CacheEntity cacheEntity) {
        LogUtils.i("===========onGetCacheSuccess()");
        parseData(cacheEntity.statusCode, cacheEntity.data, true);
    }

    @Override
    public void onGetCacheFail(boolean hasCache, CacheEntity cacheEntity) {

    }

    protected final String getString(int resId, Object... formatArgs) {
		return MyApplication.INSTANCE.getString(resId, formatArgs);
	}

    public void showProgress() {
        if (progressIndicator != null) {
            progressIndicator.showProgress();
        }
    }

//    public void showProgress(String message) {
//        if (progressIndicator != null) {
//            progressIndicator.showProgress(message);
//        }
//    }

    public void dismissProgress() {
        if (progressIndicator != null) {
            progressIndicator.dismissProgress();
        }
    }

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
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void handleResponseSuccess(int statusCode, HttpResponse response, String responseString){
        onResponeseSucess(statusCode, response, responseString);
        onResponeseEnd();
    }

	/**
	 * 当请求成功statusCode为200时候调用
	 * @param statusCode 服务器响应值
	 * @param response 项目定义的响应值
	 * @param responseString 项目定义的返回结果
	 */
	public abstract void onResponeseSucess(int statusCode, HttpResponse response, String responseString);

    public void onResponeseEnd() {
    }

    /**
	 * 请求发起前调用
	 */
	public void onResponeseStart() {
        showProgress();
    }
	
	/**
	 * 请求完成时候调用 无论失败成功都会调用
	 */
	public void onResponesefinish() {
        dismissProgress();
    }

    public void onResponeseFail(int statusCode, HttpResponse response, String responseString){
        if (isShowToast) {
            showToast(response.message);
        }
    }

	/**
	 * 错误调用
	 * @param statusCode 服务器响应值
	 * @param response 项目定义的响应值
	 * @param responseString 项目定义的响应值
	 */
    public void handleResponseFail(int statusCode, HttpResponse response, String responseString) {
        onResponeseFail(statusCode, response, responseString);
        onResponeseEnd();
	}

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public boolean isOpenCache() {
        return false;
    }

    public long cacheTime() {
        return DEFAULT_CACHE_TIME;
    }
}
