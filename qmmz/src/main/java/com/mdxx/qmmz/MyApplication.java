package com.mdxx.qmmz;

import java.util.Iterator;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.mdxx.qmmz.utils.MyVolley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	// 处放时间
	public static Map<String, Long> map;
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		MyVolley.init(this);
		
		SharedPreferences pre = this.getSharedPreferences("userdata",
				MODE_PRIVATE);
		mUserDatas = (Map<String, Object>) pre.getAll();
	}
	
	/**
	 * 保存sharedpreferemce里的值
	 */
	private Map<String, Object> mUserDatas;


	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheExtraOptions(400, 400)
				.memoryCache(new WeakMemoryCache())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	

	// 根据name取得SharedPreferences里的用户数据，如果取不到则使用默认值defaultValue
	public String getUserData(String name, String defaultValue) {
		Object object = mUserDatas.get(name);
		if (object == null) {
			return defaultValue;
		}
		String value = object.toString();
		if (value.length() == 0) {
			return defaultValue;
		}
		return value;
	}

	// 更新SharedPreferences里的用户数据
	@SuppressWarnings("deprecation")
	public void updateUserData() {
		SharedPreferences.Editor editor = this.getSharedPreferences("userdata",
				Context.MODE_PRIVATE).edit();
		for (Iterator<Map.Entry<String, Object>> it = mUserDatas.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			String value = entry.getValue() == null ? "" : entry.getValue()
					.toString();
			editor.putString(entry.getKey(), value);
		}

		editor.commit();
	}

	// 判断是否登录了
	public boolean isSignin() {
		if (Integer.parseInt(getUserData("signin", "0")) == 1) {
			return true;
		}
		return false;
	}

	// 设置SharedPreferences里的用户数据
	public void setUserData(String name, float value) {
		mUserDatas.put(name, value);
	}

	public void setUserData(String name, String value) {
		mUserDatas.put(name, value);
	}

	public void setUserData(String name, int value) {
		mUserDatas.put(name, value);
	}

	public void setUserData(String name, long value) {
		mUserDatas.put(name, value);
	}

	public void setUserData(String name, boolean value) {
		mUserDatas.put(name, value);
	}

}
