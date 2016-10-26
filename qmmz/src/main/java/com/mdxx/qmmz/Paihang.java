package com.mdxx.qmmz;

public class Paihang {
	private String name;
	private String headimgurl;
	private String money;
	private String jiangli;
	private String remark;
	
	public Paihang(String name, String headimgurl, String money, String remark,String jiangli) {
		this.name = name;
		this.headimgurl = headimgurl;
		this.money = money;
		this.remark = remark;
		this.jiangli = jiangli;
	}
	public String getName() {
		return name;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public String getJiangli() {
		return jiangli;
	}
	
	public String getMoney() {
		return money;
	}
	public String getRemark() {
		return remark;
	}
	
}
