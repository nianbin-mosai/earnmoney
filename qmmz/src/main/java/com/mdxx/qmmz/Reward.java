package com.mdxx.qmmz;

public class Reward {
	private String remark;
	private String time;
	private String money;
	public String getRemark() {
		return remark;
	}
	public String getTime() {
		return time;
	}
	public String getMoney() {
		return money;
	}

	public Reward(String remark, String time, String money) {
		this.remark = remark;
		this.time = time;
		this.money = money;
	}
	
}
