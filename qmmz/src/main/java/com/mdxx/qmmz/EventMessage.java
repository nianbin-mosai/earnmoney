package com.mdxx.qmmz;

public class EventMessage {
	String info;
	int i;
	String str;
	public EventMessage(String info, int i, String str) {
		this.info = info;
		this.i = i;
		this.str = str;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
}
