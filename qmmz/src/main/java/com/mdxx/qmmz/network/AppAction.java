package com.mdxx.qmmz.network;

import android.content.Context;

import com.mdxx.qmmz.common.GlobalUtils;
import com.mdxx.qmmz.common.UserPF;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月03日 14:40
 * 邮箱：nianbin@mosainet.com
 */
public class AppAction {
    public  static String baseUrl = "http://wapi.angles1131.com/index.php/webapi/";
    private static String  getUrl(String typeUrl){
        return baseUrl + typeUrl;
    }
    public static void getVerifyCode(Context context,String mobile,String userid,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                   .url(getUrl("User/get_code"))
                    .tag(context)
                   .addParams("phone",mobile)
                   .addParams("userid",userid)
                   .build().execute(responseHandler);

    }
    public static void checkVerifyCode(Context context,String phone,String code,String userid,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                .url(getUrl("User/check_code"))
                .tag(context)
                .addParams("phone",phone)
                .addParams("userid",userid)
                .addParams("code",code)
                .addParams("log_id", UserPF.getInstance().getLogId())
                .build().execute(responseHandler);
    }
    public static void login(Context context,String mobile,String password,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                .url(getUrl("User/login"))
                .tag(context)
                .addParams("mobile",mobile)
                .addParams("password", password)
                .addParams("imei", GlobalUtils.getIMEI(context))
                .addParams("mac",GlobalUtils.getMacAddress(context))
                .build().execute(responseHandler);

    }
    public static void register(Context context,String mobile,String password,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                .url(getUrl("User/register"))
                .tag(context)
                .addParams("mobile",mobile)
                .addParams("password", password)
                .addParams("imei", GlobalUtils.getIMEI(context))
                .addParams("mac",GlobalUtils.getMacAddress(context))
                .build().execute(responseHandler);
    }
    public static void getWebViewConfigs(Context context,OKHttpResponseHandler responseHandler){
        OkHttpUtils.get()
                .url(getUrl("Tool/webview"))
                .tag(context)
                .addParams("token", UserPF.getInstance().getToken())
                .addParams("userid",UserPF.getInstance().getUserid())
                .build().execute(responseHandler);

    }
    public static void addPoint(Context context,int type,int point,String result,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                .url(getUrl("order/add_points"))
                .tag(context)
                .addParams("token", UserPF.getInstance().getToken())
                .addParams("userid",UserPF.getInstance().getUserid())
                .addParams("points",point+"")
                .addParams("type",type+"")
                .addParams("result",result)
                .build().execute(responseHandler);
    }
    public static void getDomain(Context context,OKHttpResponseHandler responseHandler){
        OkHttpUtils.get()
                .url("http://wapi.yunxinwifi.cc/index.php/webapi/config/config")
                .tag(context)
                .build().execute(responseHandler);
    }
    public static void forgetPassword(Context context,String mobile,String password,String code,String log_id,OKHttpResponseHandler responseHandler){
        OkHttpUtils.post()
                .url(getUrl("User/password_find"))
                .tag(context)
                .addParams("mobile",mobile)
                .addParams("password", password)
                .addParams("code",code)
                .addParams("log_id",log_id)
                .build().execute(responseHandler);
    }
}
