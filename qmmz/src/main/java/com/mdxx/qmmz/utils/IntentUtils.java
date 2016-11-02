package com.mdxx.qmmz.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/***
 * intent 工具�?
 * @author leiweicheng
 *
 */
public class IntentUtils {
	
	
	/***
	 * 根据class 打开某个activity
	 * @param mContext
	 * @param clz
	 */
	public static void toActivity(Context mContext, Class clz){
		Intent i = new Intent(mContext, clz);
		mContext.startActivity(i);
	}
	
	
	public static void toActivityWithIntent(Context mContext, Intent intent){
		mContext.startActivity(intent);
	}

	public static void toActivityWithBundle(Context mContext, Class clz, Bundle bundle){
		Intent i = new Intent(mContext, clz);
		i.putExtras(bundle);
		mContext.startActivity(i);
	}
	
}
