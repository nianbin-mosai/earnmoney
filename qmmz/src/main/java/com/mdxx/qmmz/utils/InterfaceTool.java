package com.mdxx.qmmz.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mdxx.qmmz.Configure;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

public class InterfaceTool {

	public static final String ULR = "http://qmmz.maidianxm.cn/index.php/";
	private static final String PROTOCOL_CHARSET = "utf-8";

	// 判断网络连接
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				return activeNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static void Networkrequest(final Context context,
			RequestQueue queue, final ProgressDialog progess, String httpurl,
			Response.Listener success, Map params) {
		if (InterfaceTool.isNetworkAvailable(context)) {
			if (progess != null) {
				progess.show();
			}
			SharedPreferences sp = context.getSharedPreferences(Configure.Project, 0);
			String token = sp.getString("token", "");
			if(!(InterfaceTool.ULR + "user/login").equals(httpurl)){
				params.put("token", token);
			}
			final String mRequestBody = appendParameter(httpurl, params);
			JsonObjectRequest request = new JsonObjectRequest(Method.POST,
					httpurl, null, success, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							Toast.makeText(context, "连接服务器错误"+arg0.toString(),
									Toast.LENGTH_SHORT).show();
							if (progess != null) {
								progess.dismiss();
							}
						}
					}) {
				@Override
				public String getBodyContentType() {
					return "application/x-www-form-urlencoded; charset="
							+ getParamsEncoding();
				}

				@Override
				public byte[] getBody() {
					try {
						return mRequestBody == null ? null : mRequestBody
								.getBytes(PROTOCOL_CHARSET);
					} catch (UnsupportedEncodingException uee) {
						VolleyLog
								.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
										mRequestBody, PROTOCOL_CHARSET);
						return null;
					}
				}
			};

			if (progess != null) {
				progess.show();
			}
			request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
			queue.add(request);
		} else {
			Toast.makeText(context, "未连接网络", Toast.LENGTH_SHORT).show();
		}
	}

	private static String appendParameter(String url, Map<String, String> params) {
		Uri uri = Uri.parse(url);
		Uri.Builder builder = uri.buildUpon();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.appendQueryParameter(entry.getKey(), entry.getValue());
		}
		return builder.build().getQuery();
	}
	
	/**
	 * 获取随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return "&"+sb.toString();     
	 }     
}
