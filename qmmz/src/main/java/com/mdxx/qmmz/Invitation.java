package com.mdxx.qmmz;

public class Invitation {
	private String userid;
	private String time;
	private String headimgurl;
	private String allmoney;
	private String level;
	private String qq;
	private String name;

	public Invitation(String userid, String time, String headimgurl,
			String allmoney, String level,String qq,String name) {
		this.userid = userid;
		this.time = time;
		this.headimgurl = headimgurl;
		this.allmoney = allmoney;
		this.level = level;
		this.qq = qq;
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}
	public String getQq() {
		return qq;
	}
	public String getname() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public String getAllmoney() {
		return allmoney;
	}

	public String getLevel() {
		return level;
	}

}
