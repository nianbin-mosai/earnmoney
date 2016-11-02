package com.mdxx.qmmz.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * ScreenUtils
 * 
 */
public class ScreenUtils {

	//dp到px
	public static float dpToPx(Context context, float dp) {
		if (context == null) {
			return -1;
		}
		return dp * context.getResources().getDisplayMetrics().density;
	}

	//px到dp
	public static float pxToDp(Context context, float px) {
		if (context == null) {
			return -1;
		}
		return px / context.getResources().getDisplayMetrics().density;
	}

	public static int dpToPxInt(Context context, float dp) {
		return (int) (dpToPx(context, dp) + 0.5f);
	}

	public static float pxToDpCeilInt(Context context, float px) {
		return (int) (pxToDp(context, px) + 0.5f);
	}

	//得到屏幕信息
	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager mWm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		mWm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	//得到屏幕�?	
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.heightPixels;
	}
    
	//得到屏幕�?	
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.widthPixels;
	}
	
	// 屏幕密度�?.75 / 1.0 / 1.5�?	
	public static float getDensity(Context context){
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.density;
	} 
	
	// 屏幕密度DPI�?20 / 160 / 240�?	
	public static float getDensityDpi(Context context){
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.densityDpi;
	}
	
	public static void toggleFullScreen(Activity activity, boolean isFull) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
}
