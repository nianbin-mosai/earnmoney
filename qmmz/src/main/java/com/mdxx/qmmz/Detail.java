package com.mdxx.qmmz;

public class Detail {
	private String remark;
	private String time;
	private String money;
	private String headimgurl;
	
	public Detail(String remark, String time, String money,String headimgurl) {
		this.remark = remark;
		this.time = time;
		this.money = money;
		this.headimgurl = headimgurl;
	}
	public String getRemark() {
		return remark;
	}
	public String getTime() {
		return time;
	}
	public String getMoney() {
		return money;
	}
	public String getheadimgurl() {
		return headimgurl;
	}
	
}
