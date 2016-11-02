package com.mdxx.qmmz;

public class Gonggao {

	String status;
	String time;
	String url;
	String title;
	public Gonggao(String status, String time, String url, String title) {
		super();
		this.status = status;
		this.time = time;
		this.url = url;
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public String getTime() {
		return time;
	}
	public String getUrl() {
		return url;
	}
	public String getTitle() {
		return title;
	}
	
}
