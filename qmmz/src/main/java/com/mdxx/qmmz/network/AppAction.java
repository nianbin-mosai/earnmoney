package com.mdxx.qmmz.network;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.mdxx.qmmz.common.GlobalUtils;
import com.mdxx.qmmz.common.UserPF;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月03日 14:40
 * 邮箱：nianbin@mosainet.com
 */
public class AppAction {
    public static final String baseUrl = "http://wapi.angles1131.com/index.php/webapi/";
    private static String  getUrl(String typeUrl){
        return baseUrl + typeUrl;
    }
    public static void getVerifyCode(Context context,String mobile,String userid,HttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("phone",mobile);
        params.put("userid",userid);
        AsyncHttp.getInstance().execute(context,getUrl("User/get_code"),params,AsyncHttp.METHOD_POST,responseHandler);
    }
    public static void checkVerifyCode(Context context,String phone,String code,String userid,HttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("userid",userid);
        params.put("code",code);
        params.put("log_id", UserPF.getInstance().getLogId());
        AsyncHttp.getInstance().execute(context,getUrl("User/check_code"),params,AsyncHttp.METHOD_POST,responseHandler);
    }
    public static void login(Context context,String mobile,String password,HttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("mobile",mobile);
        params.put("password", password);
        AsyncHttp.getInstance().execute(context,getUrl("User/login"),params,AsyncHttp.METHOD_POST,responseHandler);
    }
    public static void register(Context context,String mobile,String password,HttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("mobile",mobile);
        params.put("password", password);
        params.put("imei", GlobalUtils.getIMEI(context));
        params.put("mac",GlobalUtils.getMacAddress(context));
        AsyncHttp.getInstance().execute(context,getUrl("User/register"),params,AsyncHttp.METHOD_POST,responseHandler);
    }
}
