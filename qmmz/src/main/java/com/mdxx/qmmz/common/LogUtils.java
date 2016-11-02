package com.mdxx.qmmz.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mdxx.qmmz.BuildConfig;
import com.mdxx.qmmz.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class LogUtils {
	private static final String TAG = "LogUtils";
	private static final boolean IS_DEBUG = BuildConfig.DEBUG;

	public static void v(String msg) {
		if (IS_DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (IS_DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (IS_DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (IS_DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (IS_DEBUG) {
			Log.e(TAG, msg);
		}
	}
	
	public static void e(String msg, Throwable tr) {
		if (IS_DEBUG) {
			Log.e(TAG, msg, tr);
		}
	}

	/**
	 * 打印当前调用堆栈
	 *
	 * @param msg
	 */
	public static void printCurrentStacktrace(String msg){
        LogUtils.e(TAG, new Exception(msg));
	}


    public static void writeLog(Context mContext, String logName, String content) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "mosai" + File.separator;
                File dir = new File(path);
                if (!dir.exists() && !dir.mkdirs()) {
                    return;
                }
                long timestamp = System.currentTimeMillis();
                String time = DateTimeUtil.getCurrDateTimeStr().replace(":", "_");
                String fileName = String.format(Locale.getDefault(), "%s-%s-%s-%d.log", mContext.getResources().getString(R.string.app_name), logName, time, timestamp);
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(content.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
