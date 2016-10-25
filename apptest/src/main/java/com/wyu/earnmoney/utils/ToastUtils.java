package com.wyu.earnmoney.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtils {
	private static Toast mToast;
	/**
	 * 弹出Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, CharSequence msg) {
		if (ToastUtils.mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mToast.setText(msg);
		}
		if(Looper.myLooper() == Looper.getMainLooper()){
			mToast.show();
		}else{
			Looper.prepare();
			mToast.show();
	        Looper.loop();
		}
		
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

	/**
	 * 消失Toast
	 */
	public static void cancelToast() { 
		if (ToastUtils.mToast != null) {
			if(Looper.myLooper() == Looper.getMainLooper()){
				mToast.cancel();
			}else{
				Looper.prepare();
				mToast.cancel();
		        Looper.loop();
			}
		}
    }
}
