package com.mdxx.qmmz;

public class Notice {
	private String imagepath; 
	private String title; 
	private String time; 
	private String tourl;
	public Notice(String imagepath, String title, String time, String tourl) {
		super();
		this.imagepath = imagepath;
		this.title = title;
		this.time = time;
		this.tourl = tourl;
	}
	public String getImagepath() {
		return imagepath;
	}
	public String getTitle() {
		return title;
	}
	public String getTime() {
		return time;
	}
	public String getTourl() {
		return tourl;
	} 
	
}
