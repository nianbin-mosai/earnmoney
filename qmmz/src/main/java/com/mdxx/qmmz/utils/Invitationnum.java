package com.mdxx.qmmz.utils;

import java.io.File;

import com.mdxx.qmmz.Configure;

import android.app.ProgressDialog;
import android.os.Environment;

public class Invitationnum {

	private String pathlist[] = { "/QQBrowser", "/Download", "/UCDownloads",
			 "/360Browser", "/kbrowser_fast" };
	private String Yaoqingma = "";
	private ProgressDialog mbar;

	public Invitationnum(ProgressDialog bar) {
		mbar = bar;
	}

	public String getfile() {
		mbar.show();
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String sdDir = "";
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().getPath()
					.toString();
		} else {
			return "";
		}
		for (int i = 0; i < pathlist.length; i++) {
			File file = new File(sdDir + pathlist[i]);
			boolean search = search(file);
			if (search) {
				mbar.dismiss();
				return Yaoqingma;
			}
		}
		mbar.dismiss();
		return Yaoqingma;
	}

	private boolean search(File file) {
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (int j = 0; j < files.length; j++) {
					if (!files[j].isDirectory()) {
						String filename = files[j].getName();
						if (filename.startsWith(Configure.Filename)
								&& filename.endsWith(".apk")) {
							String yqing = filename.substring(14,
									filename.length() - 4);
							if (yqing.length() > 4) {
								if(yqing.contains("_t")){
									int indexOf = yqing.indexOf("_t");
									yqing = yqing.substring(0, indexOf);
								}
								Yaoqingma = yqing;
								return true;
							}
						}
					} else {
						search(files[j]);
					}
				}
				if (Yaoqingma.length() > 4) {
					return true;
				}
			}
		}
		return false;
	}

}